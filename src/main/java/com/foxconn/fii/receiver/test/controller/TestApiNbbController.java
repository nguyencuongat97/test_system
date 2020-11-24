package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.ap.model.CustKpOnline;
import com.foxconn.fii.data.ap.repository.NbbCustKpOnlineRepository;
import com.foxconn.fii.data.primary.model.entity.NbbAoiStatistics;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestLineMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestModelBomMeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMetaNbb;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.data.primary.repository.NbbAoiStatisticsRepository;
import com.foxconn.fii.data.primary.repository.TestGroupMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestLineMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestModelBomMetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestUphTargetRepository;
import com.foxconn.fii.data.sfc.model.TestWipTracking;
import com.foxconn.fii.data.sfc.repository.SfcRouteControlRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcSnDetailRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestErrorRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipKeyPartsRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipTrackingRepository;
import com.foxconn.fii.receiver.test.service.NbbTeOeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
public class TestApiNbbController {

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;

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
    private SfcTestErrorRepository sfcTestErrorRepository;

    @Autowired
    private NbbCustKpOnlineRepository nbbCustKpOnlineRepository;

    @Autowired
    private SfcSnDetailRepository sfcSnDetailRepository;

    @Autowired
    private TestModelBomMetaRepository testModelBomMetaRepository;

    @Autowired
    private NbbAoiStatisticsRepository nbbAoiStatisticsRepository;

    @Autowired
    private NbbTeOeeService nbbTeOeeService;

    @Autowired
    private SfcWipKeyPartsRepository sfcWipKeyPartsRepository;

    @Autowired
    private SfcRouteControlRepository sfcRouteControlRepository;

    @RequestMapping("/customer")
    public List<String> getCustomerOfNbb() {
        List<String> customers = testModelMetaNbbRepository.findCustomerByFactory(Factory.NBB);
        customers.sort((o1, o2) -> {
            if ("OTHER".equalsIgnoreCase(o1)) {
                return 1;
            } else if ("OTHER".equalsIgnoreCase(o2)) {
                return -1;
            } else {
                return o1.compareTo(o2);
            }
        });
        return customers;
    }

    @RequestMapping("/stage")
    public List<String> getStageOfNbb(String customer) {
        List<String> stageOrder = Arrays.asList("SMT", "SMA", "FAT", "SUB-PACK", "MAIN-PACK");
        List<String> stageList = testGroupMetaNbbRepository.findStageByFactoryAndCustomer(Factory.NBB, customer);
        stageList.sort(Comparator.comparingInt(stage -> {
            int index = stageOrder.indexOf(stage);
            return index == -1 ? 1000 : index;
        }));
        return stageList;
    }

    @RequestMapping("/line")
    public Map<String, String> getLineOfNbb(String customer, String stage, String modelName, String mo, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        try {
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomerIsNot(Factory.NBB, "Other");
            }
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

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            List<String> lineList;
            if (StringUtils.isEmpty(workDate)) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                lineList = sfcSmtGroupRepository.getLineNameByWorkDateAndShiftType(factory, customer, modelList, mo, timeSpan.getStartDate(), timeSpan.getShiftType());
            } else {
                Date date = df.parse(workDate);
                lineList = sfcSmtGroupRepository.getLineNameByWorkDateAndShiftType(factory, customer, modelList, mo, date, shiftType);
            }

            Map<String, String> lineMetaMap = testLineMetaNbbRepository.findByFactory(Factory.NBB)
                    .stream().collect(Collectors.toMap(TestLineMetaNbb::getLineName, TestLineMetaNbb::getShowName, (l1, l2) -> l1));

