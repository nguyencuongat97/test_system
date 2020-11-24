package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.response.MapResponse;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.util.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test/ete")
public class TestApiEtePageController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestErrorService testErrorService;

    @GetMapping("/daily-by-group")
    public MapResponse<Map<String, TestGroupDaily>> getEteDailyByGroup(String factory, String modelName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.FULL);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            fullTimeSpan.setStartDate(calendar.getTime());
        }

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        Map<String, Map<String, TestGroupDaily>> groupMap =
                testGroupService.getGroupDailyListFromDB(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .stream().collect(Collectors.groupingBy(
                        TestGroupDaily::getGroupName,
                        Collectors.toMap(
                                group -> df.format(group.getStartDate()),
                                TestGroupDaily::clone,
                                TestGroupDaily::merge
                        )
                ));

        List<String> groupList = testGroupService.getGroupMetaList(factory, modelName, null)
                .stream().filter(TestGroupMeta::getVisible).map(TestGroupMeta::getGroupName).collect(Collectors.toList());

        Map<String, Map<String, TestGroupDaily>> result = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, TestGroupDaily>> entry : groupMap.entrySet()) {
            if (groupList.contains(entry.getKey())) {
                Map<String, TestGroupDaily> dailyMap = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
                entry.getValue().forEach(dailyMap::replace);
                result.put(entry.getKey(), dailyMap);
            }
        }

        return MapResponse.success(result);
    }

    @GetMapping("/daily-by-mo")
    public MapResponse<Map<String, TestModel>> getEteDailyByMo(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.FULL);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            fullTimeSpan.setStartDate(calendar.getTime());
        }

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        Map<String, Map<String, List<TestGroupDaily>>> groupMap =
                testGroupService.getGroupDailyListFromDB(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .stream()
                        .filter(group -> (StringUtils.isEmpty(groupName) || groupName.equalsIgnoreCase(group.getGroupName())))
                        .collect(Collectors.groupingBy(
                                TestGroupDaily::getMo,
                                Collectors.groupingBy(
                                        group -> df.format(group.getStartDate())
                                )
                        ));

        Map<String, TestGroupMeta> groupMetaMap = testGroupService.getGroupMetaList(factory, modelName, null)
                .stream().filter(TestGroupMeta::getVisible).collect(Collectors.toMap(TestGroupMeta::getGroupName, g -> g, (g1, g2) -> g1));

        Map<String, Map<String, TestModel>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, List<TestGroupDaily>>> entry : groupMap.entrySet()) {
            Map<String, TestModel> dailyMap = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            for (Map.Entry<String, List<TestGroupDaily>> entryGroup : entry.getValue().entrySet()) {
                TestModel model = new TestModel();
                model.setModelName(modelName);
                model.setMo(entryGroup.getValue().get(0).getMo());
                model.setGroupDailyList(entryGroup.getValue(), groupMetaMap);
                dailyMap.replace(entryGroup.getKey(), model);
            }
            result.put(entry.getKey(), dailyMap);
        }

        return MapResponse.success(result);
    }

    @GetMapping("/hourly-by-group")
    public MapResponse<TestGroup> getEteHourlyByGroup(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        SimpleDateFormat df = new SimpleDateFormat("MM/dd HH:mm");
        Map<String, TestGroup> groupMap =
                testGroupService.getGroupHourlyListFromDB(factory, modelName, groupName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                        .stream().collect(Collectors.toMap(
                        TestGroup::getShiftTime,
                        TestGroup::clone,
                        TestGroup::merge
                ));

        Map<String, TestGroup> result = TestUtils.getHourlyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        groupMap.forEach(result::replace);

        return MapResponse.success(result);
    }

    @GetMapping("/error-of-group")
    public ListResponse<TestErrorDaily> getEteErrorOfGroup(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyListFromDB(factory, modelName, groupName, "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(
                        TestErrorDaily::getErrorCode,
                        TestErrorDaily::clone,
                        TestErrorDaily::merge
                ));

        List<TestErrorDaily> result = errorMap.values().stream()
                .sorted(Comparator.comparing(TestErrorDaily::getFail, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return ListResponse.success(result);
    }

    @GetMapping("/station-of-error")
    public ListResponse<TestErrorDaily> getEteErrorOfStation(String factory, String modelName, String groupName, String errorCode, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyListFromDB(factory, modelName, groupName, "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream()
                .filter(error -> error.getErrorCode().equalsIgnoreCase(errorCode))
                .collect(Collectors.toMap(
                        TestErrorDaily::getStationName,
                        TestErrorDaily::clone,
                        TestErrorDaily::merge
                ));

        List<TestErrorDaily> result = errorMap.values().stream()
                .sorted(Comparator.comparing(TestErrorDaily::getFail, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return ListResponse.success(result);
    }

    @GetMapping("/station-by-error-of-group")
    public ListResponse<TestStationDaily> getEteErrorAndStationOfGroup(String factory, String modelName, String groupName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, Map<String, TestErrorDaily>> errorMap = testErrorService.getErrorDailyListFromDB(factory, modelName, groupName, "", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.groupingBy(
                        TestErrorDaily::getStationName,
                        Collectors.toMap(
                                TestErrorDaily::getErrorCode,
                                TestErrorDaily::clone,
                                TestErrorDaily::merge,
                                LinkedHashMap::new
                        )));

        List<TestStationDaily> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, TestErrorDaily>> entry : errorMap.entrySet()) {
            TestStationDaily station = new TestStationDaily();
            BeanUtils.copyPropertiesIgnoreNull(entry.getValue().values().iterator().next(), station);

            entry.setValue(entry.getValue().entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue(Comparator.comparing(TestErrorDaily::getFail))))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
            station.setErrorMetaMap(entry.getValue());

            station.setFail(entry.getValue().values().stream().mapToInt(TestErrorDaily::getFail).sum());
            result.add(station);
        }
        result.sort(Comparator.comparing(TestStationDaily::getFail, Comparator.reverseOrder()));

        return ListResponse.success(result);
    }

    @GetMapping("/error-of-station")
    public ListResponse<TestErrorDaily> getEteErrorByStation(String factory, String modelName, String groupName, String stationName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyListFromDB(factory, modelName, groupName, stationName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(
                                TestErrorDaily::getErrorCode,
                                TestErrorDaily::clone,
                                TestErrorDaily::merge
                        ));

        List<TestErrorDaily> result = errorMap.values().stream()
                .sorted(Comparator.comparing(TestErrorDaily::getFail, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return ListResponse.success(result);
    }
}
