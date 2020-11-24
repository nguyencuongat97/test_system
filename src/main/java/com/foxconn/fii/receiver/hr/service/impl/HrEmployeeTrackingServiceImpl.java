package com.foxconn.fii.receiver.hr.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingBellNoRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingOfficeUnitInfoRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingPersonInfoRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingPersonWorkingStatusRepository;
import com.foxconn.fii.receiver.hr.service.HrEmployeeTrackingService;
import com.foxconn.fii.receiver.hr.service.HrEtEmployeeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HrEmployeeTrackingServiceImpl implements HrEmployeeTrackingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HrEmployeeTrackingBellNoRepository hrEmployeeTrackingBellNoRepository;

    @Autowired
    private HrEmployeeTrackingPersonInfoRepository hrEmployeeTrackingPersonInfoRepository;

    @Autowired
    private HrEmployeeTrackingPersonWorkingStatusRepository hrEmployeeTrackingPersonWorkingStatusRepository;

    @Autowired
    private HrEmployeeTrackingOfficeUnitInfoRepository hrEmployeeTrackingOfficeUnitInfoRepository;

    @Autowired
    private HrEtEmployeeInfoService hrEtEmployeeInfoService;

    @Autowired
    private HrEtEmployeeInfoService employeeInfoService;

    @Override
    public Object getDataHistoryByEmpNo(String empNo, String startDate, String endDate) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Map<String, String> jsDataMap = new HashMap<>();
        jsDataMap.put("EmpNo", empNo);
        jsDataMap.put("CardTime_S", startDate);
        jsDataMap.put("CardTime_E", endDate);
        String jsData = objectMapper.writeValueAsString(jsDataMap);

        String url = "http://10.224.69.75:8006/ClockRecordService/ClockRecordService.asmx?op=Get_ClockRecord";
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <Get_ClockRecord xmlns=\"http://tempuri.org/\">\n" +
                "      <JSData>" + jsData + "</JSData>\n" +
                "    </Get_ClockRecord>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity;
        responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));
        NodeList nodeList = document.getElementsByTagName("Get_ClockRecordResult");
        List<Map<String, String>> cardHistoryData = objectMapper.readValue(nodeList.item(0).getTextContent(), new TypeReference<List<Map<String, String>>>(){});
