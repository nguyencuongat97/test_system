package com.foxconn.fii.receiver.re.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.b04.model.B04RepairCheckIn;
import com.foxconn.fii.data.b04.repository.B04RepairBc8mRepository;
import com.foxconn.fii.data.b04.repository.B04RepairCheckInRepository;
import com.foxconn.fii.data.b04wip.model.B04ReCheckInWip;
import com.foxconn.fii.data.b04wip.repository.B04ReCheckInWipRepository;
import com.foxconn.fii.data.primary.model.entity.RepairB06SerialError;
import com.foxconn.fii.data.primary.model.entity.RepairIODaily;
import com.foxconn.fii.data.primary.model.entity.RepairTimeDataBeta;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.receiver.re.service.RepairCheckInService;
import com.foxconn.fii.receiver.re.service.RepairIODailyService;
import com.foxconn.fii.receiver.re.service.RepairSyncDataService;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
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
import java.util.*;

@Slf4j
@Service
public class RepairSyncDataServiceImpl implements RepairSyncDataService {

    @Autowired
    private RepairIODailyService repairIODailyService;

    @Autowired
    private TestRepairSerialErrorService testRepairSerialErrorService;

    @Autowired
    private RepairCheckInService repairCheckInService;

    @Autowired
    private B04RepairBc8mRepository b04RepairBc8mRepository;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    private RepairB06SerialErrorRepository repairB06SerialErrorRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HrEmployeeInfoEatRiceRepository hrEmployeeInfoEatRiceRepository;

    @Autowired
    private RepairOnlineWipInOutRepository repairOnlineWipInOutRepository;

    @Autowired
    private B04RepairCheckInRepository b04RepairCheckInRepository;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private B04ReCheckInWipRepository b04ReCheckInWipRepository;

//    @Autowired
//    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    @Qualifier(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public void syncIODaily(Date startDate, Date endDate) {
        String factory = "B04";

        Map<String, RepairIODaily> result = new HashMap<>();

        List<Object[]> outputData = testRepairSerialErrorService.countByModelNameAndSectionNameAndStatus(factory, startDate, endDate);

        for (Object[] objects : outputData) {
            String modelName = (String) objects[0];
            String sectionName = (String) objects[1];
            TestRepairSerialError.Status status = (TestRepairSerialError.Status) objects[2];
            Number count = (Number) objects[3];

            String key = status.toString() + '_' + modelName + '_' + sectionName;
            RepairIODaily repair = result.get(key);
            if (repair == null) {
                repair = new RepairIODaily();
                repair.setFactory(factory);
                repair.setSectionName(sectionName);
                repair.setModelName(modelName);
                repair.setStartDate(startDate);
                repair.setEndDate(endDate);
                repair.setStatus(status);
            }

            if (status == TestRepairSerialError.Status.REPAIRED) {
                repair.setOutput(repair.getOutput() + count.intValue());
            } else if (status == TestRepairSerialError.Status.BC8M) {
                repair.setInput(repair.getInput() + count.intValue());
            } else if (status == TestRepairSerialError.Status.UNDER_REPAIR) {
                continue;
            }
            result.put(key, repair);
        }

        List<Object[]> inputData = repairCheckInService.countByModelNameAndSection(factory, startDate, endDate);

        for (Object[] objects : inputData) {
            String modelName = (String) objects[0];
            String sectionName = (String) objects[1];
            Number count = (Number) objects[2];

            String key = TestRepairSerialError.Status.REPAIRED.toString() + '_' + modelName + '_' + sectionName;
            RepairIODaily repair = result.get(key);
            if (repair == null) {
                repair = new RepairIODaily();
                repair.setFactory(factory);
                repair.setSectionName(sectionName);
                repair.setModelName(modelName);
                repair.setStartDate(startDate);
                repair.setEndDate(endDate);
                repair.setStatus(TestRepairSerialError.Status.REPAIRED);
            }
            repair.setInput(repair.getInput() + count.intValue());
            result.put(key, repair);
        }

