package com.foxconn.fii.receiver.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.b04.model.B04TestErrorMeta;
import com.foxconn.fii.data.b04.repository.B04TestErrorMetaRepository;
//import com.foxconn.fii.data.b06ds02.repository.B06TestSolutionMetaRepository;
import com.foxconn.fii.data.b04ds02.repository.B04DS02ErrorLogRepository;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import com.foxconn.fii.data.primary.model.entity.TestMoTypeConfig;
import com.foxconn.fii.data.primary.model.entity.TestNoteError;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import com.foxconn.fii.data.primary.repository.TestErrorDailyRepository;
import com.foxconn.fii.data.primary.repository.TestErrorMetaRepository;
import com.foxconn.fii.data.primary.repository.TestErrorRepository;
import com.foxconn.fii.data.primary.repository.TestMoTypeConfigRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestNoteErrorRepository;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestErrorRepository;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestErrorServiceImpl implements TestErrorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestErrorRepository testErrorRepository;

    @Autowired
    private TestErrorMetaRepository testErrorMetaRepository;

    @Autowired
    private B04TestErrorMetaRepository b04TestErrorMetaRepository;

//    @Autowired
//    private B06TestSolutionMetaRepository b06TestSolutionMetaRepository;

    @Autowired
    private TestNoteErrorRepository testNoteErrorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestSolutionMetaRepository testSolutionMetaRepository;

    @Autowired
    private TestErrorDailyRepository testErrorDailyRepository;

    @Autowired
    private B04DS02ErrorLogRepository b04DS02ErrorLogRepository;

    @Autowired
    private SfcTestErrorRepository sfcTestErrorRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestMoTypeConfigRepository testMoTypeConfigRepository;

    @Value("${path.data}")
    private String dataPath;

    @Override
    public int[] saveAll(List<TestError> errorList) {
        if (errorList.isEmpty()) {
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_error as target " +
                        "using(select factory=?, model_name=?, group_name=?, station_name=?, start_date=?, end_date=?, error_code=?, test_fail=?, fail=?, dppm=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.station_name=source.station_name and target.start_date=source.start_date and target.end_date=source.end_date and target.error_code=source.error_code " +
                        "when matched then " +
                        "   update set " +
                        "   target.test_fail=source.test_fail, " +
                        "   target.fail=source.fail, " +
                        "   target.dppm=source.dppm " +
                        "when not matched then " +
                        "   insert (factory, model_name, group_name, station_name, start_date, end_date, error_code, test_fail, fail, dppm) " +
                        "   values(source.factory, source.model_name, source.group_name, source.station_name, source.start_date, source.end_date, source.error_code, source.test_fail, source.fail, source.dppm);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestError error = errorList.get(i);
                        preparedStatement.setString(1, error.getFactory());
                        preparedStatement.setString(2, error.getModelName());
                        preparedStatement.setString(3, error.getGroupName());
                        preparedStatement.setString(4, error.getStationName());
                        preparedStatement.setTimestamp(5, new Timestamp(error.getStartDate().getTime()));
                        preparedStatement.setTimestamp(6, new Timestamp(error.getEndDate().getTime()));
                        preparedStatement.setString(7, error.getErrorCode());
                        preparedStatement.setInt(8, error.getTestFail());
                        preparedStatement.setInt(9, error.getFail());
                        preparedStatement.setInt(10, error.getDppm());
                    }

                    @Override
                    public int getBatchSize() {
                        return errorList.size();
                    }
                });
    }

    @Override
    public List<TestError> getErrorByCodeList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        Map<String, TestError> errorByCodeMap = new HashMap<>();

        List<TestError> errorList;
        if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
            errorList = testErrorRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
        } else if (StringUtils.isEmpty(stationName)) {
            errorList = testErrorRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
        } else {
            errorList = testErrorRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
        }

        for (TestError tmp : errorList) {
            TestError error = errorByCodeMap.get(tmp.getErrorCode());
            if (error == null) {
                error = new TestError();
                error.setFactory(tmp.getFactory());
                error.setModelName(tmp.getModelName());
                error.setGroupName(tmp.getGroupName());
                error.setStationName(tmp.getStationName());
                error.setErrorCode(tmp.getErrorCode());
                error.setStartDate(startDate);
                error.setEndDate(endDate);
            }

            error.setTestFail(error.getTestFail() + tmp.getTestFail());
            error.setFail(error.getFail() + tmp.getFail());

            errorByCodeMap.put(tmp.getErrorCode(), error);
        }

        return errorByCodeMap.values().stream().sorted(Comparator.comparing(TestError::getTestFail, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<TestErrorDaily> getErrorDailyByCodeList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, String mode) {
        Map<String, TestErrorDaily> errorByCodeMap = new HashMap<>();

        List<TestErrorDaily> errorList;
        if (Factory.B04.equalsIgnoreCase(factory) && "TE".equalsIgnoreCase(mode)) {
            if (StringUtils.isEmpty(stationName)) {
                errorList = b04DS02ErrorLogRepository.getErrorList(modelName, groupName, startDate, endDate);
            } else {
                errorList = b04DS02ErrorLogRepository.getErrorList(modelName, groupName, stationName, startDate, endDate);
            }
        } else if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            if (testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI").contains(modelName)) {
                customer = "UI";
            }
            if (StringUtils.isEmpty(stationName)) {
                errorList = sfcTestErrorRepository.findByModelAndGroup(factory, customer, modelName, groupName, startDate, endDate);
            } else {
                errorList = sfcTestErrorRepository.findByModelAndGroupAndStation(factory, customer, modelName, groupName, stationName, startDate, endDate);
            }
        } else {
            if (StringUtils.isEmpty(modelName)) {
                errorList = testErrorDailyRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate);
            } else if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
            } else if (StringUtils.isEmpty(stationName)) {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
            } else {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
            }
        }

        for (TestErrorDaily tmp : errorList) {
            TestErrorDaily error = errorByCodeMap.get(tmp.getErrorCode() + "_" + tmp.getErrorDescription());
            if (error == null) {
                error = new TestErrorDaily();
                error.setFactory(tmp.getFactory());
                error.setModelName(tmp.getModelName());
                error.setGroupName(tmp.getGroupName());
                if (!StringUtils.isEmpty(groupName) && !StringUtils.isEmpty(stationName)) {
                    error.setStationName(tmp.getStationName());
                }
                error.setErrorCode(tmp.getErrorCode());
                error.setErrorDescription(tmp.getErrorDescription());
                error.setStartDate(startDate);
                error.setEndDate(endDate);
            }

            error.setTestFail(error.getTestFail() + tmp.getTestFail());
            error.setFail(error.getFail() + tmp.getFail());

            errorByCodeMap.put(tmp.getErrorCode() + "_" + tmp.getErrorDescription(), error);
        }

        if ("PE".equalsIgnoreCase(mode)) {
            return errorByCodeMap.values().stream()
                    .filter(error -> error.getFail() > 0)
                    .sorted(Comparator.comparing(TestErrorDaily::getFail, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            return errorByCodeMap.values().stream()
                    .sorted(Comparator.comparing(TestErrorDaily::getTestFail, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Map<String, TestError> getErrorByTimeMap(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate, ShiftType shiftType) {
        Map<String, TestError> stationByTimeMap = TestUtils.getHourlyMap(startDate, endDate);

        List<TestError> errorList;
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            if (testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI").contains(modelName)) {
                customer = "UI";
            }
            errorList = sfcTestErrorRepository.findHourlyByModelAndGroupAndStationAndErrorCode(factory, customer, modelName, groupName, stationName, errorCode, startDate, endDate);
        } else {
            if (StringUtils.isEmpty(stationName)) {
                errorList = testErrorRepository.findAllByFactoryAndModelNameAndGroupNameAndErrorCodeAndStartDateBetween(factory, modelName, groupName, errorCode, startDate, endDate);
            } else {
                errorList = testErrorRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(factory, modelName, groupName, stationName, errorCode, startDate, endDate);
            }
        }

        for (TestError tmp : errorList) {
            TestError error = stationByTimeMap.get(tmp.getShiftTime());
            if (error == null) {
                error = new TestError();
                error.setFactory(tmp.getFactory());
                error.setModelName(tmp.getModelName());
                error.setGroupName(tmp.getGroupName());
                error.setErrorCode(tmp.getErrorCode());
                error.setStartDate(startDate);
                error.setEndDate(endDate);
                stationByTimeMap.put(tmp.getShiftTime(), error);
            }

            error.setTestFail(error.getTestFail() + tmp.getTestFail());
            error.setFail(error.getFail() + tmp.getFail());
            error.setDppm(error.getDppm() + tmp.getDppm());
        }

        for (Map.Entry<String, TestError> entry : stationByTimeMap.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(new TestError());
//                stationByTimeMap.put(entry.getKey(), new TestError());
            }
        }

        return stationByTimeMap;
    }

    @Override
    public Map<String, TestErrorDaily> getErrorDailyByDayMap(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate, MoType moType) {
        List<TestErrorDaily> errorList;
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            if (testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI").contains(modelName)) {
                customer = "UI";
            }
            errorList = sfcTestErrorRepository.findByModelAndGroupAndStationAndErrorCode(factory, customer, modelName, groupName, stationName, errorCode, startDate, endDate);
        } else {
            if (StringUtils.isEmpty(stationName)) {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndErrorCodeAndStartDateBetween(factory, modelName, groupName, errorCode, startDate, endDate);
            } else {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(factory, modelName, groupName, stationName, errorCode, startDate, endDate);
            }
        }

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, TestErrorDaily> result = errorList.stream()
                .filter(error -> moTypeConfig.contain(error.getMo()))
                .collect(Collectors.toMap(
                error -> TimeSpan.format(TimeSpan.of(error.getStartDate(), error.getEndDate()), TimeSpan.Type.DAILY),
                error -> error,
                (error, tmp) -> {
                    error.setTestFail(error.getTestFail() + tmp.getTestFail());
                    error.setFail(error.getFail() + tmp.getFail());
                    return error;
                }, HashMap::new));
        return result;
    }

    @Override
    public Map<String, Map<String, TestErrorDaily>> getErrorDailyByDayMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType) {
        Map<String, Map<String, TestErrorDaily>> result = new HashMap<>();

        List<TestErrorDaily> errorList;
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            if (testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI").contains(modelName)) {
                customer = "UI";
            }
            errorList = sfcTestErrorRepository.findByModelAndGroupAndStationAndErrorCode(factory, customer, modelName, groupName, stationName, null, startDate, endDate);
        } else {
            if (StringUtils.isEmpty(stationName)) {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
            } else {
                errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
            }
        }

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, List<TestErrorDaily>> errorByCodeMap = errorList.stream()
                .filter(error -> moTypeConfig.contain(error.getMo()))
                .collect(Collectors.groupingBy(error -> TimeSpan.format(TimeSpan.of(error.getStartDate(), error.getEndDate()), TimeSpan.Type.DAILY)));

        for (Map.Entry<String, List<TestErrorDaily>> entry : errorByCodeMap.entrySet()) {
            Map<String, TestErrorDaily> errorByDayMap = new HashMap<>();
            for (TestErrorDaily tmp : entry.getValue()) {
                TestErrorDaily error = errorByDayMap.get(tmp.getErrorCode());
                if (error == null) {
                    error = new TestErrorDaily();
                    error.setFactory(tmp.getFactory());
                    error.setModelName(tmp.getModelName());
                    error.setGroupName(tmp.getGroupName());
                    error.setStationName(tmp.getStationName());
                    error.setErrorCode(tmp.getErrorCode());
                }

                error.setTestFail(error.getTestFail() + tmp.getTestFail());
                error.setFail(error.getFail() + tmp.getFail());

                errorByDayMap.put(tmp.getErrorCode(), error);
            }
            result.put(entry.getKey(), errorByDayMap);
        }

        return result;
    }

    @Override
    public Map<String, String> getErrorMetaMap(String factory, String modelName) {
        Map<String, String> result;
//        if ("B04".equalsIgnoreCase(factory)) {
//            result = b04TestErrorMetaRepository.findAllByModelName(modelName)
//                    .stream().collect(Collectors.toMap(B04TestErrorMeta::getErrorCode, B04TestErrorMeta::getDescription, (e1, e2) -> e1));
//
//            result.put("__NTC__", "NO TEST CODE");
//            return result;
//        }
        result = testErrorMetaRepository.findAllByFactory(factory)
                .stream().collect(Collectors.toMap(TestErrorMeta::getErrorCode, TestErrorMeta::getDescription,  (e1, e2) -> e1));

        result.put("__NTC__", "NO TEST CODE");
        return result;
    }

    @Override
    public List<TestErrorMeta> getErrorMetaList(String factory, String modelName) {
        if (!StringUtils.isEmpty(modelName)) {
            return testErrorMetaRepository.findAllByFactoryAndModelName(factory, modelName);
        }
        return testErrorMetaRepository.findAllByFactory(factory);
    }

    @Override
    public List<String> getErrorCodeList(String factory, String modelName) {
        if ("B04".equalsIgnoreCase(factory)) {
            return b04TestErrorMetaRepository.findAllByModelName(modelName).stream().map(B04TestErrorMeta::getErrorCode).collect(Collectors.toList());
        }
//        else if ("B06".equalsIgnoreCase(factory)) {
//            List<String> errorCodeList = b06TestSolutionMetaRepository.getErrorCodeList(modelName);
//            if (errorCodeList.isEmpty() && modelName.toUpperCase().startsWith("U10")) {
//                errorCodeList = b06TestSolutionMetaRepository.getErrorCodeList("UBEE");
//            }
//            return errorCodeList;
//        }
        return testErrorMetaRepository.findAllByFactoryAndModelName(factory, modelName).stream().map(TestErrorMeta::getErrorCode).collect(Collectors.toList());
    }

    @Override
    public TestError getError(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate) {
        Optional<TestError> optionalError = testErrorRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(factory, modelName, groupName, stationName, errorCode, startDate, endDate)
                .stream().reduce((error, tmp) -> {
                    error.setTestFail(error.getTestFail() + tmp.getTestFail());
                    error.setFail(error.getFail() + tmp.getFail());
                    return error;
                });

        return optionalError.orElse(null);
    }

    @Override
    public List<TestErrorDaily> getErrorDailyList(String factory, String modelName, String errorCode, Date startDate, Date endDate, MoType moType) {
        List<TestErrorDaily> errorDailyList;
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (modelList.contains(modelName)) {
                errorDailyList =  sfcTestErrorRepository.findByModelAndErrorCode(factory, "UI", modelName, errorCode, startDate, endDate);
            } else {
                errorDailyList = sfcTestErrorRepository.findByModelAndErrorCode(factory, "", modelName, errorCode, startDate, endDate);
            }
        } else {
            errorDailyList = testErrorDailyRepository.findAllByFactoryAndModelNameAndErrorCodeAndStartDateBetween(factory, modelName, errorCode, startDate, endDate);
        }

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, TestErrorDaily> errorMap = errorDailyList
                .stream()
                .filter(error -> moTypeConfig.contain(error.getMo()))
                .collect(Collectors.toMap(
                        error -> error.getGroupName() + '_' + error.getStationName(),
                        error -> error,
                        (e1, e2) -> {
                            e1.setTestFail(e1.getTestFail() + e2.getTestFail());
                            e1.setFail(e1.getFail() + e2.getFail());
                            return e1;
                        }));

        return errorMap.values().stream()
                .filter(error -> error.getTestFail() > 0)
                .sorted(Comparator.comparing(TestErrorDaily::getTestFail, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TestErrorDaily> getErrorDailyListFromDB(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        List<TestErrorDaily> errorList;
        if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
        } else if (StringUtils.isEmpty(stationName)) {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
        } else {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
        }

        return errorList;
    }

    @Override
    public List<TestErrorDaily> getErrorDailyList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        Map<String, TestErrorDaily> errorByCodeMap = new HashMap<>();

        List<TestErrorDaily> errorList;
        if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
        } else if (StringUtils.isEmpty(stationName)) {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
        } else {
            errorList = testErrorDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
        }

        for (TestErrorDaily tmp : errorList) {
            TestErrorDaily error = errorByCodeMap.get(tmp.getErrorCode());
            if (error == null) {
                error = new TestErrorDaily();
                error.setFactory(tmp.getFactory());
                error.setModelName(tmp.getModelName());
                error.setGroupName(tmp.getGroupName());
                error.setStationName(tmp.getStationName());
                error.setErrorCode(tmp.getErrorCode());
                error.setStartDate(startDate);
                error.setEndDate(endDate);
            }

            error.setTestFail(error.getTestFail() + tmp.getTestFail());
            error.setFail(error.getFail() + tmp.getFail());

            errorByCodeMap.put(tmp.getErrorCode(), error);
        }

        return errorByCodeMap.values().stream()
                .sorted(Comparator.comparing(TestErrorDaily::getTestFail, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, TestError> getTopErrorCode(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, Integer limit) {
        Map<String, TestError> result =  new LinkedHashMap<>();

        Map<String, String> errorMetaMap = getErrorMetaMap(factory, modelName);
        List<TestError> errorByCodeList = getErrorByCodeList(factory, modelName, groupName, stationName, startDate, endDate);
        if (!errorByCodeList.isEmpty()) {
            for (int i = 0; i < errorByCodeList.size() && (limit == null || i < limit); i++) {
                TestError error = errorByCodeList.get(i);
                error.setErrorDescription(errorMetaMap.getOrDefault(errorByCodeList.get(i).getErrorCode(), ""));
                result.put(errorByCodeList.get(i).getErrorCode(), error);
            }
        }

        return result;
    }

    @Override
    public Map<String, TestErrorDaily> getErrorDailyTopErrorCode(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, Integer limit, String mode, MoType moType) {
        Map<String, TestErrorDaily> result =  new LinkedHashMap<>();

        Map<String, String> errorMetaMap = getErrorMetaMap(factory, modelName);
        Map<String, String> errorDescMap = testErrorMetaRepository.findAllByFactory(factory)
                .stream().collect(Collectors.toMap(TestErrorMeta::getDescription, TestErrorMeta::getErrorCode,  (e1, e2) -> e1));
        List<TestErrorDaily> errorByCodeList = getErrorDailyByCodeList(factory, modelName, groupName, stationName, startDate, endDate, mode);
        if (!errorByCodeList.isEmpty()) {
            for (int i = 0; i < errorByCodeList.size() && (limit == null || i < limit); i++) {
                TestErrorDaily error = errorByCodeList.get(i);
                if (StringUtils.isEmpty(error.getErrorCode())) {
                    error.setErrorCode(errorDescMap.getOrDefault(error.getErrorDescription(), ""));
                }
                if (StringUtils.isEmpty(error.getErrorDescription())) {
                    error.setErrorDescription(errorMetaMap.getOrDefault(error.getErrorCode(), ""));
                }
                result.put(error.getErrorCode(), error);
            }
        }

        return result;
    }

    @Override
    public Object saveNoteErrorService(TestNoteError noteError) throws IOException {
        TestNoteError testNoteError = new TestNoteError();
        TimeSpan fullTimeSpan = TimeSpan.from(noteError.getTimeSpan(), TimeSpan.now(TimeSpan.Type.FULL));

        if (StringUtils.isEmpty(noteError.getModelName()) || StringUtils.isEmpty(noteError.getError())) {
            return false;
        }

        if (!StringUtils.isEmpty(noteError.getRootCause()) && !StringUtils.isEmpty(noteError.getAction())) {
            TestSolutionMeta data = testSolutionMetaRepository.findByFactoryAndModelNameAndErrorCodeAndSolutionAndAction(noteError.getFactory(), noteError.getModelName(), noteError.getError(), noteError.getRootCause(), noteError.getAction());
            if (data == null) {
                List<TestSolutionMeta.GuidingStep> dt = new ArrayList<>();
                TestSolutionMeta.GuidingStep tmp = new TestSolutionMeta.GuidingStep();
                tmp.setText("");
                tmp.setImage("");
                dt.add(tmp);
                TestSolutionMeta testSolutionMeta = new TestSolutionMeta();
                testSolutionMeta.setAction(noteError.getAction());
                testSolutionMeta.setErrorCode(noteError.getError());
                testSolutionMeta.setFactory(noteError.getFactory());
                testSolutionMeta.setModelName(noteError.getModelName());
                testSolutionMeta.setSolution(noteError.getRootCause());
                testSolutionMeta.setAuthor("FOXCONN");
                testSolutionMeta.setNumberSuccess(1);
                testSolutionMeta.setNumberUsed(1);
                testSolutionMeta.setSteps(dt);
                testSolutionMetaRepository.save(testSolutionMeta);
            }
        }

        if (noteError.getUploadedFile() != null) {
            String originalFileName = noteError.getUploadedFile().getOriginalFilename();
            String fileName = System.currentTimeMillis() + "-" + new Random().nextInt(100);
            if (!StringUtils.isEmpty(originalFileName)) {
                if (originalFileName.contains(".")) {
                    fileName += "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                }
            }
            File file = new File(dataPath + "file/" + fileName);
            try {
                noteError.getUploadedFile().transferTo(file);
            } catch (IOException e) {
                log.error("### upload error", e);
            }
            noteError.setAttachFile("/ws-data/file/" + fileName);
        }

        if (noteError.getId() == 0){
            BeanUtils.copyPropertiesIgnoreNull(noteError, testNoteError, "id");
            testNoteError.setStartDate(fullTimeSpan.getStartDate());
            testNoteError.setEndDate(fullTimeSpan.getEndDate());
            if (noteError.getStatus() == null) {
                testNoteError.setStatus(TestNoteError.Status.ON_GOING);
            }
            testNoteErrorRepository.save(testNoteError);
            return true;
        }else{
            TestNoteError dataById = testNoteErrorRepository.findById(noteError.getId());
            BeanUtils.copyPropertiesIgnoreNull(noteError, dataById, "id");
            if (noteError.getStatus() == null) {
                dataById.setStatus(TestNoteError.Status.ON_GOING);
            }
            testNoteErrorRepository.save(dataById);
            return true;
        }

    }

    @Override
    public TestErrorMeta getErrorMeta(String errorCode) {
        return testErrorMetaRepository.findByErrorCode(errorCode);
    }

    @Override
    public List<String> getModelErrorList(String factory, Date startDate, Date endDate) {
        return testErrorDailyRepository.findDistinctModelNameByFactoryAndStartDateBetween(factory, startDate, endDate);
    }

    @Override
    public List<String> getGroupErrorList(String factory, String modelName, Date startDate, Date endDate) {
        return testErrorDailyRepository.findDistinctGroupNameByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
    }

    @Override
    public List<String> getStationErrorList(String factory, String modelName, String groupName, Date startDate, Date endDate) {
        return testErrorDailyRepository.findDistinctStationNameByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
    }
}
