package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.ap.repository.NbbCustKpOnlineRepository;
import com.foxconn.fii.data.primary.model.IOResponse;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestModelMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.data.primary.repository.TestGroupMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestLineMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestUphTargetRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcSnDetailRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipTrackingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/nbb")
public class TestApiGroupNbbController {

    @Autowired
    private TestModelMetaNbbRepository testModelMetaNbbRepository;

    @Autowired
    private TestGroupMetaNbbRepository testGroupMetaNbbRepository;

    @Autowired
    private TestUphTargetRepository testUphTargetRepository;

    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Autowired
    private TestLineMetaNbbRepository testLineMetaNbbRepository;

    @Autowired
    private SfcWipTrackingRepository sfcWipTrackingRepository;

    @Autowired
    private NbbCustKpOnlineRepository nbbCustKpOnlineRepository;

    @Autowired
    private SfcSnDetailRepository sfcSnDetailRepository;

    @RequestMapping("/group/output")
    public Map<String, Map<String, Map<String, Map<String, IOResponse>>>> getGroupOutput(
            String customer,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            String stage, String modelName,
            String timeSpan) {
        try {
            String factory = Factory.NBB;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd");

            List<TestModelMetaNbb> modelMetaList = testModelMetaNbbRepository.findByFactoryAndCustomer(factory, customer);

            Map<String, String> modelStageMap = modelMetaList.stream()
                    .collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

//            Map<String, Set<String>> stageModelMap = modelMetaList.stream()
//                    .collect(Collectors.groupingBy(TestModelMetaNbb::getStage, Collectors.mapping(TestModelMetaNbb::getModelName, Collectors.toSet())));
//            stageModelMap.put("SMT", stageModelMap.getOrDefault("SMA", new HashSet<>()));

            if (stageList.isEmpty() && !StringUtils.isEmpty(stage)) {
                stageList.add(stage.toUpperCase());
            }

            if (modelList.isEmpty()) {
                if (StringUtils.isEmpty(modelName)) {
                    modelList.addAll(modelMetaList.stream()
                            .filter(model -> stageList.isEmpty() || (stageList.contains(model.getStage())) || (stageList.contains("SMT") && "SMA".equalsIgnoreCase(model.getStage())))
                            .map(TestModelMetaNbb::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }


            List<TestGroupMetaNbb> testGroupMetaNbbList = testGroupMetaNbbRepository.findGroupOutputByFactory(factory, customer);

            Map<String, TestGroupMetaNbb> groupMetaMap = testGroupMetaNbbList.stream()
                    .collect(Collectors.toMap(group -> group.getStage() + "_" + group.getGroupName(), group -> group, (g1, g2) -> g1));

            Map<String, String> groupStageMap = testGroupMetaNbbList.stream()
                    .collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getStage, (g1, g2) -> g1));

            List<String> groupList = testGroupMetaNbbList.stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .map(TestGroupMetaNbb::getGroupName)
                    .collect(Collectors.toList());

            Date startDate;
            Date endDate;
            if (StringUtils.isEmpty(timeSpan)) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 30);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);

                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();

                calendar.add(Calendar.MONTH, 1);
                endDate = calendar.getTime();
            } else {
                TimeSpan fullTS = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
                startDate = fullTS.getStartDate();
                endDate = fullTS.getEndDate();
            }


            Map<String, List<TestGroup>> groupMap = sfcSmtGroupRepository.findByWorkDateBetween(factory, customer, groupList, startDate, endDate)
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

//            nbbTestGroupRepository.findByWorkDateBetween(visibleGroupList, startDate, endDate)
//                    .stream().map(NbbTestGroupRepository::mapping)
//                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
//                    .forEach(groupMap::putIfAbsent);

