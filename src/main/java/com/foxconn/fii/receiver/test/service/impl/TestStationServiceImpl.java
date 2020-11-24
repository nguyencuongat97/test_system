package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.TestStationDailyReport;
import com.foxconn.fii.data.b04ds02.repository.B04DS02ErrorLogRepository;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.receiver.test.service.*;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestStationServiceImpl implements TestStationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestLockService testLockService;

    @Autowired
    private TestTrackingService testTrackingService;

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestStationRepository testStationRepository;

    @Autowired
    private TestStationDailyRepository testStationDailyRepository;

    @Autowired
    private TestStationMetaRepository testStationMetaRepository;

    @Autowired
    private TestWarningSpecRepository testWarningSpecRepository;

    @Autowired
    private B04DS02ErrorLogRepository b04DS02ErrorLogRepository;

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestMoTypeConfigRepository testMoTypeConfigRepository;

    @Override
    public int[] saveAll(List<TestStation> stationList) {
        if (stationList.isEmpty()) {
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_station as target " +
                        "using(select factory=?, model_name=?, line_name=?, group_name=?, station_name=?, start_date=?, end_date=?, wip=?, first_fail=?, second_fail=?, pass=?, fail=?, retest_rate=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.station_name=source.station_name and target.start_date=source.start_date and target.end_date=source.end_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.line_name=source.line_name, " +
                        "   target.wip=source.wip, " +
                        "   target.first_fail=source.first_fail, " +
                        "   target.second_fail=source.second_fail, " +
                        "   target.pass=source.pass, " +
                        "   target.fail=source.fail, " +
                        "   target.retest_rate=source.retest_rate " +
                        "when not matched then " +
                        "   insert (factory, model_name, line_name, group_name, station_name, start_date, end_date, wip, first_fail, second_fail, pass, fail, retest_rate) " +
                        "   values(source.factory, source.model_name, source.line_name, source.group_name, source.station_name, source.start_date, source.end_date, source.wip, source.first_fail, source.second_fail, source.pass, source.fail, source.retest_rate);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestStation station = stationList.get(i);
                        preparedStatement.setString(1, station.getFactory());
                        preparedStatement.setString(2, station.getModelName());
                        preparedStatement.setString(3, station.getLineName());
                        preparedStatement.setString(4, station.getGroupName());
                        preparedStatement.setString(5, station.getStationName());
                        preparedStatement.setTimestamp(6, new Timestamp(station.getStartDate().getTime()));
                        preparedStatement.setTimestamp(7, new Timestamp(station.getEndDate().getTime()));
                        preparedStatement.setInt(8, station.getWip());
                        preparedStatement.setInt(9, station.getFirstFail());
                        preparedStatement.setInt(10, station.getSecondFail());
                        preparedStatement.setInt(11, station.getPass());
                        preparedStatement.setInt(12, station.getFail());
                        preparedStatement.setFloat(13, station.getRetestRate());
                    }

                    @Override
                    public int getBatchSize() {
                        return stationList.size();
                    }
                });
    }

    @Override
    public int[] saveAllMeta(List<TestStation> stationList) {
        if (stationList.isEmpty()) {
            return null;
        }

        List<TestStationMeta> stationMetaList = new ArrayList<>();
        for (TestStation station : stationList) {
            TestStationMeta stationMeta = new TestStationMeta();
            stationMeta.setFactory(station.getFactory());
            stationMeta.setModelName(station.getModelName());
            stationMeta.setLineName(station.getLineName());
            stationMeta.setGroupName(station.getGroupName());
            stationMeta.setStationName(station.getStationName());

            stationMetaList.add(stationMeta);
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_station_meta as target " +
                        "using(select factory=?, model_name=?, line_name=?, group_name=?, station_name=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.station_name=source.station_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.line_name=source.line_name, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, line_name, group_name, station_name, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.line_name, source.group_name, source.station_name, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestStationMeta station = stationMetaList.get(i);
                        preparedStatement.setString(1, station.getFactory());
                        preparedStatement.setString(2, station.getModelName());
                        preparedStatement.setString(3, station.getLineName());
                        preparedStatement.setString(4, station.getGroupName());
                        preparedStatement.setString(5, station.getStationName());
                        preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return stationMetaList.size();
                    }
                });
    }

    @Override
    public int countOnline(String factory, String modelName, String groupName, Date startDate, Date endDate) {
        return testStationRepository.countOnline(factory, modelName, groupName, startDate, endDate).size();
    }

    @Override
    public List<TestStationMeta> getStationMetaList(String factory, String modelName, String groupName) {
        return testStationMetaRepository.findAllByFactoryAndModelNameAndGroupName(factory, modelName, groupName);
    }

    @Override
    public TestStationMeta getStationMeta(String factory, String modelName, String groupName, String stationName) {
        return testStationMetaRepository.findByFactoryAndModelNameAndGroupNameAndStationName(factory, modelName, groupName, stationName);
    }

    @Override
    public Map<String, TestStation> getStationByHourlyMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, ShiftType shiftType) {
        Map<String, TestStation> stationByTimeMap = TestUtils.getHourlyMap(startDate, endDate);

        if (StringUtils.isEmpty(stationName)) {
            if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
                List<TestStationDaily> stationList;
                List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
                if (modelList.contains(modelName)) {
                    stationList = sfcTestGroupRepository.findStationHourlyByWorkDateBetween(factory, "UI", modelName, groupName, startDate, endDate);
                } else {
                    stationList = sfcTestGroupRepository.findStationHourlyByWorkDateBetween(factory, "", modelName, groupName, startDate, endDate);
                }

                stationList.forEach(tmpStation -> {
                    TestStation tmp = new TestStation();
                    BeanUtils.copyProperties(tmpStation, tmp);
                    TestStation station = stationByTimeMap.get(tmp.getShiftTime());
                    if (station != null) {
                        station.setWip(station.getWip() + tmp.getWip());
                        station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                        station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                        station.setPass(station.getPass() + tmp.getPass());
                        station.setFail(station.getFail() + tmp.getFail());
                        stationByTimeMap.put(tmp.getShiftTime(), station);
                    } else {
                        stationByTimeMap.put(tmp.getShiftTime(), tmp);
                    }
                });

            } else {
                testStationRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate)
                        .forEach(tmp -> {
                            TestStation station = stationByTimeMap.get(tmp.getShiftTime());
                            if (station != null) {
                                station.setWip(station.getWip() + tmp.getWip());
                                station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                                station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                                station.setPass(station.getPass() + tmp.getPass());
                                station.setFail(station.getFail() + tmp.getFail());
                                stationByTimeMap.put(tmp.getShiftTime(), station);
                            } else {
                                stationByTimeMap.put(tmp.getShiftTime(), tmp);
                            }
                        });
            }
        } else {
            testStationRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate)
                    .forEach(station -> stationByTimeMap.put(station.getShiftTime(), station));
        }

        for (Map.Entry<String, TestStation> entry : stationByTimeMap.entrySet()) {
            if (entry.getValue() == null) {
                stationByTimeMap.put(entry.getKey(), new TestStation());
            }
        }

        return stationByTimeMap;
    }

    @Override
    public Map<String, TestStationDaily> getStationByWeeklyMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, ShiftType shiftType) {
        List<TestStationDaily> stationList;

        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (modelList.contains(modelName)) {
                stationList = sfcTestGroupRepository.findStationHourlyByWorkDateBetween(factory, "UI", modelName, groupName, startDate, endDate);
            } else {
                stationList = sfcTestGroupRepository.findStationHourlyByWorkDateBetween(factory, "", modelName, groupName, startDate, endDate);
            }
        } else {
            if (StringUtils.isEmpty(stationName)) {
                stationList = testStationDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
            } else {
                stationList = testStationDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
            }
        }

        Map<String, TestStationDaily> result = stationList
                .stream().collect(Collectors.toMap(
                        station -> TimeSpan.format(TimeSpan.of(station.getStartDate(), station.getEndDate()), TimeSpan.Type.DAILY),
                        station -> station,
                        (station, tmp) -> {
                            station.setWip(station.getWip() + tmp.getWip());
                            station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                            station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                            station.setPass(station.getPass() + tmp.getPass());
                            station.setFail(station.getFail() + tmp.getFail());
                            return station;
                        }, HashMap::new));

        return result;
    }

    @Override
    public Map<String, Map<String, TestStation>> getStationByGroupMap(String factory, Date startDate, Date endDate, MoType moType) {
        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        return testStationRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate)
                .stream()
                .filter(station -> moTypeConfig.contain(station.getMo()))
                .collect(Collectors.groupingBy(
                        station -> station.getModelName() + '_' + station.getGroupName(),
                        Collectors.toMap(TestStation::getStationName, station -> {
                            TestStation testStation = new TestStation();
                            BeanUtils.copyProperties(station, testStation);
                            return testStation;
                        }, (station, tmp) -> {
                            station.setWip(station.getWip() + tmp.getWip());
                            station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                            station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                            station.setPass(station.getPass() + tmp.getPass());
                            station.setFail(station.getFail() + tmp.getFail());
                            return station;
                        }, HashMap::new)));
    }

    @Override
    public TestStation getStation(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType) {
        List<TestStation> stationList;
        if (StringUtils.isEmpty(stationName)) {
            stationList = testStationRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
        } else {
            stationList = testStationRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
        }

        if (stationList.isEmpty()) {
            return null;
        }

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        TestStation station = new TestStation();
        BeanUtils.copyProperties(stationList.get(0), station, "id");
        for (int i = 1; i < stationList.size(); i++) {
            TestStation tmp = stationList.get(i);
            if (moTypeConfig.contain(tmp.getMo())) {
                station.setWip(station.getWip() + tmp.getWip());
                station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                station.setPass(station.getPass() + tmp.getPass());
                station.setFail(station.getFail() + tmp.getFail());
            }
        }

        Map<String, TestError> errorCodeMap = testErrorService.getTopErrorCode(factory, station.getModelName(), station.getGroupName(), station.getStationName(), startDate, endDate, 3);
        station.setErrorMetaMap(errorCodeMap);

        return station;
    }

    @Override
    public TestStationDaily getStationDaily(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType) {
        List<TestStationDaily> stationList;
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (modelList.contains(modelName)) {
                customer = "UI";
            }

            if (StringUtils.isEmpty(stationName)) {
                stationList = sfcTestGroupRepository.findStationByWorkDateBetween(factory, customer, modelName, groupName, startDate, endDate);
            } else {
                stationList = sfcTestGroupRepository.findStationByWorkDateBetween(factory, customer, modelName, groupName, stationName, startDate, endDate);
            }
        } else {
            if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
                stationList = testStationDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
            } else if (StringUtils.isEmpty(stationName)) {
                stationList = testStationDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
            } else {
                stationList = testStationDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(factory, modelName, groupName, stationName, startDate, endDate);
            }
        }

        if (stationList.isEmpty()) {
            return null;
        }

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        TestStationDaily station = new TestStationDaily();
        BeanUtils.copyProperties(stationList.get(0), station, "id");
        for (int i = 1; i < stationList.size(); i++) {
            TestStationDaily tmp = stationList.get(i);
            if (moTypeConfig.contain(tmp.getMo())) {
                station.setWip(station.getWip() + tmp.getWip());
                station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                station.setPass(station.getPass() + tmp.getPass());
                station.setFail(station.getFail() + tmp.getFail());
            }
        }

        Map<String, TestErrorDaily> errorCodeMap = testErrorService.getErrorDailyTopErrorCode(factory, station.getModelName(), station.getGroupName(), station.getStationName(), startDate, endDate, 3, "", moType);
        station.setErrorMetaMap(errorCodeMap);

        return station;
    }

    @Override
    public List<TestStationDailyReport> getStationDailyReportList(String factory, Date startDate, Date endDate, String mode, MoType moType) {
        List<TestStationDaily> stationDailyList = testStationDailyRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate);

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, TestStationDailyReport> map = stationDailyList
                .stream()
                .filter(station -> moTypeConfig.contain(station.getMo()) && station.getRetestRate() > 3 && station.getWip() > 30)
                .collect(Collectors.toMap(
                        station -> station.getModelName() + '_' + station.getGroupName() + '_' + station.getStationName(),
                        station -> {
                            TestStationDailyReport testStation = new TestStationDailyReport();
                            BeanUtils.copyProperties(station, testStation);
                            return testStation;
                        }, (station, tmp) -> {
                            station.setWip(station.getWip() + tmp.getWip());
                            station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                            station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                            station.setPass(station.getPass() + tmp.getPass());
                            station.setFail(station.getFail() + tmp.getFail());
                            return station;
                        }, HashMap::new));

        return new ArrayList<>(map.values());
    }

    @Override
    public List<TestStationDaily> getStationDailyList(String factory, String modelName, String groupName, Date startDate, Date endDate, String mode, MoType moType) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            String customer = "";
            if (testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI").contains(modelName)) {
                customer = "UI";
            }
            return sfcTestGroupRepository.findStationByWorkDateBetween(factory, customer, modelName, groupName, startDate, endDate);
        }

        List<TestStationDaily> stationDailyList = testStationDailyRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, TestStationDaily> map = stationDailyList
                .stream()
                .filter(station -> moTypeConfig.contain(station.getMo()))
                .collect(Collectors.toMap(
                        station -> station.getModelName() + '_' + station.getGroupName() + '_' + station.getStationName(),
                        station -> {
                            TestStationDaily testStation = new TestStationDaily();
                            BeanUtils.copyProperties(station, testStation);
                            return testStation;
                        }, (station, tmp) -> {
                            station.setWip(station.getWip() + tmp.getWip());
                            station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                            station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                            station.setPass(station.getPass() + tmp.getPass());
                            station.setFail(station.getFail() + tmp.getFail());
                            return station;
                        }, HashMap::new));

        if ("B04".equalsIgnoreCase(factory) && "TE".equalsIgnoreCase(mode)) {
            Map<String, TestStationDaily> map2 = b04DS02ErrorLogRepository.getStationList(modelName, groupName, startDate, endDate)
                    .stream()
                    .filter(station -> moTypeConfig.contain(station.getMo()))
                    .collect(Collectors.toMap(
                            station -> station.getModelName() + '_' + station.getGroupName() + '_' + station.getStationName(),
                            station -> {
                                TestStationDaily testStation = new TestStationDaily();
                                BeanUtils.copyProperties(station, testStation);
                                return testStation;
                            }, (station, tmp) -> {
                                station.setWip(station.getWip() + tmp.getWip());
                                station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                                station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                                station.setPass(station.getPass() + tmp.getPass());
                                station.setFail(station.getFail() + tmp.getFail());
                                return station;
                            }, HashMap::new));

            map2.forEach((key, value) -> {
                if (map.containsKey(key)) {
                    value.setSecondFail(map.get(key).getSecondFail());
                }
            });

            return new ArrayList<>(map2.values());
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public Map<String, ListResponse> getStationIssue2(String factory, String modelName, String timeSpan) {
        Calendar calendar = Calendar.getInstance();
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<String> times = new ArrayList<>(TestUtils.getHourlyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate()).keySet());

        List<TestStation> lockedList = new ArrayList<>();
        List<TestStation> warningList = new ArrayList<>();
        List<TestStation> idleList = new ArrayList<>();
        List<TestStation> normalList = new ArrayList<>();

        Map<String, TestStation> stationMap = getStationMap(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, List<TestTracking>> trackingMap = testTrackingService.getTrackingList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream()
                .sorted(Comparator.comparing(TestTracking::getType).thenComparing(TestTracking::getCreatedAt, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(tracking -> tracking.getModelName() + "_" + tracking.getGroupName() + "_" + tracking.getStationName()));

        Map<String, TestSpecWarning> warningSpecMap = testWarningSpecRepository.findAllByFactory(factory).stream().collect(Collectors.toMap(warningSpec -> warningSpec.getModelName() + '_' + warningSpec.getGroupName(), warningSpec -> warningSpec));

        Map<String, Integer> planQtyByModelMap = testPlanService.getPlanQtyByModelMap(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.DAILY);

        Map<String, Integer> outputByModel = new HashMap<>();

        for (Map.Entry<String, TestStation> entry : stationMap.entrySet()) {
            TestStation station = entry.getValue();

            if (!StringUtils.isEmpty(modelName) && !modelName.equalsIgnoreCase(station.getModelName())) {
                continue;
            }

            outputByModel.put(station.getModelName()+"_"+station.getGroupName(), outputByModel.getOrDefault(station.getModelName()+"_"+station.getGroupName(), 0) + station.getWip());

            List<TestTracking> trackingList = trackingMap.get(entry.getKey());
            if (trackingList == null || trackingList.isEmpty()) {
                normalList.add(station);
                continue;
            }

            boolean validated = false;
            boolean idleValidated = false;
            List<Integer> notifyShiftIndexes = new ArrayList<>();
            int status = 0;
            for (TestTracking tracking : trackingList) {
                if (tracking.isLockedType()) {
                    status = 2;
                } else if (status < 1 && tracking.getType() == TestTracking.Type.WARNING_B) {
                    status = 1;
                }
                if (tracking.isValidated() || tracking.getStatus() == TestTracking.Status.OUTDATED) {
                    if (tracking.getType() != TestTracking.Type.WARNING_IDLE) {
                        validated = true;
                    }
                    idleValidated = true;
                }
                calendar.setTime(tracking.getCreatedAt());
                TimeSpan notifyTimeSpan = TimeSpan.from(calendar, TimeSpan.Type.HOURLY);
                notifyShiftIndexes.add(times.indexOf(TimeSpan.format(notifyTimeSpan, TimeSpan.Type.FULL)));
            }
            station.setNotifyShiftIndexes(notifyShiftIndexes);

            TestTracking latestTracking = trackingList.get(0);
            station.setNotifyTime(latestTracking.getCreatedAt());
            if (status == 2) {
                if (validated) {
                    station.setStatus(latestTracking.getType() == TestTracking.Type.LOCKED_A ? "LOCKED A" : "LOCKED B");
                } else {
                    if (fullTimeSpan.isNow() && latestTracking.getStatus() == TestTracking.Status.CLOSED) {
                        normalList.add(station);
                        continue;
                    }
                    station.setStatus(latestTracking.getType() == TestTracking.Type.LOCKED_A ? "ISSUE A" : "ISSUE B");
                }
                lockedList.add(station);
            } else if (status == 1) {
                if (!validated) {
                    if (fullTimeSpan.isNow()) {
                        normalList.add(station);
                        continue;
                    }
                    station.setStatus("ISSUE W");
                } else {
                    TestSpecWarning warningSpec = warningSpecMap.getOrDefault(station.getModelName() + '_' + station.getGroupName(), TestSpecWarning.DEFAULT_WARNING_SPEC);
                    if (fullTimeSpan.isNow() && !TestUtils.isOutSpec(station.getWip(), warningSpec.getWip(), station.getRetestRate(), warningSpec.getRetestRate(), station.getFirstPassRate(), warningSpec.getFirstPassRate())) {
                        normalList.add(station);
                        continue;
                    }
                    station.setStatus("WARNING");
                }
                warningList.add(station);
            } else {
                if (!idleValidated) {
                    if (fullTimeSpan.isNow()) {
                        normalList.add(station);
                        continue;
                    }
                    station.setStatus("ISSUE I");
                } else {
                    station.setStatus("IDLE");
                }
                idleList.add(station);
            }
        }

        lockedList.forEach(station -> {
            station.setTotalOutput(outputByModel.getOrDefault(station.getModelName()+"_"+station.getGroupName(), 0));
            station.setTotalPlan(planQtyByModelMap.getOrDefault(station.getModelName(), 0));
        });

        warningList.forEach(station -> {
            station.setTotalOutput(outputByModel.getOrDefault(station.getModelName()+"_"+station.getGroupName(), 0));
            station.setTotalPlan(planQtyByModelMap.getOrDefault(station.getModelName(), 0));
        });

        idleList.forEach(station -> {
            station.setTotalOutput(outputByModel.getOrDefault(station.getModelName()+"_"+station.getGroupName(), 0));
            station.setTotalPlan(planQtyByModelMap.getOrDefault(station.getModelName(), 0));
        });

        lockedList.sort(Comparator.comparing(TestStation::getNotifyShiftIndexes, (s1, s2) -> Integer.compare (s2.size(), s1.size())).thenComparing(TestStation::getNotifyTime));
        warningList.sort(Comparator.comparing(TestStation::getNotifyShiftIndexes, (s1, s2) -> Integer.compare (s2.size(), s1.size())).thenComparing(TestStation::getNotifyTime));
        idleList.sort(Comparator.comparing(TestStation::getNotifyShiftIndexes, (s1, s2) -> Integer.compare (s2.size(), s1.size())).thenComparing(TestStation::getNotifyTime));

        Map<String, ListResponse> result = new HashMap<>();
        result.put("NORMAL", ListResponse.success(StringUtils.isEmpty(modelName) ? Collections.emptyList() : normalList, normalList.size()));
        result.put("WARNING", ListResponse.success(warningList, warningList.size()));
        result.put("LOCKED", ListResponse.success(lockedList, lockedList.size()));
        result.put("IDLE", ListResponse.success(idleList, idleList.size()));

        return result;
    }

    private Map<String, TestStation> getStationMap(String factory, Date startDate, Date endDate) {
        return testStationRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate)
                .stream().collect(Collectors.toMap(
                        station -> station.getModelName() + '_' + station.getGroupName() + '_' + station.getStationName(),
                        station -> {
                            TestStation testStation = new TestStation();
                            BeanUtils.copyProperties(station, testStation);
                            return testStation;
                        }, (station, tmp) -> {
                            station.setWip(station.getWip() + tmp.getWip());
                            station.setFirstFail(station.getFirstFail() + tmp.getFirstFail());
                            station.setSecondFail(station.getSecondFail() + tmp.getSecondFail());
                            station.setPass(station.getPass() + tmp.getPass());
                            station.setFail(station.getFail() + tmp.getFail());
                            return station;
                        }, HashMap::new));
    }
}