//        cardHistoryData = cardHistoryData.stream().filter(e -> e.get("F_EMPNO").toUpperCase().startsWith("V") && !StringUtils.isEmpty(e.get("F_INOUT"))).collect(Collectors.toList());

        result.put("data", cardHistoryData);
        result.put("startDate", startDate);
        result.put("endDate", endDate);

        HrEtEmployeeInfoServiceImpl.EmployeeInfo employeeInfo = hrEtEmployeeInfoService.employeeInfo(empNo);
        result.put("empNo", empNo);
        result.put("userName", employeeInfo.getUserName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(simpleDateFormat.parse(startDate));

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(simpleDateFormat.parse(endDate));

        Map<String, List<Map<String, String>>> cardDataGroupByDate = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();

        while (calStart.compareTo(calEnd) <= 0) {
            cal.setTime(calStart.getTime());
            cal.add(Calendar.HOUR_OF_DAY, 24);

            List<Map<String, String>> tmpCardData = cardHistoryData.stream().filter(e -> {
                try {
                    Date tmpDate = sdf.parse(e.get("F_CARDTIME"));
                    return tmpDate.compareTo(calStart.getTime()) >= 0 && tmpDate.compareTo(cal.getTime()) < 0;
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    return false;
                }
            }).collect(Collectors.toList());

            cardDataGroupByDate.put(sdfShort.format(calStart.getTime()), tmpCardData);
            calStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        result.put("dataByDay", cardDataGroupByDate);

        return result;
    }

//    @Override
//    public Object getForeignEmployeeClockRecord(List<String> empNoList, String timeSpanStr) throws IOException, ParserConfigurationException, SAXException, ParseException {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdfTimeSpan = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(sdfTimeSpan.parse(timeSpanStr.split(" - ")[0]));
//        startCal.set(Calendar.MILLISECOND, 0);
//        startCal.set(Calendar.SECOND, 0);
//        startCal.set(Calendar.MINUTE, 0);
//        startCal.set(Calendar.HOUR_OF_DAY, 0);
//
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(sdfTimeSpan.parse(timeSpanStr.split(" - ")[1]));
//        endCal.set(Calendar.MILLISECOND, 999);
//        endCal.set(Calendar.SECOND, 59);
//        endCal.set(Calendar.MINUTE, 59);
//        endCal.set(Calendar.HOUR_OF_DAY, 23);
//
//
//        Map<String, Map<String, Object>> dataResult = new HashMap<>();
//
//        Map<String, Object> metaResult = new HashMap<>();
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("data", dataResult);
//        result.put("meta", metaResult);
//
//        for (String empNo : empNoList) {
//            Calendar tmpStartCal = Calendar.getInstance();
//            tmpStartCal.setTime(startCal.getTime());
//
//            Calendar tmpEndCal = Calendar.getInstance();
//            tmpEndCal.setTime(endCal.getTime());
//
//            // get ClockRecord data
//            Map<String, String> jsDataMap = new HashMap<>();
//            jsDataMap.put("EmpNo", empNo);
//            jsDataMap.put("CardTime_S", simpleDateFormat.format(tmpStartCal.getTime()));
//            jsDataMap.put("CardTime_E", simpleDateFormat.format(tmpEndCal.getTime()));
//            String jsData = objectMapper.writeValueAsString(jsDataMap);
//
//            String url = "http://10.224.69.75:8006/ClockRecordService/ClockRecordService.asmx?op=Get_ClockRecord";
//            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//                    "  <soap12:Body>\n" +
//                    "    <Get_ClockRecord xmlns=\"http://tempuri.org/\">\n" +
//                    "      <JSData>" + jsData + "</JSData>\n" +
//                    "    </Get_ClockRecord>\n" +
//                    "  </soap12:Body>\n" +
//                    "</soap12:Envelope>";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.TEXT_XML);
//            HttpEntity<String> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<String> responseEntity;
//            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));
//            NodeList nodeList = document.getElementsByTagName("Get_ClockRecordResult");
//            List<Map<String, String>> cardHistoryData = objectMapper.readValue(nodeList.item(0).getTextContent(), new TypeReference<List<Map<String, String>>>(){});
//
//            Map<String, List<Map<String, String>>> cardHistoryResult = cardHistoryData.stream().sorted((e1, e2) -> {
//                try {
//                    return sdf.parse(e1.get("F_CARDTIME")).compareTo(sdf.parse(e2.get("F_CARDTIME")));
//                } catch (ParseException e) {
//                    return 0;
//                }
//            }).collect(Collectors.groupingBy(e -> e.get("F_CARDTIME").split("T")[0], LinkedHashMap::new, Collectors.toList()));
//
//            Map<String, Boolean> shortCardHistoryResult = new LinkedHashMap<>();
//
//            while (tmpStartCal.before(tmpEndCal)) {
//                String tmpWorkDate = sdfDate.format(tmpStartCal.getTime());
//
//                List<Map<String, String>> tmpCardHistory = cardHistoryResult.get(tmpWorkDate) != null ? cardHistoryResult.get(tmpWorkDate) : Collections.EMPTY_LIST;
//                if (tmpCardHistory.isEmpty()) {
//                    shortCardHistoryResult.put(tmpWorkDate, false);
//                } else {
//                    shortCardHistoryResult.put(tmpWorkDate, true);
//                }
//
//                tmpStartCal.add(Calendar.DAY_OF_YEAR, 1);
//            }
//
//
//            // get Duty data
//
//
//            Map<String, Object> empNoResult = new HashMap<>();
//            empNoResult.put("clockRecordHistory", cardHistoryResult);
//            empNoResult.put("shortClockRecordHistory", shortCardHistoryResult);
//
//            dataResult.put(empNo, empNoResult);
//        }
//
//        return result;
//    }

    @Override
    public Object getForeignEmployeeDuty(List<String> empNoList, String timeSpanStr) throws ParseException, IOException, ParserConfigurationException, SAXException {

        if (empNoList.size() > 50) {
            empNoList = empNoList.subList(0, 50);
        }

        SimpleDateFormat sdfTimeSpan = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat sdfDutyDate = new SimpleDateFormat("yyyy/MM/dd");

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(sdfTimeSpan.parse(timeSpanStr.split(" - ")[0]));
        startCal.set(Calendar.MILLISECOND, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.HOUR_OF_DAY, 0);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(sdfTimeSpan.parse(timeSpanStr.split(" - ")[1]));
        endCal.set(Calendar.MILLISECOND, 999);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.HOUR_OF_DAY, 23);


        Map<String, Map<String, Object>> dataResult = new HashMap<>();

        Map<String, Object> metaResult = new HashMap<>();

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataResult);
        result.put("meta", metaResult);

        for (String empNo : empNoList) {
            HrEtEmployeeInfoServiceImpl.EmployeeInfo employeeInfo = employeeInfoService.employeeInfo(empNo);

            Calendar tmpStartCal = Calendar.getInstance();
            tmpStartCal.setTime(startCal.getTime());

            Calendar tmpEndCal = Calendar.getInstance();
            tmpEndCal.setTime(endCal.getTime());

            Map<String, String> jsDataMap = new HashMap<>();
            jsDataMap.put("EmpNo", empNo);
            jsDataMap.put("DepartName", "");
            jsDataMap.put("DutyDate_S", sdfDutyDate.format(startCal.getTime()));
            jsDataMap.put("DutyDate_E", sdfDutyDate.format(endCal.getTime()));

            String jsData = objectMapper.writeValueAsString(jsDataMap);

            String url = "http://10.224.69.75:8006/ClockRecordService/ClockRecordService.asmx/Get_DutyStatus";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
            requestMap.add("JSData", jsData);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));
            NodeList nodeList = document.getElementsByTagName("string");
            List<Map<String, String>> dutyDataList = objectMapper.readValue(nodeList.item(0).getTextContent(), new TypeReference<List<Map<String, String>>>(){});

            Map<String, Map<String, String>> dutyDataMap = dutyDataList.stream().collect(Collectors.toMap(e -> e.get("F_DUTYDATE"), e -> e, (oldElement, newElement) -> oldElement, LinkedHashMap::new));

            Map<String, Boolean> shortDutyResult = new LinkedHashMap<>();

            while (tmpStartCal.before(tmpEndCal)) {
                String tmpDutyDate = sdfDutyDate.format(tmpStartCal.getTime());

                Map<String, String> dutyData = dutyDataMap.get(tmpDutyDate) != null ? dutyDataMap.get(tmpDutyDate) : Collections.EMPTY_MAP;

                if (dutyData.isEmpty()) {
                    shortDutyResult.put(tmpDutyDate, false);
                } else {
                    if (StringUtils.isEmpty(dutyData.get("F_BEGINWORK")) && StringUtils.isEmpty(dutyData.get("F_BEGINREST")) && StringUtils.isEmpty(dutyData.get("F_ENDREST")) && StringUtils.isEmpty(dutyData.get("F_ENDWORK"))) {
                        shortDutyResult.put(tmpDutyDate, false);
                    } else {
                        shortDutyResult.put(tmpDutyDate, true);
                    }
                }

                tmpStartCal.add(Calendar.DAY_OF_YEAR, 1);
            }

            Map<String, Object> empNoResult = new HashMap<>();
            empNoResult.put("dutyResult", dutyDataMap);
            empNoResult.put("shortDutyResult", shortDutyResult);
            empNoResult.put("totalWorkDay", shortDutyResult.entrySet().stream().filter(e -> e.getValue() == true).count());
            empNoResult.put("totalOffDay", shortDutyResult.entrySet().stream().filter(e -> e.getValue() == false).count());
            empNoResult.put("name", employeeInfo.getUserName());

            dataResult.put(empNo, empNoResult);
        }

        return result;
    }

}
