package com.foxconn.fii.receiver.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.common.utils.CommonUtils;
import com.foxconn.fii.common.utils.ExcelUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.b04.model.Parameter;
import com.foxconn.fii.data.b04.repository.B04LogRepository;
import com.foxconn.fii.data.b06te.repository.B06TestLogRepository;
import com.foxconn.fii.data.nbbtefii.repository.NbbTestLogRepository;
import com.foxconn.fii.data.primary.model.CpkFromDatabaseRequest;
import com.foxconn.fii.data.primary.model.CpkFromFileRequest;
import com.foxconn.fii.data.primary.model.SpcData;
import com.foxconn.fii.data.primary.model.entity.TestCpk;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestParameter;
import com.foxconn.fii.data.primary.model.entity.TestReadLogConfig;
import com.foxconn.fii.data.primary.repository.TestCpkRepository;
import com.foxconn.fii.data.primary.repository.TestParameterRepository;
import com.foxconn.fii.data.primary.repository.TestReadLogConfigRepository;
import com.foxconn.fii.data.s03te.repository.S03TestLogRepository;
import com.foxconn.fii.highcharts.HighChartsService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/station")
public class TestCpkController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private B04LogRepository b04LogRepository;

    @Autowired
    private TestCpkRepository testCpkRepository;

    @Autowired
    private TestParameterRepository testParameterRepository;

    @Autowired
    private B06TestLogRepository b06TestLogRepository;

    @Autowired
    private NbbTestLogRepository nbbTestLogRepository;

    @Autowired
    private S03TestLogRepository s03TestLogRepository;

    @Autowired
    private TestReadLogConfigRepository testReadLogConfigRepository;

    @Autowired
    private HighChartsService highChartsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${path.data}")
    private String dataPath;

    @RequestMapping("/read-log-config-list")
    public ListResponse<TestReadLogConfig> getReadLogConfigList(String factory, String modelName) {
        List<TestReadLogConfig> readLogConfigList = testReadLogConfigRepository.findByFactoryAndModelName(factory, modelName);
        return ListResponse.success(readLogConfigList);
    }

    @RequestMapping("/read-log-config/{id}")
    public CommonResponse<TestReadLogConfig> getReadLogConfig(@PathVariable Long id) {
        TestReadLogConfig readLogConfig = testReadLogConfigRepository.findById(id)
                .orElseThrow(() -> CommonException.of("Read log config error cause {} is not found", id));

        if (StringUtils.isEmpty(readLogConfig.getSampleFile())) {
            throw CommonException.of("Read log config error cause {} has sample file path blank", id);
        }

        String fileName = "";
        try {
            fileName = readLogConfig.getSampleFile().replace("/ws-data/file/", "");
            List<String> sampleFileLineList = Files.readAllLines(Paths.get(dataPath + "file/" + fileName));
            readLogConfig.setSampleFileLineList(sampleFileLineList);
            return CommonResponse.success(readLogConfig);
        } catch (Exception e) {
            throw CommonException.of("Read log config error cause read file {} got exception", fileName);
        }
    }

    @PostMapping("/read-log-config/request")
    public CommonResponse<Boolean> requestReadLogConfig(@ModelAttribute TestReadLogConfig readLogConfig) {
        if (readLogConfig.getFile() == null) {
            throw CommonException.of("Request read log config error cause file is blank");
        }

        TestReadLogConfig existConfig = testReadLogConfigRepository.findByFactoryAndModelNameAndGroupName(
                readLogConfig.getFactory(), readLogConfig.getModelName(), readLogConfig.getGroupName())
                .orElse(new TestReadLogConfig());

        BeanUtils.copyPropertiesIgnoreNull(readLogConfig, existConfig, "id", "pattern");

        String fileExtension = CommonUtils.getExtension(readLogConfig.getFile().getOriginalFilename());
        String fileName = System.currentTimeMillis() + '-' + new Random().nextInt(100) + "." + fileExtension;
        File file = new File(dataPath + "file/" + fileName);
        try {
            readLogConfig.getFile().transferTo(file);
        } catch (IOException e) {
            log.error("### upload error", e);
        }
        existConfig.setSampleFile("/ws-data/file/" + fileName);
        existConfig.setPatternConverted(false);

        testReadLogConfigRepository.save(existConfig);

        return CommonResponse.success(true);
    }

    @PostMapping("/read-log-config/convert")
    public CommonResponse<Boolean> convertReadLogConfig(@ModelAttribute TestReadLogConfig readLogConfig) {
        if (readLogConfig.getId() == 0) {
            throw CommonException.of("Convert read log config error cause id is blank");
        }

        TestReadLogConfig existConfig = testReadLogConfigRepository.findById(readLogConfig.getId())
                .orElseThrow(() -> CommonException.of("Convert read log config error cause {} is not found", readLogConfig.getId()));

        existConfig.setPattern(readLogConfig.getPattern());
        existConfig.setPatternConverted(true);

        testReadLogConfigRepository.save(existConfig);

        return CommonResponse.success(true);
    }

    @RequestMapping("/cpk-config")
    public ListResponse<TestParameter> getParameterList(String factory, String customer, String modelName, String groupName) {
        List<TestParameter> parameterList;
        if (!StringUtils.isEmpty(customer)) {
            parameterList = testParameterRepository.findAllByFactoryAndCustomerAndGroupName(factory, customer, groupName);
        } else {
            parameterList = testParameterRepository.findAllByFactoryAndModelNameAndGroupName(factory, modelName, groupName);
        }
        return ListResponse.success(parameterList);
    }

    @RequestMapping("/cpk-config/{id}")
    public CommonResponse<TestParameter> getParameter(@PathVariable Integer id) {
        TestParameter parameter = testParameterRepository.findById(id)
                .orElseThrow(() -> CommonException.of("get parameter error {} not found", id));
        return CommonResponse.success(parameter);
    }

    @PostMapping("/cpk-config")
    public CommonResponse<Boolean> updateParameter(@RequestBody TestParameter parameter) {
        Pattern scheduleTimePattern = Pattern.compile("\\d+:\\d+");
        if (parameter.getScheduleTime() != null && !scheduleTimePattern.matcher(parameter.getScheduleTime()).matches()) {
            throw CommonException.of("Schedule time format is not match HH:mm");
        }

        if (parameter.getAlarmTimeInterval() != null && (parameter.getAlarmTimeInterval() < 1 || parameter.getAlarmTimeInterval() > 24)) {
            throw CommonException.of("Alarm time interval must in range [1 : 24]");
        }

        TestParameter existParameter;
        if (parameter.getId() != 0) {
            existParameter = testParameterRepository.findById(parameter.getId()).orElse(null);
        } else {
            if (!StringUtils.isEmpty(parameter.getCustomer())) {
                existParameter = testParameterRepository.findByFactoryAndCustomerAndGroupNameAndParameters(
                        parameter.getFactory(), parameter.getCustomer(), parameter.getGroupName(), parameter.getParameters());
            } else {
                existParameter = testParameterRepository.findByFactoryAndModelNameAndGroupNameAndParameters(
                        parameter.getFactory(), parameter.getModelName(), parameter.getGroupName(), parameter.getParameters());
            }
        }

        if (existParameter == null) {
            existParameter = new TestParameter();
        }

        BeanUtils.copyPropertiesIgnoreNull(parameter, existParameter, "id");
        existParameter.setScheduleTime(parameter.getScheduleTime());
        existParameter.setAlarmTimeInterval(parameter.getAlarmTimeInterval());
        testParameterRepository.save(existParameter);
        return CommonResponse.success(true);
    }

    @DeleteMapping("/cpk-config/{id}")
    public CommonResponse<Boolean> deleteParameter(@PathVariable Integer id) {
        if (id == 0) {
            throw CommonException.of("Delete parameter error id param invalid");
        }
        testParameterRepository.deleteById(id);
        return CommonResponse.success(true);
    }

    @RequestMapping("/valueOfParameter")
    public SpcData getListValueOfParameter(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false, defaultValue = "") String stationName,
            @RequestParam String parameter,
            @RequestParam(required = false) String timeSpan) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        if ("B04".equalsIgnoreCase(factory)) {
            TestModelMeta modelMeta = testModelService.getModelMeta(factory, modelName);
            if (modelMeta == null || StringUtils.isEmpty(modelMeta.getTableName())) {
                return null;
            }

            Parameter parameterMeta = b04LogRepository.getParameterByModelNameAndParameter(modelMeta.getTableName(), parameter);
            if (parameterMeta == null) {
                return null;
            }

            List<Double> data = b04LogRepository.getValueOfParameterByModelNameAndStationNameAndDateTimeBetween(
                    parameter, modelMeta.getTableName(), stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), Double.class);

            Double cpk = TestUtils.calculateCPK(data, parameterMeta.getLowSpec(), parameterMeta.getHighSpec());

            SpcData result = new SpcData();
            result.setCpk(cpk == null ? 0 : cpk);
            result.setValues(data);
            return result;
        } else {

            TestParameter parameterMeta = testParameterRepository.findByFactoryAndModelNameAndGroupNameAndParameters(factory, modelName, groupName, parameter);
            if (parameterMeta == null) {
                return null;
            }

            List<Double> data;
            if (StringUtils.isEmpty(stationName)) {
//                data = b06TestLogRepository.findAllByModelNameAndGroupNameAndTestItemAndDateTimeBetween(
//                        modelName, groupName, parameter, new Timestamp(fullTimeSpan.getStartDate().getTime()), new Timestamp(fullTimeSpan.getEndDate().getTime()))
//                        .stream().map(B06TestLog::getPValue).filter(Objects::nonNull).collect(Collectors.toList());
                data = b06TestLogRepository.findPValueByModelNameAndGroupNameAndTestItemAndWorkDateBetween(
                        modelName, groupName, parameter, df.format(fullTimeSpan.getStartDate()), df.format(fullTimeSpan.getEndDate()))
                        .stream()/*.map(B06TestLog::getPValue)*/.filter(Objects::nonNull).collect(Collectors.toList());
            } else {
//                data = b06TestLogRepository.findAllByModelNameAndGroupNameAndStationNameAndTestItemAndDateTimeBetween(
//                        modelName, groupName, stationName, parameter, new Timestamp(fullTimeSpan.getStartDate().getTime()), new Timestamp(fullTimeSpan.getEndDate().getTime()))
//                        .stream().map(B06TestLog::getPValue).filter(Objects::nonNull).collect(Collectors.toList());
                data = b06TestLogRepository.findPValueByModelNameAndGroupNameAndStationNameAndTestItemAndWorkDateBetween(
                        modelName, groupName, stationName, parameter, df.format(fullTimeSpan.getStartDate()), df.format(fullTimeSpan.getEndDate()))
                        .stream()/*.map(B06TestLog::getPValue)*/.filter(Objects::nonNull).collect(Collectors.toList());
            }
            //Double cpk = null;

            Double cpk = TestUtils.calculateCPK(data, parameterMeta.getLowSpec(), parameterMeta.getHighSpec());

            SpcData result = new SpcData();
            result.setCpk(cpk == null ? 0 : cpk);
            result.setValues(data);
            return result;
        }
    }

    @RequestMapping("/cpkOfParameterByDay")
    public Map<String, Double> getListCpkOfParameter(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam String groupName,
            @RequestParam(required = false, defaultValue = "") String stationName,
            @RequestParam String parameter,
            @RequestParam(required = false) String timeSpan) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        List<TestCpk> cpkList = testCpkRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndParameterAndStartDateBetween(
                factory, modelName, groupName, stationName, parameter, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        return cpkList.stream().collect(Collectors.toMap(
                cpk -> TimeSpan.format(TimeSpan.of(cpk.getStartDate(), cpk.getEndDate()), TimeSpan.Type.FULL), TestCpk::getNewCpk,
                (u, v) -> v,
                TreeMap::new));
    }

    @GetMapping("/cpk/customer")
    public ListResponse<String> getCustomerList(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.HOUR, 1);
        String startDate = df2.format(calendar.getTime());

        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR, 1);
        String endDate = df2.format(calendar.getTime());

        List<String> customerList;
        if (Factory.NBB.equalsIgnoreCase(factory)) {
            customerList = nbbTestLogRepository.findCustomerByFullWorkDateBetween(startDate, endDate);
        } else {
            customerList = new ArrayList<>();
        }
        return ListResponse.success(customerList);
    }

    @GetMapping("/cpk/model")
    public ListResponse<String> getModelList(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.HOUR, 1);
        String startDate = df2.format(calendar.getTime());

        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR, 1);
        String endDate = df2.format(calendar.getTime());

        List<String> modelList;
        if (Factory.B06.equalsIgnoreCase(factory)) {
            modelList = b06TestLogRepository.findModelNameByFullWorkDateBetween(startDate, endDate);
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            modelList = s03TestLogRepository.findModelNameByFullWorkDateBetween(startDate, endDate);
        } else {
            modelList = new ArrayList<>();
        }
        return ListResponse.success(modelList);
    }

    @GetMapping("/cpk/group")
    public ListResponse<String> getGroupList(String factory, String customer, String modelName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.HOUR, 1);
        String startDate = df2.format(calendar.getTime());

        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR, 1);
        String endDate = df2.format(calendar.getTime());

        List<String> groupList;
        if (Factory.NBB.equalsIgnoreCase(factory)) {
            groupList = nbbTestLogRepository.findGroupNameByCustomerFullWorkDateBetween(customer, startDate, endDate);
        } else if (Factory.B06.equalsIgnoreCase(factory)) {
            groupList = b06TestLogRepository.findGroupNameByModelNameFullWorkDateBetween(modelName, startDate, endDate);
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            groupList = s03TestLogRepository.findGroupNameByModelNameFullWorkDateBetween(modelName, startDate, endDate);
        } else {
            groupList = new ArrayList<>();
        }
        return ListResponse.success(groupList);
    }

    @GetMapping("/cpk/station")
    public ListResponse<String> getStationList(String factory, String customer, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.HOUR, 1);
        String startDate = df2.format(calendar.getTime());

        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR, 1);
        String endDate = df2.format(calendar.getTime());

        List<String> groupList;
        if (Factory.NBB.equalsIgnoreCase(factory)) {
            groupList = nbbTestLogRepository.findStationNameByCustomerAndGroupNameFullWorkDateBetween(customer, groupName, startDate, endDate);
        } else if (Factory.B06.equalsIgnoreCase(factory)) {
            groupList = b06TestLogRepository.findStationNameByModelNameAndGroupNameFullWorkDateBetween(modelName, groupName, startDate, endDate);
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            groupList = s03TestLogRepository.findStationNameByModelNameAndGroupNameFullWorkDateBetween(modelName, groupName, startDate, endDate);
        } else {
            groupList = new ArrayList<>();
        }
        return ListResponse.success(groupList);
    }

    @GetMapping("/cpk/parameter")
    public ListResponse<String> getParameterNameList(String factory, String customer, String modelName, String groupName) {
        List<TestParameter> parameterList;
        if (!StringUtils.isEmpty(customer)) {
            parameterList = testParameterRepository.findAllByFactoryAndCustomerAndGroupName(factory, customer, groupName);
        } else {
            parameterList = testParameterRepository.findAllByFactoryAndModelNameAndGroupName(factory, modelName, groupName);
        }
        List<String> parameterNameList = parameterList.stream().map(TestParameter::getParameters).distinct().collect(Collectors.toList());
        return ListResponse.success(parameterNameList);
    }

    @PostMapping("/cpk/all")
    public ListResponse<SpcData> getCpkDataList(@RequestBody CpkFromDatabaseRequest request) {

        TimeSpan fullTimeSpan = TimeSpan.from(request.getTimeSpan(), TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.HOUR, 1);
        String startDate = df2.format(calendar.getTime());

        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR, 1);
        String endDate = df2.format(calendar.getTime());

        List<SpcData> result = new ArrayList<>();

        if ("B06".equalsIgnoreCase(request.getFactory())) {
            Map<String, TestParameter> parameterMetaMap = testParameterRepository.findAllByFactoryAndModelNameAndGroupName(request.getFactory(), request.getModelName(), request.getGroupName())
                    .stream().filter(p -> request.getParameters().contains(p.getParameters()))
                    .collect(Collectors.toMap(TestParameter::getParameters, p -> p, (p1, p2) -> p1));
            if (!parameterMetaMap.isEmpty()) {
                Map<String, List<Double>> data;
                if (StringUtils.isEmpty(request.getStationName())) {
//                    data = b06TestLogRepository.findByModelNameAndGroupNameAndFullWorkDateBetween(request.getModelName(), request.getGroupName(), startDate, endDate)
//                            .stream().collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> (Double) log[1], Collectors.toList())));

                    data = b06TestLogRepository.findByModelNameAndGroupNameAndFullWorkDateBetweenAndParametersIn(request.getModelName(), request.getGroupName(), startDate, endDate, request.getParameters())
                            .stream().collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> (Double) log[1], Collectors.toList())));
                } else {
                    data = b06TestLogRepository.findByModelNameAndGroupNameAndStationNameAndFullWorkDateBetween(request.getModelName(), request.getGroupName(), request.getStationName(), startDate, endDate)
                            .stream().collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> (Double) log[1], Collectors.toList())));
                }
                log.debug("### get cpk data from db success");
                for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
                    if (parameterMetaMap.containsKey(entry.getKey())) {
                        TestParameter parameterMeta = parameterMetaMap.get(entry.getKey());
                        SpcData cpk = TestUtils.calculateCPKData(entry.getValue(), parameterMeta.getLowSpec(), parameterMeta.getHighSpec());
                        if (cpk != null) {
                            cpk.setParameter(parameterMeta.getParameters());
                            result.add(cpk);
                        }
                    }
                }
            }
        } else if (Factory.NBB.equalsIgnoreCase(request.getFactory())) {
            Pattern pattern = Pattern.compile("^(-?\\d+\\.?\\d*)$");
            Map<String, TestParameter> parameterMetaMap = testParameterRepository.findAllByFactoryAndCustomerAndGroupName(request.getFactory(), request.getCustomer(), request.getGroupName())
                    .stream().filter(p -> request.getParameters().contains(p.getParameters()))
                    .collect(Collectors.toMap(TestParameter::getParameters, p -> p, (p1, p2) -> p1));
            if (!parameterMetaMap.isEmpty()) {
                Map<String, List<Double>> data;
                if (StringUtils.isEmpty(request.getStationName())) {
                    data = nbbTestLogRepository.findByCustomerAndGroupNameAndFullWorkDateBetween(request.getCustomer(), request.getGroupName(), startDate, endDate)
                            .stream()
                            .filter(log -> {
                                Matcher matcher = pattern.matcher((String) log[1]);
                                return matcher.find();
                            })
                            .collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> Double.parseDouble((String) log[1]), Collectors.toList())));
                } else {
                    data = nbbTestLogRepository.findByCustomerAndGroupNameAndStationNameAndFullWorkDateBetween(request.getCustomer(), request.getGroupName(), request.getStationName(), startDate, endDate)
                            .stream()
                            .filter(log -> {
                                Matcher matcher = pattern.matcher((String) log[1]);
                                return matcher.find();
                            })
                            .collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> Double.parseDouble((String) log[1]), Collectors.toList())));
                }
                log.debug("### get cpk data from db success");
                for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
                    if (parameterMetaMap.containsKey(entry.getKey())) {
                        TestParameter parameterMeta = parameterMetaMap.get(entry.getKey());
                        SpcData cpk = TestUtils.calculateCPKData(entry.getValue(), parameterMeta.getLowSpec(), parameterMeta.getHighSpec());
                        if (cpk != null) {
                            cpk.setParameter(parameterMeta.getParameters());
                            result.add(cpk);
                        }
                    }
                }
            }
        } else if (Factory.S03.equalsIgnoreCase(request.getFactory())) {
            Map<String, TestParameter> parameterMetaMap = testParameterRepository.findAllByFactoryAndModelNameAndGroupName(request.getFactory(), request.getModelName(), request.getGroupName())
                    .stream().filter(p -> request.getParameters().contains(p.getParameters()))
                    .collect(Collectors.toMap(TestParameter::getParameters, p -> p, (p1, p2) -> p1));
            if (!parameterMetaMap.isEmpty()) {
                Map<String, List<Double>> data;
                if (StringUtils.isEmpty(request.getStationName())) {
                    data = s03TestLogRepository.findByModelNameAndGroupNameAndFullWorkDateBetween(request.getModelName(), request.getGroupName(), startDate, endDate)
                            .stream().collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> (Double) log[1], Collectors.toList())));
                } else {
                    data = s03TestLogRepository.findByModelNameAndGroupNameAndStationNameAndFullWorkDateBetween(request.getModelName(), request.getGroupName(), request.getStationName(), startDate, endDate)
                            .stream().collect(Collectors.groupingBy(log -> (String) log[0], Collectors.mapping(log -> (Double) log[1], Collectors.toList())));
                }
                log.debug("### get cpk data from db success");
                for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
                    if (parameterMetaMap.containsKey(entry.getKey())) {
                        TestParameter parameterMeta = parameterMetaMap.get(entry.getKey());
                        SpcData cpk = TestUtils.calculateCPKData(entry.getValue(), parameterMeta.getLowSpec(), parameterMeta.getHighSpec());
                        if (cpk != null) {
                            cpk.setParameter(parameterMeta.getParameters());
                            result.add(cpk);
                        }
                    }
                }
            }
        }

        return ListResponse.success(result);
    }

    @PostMapping(value = "/cpk/all/export")
    public void getCpkAll(@RequestBody CpkFromDatabaseRequest request, HttpServletResponse httpServletResponse) {
        TimeSpan timeSpan = TimeSpan.from(request.getTimeSpan(), TimeSpan.now(TimeSpan.Type.FULL));

        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=cpk-" + timeSpan + ".xlsx");
        httpServletResponse.setContentType("application/octet-stream");
//        httpServletResponse.setCharacterEncoding("UTF-8");

        List<SpcData> cpkList = getCpkDataList(request).getData();

        exportCpkFile(cpkList, httpServletResponse);
    }

    @PostMapping("/cpk/from-file/upload")
    public CommonResponse<CpkFromFileRequest> uploadCpkFile(MultipartFile file) {
        CpkFromFileRequest cpkFromFileRequest = new CpkFromFileRequest();

        try {
            String fileExtension = CommonUtils.getExtension(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + '-' + new Random().nextInt(100) + "." + fileExtension;
            File tmpFile = new File(dataPath + "tmp/" + fileName);
            file.transferTo(tmpFile);
            cpkFromFileRequest.setTmpFile(fileName);

            Workbook workbook;
            if ("xls".equalsIgnoreCase(fileExtension)) {
                workbook = new HSSFWorkbook(new FileInputStream(tmpFile));
            } else if ("xlsx".equalsIgnoreCase(fileExtension)) {
                workbook = new XSSFWorkbook(new FileInputStream(tmpFile));
            } else {
                throw CommonException.of("upload mps file support only xls and xlsx");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                CpkFromFileRequest.TestItemRequest item = new CpkFromFileRequest.TestItemRequest();
                item.setTestItem(ExcelUtils.getStringValue(row.getCell(i)));
                item.setSampleValue(ExcelUtils.getStringValue(sheet.getRow(1).getCell(i)));
                item.setColumnIndex(i);
                cpkFromFileRequest.getTestItemList().add(item);
            }
        } catch (Exception e) {
            log.error("### getCpkDataList error", e);
            throw new CommonException(String.format("getCpkDataList error with message %s", e.getMessage()));
        }

        return CommonResponse.success(cpkFromFileRequest);
    }

    @PostMapping("/cpk/from-file/calc")
    public ListResponse<SpcData> calcCpkFromFile(@RequestBody CpkFromFileRequest cpkFromFileRequest) {
        List<SpcData> result = new ArrayList<>();

        try {
            Path path = Paths.get(dataPath + "tmp/" + cpkFromFileRequest.getTmpFile());
            if (!Files.exists(path)) {
                throw CommonException.of("Temporary file {} not found", cpkFromFileRequest.getTmpFile());
            }

            Map<String, List<Double>> data = new LinkedHashMap<>();

            Workbook workbook;
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(path.toString()))) {
                workbook = new HSSFWorkbook(Files.newInputStream(path));
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(path.toString()))) {
                workbook = new XSSFWorkbook(Files.newInputStream(path));
            } else {
                throw CommonException.of("upload mps file support only xls and xlsx");
            }

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (CpkFromFileRequest.TestItemRequest item : cpkFromFileRequest.getTestItemList()) {
                    if (row.getCell(item.getColumnIndex()) != null) {
                        List<Double> values = data.getOrDefault(item.getTestItem(), new ArrayList<>());
                        values.add(ExcelUtils.getDoubleValue(row.getCell(item.getColumnIndex())));
                        data.put(item.getTestItem(), values);
                    }
                }
            }

            Map<String, CpkFromFileRequest.TestItemRequest> itemMap = cpkFromFileRequest.getTestItemList().stream()
                    .collect(Collectors.toMap(CpkFromFileRequest.TestItemRequest::getTestItem, i -> i, (i1, i2) -> i1));

            for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
                if (itemMap.containsKey(entry.getKey())) {
                    CpkFromFileRequest.TestItemRequest item = itemMap.get(entry.getKey());
                    SpcData cpk = TestUtils.calculateCPKData(entry.getValue(), item.getLsl(), item.getUsl());
                    if (cpk != null) {
                        cpk.setParameter(item.getTestItem());
                        result.add(cpk);
                    }
                }
            }

        } catch (Exception e) {
            log.error("### getCpkDataList error", e);
            throw new CommonException(String.format("getCpkDataList error with message %s", e.getMessage()));
        }

        return ListResponse.success(result);
    }

    @PostMapping(value = "/cpk/from-file/export", produces = "text/csv; charset=utf-8")
    public void exportCpkFromFile(@RequestBody CpkFromFileRequest cpkFromFileRequest, HttpServletResponse httpServletResponse) {

        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=cpk-" + cpkFromFileRequest.getTmpFile());
        httpServletResponse.setCharacterEncoding("UTF-8");

        List<SpcData> cpkList = calcCpkFromFile(cpkFromFileRequest).getData();

        exportCpkFile(cpkList, httpServletResponse);
    }

    private void exportCpkFile(List<SpcData> cpkList, HttpServletResponse response) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("CPK");

            sheet.setColumnWidth(0, (short) 15000);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setWrapText(true);
            List<String> headers = Arrays.asList("TEST ITEM", "MIN", "AVG", "MAX", "LSL", "USL", "PRIME", "CPK");

            CellStyle redStyle = workbook.createCellStyle();
            redStyle.setFillForegroundColor(IndexedColors.RED.index);
            redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle greenStyle = workbook.createCellStyle();
            greenStyle.setFillForegroundColor(IndexedColors.GREEN.index);
            greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = header.createCell(j);
                cell.setCellValue(headers.get(j));
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < cpkList.size(); i++) {
                Row row = sheet.createRow(i + 1);

                int j = 0;
                Cell cell = row.createCell(j++);
                cell.setCellValue(cpkList.get(i).getParameter());

                cell = row.createCell(j++);
                cell.setCellValue(cpkList.get(i).getMin());

                cell = row.createCell(j++);
                cell.setCellValue(cpkList.get(i).getAverage());

                cell = row.createCell(j++);
                cell.setCellValue(cpkList.get(i).getMax());

                cell = row.createCell(j++);
                if (cpkList.get(i).getLsl() != null) {
                    cell.setCellValue(cpkList.get(i).getLsl());
                }

                cell = row.createCell(j++);
                if (cpkList.get(i).getUsl() != null) {
                    cell.setCellValue(cpkList.get(i).getUsl());
                }

                cell = row.createCell(j++);
                cell.setCellValue(cpkList.get(i).getSigmaWithin());

                cell = row.createCell(j);
                cell.setCellValue(cpkList.get(i).getCpk());
                if (cpkList.get(i).getCpk() < 1.33) {
                    cell.setCellStyle(redStyle);
                } else {
                    cell.setCellStyle(greenStyle);
                }
            }

            String template = "{\n" +
                    "\tchart: {\n" +
                    "\t\tzoomType: 'x',\n" +
                    "\t\tstyle: {\n" +
                    "\t\t\tfontFamily: '-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif,\"Apple Color Emoji\",\"Segoe UI Emoji\",\"Segoe UI Symbol\",\"Noto Color Emoji\"'\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\n" +
                    "\ttitle: {\n" +
                    "\t\ttext: 'Process Capability of %s'" +
                    "\t},\n" +
                    "\n" +
                    "\ttooltip: {\n" +
                    "\t\tvalueDecimals: 4\n" +
                    "\t},\n" +
                    "\n" +
                    "\txAxis: [{\n" +
                    "\t\ttitle: { text: '' },\n" +
                    "\t\talignTicks: false,\n" +
                    "\t\topposite: true,\n" +
                    "\t\ttickInterval: 1,\n" +
                    "\t\ttickPositions: [%d]\n" +
                    "\t}, {\n" +
                    "\t\talignTicks: false,\n" +
                    "\t\tsoftMax: %f,\n" +
                    "\t\tsoftMin: %f,\n" +
                    "\t\tplotLines: [{\n" +
                    "\t\t\tvalue: %f,\n" +
                    "\t\t\tcolor: 'red',\n" +
                    "\t\t\tdashStyle: 'longdash',\n" +
                    "\t\t\twidth: 1,\n" +
                    "\t\t\tzIndex: 2,\n" +
                    "\t\t\tlabel: {\n" +
                    "\t\t\t\ttext: 'USL',\n" +
                    "\t\t\t\tstyle: {\n" +
                    "\t\t\t\t\tcolor: '#666666'\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t},{\n" +
                    "\t\t\tvalue: %f,\n" +
                    "\t\t\tcolor: 'red',\n" +
                    "\t\t\tdashStyle: 'longdash',\n" +
                    "\t\t\twidth: 1,\n" +
                    "\t\t\tzIndex: 2,\n" +
                    "\t\t\tlabel: {\n" +
                    "\t\t\t\ttext: 'LSL',\n" +
                    "\t\t\t\tstyle: {\n" +
                    "\t\t\t\t\tcolor: '#666666'\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t}]\n" +
                    "\t}],\n" +
                    "\n" +
                    "\tyAxis: [{\n" +
                    "\t\ttitle: { text: '' },\n" +
                    "\t\tlabels: {\n" +
                    "\t\t\tenabled: true\n" +
                    "\t\t},\n" +
                    "\t\talignTicks: false,\n" +
                    "\t\tsoftMax: %f,\n" +
                    "\t\tsoftMin: %f,\n" +
                    "\t}, {\n" +
                    "\t\ttitle: { text: '' },\n" +
                    "\t\topposite: true,\n" +
                    "\t\tlabels: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t}\n" +
                    "\t}, {\n" +
                    "\t\ttitle: { text: '' },\n" +
                    "\t\topposite: true,\n" +
                    "\t\tlabels: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t}\n" +
                    "\t}],\n" +
                    "\n" +
                    "\tseries: [{\n" +
                    "\t\tlabel: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t},\n" +
                    "\t\tname: 'Quantity',\n" +
                    "\t\ttype: 'histogram',\n" +
                    "\t\txAxis: 1,\n" +
                    "\t\tyAxis: 2,\n" +
                    "\t\tbaseSeries: 's1',\n" +
                    "\t\tzIndex: -2,\n" +
                    "\t\ttooltip: {\n" +
                    "\t\t\tvalueDecimals: 0\n" +
                    "\t\t},\n" +
                    "\t\tshowInLegend: false\n" +
                    "\t}, {\n" +
                    "\t\tlabel: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t},\n" +
                    "\t\tname: 'Overall',\n" +
                    "\t\ttype: 'bellcurve',\n" +
                    "\t\tdashStyle: 'longdash',\n" +
                    "\t\txAxis: 1,\n" +
                    "\t\tyAxis: 1,\n" +
                    "\t\tbaseSeries: 's1',\n" +
                    "\t\tzIndex: -1,\n" +
                    "\t\tfillOpacity: 0\n" +
                    "\t}, {\n" +
                    "\t\tlabel: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t},\n" +
                    "\t\tname: 'Within',\n" +
                    "\t\ttype: 'areaspline',\n" +
                    "\t\txAxis: 1,\n" +
                    "\t\tyAxis: 1,\n" +
                    "\t\tdata: %s,\n" +
                    "\t\tzIndex: -1,\n" +
                    "\t\tfillOpacity: 0,\n" +
                    "\t\tmarker: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t},\n" +
                    "\t}, {\n" +
                    "\t\tlabel: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t},\n" +
                    "\t\tname: 'Data',\n" +
                    "\t\ttype: 'line',\n" +
                    "\t\tid: 's1',\n" +
                    "\t\tdata: %s,\n" +
                    "\t\tvisible: false,\n" +
                    "\t\tshowInLegend: false\n" +
                    "\t}],\n" +
                    "\n" +
                    "\tlegend: {\n" +
                    "\t\tstyle: {\n" +
                    "\t\t\tfontSize: '11px'\n" +
                    "\t\t},\n" +
                    "\t\tlayout: 'horizontal',\n" +
                    "\t\talign: 'center',\n" +
                    "\t\tverticalAlign: 'bottom'\n" +
                    "\t},\n" +
                    "\tcredits: {\n" +
                    "\t\tenabled: false\n" +
                    "\t},\n" +
                    "\tnavigation: {\n" +
                    "\t\tbuttonOptions: {\n" +
                    "\t\t\tenabled: false\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "}";
            CreationHelper helper = workbook.getCreationHelper();
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            DataFormat format = workbook.createDataFormat();
            CellStyle valueStyle = workbook.createCellStyle();
            valueStyle.setAlignment(HorizontalAlignment.RIGHT);
            valueStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            valueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            valueStyle.setDataFormat(format.getFormat("#,##0.00"));

            for (SpcData cpk : cpkList) {
                String options = String.format(template, cpk.getParameter(), cpk.getValues().size(),
                        cpk.getUsl(), cpk.getLsl(), cpk.getUsl(), cpk.getLsl(), cpk.getUsl(), cpk.getLsl(),
                        objectMapper.writeValueAsString(cpk.getPointCpkList()).replace("\"", ""), objectMapper.writeValueAsString(cpk.getValues()));

                String image = highChartsService.export(options);
                if (StringUtils.isEmpty(image)) {
                    continue;
                }

                byte[] bytes = Files.readAllBytes(Paths.get(image));
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                Sheet iSheet = workbook.createSheet(cpk.getParameter());
                Drawing drawing = iSheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1(0);
                Picture picture = drawing.createPicture(anchor, pictureIdx);
                picture.resize();

                int titleIndex = 11;
                int valueIndex = 12;

                iSheet.setColumnWidth(titleIndex, (short) 4000);
                iSheet.setColumnWidth(valueIndex, (short) 4000);

                int j = 1;
                Row iRow = iSheet.createRow(j++);
                Cell iCell = iRow.createCell(valueIndex);
                iCell.setCellValue("Process Data");
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Sample Size");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getValues().size());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Avg");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getAverage());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Sigma Within");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getSigmaWithin());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Sigma Overall");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getSigmaOverall());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue("Potential Capability");
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Cp");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getCp() != null) {
                    iCell.setCellValue(cpk.getCp());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Cpl");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getCpl() != null) {
                    iCell.setCellValue(cpk.getCpl());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Cpu");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getCpu() != null) {
                    iCell.setCellValue(cpk.getCpu());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Cpk");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getCpk());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue("Overall Capability");
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Pp");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getPp() != null) {
                    iCell.setCellValue(cpk.getPp());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Ppl");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getPpl() != null) {
                    iCell.setCellValue(cpk.getPpl());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Ppu");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getPpu() != null) {
                    iCell.setCellValue(cpk.getPpu());
                }
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j++);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Ppk");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                iCell.setCellValue(cpk.getPpk());
                iCell.setCellStyle(valueStyle);

                iRow = iSheet.createRow(j);
                iCell = iRow.createCell(titleIndex);
                iCell.setCellValue("Cpm");
                iCell.setCellStyle(titleStyle);
                iCell = iRow.createCell(valueIndex);
                if (cpk.getCpm() != null) {
                    iCell.setCellValue(cpk.getCpm());
                }
                iCell.setCellStyle(valueStyle);

                Files.deleteIfExists(Paths.get(image));
            }

            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            log.error("### writePublishFile error", e);
            throw new CommonException(String.format("writePublishFile error with message %s", e.getMessage()));
        }
    }
}