            List<TestGroup> tmpList = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> StringUtils.isEmpty(customer) || modelList.contains(group.getModelName()))
                    .collect(Collectors.toList());

            Map<String, Map<String, Map<String, Map<String, IOResponse>>>> testGroupMap = tmpList.stream()
                    .collect(Collectors.groupingBy(
                            group -> df.format(group.getStartDate()),
                            TreeMap::new,
                            Collectors.groupingBy(
                                    group -> {
                                        String stg = modelStageMap.get(group.getModelName());
                                        if ("SMA".equalsIgnoreCase(stg)) {
                                            stg = groupStageMap.get(group.getGroupName());
                                        }
                                        return stg;
                                    },
                                    LinkedHashMap::new,
                                    Collectors.groupingBy(
                                            group -> stageList.contains("MAIN-PACK") ? group.getModelName() : group.getLineName(),
                                            TreeMap::new,
                                            Collectors.toMap(
                                                    group -> {
                                                        TimeSpan ts = TimeSpan.of(group.getStartDate(), group.getEndDate());
                                                        return ts == null ? "" : ts.getShiftType().toString();
                                                    },
                                                    group -> {
                                                        String stg = modelStageMap.get(group.getModelName());
                                                        if ("SMA".equalsIgnoreCase(stg)) {
                                                            stg = groupStageMap.get(group.getGroupName());
                                                        }

                                                        IOResponse io = new IOResponse();
                                                        TestGroupMetaNbb groupMeta = groupMetaMap.get(stg + "_" +group.getGroupName());

                                                        if (groupMeta != null) {
                                                            int remark = groupMeta.getRemark();
                                                            int pass = group.getPass() + group.getFail();
//                                                            if (group.getGroupName().contains("BFT")) {
//                                                                pass += group.getFail();
//                                                            }

                                                            if (1 == remark || 0 == remark) {
                                                                io.setInput(pass);
                                                            }
                                                            if (3 == remark || 0 == remark) {
                                                                io.setOutput(pass);
                                                            }
                                                        }
                                                        return io;
                                                    },
                                                    (g1, g2) -> {
                                                        g1.setInput(g1.getInput() + g2.getInput());
                                                        g1.setOutput(g1.getOutput() + g2.getOutput());
                                                        return g1;
                                                    }
                                            )
                                    )
                            )
                    ));

            Map<String, Map<String, Map<String, IOResponse>>> header = new TreeMap<>();

            for (Map.Entry<String, Map<String, Map<String, Map<String, IOResponse>>>> entryOutput : testGroupMap.entrySet()) {
                for (Map.Entry<String, Map<String, Map<String, IOResponse>>> entry : entryOutput.getValue().entrySet()) {
                    String s = entry.getKey();
                    Map<String, Map<String, IOResponse>> shiftMap = entry.getValue();

                    Map<String, IOResponse> total = new TreeMap<>();
                    total.put("DAY", new IOResponse());
                    total.put("NIGHT", new IOResponse());

                    Map<String, Map<String, IOResponse>> modelHeader = header.getOrDefault(s, new TreeMap<>());
                    modelHeader.put("TOTAL", total);

                    shiftMap.forEach((shift, modelMap) -> {
                        if (!modelHeader.containsKey(shift)) {
                            modelHeader.put(shift, new TreeMap<>());
                        }
                        modelMap.forEach((model, value) -> {
                            modelHeader.get(shift).put(model, null);
                            if (total.containsKey(model)) {
                                total.get(model).setInput(total.get(model).getInput() + value.getInput());
                                total.get(model).setOutput(total.get(model).getOutput() + value.getOutput());
                            }
                        });
                        header.put(s, modelHeader);
                    });

                    shiftMap.put("TOTAL", total);
                }
            }

            testGroupMap.put("HEADER", header);

            return testGroupMap;
        } catch (Exception e) {
            log.error("### getGroupOutput error", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/group/uphByLine")
    public Map<String, Map<String, Integer>> getGroupUphByLine(
            String customer,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            @RequestParam(required = false, defaultValue = "") List<String> lineList,
            @RequestParam(required = false, defaultValue = "") List<String> moList,
            String stage, String modelName, String lineName, String mo,
            String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaList = testModelMetaNbbRepository.findByFactoryAndCustomer(factory, customer);
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaList = testModelMetaNbbRepository.findByFactoryAndCustomerIsNot(factory, "Other");
            }

            Map<String, String> modelSubStage = modelMetaList.stream()
                    .collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));


            if (stageList.isEmpty() && !StringUtils.isEmpty(stage)) {
                stageList.add(stage.toUpperCase());
            }

            if (lineList.isEmpty() && !StringUtils.isEmpty(lineName)) {
                lineList.add(lineName.toUpperCase());
            }

            if (moList.isEmpty() && !StringUtils.isEmpty(mo)) {
                moList.add(mo.toUpperCase());
            }

            if (modelList.isEmpty()) {
                if (StringUtils.isEmpty(modelName)) {
                    modelList.addAll(modelMetaList.stream()
                            .filter(model -> stageList.isEmpty() || (stageList.contains(model.getStage())) || (stageList.contains("SMT") && "SMA".equalsIgnoreCase(model.getStage())))
                            .map(TestModelMetaNbb::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }

            List<TestGroupMetaNbb> groupMetaNbbList;
            if ("ALL".equalsIgnoreCase(customer)) {
                groupMetaNbbList = testGroupMetaNbbRepository.findByFactory(factory);
            } else {
                groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer(factory, customer);
            }

            Map<String, String> groupSubStageMap = groupMetaNbbList.stream()
                    .collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            List<String> groupList = groupMetaNbbList.stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());


            if (modelList.isEmpty() || groupList.isEmpty()) {
                return new HashMap<>();
            }

            Date date;
            Calendar calendar = Calendar.getInstance();
            TimeSpan timeSpan;
            try {
                date = df.parse(workDate);
                timeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);
            } catch (Exception ignored) {
                timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                calendar.setTime(timeSpan.getStartDate());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                date = calendar.getTime();
                shiftType = timeSpan.getShiftType();
            }

            ShiftType targetShiftType = shiftType == null ? ShiftType.DAY : shiftType;
            Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomer("nbb", customer)
                    .stream().collect(Collectors.toMap(TestUphTarget::getGroupName,
                            TestUphTarget::getUph,
                            (u1, u2) -> u1 + u2));

            Map<String, List<TestGroup>> groupMap = sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

//            nbbTestGroupRepository.findByModelNameAndWorkDateAndShiftType(modelList, date, shiftType).stream()
//                    .map(NbbTestGroupRepository::mapping)
//                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
//                    .forEach(groupMap::putIfAbsent);

            sfcSnDetailRepository.findByModelNameAndInStationTimeBetween(Factory.NBB, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                    .stream().map(map -> SfcSnDetailRepository.mapping(Factory.NBB, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                    .forEach(groupMap::putIfAbsent);

            List<TestGroup> tmpList = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> (stageList.isEmpty() || groupList.contains(group.getGroupName())) &&
                            (lineList.isEmpty() || lineList.contains(group.getLineName())) &&
                            (moList.isEmpty() || moList.contains(group.getMo())))
                    .collect(Collectors.toList());

            Map<String, Map<String, Integer>> result = tmpList.stream()
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
                                            return group.getGroupName();
                                        }
                                        return group.getGroupName() + "(" + subByModel + ")";
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            group -> TimeSpan.format(TimeSpan.of(group.getStartDate(), group.getEndDate()), TimeSpan.Type.HOURLY),
                                            TestGroup::getPass,
                                            (g1, g2) -> g1 + g2
                                    )));

            result = result.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> {
                        int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                        return index == -1 ? 1000 : index;
                    }))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            int index = 24;
            if (shiftType != null) {
                index = 12;
            }
            TimeSpan dailyTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            calendar.setTime(dailyTimeSpan.getStartDate());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            if (calendar.getTime().equals(date)) {
                TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
                index = hourlyTimeSpan.getShiftIndex() + 1;
                if (shiftType == null && hourlyTimeSpan.getShiftType() == ShiftType.NIGHT) {
                    index += 12;
                }
            }

            for (Map.Entry<String, Map<String, Integer>> entry : result.entrySet()) {
                int accumulate = entry.getValue().values().stream().mapToInt(value -> value).sum();
                entry.getValue().put("accumulate", accumulate);
                entry.getValue().put("uph-average", accumulate / index);
                entry.getValue().put("uph-target", uphTargetMap.getOrDefault(entry.getKey(), 0));
            }

            return result;
        } catch (
                Exception e) {
            log.error("", e);
            return new HashMap<>();
        }

    }

    @RequestMapping("/group/uphByModel")
    public Map<String, Map<String, Integer>> getGroupUphByModel(String customer, String stage, String modelName, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            log.debug("### UPH by model START");
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer(factory, customer);

            Map<String, String> modelSubStage = modelMetaNbbList.stream()
                    .collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));

            List<String> modelList;
            if (StringUtils.isEmpty(modelName)) {
                modelList = modelMetaNbbList.stream()
                        .filter(model -> StringUtils.isEmpty(stage) ||
                                (stage.equalsIgnoreCase(model.getStage()) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage()))))
                        .map(TestModelMetaNbb::getModelName)
                        .collect(Collectors.toList());
            } else {
                modelList = Collections.singletonList(modelName);
            }


            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer(factory, customer);

            Map<String, String> groupSubStageMap = groupMetaNbbList.stream()
                    .collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            List<String> groupList = groupMetaNbbList.stream()
                    .filter(group -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(group.getStage()))
                    .map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());


            if (modelList.isEmpty() || groupList.isEmpty()) {
                return new HashMap<>();
            }

            Date date;
            Calendar calendar = Calendar.getInstance();
            TimeSpan timeSpan;
            try {
                date = df.parse(workDate);
                timeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);
            } catch (Exception ignored) {
                timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                calendar.setTime(timeSpan.getStartDate());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                date = calendar.getTime();
                shiftType = timeSpan.getShiftType();
            }

            ShiftType targetShiftType = shiftType == null ? ShiftType.DAY : shiftType;
            Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomer(factory, customer)
                    .stream().collect(Collectors.toMap(TestUphTarget::getGroupName,
                            TestUphTarget::getUph,
                            (u1, u2) -> u1 + u2));

            log.debug("### UPH by model START get data from db");
            Map<String, List<TestGroup>> groupMap = sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

            sfcSnDetailRepository.findByModelNameAndInStationTimeBetween(Factory.NBB, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                    .stream().map(map -> SfcSnDetailRepository.mapping(Factory.NBB, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                    .forEach(groupMap::putIfAbsent);

            log.debug("### UPH by model END get data from db");
            Map<String, Map<String, Integer>> result = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> StringUtils.isEmpty(stage) || groupList.contains(group.getGroupName()))
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
                                            return group.getGroupName();
                                        }
                                        return group.getGroupName() + "(" + subByModel + ")";
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            group -> TimeSpan.format(TimeSpan.of(group.getStartDate(), group.getEndDate()), TimeSpan.Type.HOURLY),
                                            TestGroup::getPass,
                                            (g1, g2) -> g1 + g2
                                    )));

            result = result.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> {
                        int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                        return index == -1 ? 1000 : index;
                    }))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            int index = 24;
            if (shiftType != null) {
                index = 12;
            }
            TimeSpan dailyTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            calendar.setTime(dailyTimeSpan.getStartDate());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            if (calendar.getTime().equals(date)) {
                TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
                index = hourlyTimeSpan.getShiftIndex() + 1;
                if (shiftType == null && hourlyTimeSpan.getShiftType() == ShiftType.NIGHT) {
                    index += 12;
                }
            }

            for (Map.Entry<String, Map<String, Integer>> entry : result.entrySet()) {
                int accumulate = entry.getValue().values().stream().mapToInt(value -> value).sum();
                entry.getValue().put("accumulate", accumulate);
                entry.getValue().put("uph-average", accumulate / index);
                entry.getValue().put("uph-target", uphTargetMap.getOrDefault(entry.getKey(), 0));
            }

            log.debug("### UPH by model END");

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/group/outputOfDate")
    public Map<String, Map<String, Integer>> getGroupOutputOfDateByModel(
            String customer,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            @RequestParam(required = false, defaultValue = "") List<String> lineList,
            @RequestParam(required = false, defaultValue = "") List<String> moList,
            String stage, String modelName, String lineName, String mo,
            String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {

            List<TestModelMetaNbb> modelMetaList = testModelMetaNbbRepository.findByFactoryAndCustomer(factory, customer);

            Map<String, String> modelSubStage = modelMetaList.stream()
                    .collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));

            if (stageList.isEmpty() && !StringUtils.isEmpty(stage)) {
                stageList.add(stage.toUpperCase());
            }

            if (lineList.isEmpty() && !StringUtils.isEmpty(lineName)) {
                lineList.add(lineName.toUpperCase());
            }

            if (moList.isEmpty() && !StringUtils.isEmpty(mo)) {
                moList.add(mo.toUpperCase());
            }

            if (modelList.isEmpty()) {
                if (StringUtils.isEmpty(modelName)) {
                    modelList.addAll(modelMetaList.stream()
                            .filter(model -> stageList.isEmpty() || (stageList.contains(model.getStage())) || (stageList.contains("SMT") && "SMA".equalsIgnoreCase(model.getStage())))
                            .map(TestModelMetaNbb::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }


            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer(factory, customer);

            Map<String, String> groupSubStageMap = groupMetaNbbList.stream()
                    .collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            List<String> groupList = groupMetaNbbList.stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());


            if (modelList.isEmpty() || groupList.isEmpty()) {
                return new HashMap<>();
            }


            Date date;
            Calendar calendar = Calendar.getInstance();
            TimeSpan timeSpan;
            try {
                date = df.parse(workDate);
                timeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);
            } catch (Exception ignored) {
                timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                calendar.setTime(timeSpan.getStartDate());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                date = calendar.getTime();
                shiftType = timeSpan.getShiftType();
            }

            ShiftType targetShiftType = shiftType == null ? ShiftType.DAY : shiftType;
            Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomer(factory, customer)
                    .stream().collect(Collectors.toMap(TestUphTarget::getGroupName,
                            TestUphTarget::getUph,
                            (u1, u2) -> u1 + u2));

            Map<String, List<TestGroup>> groupMap = sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

            sfcSnDetailRepository.findByModelNameAndInStationTimeBetween(Factory.NBB, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                    .stream().map(map -> SfcSnDetailRepository.mapping(Factory.NBB, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                    .forEach(groupMap::putIfAbsent);

            Map<String, Map<String, Integer>> result = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> (stageList.isEmpty() || groupList.contains(group.getGroupName())) &&
                            (lineList.isEmpty() || lineList.contains(group.getLineName())) &&
                            (moList.isEmpty() || moList.contains(group.getMo())))
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
                                            return group.getGroupName();
                                        }
                                        return group.getGroupName() + "(" + subByModel + ")";
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            TestGroup::getLineName,
                                            TestGroup::getPass,
                                            (g1, g2) -> g1 + g2
                                    )));

            result = result.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> {
                        int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                        return index == -1 ? 1000 : index;
                    }))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            Map<String, Integer> header = new LinkedHashMap<>();
            result.forEach((group, lineMap) -> {
                lineMap.forEach((line, value) -> header.put(line, 0));
                int total = lineMap.values().stream().mapToInt(value -> value).sum();
                lineMap.put("accumulate", total);
                lineMap.put("uph-target", uphTargetMap.getOrDefault(group, 0));
            });

            result.put("HEADER", header);

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/group/wipByLine")
    public Map<String, Map<String, Integer>> getGroupWIPByLine(String customer, String stage, String modelName) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer("nbb", customer);

            List<String> modelList;
            if (StringUtils.isEmpty(modelName)) {
                modelList = modelMetaNbbList.stream()
                        .filter(model -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(model.getStage()) ||
                                ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .map(TestModelMetaNbb::getModelName)
                        .collect(Collectors.toList());
            } else {
                modelList = Collections.singletonList(modelName);
            }

            if (modelList.isEmpty()) {
                return new HashMap<>();
            }

            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer("nbb", customer)
                    .stream().filter(group -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(group.getStage())).collect(Collectors.toList());

            Map<String, String> groupStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> groupSubStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, String> modelStageMap = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> modelSubStage = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, Map<String, Integer>> result = sfcWipTrackingRepository.findByModel(factory, customer, modelList).stream()
                    .map(SfcWipTrackingRepository::mapping)
                    .filter(group -> StringUtils.isEmpty(stage) ||
                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName())))
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
                                            return group.getGroupName();
                                        }
                                        return group.getGroupName() + "(" + subByModel + ")";
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            group -> group.getLineName(),
                                            TestGroup::getWip,
                                            (g1, g2) -> g1 + g2
                                    )));

            List<String> groupList = groupMetaNbbList.stream().map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());

            result = result.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> {
                        int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                        return index == -1 ? 1000 : index;
                    }))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            Map<String, Integer> header = new LinkedHashMap<>();
            result.forEach((group, lineMap) -> {
                lineMap.forEach((line, value) -> header.put(line, 0));
            });

            result.put("HEADER", header);

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/group/wip")
    public Map<String, Map<String, Map<String, Integer>>> getGroupWIP(String customer, String stage, String modelName) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer("nbb", customer);

            List<String> modelList;
            if (StringUtils.isEmpty(modelName)) {
                modelList = modelMetaNbbList.stream()
                        .filter(model -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(model.getStage()) ||
                                ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .map(TestModelMetaNbb::getModelName)
                        .collect(Collectors.toList());
            } else {
                modelList = Collections.singletonList(modelName);
            }

            if (modelList.isEmpty()) {
                return new HashMap<>();
            }

            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer("nbb", customer)
                    .stream().filter(group -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(group.getStage())).collect(Collectors.toList());

            Map<String, String> groupStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> groupSubStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, String> modelStageMap = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> modelSubStage = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, Map<String, Map<String, Integer>>> result = sfcWipTrackingRepository.findByModel(factory, customer, modelList).stream()
                    .map(SfcWipTrackingRepository::mapping)
                    .filter(group -> StringUtils.isEmpty(stage) ||
                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName())))
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String key = modelStageMap.getOrDefault(group.getModelName(), "");
                                        if ("SMA".equalsIgnoreCase(key)) {
                                            key = groupStageMap.getOrDefault(group.getGroupName(), "SMA");
                                        }
                                        return key;
                                    },
                                    HashMap::new,
                                    Collectors.groupingBy(
                                            group -> group.getMo(),
                                            HashMap::new,
                                            Collectors.toMap(
                                                    group -> {
                                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
                                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
                                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
                                                            return group.getGroupName();
                                                        }
                                                        return group.getGroupName() + "(" + subByModel + ")";
                                                    },
                                                    TestGroup::getWip,
                                                    (g1, g2) -> g1 + g2
                                            ))));

            Map<String, Map<String, TestGroup>> linkQty = sfcWipTrackingRepository.findWipLinkByModel(factory, customer, modelList).stream()
                    .map(SfcWipTrackingRepository::mapping)
                    .filter(group -> StringUtils.isEmpty(stage) ||
                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName())))
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String key = modelStageMap.getOrDefault(group.getModelName(), "");
                                        if ("SMA".equalsIgnoreCase(key)) {
                                            key = groupStageMap.getOrDefault(group.getGroupName(), "SMA");
                                        }
                                        return key;
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            group -> group.getMo(),
                                            group -> group,
                                            (g1, g2) -> {
                                                g1.setWip(g1.getWip() + g2.getWip());
                                                g1.setPass(g1.getPass() + g2.getPass());
                                                return g1;
                                            }
                                    )));

            result.forEach((s, moMap) -> {
                if (linkQty.containsKey(s)) {
                    moMap.forEach((mo, groupMap) -> {
                        if (linkQty.get(s).containsKey(mo)) {
                            groupMap.put("Total Qty", linkQty.get(s).get(mo).getWip());
                            groupMap.put("Sub Total", linkQty.get(s).get(mo).getPlan() - linkQty.get(s).get(mo).getWip());
                            groupMap.put("Link Qty", linkQty.get(s).get(mo).getPass());
                            groupMap.put("Unlink Qty", linkQty.get(s).get(mo).getWip() - linkQty.get(s).get(mo).getPass());
                        }
                    });
                }
            });

            List<String> groupList = groupMetaNbbList.stream().map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());

            for (Map.Entry<String, Map<String, Map<String, Integer>>> entryResult : result.entrySet()) {
                for (Map.Entry<String, Map<String, Integer>> entryMo : entryResult.getValue().entrySet()) {
                    entryMo.setValue(entryMo.getValue().entrySet().stream()
//                            .filter(entry -> groupList.contains(entry.getKey().split("[(]")[0]))
                            .sorted(Comparator.comparingInt(entry -> {
                                int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                                return index == -1 ? 1000 : index;
                            }))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
                }
            }

            result.forEach((s, moMap) -> {
                Map<String, Integer> header = new HashMap<>();
                moMap.forEach((mo, groupMap) -> {
                    groupMap.forEach((group, value) -> header.put(group, 0));
                });
                moMap.put("HEADER", header);
            });

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/group/wipByModel")
    public Map<String, Map<String, TestGroup>> getGroupWIPByModel(
            String customer,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            @RequestParam(required = false, defaultValue = "") List<String> moList,
            String stage, String modelName, String mo) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaList = testModelMetaNbbRepository.findByFactoryAndCustomer("nbb", customer);

            Map<String, String> modelStageMap = modelMetaList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

            if (stageList.isEmpty() && !StringUtils.isEmpty(stage)) {
                stageList.add(stage.toUpperCase());
            }

            if (moList.isEmpty() && !StringUtils.isEmpty(mo)) {
                moList.add(mo.toUpperCase());
            }

            if (modelList.isEmpty()) {
                if (StringUtils.isEmpty(modelName)) {
                    modelList.addAll(modelMetaList.stream()
                            .filter(model -> stageList.isEmpty() || (stageList.contains(model.getStage())) || (stageList.contains("SMT") && "SMA".equalsIgnoreCase(model.getStage())))
                            .map(TestModelMetaNbb::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }

            if (modelList.isEmpty() || stageList.contains("SMT")) {
                return new HashMap<>();
            }

            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer("nbb", customer).stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .collect(Collectors.toList());

            Map<String, String> groupStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getStage, (s1, s2) -> s1));

            List<TestGroup> groupList = sfcWipTrackingRepository.findWipLinkByModel(factory, customer, modelList).stream()
                    .map(SfcWipTrackingRepository::mappingWip)
                    .filter(group -> (stageList.isEmpty() || stageList.contains(modelStageMap.get(group.getModelName()))) &&
                    (moList.isEmpty() || moList.contains(group.getMo())))
                    .collect(Collectors.toList());

            Map<String, Map<String, TestGroup>> linkQty = groupList.stream()
                    .collect(
                            Collectors.groupingBy(
                                    group -> {
                                        String key = modelStageMap.getOrDefault(group.getModelName(), "");
                                        if ("SMA".equalsIgnoreCase(key)) {
                                            key = groupStageMap.getOrDefault(group.getGroupName(), "SMA");
                                        }
                                        return key;
                                    },
                                    HashMap::new,
                                    Collectors.toMap(
                                            group -> group.getModelName() + "_" + group.getMo(),
                                            group -> group,
                                            (g1, g2) -> {
                                                g1.setPlan(g1.getPlan() + g2.getPlan());
                                                g1.setWip(g1.getWip() + g2.getWip());
                                                g1.setPass(g1.getPass() + g2.getPass());
                                                g1.setFail(g1.getFail() + g2.getFail());
                                                return g1;
                                            },
                                            TreeMap::new
                                    )));

            return linkQty;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

}