        List<Object[]> bc8mInput = b04RepairBc8mRepository.countInputByModelName(startDate, endDate);
        for (Object[] objects : bc8mInput) {
            String modelName = (String) objects[0];
            Number count = (Number) objects[1];

            String key = TestRepairSerialError.Status.BC8M.toString() + '_' + modelName + '_' + "SI";
            RepairIODaily repair = result.get(key);
            if (repair == null) {
                repair = new RepairIODaily();
                repair.setFactory(factory);
                repair.setSectionName("SI");
                repair.setModelName(modelName);
                repair.setStartDate(startDate);
                repair.setEndDate(endDate);
                repair.setStatus(TestRepairSerialError.Status.BC8M);
            }
            repair.setInput(repair.getInput() + count.intValue());
            result.put(key, repair);
        }

        List<Object[]> bc8mOutput = b04RepairBc8mRepository.countOutputByModelName(startDate, endDate);
        for (Object[] objects : bc8mOutput) {
            String modelName = (String) objects[0];
            Number count = (Number) objects[1];

            String key = TestRepairSerialError.Status.BC8M.toString() + '_' + modelName + '_' + "SI";
            RepairIODaily repair = result.get(key);
            if (repair == null) {
                repair = new RepairIODaily();
                repair.setFactory(factory);
                repair.setSectionName("SI");
                repair.setModelName(modelName);
                repair.setStartDate(startDate);
                repair.setEndDate(endDate);
                repair.setStatus(TestRepairSerialError.Status.BC8M);
            }
            repair.setOutput(repair.getOutput() + count.intValue());
            result.put(key, repair);
        }

        result.values().forEach(repair -> {
//            if (repair.getInput() > repair.getOutput()) {
                repair.setRemain(repair.getInput() - repair.getOutput());
//            }
        });

        repairIODailyService.saveAll(new ArrayList<>(result.values()));
    }

    @Override
    public void B06ReSyncDataFromWebService(Date startDate, Date endDate)throws Exception {
        RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetDetailRepairReport xmlns=\"http://tempuri.org/\">\n" +
                "      <start_time>" + dateFormat.format(startDate) + "</start_time>\n" +
                "      <end_time>"+ dateFormat.format(endDate) +"</end_time>\n" +
                "    </GetDetailRepairReport>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.81.86/GetDataPE/GetDataPE.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### DuongTungErrorB0601 : ", e);
            return;
        }
        List<RepairB06SerialError> objectList = parseDataReFromWebserviceB06(responseEntity.getBody());

        //  objectList = objectList.stream().sorted((e1, e2)  -> e1.getInStationTime().compareTo(e2.getInStationTime())).collect(Collectors.toList());