            return lineList.stream()
                    .collect(Collectors.toMap(line -> line, line -> lineMetaMap.getOrDefault(line, line), (l1, l2) -> l1, TreeMap::new));
        } catch (ParseException e) {
            log.error("### getLineOfNbb error", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/mo")
    public List<String> getMoOfNbb(String customer, String stage, String modelName, String lineName, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaNbbList;
            if ("ALL".equalsIgnoreCase(customer)) {
                modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomerIsNot(Factory.NBB, "Other");
            } else {
                modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);
            }

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
            log.error("### getGroupOfNbb error", e);
            return Collections.emptyList();
        }
    }

    @RequestMapping("/model")
    public List<String> getModelOfNbb(String customer, String stage, String workDate, ShiftType shiftType, String timeSpan) {
        String factory = Factory.NBB;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            List<String> modelList;

            if (!StringUtils.isEmpty(timeSpan)) {
                TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
                modelList = sfcTestGroupRepository.getModelNameByWorkDateBetween(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
                modelList.addAll(sfcSmtGroupRepository.getModelNameByWorkDateBetween(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate()));
            } else if (StringUtils.isEmpty(workDate)) {
                TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
                modelList = sfcTestGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getShiftType());
                modelList.addAll(sfcSmtGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getShiftType()));
            } else {
                Date date = df.parse(workDate);
                modelList = sfcTestGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, date, shiftType);
                modelList.addAll(sfcSmtGroupRepository.getModelNameByWorkDateAndShiftType(factory, customer, date, shiftType));
            }

            if (StringUtils.isEmpty(stage)) {
                if (StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) {
                    return testModelMetaNbbRepository.findByFactoryAndCustomerIsNot(Factory.NBB, "Other")
                            .stream().map(TestModelMetaNbb::getModelName)
                            .filter(modelList::contains)
                            .collect(Collectors.toList());
                }
                return testModelMetaNbbRepository.findModelByFactoryAndCustomer(Factory.NBB, customer)
                        .stream().filter(modelList::contains)
                        .collect(Collectors.toList());
            }

            if (StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) {
                List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomerIsNot(Factory.NBB, "Other")
                        .stream()
                        .filter(model -> stage.equalsIgnoreCase(model.getStage()) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .collect(Collectors.toList());
                return modelMetaNbbList.stream()
                        .map(TestModelMetaNbb::getModelName)
                        .filter(modelList::contains)
                        .collect(Collectors.toList());
            }

            if ("SMT".equalsIgnoreCase(stage)) {
                return testModelMetaNbbRepository.findModelByFactoryAndCustomerAndStage(Factory.NBB, customer, "SMA")
                        .stream().filter(modelList::contains)
                        .collect(Collectors.toList());
            }
            return testModelMetaNbbRepository.findModelByFactoryAndCustomerAndStage(Factory.NBB, customer, stage)
                    .stream().filter(modelList::contains)
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("### getModelOfNbb error", e);
            return Collections.emptyList();
        }
    }

    @RequestMapping("/group")
    public List<String> getGroupOfNbb(String modelName, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            if (StringUtils.isEmpty(workDate)) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                return sfcSmtGroupRepository.getGroupNameByModelNameAndWorkDateAndShiftType(factory, "", modelName, timeSpan.getStartDate(), timeSpan.getShiftType());
            } else {
                Date date = df.parse(workDate);
                return sfcSmtGroupRepository.getGroupNameByModelNameAndWorkDateAndShiftType(factory, "", modelName, date, shiftType);
            }
        } catch (ParseException e) {
            log.error("### getGroupOfNbb error", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/modelMeta")
    public List<TestModelMetaNbb> getModelMetaListOfNbb() {
        return testModelMetaNbbRepository.findByFactory(Factory.NBB);
    }

    @PutMapping("/modelMeta/{id}")
    public Boolean updateModelOfNbb(@PathVariable("id") Integer id, @RequestBody TestModelMetaNbb modelMeta) {
        TestModelMetaNbb modelMetaTarget = testModelMetaNbbRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("model meta id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(modelMeta, modelMetaTarget, "id");
        testModelMetaNbbRepository.save(modelMetaTarget);
        return true;
    }

    @GetMapping("/groupMeta")
    public List<TestGroupMetaNbb> getGroupMetaListOfNbb(String customer, String stage) {
        if (StringUtils.isEmpty(customer)) {
            return testGroupMetaNbbRepository.findByFactory(Factory.NBB);
        } else if (StringUtils.isEmpty(stage)) {
            return testGroupMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);
        } else {
            return testGroupMetaNbbRepository.findByFactoryAndCustomerAndStage(Factory.NBB, customer, stage);
        }
    }

    @PostMapping("/groupMeta")
    public Boolean addGroupOfNbb(@RequestBody TestGroupMetaNbb groupMeta) {
        if (!testGroupMetaNbbRepository.findByFactoryAndCustomerAndStageAndGroupName(Factory.NBB, groupMeta.getCustomer(), groupMeta.getStage(), groupMeta.getGroupName()).isEmpty()) {
            return false;
        }
        TestGroupMetaNbb groupMetaTarget = new TestGroupMetaNbb();
        BeanUtils.copyPropertiesIgnoreNull(groupMeta, groupMetaTarget, "id");
        testGroupMetaNbbRepository.save(groupMetaTarget);
        return true;
    }

    @PutMapping("/groupMeta/{id}")
    public Boolean updateGroupOfNbb(@PathVariable("id") Integer id, @RequestBody TestGroupMetaNbb groupMeta) {
        TestGroupMetaNbb groupMetaTarget = testGroupMetaNbbRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("group meta id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(groupMeta, groupMetaTarget, "id");
        testGroupMetaNbbRepository.save(groupMetaTarget);
        return true;
    }

    @DeleteMapping("/groupMeta/{id}")
    public Boolean removeGroupOfNbb(@PathVariable("id") Integer id, @RequestBody TestGroupMetaNbb groupMeta) {
        TestGroupMetaNbb groupMetaTarget = testGroupMetaNbbRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("group meta id %d not found", id)));

        testGroupMetaNbbRepository.delete(groupMetaTarget);
        return true;
    }

    @RequestMapping("/uph")
    public List<TestUphTarget> getUph(String customer, String modelName) {
        if (StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) {
            return testUphTargetRepository.findByFactory(Factory.NBB);
        } else if (StringUtils.isEmpty(modelName)) {
            return testUphTargetRepository.findByFactoryAndCustomer(Factory.NBB, customer);
        } else {
            return testUphTargetRepository.findByFactoryAndCustomerAndModelName(Factory.NBB, customer, modelName);
        }
    }

    @PostMapping("/uph")
    public Boolean createUph(@RequestBody TestUphTarget uph) {
        TestUphTarget uphTarget = new TestUphTarget();
        BeanUtils.copyPropertiesIgnoreNull(uph, uphTarget, "id");
        if (testUphTargetRepository.findByFactoryAndCustomerAndLineNameAndModelNameAndGroupName(Factory.NBB, uphTarget.getCustomer(), uphTarget.getLineName(), uphTarget.getModelName(), uphTarget.getGroupName()).isEmpty()) {
            testUphTargetRepository.save(uphTarget);
        }
        return true;
    }

    @PutMapping("/uph/{id}")
    public Boolean updateUph(@PathVariable("id") Integer id, @RequestBody TestUphTarget uph) {
        TestUphTarget uphTarget = testUphTargetRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(uph, uphTarget, "id");
        testUphTargetRepository.save(uphTarget);
        return true;
    }

    @DeleteMapping("/uph/{id}")
    public Boolean removeUph(@PathVariable("id") Integer id) {
        TestUphTarget uphTarget = testUphTargetRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph id %d not found", id)));

        testUphTargetRepository.delete(uphTarget);
        return true;
    }

    @RequestMapping("/group/overall")
    public Map<String, Map<String, Map<String, TestGroup>>> getGroupOverall(String customer, String stage, String modelName, String lineName, String mo, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);

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

            List<TestGroupMetaNbb> groupMetaNbbList = testGroupMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);

            Map<String, String> groupStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> groupSubStageMap = groupMetaNbbList
                    .stream().collect(Collectors.toMap(TestGroupMetaNbb::getGroupName, TestGroupMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, String> modelStageMap = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, String> modelSubStage = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getSubStage, (s1, s2) -> s1));

            Map<String, List<TestGroup>> groupMap = sfcTestGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType).stream()
                    .map(map -> SfcTestGroupRepository.mappingToTestGroup(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()));

            sfcSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(factory, customer, modelList, date, shiftType)
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                    .forEach(groupMap::putIfAbsent);

            sfcSnDetailRepository.findByModelNameAndInStationTimeBetween(factory, customer, modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
                    .stream().map(map -> SfcSnDetailRepository.mapping(factory, map))
                    .collect(Collectors.groupingBy(group -> group.getGroupName() + "_" + group.getModelName()))
                    .forEach(groupMap::putIfAbsent);

            Map<String, Map<String, Map<String, TestGroup>>> result = groupMap.values().stream().flatMap(Collection::stream)
                    .filter(group -> (StringUtils.isEmpty(stage) ||
                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName()))) &&
                            (StringUtils.isEmpty(lineName) || lineName.equalsIgnoreCase(group.getLineName())) &&
                            (StringUtils.isEmpty(mo) || mo.equalsIgnoreCase(group.getMo())))
                    .collect(
                            Collectors.groupingBy(
                                    group -> groupStageMap.getOrDefault(group.getGroupName(), ""),
                                    HashMap::new,
                                    Collectors.groupingBy(
                                            group -> group.getLineName(),
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
                                                    group -> group,
                                                    (g1, g2) -> {
                                                        g1.setWip(g1.getWip() + g2.getWip());
                                                        g1.setPass(g1.getPass() + g2.getPass());
                                                        g1.setFail(g1.getFail() + g2.getFail());
                                                        g1.setFirstFail(g1.getFirstFail() + g2.getFirstFail());
                                                        g1.setSecondFail(g1.getSecondFail() + g2.getSecondFail());
                                                        g1.setRetest(g1.getRetest() + g2.getRetest());
                                                        return g1;
                                                    }
                                            ))));

