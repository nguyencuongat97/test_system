package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.primary.model.ConfirmTaskRequest;
import com.foxconn.fii.data.primary.model.TrackingResponse;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.model.entity.TestTrackingHistory;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingHistoryRepository;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.receiver.test.service.TestTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/tracking")
public class TestApiTrackingController {

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestTrackingService testTrackingService;

    @Autowired
    private TestTrackingHistoryRepository testTrackingHistoryRepository;

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private TestResourceService testResourceService;

    @GetMapping("/detail")
    public TrackingResponse detailTask(
            @RequestParam Long trackingId) {

        List<TestTrackingHistory> trackingHistoryList = testTrackingHistoryRepository.findAllByTrackingId(trackingId);

        if (trackingHistoryList.isEmpty()) {
            return null;
        }

        TrackingResponse tracking = TrackingResponse.of(trackingHistoryList.get(0).getTracking());

        trackingHistoryList.forEach(history -> TrackingResponse.loadHistory(tracking, history));

        if (tracking.getType() == TestTracking.Type.LOCKED_A || tracking.getStatus() == TestTracking.Status.CONFIRMED || tracking.getStatus() == TestTracking.Status.CLOSED || tracking.getStatus() == TestTracking.Status.REOPEN) {
            if (!StringUtils.isEmpty(tracking.getErrorCode())) {
                Map<String, TestError> errorMap = testErrorService.getTopErrorCode(tracking.getFactory(), tracking.getModelName(), tracking.getGroupName(), tracking.getStationName(), tracking.getStartDate(), tracking.getEndDate(), null);
                tracking.setErrorMetaMap(new LinkedHashMap<>());
                tracking.getErrorMetaMap().put(tracking.getErrorCode(), errorMap.getOrDefault(tracking.getErrorCode(), new TestError()));
            }
        } else {
            Map<String, TestError> errorMap = testErrorService.getTopErrorCode(tracking.getFactory(), tracking.getModelName(), tracking.getGroupName(), tracking.getStationName(), tracking.getStartDate(), tracking.getEndDate(), 3);
            tracking.setErrorMetaMap(errorMap);
        }

        return tracking;
    }

    @PostMapping("/handle")
    public String handleTask(
            @RequestParam Long trackingId,
            @RequestParam String employee) {

        return testTrackingService.handleTask(trackingId, employee);
    }

    @PostMapping("/arrive")
    public String arriveTask(
            @RequestParam Long trackingId,
            @RequestParam String employee) {

        return testTrackingService.arriveTask(trackingId, employee);
    }

    @PostMapping("/confirm")
    public String confirmSolution(
            @ModelAttribute ConfirmTaskRequest confirmRequest) {

        return testTrackingService.confirmTask(confirmRequest);
    }

    @PostMapping("/giveUp")
    public String giveUpTask(
            @RequestParam Long trackingId,
            @RequestParam String employee) {

        return testTrackingService.giveUpTask(trackingId, employee);
    }

