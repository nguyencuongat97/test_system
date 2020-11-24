package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.sfc.model.TestWipTracking;
import com.foxconn.fii.data.primary.model.IOResponse;
import com.foxconn.fii.data.primary.model.ProductionResponse;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestUphRealtime;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestUphRealtimeRepository;
import com.foxconn.fii.data.primary.repository.TestUphTargetRepository;
import com.foxconn.fii.data.sfc.repository.SfcRouteControlRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcSnDetailRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipTrackingRepository;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
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
@RequestMapping("/api/test")
public class TestApiGroupSfcController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    @Autowired
    private TestUphTargetRepository testUphTargetRepository;

    @Autowired
    private TestUphRealtimeRepository testUphRealtimeRepository;


    /**
     * SFC
     */
    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Autowired
    private SfcSnDetailRepository sfcSnDetailRepository;

    @Autowired
    private SfcWipTrackingRepository sfcWipTrackingRepository;

    @Autowired
    private SfcRouteControlRepository sfcRouteControlRepository;

    /**
     * UPH TRACKING DASHBOARD
     */
    @RequestMapping("/group/uph-dashboard")
    public Map<String, Map<String, ProductionResponse>> getGroupUphByLine(
            String factory,
            String customer,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            @RequestParam(required = false, defaultValue = "") List<String> lineList,
            @RequestParam(required = false, defaultValue = "") List<String> moList,
            String stage, String modelName, String lineName, String mo,
            String workDate, ShiftType shiftType, Boolean quarter) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMeta> modelMetaList;
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaList = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
            } else {
                modelMetaList = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, customer);
            }

            Map<String, String> modelSubStage = modelMetaList.stream()
                    .collect(Collectors.toMap(TestModelMeta::getModelName, TestModelMeta::getSubStage, (s1, s2) -> s1));

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
                            .map(TestModelMeta::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }

            List<TestGroupMeta> groupMetaList;
            if ("ALL".equalsIgnoreCase(customer)) {
                groupMetaList = testGroupMetaRepository.findAllByFactory(factory);
            } else {
                groupMetaList = testGroupMetaRepository.findByFactoryAndCustomer(factory, customer);
            }

            Map<String, String> groupSubStageMap = groupMetaList.stream()
                    .collect(Collectors.toMap(TestGroupMeta::getGroupName, TestGroupMeta::getSubStage, (s1, s2) -> s1));


            List<String> groupList = groupMetaList.stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .map(TestGroupMeta::getGroupName).collect(Collectors.toList());

            if (modelList.isEmpty() || (!stageList.isEmpty() && groupList.isEmpty())) {
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

//            System.out.println("ShiftType.Day : " + ShiftType.DAY);
//            System.out.println("ShiftType : " + shiftType);
//            System.out.println("targetShiftType la  : " + targetShiftType);

            Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomer(factory, customer)
                    .stream().collect(Collectors.toMap(uphTarget -> uphTarget.getGroupName() + "_" + uphTarget.getTime(),
                            TestUphTarget::getUph,
                            (u1, u2) -> u1 + u2 ));

            Map<String, List<TestGroup>> groupMap;
            if (quarter != null && quarter) {
                groupMap = sfcSnDetailRepository.findGroupQuarterByModelNameAndInStationTimeBetween(factory, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                        .stream().map(map -> SfcSnDetailRepository.mapping(factory, map))
                        .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));
            } else {
                groupMap = sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                        .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                        .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

//            sfcTestGroupRepository.findByModelNameAndWorkDateAndShiftType(modelList, date, shiftType).stream()
//                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
//                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
//                    .forEach(groupMap::putIfAbsent);

                sfcSnDetailRepository.findByModelNameAndInStationTimeBetween(factory, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                        .stream().map(map -> SfcSnDetailRepository.mapping(factory, map))
                        .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                        .forEach(groupMap::putIfAbsent);
            }

            List<TestGroup> tmpList = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> (stageList.isEmpty() || groupList.contains(group.getGroupName())) &&
                            (lineList.isEmpty() || lineList.contains(group.getLineName())) &&
                            (moList.isEmpty() || moList.contains(group.getMo())))
                    .collect(Collectors.toList());

            Map<String, Map<String, ProductionResponse>> result = tmpList.stream()
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
                                            group -> ProductionResponse.of(group.getPass(), group.getFail(), 0),
                                            (g1, g2) -> {
                                                g1.setPass(g1.getPass() + g2.getPass());
                                                g1.setFail(g1.getFail() + g2.getFail());
                                                return g1;
                                            }
                                    )));

            List<String> moOnlineList = sfcSmtGroupRepository.getMoByWorkDateAndShiftType(factory, customer, modelList, lineName, date, shiftType);
            List<String> sortedGroupList = sfcRouteControlRepository.getGroupListOrderByMo(factory, customer, moOnlineList);
            result = result.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> {
                        int index;
                        if (Factory.NBB.equalsIgnoreCase(factory)) {
                            index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                        } else {
                            index = sortedGroupList.indexOf(entry.getKey().split("[(]")[0]);
                        }
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

            if (quarter != null && quarter) {
                index *= 4;
            }

            for (Map.Entry<String, Map<String, ProductionResponse>> entry : result.entrySet()) {
                int accumulatePass = entry.getValue().values().stream().mapToInt(ProductionResponse::getPass).sum();
                int accumulateFail = entry.getValue().values().stream().mapToInt(ProductionResponse::getFail).sum();
                entry.getValue().put("accumulate", ProductionResponse.of(accumulatePass, accumulateFail,0));
                entry.getValue().put("uph-average", ProductionResponse.of(accumulatePass / index, accumulateFail / index, 0));
//                entry.getValue().put("uph-target", ProductionResponse.of(uphTargetMap.getOrDefault(entry.getKey(), 0), 0));
                for (Map.Entry<String, ProductionResponse> entryTime : entry.getValue().entrySet()){
                    String key = entry.getKey() + "_" + entryTime.getKey() ;
                    entryTime.getValue().setUph(uphTargetMap.getOrDefault(key, 0));
                }
            }

            return result;
        } catch (
                Exception e) {
            log.error("", e);
            return new HashMap<>();
        }

    }


    /**
     * UPH ALL MODEL
     */
    @RequestMapping("/group/uph-all-model")
    public Map<String, Map<String, Map<String, Object>>> getUphAllModel(
            @RequestParam String factory,
            @RequestParam(required = false, defaultValue = "") String customer,
            @RequestParam(required = false) String timeSpan,
            @RequestParam String workDate,
            @RequestParam ShiftType shiftType
    ) {
        Date date;
        Calendar calendar = Calendar.getInstance();
        TimeSpan fullTimeSpan;
        if (!StringUtils.isEmpty(timeSpan)) {
            fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
            date = calendar.getTime();
            shiftType = fullTimeSpan.getShiftType();
        } else {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                date = df.parse(workDate);
                fullTimeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);
            } catch (Exception ignored) {
                fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                calendar.setTime(fullTimeSpan.getStartDate());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                date = calendar.getTime();
                shiftType = fullTimeSpan.getShiftType();
            }
        }

        List<String> modelList;
