package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.TestCycleTime;
import com.foxconn.fii.data.TestStationDailyReport;
import com.foxconn.fii.data.b04.model.Parameter;
import com.foxconn.fii.data.b04.repository.B04LogRepository;
import com.foxconn.fii.data.b04ds02.repository.B04DS02ErrorLogRepository;
import com.foxconn.fii.data.b06te.model.CopyIcData;
import com.foxconn.fii.data.b06te.repository.B06TestLogDataRepository;
import com.foxconn.fii.data.b06te.repository.CopyIcDataRepository;
import com.foxconn.fii.data.nbbtefii.repository.NbbTeFiiEquipmentRepository;
import com.foxconn.fii.data.primary.model.TrackingResponse;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.TemperatureDataRepository;
import com.foxconn.fii.data.primary.repository.TestCpkRepository;
import com.foxconn.fii.data.primary.repository.TestFactoryMetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestParameterRepository;
import com.foxconn.fii.data.primary.repository.TestPartErrorStatisticsRepository;
import com.foxconn.fii.data.primary.repository.TestPcEmailListRepository;
import com.foxconn.fii.data.primary.repository.TestPcasMetaRepository;
import com.foxconn.fii.data.primary.repository.TestSolutionHistoryRepository;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaNewRepository;
import com.foxconn.fii.data.primary.repository.TestStationEquipmentRepository;
import com.foxconn.fii.data.primary.repository.TestStationErrorReportRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingHistoryRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.receiver.test.service.TemperatureDeviceService;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestLineService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.receiver.test.service.TestSolutionService;
import com.foxconn.fii.receiver.test.service.TestStationService;
import com.foxconn.fii.receiver.test.service.TestTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestApiController {

    @Autowired
    private TestFactoryMetaRepository testFactoryMetaRepository;

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestStationService testStationService;

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private TestSolutionService testSolutionService;

    @Autowired
    private B04LogRepository b04LogRepository;

    @Autowired
    private TestResourceService testResourceService;

    @Autowired
    private TestParameterRepository testParameterRepository;

    @Autowired
    private TestStationEquipmentRepository testStationEquipmentRepository;

    @Autowired
    private TestRepairSerialErrorService testRepairSerialErrorService;

    @Autowired
    private TestLineService testLineService;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TemperatureDeviceService temperatureDeviceService;

    @Autowired
    private TemperatureDataRepository temperatureDataRepository;

    @Autowired
    private B04DS02ErrorLogRepository b04DS02ErrorLogRepository;

    @Autowired
    private NbbTeFiiEquipmentRepository nbbTeFiiEquipmentRepository;

    @Autowired
    private B06TestLogDataRepository b06TestLogDataRepository;

    @Autowired
    private TestPcasMetaRepository testPcasMetaRepository;

    @Autowired
    private TestPcEmailListRepository testPcEmailListRepository;

    @Autowired
    private CopyIcDataRepository copyIcDataRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestTrackingService testTrackingService;

    @Autowired
    private TestTrackingHistoryRepository testTrackingHistoryRepository;

    // BEGIN Giang modified
    @Autowired
    private TestCpkRepository testCpkRepository;
    // END Giang modified

    @Value("${path.data}")
    private String dataPath;

    @Autowired
    private TestPartErrorStatisticsRepository testPartErrorStatisticsRepository;

    @Autowired
    private TestStationErrorReportRepository testStationErrorReportRepository;

    @Autowired
    private TestSolutionHistoryRepository testSolutionHistoryRepository;

    @Autowired
    private TestSolutionMetaNewRepository testSolutionMetaNewRepository;

    /**
     * SFC
     */
    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;

    /**
     * COMMON
     */

    @RequestMapping("/factory")
    public List<TestFactoryMeta> getFactoryMetaList() {
        List<TestFactoryMeta> factoryMetaList = testFactoryMetaRepository.findAll();
        factoryMetaList.sort(Comparator.comparing(TestFactoryMeta::getFactory));
        return factoryMetaList;
    }

    @RequestMapping("/model")
    public List<TestModelMeta> getModelMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {

        try {
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

            if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory)) {
                return testModelService.getModelMetaList(factory, parameter)
                        .stream()
                        .filter(meta -> meta.getUpdatedAt().after(dailyTimeSpan.getStartDate()))
                        .sorted(Comparator.comparing(TestModelMeta::getUpdatedAt, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
                List<TestModelMeta> modelList = testModelService.getModelMetaList(factory, null);

                List<String> modelOnline = testModelService.getModelMetaOnlineList(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
                modelList.sort(Comparator.comparingInt(model -> {
                    int index = modelOnline.indexOf(model.getModelName());
                    return index == -1 ? 1000 : index;
                }));

                return modelList;
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            List<String> modelList;

            if (!StringUtils.isEmpty(timeSpan)) {
                modelList = sfcSmtGroupRepository.getModelNameByWorkDateBetween(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
//                modelList.addAll(sfcTestGroupRepository.getModelNameByWorkDateBetween(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate()));
            } else if (StringUtils.isEmpty(workDate)) {
                modelList = sfcSmtGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getShiftType());
//                modelList.addAll(sfcTestGroupRepository.getModelNameByWorkDateAndShiftType(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getShiftType()));
            } else {
                Date date = df.parse(workDate);
                modelList = sfcSmtGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, date, shiftType);
//                modelList.addAll(sfcTestGroupRepository.getModelNameByWorkDateAndShiftType(factory, date, shiftType));
            }

            if (StringUtils.isEmpty(stage)) {
                if (StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) {
                    return testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory)
                            .stream().map(TestModelMeta::getModelName)
                            .filter(modelList::contains)
                            .map(model -> {
                                TestModelMeta modelMeta = new TestModelMeta();
                                modelMeta.setModelName(model);
                                return modelMeta;
                            })
                            .collect(Collectors.toList());
                }
                return testModelMetaRepository.findModelByFactoryAndCustomer(factory, customer)
                        .stream().filter(modelList::contains)
                        .map(model -> {
                            TestModelMeta modelMeta = new TestModelMeta();
                            modelMeta.setModelName(model);
                            return modelMeta;
                        })
                        .collect(Collectors.toList());
            }

            if (StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) {
                List<TestModelMeta> modelMetaList = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory)
                        .stream()
                        .filter(model -> stage.equalsIgnoreCase(model.getStage()) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .collect(Collectors.toList());
                return modelMetaList.stream()
                        .map(TestModelMeta::getModelName)
                        .filter(modelList::contains)
                        .map(model -> {
                            TestModelMeta modelMeta = new TestModelMeta();
                            modelMeta.setModelName(model);
                            return modelMeta;
                        })
                        .collect(Collectors.toList());
            }

            if ("SMT".equalsIgnoreCase(stage)) {
                return testModelMetaRepository.findModelByFactoryAndCustomerAndStage(factory, customer, "SMA")
                        .stream().filter(modelList::contains)
                        .map(model -> {
                            TestModelMeta modelMeta = new TestModelMeta();
                            modelMeta.setModelName(model);
                            return modelMeta;
                        })
                        .collect(Collectors.toList());
            }
            return testModelMetaRepository.findModelByFactoryAndCustomerAndStage(factory, customer, stage)
                    .stream().filter(modelList::contains)
                    .map(model -> {
                        TestModelMeta modelMeta = new TestModelMeta();
                        modelMeta.setModelName(model);
                        return modelMeta;
                    })
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("### getModel error", e);
            return Collections.emptyList();
        }
    }

    @RequestMapping("/group")
    public List<TestGroupMeta> getGroupMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam String modelName,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {

        if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory)) {
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
            return testGroupService.getGroupMetaList(factory, modelName, parameter)
                    .stream()
                    .filter(meta -> (meta.getUpdatedAt() != null && meta.getUpdatedAt().after(dailyTimeSpan.getStartDate()) && meta.getVisible()))
                    .sorted(Comparator.comparing(TestGroupMeta::getStep))
                    .collect(Collectors.toList());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date;
                ShiftType shift;
                if (!StringUtils.isEmpty(timeSpan)) {
                    TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
                    date = fullTimeSpan.getStartDate();
                    shift = fullTimeSpan.getShiftType();
                } else if (StringUtils.isEmpty(workDate)) {
                    TimeSpan fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                    date = fullTimeSpan.getStartDate();
                    shift = fullTimeSpan.getShiftType();
                } else {
                    date = df.parse(workDate);
                    shift = shiftType;
                }

                TestModelMeta modelMeta = testModelService.getModelMeta(factory, modelName);

                List<String> groupMetaList;
                if (modelMeta != null) {
                    groupMetaList = testGroupService.getGroupMetaList(factory, modelMeta.getCustomer(), modelMeta.getStage(), modelName, parameter)
                            .stream().filter(TestGroupMeta::getVisible).map(TestGroupMeta::getGroupName).collect(Collectors.toList());
                } else {
                    groupMetaList = new ArrayList<>();
                }

                return sfcSmtGroupRepository.getGroupNameByModelNameAndWorkDateAndShiftType(factory, customer, modelName, date, shift)
                        .stream()
                        .filter(groupMetaList::contains)
                        .map(group -> {
                            TestGroupMeta groupMeta = new TestGroupMeta();
                            groupMeta.setGroupName(group);
                            return groupMeta;
                        })
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                log.error("### getGroup error", e);
                return Collections.emptyList();
            }
        }
    }

    @RequestMapping("/station")
    public List<TestStationMeta> getStationMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam String modelName,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {
        if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory)) {
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
            List<TestStationMeta> stationMetaList = new ArrayList<>();
            if (StringUtils.isEmpty(groupName)) {
                List<TestGroupMeta> groupMetaList = testGroupService.getGroupMetaList(factory, modelName, parameter);
                for (TestGroupMeta groupMeta : groupMetaList) {
                    if (groupMeta.getVisible()) {
                        stationMetaList.addAll(testStationService.getStationMetaList(factory, modelName, groupMeta.getGroupName()));
                    }
                }
            } else {
                stationMetaList = testStationService.getStationMetaList(factory, modelName, groupName);
            }

            return stationMetaList.stream()
                    .filter(meta -> (meta.getUpdatedAt() != null && meta.getUpdatedAt().after(dailyTimeSpan.getStartDate())))
                    .sorted(Comparator.comparing(TestStationMeta::getStationName))
                    .collect(Collectors.toList());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date;
                ShiftType shift;
                if (!StringUtils.isEmpty(timeSpan)) {
                    TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
                    date = fullTimeSpan.getStartDate();
                    shift = fullTimeSpan.getShiftType();
                } else if (StringUtils.isEmpty(workDate)) {
                    TimeSpan fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                    date = fullTimeSpan.getStartDate();
                    shift = fullTimeSpan.getShiftType();
                } else {
                    date = df.parse(workDate);
                    shift = shiftType;
                }
                return sfcTestGroupRepository.getStationNameByModelNameAndWorkDateAndShiftType(factory, customer, modelName, groupName, date, shift)
                        .stream()
                        .map(station -> {
                            TestStationMeta stationMeta = new TestStationMeta();
                            stationMeta.setStationName(station);
                            return stationMeta;
                        })
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                log.error("### getGroup error", e);
                return Collections.emptyList();
            }
        }
    }

    @RequestMapping("/errorCode")
    public List<String> getErrorMetaList(@RequestParam String factory, @RequestParam String modelName) {
        Set<String> errorSet = new HashSet<>(testSolutionService.getErrorCodeList(factory, modelName));
        errorSet.addAll(testErrorService.getErrorCodeList(factory, modelName));
        return new ArrayList<>(errorSet);
    }

    @GetMapping("/solution")
    public List<TestSolutionMeta> getSolutionByErrorCode(
            @RequestParam String factory, @RequestParam String modelName, @RequestParam String errorCode) {
        List<TestSolutionMeta> result = testSolutionService.getSolutionList(factory, modelName, errorCode);
        result.sort(Comparator.comparing(TestSolutionMeta::getOfficial, Comparator.reverseOrder())
                .thenComparing(TestSolutionMeta::getNumberSuccess, Comparator.reverseOrder()));
        return result.size() <= 5 ? result : result.subList(0, 5);
    }

    @GetMapping("/solution/{solutionId}")
    public TestSolutionMeta getSolutionById(@PathVariable Integer solutionId) {
        return testSolutionService.getSolution(solutionId);
    }

    @PostMapping("/solution/mark-official/{solutionId}")
    public Boolean markOfficial(@PathVariable Integer solutionId) {
        return testSolutionService.markOfficial(solutionId);
    }

    @RequestMapping("/model/parameter")
    public List<Parameter> getParametersByModelName(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam String groupName) {

        if ("B04".equalsIgnoreCase(factory)) {
            TestModelMeta modelMeta = testModelService.getModelMeta(factory, modelName);
            if (modelMeta == null || StringUtils.isEmpty(modelMeta.getTableName())) {
                return Collections.emptyList();
            }

            return b04LogRepository.getParametersByModelName(modelMeta.getTableName());
        }

        return testParameterRepository.findAllByFactoryAndModelNameAndGroupName(factory, modelName, groupName)
                .stream().sorted(Comparator.comparing(TestParameter::getParameters))
                .map(parameter -> Parameter.of(parameter.getParameters(), parameter.getLowSpec(), parameter.getHighSpec()))
                .collect(Collectors.toList());
    }

    @GetMapping("/employee")
    public TestResource getInformationEmployee(String employeeNo) {
        return testResourceService.findByEmployeeNo(employeeNo);
    }

    /**
     * FUNCTION
     */

    @RequestMapping("/model/updatedaily")
    public List<TestModel> getDailyModelUpdate(
            @RequestParam String factory,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean customer,
            @RequestParam(required = false, defaultValue = "ALL") MoType moType) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        return testModelService.getAllModelDaily(factory, customerName, fullTimeSpan, customer, moType);
    }

    @RequestMapping("/stationEquipment")
    public List<TestStationEquipment> getStationEquipmentList(@RequestParam String factory, String modelName, String groupName, String stationName) {
        return testStationEquipmentRepository.findAllByFactoryAndModelNameAndGroupNameAndStationName(factory, modelName, groupName, stationName);
    }

    @PostMapping("/upload")
    public String upload(String type, String[] captionFiles, MultipartFile[] uploadingFiles) {
        String subFolder = "file/";
        if ("image".equalsIgnoreCase(type)) {
            subFolder = "image/";
        }

        for (MultipartFile uploadedFile : uploadingFiles) {
            String fileName = System.currentTimeMillis() + '-' + new Random().nextInt(100) + ".jpg";
            File file = new File(dataPath + subFolder + fileName);

            try {
                uploadedFile.transferTo(file);
            } catch (IOException e) {
                log.error("### upload error", e);
            }
        }

        return "success";
    }

    @RequestMapping("/defectedIssue")
    public Map<String, Long> getDefectedIssue(String factory, String modelName, String groupName, String stationName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.MONTH, -1);
        fullTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> serialErrorMap = testRepairSerialErrorService.countByErrorCodeAndReason(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("UNDER REPAIR", 0L);
        result.put("NTF", 0L);
        result.put("PROCESS ISSUE", 0L);
        result.put("COMPONENT ISSUE", 0L);
        result.put("OTHER", 0L);

        for (Object[] value : serialErrorMap) {
            if (StringUtils.isEmpty(value[1])) {
                result.put("UNDER REPAIR", result.get("UNDER REPAIR") + (Long) value[2]);
                continue;
            }
            switch ((String) value[1]) {
                case "H003":
                    result.put("NTF", result.get("NTF") + (Long) value[2]);
                    break;
                case "B000":
                    result.put("COMPONENT ISSUE", result.get("COMPONENT ISSUE") + (Long) value[2]);
                    break;
                case "C001":
                    result.put("OTHER", result.get("OTHER") + (Long) value[2]);
                    break;
                default:
                    result.put("PROCESS ISSUE", result.get("PROCESS ISSUE") + (Long) value[2]);
            }
        }

        return result;
    }

    @RequestMapping("/ntfIssue")
    public Map<String, Long> getNtfIssue(String factory, String modelName, String groupName, String stationName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.MONTH, -1);
        fullTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> serialErrorMap = testRepairSerialErrorService.countByErrorCodeAndReason(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Long> map = new HashMap<>();

        for (Object[] value : serialErrorMap) {
            if ("H003".equals(value[1])) {
                String errorCode = (String) value[0];
                Long count = map.getOrDefault(errorCode, 0L) + (Long) value[2];
                map.put(errorCode, count);
            }
        }

        Map<String, Long> sorted = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return sorted;
    }

    @RequestMapping("/error/defectedIssue")
    public Map<String, Long> getDefectedIssueByError(String factory, String modelName, String errorCode, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.MONTH, -1);
        fullTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> serialErrorMap = testRepairSerialErrorService.countByReason(factory, modelName, "SI", errorCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("REPAIR", 0L);
        result.put("NTF", 0L);
        result.put("PROCESS ISSUE", 0L);
        result.put("COMPONENT ISSUE", 0L);
        result.put("OTHER", 0L);

        for (Object[] value : serialErrorMap) {
            if (StringUtils.isEmpty(value[0])) {
                result.put("REPAIR", result.get("REPAIR") + ((Number) value[1]).longValue());
                continue;
            }
            switch ((String) value[0]) {
                case "H003":
                    result.put("NTF", result.get("NTF") + ((Number) value[1]).longValue());
                    break;
                case "B000":
                    result.put("COMPONENT ISSUE", result.get("COMPONENT ISSUE") + ((Number) value[1]).longValue());
                    break;
                case "C001":
                    result.put("OTHER", result.get("OTHER") + ((Number) value[1]).longValue());
                    break;
                default:
                    result.put("PROCESS ISSUE", result.get("PROCESS ISSUE") + ((Number) value[1]).longValue());
            }
        }

        return result;
    }

    @RequestMapping("/station/ntfIssue")
    public Map<String, Long> getNtfByStation(String factory, String modelName, String errorCode, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.MONTH, -1);
        fullTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> serialErrorMap = testRepairSerialErrorService.countByStation(factory, modelName, "SI", errorCode, "H003", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Long> map = serialErrorMap.stream()
                .filter(objects -> !StringUtils.isEmpty(objects[1]))
                .collect(Collectors.toMap(
                        objects -> (String) objects[1],
                        objects -> ((Number) objects[2]).longValue(),
                        (o1, o2) -> o1,
                        HashMap::new));

        Map<String, Long> sorted = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return sorted;
    }

    @RequestMapping("/group/total")
    public List<TestGroupDaily> getGroupTotal(String factory, String modelName, String timeSpan, Boolean customer, Boolean includeStation, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        TestModelMeta modelMeta = testModelService.getModelMeta(factory, modelName);

        Map<String, TestGroupMeta> testGroupMetaMap = testGroupService.getGroupMetaList(
                factory, modelMeta != null ? modelMeta.getCustomer() : null, modelMeta != null ? modelMeta.getStage() : null, modelName, null)
                .stream().filter(TestGroupMeta::getVisible).collect(Collectors.toMap(TestGroupMeta::getGroupName, g -> g, (g1, g2) -> g1));

        List<TestGroupDaily> result = testGroupService.getGroupDailyList(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType);
        result = result.stream()
//                .filter(group -> !group.getGroupName().contains("UK") && !group.getGroupName().contains("UP") && !group.getGroupName().contains("OBA"))
                .filter(g -> testGroupMetaMap.containsKey(g.getGroupName()) && testGroupMetaMap.get(g.getGroupName()).getVisible())
                .collect(Collectors.toList());

        int output = -1;
        int outputFTRC = -1;
        int totalSF = 0;
        for (TestGroupDaily group : result) {
            if (output == -1 || group.getPass() < output) {
                output = group.getPass();
            }
            if ((group.getGroupName().startsWith("FT") || group.getGroupName().startsWith("RC")) && (outputFTRC == -1 || group.getPass() < outputFTRC)) {
                outputFTRC = group.getPass();
            }
            totalSF += group.getSecondFail();
        }
        if (outputFTRC > -1) {
            output = outputFTRC;
        }

        int iBase = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        List<Float> fixedRRList = Arrays.asList(1.91f, 2.0f, 1.96f, 1.85f, 2.03f, 1.8f, 1.88f);
        List<Float> fixedSFList = Arrays.asList(0.43f, 0.41f, 0.52f, 0.47f, 0.45f, 0.49f, 0.50f);

        int fakeSF = Math.round(output * fixedSFList.get(iBase % fixedSFList.size()) / 100);
        int remainSF = fakeSF;

        for (TestGroupDaily group : result) {
            if (includeStation == null || includeStation) {
                List<TestStationDaily> stationList = testStationService.getStationDailyList(factory, modelName, group.getGroupName(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType);
                stationList.sort(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()));
                group.setStationList(stationList);
            }

            if (customer != null && customer) {
                float retestRateTmp = group.getRetestRate();
                if (totalSF > fakeSF) {
                    if (remainSF > 0) {
                        group.setSecondFail((int) Math.round(Math.ceil(group.getSecondFail() * 1.0f / totalSF * fakeSF)));
                    } else {
                        group.setSecondFail(0);
                    }
                    remainSF -= group.getSecondFail();
                }

                if (retestRateTmp > 2 || ("WT".equalsIgnoreCase(group.getGroupName()) && group.getRetestRate() > 3f)) {
                    group.setFirstFail(Math.round(group.getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + group.getSecondFail());
                } else if (retestRateTmp == 0) {
                    group.setFirstFail(Math.round(group.getWip() * 0.2f / 100) + group.getSecondFail());
                } else {
                    group.setFirstFail(Math.round(group.getWip() * retestRateTmp / 100) + group.getSecondFail());
                }
            }

            Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyTopErrorCode(group.getFactory(), group.getModelName(), group.getGroupName(), null, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), 3, mode, moType);
            group.setErrorMetaMap(errorMap);
        }

//        result.sort(Comparator.comparing(TestGroupDaily::getRetestRate, Comparator.reverseOrder()));
        result.sort(Comparator.comparingInt(g -> {
            if (testGroupMetaMap.containsKey(g.getGroupName()) && testGroupMetaMap.get(g.getGroupName()).getStep() != null) {
                return testGroupMetaMap.get(g.getGroupName()).getStep();
            }
            return 1000;
        }));
        return result;
    }

    @RequestMapping("/station/total")
    public List<TestStationDaily> getStationTotal(String factory, String modelName, String groupName, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<TestStationDaily> result = testStationService.getStationDailyList(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType);
        for (TestStationDaily station : result) {
            Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyTopErrorCode(station.getFactory(), station.getModelName(), station.getGroupName(), station.getStationName(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), 3, mode, moType);
            station.setErrorMetaMap(errorMap);
        }

        result.sort(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()));
        return result;
    }

    @RequestMapping("/station/total/report")
    public List<TestStationDailyReport> getStationTotalReport(String factory, String modelName, String groupName, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }

        TestGroupMeta groupMeta = testGroupService.getGroupMeta(factory, modelName, groupName);

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<TestStationDaily> stationList = testStationService.getStationDailyList(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType);
        List<TestStationDailyReport> result = stationList.stream()
                .filter(station -> ((groupMeta == null && station.getRetestRate() > 3) || (groupMeta != null && station.getRetestRate() > groupMeta.getTargetRetestRate())) && station.getWip() > 30)
                .map(station -> {
                    TestStationDailyReport ins = new TestStationDailyReport();
                    BeanUtils.copyPropertiesIgnoreNull(station, ins);
                    TestTracking tracking = testTrackingService.getTracking(factory, modelName, groupName, station.getStationName(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
                    if (tracking != null) {
                        TrackingResponse trackingResponse = new TrackingResponse();
                        testTrackingHistoryRepository.findAllByTrackingId(tracking.getId())
                                .forEach(history -> TrackingResponse.loadHistory(trackingResponse, history));
                        ins.setRootCause(trackingResponse.getWhy());
                        ins.setAction(trackingResponse.getAction());
                        ins.setOwner(trackingResponse.getEmployee());
                    }
                    return ins;
                })
                .sorted(Comparator.comparing(TestStationDailyReport::getRetestRate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
//        for (TestStationDaily station : result) {
//            Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyTopErrorCode(station.getFactory(), station.getModelName(), station.getGroupName(), station.getStationName(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), 3, mode, moType);
//            station.setErrorMetaMap(errorMap);
//        }

        return result;
    }

    @RequestMapping("/station/error/report")
    public ListResponse<TestStationErrorReport> getStationErrorReportList(String factory, String modelName, String groupName, String timeSpan, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }

        TestGroupMeta groupMeta = testGroupService.getGroupMeta(factory, modelName, groupName);

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestStationErrorReport> reportHistoryMap = testStationErrorReportRepository.findByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(
                        station -> String.join("_", station.getModelName(), station.getGroupName(), station.getStationName()),
                        station -> station,
                        (s1, s2) -> s1));

        List<TestStationErrorReport> reportList = testStationService.getStationDailyList(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), mode, moType).stream()
                .filter(station -> ((groupMeta == null && station.getRetestRate() > 3) || (groupMeta != null && station.getRetestRate() > groupMeta.getTargetRetestRate())) && station.getWip() > 30)
                .map(station -> {
                    TestStationErrorReport report = new TestStationErrorReport();
                    BeanUtils.copyPropertiesIgnoreNull(station, report, "id");
                    String key = String.join("_", station.getModelName(), station.getGroupName(), station.getStationName());
                    if (reportHistoryMap.containsKey(key)) {
                        TestStationErrorReport reportHistory = reportHistoryMap.get(key);
                        report.setId(reportHistory.getId());
                        report.setRootCause(reportHistory.getRootCause());
                        report.setAction(reportHistory.getAction());
                        report.setDueDate(reportHistory.getDueDate());
                        report.setStatus(reportHistory.getStatus());
                        report.setAttachFile(reportHistory.getAttachFile());
                    }
                    return report;
                })
                .collect(Collectors.toList());

        return ListResponse.success(reportList);
    }

    @PostMapping("/station/error/report")
    public CommonResponse<Boolean> updateStationErrorReport(@ModelAttribute TestStationErrorReport report) {
        TimeSpan fullTimeSpan = TimeSpan.from(report.getTimeSpan(), TimeSpan.now(TimeSpan.Type.DAILY));

        if (StringUtils.isEmpty(report.getModelName()) ||
                (StringUtils.isEmpty(report.getErrorCode()) &&
                        (StringUtils.isEmpty(report.getGroupName()) || StringUtils.isEmpty(report.getStationName())))) {
            throw CommonException.of("Some fields is blank");
        }

        if (report.getUploadedFile() != null) {
            String originalFileName = report.getUploadedFile().getOriginalFilename();
            String fileName = System.currentTimeMillis() + "-" + new Random().nextInt(100);
            if (!StringUtils.isEmpty(originalFileName)) {
                if (originalFileName.contains(".")) {
                    fileName += "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                }
            }
            File file = new File(dataPath + "file/" + fileName);
            try {
                report.getUploadedFile().transferTo(file);
                report.setAttachFile("/ws-data/file/" + fileName);
            } catch (IOException e) {
                log.error("### upload error", e);
            }
        }

        if (report.getId() == 0) {
            TestStationErrorReport ins = new TestStationErrorReport();
            BeanUtils.copyPropertiesIgnoreNull(report, ins, "id");
            ins.setStartDate(fullTimeSpan.getStartDate());
            ins.setEndDate(fullTimeSpan.getEndDate());
            if (report.getStatus() == null) {
                ins.setStatus(TestStationErrorReport.Status.ON_GOING);
            }
            testStationErrorReportRepository.save(ins);
        } else {
            TestStationErrorReport ins = testStationErrorReportRepository.findById(report.getId())
                    .orElseThrow(() -> CommonException.of("Report {} not found", report.getId()));
            BeanUtils.copyPropertiesIgnoreNull(report, ins, "id");
            if (report.getStatus() == null) {
                report.setStatus(TestStationErrorReport.Status.ON_GOING);
            }
            testStationErrorReportRepository.save(ins);
        }

        return CommonResponse.success(true);
    }

    @RequestMapping("/line/status")
    public List<TestLineMeta> getLineStatus(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestLineMeta> lineMetaList = testLineService.getLineList(factory)
                .stream().collect(Collectors.toMap(
                        line -> line.getSectionName() + "_" + line.getLineName(),
                        line -> line,
                        (line, tmp) -> line));

        Map<String, Integer> planQtyByModelMap = testPlanService.getPlanQtyByModelMap(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.HOURLY);
        Map<String, List<TestGroup>> result = testGroupService.getGroupMapLineName(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        for (Map.Entry<String, List<TestGroup>> entry : result.entrySet()) {
            for (TestGroup group : entry.getValue()) {
                group.setPlan(planQtyByModelMap.getOrDefault(group.getModelName(), 0) * (int) ((fullTimeSpan.getEndDate().getTime() - fullTimeSpan.getStartDate().getTime()) / (60 * 60 * 1000)));
            }
            entry.getValue().sort(Comparator.comparing(TestGroup::getRetestRate, Comparator.reverseOrder()));
        }

        List<TestResource> resourceList = testResourceService.getResourceStatus(factory);

        Map<String, List<TestResource>> resourceMap = resourceList.stream().collect(Collectors.groupingBy(resource -> {
            if (resource.getTracking() == null || StringUtils.isEmpty(resource.getTracking().getLineName())) {
                return "";
            }
            return resource.getTracking().getLineName();
        }));

        lineMetaList.forEach((key, value) -> {
            if ("SI".equalsIgnoreCase(value.getSectionName())) {
                value.setGroupList(result.getOrDefault(key.split("_")[1], new ArrayList<>()));
                value.setResourceList(resourceMap.getOrDefault(key.split("_")[1], new ArrayList<>()));
            } else {
                value.setGroupList(new ArrayList<>());
                value.setResourceList(new ArrayList<>());
            }
        });

        TestLineMeta available = new TestLineMeta();
        available.setLineName("");
        available.setResourceList(resourceMap.getOrDefault("", new ArrayList<>()));
        lineMetaList.put("", available);

        return new ArrayList<>(lineMetaList.values());
    }

    @RequestMapping("/temperature")
    public List<TemperatureDevice> getTemperatureDevice(String location) {
        List<TemperatureDevice> deviceList;
        if (StringUtils.isEmpty(location)) {
            deviceList = temperatureDeviceService.findAll();
        } else {
            deviceList = temperatureDeviceService.findByLocation(location);
        }

        for (TemperatureDevice device : deviceList) {
            device.setLatestData(temperatureDataRepository.findTop1ByDeviceCodeOrderByCreatedAtDesc(device.getCode()));
        }

        return deviceList;
    }

    @PostMapping("/temperature")
    public Boolean createTemperatureDevice(@RequestBody TemperatureDevice temp) {
        TemperatureDevice tempTarget = temperatureDeviceService.findByCode(temp.getCode()).orElse(new TemperatureDevice());
        BeanUtils.copyPropertiesIgnoreNull(temp, tempTarget, "id");

        temperatureDeviceService.save(tempTarget);
        return true;
    }


    @GetMapping("/temperature/tracking/by/day")
    public ListResponse<TemperatureData> temperatureTrackingByDay(@RequestParam(required = false, defaultValue = "") String deviceCode,
                                                                  @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
        List<TemperatureData> list = temperatureDataRepository.getAllByCodeDeviceAndTimeBetween(deviceCode, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        return ListResponse.success(list);
    }

    @PutMapping("/temperature/{id}")
    public Boolean updateTemperatureDevice(@PathVariable("id") Integer id, @RequestBody TemperatureDevice tmp) {
        TemperatureDevice tempTarget = temperatureDeviceService.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("temperature id %d not found", id)));

        temperatureDeviceService.findByCode(tmp.getCode()).ifPresent(temp -> {
            if (temp.getId() != id) {
                throw CommonException.of("temperature code %s is exist", tmp.getCode());
            }
        });

        BeanUtils.copyPropertiesIgnoreNull(tmp, tempTarget, "id");
        temperatureDeviceService.save(tempTarget);
        return true;
    }

    @DeleteMapping("/temperature/{id}")
    public Boolean removeTemperatureDevice(@PathVariable("id") Integer id) {
        temperatureDeviceService.delete(id);
        return true;
    }

    @PostMapping("/temperature/layout")
    public Boolean uploadLayout(@RequestParam String location, @RequestPart MultipartFile uploadedFile) {
        String subFolder = "image/temperature";

        if (Files.notExists(Paths.get(dataPath + subFolder))) {
            try {
                Files.createDirectory(Paths.get(dataPath + subFolder));
            } catch (IOException e) {
                log.error("### upload error", e);
            }
        }

        Path path = Paths.get(dataPath + subFolder + "/" + location + ".jpg");
        File file = new File(path.toString());

        try {
            uploadedFile.transferTo(file);
        } catch (IOException e) {
            log.error("### upload error", e);
        }

        return true;
    }

    // BEGIN Giang modified
    @GetMapping("/b06/getdata/testcpk")
    public Object b06GetDataTestCpk(
            @RequestParam String modelName,
            @RequestParam String groupName,
            @RequestParam String stationName,
            @RequestParam String workDate,
            @RequestParam String shiftType) {
        String factory = "B06";
        TimeSpan timeSpan = TimeSpan.from(workDate.trim() + " " + shiftType.toUpperCase().trim(), TimeSpan.Type.DAILY);
        List<Map<String, Object>> queryData;
        if (!StringUtils.isEmpty(groupName)) {
            queryData = testCpkRepository.jpqlGetCpkAndSpec(factory, modelName, groupName, stationName, timeSpan.getStartDate(), timeSpan.getEndDate());
        } else {
            queryData = testCpkRepository.jpqlGetCpkAndSpecGroupNameIsNull(factory, modelName, stationName, timeSpan.getStartDate(), timeSpan.getEndDate());
        }

//        Map<String, Object> resultMeta = new LinkedHashMap<>();
//        resultMeta.put("factory", factory);
//        resultMeta.put("modelName", modelName);
//        resultMeta.put("groupName", groupName);
//        resultMeta.put("stationName", stationName);
//        resultMeta.put("workDate", workDate);
//        resultMeta.put("shiftType", shiftType.toUpperCase());
//        resultMeta.put("startTime", timeSpan.getStartDate());
//        resultMeta.put("endTime", timeSpan.getEndDate());
//
//        List<Map<String, Object>> resultData = new ArrayList<>();
//        for (Map<String, Object> queryDataPart : queryData) {
//            Map<String, Object> tmpObject = new LinkedHashMap<>();
//            tmpObject.put("parameter", ((TestCpk) queryDataPart.get("cpk")).getParameter());
//            tmpObject.put("cpk", ((TestCpk) queryDataPart.get("cpk")).getNewCpk());
//            tmpObject.put("variance", ((TestCpk) queryDataPart.get("cpk")).getVariance());
//            tmpObject.put("average", ((TestCpk) queryDataPart.get("cpk")).getAverage());
//            tmpObject.put("numberOfValue", ((TestCpk) queryDataPart.get("cpk")).getNumberOfValue());
//            tmpObject.put("lowSpec", ((TestParameter) queryDataPart.get("spec")).getLowSpec());
//            tmpObject.put("highSpec", ((TestParameter) queryDataPart.get("spec")).getHighSpec());
//            resultData.add(tmpObject);
//        }
//
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("meta", resultMeta);
//        result.put("data", resultData);

        String result = "";
        for (Map<String, Object> queryDataPart : queryData) {
            result += ((TestCpk) queryDataPart.get("cpk")).getParameter()
                    + ";" + ((TestCpk) queryDataPart.get("cpk")).getNewCpk()
                    + ";" + ((TestCpk) queryDataPart.get("cpk")).getVariance()
                    + ";" + ((TestCpk) queryDataPart.get("cpk")).getAverage()
                    + ";" + ((TestCpk) queryDataPart.get("cpk")).getNumberOfValue()
                    + ";" + ((TestParameter) queryDataPart.get("spec")).getLowSpec()
                    + ";" + ((TestParameter) queryDataPart.get("spec")).getHighSpec()
                    + "\n";
        }

        return result;
    }
    // END Giang modified

    @GetMapping("/cycle-time")
    public List<TestCycleTime> getCycleTimeList(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestCycleTime> cycleTimeList;
        if (Factory.B04.equalsIgnoreCase(factory)) {
            if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(groupName)) {
                cycleTimeList = b04DS02ErrorLogRepository.getCycleTimeByGroup(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            } else {
                cycleTimeList = b04DS02ErrorLogRepository.getCycleTimeByStation(modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            }
        } else if (Factory.NBB.equalsIgnoreCase(factory)) {
            if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(groupName)) {
                cycleTimeList = nbbTeFiiEquipmentRepository.getCycleTimeByGroup(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            } else {
                cycleTimeList = nbbTeFiiEquipmentRepository.getCycleTimeByStation(modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            }
        } else if (Factory.B06.equalsIgnoreCase(factory)) {
            if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(groupName)) {
                cycleTimeList = b06TestLogDataRepository.getCycleTimeByGroup(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
            } else {
                cycleTimeList = b06TestLogDataRepository.getCycleTimeByStation(modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
            }
        } else {
            cycleTimeList = new ArrayList<>();
        }

        Map<String, TestPlanMeta> planMap = testPlanService.getPlanList(factory, "SI", "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.DAILY)
                .stream().collect(Collectors.toMap(
                        TestPlanMeta::getModelName,
                        plan -> plan,
                        (p1, p2) -> {
                            p1.setPlan(p1.getPlan() + p2.getPlan());
                            p1.setDemand(p1.getDemand() + p2.getDemand());
                            return p1;
                        },
                        HashMap::new));

        Map<String, TestPlanTmp> tmpPlanMap = testPlanService.getTmpPlanList(factory, "SI", "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanTmp.Type.DAILY)
                .stream().collect(Collectors.toMap(
                        TestPlanTmp::getModelName,
                        plan -> plan,
                        (p1, p2) -> {
                            p1.setPlan(p1.getPlan() + p2.getPlan());
                            p1.setDemand(p1.getDemand() + p2.getDemand());
                            return p1;
                        },
                        HashMap::new));

        Map<String, TestPcasMeta> pcasMap = testPcasMetaRepository.findByFactory(factory)
                .stream().collect(Collectors.toMap(
                        pcas -> pcas.getModelName() + "_" + pcas.getGroupName(),
                        pcas -> pcas,
                        (p1, p2) -> p1
                ));

        Map<String, Map<String, TestGroupDaily>> outputMap = testGroupService.getGroupDailyMapByModel(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), MoType.ALL);

        cycleTimeList.forEach(cycle -> {
            TestPlanMeta planMeta = planMap.getOrDefault(cycle.getModelName(), new TestPlanMeta());
            cycle.setRealPlan(planMeta.getPlan());

            TestPlanTmp tmpPlan = tmpPlanMap.getOrDefault(cycle.getModelName(), new TestPlanTmp());
            cycle.setPlan(tmpPlan.getPlan());

            TestPcasMeta pcas = pcasMap.get(cycle.getModelName() + "_" + cycle.getGroupName());
            if (pcas != null) {
                cycle.setCycle(pcas.getCycleTime());
                cycle.setOperationTime(pcas.getOperatorTime());
                cycle.setPingTime(pcas.getPingTime());
                cycle.setTesterExist(pcas.getTesterExist());
                cycle.setWorkTime(pcas.getWorkTime());
            }
            if (outputMap.containsKey(cycle.getModelName()) && outputMap.get(cycle.getModelName()).containsKey(cycle.getGroupName())) {
                cycle.setRealOutput(outputMap.get(cycle.getModelName()).get(cycle.getGroupName()).getPass());
            }
        });
        cycleTimeList.sort(Comparator.comparing(TestCycleTime::getModelName).thenComparing(TestCycleTime::getGroupName));
        return cycleTimeList;
    }

    @RequestMapping("/copyic-data/all")
    public List<CopyIcData> getAllCopyIcData(String factory, String modelName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<CopyIcData> result = new ArrayList<>();
        List<CopyIcData> data = copyIcDataRepository.findByTimeBetween(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Map<String, Map<String, CopyIcData>>> map = data.stream()
                .collect(Collectors.groupingBy(CopyIcData::getModel,
                        Collectors.groupingBy(CopyIcData::getMachine,
                                Collectors.toMap(
                                        copyIc -> String.join(";", copyIc.getPartNumber(), copyIc.getChecksum(), copyIc.getBinFile()),
                                        copyIc -> copyIc,
                                        (c1, c2) -> c2.getTime().after(c1.getTime()) ? c2 : c1))));

        Map<String, Integer> planMap = testPlanService.getPlanList(factory, "PTH", "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestPlanMeta.Type.DAILY)
                .stream().collect(Collectors.toMap(TestPlanMeta::getModelName, TestPlanMeta::getPlan, Integer::sum));

        for (Map.Entry<String, Map<String, Map<String, CopyIcData>>> modelEntry : map.entrySet()) {
            CopyIcData tmp = null;
            for (Map.Entry<String, Map<String, CopyIcData>> machineEntry : modelEntry.getValue().entrySet()) {
                List<CopyIcData> dataList = new ArrayList<>(machineEntry.getValue().values());
                int i = 0;
                if (tmp == null) {
                    tmp = new CopyIcData();
                    BeanUtils.copyPropertiesIgnoreNull(dataList.get(0), tmp);
                    i = 1;
                }
                for (; i < dataList.size(); i++) {
                    if (tmp.getPassQty() != null && dataList.get(i).getPassQty() != null) {
                        tmp.setPassQty(tmp.getPassQty() + dataList.get(i).getPassQty());
                    }
                    if (tmp.getFailQty() != null && dataList.get(i).getFailQty() != null) {
                        tmp.setFailQty(tmp.getFailQty() + dataList.get(i).getFailQty());
                    }
                    if (tmp.getTotalQty() != null && dataList.get(i).getTotalQty() != null) {
                        tmp.setTotalQty(tmp.getTotalQty() + dataList.get(i).getTotalQty());
                    }
                }
            }
            if (tmp != null) {
                tmp.setMachineNumber(modelEntry.getValue().size());
                tmp.setPlan(planMap.getOrDefault(tmp.getModel(), 0));
                result.add(tmp);
            }
        }

        return result;
    }

    @RequestMapping("/copyic-data")
    public List<CopyIcData> getCopyIcData(String factory, String modelName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<CopyIcData> result = new ArrayList<>();
        List<CopyIcData> data = copyIcDataRepository.findByModelAndTimeBetween(modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Map<String, Map<String, CopyIcData>>> map = data.stream()
                .collect(Collectors.groupingBy(CopyIcData::getModel,
                        Collectors.groupingBy(CopyIcData::getMachine,
                                Collectors.toMap(
                                        copyIc -> String.join(";", copyIc.getPartNumber(), copyIc.getChecksum(), copyIc.getBinFile()),
                                        copyIc -> copyIc,
                                        (c1, c2) -> c2.getTime().after(c1.getTime()) ? c2 : c1))));

        map.forEach((model, machineList) ->
                machineList.forEach((machine, dataMap) ->
                        dataMap.forEach((key, copyIc) ->
                                result.add(copyIc)
                        )
                )
        );

        return result;
    }

    @RequestMapping("/model/plan/tmp")
    public List<TestPlanTmp> getTmpPlan(String factory, String sectionName, String modelName, TestPlanTmp.Type type, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        if (type == TestPlanTmp.Type.MONTHLY) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            fullTimeSpan.setStartDate(calendar.getTime());
        }
        return testPlanService.getTmpPlanList(factory, sectionName, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), type);
    }

    @PostMapping("/model/plan/tmp")
    public Boolean saveTmpPlan(@RequestBody TestPlanTmp tmpPlan) {
        testPlanService.savePlanTmp(tmpPlan);
        return true;
    }

    @PutMapping("/model/plan/tmp/{id}")
    public Boolean savePlanModelName(@PathVariable int id, @RequestBody TestPlanTmp tmpPlan) {
        TestPlanTmp plan = testPlanService.findTmpPlanById(id)
                .orElseThrow(() -> CommonException.of(String.format("test plan meta %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(tmpPlan, plan, "id");
        testPlanService.savePlanTmp(plan);

        return true;
    }

    @DeleteMapping("/model/plan/tmp/{id}")
    public Boolean deletePlan(@PathVariable int id) {
        TestPlanTmp plan = testPlanService.findTmpPlanById(id)
                .orElseThrow(() -> CommonException.of(String.format("test plan meta %d not found", id)));

        testPlanService.deleteTmpPlanById(id);

        return true;
    }

    @GetMapping("/part-error-statistics")
    public ListResponse<TestPartErrorStatistics> getPartErrorStatisticsList(String factory, String timeSpan) {
        TimeSpan timeSpanFull = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<TestPartErrorStatistics> statisticsList = testPartErrorStatisticsRepository.findByFactoryAndWorkDateBetween(factory, timeSpanFull.getStartDate(), timeSpanFull.getEndDate());

        Map<String, Integer> workDateMap = new TreeMap<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Map<String, TestPartErrorStatistics> map = statisticsList.stream().collect(Collectors.toMap(
                statistics -> statistics.getFactory() + "_" + statistics.getModelName() + "_" + statistics.getPartName() + "_" + statistics.getRootCause(),
                statistics -> {
                    String key = df.format(statistics.getWorkDate()) + " " + statistics.getShiftType();
                    workDateMap.put(key, 0);
                    statistics.getWorkDateMap().put("TOTAL", statistics.getQuantity());
                    statistics.getWorkDateMap().put(key, statistics.getQuantity());
                    return statistics;
                },
                (s1, s2) -> {
                    String key = df.format(s2.getWorkDate()) + " " + s2.getShiftType();
                    workDateMap.put(key, 0);
                    s1.getWorkDateMap().put("TOTAL", s1.getWorkDateMap().getOrDefault("TOTAL", 0) + s2.getQuantity());
                    s1.getWorkDateMap().put(key, s2.getQuantity());
                    return s1;
                }));

        TestPartErrorStatistics header = new TestPartErrorStatistics();
        header.setPartName("HEADER");
        header.setWorkDateMap(workDateMap);

        List<TestPartErrorStatistics> result = new ArrayList<>();
        result.add(header);
        result.addAll(map.values());

        return ListResponse.success(result);
    }

    @PostMapping("/part-error-statistics")
    public CommonResponse<Boolean> updatePartErrorStatisticsList(TestPartErrorStatistics partErrorStatistics) {
        TestPartErrorStatistics partError = testPartErrorStatisticsRepository.findByFactoryAndModelNameAndPartNumberAndWorkDateAndShiftType(
                partErrorStatistics.getFactory(), partErrorStatistics.getModelName(), partErrorStatistics.getPartNumber(), partErrorStatistics.getWorkDate(), partErrorStatistics.getShiftType());

        if (partError == null) {
            partError = new TestPartErrorStatistics();
            BeanUtils.copyPropertiesIgnoreNull(partErrorStatistics, partError);
        } else {
            BeanUtils.copyPropertiesIgnoreNull(partErrorStatistics, partError, "id");
        }
        testPartErrorStatisticsRepository.save(partError);

        return CommonResponse.success(true);
    }

    @DeleteMapping("/part-error-statistics/{id}")
    public CommonResponse<Boolean> deletePartErrorStatisticsList(@PathVariable Integer id) {
        testPartErrorStatisticsRepository.deleteById(id);

        return CommonResponse.success(true);
    }

    @GetMapping("/model-error")
    public ListResponse<String> getListModelHasError(String factory, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        List<String> modelList = testErrorService.getModelErrorList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        return ListResponse.success(modelList);
    }

    @GetMapping("/group-error")
    public ListResponse<String> getListGroupHasError(String factory, String modelName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

//        List<TestErrorDaily> errorDailyList = testErrorService.getErrorDailyList(factory, modelName, null, null, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        List<String> stationList = errorDailyList.stream().map(TestErrorDaily::getGroupName).distinct().collect(Collectors.toList());
        List<String> groupList = testErrorService.getGroupErrorList(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        return ListResponse.success(groupList);
    }

    @GetMapping("/station-error")
    public ListResponse<String> getListStationHasError(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

//        List<TestErrorDaily> errorDailyList = testErrorService.getErrorDailyList(factory, modelName, groupName, null, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        List<String> stationList = errorDailyList.stream().map(TestErrorDaily::getStationName).distinct().collect(Collectors.toList());
        List<String> stationList = testErrorService.getStationErrorList(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        return ListResponse.success(stationList);
    }

    @GetMapping("/solution/root-cause")
    public ListResponse<String> getRootCauseList(String factory, String modelName, String errorCode) {
        TestModelMeta modelMeta = testModelMetaRepository.findByFactoryAndModelName(factory, modelName);

        List<String> rootCauseList = testSolutionMetaNewRepository.findAllByFactoryAndModelNameAndErrorCode(factory, modelName, errorCode)
                .stream().map(TestSolutionMetaNew::getRootCause).distinct().collect(Collectors.toList());

        if (!StringUtils.isEmpty(modelMeta.getCustomer())) {
            rootCauseList.addAll(testSolutionMetaNewRepository.findAllByFactoryAndCustomerAndErrorCode(factory, modelMeta.getCustomer(), errorCode)
                    .stream().map(TestSolutionMetaNew::getRootCause).distinct().collect(Collectors.toList()));
        }

        return ListResponse.success(rootCauseList);
    }

    @GetMapping("/solution/action")
    public ListResponse<TestSolutionMetaNew> getRootCauseList(String factory, String modelName, String errorCode, String rootCause) {
        TestModelMeta modelMeta = testModelMetaRepository.findByFactoryAndModelName(factory, modelName);

        List<TestSolutionMetaNew> solutionList = testSolutionMetaNewRepository.findAllByFactoryAndModelNameAndErrorCodeAndRootCause(factory, modelName, errorCode, rootCause);

        if (!StringUtils.isEmpty(modelMeta.getCustomer())) {
            solutionList.addAll(testSolutionMetaNewRepository.findAllByFactoryAndCustomerAndErrorCodeAndRootCause(factory, modelMeta.getCustomer(), errorCode, rootCause));
        }

        return ListResponse.success(solutionList);
    }

    @GetMapping("/error-analysis")
    public ListResponse<TestSolutionHistory> getErrorAnalysisList(
            String factory, String modelName, String groupName, String stationName, String timeSpan) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestErrorMeta> errorMetaMap = testErrorService.getErrorMetaList(factory, "")
                .stream().collect(Collectors.toMap(TestErrorMeta::getErrorCode, e -> e, (e1, e2) -> e1));

        Map<String, TestSolutionHistory> solutionHistoryMap = testSolutionHistoryRepository.findByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(
                factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(TestSolutionHistory::getErrorCode, history -> history, (h1, h2) -> h1));

        List<TestSolutionHistory> result = new ArrayList<>();
        testErrorService.getErrorDailyList(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(error -> {
                    TestErrorMeta errorMeta = errorMetaMap.get(error.getErrorCode());
                    TestSolutionHistory history;
                    if (solutionHistoryMap.containsKey(error.getErrorCode())) {
                        history = solutionHistoryMap.get(error.getErrorCode());
                    } else {
                        history = new TestSolutionHistory();
                        BeanUtils.copyPropertiesIgnoreNull(error, history);
                    }
                    if (errorMeta != null) {
                        history.setErrorDescription(errorMeta.getDescription());
                        history.setComponent(errorMeta.getComponent());
                    }
                    result.add(history);
                });

        return ListResponse.success(result);
    }

    @PostMapping("/error-analysis")
    public CommonResponse<Boolean> saveErrorAnalysis(@RequestBody TestSolutionHistory history) {
        if (history.getSolution() == null) {
            throw CommonException.of("Save error analysis error cause solution is null");
        }

        if (history.getSolution().getId() == 0) {
            TestModelMeta modelMeta = testModelMetaRepository.findByFactoryAndModelName(history.getFactory(), history.getModelName());

            TestSolutionMetaNew existSolution = testSolutionMetaNewRepository.findByFactoryAndModelNameAndErrorCodeAndRootCauseAndAction(
                    history.getFactory(), history.getModelName(), history.getErrorCode(), history.getSolution().getRootCause(), history.getSolution().getAction());

            if (existSolution == null) {
                List<TestSolutionMetaNew> solutionList = testSolutionMetaNewRepository.findByFactoryAndCustomerAndErrorCodeAndRootCauseAndAction(
                        history.getFactory(), modelMeta.getCustomer(), history.getErrorCode(), history.getSolution().getRootCause(), history.getSolution().getAction());
                if (StringUtils.isEmpty(modelMeta.getCustomer()) || solutionList.isEmpty()) {
                    existSolution = new TestSolutionMetaNew();
                    existSolution.setFactory(history.getFactory());
                    existSolution.setCustomer(modelMeta.getCustomer());
                    existSolution.setModelName(history.getModelName());
                    existSolution.setErrorCode(history.getErrorCode());
                    existSolution.setRootCause(history.getSolution().getRootCause());
                    existSolution.setAction(history.getSolution().getAction());
                    testSolutionMetaNewRepository.save(existSolution);
                } else {
                    existSolution = solutionList.get(0);
                }
            }
            history.getSolution().setId(existSolution.getId());
        }

        if (history.getId() > 0) {
            TestSolutionHistory existHistory = testSolutionHistoryRepository.findById(history.getId())
                    .orElseThrow(() -> CommonException.of("Test Solution History {} not found", history.getId()));
            BeanUtils.copyPropertiesIgnoreNull(history, existHistory, "id");
            testSolutionHistoryRepository.save(existHistory);
        } else {
            TimeSpan timeSpan = TimeSpan.from(history.getTimeSpan(), TimeSpan.now(TimeSpan.Type.DAILY));
            Optional<TestSolutionHistory> optional = testSolutionHistoryRepository.findByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateAndEndDate(
                    history.getFactory(), history.getModelName(), history.getGroupName(), history.getStationName(), history.getErrorCode(), timeSpan.getStartDate(), timeSpan.getEndDate());
            if (optional.isPresent()) {
                BeanUtils.copyPropertiesIgnoreNull(history, optional.get(), "id");
                testSolutionHistoryRepository.save(optional.get());
            } else {
                history.setStartDate(timeSpan.getStartDate());
                history.setEndDate(timeSpan.getEndDate());
                testSolutionHistoryRepository.save(history);
            }
        }

        return CommonResponse.success(true);
    }
}
