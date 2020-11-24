package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.TestGroupDailyRepository;
import com.foxconn.fii.data.primary.repository.TestGroupRepository;
import com.foxconn.fii.data.primary.repository.TestMoTypeConfigRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestPlanMetaRepository;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestModelServiceImpl implements TestModelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private TestGroupDailyRepository testGroupDailyRepository;

    @Autowired
    private TestPlanMetaRepository testPlanMetaRepository;

    @Autowired
    private TestMoTypeConfigRepository testMoTypeConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void saveAll(List<TestModel> modelList) {
        if (modelList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
            "merge into test_model as target " +
                    "using(select factory=?, model_name=?, start_date=?, end_date=?, wip=?, first_fail=?, second_fail=?, pass=?, fail=?) as source " +
                    "   on target.factory=source.factory and target.model_name=source.model_name and target.start_date=source.start_date and target.end_date=source.end_date " +
                    "when matched then " +
                    "   update set " +
                    "   target.wip=source.wip, " +
                    "   target.first_fail=source.first_fail, " +
                    "   target.second_fail=source.second_fail, " +
                    "   target.pass=source.pass, " +
                    "   target.fail=source.fail " +
                    "when not matched then " +
                    "   insert (factory, model_name, start_date, end_date, wip, first_fail, second_fail, pass, fail) " +
                    "   values(source.factory, source.model_name, source.start_date, source.end_date, source.wip, source.first_fail, source.second_fail, source.pass, source.fail);",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    TestModel model = modelList.get(i);
                    preparedStatement.setString(1, model.getFactory());
                    preparedStatement.setString(2, model.getModelName());
                    preparedStatement.setTimestamp(3, new Timestamp(model.getStartDate().getTime()));
                    preparedStatement.setTimestamp(4, new Timestamp(model.getEndDate().getTime()));
                    preparedStatement.setInt(5, model.getWip());
                    preparedStatement.setInt(6, model.getFirstFail());
                    preparedStatement.setInt(7, model.getSecondFail());
                    preparedStatement.setInt(8, model.getPass());
                    preparedStatement.setInt(9, model.getFail());
                }

                @Override
                public int getBatchSize() {
                    return modelList.size();
                }
            });
    }

    @Override
    public void saveAllMeta(List<TestModel> modelList) {
        if (modelList.isEmpty()) {
            return;
        }

        List<TestModelMeta> modelMetaList = new ArrayList<>();
        for (TestModel model : modelList) {
            TestModelMeta modelMeta = new TestModelMeta();
            modelMeta.setFactory(model.getFactory());
            modelMeta.setModelName(model.getModelName());

            modelMetaList.add(modelMeta);
        }

        jdbcTemplate.batchUpdate(
            "merge into test_model_meta as target " +
                    "using(select factory=?, model_name=?, created_at=?, updated_at=?) as source " +
                    "   on target.factory=source.factory and target.model_name=source.model_name " +
                    "when matched then " +
                    "   update set " +
                    "   target.updated_at=source.updated_at " +
                    "when not matched then " +
                    "   insert (factory, model_name, created_at, updated_at) " +
                    "   values(source.factory, source.model_name, source.created_at, source.updated_at);",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    TestModelMeta model = modelMetaList.get(i);
                    preparedStatement.setString(1, model.getFactory());
                    preparedStatement.setString(2, model.getModelName());
                    preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                }

                @Override
                public int getBatchSize() {
                    return modelMetaList.size();
                }
            });
    }

    @Override
    public void updateAllMeta(List<TestModelMeta> modelMetaList) {
        if (modelMetaList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into test_model_meta as target " +
                        "using(select factory=?, model_name=?, parameter=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.parameter=source.parameter, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, parameter, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.parameter, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestModelMeta model = modelMetaList.get(i);
                        preparedStatement.setString(1, model.getFactory());
                        preparedStatement.setString(2, model.getModelName());
                        preparedStatement.setBoolean(3, model.getParameter());
                        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return modelMetaList.size();
                    }
                });
    }

    @Override
    public void save(TestModelMeta modelMeta) {
        testModelMetaRepository.save(modelMeta);
    }

    @Override
    public void delete(TestModelMeta modelMeta) {
        testModelMetaRepository.deleteById(modelMeta.getId());
    }

    @Override
    public List<TestModelMeta> getModelMetaList() {
        return testModelMetaRepository.findAll();
    }

    @Override
    public List<TestModelMeta> getModelMetaList(String factory) {
        return testModelMetaRepository.findAllByFactoryAndParameterIsTrueAndVisibleIsTrue(factory);
    }

    @Override
    public List<TestModelMeta> getModelMetaList(String factory, Boolean parameter) {
        if (parameter == null) {
            return testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        return testModelMetaRepository.findAllByFactoryAndParameterIsTrueAndVisibleIsTrue(factory);
    }

    @Override
    public List<TestModelMeta> getModelMetaList(String factory, String customer, String stage) {
        if (!StringUtils.isEmpty(customer) && !StringUtils.isEmpty(stage)) {
            return testModelMetaRepository.findByFactoryAndCustomerAndStageAndVisibleIsTrue(factory, customer, stage);
        } else if (!StringUtils.isEmpty(customer)) {
            return testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, customer);
        }
        return testModelMetaRepository.findAllByFactory(factory);
    }

    @Override
    public Map<String, TestModelMeta> getModelMetaMap() {
        return testModelMetaRepository.findAll().stream()
                .collect(Collectors.toMap(modelMeta -> modelMeta.getFactory() + '_' + modelMeta.getModelName(), modelMeta -> modelMeta));
    }

    @Override
    public TestModelMeta getModelMeta(String factory, String modelName) {
        return testModelMetaRepository.findByFactoryAndModelName(factory, modelName);
    }

    @Override
    public List<TestModel> getModelList(String factory, Date startDate, Date endDate, ShiftType shiftType, MoType moType) {
        Map<String, Map<String, TestGroupDaily>> groupMapByModel = testGroupService.getGroupDailyMapByModel(factory, startDate, endDate, moType);

        Map<String, Integer> planMap = testPlanService.getPlanQtyByModelMap(factory, startDate, endDate, TestPlanMeta.Type.DAILY);

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

        List<TestModel> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, TestGroupDaily>> entry : groupMapByModel.entrySet()) {
            TestModel model = new TestModel();
            model.setFactory(factory);
            model.setModelName(entry.getKey());
            if (modelMetaMap.containsKey(model.getModelName())) {
                model.setCustomer(modelMetaMap.get(model.getModelName()).getCustomer());
            }

            model.setPlan(planMap.getOrDefault(entry.getKey(), 0));
            model.setGroupDailyList(
                    new ArrayList<>(entry.getValue().values()),
                    testGroupMetaMap.getOrDefault(Factory.NBB.equalsIgnoreCase(factory) ? model.getCustomer() : model.getModelName(), new HashMap<>()));
            model.setStartDate(startDate);
            model.setEndDate(endDate);
            result.add(model);
        }

        result.sort(Comparator.comparing(TestModel::getOutput, Comparator.reverseOrder()).thenComparing(TestModel::getRetestRate, Comparator.reverseOrder()));

        return result;
    }

    @Override
    public TestModel getModel(String factory, String modelName, TimeSpan timeSpan, Boolean isCustomer, MoType moType) {
        List<TestGroupDaily> groupDailyList = testGroupService.getGroupDailyList(factory, modelName, timeSpan.getStartDate(), timeSpan.getEndDate(), "", moType);
        if (isCustomer != null && isCustomer) {
            int output = -1;
            int outputFTRC = -1;
            int totalSF = 0;
            for (TestGroupDaily group : groupDailyList) {
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

            for (TestGroupDaily group : groupDailyList) {
                float retestRateTmp = group.getRetestRate();
                if (totalSF > fakeSF) {
                    if (remainSF > 0) {
                        group.setSecondFail((int) Math.round(Math.ceil(group.getSecondFail() * 1.0f / totalSF * fakeSF)));
                    } else {
                        group.setSecondFail(0);
                    }
                    remainSF -= group.getSecondFail();
                }

                if (retestRateTmp > 2) {
                    group.setFirstFail(Math.round(group.getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + group.getSecondFail());
                } else if (retestRateTmp == 0) {
                    group.setFirstFail(Math.round(group.getWip() * 0.2f / 100) + group.getSecondFail());
                } else {
                    group.setFirstFail((int) Math.round(Math.floor(group.getWip() * retestRateTmp / 100) + group.getSecondFail()));
                }
            }
        }

        Map<String, TestGroupMeta> groupMetaMap = testGroupService.getGroupMetaList(factory, modelName, null)
                .stream().filter(TestGroupMeta::getVisible).collect(Collectors.toMap(TestGroupMeta::getGroupName, g -> g, (g1, g2) -> g1));

        TestModel model = new TestModel();
        model.setGroupDailyList(groupDailyList, groupMetaMap);
        if (model.getGroupList().isEmpty()) {
            return null;
        }

        model.setFactory(factory);
        model.setModelName(modelName);
        model.setStartDate(timeSpan.getStartDate());
        model.setEndDate(timeSpan.getEndDate());
        return model;
    }

    @Override
    public List<TestModel> getAllModelDaily(String factory, String customer, TimeSpan timeSpan, Boolean isCustomer, MoType moType) {
        Map<String, TestModelMeta> modelMetaMap = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory)
                .stream().collect(Collectors.toMap(TestModelMeta::getModelName, m -> m, (m1, m2) -> m1));

        List<TestGroupMeta> groupMetaList = testGroupService.getGroupMetaList(factory, null, null);
        Map<String, List<String>> groupMetaMap = groupMetaList
                .stream().filter(TestGroupMeta::getVisible).collect(
                        Collectors.groupingBy(
                                g -> {
                                    if (Factory.NBB.equalsIgnoreCase(factory)) {
                                        return g.getCustomer();
                                    }
                                    return g.getModelName();
                                },
                                Collectors.mapping(TestGroupMeta::getGroupName, Collectors.toList())));

        Map<String, Map<String, TestGroupMeta>> testGroupMetaMap = groupMetaList
                .stream().filter(TestGroupMeta::getVisible).collect(
                        Collectors.groupingBy(
                                g -> {
                                    if (Factory.NBB.equalsIgnoreCase(factory)) {
                                        return g.getCustomer();
                                    }
                                    return g.getModelName();
                                },
                                Collectors.toMap(TestGroupMeta::getGroupName, g -> g, (g1, g2) -> g1)));

        Map<String, Map<String, TestGroupDaily>> groupMapByModel = testGroupService.getGroupDailyMapByModel(factory, timeSpan.getStartDate(), timeSpan.getEndDate(), moType);

        if (isCustomer) {
            int iBase = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            List<Float> fixedRRList = Arrays.asList(1.91f, 2.0f, 1.96f, 1.85f, 2.03f, 1.8f, 1.88f);
            List<Float> fixedSFList = Arrays.asList(0.43f, 0.41f, 0.52f, 0.47f, 0.45f, 0.49f, 0.50f);

            groupMapByModel.forEach((modelName, groupMap) -> {
                int output = -1;
                int outputFTRC = -1;
                int totalSF = 0;
                for (Map.Entry<String, TestGroupDaily> entry : groupMap.entrySet()) {
                    if (output == -1 || entry.getValue().getPass() < output) {
                        output = entry.getValue().getPass();
                    }
                    if ((entry.getKey().startsWith("FT") || entry.getKey().startsWith("RC")) && (outputFTRC == -1 || entry.getValue().getPass() < outputFTRC)) {
                        outputFTRC = entry.getValue().getPass();
                    }
                    totalSF += entry.getValue().getSecondFail();
                }
                if (outputFTRC > -1) {
                    output = outputFTRC;
                }

                int fakeSF =  Math.round(output * fixedSFList.get(iBase % fixedSFList.size()) / 100);
                int remainSF = fakeSF;

                for (Map.Entry<String, TestGroupDaily> entry : groupMap.entrySet()) {
                    float retestRateTmp = entry.getValue().getRetestRate();
                    if (totalSF > fakeSF) {
                        if (remainSF > 0) {
                            entry.getValue().setSecondFail((int) Math.round(Math.ceil(entry.getValue().getSecondFail() * 1.0f / totalSF * fakeSF)));
                        } else {
                            entry.getValue().setSecondFail(0);
                        }
                        remainSF -= entry.getValue().getSecondFail();
                    }

                    if (retestRateTmp > 2) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + entry.getValue().getSecondFail());
                    } else if (retestRateTmp == 0) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * 0.2f / 100) + entry.getValue().getSecondFail());
                    } else {
                        entry.getValue().setFirstFail((int) Math.round(Math.floor(entry.getValue().getWip() * retestRateTmp / 100) + entry.getValue().getSecondFail()));
                    }
                }
            });
        }

        List<TestModel> data = new ArrayList<>();
        Set<String> dataModel = new HashSet<>();

        Map<String, Integer> planMap = testPlanService.getPlanQtyByModelMap(factory, timeSpan.getStartDate(), timeSpan.getEndDate(), TestPlanMeta.Type.DAILY);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(timeSpan.getStartDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endMonth = calendar.getTime();

        Map<String, Integer> planMonthMap = testPlanService.getPlanQtyByModelMap(factory, startMonth, endMonth, TestPlanMeta.Type.MONTHLY);
        Map<String, Integer> planMonthMap2 = testPlanService.getPlanQtyByModelMap(factory, startMonth, endMonth, TestPlanMeta.Type.DAILY);

        for (Map.Entry<String, Map<String, TestGroupDaily>> entry : groupMapByModel.entrySet()) {
            TestModel testModel = new TestModel();
            testModel.setFactory(factory);
            testModel.setModelName(entry.getKey());

            TestModelMeta modelMeta = modelMetaMap.get(testModel.getModelName());
            if (modelMeta != null) {
                testModel.setCustomer(modelMeta.getCustomer());
                testModel.setCustomerName(modelMeta.getCustomerName());
            }

            if (!StringUtils.isEmpty(customer)) {
                if (modelMeta == null || !customer.equalsIgnoreCase(modelMeta.getCustomer())) {
                    continue;
                }
            }

//            List<TestGroupDaily> groupList = entry.getValue().values().stream()
//                    .filter(group -> Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory) ||
//                            (groupMetaMap.containsKey(group.getModelName()) && groupMetaMap.get(group.getModelName()).contains(group.getGroupName())))
//                    .collect(Collectors.toList());
//            if (groupList.isEmpty()) {
//                continue;
//            }

            testModel.setGroupDailyList(new ArrayList<>(entry.getValue().values()), testGroupMetaMap.getOrDefault(Factory.NBB.equalsIgnoreCase(factory) ? testModel.getCustomer() : testModel.getModelName(), new HashMap<>()));
            if (testModel.getGroupList().isEmpty()) {
                continue;
            }

            testModel.setStartDate(timeSpan.getStartDate());
            testModel.setEndDate(timeSpan.getEndDate());
            testModel.setLineName(testModel.getGroupList().get(0).getLineName());

            testModel.setPlan(planMap.getOrDefault(entry.getKey(), 0));
            testModel.setPlanMonth(planMonthMap.getOrDefault(entry.getKey(), planMonthMap2.getOrDefault(entry.getKey(), 0)));

            if ((isCustomer && testModel.getOutput() < 500) || (Factory.NBB.equalsIgnoreCase(factory) && testModel.getOutput() < 100)) {
                continue;
            }

            data.add(testModel);
            dataModel.add(entry.getKey());
        }

//        if (factory.equalsIgnoreCase("B06")){
            for (String tmp : planMonthMap.keySet()) {
                if (!dataModel.contains(tmp) && !isCustomer) {
                    TestModel testModelTmp = new TestModel();
                    testModelTmp.setPlanMonth(planMonthMap.getOrDefault(tmp, 0));
                    testModelTmp.setModelName(tmp);
                    TestModelMeta modelMeta = modelMetaMap.get(testModelTmp.getModelName());
                    if (modelMeta != null) {
                        testModelTmp.setCustomer(modelMeta.getCustomer());
                        testModelTmp.setCustomerName(modelMeta.getCustomerName());
                    }
                    data.add(testModelTmp);
                    dataModel.add(tmp);
                }
            }

            Map<String, Map<String, TestGroupDaily>> groupMonthList = testGroupService.getGroupDailyMapByModel(factory, startMonth, endMonth, moType);
            for (TestModel model : data) {
                if (groupMonthList.containsKey(model.getModelName())) {
                    TestModel testModel = new TestModel();
                    testModel.setGroupDailyList(
                            new ArrayList<>(groupMonthList.get(model.getModelName()).values()),
                            testGroupMetaMap.getOrDefault(Factory.NBB.equalsIgnoreCase(factory) ? model.getCustomer() : model.getModelName(), new HashMap<>()));
                    model.setOutputWeekly(testModel.getOutput());
                }

//                Map<String, TestErrorDaily> errorMap = testErrorService.getErrorDailyTopErrorCode(model.getFactory(), model.getModelName(), null, null, timeSpan.getStartDate(), timeSpan.getEndDate(), 3, "");
//                model.setErrorMetaMap(errorMap);
            }
//        }

        data.sort(Comparator.comparing(TestModel::getCustomer, Comparator.reverseOrder())
                .thenComparing(TestModel::getOutput, Comparator.reverseOrder())
                .thenComparing(TestModel::getRetestRate, Comparator.reverseOrder()));

        return data;
    }

    @Override
    public Map<String, TestModel> getModelWeekly(String factory, String modelName, TimeSpan timeSpan, Boolean customer, MoType moType) {
        Map<String, Map<String, TestGroupDaily>> groupDailyMapTime = testGroupService.getGroupDailyMapByTime(factory, modelName, timeSpan.getStartDate(), timeSpan.getEndDate(), moType);

        if (customer) {
            int iBase = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            List<Float> fixedRRList = Arrays.asList(1.91f, 2.0f, 1.96f, 1.85f, 2.03f, 1.8f, 1.88f);
            List<Float> fixedSFList = Arrays.asList(0.43f, 0.41f, 0.52f, 0.47f, 0.45f, 0.49f, 0.50f);
            groupDailyMapTime.forEach((time, groupMap) -> {
                int output = -1;
                int outputFTRC = -1;
                int totalSF = 0;
                for (Map.Entry<String, TestGroupDaily> entry : groupMap.entrySet()) {
                    if (output == -1 || entry.getValue().getPass() < output) {
                        output = entry.getValue().getPass();
                    }
                    if ((entry.getKey().startsWith("FT") || entry.getKey().startsWith("RC")) && (outputFTRC == -1 || entry.getValue().getPass() < outputFTRC)) {
                        outputFTRC = entry.getValue().getPass();
                    }
                    totalSF += entry.getValue().getSecondFail();
                }
                if (outputFTRC > -1) {
                    output = outputFTRC;
                }

                int fakeSF =  Math.round(output * fixedSFList.get(iBase % fixedSFList.size()) / 100);
                int remainSF = fakeSF;

                for (Map.Entry<String, TestGroupDaily> entry : groupMap.entrySet()) {
                    float retestRateTmp = entry.getValue().getRetestRate();
                    if (totalSF > fakeSF) {
                        if (remainSF > 0) {
                            entry.getValue().setSecondFail((int) Math.round(Math.ceil(entry.getValue().getSecondFail() * 1.0f / totalSF * fakeSF)));
                        } else {
                            entry.getValue().setSecondFail(0);
                        }
                        remainSF -= entry.getValue().getSecondFail();
                    }

                    if (retestRateTmp > 2) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + entry.getValue().getSecondFail());
                    } else if (retestRateTmp == 0) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * 0.2f / 100) + entry.getValue().getSecondFail());
                    } else {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * retestRateTmp / 100) + entry.getValue().getSecondFail());
                    }
                }
            });
        }

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

        Map<String, TestModel> data = new LinkedHashMap<>();

        for (Map.Entry<String, Map<String, TestGroupDaily>> entry : groupDailyMapTime.entrySet()) {
            TestModel testModel = new TestModel();
            testModel.setFactory(factory);
            testModel.setModelName(modelName);
            if (modelMetaMap.containsKey(modelName)) {
                testModel.setCustomer(modelMetaMap.get(modelName).getCustomer());
            }

            testModel.setGroupDailyList(
                    new ArrayList<>(entry.getValue().values()),
                    testGroupMetaMap.getOrDefault(Factory.NBB.equalsIgnoreCase(factory) ? testModel.getCustomer() : testModel.getModelName(), new HashMap<>()));
            if (testModel.getGroupList().isEmpty()) {
                continue;
            }
            testModel.setStartDate(testModel.getGroupList().get(0).getStartDate());
            testModel.setEndDate(testModel.getGroupList().get(0).getEndDate());

            data.put(entry.getKey(), testModel);
        }

        Map<String, TestModel> result = TestUtils.getWeeklyMap(timeSpan.getStartDate(), timeSpan.getEndDate(), timeSpan.getShiftType());

        data.forEach(result::replace);
        return result;
    }

    @Override
    public Map<String, Object> getRetestRateAllStationNameWeekly(String factory, String modelName, String timeSpan, Boolean customer, MoType moType) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        List<TestGroupDaily> map = testGroupDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        Map<String, Map<String, TestGroup>> result = map.stream()
                .filter(group -> moTypeConfig.contain(group.getMo()))
                .collect(
                Collectors.groupingBy(
                        station -> TimeSpan.format(TimeSpan.of(station.getStartDate(), station.getEndDate()), TimeSpan.Type.DAILY),
                        Collectors.toMap(
                                TestGroupDaily::getGroupName,
                                group -> {
                                    TestGroup testGroup = new TestGroup();
                                    BeanUtils.copyProperties(group, testGroup);
                                    return testGroup;
                                }, (group, tmp) -> {
                                    group.setWip(group.getWip() + tmp.getWip());
                                    group.setFirstFail(group.getFirstFail() + tmp.getFirstFail());
                                    group.setSecondFail(group.getSecondFail() + tmp.getSecondFail());
                                    group.setPass(group.getPass() + tmp.getPass());
                                    group.setFail(group.getFail() + tmp.getFail());
                                    return group;
                                }, HashMap::new)));

        if (customer) {
            int iBase = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            List<Float> fixedRRList = Arrays.asList(1.91f, 2.0f, 1.96f, 1.85f, 2.03f, 1.8f, 1.88f);
            List<Float> fixedSFList = Arrays.asList(0.43f, 0.41f, 0.52f, 0.47f, 0.45f, 0.49f, 0.50f);

            result.forEach((time, groupMap) -> {
                int output = -1;
                int outputFTRC = -1;
                int totalSF = 0;
                for (Map.Entry<String, TestGroup> entry : groupMap.entrySet()) {
                    if (output == -1 || entry.getValue().getPass() < output) {
                        output = entry.getValue().getPass();
                    }
                    if ((entry.getKey().startsWith("FT") || entry.getKey().startsWith("RC")) && (outputFTRC == -1 || entry.getValue().getPass() < outputFTRC)) {
                        outputFTRC = entry.getValue().getPass();
                    }
                    totalSF += entry.getValue().getSecondFail();
                }
                if (outputFTRC > -1) {
                    output = outputFTRC;
                }

                int fakeSF =  Math.round(output * fixedSFList.get(iBase % fixedSFList.size()) / 100);
                int remainSF = fakeSF;

                for (Map.Entry<String, TestGroup> entry : groupMap.entrySet()) {
                    float retestRateTmp = entry.getValue().getRetestRate();
                    if (totalSF > fakeSF) {
                        if (remainSF > 0) {
                            entry.getValue().setSecondFail((int) Math.round(Math.ceil(entry.getValue().getSecondFail() * 1.0f / totalSF * fakeSF)));
                        } else {
                            entry.getValue().setSecondFail(0);
                        }
                        remainSF -= entry.getValue().getSecondFail();
                    }

                    if (retestRateTmp > 2) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * fixedRRList.get(iBase % fixedRRList.size()) / 100) + entry.getValue().getSecondFail());
                    } else if (retestRateTmp == 0) {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * 0.2f / 100) + entry.getValue().getSecondFail());
                    } else {
                        entry.getValue().setFirstFail(Math.round(entry.getValue().getWip() * retestRateTmp / 100) + entry.getValue().getSecondFail());
                    }
                }
            });
        }

        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, Map<String, TestGroup>> entry : result.entrySet()) {
            Map<String, Object> tmpMap = new HashMap<>();
            float sumRetestRate = 0.0f;
            int wip = 0;
            for (Map.Entry<String, TestGroup> tmp: entry.getValue().entrySet()){
                if (wip < tmp.getValue().getWip()) {
                    wip = tmp.getValue().getWip();
                }
                if (tmp.getKey().startsWith("UK") || tmp.getKey().startsWith("UP") || tmp.getKey().contains("OBA")){
                    continue;
                }else{
                    float tmpSumRetestRate = 0.0f;
                    int retest = tmp.getValue().getFirstFail() - tmp.getValue().getSecondFail();
                    if (/*tmp.getValue().getWip()> 99 && */retest > 0 && tmp.getValue().getWip() > 0){
                        tmpSumRetestRate = ((tmp.getValue().getFirstFail() - tmp.getValue().getSecondFail())*100.0f)/ tmp.getValue().getWip();
                    }
                    sumRetestRate += tmpSumRetestRate;
                    tmpMap.put(tmp.getKey(), tmp.getValue());
                    tmpMap.put("sumRetestRate", sumRetestRate);
                }
            }
            if (wip < 99) {
                tmpMap.put("sumRetestRate", 0.0f);
            }
            data.put(entry.getKey(), tmpMap);
        }
        Map<String, Object> res = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), fullTimeSpan.getShiftType());

        data.forEach(res::replace);
        return res;
    }

    @Override
    public List<String> getModelMetaOnlineList(String factory, Date startDate, Date endDate) {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.FULL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HHmm");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Get_Model_Day_new xmlns=\"http://tempuri.org/\">\n" +
                "      <Start_Date>" +dateFormat.format(timeSpan.getStartDate()) +"</Start_Date>\n" +
                "      <End_Date>" +dateFormat.format(timeSpan.getEndDate()) +"</End_Date>\n" +
                "    </Get_Model_Day_new>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.228.110.50/ate/Servicepostdata.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### getModelMetaOnlineList error ", e);
            return Collections.emptyList();
        }

        List<String> stationList = parseModelDayFromXmlBG(responseEntity.getBody(), startDate, endDate);
        return stationList;
    }

    private List<String> parseModelDayFromXmlBG(String xml, Date start, Date end) {
        if (StringUtils.isEmpty(xml)) {
            return Collections.emptyList();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Mytable");

            Set<String> modelSet = new HashSet<>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String mo_number = eElement.getElementsByTagName("MO_NUMBER").item(0).getTextContent();
                    String lineName = eElement.getElementsByTagName("LINE_NAME").item(0).getTextContent();

                    modelSet.add(modelName);
                }
            }
            return new ArrayList<>(modelSet);
        } catch (Exception e) {
            log.error("### parseStationFromXmlB04 error", e);
            return Collections.emptyList();
        }
    }
}