//        if (Factory.C03.equalsIgnoreCase(factory)) {
//            Date minDate = fullTimeSpan.getStartDate();
//            modelList = testModelService.getModelMetaList(factory, null)
//                    .stream()
//                    .filter(meta -> meta.getUpdatedAt().after(minDate))
//                    .map(TestModelMeta::getModelName)
//                    .collect(Collectors.toList());
//        } else {
            modelList = sfcSmtGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, date, shiftType);
//        }

        List<TestGroup> groupList = sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map)).collect(Collectors.toList());

        List<TestUphRealtime> realtimeList = testUphRealtimeRepository.findByFactoryOrderByWorkSectionAsc("B06");

        Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomer(factory, customer)
                .stream().collect(Collectors.toMap(
                        uph -> uph.getModelName() + "_" + uph.getGroupName(),
                        TestUphTarget::getUph,
                        (u1, u2) -> u1 + u2));

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
        Map<String, Map<String, Map<String, Object>>> result = groupList.stream()
                .collect(Collectors.groupingBy(TestGroup::getModelName,
                        Collectors.groupingBy(TestGroup::getGroupName,
                                Collectors.toMap(
                                        group -> DATE_FORMAT.format(group.getStartDate()) + " - " + DATE_FORMAT.format(group.getEndDate()),
                                        TestGroup::getPass,
                                        (p1, p2) -> (int) p1 + (int) p2))));

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

        for (Map.Entry<String, Map<String, Map<String, Object>>> entryModel : result.entrySet()) {
            for (Map.Entry<String, Map<String, Object>> entry : entryModel.getValue().entrySet()) {
                int accumulate = entry.getValue().values().stream().mapToInt(value -> (int) value).sum();
                int uphTarget = uphTargetMap.getOrDefault(entry.getKey(), 0);

                for (Map.Entry<String, Object> subEntry : entry.getValue().entrySet()) {
                    Map<String, Object> map = new HashMap<>();
                    int pass = (int) subEntry.getValue();
                    Integer workSection = Integer.valueOf(subEntry.getKey().substring(0, 2)) + 1;
                    double realTime = realtimeList.stream().filter(e -> e.getWorkSection().equals(workSection)).mapToDouble(TestUphRealtime::getRealTime).findFirst().orElse(1D);

                    map.put("qty", pass);
                    if (uphTarget * realTime > 0D) {
                        map.put("rate", Math.round(100 * 100D * pass / (uphTarget * realTime)) / 100D);
                    } else {
                        map.put("rate", null);
                    }
                    subEntry.setValue(map);
                }

                entry.getValue().put("accumulate", accumulate);
                entry.getValue().put("uph-average", accumulate / index);
                entry.getValue().put("uph-target", uphTargetMap.getOrDefault(entryModel.getKey() + "_" + entry.getKey(), 0));
            }
        }

        return result;
    }


    /**
     * INPUT OUTPUT GROUP
     */
    @RequestMapping("/group/output")
    public Map<String, Map<String, Map<String, Map<String, IOResponse>>>> getGroupOutput(
            String customer,
            String factory,
            @RequestParam(required = false, defaultValue = "") List<String> stageList,
            @RequestParam(required = false, defaultValue = "") List<String> modelList,
            String stage, String modelName,
            String timeSpan) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd");

            List<TestModelMeta> modelMetaList = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, customer);

            Map<String, String> modelStageMap = modelMetaList.stream()
                    .collect(Collectors.toMap(TestModelMeta::getModelName, TestModelMeta::getStage, (s1, s2) -> s1));

