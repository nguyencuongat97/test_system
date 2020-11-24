package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.b04ds02.repository.B04DS02ErrorLogRepository;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestMoTypeConfig;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.repository.TestGroupDailyRepository;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestGroupRepository;
import com.foxconn.fii.data.primary.repository.TestMoTypeConfigRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
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
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestGroupServiceImpl implements TestGroupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private TestGroupDailyRepository testGroupDailyRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    @Autowired
    private B04DS02ErrorLogRepository b04DS02ErrorLogRepository;

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestMoTypeConfigRepository testMoTypeConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestModelService testModelService;

    @Override
    public int[] saveAll(List<TestGroup> groupList) {
        if (groupList.isEmpty()) {
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_group as target " +
                        "using(select factory=?, model_name=?, line_name=?, group_name=?, start_date=?, end_date=?, wip=?, first_fail=?, second_fail=?, pass=?, fail=?, retest_rate=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.start_date=source.start_date and target.end_date=source.end_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.line_name=source.line_name, " +
                        "   target.wip=source.wip, " +
                        "   target.first_fail=source.first_fail, " +
                        "   target.second_fail=source.second_fail, " +
                        "   target.pass=source.pass, " +
                        "   target.fail=source.fail, " +
                        "   target.retest_rate=source.retest_rate " +
                        "when not matched then " +
                        "   insert (factory, model_name, line_name, group_name, start_date, end_date, wip, first_fail, second_fail, pass, fail, retest_rate) " +
                        "   values(source.factory, source.model_name, source.line_name, source.group_name, source.start_date, source.end_date, source.wip, source.first_fail, source.second_fail, source.pass, source.fail, source.retest_rate);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestGroup group = groupList.get(i);
                        preparedStatement.setString(1, group.getFactory());
                        preparedStatement.setString(2, group.getModelName());
                        preparedStatement.setString(3, group.getLineName());
                        preparedStatement.setString(4, group.getGroupName());
                        preparedStatement.setTimestamp(5, new Timestamp(group.getStartDate().getTime()));
                        preparedStatement.setTimestamp(6, new Timestamp(group.getEndDate().getTime()));
                        preparedStatement.setInt(7, group.getWip());
                        preparedStatement.setInt(8, group.getFirstFail());
                        preparedStatement.setInt(9, group.getSecondFail());
                        preparedStatement.setInt(10, group.getPass());
                        preparedStatement.setInt(11, group.getFail());
                        preparedStatement.setFloat(12, group.getRetestRate());
                    }

                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }

    @Override
    public int[] saveAllMeta(List<TestGroup> groupList) {
        if (groupList.isEmpty()) {
            return null;
        }

        List<TestGroupMeta> groupMetaList = new ArrayList<>();
        for (TestGroup group : groupList) {
            TestGroupMeta groupMeta = new TestGroupMeta();
            groupMeta.setFactory(group.getFactory());
            groupMeta.setModelName(group.getModelName());
            groupMeta.setLineName(group.getLineName());
            groupMeta.setGroupName(group.getGroupName());

            groupMetaList.add(groupMeta);
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_group_meta as target " +
                        "using(select factory=?, model_name=?, line_name=?, group_name=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.line_name=source.line_name, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, line_name, group_name, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.line_name, source.group_name, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestGroupMeta group = groupMetaList.get(i);
                        preparedStatement.setString(1, group.getFactory());
                        preparedStatement.setString(2, group.getModelName());
                        preparedStatement.setString(3, group.getLineName());
                        preparedStatement.setString(4, group.getGroupName());
                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return groupMetaList.size();
                    }
                });
    }

    @Override
    public List<TestGroupMeta> getGroupMetaList(String factory, String modelName, Boolean parameter) {
        List<TestGroupMeta> groupMetaList;
        if (StringUtils.isEmpty(modelName)) {
            groupMetaList = testGroupMetaRepository.findAllByFactory(factory);
        } else if (parameter == null) {
            groupMetaList = testGroupMetaRepository.findAllByFactoryAndModelName(factory, modelName);
        } else {
            groupMetaList = testGroupMetaRepository.findAllByFactoryAndModelNameAndParameterIsTrue(factory, modelName);
        }

        return groupMetaList;
    }

    @Override
    public List<TestGroupMeta> getGroupMetaList(String factory, String customer, String stage, String modelName, Boolean parameter) {
        List<TestGroupMeta> groupMetaList;
        if (Factory.NBB.equalsIgnoreCase(factory) || Factory.S03.equalsIgnoreCase(factory)) {
            if (StringUtils.isEmpty(stage)) {
                groupMetaList = testGroupMetaRepository.findAllByFactoryAndCustomer(factory, customer);
            } else {
                groupMetaList = testGroupMetaRepository.findAllByFactoryAndCustomerAndStage(factory, customer, stage);
            }
        } else {
            groupMetaList = getGroupMetaList(factory, modelName, parameter);
        }

        return groupMetaList;
    }

    @Override
    public TestGroupMeta getGroupMeta(String factory, String modelName, String groupName) {
        return testGroupMetaRepository.findTop1ByFactoryAndModelNameAndGroupName(factory, modelName, groupName);
    }

    @Override
    public Map<String, List<TestGroup>> getGroupMapLineName(String factory, Date startDate, Date endDate) {
        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactory(factory)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        Map<String, TestGroup> map = testGroupRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate)
                .stream().filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.toMap(
                        group -> group.getModelName() + '_' + group.getLineName() + '_' + group.getGroupName(),
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
                        }, HashMap::new));

        Map<String, List<TestGroup>> result = map.values().stream().collect(Collectors.groupingBy(group -> group.getLineName()));

        return result;
    }


    @Override
    public List<TestGroup> getGroupHourlyListFromDB(String factory, String modelName, String groupName, Date startDate, Date endDate) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<TestGroup> groupList = sfcTestGroupRepository.findHourlyByWorkDateBetween(factory, "", modelName, groupName, startDate, endDate);

            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (Factory.S03.equalsIgnoreCase(factory) && modelList.contains(modelName)) {
                groupList.addAll(sfcTestGroupRepository.findHourlyByWorkDateBetween(factory, "UI", modelName, groupName, startDate, endDate));
            }
            return groupList;
        }
        return testGroupRepository.findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(factory, modelName, groupName, startDate, endDate);
    }

    @Override
    public List<TestGroupDaily> getGroupDailyListFromDB(String factory, String modelName, Date startDate, Date endDate) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<TestGroupDaily> groupList = sfcTestGroupRepository.findByWorkDateBetween(factory, "", Collections.singletonList(modelName), startDate, endDate);

            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (Factory.S03.equalsIgnoreCase(factory) && modelList.contains(modelName)) {
                groupList.addAll(sfcTestGroupRepository.findByWorkDateBetween(factory, "UI", Collections.singletonList(modelName), startDate, endDate));
            }
            return groupList;
        }
        return testGroupDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
    }

    @Override
    public List<TestGroupDaily> getGroupDailyList(String factory, Date startDate, Date endDate) {
        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactory(factory)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        Map<String, TestGroupDaily> map = testGroupDailyRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate)
                .stream().filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.toMap(
                        group -> group.getModelName() + '_' + group.getGroupName(),
                        TestGroupDaily::clone,
                        TestGroupDaily::merge,
                        HashMap::new));

        return new ArrayList<>(map.values());
    }

    @Override
    public List<TestGroupDaily> getGroupDailyList(String factory, String modelName, Date startDate, Date endDate, String mode, MoType moType) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            List<TestGroupDaily> groupDailyList;
            if (modelList.contains(modelName)) {
                groupDailyList = sfcTestGroupRepository.findByWorkDateBetween(factory, "UI", modelName, startDate, endDate);
            } else {
                groupDailyList = sfcTestGroupRepository.findByWorkDateBetween(factory, "", modelName, startDate, endDate);
            }

            Map<String, TestGroupDaily> map = groupDailyList.stream()
                    .collect(Collectors.toMap(
                            group -> group.getModelName() + '_' + group.getGroupName(),
                            TestGroupDaily::clone,
                            TestGroupDaily::merge,
                            HashMap::new));

            return map.values().stream().sorted(Comparator.comparing(TestGroupDaily::getGroupName)).collect(Collectors.toList());
        }

        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactoryAndModelName(factory, modelName)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        List<TestGroupDaily> groupDailyList = testGroupDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        Map<String, TestGroupDaily> map = groupDailyList
                .stream()
                .filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()) && (moTypeConfig.contain(group.getMo())))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.toMap(
                        group -> group.getModelName() + '_' + group.getGroupName(),
                        TestGroupDaily::clone,
                        TestGroupDaily::merge,
                        HashMap::new));

        if ("B04".equalsIgnoreCase(factory) && "TE".equalsIgnoreCase(mode)) {
            Map<String, TestGroupDaily> map2 = b04DS02ErrorLogRepository.getGroupList(modelName, startDate, endDate)
                    .stream()
                    .filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()))
                    .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                    .collect(Collectors.toMap(
                            group -> group.getModelName() + '_' + group.getGroupName(),
                            TestGroupDaily::clone,
                            TestGroupDaily::merge,
                            HashMap::new));

            map2.forEach((key, value) -> {
                if (map.containsKey(key)) {
                    value.setSecondFail(map.get(key).getSecondFail());
                }
            });

            return map2.values().stream().sorted(Comparator.comparing(group -> group.getMeta().getStep())).collect(Collectors.toList());
        }

        return map.values().stream().sorted(Comparator.comparing(group -> group.getMeta().getStep())).collect(Collectors.toList());
    }

    @Override
    public Map<String, Map<String, Map<String, TestGroupDaily>>> getGroupDailyMapByModelAndTime(String factory, Date startDate, Date endDate) {
        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactory(factory)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        List<TestGroupDaily> map = testGroupDailyRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate);
        return map.stream().filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.groupingBy(
                        TestGroupDaily::getModelName,
                        Collectors.groupingBy(
                                station -> TimeSpan.format(TimeSpan.of(station.getStartDate(), station.getEndDate()), TimeSpan.Type.DAILY),
                                Collectors.toMap(
                                        TestGroupDaily::getGroupName,
                                        TestGroupDaily::clone,
                                        TestGroupDaily::merge,
                                        HashMap::new))));
    }


    @Override
    public Map<String, Map<String, TestGroupDaily>> getGroupDailyMapByModel(String factory, Date startDate, Date endDate, MoType moType) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<TestGroupDaily> groupList = sfcTestGroupRepository.findByWorkDateBetween(factory, "", new ArrayList<>(), startDate, endDate);

            if (Factory.S03.equalsIgnoreCase(factory)) {
                List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
                groupList.addAll(sfcTestGroupRepository.findByWorkDateBetween(factory, "UI", modelList, startDate, endDate));
            }

            return groupList
                    .stream()
                    .collect(Collectors.groupingBy(
                            TestGroupDaily::getModelName,
                            Collectors.toMap(
                                    TestGroupDaily::getGroupName,
                                    group -> group,
                                    TestGroupDaily::merge,
                                    HashMap::new)));
        }

        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactory(factory)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        List<TestGroupDaily> groupList = testGroupDailyRepository.findAllByFactoryAndStartDateBetween(factory, startDate, endDate);

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        return groupList
                .stream().filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()) && (moTypeConfig.contain(group.getMo())))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.groupingBy(
                        TestGroupDaily::getModelName,
                        Collectors.toMap(
                                TestGroupDaily::getGroupName,
                                group -> group,
                                TestGroupDaily::merge,
                                HashMap::new)));
    }

    @Override
    public Map<String, Map<String, TestGroupDaily>> getGroupDailyMapByTime(String factory, String modelName, Date startDate, Date endDate, MoType moType) {
        if (Factory.S03.equalsIgnoreCase(factory) || Factory.C03.equalsIgnoreCase(factory) || Factory.NBB.equalsIgnoreCase(factory)) {
            List<TestGroupDaily> groupList = sfcTestGroupRepository.findByWorkDateBetween(factory, "", Collections.singletonList(modelName), startDate, endDate);

            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            if (Factory.S03.equalsIgnoreCase(factory) && modelList.contains(modelName)) {
                groupList.addAll(sfcTestGroupRepository.findByWorkDateBetween(factory, "UI", Collections.singletonList(modelName), startDate, endDate));
            }

            return groupList
                    .stream()
                    .collect(Collectors.groupingBy(
                            group -> TimeSpan.format(TimeSpan.of(group.getStartDate(), group.getEndDate()), TimeSpan.Type.DAILY),
                            Collectors.toMap(
                                    TestGroupDaily::getGroupName,
                                    group -> group,
                                    TestGroupDaily::merge,
                                    HashMap::new)));
        }

        Map<String, TestGroupMeta> metaMap = testGroupMetaRepository.findAllByFactoryAndModelName(factory, modelName)
                .stream().filter(TestGroupMeta::getVisible)
                .collect(Collectors.toMap(group -> group.getModelName() + '_' + group.getGroupName(), group -> group, (g1, g2) -> g1));

        List<TestGroupDaily> map = testGroupDailyRepository.findAllByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);

        TestMoTypeConfig moTypeConfig = testMoTypeConfigRepository.findByFactoryAndMoType(factory, moType).orElse(new TestMoTypeConfig());

        return map.stream()
                .filter(group -> metaMap.containsKey(group.getModelName() + '_' + group.getGroupName()) && (moTypeConfig.contain(group.getMo())))
                .peek(group -> group.setMeta(metaMap.get(group.getModelName() + '_' + group.getGroupName())))
                .collect(Collectors.groupingBy(
                        group -> TimeSpan.format(TimeSpan.of(group.getStartDate(), group.getEndDate()), TimeSpan.Type.DAILY),
                        Collectors.toMap(
                                TestGroupDaily::getGroupName,
                                TestGroupDaily::clone,
                                TestGroupDaily::merge,
                                HashMap::new)));
    }

    @Override
    public List<TestGroup> getGroupDailyBG(String factory, List<String> modelList, Date startDate, Date endDate) {

        String modelNameList = String.join(",", modelList);
        String groupNameList = testGroupMetaRepository.findGroupInputOutputByFactory(factory, modelList)
                .stream().map(TestGroupMeta::getGroupName).collect(Collectors.joining(","));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HHmm");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <INPUT_OUTPUT xmlns=\"http://tempuri.org/\">\n" +
                "      <start_date>" +dateFormat.format(startDate) +"</start_date>\n" +
                "      <start_time>" +hourFormat.format(startDate) +"</start_time>\n" +
                "      <end_date>" +dateFormat.format(endDate) +"</end_date>\n" +
                "      <end_time>" +hourFormat.format(endDate) +"</end_time>\n" +
                "      <group_name>"+groupNameList+"</group_name>\n" +
                "      <model_name>"+modelNameList+"</model_name>\n" +
                "    </INPUT_OUTPUT>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.228.110.50/ate/Servicepostdata.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### getGroupDailyB01 error ", e);
            return Collections.emptyList();
        }

        Map<String, List<String>> modelMap = testModelService.getModelMetaList().stream()
                .collect(Collectors.groupingBy(model -> model.getFactory().toUpperCase(), Collectors.mapping(TestModelMeta::getModelName, Collectors.toList())));

        List<TestGroup> stationList = parseGroupFromXmlB01(responseEntity.getBody(), startDate, endDate, modelMap);
        return stationList.stream().filter(group -> factory.equalsIgnoreCase(group.getFactory())).collect(Collectors.toList());
    }

    private List<TestGroup> parseGroupFromXmlB01(String xml, Date start, Date end, Map<String, List<String>> modelMap) {
        if (StringUtils.isEmpty(xml)) {
            return Collections.emptyList();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Table");

            List<TestGroup> groupList = new ArrayList<>();
//            Map<String, TestGroup> stationMap = new HashMap<>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String groupName = eElement.getElementsByTagName("GROUP_NAME").item(0).getTextContent();
                    String lineName = eElement.getElementsByTagName("LINE_NAME").item(0).getTextContent();

                    String factoryName = "UNKNOWN";
                    if (modelMap.get("B01").contains(modelName)) {
                        factoryName = "B01";
                    }
                    else if (modelMap.get("A02").contains(modelName)) {
                        factoryName = "A02";
                    }

//                    String key = factoryName + "_" + modelName + "_" + groupName;
//                    TestGroup station = stationMap.getOrDefault(key, new TestGroup());
                    TestGroup station = new TestGroup();

                    station.setFactory(factoryName);
                    try {
                        TimeSpan timeSpan = TimeSpan.from(eElement.getElementsByTagName("WORK_TIME").item(0).getTextContent(), "yyyyMMdd", TimeSpan.Type.DAILY);
                        if (timeSpan != null) {
                            station.setStartDate(timeSpan.getStartDate());
                            station.setEndDate(timeSpan.getEndDate());
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }

                    station.setModelName(modelName);
                    station.setGroupName(groupName);
                    station.setLineName(lineName);

                    station.setWip(station.getWip() + Integer.parseInt(eElement.getElementsByTagName("WIP_QTY").item(0).getTextContent()));
                    station.setPass(station.getPass() + Integer.parseInt(eElement.getElementsByTagName("PASS_QTY").item(0).getTextContent()));
                    station.setFail(station.getFail() + Integer.parseInt(eElement.getElementsByTagName("FAIL_QTY").item(0).getTextContent()));
//                    station.setRepass(station.getRepass() + Integer.parseInt(eElement.getElementsByTagName("REPASS_QTY").item(0).getTextContent()));
//                    station.setRefail(station.getRefail() + Integer.parseInt(eElement.getElementsByTagName("REFAIL_QTY").item(0).getTextContent()));
//                    station.setWip(station.getWip() + station.getPass() + station.getFail());

//                    stationMap.put(key, station);
                    groupList.add(station);
                }
            }

//            return new ArrayList<>(stationMap.values());
            return groupList;
        } catch (Exception e) {
            log.error("### parseStationFromXmlB04 error", e);
            return Collections.emptyList();
        }
    }
}
