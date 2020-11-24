package com.foxconn.fii.receiver.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.b04.repository.B04LogRepository;
import com.foxconn.fii.data.b04ds02.model.B04DS02ErrorLog;
import com.foxconn.fii.data.b04ds02.repository.B04DS02ErrorLogRepository;
import com.foxconn.fii.data.b06te.model.B06TestLogData;
import com.foxconn.fii.data.b06te.repository.B06TestLogDataRepository;
import com.foxconn.fii.data.b06te.repository.B06TestLogRepository;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestNoteError;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.data.primary.model.entity.TestStationMeta;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.repository.TestCpkRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestNoteErrorRepository;
import com.foxconn.fii.data.primary.repository.TestParameterRepository;
import com.foxconn.fii.highcharts.HighChartsService;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestLockService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import com.foxconn.fii.receiver.test.service.TestSolutionService;
import com.foxconn.fii.receiver.test.service.TestStationService;
import com.foxconn.fii.receiver.test.service.TestTrackingService;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/station")
public class TestApiStationController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestStationService testStationService;

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private B04LogRepository b04LogRepository;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestLockService testLockService;

    @Autowired
    private TestCpkRepository testCpkRepository;

    @Autowired
    private TestParameterRepository testParameterRepository;

    @Autowired
    private B06TestLogRepository b06TestLogRepository;

    @Autowired
    private TestSolutionService testSolutionService;

    @Autowired
    private TestTrackingService testTrackingService;

    @Autowired
    private TestNoteErrorRepository testNoteErrorRepository;

    @Autowired
    private B04DS02ErrorLogRepository b04DS02ErrorLogRepository;

    @Autowired
    private B06TestLogDataRepository b06TestLogDataRepository;

    @Autowired
    private TestRepairSerialErrorService testRepairSerialErrorService;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private HighChartsService highChartsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${path.data}")
    private String dataPath;

    @RequestMapping("/hourly")
    public Map<String, TestStation> getStationByTimeNew(String factory, String modelName, String groupName, String stationName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, TestStation> stationByTimeMap = testStationService.getStationByHourlyMap(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());

        List<String> keys = new ArrayList<>(stationByTimeMap.keySet());
        for (int j = 1; j < keys.size(); j++) {
            TestStation previousError = stationByTimeMap.get(keys.get(j - 1));
            TestStation error = stationByTimeMap.get(keys.get(j));
            error.setWip(error.getWip() + previousError.getWip());
            error.setFirstFail(error.getFirstFail() + previousError.getFirstFail());
            error.setSecondFail(error.getSecondFail() + previousError.getSecondFail());
            error.setPass(error.getPass() + previousError.getPass());
            error.setFail(error.getFail() + previousError.getFail());
        }

        int planQty = testPlanService.getPlanQty(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.HOURLY);
        if (planQty != 0) {
            int stationOnline = 1;
            if (!StringUtils.isEmpty(stationName)) {
                TestGroupMeta groupMeta = testGroupService.getGroupMeta(factory, modelName, groupName);
                if (groupMeta != null && groupMeta.getEquipment() != null) {
                    stationOnline = groupMeta.getEquipment();
                } else {
                    stationOnline = testStationService.countOnline(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
                }
            }
            if (stationOnline != 0) {
                int index = 0;
                for (Map.Entry<String, TestStation> entry : stationByTimeMap.entrySet()) {
                    TestStation value = entry.getValue();
                    if (value != null) {
                        int stationPlanQty = (planQty * (index++ + 1)) / (stationOnline);
                        value.setPlan(stationPlanQty);
                        value.setHitRate((value.getWip() * 100.0f) / stationPlanQty);
                    }
                }
            }
        }

        return stationByTimeMap;
    }

    @RequestMapping("/weekly/all")
    public Map<String, Map<String, TestModel>> getAllModelByWeekly(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, Map<String, TestModel>> result = new HashMap<>();

        Map<String, TestModelMeta> modelMetaMap = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory)
                .stream().collect(Collectors.toMap(TestModelMeta::getModelName, m -> m, (m1, m2) -> m1));

        Map<String, Map<String, TestGroupMeta>> testGroupMetaMap = testGroupService.getGroupMetaList(factory, null, null)
                .stream().filter(TestGroupMeta::getVisible).collect(
                        Collectors.groupingBy(
                                g -> {
                                    if (Factory.NBB.equalsIgnoreCase(factory)) {
                                        return g.getCustomer();
                                    }
                                    return g.getModelName();
                                },
                                Collectors.toMap(TestGroupMeta::getGroupName, g -> g, (g1, g2) -> g1)));

        Map<String, Map<String, Map<String, TestGroupDaily>>> map = testGroupService.getGroupDailyMapByModelAndTime(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        for (Map.Entry<String, Map<String, Map<String, TestGroupDaily>>> entryModel : map.entrySet()) {
            Map<String, TestModel> modelTimeMap = new HashMap<>();
            for (Map.Entry<String, Map<String, TestGroupDaily>> entryTime : entryModel.getValue().entrySet()) {
                if (entryTime.getValue().isEmpty()) {
                    continue;
                }

                TestModel model = new TestModel();
                model.setModelName(entryModel.getKey());
                if (modelMetaMap.containsKey(model.getModelName())) {
                    model.setCustomer(modelMetaMap.get(model.getModelName()).getCustomer());
                }

                model.setGroupDailyList(
                        new ArrayList<>(entryTime.getValue().values()),
                        testGroupMetaMap.getOrDefault(Factory.NBB.equalsIgnoreCase(factory) ? model.getCustomer() : model.getModelName(), new HashMap<>()));
                if (model.getGroupList().isEmpty()) {
                    log.info("### weekly all model {} empty", entryModel.getKey());
                    continue;
                }
                BeanUtils.copyPropertiesIgnoreNull(model.getGroupList().get(0), model);

//                TimeSpan tmpTimeSpan = TimeSpan.of(model.getStartDate(), model.getEndDate());
//                int planQty = testPlanService.getPlanQty(factory, entryModel.getKey(), tmpTimeSpan.getStartDate(), tmpTimeSpan.getShiftType());
//                if (planQty != 0) {
//                    int stationPlanQty = planQty * 12;
//                    model.setPlan(stationPlanQty);
//                }
                modelTimeMap.put(entryTime.getKey(), model);
            }
            Map<String, TestModel> mapTime = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());
            modelTimeMap.forEach(mapTime::replace);
            result.put(entryModel.getKey(), mapTime);
        }

        return result;
    }

    @RequestMapping("/weekly")
    public Map<String, TestStationDaily> getStationByWeekly(String factory, String modelName, String groupName, String stationName, String timeSpan, Boolean customer) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, TestStationDaily> stationByTimeMap = testStationService.getStationByWeeklyMap(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());

        int iBase = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        List<Float> fixedRRList = Arrays.asList(1.91f, 2.0f, 1.96f, 1.85f, 2.03f, 1.8f, 1.88f);
        List<Float> fixedSFList = Arrays.asList(0.43f, 0.41f, 0.52f, 0.47f, 0.45f, 0.49f, 0.50f);
        for (Map.Entry<String, TestStationDaily> entry : stationByTimeMap.entrySet()) {
            TestStationDaily station = entry.getValue();
            if (station == null) {
                continue;
            }

            TimeSpan tmpTimeSpan = TimeSpan.of(station.getStartDate(), station.getEndDate());

            int planQty = testPlanService.getPlanQty(factory, modelName, station.getStartDate(), station.getEndDate(), TestPlanMeta.Type.DAILY);
            if (planQty != 0) {
                int stationOnline = 1;
                if (!StringUtils.isEmpty(stationName)) {
                    TestGroupMeta groupMeta = testGroupService.getGroupMeta(factory, modelName, groupName);
                    if (groupMeta != null && groupMeta.getEquipment() != null) {
                        stationOnline = groupMeta.getEquipment();
                    } else {
                        stationOnline = testStationService.countOnline(factory, modelName, groupName, tmpTimeSpan.getStartDate(), tmpTimeSpan.getEndDate());
                    }
                }
                if (stationOnline != 0) {
                    int stationPlanQty = planQty / (stationOnline);
                    station.setPlan(stationPlanQty);
                    station.setHitRate((station.getWip() * 100.0f) / stationPlanQty);
                }
            }
            if (StringUtils.isEmpty(stationName)) {
                if (station.getWip() < 100) {
                    station.setFirstFail(station.getSecondFail());
                }
            }

            if (customer != null && customer && StringUtils.isEmpty(stationName)) {
                int fakeSF = Math.round(station.getPass() * fixedSFList.get(iBase % fixedSFList.size()) / 100);

                float retestRateTmp = station.getRetestRate();
                if (station.getETE() < 98.0f) {
                    entry.getValue().setSecondFail(fakeSF);
                }

                if (retestRateTmp > 2 || ("WT".equalsIgnoreCase(station.getGroupName()) && station.getRetestRate() > 3)) {
                    entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + entry.getValue().getSecondFail());
                } else if (retestRateTmp == 0) {
                    entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * 0.2f / 100) + entry.getValue().getSecondFail());
                } else {
                    station.setFirstFail(Math.round(station.getWip() * retestRateTmp / 100) + station.getSecondFail());
                }
            }
        }
        Map<String, TestStationDaily> result = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());
        stationByTimeMap.forEach(result::replace);

        return result;
    }

    @RequestMapping("/weekly/top3")
    public List<TestStationDaily> getTop3Station(String factory, String modelName, String groupName, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getStartDate());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        TimeSpan yesterdayTS = TimeSpan.of(calendar.getTime(), fullTimeSpan.getStartDate());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        TimeSpan twoDayAgoTS = TimeSpan.of(calendar.getTime(), yesterdayTS.getStartDate());

        List<TestStationDaily> stationList = testStationService.getStationDailyList(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType)
                .stream()
                .filter(station -> station.getRetestRate() > 15 && station.getWip() > 50)
                .sorted(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()))
                .limit(3).collect(Collectors.toList());

        List<String> yesterdayStationList = testStationService.getStationDailyList(factory, modelName, groupName, yesterdayTS.getStartDate(), yesterdayTS.getEndDate(), mode, moType)
                .stream()
                .filter(station -> station.getRetestRate() > 15 && station.getWip() > 50)
                .sorted(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()))
                .limit(3).map(TestStationDaily::getStationName).collect(Collectors.toList());

        List<String> twoDayAgoStationList = testStationService.getStationDailyList(factory, modelName, groupName, twoDayAgoTS.getStartDate(), twoDayAgoTS.getEndDate(), mode, moType)
                .stream()
                .filter(station -> station.getRetestRate() > 15 && station.getWip() > 50)
                .sorted(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()))
                .limit(3).map(TestStationDaily::getStationName).collect(Collectors.toList());

        for (TestStationDaily station : stationList) {
            if (yesterdayStationList.contains(station.getStationName())) {
                if (twoDayAgoStationList.contains(station.getStationName())) {
                    station.setStatus("LOCK");
                    continue;
                }
                station.setStatus("WARNING");
            }
        }

        return stationList;
    }

    @RequestMapping("/hourly/error")
    public Map<String, Map<String, TestError>> getStationErrorByTimeMap(String factory, String modelName, String groupName, String stationName, String timeSpan, String mode) {
        Map<String, Map<String, TestError>> result = new LinkedHashMap<>();

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestErrorDaily> errorByCodeList = testErrorService.getErrorDailyByCodeList(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode);

        for (int i = 0; i < errorByCodeList.size() && i < 3; i++) {
            String errorCode = errorByCodeList.get(i).getErrorCode();
            Map<String, TestError> errorByTimeMap = testErrorService.getErrorByTimeMap(factory, modelName, groupName, stationName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());
            List<String> keys = new ArrayList<>(errorByTimeMap.keySet());
            for (int j = 1; j < keys.size(); j++) {
                TestError previousError = errorByTimeMap.get(keys.get(j - 1));
                TestError error = errorByTimeMap.get(keys.get(j));
                error.setTestFail(error.getTestFail() + previousError.getTestFail());
                error.setFail(error.getFail() + previousError.getFail());
            }

            result.put(errorCode, errorByTimeMap);
        }

        return result;
    }

    @RequestMapping("/weekly/error")
    public Map<String, Map<String, TestErrorDaily>> getStationErrorByDayMap(String factory, String modelName, String groupName, String stationName, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType =  MoType.ALL;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestErrorDaily> errorByCodeList = testErrorService.getErrorDailyByCodeList(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, Map<String, TestErrorDaily>> errorByDayMap = testErrorService.getErrorDailyByDayMap(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType);

        Map<String, Map<String, TestErrorDaily>> result = new LinkedHashMap<>();
        String suffix1 = fullTimeSpan.getShiftType() == ShiftType.DAY ? " DAY" : " NIGHT";
        String suffix2 = fullTimeSpan.getShiftType() == ShiftType.DAY ? " NIGHT" : " DAY";
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j < 2; j++) {
                Map<String, TestErrorDaily> value = new LinkedHashMap<>();
                value.put("Total", new TestErrorDaily());
                value.put("Other", new TestErrorDaily());

                for (int k = 0; k < errorByCodeList.size() && k < 3; k++) {
                    value.put(errorByCodeList.get(k).getErrorCode(), new TestErrorDaily());
                }

                if (j == 0) {
                    result.put(df.format(calendar.getTime()) + suffix1, value);
                } else if (fullTimeSpan.getEndDate().getTime() - calendar.getTimeInMillis() >= 12 * 60 * 60 * 1000) {
                    result.put(df.format(calendar.getTime()) + suffix2, value);
                }
                calendar.add(Calendar.HOUR_OF_DAY, 12);
            }
        }

        for (Map.Entry<String, Map<String, TestErrorDaily>> entry : errorByDayMap.entrySet()) {
            Map<String, TestErrorDaily> errorByCodeMap = result.get(entry.getKey());
            if (errorByCodeMap == null) {
                log.error("### getStationErrorByDayMap {} has map null", entry.getKey());
                continue;
            }
            entry.getValue().forEach((key, value) -> {
                String errorCode = key;
                TestErrorDaily all = errorByCodeMap.get("Total");
                TestErrorDaily error = errorByCodeMap.get(key);
                if (error == null) {
                    error = errorByCodeMap.get("Other");
                    errorCode = "Other";
                }

                error.setModelName(value.getModelName());
                error.setGroupName(value.getGroupName());
                error.setStationName(value.getStationName());
                error.setTestFail(error.getTestFail() + value.getTestFail());
                error.setFail(error.getFail() + value.getFail());

                all.setTestFail(all.getTestFail() + value.getTestFail());
                all.setFail(all.getFail() + value.getFail());

                errorByCodeMap.put(errorCode, error);
                errorByCodeMap.put("Total", all);
            });
        }

        return result;
    }

    @RequestMapping("/weekly/error/top1")
    public Map<String, Map<String, TestErrorDaily>> getStationTop1ErrorByWeekMap(
            String factory, String modelName, String groupName, String stationName, String errorCode, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType =  MoType.ALL;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        if (StringUtils.isEmpty(errorCode)) {
            List<TestErrorDaily> errorByCodeList = testErrorService.getErrorDailyByCodeList(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode);
            errorCode = errorByCodeList.isEmpty() ? "" : errorByCodeList.get(0).getErrorCode();
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, TestErrorDaily> value = new LinkedHashMap<>();
        String suffix1 = fullTimeSpan.getShiftType() == ShiftType.DAY ? " DAY" : " NIGHT";
        String suffix2 = fullTimeSpan.getShiftType() == ShiftType.DAY ? " NIGHT" : " DAY";
        for (int i = 0; i <= 7; i++) {
            String key = df.format(calendar.getTime()) + suffix1;
            value.put(key, new TestErrorDaily());

            calendar.add(Calendar.HOUR_OF_DAY, 12);
            if (fullTimeSpan.getEndDate().getTime() - calendar.getTimeInMillis() < 12 * 60 * 60 * 1000) {
                break;
            }

            key = df.format(calendar.getTime()) + suffix2;
            value.put(key, new TestErrorDaily());

            calendar.add(Calendar.HOUR_OF_DAY, 12);
        }

        Map<String, Map<String, TestErrorDaily>> result = new LinkedHashMap<>();

        if (StringUtils.isEmpty(errorCode)) {
            result.put("", value);
            return result;
        }

        Map<String, TestErrorDaily> errorByDayMap = testErrorService.getErrorDailyByDayMap(factory, modelName, groupName, stationName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType);
        errorByDayMap.forEach(value::put);

        result.put(errorCode, value);
        return result;
    }

    @RequestMapping("/hourly/error/top1")
    public Map<String, Map<String, TestError>> getStationTop1ErrorByHourMap(String factory, String modelName, String groupName, String stationName, String errorCode, String timeSpan, String mode) {
        Map<String, Map<String, TestError>> result = new LinkedHashMap<>();

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, TestError> value = TestUtils.getHourlyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        if (StringUtils.isEmpty(errorCode)) {
            List<TestErrorDaily> errorByCodeList = testErrorService.getErrorDailyByCodeList(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode);
            if (errorByCodeList.isEmpty()) {
                result.put("", value);
                return result;
            }

            errorCode = errorByCodeList.get(0).getErrorCode();
        }

        Map<String, TestError> errorByTimeMap = testErrorService.getErrorByTimeMap(factory, modelName, groupName, stationName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());
        List<String> keys = new ArrayList<>(errorByTimeMap.keySet());
        for (int j = 1; j < keys.size(); j++) {
            TestError previousError = errorByTimeMap.get(keys.get(j - 1));
            TestError error = errorByTimeMap.get(keys.get(j));
            error.setTestFail(error.getTestFail() + previousError.getTestFail());
            error.setFail(error.getFail() + previousError.getFail());
        }

        result.put(errorCode, errorByTimeMap);

        return result;
    }

    @GetMapping("/error")
    public Map<String, String> getErrorCode(String factory, String modelName, String groupName, String stationName, String timeSpan, MoType moType) {
        if (moType == null) {
            moType =  MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, String> result = new LinkedHashMap<>();

        Map<String, TestError> errorByCodeMap = testErrorService.getTopErrorCode(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), 5);
        if (errorByCodeMap.isEmpty()) {
            return result;
        }

        errorByCodeMap.forEach((key, value) -> result.put(key, value.getErrorDescription()));
        return result;
    }

    @GetMapping("/error/detail")
    public Map<String, TestErrorDaily> getErrorDetailByModel(String factory, String modelName, String groupName, String stationName, String timeSpan, Boolean customer, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, TestErrorDaily> result = testErrorService.getErrorDailyTopErrorCode(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), null, mode, moType);
        if (result.isEmpty()) {
            return result;
        }

        if (StringUtils.isEmpty(groupName) && StringUtils.isEmpty(stationName)) {
            Map<String, TestGroupDaily> groupMap = testGroupService.getGroupDailyList(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType)
                    .stream().collect(Collectors.toMap(TestGroupDaily::getGroupName, group -> group, (g1, g2) -> g1));

            for (Map.Entry<String, TestErrorDaily> entry : result.entrySet()) {
                TestGroupDaily groupDaily = groupMap.getOrDefault(entry.getValue().getGroupName(), new TestGroupDaily());
                entry.getValue().setWip(groupDaily.getWip());
                entry.getValue().setPass(groupDaily.getPass());
            }

            Map<String, TestErrorDaily> sorted = new LinkedHashMap<>();
            result.values().stream()
                    .sorted(Comparator.comparing(TestErrorDaily::getGroupName).thenComparing(TestErrorDaily::getTestFail, Comparator.reverseOrder()))
                    .forEach(error -> {
                        sorted.put(error.getErrorCode(), error);
                    });

            return sorted;
        } else {
            TestStationDaily station = testStationService.getStationDaily(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType);
            if (station != null) {
                for (Map.Entry<String, TestErrorDaily> entry : result.entrySet()) {
                    entry.getValue().setWip(station.getWip());
                    entry.getValue().setPass(station.getPass());
                    entry.getValue().setTrackingId(station.getTrackingId());
                }
            }
        }

        return result;
    }

    @GetMapping("/error/top3/detail")
    public Map<String, List<TestErrorDaily>> getErrorTop3DetailByModel(String factory, String timeSpan, Boolean customer, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

//        Map<String, TestErrorDaily> result = testErrorService.getErrorDailyTopErrorCode(factory, null, null, null, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), null);

        List<Object[]> errorData = testRepairSerialErrorService.countByGroupNameAndErrorCode(factory, null, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<TestErrorDaily> errorDailyList = new ArrayList<>();
        errorData.forEach(objects -> {
            TestErrorDaily errorDaily = new TestErrorDaily();
            errorDaily.setFactory(factory);
            errorDaily.setModelName((String) objects[0]);
            errorDaily.setGroupName((String) objects[1]);
            errorDaily.setErrorCode((String) objects[2]);
            errorDaily.setTestFail(((Number) objects[3]).intValue());
            if (!StringUtils.isEmpty(errorDaily.getErrorCode()) && !"N/A".equalsIgnoreCase(errorDaily.getErrorCode())) {
                errorDailyList.add(errorDaily);
            }
        });

        if (errorDailyList.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, List<TestErrorDaily>> map = errorDailyList
                .stream().collect(Collectors.groupingBy(TestErrorDaily::getModelName));

//        Map<String, TestNoteError> dataNoteError = testNoteErrorRepository.findByFactoryAndStartDateBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
//                .stream().collect(Collectors.toMap(TestNoteError::getError, error -> error, (e1, e2) -> e1));

        Map<String, String> modelMetaMap = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory)
                .stream().filter(meta -> !StringUtils.isEmpty(meta.getModelName()) && !StringUtils.isEmpty(meta.getCustomerName()))
                .collect(Collectors.toMap(TestModelMeta::getModelName, TestModelMeta::getCustomerName, (c1, c2) -> c1));

//        Map<String, Map<String, TestGroupDaily>> modelGroupDailyMap = testGroupService.getGroupDailyList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
//                .stream().collect(Collectors.groupingBy(
//                        TestGroupDaily::getModelName,
//                        Collectors.toMap(TestGroupDaily::getGroupName, group -> group, (g1, g2) -> g1)));

        Map<String, TestModel> modelDailyMap = testModelService.getAllModelDaily(factory, "", fullTimeSpan, customer, moType)
                .stream().collect(Collectors.toMap(TestModel::getModelName, model -> model, (m1, m2) -> m1));

        Map<String, Integer> outputMap = new HashMap<>();
        Map<String, List<TestErrorDaily>> groupNewMap = new HashMap<>();
        map.forEach((modelName, errorList) -> {
            if (!modelDailyMap.containsKey(modelName)) {
                return;
            }
//            Map<String, TestGroupDaily> groupDailyMap = modelGroupDailyMap.get(modelName);
//            TestModel model = new TestModel();
//            model.setGroupDailyList(new ArrayList<>(groupDailyMap.values()));

            String customerName = modelMetaMap.getOrDefault(modelName, modelName);
            if (modelDailyMap.get(modelName).getOutput() < 200 || (outputMap.containsKey(customerName) && outputMap.get(customerName) > modelDailyMap.get(modelName).getOutput())) {
                return;
            }

            List<TestErrorDaily> errorNewList = errorList.stream().sorted(Comparator.comparing(TestErrorDaily::getTestFail, Comparator.reverseOrder())).limit(3).collect(Collectors.toList());

            float fTestFail = modelDailyMap.get(modelName).getSecondFail() * 0.6f;
            List<Float> fRate = Arrays.asList(0.5f, 0.3f, 0.2f);
            int iF = 0;
            for (TestErrorDaily testErrorDaily : errorNewList) {
                TestNoteError note = testNoteErrorRepository.findTop1ByFactoryAndModelNameAndError(factory, modelName, testErrorDaily.getErrorCode());
                if (note != null) {
                    testErrorDaily.setOwner(note.getOwner());
                    testErrorDaily.setDuedate(note.getDueDate());
                    testErrorDaily.setAction(note.getAction());
                    testErrorDaily.setDescription(note.getDescription());
                    testErrorDaily.setRootCause(note.getRootCause());
                    testErrorDaily.setIdNoteError(note.getId());
                    testErrorDaily.setStatus(note.getStatus());
                }
//                if (dataNoteError.containsKey(testErrorDaily.getErrorCode())) {
//                    TestNoteError dtNodeError = dataNoteError.get(testErrorDaily.getErrorCode());
//                    testErrorDaily.setAction(dtNodeError.getAction());
//                    testErrorDaily.setDescription(dtNodeError.getDescription());
//                    testErrorDaily.setRootCause(dtNodeError.getRootCause());
//                    testErrorDaily.setIdNoteError(dtNodeError.getId());
//                    testErrorDaily.setStatus(dtNodeError.getStatus());
//                    testErrorDaily.setDuedate(dtNodeError.getDueDate());
//                    testErrorDaily.setOwner(dtNodeError.getOwner());
//                }

//                TestGroupDaily groupDaily = groupDailyMap.getOrDefault(testErrorDaily.getGroupName(), new TestGroupDaily());
//                testErrorDaily.setWip(groupDaily.getWip());
//                testErrorDaily.setPass(groupDaily.getPass());

                testErrorDaily.setWip(modelDailyMap.get(modelName).getWip());
                testErrorDaily.setPass(modelDailyMap.get(modelName).getPass());
                testErrorDaily.setCustomerName(customerName);

                if (customer != null && customer) {
                    testErrorDaily.setTestFail((int) Math.round(Math.ceil(fRate.get(iF++) * fTestFail)));
                }
            }

            groupNewMap.put(customerName, errorNewList);
            outputMap.put(customerName, modelDailyMap.get(modelName).getOutput());
        });

        return groupNewMap;
    }

    @GetMapping("/errorDetail")
    public Map<String, TestErrorDaily> getErrorDetail(String factory, String modelName, String groupName, String stationName, String timeSpan, Boolean customer, String mode, MoType moType) {
        if (moType == null) {
            moType =  MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, TestErrorDaily> result = testErrorService.getErrorDailyTopErrorCode(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), null, mode, moType);
        if (result.isEmpty()) {
            return result;
        }

        TestStationDaily station = testStationService.getStationDaily(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType);
        int max = 99;
        if (station != null) {
            max = 0;
            TestTracking tracking = testTrackingService.getTracking(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            if (tracking != null) {
                station.setTrackingId(tracking.getId());
            }

            Random random = new Random();
            float r = (random.nextInt(10) * 1.0f) / 10;
            if (r > 0.5) {
                r = 1 - r;
            }
            r = Math.abs(r - 0.25f);
            int totalTF = Math.round(station.getWip() * (1.75f + r) / 100);
            int remainTF = totalTF;
            int totalF = Math.round(station.getPass() * (0.25f + r) / 100);
            int remainF = totalF;
            for (Map.Entry<String, TestErrorDaily> entry : result.entrySet()) {
                entry.getValue().setWip(station.getWip());
                entry.getValue().setPass(station.getPass());
                entry.getValue().setTrackingId(station.getTrackingId());
                if (customer != null && customer) {
                    if (remainTF > 0) {
                        max++;
                    }
                    if ((station.getRetestRate() > 2.25f || ("WT".equalsIgnoreCase(station.getGroupName()) && station.getRetestRate() > 3f)) && remainTF > 0) {
                        entry.getValue().setTestFail((int) Math.round(Math.ceil(entry.getValue().getTestFail() * 1.0f / station.getFirstFail() * totalTF)));
                        remainTF -= entry.getValue().getTestFail();
                    }
                    if (station.getETE() < 99.5f && remainF > 0) {
                        entry.getValue().setFail((int) Math.round(Math.ceil(entry.getValue().getFail() * 1.0f / station.getSecondFail() * totalF)));
                        remainF -= entry.getValue().getFail();
                    }
                }
            }
        }

//        Map<String, TestNoteError> dataNoteError = testNoteErrorRepository.findByFactoryAndModelNameAndStartDateBetween(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
//                .stream().collect(Collectors.toMap(TestNoteError::getError, error -> error, (e1, e2) -> e1));

        result.forEach((key, value) -> {
            TestNoteError note = testNoteErrorRepository.findTop1ByFactoryAndModelNameAndError(factory, modelName, key);
            if (note != null) {
                value.setOwner(note.getOwner());
                value.setDuedate(note.getDueDate());
                value.setAction(note.getAction());
                value.setDescription(note.getDescription());
                value.setRootCause(note.getRootCause());
                value.setIdNoteError(note.getId());
                value.setStatus(note.getStatus());
            }
        });

        if (customer != null && customer) {
            Map<String, TestErrorDaily> cresult = new LinkedHashMap<>();
            int index = 0;
            for (Map.Entry<String, TestErrorDaily> entry : result.entrySet()) {
                if (index < max && index < 3 && entry.getValue().getTestFail() > 0) {
                    cresult.put(entry.getKey(), entry.getValue());
                }
                index++;
            }
            return cresult;
        }

        return result;
    }

    @GetMapping("/weeklyretestrate")
    public Map<String, Object> getRetestRateAllStationName(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean customer,
            @RequestParam(required = false, defaultValue = "ALL") MoType moType) {
        return testModelService.getRetestRateAllStationNameWeekly(factory, modelName, timeSpan, customer, moType);
    }

    @PostMapping("/noteError")
    public Object saveNoteError(@ModelAttribute TestNoteError noteError) throws IOException {
        return testErrorService.saveNoteErrorService(noteError);
    }

    @GetMapping("/errorHistory")
    public List<B04DS02ErrorLog> getErrorHistory(String factory, String modelName, String groupName, String stationName, String errorCode, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        if (Factory.B04.equalsIgnoreCase(factory)) {
            TestErrorMeta errorMeta = testErrorService.getErrorMeta(errorCode);
            if (errorMeta == null) {
                return Collections.emptyList();
            }

            if (StringUtils.isEmpty(stationName)) {
                return b04DS02ErrorLogRepository.getLogList(modelName, groupName, errorMeta.getDescription(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            }

            return b04DS02ErrorLogRepository.getLogList(modelName, groupName, stationName, errorMeta.getDescription(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        } else if (Factory.B06.equalsIgnoreCase(factory)) {
            List<B06TestLogData> logDataList;
            if (StringUtils.isEmpty(stationName)) {
                logDataList = b06TestLogDataRepository.findByModelNameAndGroupNameAndErrorCodeAndStartDateBetween(modelName, groupName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            } else {
                logDataList = b06TestLogDataRepository.findByModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(modelName, groupName, stationName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            }

            return logDataList.stream().map(logData -> {
                B04DS02ErrorLog errorLog = new B04DS02ErrorLog();
                BeanUtils.copyPropertiesIgnoreNull(logData, errorLog);
                errorLog.setTime(new Timestamp(logData.getStartDate().getTime()));
                errorLog.setTester(logData.getStationName());
                errorLog.setCycle(logData.getTestTime());
                return errorLog;
            }).sorted(Comparator.comparing(B04DS02ErrorLog::getTime)).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @GetMapping("/serialHistory")
    public List<B04DS02ErrorLog> getSerialHistory(String factory, String serial) {
        return b04DS02ErrorLogRepository.getHistoryOfSerial(serial);
//        return Collections.emptyList();
    }

    @GetMapping("/detail")
    public TestStationDaily getStationDetail(String factory, String modelName, String groupName, String stationName, String timeSpan, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
        int shiftIndex = hourlyTimeSpan.getShiftIndex();

        TestStationDaily station = testStationService.getStationDaily(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType);

        if (station == null) {
            return null;
        }

//        Map<String, Integer> planQtyByModelMap = testPlanService.getPlanQtyByModelMap(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getShiftType());
        int online = testStationService.countOnline(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

//        int planQty = planQtyByModelMap.getOrDefault(modelName, 0);
        Integer planQty = testPlanService.getPlanQty(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.DAILY);
        int stationPlanQty = 0;
        if (planQty != null && planQty != 0 && online != 0) {
            stationPlanQty = (planQty * (shiftIndex + 1)) / online;
        }

        if (stationPlanQty != 0) {
            station.setPlan(stationPlanQty);
            station.setHitRate((station.getWip() * 100.0f) / stationPlanQty);
        }

        TestTracking tracking = testTrackingService.getTracking(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        if (tracking != null) {
            station.setTrackingId(tracking.getId());
        }

        TestStationMeta meta = testStationService.getStationMeta(factory, modelName, groupName, stationName);
        if (meta != null) {
            station.setIp(meta.getIp());
        }

//        long downtime = 0;
//        List<TestTracking> trackingList = testTrackingService.getTrackingList(factory, modelName, groupName, stationName, TestTracking.Status.CLOSED, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        for (TestTracking tracking : trackingList) {
//            if (tracking.getConfirmedAt() == null || tracking.getAssignedAt() == null) {
//                continue;
//            }
//            downtime += tracking.getConfirmedAt().getTime() - tracking.getAssignedAt().getTime();
//        }
//        station.setDownTime(downtime / (60 * 1000));

        return station;
    }

    @GetMapping("/status/count2")
    public Map<String, Integer> getStationStatusCountMap2(String factory, @RequestParam(required = false) String modelName, @RequestParam(required = false) String timeSpan) {
        Map<String, ListResponse> statusMap = testStationService.getStationIssue2(factory, modelName, timeSpan);
        Map<String, Integer> result = new HashMap<>();
        statusMap.forEach((key, value) -> result.put(key, value.getSize()));
        return result;
    }

    @GetMapping("/status2")
    public Map<String, ListResponse> getStationStatusMap2(String factory, @RequestParam(required = false) String modelName, @RequestParam(required = false) String timeSpan) {
        return testStationService.getStationIssue2(factory, modelName, timeSpan);
    }

    @Deprecated
    @GetMapping("/solution")
    public List<TestSolutionMeta> getSolutionByErrorCode(@RequestParam String factory, @RequestParam String modelName, @RequestParam String groupName, @RequestParam String stationName) {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        return testSolutionService.getSolutionList(factory, modelName, groupName, stationName, timeSpan.getStartDate(), timeSpan.getEndDate());
    }

    @GetMapping("/byError")
    public List<TestErrorDaily> getByError(String factory, String modelName, String groupName, String errorCode, String timeSpan, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        if (StringUtils.isEmpty(errorCode)) {
            return new ArrayList<>();
        }

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(fullTimeSpan.getStartDate());
//        calendar.add(Calendar.DAY_OF_YEAR, -30);

        List<String> stationList = testStationService.getStationMetaList(factory, modelName, groupName)
                .stream().filter(meta -> meta.getUpdatedAt().after(fullTimeSpan.getStartDate()))
                .map(TestStationMeta::getStationName).collect(Collectors.toList());

        List<TestErrorDaily> result = new ArrayList<>();
        Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyList(factory, modelName, errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), moType)
                .stream().collect(Collectors.toMap(TestErrorDaily::getStationName, error -> error, (e1, e2) -> e1));

        result.addAll(errorMap.values());

        for (String station : stationList) {
            if (!errorMap.containsKey(station)) {
                TestErrorDaily error = new TestErrorDaily();
                error.setFactory(factory);
                error.setModelName(modelName);
                error.setGroupName(groupName);
                error.setStationName(station);
                result.add(error);
            }
        }

        result.sort(Comparator.comparing(TestErrorDaily::getTestFail, Comparator.reverseOrder()));

        return result;
    }

}