//        try {
//            saveAllBC8MCheckIn(dt);
//        }catch (Exception e){
//            log.error("### DuongTungError03 : ", e);
//        }
        log.info("###size-dataRe06: "+ objectList.size());
        try {
            repairB06SerialErrorRepository.saveAll(objectList);
        }catch (Exception e){
            log.error("### DuongTungErrorB0604 : ", e);
        }
    }


    private List<RepairB06SerialError> parseDataReFromWebserviceB06(String xml) {
        if (StringUtils.isEmpty(xml)) {
            log.info("### DuongTungErrorB0611");
            return  Collections.emptyList();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("TblGetDetailRepairReport");

            List<RepairB06SerialError> repairB06SerialErrors = new ArrayList<>();

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                String testGroup = "";String errorCode = ""; String testTime = null; String empNo = "";
                String repairer = ""; String repairTime = null; String reasonCode= ""; String reasonDescE = "";  String location = null; String modelName = "";

                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String serialNumber = eElement.getElementsByTagName("SERIAL_NUMBER").item(0).getTextContent();
                    String moNumber = eElement.getElementsByTagName("MO_NUMBER").item(0).getTextContent();

                    if (eElement.getElementsByTagName("MODEL_NAME").getLength() > 0){
                         modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("TEST_GROUP").getLength() > 0){
                        testGroup = eElement.getElementsByTagName("TEST_GROUP").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("ERROR_CODE").getLength() > 0){
                        errorCode = eElement.getElementsByTagName("ERROR_CODE").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("TEST_TIME").getLength() > 0){
                        testTime = eElement.getElementsByTagName("TEST_TIME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("EMP_NO").getLength() > 0){
                        empNo = eElement.getElementsByTagName("EMP_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("REPAIRMAN").getLength() > 0){
                        repairer = eElement.getElementsByTagName("REPAIRMAN").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("REPAIR_TIME").getLength() > 0){
                        repairTime = eElement.getElementsByTagName("REPAIR_TIME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("REASON_CODE").getLength() > 0){
                        reasonCode = eElement.getElementsByTagName("REASON_CODE").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("REASON_DESC_E").getLength() > 0){
                        reasonDescE = eElement.getElementsByTagName("REASON_DESC_E").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("LOCATION").getLength() > 0){
                        location = eElement.getElementsByTagName("LOCATION").item(0).getTextContent();
                    }
                    String factoryName = "B06";

                    RepairB06SerialError repairB06SerialError = new RepairB06SerialError();

                    repairB06SerialError.setFactory(factoryName);
                    repairB06SerialError.setSerialNumber(serialNumber);
                    repairB06SerialError.setMo(moNumber);
                    repairB06SerialError.setModelName(modelName);
                    repairB06SerialError.setTestGroup(testGroup);
                    repairB06SerialError.setErrorCode(errorCode);
                    repairB06SerialError.setTestTime(simpleDateFormat.parse(testTime));
                    repairB06SerialError.setEmpNo(empNo);
                    repairB06SerialError.setRepairer(repairer);
                    repairB06SerialError.setRepairTime(simpleDateFormat.parse(repairTime));
                    repairB06SerialError.setReasonCode(reasonCode);
                    repairB06SerialError.setReasonDescE(reasonDescE);
                    repairB06SerialError.setLocation(location);

                    repairB06SerialErrors.add(repairB06SerialError);
                }
            }
         //   Map<String, Object> data = new HashMap<>();
//            data.put("dataRaw", repairBC8MCheckInList);
//            data.put("dataIndex", mData);
            return repairB06SerialErrors;
        } catch (Exception e) {
            log.error("### DuongTungErrorB0610 : ", e);
            return  Collections.emptyList();
        }
    }




    @Override
    public void syncDataOnlineWipIn(Date startDate, Date endDate) throws Exception {
        List<String> listStaionNot = new ArrayList<>();
        listStaionNot.add("R_SNSCAN");
        listStaionNot.add("R_SOLDE_VI");
        listStaionNot.add("R_IPQC_SP2");
        listStaionNot.add("R_IPQC_SP");
        listStaionNot.add("R_VINSP");
        listStaionNot.add("R_X_RAY2");
        listStaionNot.add("HOLD-R_INSP");
        listStaionNot.add("R_HEAT");
        listStaionNot.add("R_INSP");
        listStaionNot.add("R_WASH1");

        List<String> listErrorCodeNot = new ArrayList<>();
        listErrorCodeNot.add("VI0016");
        listErrorCodeNot.add("VI0017");
        listErrorCodeNot.add("VI0018");
        listErrorCodeNot.add("VI0028");
        listErrorCodeNot.add("VI0029");
        listErrorCodeNot.add("PTH001");
        listErrorCodeNot.add("PTH002");
        listErrorCodeNot.add("VI0012");
//                                   b04RepairCheckInRepository
        List<B04ReCheckInWip> data = b04ReCheckInWipRepository.findByInputTimeBetweenAndStationNameNotInAndErrorCodeNotInOrderByInputTimeAsc(startDate, endDate, listStaionNot, listErrorCodeNot);
        if (data.size() > 0){
            saveOnlineWipCheckIn(data);

        }
    }

    public int[] saveOnlineWipCheckIn(List<B04ReCheckInWip> groupList) {
        if (groupList.isEmpty()) {
            log.error("### DuongTungErrorOnlineWipIn01");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into re_online_wip_in_out as target " +
                        "using(select factory=?, serial_number=?, model_name=?, group_name=?, station_name=?, error_code=?, test_time=?, repairer=?, repair_time=?, reason_code=?, workdate=?, shift=?, section_name=?, mo=?, test_location_code=?, checkin_time=?) as source " +
                        "   on target.serial_number=source.serial_number and target.checkin_time <= source.checkin_time " +
                        "when matched then " +
                        "   update set " +
                        "   target.group_name=source.group_name, " +
                        "   target.station_name=source.station_name, " +
                        "   target.error_code=source.error_code, " +
                        "   target.test_time=source.test_time, " +
                        "   target.checkin_time=source.checkin_time " +
                        "when not matched then " +
                        "   insert (factory, serial_number, model_name, group_name, station_name, error_code, test_time, repairer, repair_time, reason_code, workdate, shift, section_name, mo, test_location_code, checkin_time) " +
                        "   values(source.factory, source.serial_number, source.model_name, source.group_name, source.station_name, source.error_code, source.test_time, source.repairer, source.repair_time, source.reason_code, source.workdate, source.shift, source.section_name, source.mo, source.test_location_code, source.checkin_time);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        B04ReCheckInWip group = groupList.get(i);
                        preparedStatement.setString(1, "B04");
                        preparedStatement.setString(2, group.getSerial());
                        preparedStatement.setString(3, group.getModelName());
                        preparedStatement.setString(4, null);
                        preparedStatement.setString(5, group.getStationName());
                        preparedStatement.setString(6, group.getErrorCode());
                        preparedStatement.setTimestamp(7, new Timestamp(group.getTimeInLine().getTime()));
                        preparedStatement.setString(8, null);
                        preparedStatement.setString(9, null);
                        preparedStatement.setTimestamp(10, null);
                        preparedStatement.setString(11, null);
                        preparedStatement.setString(12, null);
                        preparedStatement.setString(13, "SI");
                        preparedStatement.setString(14, group.getMo());
                        preparedStatement.setString(15, null);
                        preparedStatement.setTimestamp(16, new Timestamp(group.getInputTime().getTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }


    public int[] updateRepairOnlineWip(List<TestRepairSerialError> groupList) {
        if (groupList.isEmpty()) {
            log.info("### DuongTungErrorOnlineWipOut01");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into re_online_wip_in_out as target " +
                        "using(select factory=?, serial_number=?, model_name=?, group_name=?, station_name=?, error_code=?, test_time=?, repairer=?, repair_time=?, reason_code=?, workdate=?, shift=?, section_name=?, mo=?, test_location_code=?) as source " +
                        "   on target.serial_number=source.serial_number and target.checkin_time <= source.repair_time " +
                        "when matched then " +
                        "   update set " +
                        "   target.group_name=source.group_name, " +
                        "   target.repairer=source.repairer, " +
                        "   target.repair_time=source.repair_time, " +
                        "   target.reason_code=source.reason_code, " +
                        "   target.workdate=source.workdate, " +
                        "   target.shift=source.shift, " +
                        "   target.test_location_code=source.test_location_code ;",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestRepairSerialError group = groupList.get(i);
                        preparedStatement.setString(1, group.getFactory());
                        preparedStatement.setString(2, group.getSerialNumber());
                        preparedStatement.setString(3, group.getModelName());
                        preparedStatement.setString(4, group.getGroupName());
                        preparedStatement.setString(5, group.getStationName());
                        preparedStatement.setString(6, group.getErrorCode());
                        preparedStatement.setTimestamp(7, new Timestamp(group.getTestTime().getTime()));
                        preparedStatement.setString(8, group.getRepairer());
                        preparedStatement.setTimestamp(9, new Timestamp(group.getRepairTime().getTime()));
                        preparedStatement.setString(10, group.getReason());
                        preparedStatement.setString(11, group.getWorkdate());
                        preparedStatement.setString(12, group.getShift());
                        preparedStatement.setString(13, group.getSectionName());
                        preparedStatement.setString(14, group.getMo());
                        preparedStatement.setString(15, group.getTestLocationCode());

                    }
                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }

    @Override
    public void syncDataRepairOnlineWipFromB04(Date start, Date end) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>" +
                "    <GET_R_REPAIR_T xmlns=\"http://tempuri.org/\">\n" +
                "      <start_time>" + dateFormat.format(start) + "</start_time>\n" +
                "      <end_time>" + dateFormat.format(end) + "</end_time>\n" +
                "    </GET_R_REPAIR_T>" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.81.64/b04/Servicepostdata.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### getDataStation error ", e);
            return;
        }


        List<TestRepairSerialError> testRepairSerialErrors = parseRepairSerialErrorFromXmlB04(responseEntity.getBody());
        log.debug("### sync data repair CheckOUT online wip count  {}", testRepairSerialErrors.size());
//        repairSerialErrorList = repairSerialErrorList.stream().filter(error -> error.getSerialNumber().equalsIgnoreCase("194804534")).collect(Collectors.toList());
        //testRepairSerialErrorService.saveAll(testRepairSerialErrors);
        updateRepairOnlineWip(testRepairSerialErrors);
    }

    private List<TestRepairSerialError> parseRepairSerialErrorFromXmlB04(String xml) {
        if (StringUtils.isEmpty(xml)) {
            return Collections.emptyList();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Mytable");

            List<TestRepairSerialError> errorMap = new ArrayList<>();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String sectionName = eElement.getElementsByTagName("TEST_SECTION").item(0).getTextContent();
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String groupName = eElement.getElementsByTagName("TEST_GROUP").item(0).getTextContent();
                    String stationName = eElement.getElementsByTagName("TEST_STATION").item(0).getTextContent();
                    String errorCode = eElement.getElementsByTagName("TEST_CODE").item(0).getTextContent();
                    String repairTime = eElement.getElementsByTagName("REPAIR_TIME").item(0).getTextContent();
//                    if (StringUtils.isEmpty(sectionName) ||
//                            StringUtils.isEmpty(groupName) || "'SNSCAN'".contains(groupName.toUpperCase()) ||
//                            StringUtils.isEmpty(errorCode) ||  "'VI0016', 'VI0017', 'VI0018', 'VI0028', 'VI0029', 'PTH001', 'PTH002','VI0012'".contains(errorCode.toUpperCase())) {
//                        continue;
//                    }
                    if (StringUtils.isEmpty(repairTime)){
                        continue;
                    }

                    String factoryName = "B04";

                    TestRepairSerialError error = new TestRepairSerialError();
                    error.setRepairTime(df.parse(repairTime));
                    error.setFactory(factoryName);
                    error.setModelName(modelName);
                    error.setSectionName(sectionName);
                    error.setGroupName(groupName);
                    error.setStationName(stationName);
                    error.setErrorCode(errorCode);
                    error.setMo(eElement.getElementsByTagName("MO_NUMBER").item(0).getTextContent());
                    error.setSerialNumber(eElement.getElementsByTagName("SERIAL_NUMBER").item(0).getTextContent());
                    String testTime = eElement.getElementsByTagName("TEST_TIME").item(0).getTextContent();
                    error.setTestTime(df.parse(testTime));
                    error.setTestLocationCode(eElement.getElementsByTagName("LOCATION_CODE").item(0).getTextContent());
                    error.setRepairer(eElement.getElementsByTagName("REPAIRER").item(0).getTextContent());
                    error.setReason(eElement.getElementsByTagName("REASON_CODE").item(0).getTextContent());
                    error.setWorkdate(eElement.getElementsByTagName("R_CLASS_DATE").item(0).getTextContent());
                    error.setShift(eElement.getElementsByTagName("R_CLASS").item(0).getTextContent());

                    errorMap.add(error);
                }
            }

            return errorMap;
        } catch (Exception e) {
            log.error("### parseRepairSerialErrorFromXmlB04 re_online_wip_in_out", e);
            return Collections.emptyList();
        }
    }




}
