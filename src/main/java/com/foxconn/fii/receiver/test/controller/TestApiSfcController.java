package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestLineMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestStationMeta;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestLineMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestStationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/sfc")
public class TestApiSfcController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestStationService testStationService;


    @Autowired
    private TestLineMetaNbbRepository testLineMetaNbbRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;


    /** SFC */
    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;


    @RequestMapping("/customer")
    public List<String> getCustomer(String factory) {
        return testModelMetaRepository.findCustomerByFactory(factory)
                .stream().filter(customer -> !StringUtils.isEmpty(customer)).sorted((o1, o2) -> {
                    if ("OTHER".equalsIgnoreCase(o1)) {
                        return 1;
                    } else if ("OTHER".equalsIgnoreCase(o2)) {
                        return -1;
                    } else {
                        return o1.compareTo(o2);
                    }
                }).collect(Collectors.toList());
    }

    @RequestMapping("/stage")
    public List<String> getStage(String factory, String customer) {
        List<String> stageOrder = Arrays.asList("SMT", "SMA", "FAT", "SUB-PACK", "MAIN-PACK");
        List<String> stageList = testGroupMetaRepository.findStageByFactoryAndCustomer(factory, customer);
        testModelMetaRepository.findStageByFactoryAndCustomer(factory, customer).forEach(stage -> {
            if (!stageList.contains(stage)) {
                stageList.add(stage);
            }
        });

        stageList.sort(Comparator.comparingInt(stage -> {
            int index = stageOrder.indexOf(stage);
            return index == -1 ? 1000 : index;
        }));
        return stageList;
    }

    @RequestMapping("/line")
    public Map<String, String> getLine(String factory, String customer, String stage, String modelName, String mo, String workDate, ShiftType shiftType) {
        try {
            List<TestModelMeta> modelMetaList = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, customer);
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaList = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
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

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            List<String> lineList;
            if (StringUtils.isEmpty(workDate)) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                lineList = sfcSmtGroupRepository.getLineNameByWorkDateAndShiftType(factory, customer, modelList, mo, timeSpan.getStartDate(), timeSpan.getShiftType());
            } else {
                Date date = df.parse(workDate);
                lineList = sfcSmtGroupRepository.getLineNameByWorkDateAndShiftType(factory, customer, modelList, mo, date, shiftType);
            }

            Map<String, String> lineMetaMap = testLineMetaNbbRepository.findByFactory(factory)
                    .stream().collect(Collectors.toMap(TestLineMetaNbb::getLineName, TestLineMetaNbb::getShowName, (l1, l2) -> l1));

            return lineList.stream()
                    .collect(Collectors.toMap(line -> line, line -> lineMetaMap.getOrDefault(line, line), (l1, l2) -> l1, TreeMap::new));
        } catch (ParseException e) {
            log.error("### getLine error", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/mo")
    public List<String> getMo(String factory, String customer, String stage, String modelName, String lineName, String workDate, ShiftType shiftType) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMeta> modelMetaList;
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaList = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
            } else {
                modelMetaList = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, customer);
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
                return new ArrayList<>();
            }

            if (StringUtils.isEmpty(workDate)) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                return sfcSmtGroupRepository.getMoByWorkDateAndShiftType(factory, customer, modelList, lineName, timeSpan.getStartDate(), timeSpan.getShiftType());
            } else {
                if (!"ALL".equalsIgnoreCase(workDate)) {
                    Date date = df.parse(workDate);
                    return sfcSmtGroupRepository.getMoByWorkDateAndShiftType(factory, customer, modelList, lineName, date, shiftType);
                } else {
                    return sfcSmtGroupRepository.getMoByWorkDateAndShiftType(factory, customer, modelList, lineName, null, null);
                }
            }
        } catch (ParseException e) {
            log.error("### get MO error", e);
            return Collections.emptyList();
        }
    }

    @RequestMapping("/model")
    public List<String> getModel(String factory, String customer, String stage, String workDate, ShiftType shiftType, String timeSpan, Boolean parameter) {
        try {
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

            if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory) ) {
                return testModelService.getModelMetaList(factory, parameter)
                        .stream()
                        .filter(meta -> meta.getUpdatedAt().after(dailyTimeSpan.getStartDate()))
                        .sorted(Comparator.comparing(TestModelMeta::getUpdatedAt, Comparator.reverseOrder()))
                        .map(TestModelMeta::getModelName)
                        .collect(Collectors.toList());
            } else if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
                List<String> modelList = testModelService.getModelMetaList(factory, null)
                        .stream()
                        .map(TestModelMeta::getModelName)
                        .collect(Collectors.toList());

                List<String> modelOnline = testModelService.getModelMetaOnlineList(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
                modelList.sort(Comparator.comparingInt(model -> {
                    int index = modelOnline.indexOf(model);
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
                            .collect(Collectors.toList());
                }
                return testModelMetaRepository.findModelByFactoryAndCustomer(factory, customer)
                        .stream().filter(modelList::contains)
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
                        .collect(Collectors.toList());
            }

            if ("SMT".equalsIgnoreCase(stage)) {
                return testModelMetaRepository.findModelByFactoryAndCustomerAndStage(factory, customer, "SMA")
                        .stream().filter(modelList::contains)
                        .collect(Collectors.toList());
            }
            return testModelMetaRepository.findModelByFactoryAndCustomerAndStage(factory, customer, stage)
                    .stream().filter(modelList::contains)
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("### getModel error", e);
            return Collections.emptyList();
        }
    }

    @RequestMapping("/group")
    public List<String> getGroup(String factory, String customer, String modelName, String workDate, ShiftType shiftType, String timeSpan, Boolean parameter) {
        if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory) ) {
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
            return testGroupService.getGroupMetaList(factory, modelName, parameter)
                    .stream()
                    .filter(meta -> (meta.getUpdatedAt() != null && meta.getUpdatedAt().after(dailyTimeSpan.getStartDate()) && meta.getVisible()))
                    .sorted(Comparator.comparing(TestGroupMeta::getStep))
                    .map(TestGroupMeta::getGroupName)
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
                return sfcSmtGroupRepository.getGroupNameByModelNameAndWorkDateAndShiftType(factory, customer, modelName, date, shift);
            } catch (ParseException e) {
                log.error("### getGroup error", e);
                return Collections.emptyList();
            }
        }
    }

    @RequestMapping("/station")
    public List<String> getStation(String factory, String customer, String modelName, String groupName, String workDate, ShiftType shiftType, String timeSpan, Boolean parameter) {
        if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory) ) {
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
                    .map(TestStationMeta::getStationName)
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
                return sfcTestGroupRepository.getStationNameByModelNameAndWorkDateAndShiftType(factory, customer, modelName, groupName, date, shift);
            } catch (ParseException e) {
                log.error("### getGroup error", e);
                return Collections.emptyList();
            }
        }
    }
}