//            Map<String, Set<String>> stageModelMap = modelMetaList.stream()
//                    .collect(Collectors.groupingBy(TestModelMeta::getStage, Collectors.mapping(TestModelMeta::getModelName, Collectors.toSet())));
//            stageModelMap.put("SMT", stageModelMap.getOrDefault("SMA", new HashSet<>()));

            if (stageList.isEmpty() && !StringUtils.isEmpty(stage)) {
                stageList.add(stage.toUpperCase());
            }

            if (modelList.isEmpty()) {
                if (StringUtils.isEmpty(modelName)) {
                    modelList.addAll(modelMetaList.stream()
                            .filter(model -> stageList.isEmpty() || (stageList.contains(model.getStage())) || (stageList.contains("SMT") && "SMA".equalsIgnoreCase(model.getStage())))
                            .map(TestModelMeta::getModelName)
                            .distinct()
                            .collect(Collectors.toList()));
                } else {
                    modelList.add(modelName.toUpperCase());
                }
            }

            List<TestGroupMeta> testGroupMetaList;
            if (!StringUtils.isEmpty(customer)) {
                testGroupMetaList = testGroupMetaRepository.findGroupInputOutputByFactory(factory, customer);
            } else {
                testGroupMetaList = testGroupMetaRepository.findGroupInputOutputByFactory(factory, modelList);
            }

            Map<String, TestGroupMeta> groupMetaMap = testGroupMetaList.stream()
                    .collect(Collectors.toMap(group -> group.getStage() + "_" + group.getGroupName(), group -> group, (g1, g2) -> g1));

            Map<String, String> groupStageMap = testGroupMetaList.stream()
                    .collect(Collectors.toMap(TestGroupMeta::getGroupName, TestGroupMeta::getStage, (g1, g2) -> g1));

            List<String> groupList = testGroupMetaList.stream()
                    .filter(group -> stageList.isEmpty() || stageList.contains(group.getStage()))
                    .map(TestGroupMeta::getGroupName)
                    .collect(Collectors.toList());

            if (groupList.isEmpty()) {
                return new HashMap<>();
            }

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

            Map<String, List<TestGroup>> groupMap;
            if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
                groupMap = testGroupService.getGroupDailyBG(factory, modelList, startDate, endDate)
                        .stream().collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));
            } else {
                groupMap = sfcSmtGroupRepository.findByWorkDateBetween(factory, customer, groupList, startDate, endDate)
                        .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                        .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));
            }