//            Map<String, Map<String, Map<String, TestGroup>>> result = new HashMap<>();
//            if (!"SMT".equalsIgnoreCase(stage)) {
//                result = nbbTestGroupRepository.findByModelNameAndWorkDateAndShiftType(modelList, date, shiftType).stream()
//                        .map(NbbTestGroupRepository::mapping)
//                        .filter(group -> (StringUtils.isEmpty(stage) ||
//                                (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
//                                        stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName()))) &&
//                                (StringUtils.isEmpty(lineName) || lineName.equalsIgnoreCase(group.getLineName())) &&
//                                (StringUtils.isEmpty(mo) || mo.equalsIgnoreCase(group.getMo())))
//                        .collect(
//                                Collectors.groupingBy(
//                                        group -> groupStageMap.getOrDefault(group.getGroupName(), ""),
//                                        HashMap::new,
//                                        Collectors.groupingBy(
//                                                group -> group.getLineName(),
//                                                HashMap::new,
//                                                Collectors.toMap(
//                                                        group -> {
//                                                            String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
//                                                            String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
//                                                            if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
//                                                                return group.getGroupName();
//                                                            }
//                                                            return group.getGroupName() + "(" + subByModel + ")";
//                                                        },
//                                                        group -> group,
//                                                        (g1, g2) -> {
//                                                            g1.setWip(g1.getWip() + g2.getWip());
//                                                            g1.setPass(g1.getPass() + g2.getPass());
//                                                            g1.setFail(g1.getFail() + g2.getFail());
//                                                            g1.setFirstFail(g1.getFirstFail() + g2.getFirstFail());
//                                                            g1.setSecondFail(g1.getSecondFail() + g2.getSecondFail());
//                                                            g1.setRetest(g1.getRetest() + g2.getRetest());
//                                                            return g1;
//                                                        }
//                                                ))));
//            }
//
//            Map<String, Map<String, Map<String, TestGroup>>> smtMap = nbbSmtGroupRepository.findByModelNameAndWorkDateAndShiftType(modelList, date, shiftType)
//                    .stream().map(NbbSmtGroupRepository::mapping)
//                    .filter(group -> (StringUtils.isEmpty(stage) ||
//                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
//                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName()))) &&
//                            (StringUtils.isEmpty(lineName) || lineName.equalsIgnoreCase(group.getLineName())) &&
//                            (StringUtils.isEmpty(mo) || mo.equalsIgnoreCase(group.getMo())))
//                    .collect(
//                            Collectors.groupingBy(
//                                    group -> groupStageMap.getOrDefault(group.getGroupName(), ""),
//                                    HashMap::new,
//                                    Collectors.groupingBy(
//                                            group -> group.getLineName(),
//                                            HashMap::new,
//                                            Collectors.toMap(
//                                                    group -> {
//                                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
//                                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
//                                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
//                                                            return group.getGroupName();
//                                                        }
//                                                        return group.getGroupName() + "(" + subByModel + ")";
//                                                    },
//                                                    group -> group,
//                                                    (g1, g2) -> {
//                                                        g1.setWip(g1.getWip() + g2.getWip());
//                                                        g1.setPass(g1.getPass() + g2.getPass());
//                                                        g1.setFail(g1.getFail() + g2.getFail());
//                                                        g1.setFirstFail(g1.getFirstFail() + g2.getFirstFail());
//                                                        g1.setSecondFail(g1.getSecondFail() + g2.getSecondFail());
//                                                        g1.setRetest(g1.getRetest() + g2.getRetest());
//                                                        return g1;
//                                                    }
//                                            ))));
//
//            for (Map.Entry<String, Map<String, Map<String, TestGroup>>> entry : smtMap.entrySet()) {
//                if (result.containsKey(entry.getKey())) {
//                    for (Map.Entry<String, Map<String, TestGroup>> entryLine : entry.getValue().entrySet()) {
//                        if (!result.get(entry.getKey()).containsKey(entryLine.getKey())) {
//                            result.get(entry.getKey()).put(entryLine.getKey(), entryLine.getValue());
//                        }
//                    }
//                }
//            }
//
//            Map<String, Map<String, Map<String, TestGroup>>> otherMap = nbbSnDetailRepository.findByModelNameAndInStationTimeBetween(modelList, timeSpan.getStartDate(), timeSpan.getEndDate())
//                    .stream().map(NbbSnDetailRepository::mapping)
//                    .filter(group -> (StringUtils.isEmpty(stage) ||
//                            (stage.equalsIgnoreCase(modelStageMap.get(group.getModelName())) || ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(modelStageMap.get(group.getModelName())))) &&
//                                    stage.equalsIgnoreCase(groupStageMap.get(group.getGroupName()))) &&
//                            (StringUtils.isEmpty(lineName) || lineName.equalsIgnoreCase(group.getLineName())) &&
//                            (StringUtils.isEmpty(mo) || mo.equalsIgnoreCase(group.getMo())))
//                    .collect(
//                            Collectors.groupingBy(
//                                    group -> groupStageMap.getOrDefault(group.getGroupName(), ""),
//                                    HashMap::new,
//                                    Collectors.groupingBy(
//                                            group -> group.getLineName(),
//                                            HashMap::new,
//                                            Collectors.toMap(
//                                                    group -> {
//                                                        String subByGroup = groupSubStageMap.getOrDefault(group.getGroupName(), "");
//                                                        String subByModel = modelSubStage.getOrDefault(group.getModelName(), "");
//                                                        if (StringUtils.isEmpty(subByGroup) || StringUtils.isEmpty(subByModel)) {
//                                                            return group.getGroupName();
//                                                        }
//                                                        return group.getGroupName() + "(" + subByModel + ")";
//                                                    },
//                                                    group -> group,
//                                                    (g1, g2) -> {
//                                                        g1.setWip(g1.getWip() + g2.getWip());
//                                                        g1.setPass(g1.getPass() + g2.getPass());
//                                                        g1.setFail(g1.getFail() + g2.getFail());
//                                                        g1.setFirstFail(g1.getFirstFail() + g2.getFirstFail());
//                                                        g1.setSecondFail(g1.getSecondFail() + g2.getSecondFail());
//                                                        g1.setRetest(g1.getRetest() + g2.getRetest());
//                                                        return g1;
//                                                    }
//                                            ))));
//
//            for (Map.Entry<String, Map<String, Map<String, TestGroup>>> entry : otherMap.entrySet()) {
//                if (result.containsKey(entry.getKey())) {
//                    for (Map.Entry<String, Map<String, TestGroup>> entryLine : entry.getValue().entrySet()) {
//                        if (!result.get(entry.getKey()).containsKey(entryLine.getKey())) {
//                            result.get(entry.getKey()).put(entryLine.getKey(), entryLine.getValue());
//                        }
//                    }
//                }
//            }

            List<String> groupList = groupMetaNbbList.stream().map(TestGroupMetaNbb::getGroupName).collect(Collectors.toList());

            result.forEach((s, lineMap) -> {
                for (Map.Entry<String, Map<String, TestGroup>> entryLine : lineMap.entrySet()) {
                    entryLine.setValue(entryLine.getValue().entrySet().stream()
                            .sorted(Comparator.comparingInt(entry -> {
                                int index = groupList.indexOf(entry.getKey().split("[(]")[0]);
                                return index == -1 ? 1000 : index;
                            }))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
                }
            });

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/wip-group")
    public Map<String, Map<String, TestGroup>> getWipGroupByMo(String mo, String serial) {
        String factory = Factory.NBB;
        if (StringUtils.isEmpty(mo) && StringUtils.isEmpty(serial)) {
            return new HashMap<>();
        }

        if (StringUtils.isEmpty(mo)) {
            List<TestWipTracking> serialWipTracking = getSerialNumberDetailWipGroup(serial);
            if (serialWipTracking.isEmpty()) {
                return new HashMap<>();
            }
            mo = serialWipTracking.get(0).getMo();
        }

        List<String> groupNameList = sfcRouteControlRepository.getGroupListByMoNumber(factory, "", mo);

        List<TestGroup> groupList = sfcWipTrackingRepository.findByMoNumber(factory, "", StringUtils.isEmpty(mo) ? Collections.emptyList() : Collections.singletonList(mo), serial)
                .stream().map(SfcWipTrackingRepository::mapping).collect(Collectors.toList());

        Map<String, Map<String, TestGroup>> result = new HashMap<>();
        Map<String, Map<String, TestGroup>> tmp = groupList.stream().collect(Collectors.groupingBy(TestGroup::getLineName,
                Collectors.toMap(
                        TestGroup::getGroupName,
                        group -> group,
                        (g1, g2) -> g1
                )));

        tmp.forEach((line, tmpGroupMap) -> {
            Map<String, TestGroup> groupMap = new LinkedHashMap<>();
            Map.Entry<String, TestGroup> entry = tmpGroupMap.entrySet().iterator().next();
            for(String group : groupNameList) {
                if (tmpGroupMap.containsKey(group)) {
                    groupMap.put(group, tmpGroupMap.get(group));
                } else {
                    TestGroup g = new TestGroup();
                    BeanUtils.copyPropertiesIgnoreNull(entry.getValue(), g, "groupName", "wip");
                    g.setGroupName(group);
                    g.setWip(0);
                    groupMap.put(group, g);
                }
            }
            tmpGroupMap.forEach((groupName, group) -> {
                if (!groupMap.containsKey(groupName)) {
                    groupMap.put(groupName, group);
                }
            });
            result.put(line, groupMap);
        });

        return result;
    }

    @RequestMapping("/wip-group-detail")
    public List<TestWipTracking> getWipGroupDetail(String mo, String wipGroup, String serial) {
        String factory = Factory.NBB;
        return sfcWipTrackingRepository.findByMoNumberAndWipGroup(factory, "", Arrays.asList(mo), Arrays.asList(wipGroup))
                .stream()
                .map(SfcWipTrackingRepository::mappingWipTracking)
                .filter(tracking -> StringUtils.isEmpty(serial) || serial.equalsIgnoreCase(tracking.getSerialNumber()))
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/wip-group-history")
    public List<TestWipTracking> getWipGroupHistory(String mo, String wipGroup, String serial) {
        if (StringUtils.isEmpty(mo) || StringUtils.isEmpty(wipGroup)) {
            return Collections.emptyList();
        }

        return sfcSnDetailRepository.findByMoAndGroupName(Factory.NBB, "", mo, wipGroup)
                .stream()
                .map(SfcSnDetailRepository::mappingWipTracking)
                .filter(tracking -> StringUtils.isEmpty(serial) || serial.equalsIgnoreCase(tracking.getSerialNumber()))
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/serial-detail")
    public List<TestWipTracking> getSerialNumberDetail(String serial) {
        return sfcSnDetailRepository.findBySerialNumber(Factory.NBB, "", serial)
                .stream().map(SfcSnDetailRepository::mappingWipTracking)
                .sorted(Comparator.comparing(TestWipTracking::getInStationTime))
                .collect(Collectors.toList());
    }

    @RequestMapping("/serial-detail-wip-group")
    public List<TestWipTracking> getSerialNumberDetailWipGroup(String serial) {
        return sfcWipTrackingRepository.findBySerialNumber(Factory.NBB, "", serial)
                .stream().map(SfcSnDetailRepository::mappingWipTracking)
                .collect(Collectors.toList());
    }

    @RequestMapping("/error/overall")
    public Map<String, Map<String, TestError>> getErrorOverall(String customer, String stage, String modelName, String lineName, String mo, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            List<TestModelMetaNbb> modelMetaNbbList = testModelMetaNbbRepository.findByFactoryAndCustomer(Factory.NBB, customer);

            List<String> modelList = new ArrayList<>();
            if (StringUtils.isEmpty(modelName)) {
                modelList = modelMetaNbbList.stream()
                        .filter(model -> StringUtils.isEmpty(stage) || stage.equalsIgnoreCase(model.getStage()) ||
                                ("SMT".equalsIgnoreCase(stage) && "SMA".equalsIgnoreCase(model.getStage())))
                        .map(TestModelMetaNbb::getModelName)
                        .collect(Collectors.toList());
            } else {
                modelList.add(modelName);
            }

            if (modelList.isEmpty()) {
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

            Map<String, String> modelStageMap = modelMetaNbbList
                    .stream().collect(Collectors.toMap(TestModelMetaNbb::getModelName, TestModelMetaNbb::getStage, (s1, s2) -> s1));

            Map<String, Map<String, TestError>> result = sfcTestErrorRepository.findByModel(factory, customer, modelList, date, shiftType).stream()
                    .map(map -> SfcTestErrorRepository.mapToTestError(factory, map))
                    .filter(error -> (StringUtils.isEmpty(lineName) || lineName.equalsIgnoreCase(error.getLineName())) &&
                            (StringUtils.isEmpty(mo) || mo.equalsIgnoreCase(error.getMo())))
                    .collect(
                            Collectors.groupingBy(
                                    error -> modelStageMap.getOrDefault(error.getModelName(), ""),
                                    HashMap::new,
                                    Collectors.toMap(
                                            error -> StringUtils.isEmpty(error.getErrorDescription()) ? error.getErrorCode() : error.getErrorDescription(),
                                            group -> group,
                                            (e1, e2) -> {
                                                e1.setFail(e1.getFail() + e2.getFail());
                                                e1.setTestFail(e1.getTestFail() + e2.getTestFail());
                                                return e1;
                                            }
                                    )));


            for (Map.Entry<String, Map<String, TestError>> entryResult : result.entrySet()) {
                entryResult.setValue(entryResult.getValue().entrySet().stream()
                        .sorted(Collections.reverseOrder(Comparator.comparing(entry -> {
                            return entry.getValue().getTestFail() + entry.getValue().getFail();
                        })))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)));
            }

            return result;
        } catch (Exception e) {
            log.error("", e);
            return new HashMap<>();
        }
    }

    @GetMapping("/allpart/kitting")
    public List<CustKpOnline> getWaitingKitting() {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);

        List<CustKpOnline> onlineList = nbbCustKpOnlineRepository.findByWorkTimeBetween(timeSpan.getStartDate(), timeSpan.getEndDate())
                .stream().map(NbbCustKpOnlineRepository::mapping)
                .sorted(Comparator.comparing(CustKpOnline::getTime))
                .collect(Collectors.toList());

        return onlineList;
    }

    @GetMapping("/allpart/material")
    public List<TestModelBomMeta> getMaterialQtyByMo(String customer, String stage, String modelName, String mo, String workDate, ShiftType shiftType) {
        String factory = Factory.NBB;
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);

        List<String> moList;
        if (StringUtils.isEmpty(mo)) {
            moList = getMoOfNbb(customer, stage, modelName, null, workDate, shiftType);
        } else {
            moList = Collections.singletonList(mo);
        }

        Map<String, Integer> outputGroupMap = sfcSmtGroupRepository.findByMoNumber(factory, customer, modelName, null)
                .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map))
                .collect(Collectors.toMap(
                        TestGroup::getGroupName,
                        TestGroup::getPass,
                        (o1, o2) -> o1 + o2
                ));

        Map<String, Integer> deliverMap = nbbCustKpOnlineRepository.findByMoNumber(null)
                .stream().map(NbbCustKpOnlineRepository::mapping)
                .collect(Collectors.toMap(
                        CustKpOnline::getCustKpNo,
                        CustKpOnline::getDeliverQty,
                        (d1, d2) -> d1));

        Map<String, Integer> kittingMap = nbbCustKpOnlineRepository.getKittingStored()
                .stream().map(NbbCustKpOnlineRepository::mapping)
                .collect(Collectors.toMap(
                        CustKpOnline::getCustKpNo,
                        CustKpOnline::getDeliverQty,
                        (d1, d2) -> d1));

        Map<String, Integer> whsMap = nbbCustKpOnlineRepository.getWhsStored()
                .stream().map(NbbCustKpOnlineRepository::mapping)
                .collect(Collectors.toMap(
                        CustKpOnline::getCustKpNo,
                        CustKpOnline::getDeliverQty,
                        (d1, d2) -> d1));

        List<TestModelBomMeta> result = testModelBomMetaRepository.findByFactoryAndModelName(Factory.NBB, modelName);

        for (TestModelBomMeta bomMeta : result) {
            bomMeta.setCheckoutQty(outputGroupMap.getOrDefault(bomMeta.getOutputGroupName(), 0) * bomMeta.getStandardQty());
            bomMeta.setDeliverQty(deliverMap.getOrDefault(bomMeta.getKpNo(), 0));
            bomMeta.setKittingQty(kittingMap.getOrDefault(bomMeta.getKpNo(), 0));
            bomMeta.setWhsQty(whsMap.getOrDefault(bomMeta.getKpNo(), 0));
        }

        result.sort(Comparator.comparing(TestModelBomMeta::getRemainQty));

        return result;
    }

    @RequestMapping("/aoi/model")
    public List<String> getAoiModelOfNbb(String lineName, String workDate, ShiftType shiftType, String timeSpan) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        Date date;
        Calendar calendar = Calendar.getInstance();
        TimeSpan fullTS;
        try {
            date = df.parse(workDate);
            fullTS = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);
        } catch (Exception ignored) {
            fullTS = TimeSpan.now(TimeSpan.Type.DAILY);
            calendar.setTime(fullTS.getStartDate());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            date = calendar.getTime();
            shiftType = fullTS.getShiftType();
        }

        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startSection = "08";
        String endSection = "08";
        if (shiftType == ShiftType.DAY) {
            endSection = "20";
        } else if (shiftType == ShiftType.NIGHT) {
            startSection = "20";
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return nbbAoiStatisticsRepository.getModelNameByFactoryAndLineNameAndWorkDateBetween(Factory.NBB, lineName, sdf.format(date), startSection, sdf.format(calendar.getTime()), endSection);
    }

    @GetMapping("/aoi/io")
    public List<NbbAoiStatistics> getAoiStatistics(String lineName, String modelName, String workDate, ShiftType shiftType) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

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

        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startSection = "08";
        String endSection = "08";
        if (shiftType == ShiftType.DAY) {
            endSection = "20";
        } else if (shiftType == ShiftType.NIGHT) {
            startSection = "20";
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        List<NbbAoiStatistics> tmpList = nbbAoiStatisticsRepository.findByFactoryAndLineNameAndWorkDateBetween(Factory.NBB, lineName, sdf.format(date), startSection, sdf.format(calendar.getTime()), endSection)
                .stream().filter(aoi -> StringUtils.isEmpty(modelName) || modelName.equalsIgnoreCase(aoi.getModelName()))
                .sorted(Comparator.comparing(NbbAoiStatistics::getStartDate).thenComparing(NbbAoiStatistics::getModelName))
                .collect(Collectors.toList());

        Map<String, NbbAoiStatistics> total = tmpList.stream()
                .collect(Collectors.toMap(
                        NbbAoiStatistics::getModelName,
                        aoi -> {
                            NbbAoiStatistics ins = new NbbAoiStatistics();
                            BeanUtils.copyPropertiesIgnoreNull(aoi, ins);
                            ins.setTimeSpan("Total");
                            return ins;
                        },
                        (a1, a2) -> {
                            a1.setPass(a1.getPass() + a2.getPass());
                            a1.setWkFail(a1.getWkFail() + a2.getWkFail());
                            a1.setRepass(a1.getRepass() + a2.getRepass());
                            a1.setRefail(a1.getRefail() + a2.getRefail());
                            return a1;
                        }));

        tmpList.addAll(total.values());

        return tmpList;
    }

    // OEE BEGIN (Giang)
    @GetMapping("/equipment/used/count")
    public Object testEquipmentUsedCount(@RequestParam String customer, @RequestParam(required = false, defaultValue = "") String stage, @RequestParam(required = false, defaultValue = "") String group, @RequestParam(required = false, defaultValue = "") String line, @RequestParam(required = false, defaultValue = "") String station, @RequestParam(required = false, defaultValue = "") String startDate, @RequestParam(required = false, defaultValue = "") String endDate) throws ParseException {

        // return nbbTeOeeService.countData(factory, customer);

        return nbbTeOeeService.getOverviewData(customer, stage, group, line, station, startDate, endDate);

    }