    @GetMapping("/employee")
    public List<TrackingResponse> getTrackingListByEmployee(
            @RequestParam String employee,
            @RequestParam(required = false) String timeSpan) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            fullTimeSpan.setStartDate(calendar.getTime());
        }

        Map<Long, TrackingResponse> result = new HashMap<>();
        testTrackingHistoryRepository.findAllByEmployeeAndActionAtBetween(employee, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });

        return new ArrayList<>(result.values());
    }

    @GetMapping("/list")
    public List<TrackingResponse> getTasks(
            @RequestParam(required = false) String factory,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) String timeSpan
    ) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        if (!StringUtils.isEmpty(employee)) {
            TestResource resource = testResourceService.findByEmployeeNo(employee);
            if (resource == null) {
                return Collections.emptyList();
            }

            factory = resource.getFactory();
        }

        Map<Long, TrackingResponse> result = new HashMap<>();
        if (!StringUtils.isEmpty(customer)) {
            List<String> modelList;
            if (StringUtils.isEmpty(stage)) {
                modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, customer);
            } else {
                modelList = testModelMetaRepository.findModelByFactoryAndCustomerAndStage(factory, customer, stage);
            }
            if (modelList.isEmpty()) {
                return Collections.emptyList();
            }

            testTrackingHistoryRepository.findAllByFactoryAndModelNameInAndActionAtBetween(factory, modelList, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                    .forEach(history -> {
                        TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                        TrackingResponse.loadHistory(tracking, history);

                        result.put(history.getTracking().getId(), tracking);
                    });
        } else {
            if (StringUtils.isEmpty(modelName)) {
                testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .forEach(history -> {
                            TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                            TrackingResponse.loadHistory(tracking, history);

                            result.put(history.getTracking().getId(), tracking);
                        });
            } else {
                testTrackingHistoryRepository.findAllByFactoryAndModelNameAndActionAtBetween(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .forEach(history -> {
                            TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                            TrackingResponse.loadHistory(tracking, history);

                            result.put(history.getTracking().getId(), tracking);
                        });
            }
        }


        return result.values().stream()
                .filter(tracking -> /*tracking.getStatus() != TestTracking.Status.AUTO_CLOSED &&*/ tracking.getType() != TestTracking.Type.WARNING_IDLE)
                .sorted(Comparator.comparing(TrackingResponse::getCreatedAt, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @GetMapping("/list/tasks")
    public Map<String, Map<String, Map<String, List<TrackingResponse>>>> getTaskList(
            @RequestParam String employee,
            @RequestParam(required = false) String timeSpan) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        TestResource resource = testResourceService.findByEmployeeNo(employee);
        if (resource == null) {
            return new HashMap<>();
        }

        Map<Long, TrackingResponse> trackingMap = new HashMap<>();
        testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(resource.getFactory(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = trackingMap.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    trackingMap.put(history.getTracking().getId(), tracking);
                });

        Map<String, Map<String, Map<String, List<TrackingResponse>>>> result = trackingMap.values().stream()
                .filter(tracking -> tracking.getStatus() != TestTracking.Status.AUTO_CLOSED)
                .collect(Collectors.groupingBy(tracking -> tracking.getType().toString(),
                        Collectors.groupingBy(TrackingResponse::getModelName,
                                Collectors.groupingBy(TrackingResponse::getGroupName))));

        return result;
    }

    @GetMapping("/status")
    public Map<String, Integer> getTaskStatus(
            @RequestParam String factory,
            @RequestParam(required = false) String timeSpan
    ) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Map<Long, TrackingResponse> result = new HashMap<>();
        testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });
        int autoClosed = 0;
        int done = 0;
        int notYet = 0;
        for (TrackingResponse tracking : result.values()) {
            if (tracking.getType() == TestTracking.Type.WARNING_IDLE) {
                continue;
            }

            if (tracking.getStatus() == TestTracking.Status.AUTO_CLOSED) {
                autoClosed ++;
            } else if (tracking.getStatus() == TestTracking.Status.CLOSED || tracking.getStatus() == TestTracking.Status.REOPEN || tracking.getStatus() == TestTracking.Status.CONFIRMED) {
                done ++;
            } else {
                notYet ++;
            }
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("auto_closed", autoClosed);
        map.put("done", done);
        map.put("not_yet", notYet);

        return map;
    }

    @GetMapping("/history")
    public List<TrackingResponse> getActionHistory(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam String groupName,
            @RequestParam String stationName,
            @RequestParam(required = false) String timeSpan
    ) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<Long, TrackingResponse> result = new HashMap<>();
        testTrackingHistoryRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndActionAtBetween(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });

//        return result.values().stream().filter(TrackingResponse::isLockedType).collect(Collectors.toList());
        return result.values().stream().sorted(Comparator.comparing(TrackingResponse::getCreatedAt)).collect(Collectors.toList());
    }

    @GetMapping(value = "/history/export", produces = "text/csv; charset=utf-8")
    public void getActionHistories(
            @RequestParam String factory,
            @RequestParam(required = false) String timeSpan,
            HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=resource-value.xls");
        httpServletResponse.setCharacterEncoding("UTF-8");

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<Long, TrackingResponse> result = new HashMap<>();
        testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });

//        List<TrackingResponse> trackingList = result.values().stream().filter(TrackingResponse::isLockedType).collect(Collectors.toList());
        List<TrackingResponse> trackingList = result.values().stream()
                .filter(tracking -> tracking.getType() != TestTracking.Type.WARNING_IDLE)
                .sorted(Comparator.comparing(TrackingResponse::getCreatedAt)).collect(Collectors.toList());

        String[] headers = {"No", "Date Time", "Factory", "Model", "Station", "Tester", "Type", "Top 3 Error Code", "Status", "Engineer", "Assigned Time", "Confirmed Time", "Error Code", "Root Cause", "Action", "Downtime", "Closed Time", "Result"};

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("resource-value");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();

        for (int i = 0; i < trackingList.size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell cellNo = row.createCell(0);
            cellNo.setCellValue(i + 1);
            cellNo.setCellStyle(style);

            List<String> cellValues = fromTestTracking(trackingList.get(i));
            for (int j = 0; j < cellValues.size(); j++) {
                Cell cell = row.createCell(j + 1);
                cell.setCellValue(cellValues.get(j));
                cell.setCellStyle(style);
            }
        }

        try {
            workbook.write(httpServletResponse.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("### exportResourceValue error", e);
        }
    }

    private List<String> fromTestTracking(TrackingResponse tracking) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return Arrays.asList(
                df.format(tracking.getCreatedAt()),
                tracking.getFactory(),
                tracking.getModelName(),
                tracking.getGroupName(),
                tracking.getStationName(),
                String.valueOf(tracking.getType()),
                tracking.getTop3ErrorCode(),
                String.valueOf(tracking.getStatus()),
                tracking.getEmployee(),
                tracking.getAssignedAt() != null ? df.format(tracking.getAssignedAt()) : "",
                tracking.getConfirmedAt() != null ? df.format(tracking.getConfirmedAt()) : "",
                tracking.getErrorCode(),
                tracking.getWhy(),
                tracking.getAction(),
                tracking.getDowntime(),
                tracking.getClosedAt() != null ? df.format(tracking.getClosedAt()) : "",
                tracking.getResult()
        );
    }
}