//            sfcTestGroupRepository.findByWorkDateBetween(visibleGroupList, startDate, endDate)
//                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
//                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
//                    .forEach(groupMap::putIfAbsent);

            List<TestGroup> tmpList = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> (!Factory.B01.equalsIgnoreCase(factory) && !Factory.A02.equalsIgnoreCase(factory) && StringUtils.isEmpty(customer)) || modelList.contains(group.getModelName()))
                    .collect(Collectors.toList());

            Map<String, Map<String, Map<String, Map<String, IOResponse>>>> testGroupMap = tmpList.stream()
                    .collect(Collectors.groupingBy(
                            group -> df.format(group.getStartDate()),
                            TreeMap::new,
                            Collectors.groupingBy(
                                    group -> {
                                        String stg = modelStageMap.get(group.getModelName());
                                        if ("SMA".equalsIgnoreCase(stg) || Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
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
                                                        if ("SMA".equalsIgnoreCase(stg) || Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
                                                            stg = groupStageMap.get(group.getGroupName());
                                                        }

                                                        IOResponse io = new IOResponse();
                                                        TestGroupMeta groupMeta = groupMetaMap.get(stg + "_" + group.getGroupName());

                                                        if (groupMeta != null) {
                                                            int pass = group.getPass() + group.getFail();
//                                                            if (group.getGroupName().contains("BFT")) {
//                                                                pass += group.getFail();
//                                                            }

                                                            if (groupMeta.getRemark().contains("IN")) {
                                                                io.setInput(pass);
                                                            }
                                                            if (groupMeta.getRemark().contains("OUT")) {
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


    /**
     * WIP GROUP
     */
    @RequestMapping("/wip-group")
    public Map<String, Map<String, TestGroup>> getWipGroupByMo(String factory, String customer, String mo, String serial) {
        if (StringUtils.isEmpty(mo) && StringUtils.isEmpty(serial)) {
            return new HashMap<>();
        }

        if (StringUtils.isEmpty(mo)) {
            List<TestWipTracking> serialWipTracking = getSerialNumberDetailWipGroup(factory, customer, serial);
            if (serialWipTracking.isEmpty()) {
                return new HashMap<>();
            }
            mo = serialWipTracking.get(0).getMo();
        }

        List<String> groupNameList = sfcRouteControlRepository.getGroupListByMoNumber(factory, customer, mo);

        List<TestGroup> groupList = sfcWipTrackingRepository.findByMoNumber(factory, customer, StringUtils.isEmpty(mo) ? Collections.emptyList() : Collections.singletonList(mo), serial)
                .stream().map(SfcWipTrackingRepository::mapping).collect(Collectors.toList());

        Map<String, Map<String, TestGroup>> result = new LinkedHashMap<>();
        Map<String, Map<String, TestGroup>> tmp = groupList.stream().collect(Collectors.groupingBy(TestGroup::getLineName,
                Collectors.toMap(
                        TestGroup::getGroupName,
                        group -> group,
                        (g1, g2) -> g1
                )));

        Map<String, TestGroup> totalGroupMap = new LinkedHashMap<>();
        tmp.forEach((line, tmpGroupMap) -> {
            Map<String, TestGroup> groupMap = new LinkedHashMap<>();
            Map.Entry<String, TestGroup> entry = tmpGroupMap.entrySet().iterator().next();
            for (String groupName : groupNameList) {
                if (tmpGroupMap.containsKey(groupName)) {
                    groupMap.put(groupName, tmpGroupMap.get(groupName));

                    TestGroup totalGroup = totalGroupMap.get(groupName);
                    if (totalGroup != null) {
                        totalGroup.setWip(totalGroup.getWip() + tmpGroupMap.get(groupName).getWip());
                    } else {
                        TestGroup g2 = new TestGroup();
                        BeanUtils.copyPropertiesIgnoreNull(tmpGroupMap.get(groupName), g2);
                        totalGroupMap.put(groupName, g2);
                    }
                } else {
                    TestGroup g = new TestGroup();
                    BeanUtils.copyPropertiesIgnoreNull(entry.getValue(), g, "groupName", "wip");
                    g.setGroupName(groupName);
                    g.setWip(0);
                    groupMap.put(groupName, g);

                    TestGroup totalGroup = totalGroupMap.get(groupName);
                    if (totalGroup == null) {
                        TestGroup g2 = new TestGroup();
                        BeanUtils.copyPropertiesIgnoreNull(g, g2);
                        totalGroupMap.put(groupName, g2);
                    }
                }
            }

            tmpGroupMap.forEach((groupName, group) -> {
                if (!groupMap.containsKey(groupName)) {
                    groupMap.put(groupName, group);

                    TestGroup totalGroup = totalGroupMap.get(groupName);
                    if (totalGroup != null) {
                        totalGroup.setWip(totalGroup.getWip() + group.getWip());
                    } else {
                        TestGroup g2 = new TestGroup();
                        BeanUtils.copyPropertiesIgnoreNull(group, g2);
                        totalGroupMap.put(groupName, g2);
                    }
                }
            });

            result.put(line, groupMap);
        });

        if (result.size() > 1) {
            Map<String, Map<String, TestGroup>> result2 = new LinkedHashMap<>();
            result2.put("TOTAL", totalGroupMap);
            result2.putAll(result);
            return result2;
        }
        return result;
    }

    @RequestMapping("/wip-group-detail")
    public List<TestWipTracking> getWipGroupDetail(String factory, String customer, String mo, String wipGroup, String serial) {
        return sfcWipTrackingRepository.findByMoNumberAndWipGroup(factory, customer, Collections.singletonList(mo), Collections.singletonList(wipGroup))
                .stream()
                .map(SfcWipTrackingRepository::mappingWipTracking)
                .filter(tracking -> StringUtils.isEmpty(serial) || serial.equalsIgnoreCase(tracking.getSerialNumber()))
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/wip-group-history")
    public List<TestWipTracking> getWipGroupHistory(String factory, String customer, String mo, String wipGroup, String serial) {
        if (StringUtils.isEmpty(mo) || StringUtils.isEmpty(wipGroup)) {
            return Collections.emptyList();
        }

        return sfcSnDetailRepository.findByMoAndGroupName(factory, customer, mo, wipGroup)
                .stream()
                .map(SfcSnDetailRepository::mappingWipTracking)
                .filter(tracking -> StringUtils.isEmpty(serial) || serial.equalsIgnoreCase(tracking.getSerialNumber()))
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/serial-detail")
    public List<TestWipTracking> getSerialNumberDetail(String factory, String customer, String serial) {
        return sfcSnDetailRepository.findBySerialNumber(factory, customer, serial)
                .stream().map(SfcSnDetailRepository::mappingWipTracking)
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/serial-detail-wip-group")
    public List<TestWipTracking> getSerialNumberDetailWipGroup(String factory, String customer, String serial) {
        return sfcSnDetailRepository.findBySerialNumber(factory, customer, serial)
                .stream().map(SfcSnDetailRepository::mappingWipTracking).collect(Collectors.toList());
    }

    /**
     * WIP OUTPUT GROUP
     */
    @RequestMapping("/wip-output-group")
    public Map<String, Map<String, TestGroup>> getWipOutputGroup(String factory, String customer, String stage, String modelName, String lineName, String workDate, ShiftType shiftType) {
        String timeSpan = String.format("%s %s", workDate, shiftType.toString());
        TimeSpan fullTimeSpan = TimeSpan.from(TimeSpan.now(TimeSpan.Type.FULL), timeSpan, TimeSpan.Type.DAILY, "yyyy/MM/dd");
        try {
            List<TestModelMeta> modelMetaList;
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaList = testModelMetaRepository.findAllByFactory(factory);
            } else {
                modelMetaList = testModelMetaRepository.findByFactoryAndCustomer(factory, customer);
            }

            List<String> modelList;
            if (StringUtils.isEmpty(modelName)) {
                modelList = modelMetaList.stream()
                        .filter(model -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(model.getStage()) ||
                                ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .map(TestModelMeta::getModelName)
                        .collect(Collectors.toList());
            } else {
                modelList = Collections.singletonList(modelName);
            }

            if (modelList.isEmpty()) {
                return new HashMap<>();
            }

            List<String> moOnlineList = sfcSmtGroupRepository.getMoByWorkDateAndShiftType(factory, customer, modelList, lineName, fullTimeSpan.getStartDate(), fullTimeSpan.getShiftType());

            List<TestGroupMeta> groupMetaList = testGroupMetaRepository.findGroupWipByFactory(factory, modelList);
            Map<String, TestGroupMeta> groupMap = groupMetaList.stream().collect(Collectors.toMap(
                    group -> String.join("_", group.getModelName(), group.getGroupName()), group -> group, (g1, g2) -> g1));
            List<String> groupList = groupMetaList.stream().map(TestGroupMeta::getGroupName).distinct().collect(Collectors.toList());

            Map<String, Map<String, TestGroup>> outputMap;
            if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
                outputMap = new HashMap<>();
            } else {
                List<TestGroup> tmp = sfcWipTrackingRepository.findByMoList(factory, customer, moOnlineList, groupList, fullTimeSpan.getEndDate())
//                        .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map)).collect(Collectors.toList());
                        .stream().map(map -> SfcWipTrackingRepository.mappingWip(map)).collect(Collectors.toList());
                outputMap = tmp.stream()
                        .filter(group -> groupMap.containsKey(String.join("_", group.getModelName(), group.getGroupName())))
                        .collect(Collectors.groupingBy(TestGroup::getModelName, Collectors.toMap(group -> groupMap.get(String.join("_", group.getModelName(), group.getGroupName())).getSubStage(), group -> group, (g1, g2) -> {
                            g1.setWip(g1.getWip() + g2.getWip());
                            return g1;
                        })));
            }

            return outputMap;
        } catch (Exception e) {
            log.error("### get wip-output-group error", e);
            return new HashMap<>();
        }
    }
}