//    @GetMapping("/oee/station/status")
//    public Object oeeStationStatus(String customer, String stage, String group) {
//        return nbbTeOeeService.stationStatus(customer, stage, group);
//    }

    @GetMapping("/oee/station/status/v2")
    public Object oeeStationStatusV2(String customer, String stage, String group) {
        return nbbTeOeeService.stationStatusV2(customer, stage, group);
    }

    @GetMapping("/oee/list/group")
    public Object oeeListGroup(String customer, String stage) {
        return testGroupMetaNbbRepository.findGroupByFactoryAndCustomerAndStage(Factory.NBB, customer, stage);
    }
    // OEE END

    // VLRR (Tung)
    @GetMapping("vlrr/getdata")
    public Object getListKeypartLike(String keyPartNo, String keyPart, Integer page, String timeSpan) throws ParseException {
        String factory = Factory.NBB;
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<String> dataList = new ArrayList<>();
        Integer rowPage = 25;
        ///  String [] srtKeyPartNo = keyPartNo.split(";");
        List<String> listKeyPartNo = Arrays.asList(keyPartNo.split(";"));
        Integer rowStart = rowPage * (page - 1);
        Integer rowEnd = rowPage * page;
        //  Pageable pageable = PageRequest.of(pageIndex, pageSize);
        if (keyPartNo.equalsIgnoreCase("")) {
            switch (keyPart) {
                case "0DU":
                    dataList.add("G852-01064-01");
                    dataList.add("G852-01237-01");
                    dataList.add("G852-00739-01");
                    dataList.add("G852-00741-01");
                    dataList.add("G852-01075-01");
                    dataList.add("G864-00168-01");
                    dataList.add("G852-01064-02");
                    dataList.add("G852-01064-03");

                    break;
                case "1F3":
                    dataList.add("G852-01064-01");
                    dataList.add("G852-01237-01");
                    dataList.add("G852-00739-01");
                    dataList.add("G852-00741-01");
                    dataList.add("G850-00134-01");
                    dataList.add("G850-00103-02");

                    break;
                case "1DM":
                    dataList.add("G804-00370-01");
                    dataList.add("G804-00293-01");
                    dataList.add("G804-00370-02");
                    dataList.add("G804-00370-03");
                    break;
                case "0ET":
                    dataList.add("G804-000370-01");
                    dataList.add("G804-00293-01");
                default:
                    System.out.println("dataList.size == 0");
            }
        }
        if (dataList.size() == 0) {
            //   String dde = "";
//            if (serialNumber.equalsIgnoreCase("SM") || serialNumber.equalsIgnoreCase("SV")){
            List<Map<String, Object>> data = sfcWipKeyPartsRepository.findByKeyPartLikeIndex1(factory, "", keyPart, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), rowStart, rowEnd, listKeyPartNo);
            return data;
        } else {
            List<Map<String, Object>> data = sfcWipKeyPartsRepository.findByKeyPartLikeIndex1(factory, "", keyPart, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), rowStart, rowEnd, dataList);
            log.info("pause");
            return data;
        }
        //   }else{
        //  List<Map<String, Object>> data =  nbbWipKeyPartsRepository.findByKeyPartLikeIndex(keyPart, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), rowStart, rowEnd);
        //    return false;
        //  }

    }

    @GetMapping("vlrr/pageIndex")
    public Map<String, Object> getLisdtKeypartLike(String keyPart, String keyPartNo, String timeSpan) throws ParseException {
        String factory = Factory.NBB;
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<String> dataList = new ArrayList<>();
        List<String> listKeyPartNo = Arrays.asList(keyPartNo.split(";"));
        if (keyPartNo.equalsIgnoreCase("")) {
            switch (keyPart) {
                case "0DU":
                    dataList.add("G852-01064-01");
                    dataList.add("G852-01237-01");
                    dataList.add("G852-00739-01");
                    dataList.add("G852-00741-01");
                    dataList.add("G852-01075-01");
                    dataList.add("G864-00168-01");
                    dataList.add("G852-01064-02");
                    dataList.add("G852-01064-03");

                    break;
                case "1F3":
                    dataList.add("G852-01064-01");
                    dataList.add("G852-01237-01");
                    dataList.add("G852-00739-01");
                    dataList.add("G852-00741-01");
                    dataList.add("G850-00134-01");
                    dataList.add("G850-00103-02");

                    break;
                case "1DM":
                    dataList.add("G804-00370-01");
                    dataList.add("G804-00293-01");
                    dataList.add("G804-00370-02");
                    dataList.add("G804-00370-03");
                    break;
                case "0ET":
                    dataList.add("G804-000370-01");
                    dataList.add("G804-00293-01");
                default:
                    System.out.println("dataList.size == 0");
            }
        }
        Integer rowPage = 25;
        Object object = new Object();
        if (dataList.size() == 0) {
            object = sfcWipKeyPartsRepository.countPageIndex(factory, "", keyPart, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listKeyPartNo);
        } else {
            object = sfcWipKeyPartsRepository.countPageIndex(factory, "", keyPart, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), dataList);

        }

        Map<String, Object> tmpMap = (Map<String, Object>) ((ArrayList) object).get(0);
        BigDecimal bigDecimal = (BigDecimal) tmpMap.get("qty");
        Integer qty = bigDecimal.intValue();
        Map<String, Object> data = new HashMap<>();
        data.put("totalData", qty);
        if (qty <= rowPage) {
            data.put("pageQty", 1);
        } else {
            float pageIndex = qty / rowPage;
            data.put("pageQty", pageIndex + 1);
        }
        return data;
    }

    @GetMapping("vlrr/exportKeyPart")
    public void exportDataByKeyPart(HttpServletResponse resonse, @RequestParam String keyPart, @RequestParam(required = false, defaultValue = "") String keyPartNo, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException, IOException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        nbbTeOeeService.exportDataKeyPart(resonse, keyPart, keyPartNo, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
    }
    // END VLRR


}
