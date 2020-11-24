package com.foxconn.fii.receiver.hr.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.receiver.hr.service.HrEtGetDataService;
import com.foxconn.fii.receiver.hr.service.HrEtWorkCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrEtGetDataServiceImpl implements HrEtGetDataService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HrEmployeeTrackingWorkShiftMetaRepository hrEmployeeTrackingWorkShiftMetaRepository;

    @Autowired
    private HrEmployeeTrackingWorkResultMetaRepository hrEmployeeTrackingWorkResultMetaRepository;

    @Autowired
    private HrEmployeeTrackingOfficeUnitInfoRepository hrEmployeeTrackingOfficeUnitInfoRepository;

    @Autowired
    private HrEmployeeTrackingPersonInfoRepository hrEmployeeTrackingPersonInfoRepository;

    @Autowired
    private HrEmployeeTrackingPersonWorkingStatusRepository hrEmployeeTrackingPersonWorkingStatusRepository;

    @Autowired
    private HrEtEmployeeCountingRepository hrEtEmployeeCountingRepository;

    @Autowired
    private HrEtWorkCountService hrEtWorkCountService;

    @Autowired
    private HrEtBackupDutyRepository hrEtBackupDutyRepository;

    @Autowired
    private HrEtBackupOvertimeDataRepository hrEtBackupOvertimeDataRepository;

    @Override
    public void getDailyData(int dayOfMonth, int month) throws ParserConfigurationException, SAXException, ParseException, IOException {
        // Hr personInfo sync data BEGIN
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
        HrEmployeeTrackingPersonWorkingStatus lastWorkingStatus = hrEmployeeTrackingPersonWorkingStatusRepository.findTop1ByOrderByWorkDateDesc();
        if (lastWorkingStatus != null) {
            String lastWorkDateStr = sdfDate.format(lastWorkingStatus.getWorkDate());
            String currentDateStr = sdfDate.format(new Date());
            if (!lastWorkDateStr.equalsIgnoreCase(currentDateStr)) {
                updateEmployeePersonInfo();
            }
        }
        // Hr personInfo sync data END

        // Hr dashboard sync data BEGIN
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.MILLISECOND, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MINUTE, 0);
        if (startCal.get(Calendar.HOUR_OF_DAY) >= 12) {
            startCal.set(Calendar.HOUR_OF_DAY, 12);
        } else {
            startCal.set(Calendar.HOUR_OF_DAY, 0);
        }

        if (month >= 1 && month <= 12) {
            startCal.set(Calendar.MONTH, month - 1);
        }

        if (dayOfMonth > 0 && dayOfMonth <= startCal.getMaximum(Calendar.DAY_OF_MONTH)) {
            startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        startCal.add(Calendar.HOUR_OF_DAY, -12);
        Calendar endCal = Calendar.getInstance();

        while (startCal.getTime().compareTo(new Date()) <= 0) {
            endCal.setTime(startCal.getTime());
            endCal.add(Calendar.HOUR_OF_DAY, 10);
            endCal.add(Calendar.MILLISECOND, -1);
            getData(startCal.getTime(), endCal.getTime());
            employeeCounting(new java.sql.Date(startCal.getTimeInMillis()), new java.sql.Date(startCal.getTimeInMillis()));
            startCal.add(Calendar.HOUR_OF_DAY, 12);
//            startCal.add(Calendar.DAY_OF_MONTH, 1);
//            break;
        }
        // Hr dashboard sync data END
    }

    @Override
    public void getData(Date startDate, Date endDate) throws ParseException, ParserConfigurationException, IOException, SAXException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Map<String, String> jsDataMap = new HashMap<>();
        jsDataMap.put("EmpNo", "");
        jsDataMap.put("CardTime_S", sdf.format(startDate));
        jsDataMap.put("CardTime_E", sdf.format(endDate));
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
        cardHistoryData = cardHistoryData.stream().filter(e -> e.get("F_EMPNO").toUpperCase().startsWith("V")).collect(Collectors.toList());

        checkNewEmployee(cardHistoryData.stream().map(e -> e.get("F_EMPNO")).collect(Collectors.toSet()));

        checkWorkingStatus(cardHistoryData, new java.sql.Date(startDate.getTime()));

        log.info("### HR getdata " + sdf.format(startDate) + " to " + sdf.format(endDate) + " success ###");
    }

    @Override
    public void employeeCounting(java.sql.Date startDate, java.sql.Date endDate) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        calendar.setTimeInMillis(startDate.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -4);
        java.sql.Date startQueryDate = new java.sql.Date(calendar.getTimeInMillis());

        List<HrEmployeeTrackingPersonWorkingStatus> statusList = hrEmployeeTrackingPersonWorkingStatusRepository.findByWorkDateBetween(startQueryDate, endDate);

        List<HrEmployeeTrackingPersonInfo> personInfoData = hrEmployeeTrackingPersonInfoRepository.findAll();

        Set<String> departSet = personInfoData.stream().map(e -> e.getOfficeName()).collect(Collectors.toSet());

        List<HrEtEmployeeCounting> employeeCountingList = hrEtEmployeeCountingRepository.findByWorkDateBetween(startDate, endDate);

        java.sql.Date markDate;
        try {
            markDate  = hrEmployeeTrackingPersonWorkingStatusRepository.findTop1ByOrderByWorkDateAsc().getWorkDate();
        } catch (Exception e) {
            log.info(e.toString());
            return;
        }

        startCal.setTimeInMillis(startDate.getTime());
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        endCal.setTimeInMillis(endDate.getTime());
        endCal.set(Calendar.HOUR_OF_DAY, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);

        List<HrEtEmployeeCounting> countingResult = new ArrayList<>();

        List<HrEmployeeTrackingPersonWorkingStatus> newWorkingStatusList = new ArrayList<>();

        List<HrEmployeeTrackingPersonInfo> newPersonInfoList = new ArrayList<>();

        while (!startCal.after(endCal)) {
            for (String depart : departSet) {
                List<HrEmployeeTrackingPersonInfo> personInfoList = personInfoData.stream().filter(e -> e.getOfficeName().equalsIgnoreCase(depart)).collect(Collectors.toList());

                List<HrEmployeeTrackingPersonWorkingStatus> workingStatusList = statusList.stream().filter(e -> e.getDepartName().equalsIgnoreCase(depart) && e.getWorkStatus().equalsIgnoreCase("WORKING")).collect(Collectors.toList());

                java.sql.Date tmpDate = new java.sql.Date(startCal.getTimeInMillis());

                HrEtEmployeeCounting employeeCountingWorkFull = employeeCountingList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("FULL") && e.getDepartName().equalsIgnoreCase(depart)).findFirst().orElse(new HrEtEmployeeCounting());
                if (employeeCountingWorkFull.getId() == null) {
                    employeeCountingWorkFull.setDepartName(depart);
                    employeeCountingWorkFull.setCountType("WORKING");
                    employeeCountingWorkFull.setWorkDate(tmpDate);
                    employeeCountingWorkFull.setWorkShift("FULL");
                    employeeCountingWorkFull.setCountQty((int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate)).count());
                    countingResult.add(employeeCountingWorkFull);
                } else {
                    int countQty = (int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate)).count();
                    if (!employeeCountingWorkFull.getCountQty().equals(countQty)) {
                        employeeCountingWorkFull.setCountQty(countQty);
                        countingResult.add(employeeCountingWorkFull);
                    }
                }

                HrEtEmployeeCounting employeeCountingWorkDay = employeeCountingList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("DAY") && e.getDepartName().equalsIgnoreCase(depart)).findFirst().orElse(new HrEtEmployeeCounting());
                if (employeeCountingWorkDay.getId() == null) {
                    employeeCountingWorkDay.setDepartName(depart);
                    employeeCountingWorkDay.setCountType("WORKING");
                    employeeCountingWorkDay.setWorkDate(tmpDate);
                    employeeCountingWorkDay.setWorkShift("DAY");
                    employeeCountingWorkDay.setCountQty((int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getWorkShift().equalsIgnoreCase("DAY")).count());
                    countingResult.add(employeeCountingWorkDay);
                } else {
                    int countQty = (int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getWorkShift().equalsIgnoreCase("DAY")).count();
                    if (!employeeCountingWorkDay.getCountQty().equals(countQty)) {
                        employeeCountingWorkDay.setCountQty(countQty);
                        countingResult.add(employeeCountingWorkDay);
                    }
                }

                HrEtEmployeeCounting employeeCountingWorkNight = employeeCountingList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("NIGHT") && e.getDepartName().equalsIgnoreCase(depart)).findFirst().orElse(new HrEtEmployeeCounting());
                if (employeeCountingWorkNight.getId() == null) {
                    employeeCountingWorkNight.setDepartName(depart);
                    employeeCountingWorkNight.setCountType("WORKING");
                    employeeCountingWorkNight.setWorkDate(tmpDate);
                    employeeCountingWorkNight.setWorkShift("NIGHT");
                    employeeCountingWorkNight.setCountQty((int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getWorkShift().equalsIgnoreCase("NIGHT")).count());
                    countingResult.add(employeeCountingWorkNight);
                } else {
                    int countQty = (int) workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getWorkShift().equalsIgnoreCase("NIGHT")).count();
                    if (!employeeCountingWorkNight.getCountQty().equals(countQty)) {
                        employeeCountingWorkNight.setCountQty(countQty);
                        countingResult.add(employeeCountingWorkNight);
                    }
                }

                List<String> workingEmpList = workingStatusList.stream().filter(e -> e.getWorkDate().equals(tmpDate)).map(e -> e.getEmpNo()).collect(Collectors.toList());
                List<HrEmployeeTrackingPersonInfo> missingList = personInfoList.stream().filter(e -> e.getHireDate().compareTo(tmpDate) <= 0 && e.getLeaveDate().after(tmpDate) && !workingEmpList.contains(e.getEmpNo()) && (e.getStopTrackingDate() == null || e.getStopTrackingDate().compareTo(tmpDate) >= 0)).collect(Collectors.toList());
                int missingQty = missingList.size();

                calendar.setTime(startCal.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, -4);
                java.sql.Date tmpQueryDate = new java.sql.Date(calendar.getTimeInMillis());
                int alertCount = 0;

                for (HrEmployeeTrackingPersonInfo personInfo : missingList) {
                    HrEmployeeTrackingPersonWorkingStatus workingStatus = statusList.stream().filter(e -> e.getEmpNo().equalsIgnoreCase(personInfo.getEmpNo()) && e.getWorkDate().equals(tmpDate)).findFirst().orElse(new HrEmployeeTrackingPersonWorkingStatus());
                    if (!StringUtils.isEmpty(workingStatus.getDepartName()) && !StringUtils.isEmpty(personInfo.getOfficeName()) && !personInfo.getOfficeName().equalsIgnoreCase(workingStatus.getDepartName())) {
                        missingQty--;
                        continue;
                    }

                    boolean editMark = false;

                    if (StringUtils.isEmpty(workingStatus.getWorkStatus()) || !workingStatus.getWorkStatus().equalsIgnoreCase("WORKING")) {
                        workingStatus.setEmpNo(personInfo.getEmpNo());
                        workingStatus.setWorkDate(tmpDate);
                        if (StringUtils.isEmpty(workingStatus.getDepartName())) {
                            workingStatus.setDepartName(personInfo.getOfficeName());
                        }
                        workingStatus.setWorkShift("FULL");
                        workingStatus.setWorkStatus("MISSING");

                        editMark = true;
                    }

                    if (tmpQueryDate.compareTo(markDate) >= 0 && personInfo.getHireDate().before(tmpQueryDate)) {
                        long workDays = statusList.stream().filter(e -> e.getEmpNo().equalsIgnoreCase(personInfo.getEmpNo()) && e.getWorkStatus().equalsIgnoreCase("WORKING") && e.getWorkDate().compareTo(tmpQueryDate) >= 0 && e.getWorkDate().compareTo(tmpDate) < 0).count();
                        if (workDays == 0 && !workingStatus.getWorkStatus().equalsIgnoreCase("WORKING")) {
                            workingStatus.setEmpNo(personInfo.getEmpNo());
                            workingStatus.setWorkDate(tmpDate);
                            if (StringUtils.isEmpty(workingStatus.getDepartName())) {
                                workingStatus.setDepartName(personInfo.getOfficeName());
                            }
                            workingStatus.setWorkShift("FULL");
                            workingStatus.setWorkStatus("ALERT");
                            alertCount++;
                            missingQty--;

                            editMark = true;

                            personInfo.setStopTrackingDate(tmpDate);
                            newPersonInfoList.add(personInfo);
                        }
                    }

                    if (editMark) {
                        newWorkingStatusList.add(workingStatus);
                    }
                }

                HrEtEmployeeCounting employeeCountingMissing = employeeCountingList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getCountType().equalsIgnoreCase("MISSING") && e.getWorkShift().equalsIgnoreCase("FULL") && e.getDepartName().equalsIgnoreCase(depart)).findFirst().orElse(new HrEtEmployeeCounting());

                if (employeeCountingMissing.getId() == null) {
                    employeeCountingMissing.setDepartName(depart);
                    employeeCountingMissing.setCountType("MISSING");
                    employeeCountingMissing.setWorkDate(tmpDate);
                    employeeCountingMissing.setWorkShift("FULL");
                    employeeCountingMissing.setCountQty(missingQty);
                    countingResult.add(employeeCountingMissing);
                } else {
                    if (!employeeCountingMissing.getCountQty().equals(missingQty)) {
                        employeeCountingMissing.setCountQty(missingQty);
                        countingResult.add(employeeCountingMissing);
                    }
                }

                HrEtEmployeeCounting employeeCountingAlert = employeeCountingList.stream().filter(e -> e.getWorkDate().equals(tmpDate) && e.getCountType().equalsIgnoreCase("ALERT") && e.getWorkShift().equalsIgnoreCase("FULL") && e.getDepartName().equalsIgnoreCase(depart)).findFirst().orElse(new HrEtEmployeeCounting());

                if (employeeCountingAlert.getId() == null) {
                    employeeCountingAlert.setDepartName(depart);
                    employeeCountingAlert.setCountType("ALERT");
                    employeeCountingAlert.setWorkDate(tmpDate);
                    employeeCountingAlert.setWorkShift("FULL");
                    employeeCountingAlert.setCountQty(alertCount);
                    countingResult.add(employeeCountingAlert);
                } else {
                    if (!employeeCountingAlert.getCountQty().equals(alertCount)) {
                        employeeCountingAlert.setCountQty(alertCount);
                        countingResult.add(employeeCountingAlert);
                    }
                }

                List<HrEmployeeTrackingPersonInfo> turnOnTrackingPersonInfoList = personInfoData.stream().filter(e -> workingEmpList.contains(e.getEmpNo()) && e.getStopTrackingDate() != null).collect(Collectors.toList());
                for (HrEmployeeTrackingPersonInfo personInfo: turnOnTrackingPersonInfoList) {
                    personInfo.setStopTrackingDate(null);
                }
                newPersonInfoList.addAll(turnOnTrackingPersonInfoList);
            }

            startCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        hrEmployeeTrackingPersonInfoRepository.saveAll(newPersonInfoList);
        hrEmployeeTrackingPersonWorkingStatusRepository.saveAll(newWorkingStatusList);
        hrEtEmployeeCountingRepository.saveAll(countingResult);

        log.info("### HR depart employee counting " + sdf.format(startDate) + " success ###");
    }

    @Override
    public void updateEmployeePersonInfo() throws ParserConfigurationException, IOException, SAXException {
        java.sql.Date today = new java.sql.Date((new Date()).getTime());

        Set<String> ouCodeList = hrEmployeeTrackingOfficeUnitInfoRepository.findAll().stream().map(e -> e.getOfficeUnitCode()).collect(Collectors.toSet());
        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll().stream().filter(e -> e.getLeaveDate().after(today)).collect(Collectors.toList());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Set<String> checkedEmpNo = new HashSet<>();

        Set<String> newOuCodeList = new HashSet<>();
        Set<HrEmployeeTrackingOfficeUnitInfo> newOfficeUnitInfoList = new HashSet<>();

        for (String ouCode : ouCodeList) {
            String url = "http://10.132.37.98:8006/personal/ElistQuery.asmx?op=ByDepartment";
            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <ByDepartment xmlns=\"http://tempuri.org/\">\n" +
                    "      <DepartID>" + ouCode + "</DepartID>\n" +
                    "    </ByDepartment>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity;
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));


                NodeList personInfoNodeList = document.getElementsByTagName("ElistQuery");
                for (int i = 0; i < personInfoNodeList.getLength(); i++) {
                    NodeList personInfoNode = personInfoNodeList.item(i).getChildNodes();
                    try {
                        String userId = ((Element) personInfoNode).getElementsByTagName("USER_ID").item(0).getTextContent();
                        String leaveDateStr = ((Element) personInfoNode).getElementsByTagName("LEAVEDAY").item(0).getTextContent();
                        String hireDateStr = ((Element) personInfoNode).getElementsByTagName("HIREDATE").getLength() > 0 ? ((Element) personInfoNode).getElementsByTagName("HIREDATE").item(0).getTextContent() : null;
                        String userName = "";
                        if (((Element) personInfoNode).getElementsByTagName("USER_NAME").getLength() > 0) {
                            userName = ((Element) personInfoNode).getElementsByTagName("USER_NAME").item(0).getTextContent();
                        }
                        String officeCode = ((Element) personInfoNode).getElementsByTagName("CURRENT_OU_CODE").item(0).getTextContent();
                        String officeName = ((Element) personInfoNode).getElementsByTagName("CURRENT_OU_NAME").item(0).getTextContent();

                        HrEmployeeTrackingPersonInfo personInfo = personInfoList.stream().filter(e -> e.getEmpNo().equalsIgnoreCase(userId)).findFirst().orElse(null);
                        if (personInfo != null) {
                            personInfo.setName(userName);
                            personInfo.setOfficeName(officeName);
                            personInfo.setOfficeCode(officeCode);
                            personInfo.setLeaveDate(new java.sql.Date(sdf.parse(leaveDateStr).getTime()));
                            if (!StringUtils.isEmpty(hireDateStr)) {
                                personInfo.setHireDate(new java.sql.Date(sdf.parse(hireDateStr).getTime()));
                            }
                            checkedEmpNo.add(userId);

                            if (!ouCodeList.contains(officeCode) && !newOuCodeList.contains(officeCode)) {
                                newOuCodeList.add(officeCode);

                                HrEmployeeTrackingOfficeUnitInfo hrEmployeeTrackingOfficeUnitInfo = new HrEmployeeTrackingOfficeUnitInfo();
                                hrEmployeeTrackingOfficeUnitInfo.setOfficeUnitCode(officeCode);
                                hrEmployeeTrackingOfficeUnitInfo.setOfficeUnitName(officeName);

                                newOfficeUnitInfoList.add(hrEmployeeTrackingOfficeUnitInfo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }

        Set<String> uncheckedEmpNo = personInfoList.stream().filter(e -> !checkedEmpNo.contains(e.getEmpNo())).map(e -> e.getEmpNo()).collect(Collectors.toSet());
        for (String empNo : uncheckedEmpNo) {
            String url = "http://10.132.37.98:8006/personal/ElistQuery.asmx?op=ByUserID";
            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <ByUserID xmlns=\"http://tempuri.org/\">\n" +
                    "      <EmployeeID>" + empNo + "</EmployeeID>\n" +
                    "    </ByUserID>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity;
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));

            try {
                String userId = document.getElementsByTagName("USER_ID").item(0).getTextContent();
                String leaveDateStr = document.getElementsByTagName("LEAVEDAY").item(0).getTextContent();
                String hireDateStr = document.getElementsByTagName("HIREDATE").item(0).getTextContent();
                String userName = "";
                if (document.getElementsByTagName("USER_NAME").getLength() > 0) {
                    userName = document.getElementsByTagName("USER_NAME").item(0).getTextContent();
                }
                String officeCode = document.getElementsByTagName("CURRENT_OU_CODE").item(0).getTextContent();
                String officeName = document.getElementsByTagName("CURRENT_OU_NAME").item(0).getTextContent();

                HrEmployeeTrackingPersonInfo personInfo = personInfoList.stream().filter(e -> e.getEmpNo().equalsIgnoreCase(userId)).findFirst().orElse(null);
                if (personInfo != null) {
                    personInfo.setName(userName);
                    personInfo.setOfficeName(officeName);
                    personInfo.setOfficeCode(officeCode);
                    personInfo.setLeaveDate(new java.sql.Date(sdf.parse(leaveDateStr).getTime()));
                    personInfo.setHireDate(new java.sql.Date(sdf.parse(hireDateStr).getTime()));

                    if (!ouCodeList.contains(officeCode) && !newOuCodeList.contains(officeCode)) {
                        newOuCodeList.add(officeCode);

                        HrEmployeeTrackingOfficeUnitInfo hrEmployeeTrackingOfficeUnitInfo = new HrEmployeeTrackingOfficeUnitInfo();
                        hrEmployeeTrackingOfficeUnitInfo.setOfficeUnitCode(officeCode);
                        hrEmployeeTrackingOfficeUnitInfo.setOfficeUnitName(officeName);

                        newOfficeUnitInfoList.add(hrEmployeeTrackingOfficeUnitInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hrEmployeeTrackingOfficeUnitInfoRepository.saveAll(newOfficeUnitInfoList);

        hrEmployeeTrackingPersonInfoRepository.saveAll(personInfoList);
    }

    private void checkWorkingStatus(List<Map<String, String>> cardHistoryData, java.sql.Date workDate) throws ParseException {
        Map<String, List<Map<String, String>>> cardHistoryMap = cardHistoryData.stream().collect(Collectors.groupingBy(e -> e.get("F_EMPNO")));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll();

        List<HrEmployeeTrackingPersonWorkingStatus> workingStatusList = hrEmployeeTrackingPersonWorkingStatusRepository.findByWorkDate(workDate);

        List<HrEmployeeTrackingPersonWorkingStatus> newWorkingStatusList = new ArrayList<>();

        for (List<Map<String, String>> personData : cardHistoryMap.values()) {
            Map<String, String> lastCard = personData.stream().max((e1, e2) -> {
                try {
                    Date d1 = sdf.parse(e1.get("F_CARDTIME"));
                    Date d2 = sdf.parse(e2.get("F_CARDTIME"));
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }).orElse(null);
            if (lastCard == null) {
                continue;
            }

            HrEmployeeTrackingPersonInfo personInfo = personInfoList.stream().filter(e -> e.getEmpNo().equalsIgnoreCase(lastCard.get("F_EMPNO")))
                    .findFirst().orElse(null);
            if (personInfo == null) {
                continue;
            }

            HrEmployeeTrackingPersonWorkingStatus workingStatus = workingStatusList.stream()
                    .filter(e -> e.getEmpNo().equalsIgnoreCase(personInfo.getEmpNo())).findFirst().orElse(null);
            if (workingStatus == null) {
                workingStatus = new HrEmployeeTrackingPersonWorkingStatus();
                workingStatus.setEmpNo(personInfo.getEmpNo());
                workingStatus.setWorkDate(workDate);
                workingStatus.setDepartName(personInfo.getOfficeName());
            } else if (workingStatus.getWorkStatus().equalsIgnoreCase("WORKING")){
                continue;
            }

            Calendar cardTime = Calendar.getInstance();
            cardTime.setTime(sdf.parse(lastCard.get("F_CARDTIME")));
            if (StringUtils.isEmpty(lastCard.get("F_INOUT"))) {
                workingStatus.setWorkShift("FULL");
                workingStatus.setWorkStatus("WORKING");
                newWorkingStatusList.add(workingStatus);
            } else if (cardTime.get(Calendar.HOUR_OF_DAY) >= 3 && cardTime.get(Calendar.HOUR_OF_DAY) <= 9 && lastCard.get("F_INOUT").equalsIgnoreCase("進")) {
                workingStatus.setWorkShift("DAY");
                workingStatus.setWorkStatus("WORKING");
                newWorkingStatusList.add(workingStatus);
            } else if (cardTime.get(Calendar.HOUR_OF_DAY) >= 15 && cardTime.get(Calendar.HOUR_OF_DAY) <= 21 && lastCard.get("F_INOUT").equalsIgnoreCase("進")) {
                workingStatus.setWorkShift("NIGHT");
                workingStatus.setWorkStatus("WORKING");
                newWorkingStatusList.add(workingStatus);
            }
        }

        hrEmployeeTrackingPersonWorkingStatusRepository.saveAll(newWorkingStatusList);
    }

    private void checkNewEmployee(Set<String> empNoList) throws ParserConfigurationException, IOException, SAXException {
        List<String> currentEmpNoList = hrEmployeeTrackingPersonInfoRepository.findAll().stream().map(e -> e.getEmpNo()).collect(Collectors.toList());

        List<String> currentOfficeCodeList = hrEmployeeTrackingOfficeUnitInfoRepository.findAll().stream().map(e -> e.getOfficeUnitCode()).collect(Collectors.toList());

        empNoList = empNoList.stream().filter(e -> !currentEmpNoList.contains(e)).collect(Collectors.toSet());

        for (String empNo : empNoList) {
            String url = "http://10.132.37.98:8006/personal/ElistQuery.asmx?op=ByUserID";
            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <ByUserID xmlns=\"http://tempuri.org/\">\n" +
                    "      <EmployeeID>" + empNo + "</EmployeeID>\n" +
                    "    </ByUserID>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity;
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));

            try {
                String userId = document.getElementsByTagName("USER_ID").item(0).getTextContent();
                String leaveDateStr = document.getElementsByTagName("LEAVEDAY").item(0).getTextContent();
                String hireDateStr = document.getElementsByTagName("HIREDATE").item(0).getTextContent();
                String userName = "";
                if (document.getElementsByTagName("USER_NAME").getLength() > 0) {
                    userName = document.getElementsByTagName("USER_NAME").item(0).getTextContent();
                }
                String officeCode = document.getElementsByTagName("CURRENT_OU_CODE").item(0).getTextContent();
                String officeName = document.getElementsByTagName("CURRENT_OU_NAME").item(0).getTextContent();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                Calendar hireDate = Calendar.getInstance();
                hireDate.setTime(sdf.parse(hireDateStr));

                Calendar leaveDate = Calendar.getInstance();
                leaveDate.setTime(sdf.parse(leaveDateStr));

                HrEmployeeTrackingPersonInfo personInfo = new HrEmployeeTrackingPersonInfo();

                personInfo.setEmpNo(userId);
                personInfo.setHireDate(new java.sql.Date(hireDate.getTimeInMillis()));
                personInfo.setLeaveDate(new java.sql.Date(leaveDate.getTimeInMillis()));
                personInfo.setName(userName);
                personInfo.setOfficeCode(officeCode);
                personInfo.setOfficeName(officeName);

                hrEmployeeTrackingPersonInfoRepository.save(personInfo);

                if (!currentOfficeCodeList.contains(officeCode)) {
                    HrEmployeeTrackingOfficeUnitInfo officeUnitInfo = new HrEmployeeTrackingOfficeUnitInfo();
                    officeUnitInfo.setOfficeUnitCode(officeCode);
                    officeUnitInfo.setOfficeUnitName(officeName);
                    officeUnitInfo.setFactoryName("UNKNOWN");
                    hrEmployeeTrackingOfficeUnitInfoRepository.save(officeUnitInfo);
                    currentOfficeCodeList.add(officeCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void backupDutyData() throws IOException, SAXException, ParserConfigurationException, ParseException {

        SimpleDateFormat sdfWorkDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll().stream().filter(e -> e.getLeaveDate().after(cal.getTime())).collect(Collectors.toList());

        for (HrEmployeeTrackingPersonInfo personInfo : personInfoList) {
            int tryTime = 0;
            List<Map<String, String>> dutyData = new ArrayList<>();

            while (tryTime < 10) {
                try {
                    dutyData = hrEtWorkCountService.dutyData(personInfo.getEmpNo(), "", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    tryTime += 100;
                } catch (Exception e) {
                    log.error("can't get backupDutyData");
                }
                tryTime++;
            }
//            List<Map<String, String>> dutyData = hrEtWorkCountService.dutyData(personInfo.getEmpNo(), "", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (dutyData.isEmpty()) {
                continue;
            }

            List<HrEtBackupDuty> backupDutyList = new ArrayList<>();

            for (Map<String, String> duty : dutyData) {
                String employeeNo = duty.get("F_EMPNO");
                String employeeName = duty.get("F_NAME");
                String employeeSite = duty.get("F_SITE");
                String employeeDepartName = duty.get("F_DEPARTNAME");
                String employeeClassNo = duty.get("F_CLASSNO");
                java.sql.Date workDate = new java.sql.Date(sdfWorkDate.parse(duty.get("F_DUTYDATE")).getTime());
                Date beginWork = duty.get("F_BEGINWORK") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_BEGINWORK"));
                Date beginRest = duty.get("F_BEGINREST") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_BEGINREST"));
                Date endRest = duty.get("F_ENDREST") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_ENDREST"));
                Date endWork = duty.get("F_ENDWORK") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_ENDWORK"));
                Date beginOvertime = duty.get("F_OVERTIMEFROM") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_OVERTIMEFROM"));
                Date endOvertime = duty.get("F_OVERTIMETO") == null ? null : sdfTime.parse(duty.get("F_DUTYDATE") + " " + duty.get("F_OVERTIMETO"));
                Double overtimeCount = duty.get("F_OVERTIMECOUNT") == null ? null : Double.valueOf(duty.get("F_OVERTIMECOUNT"));
                String workResult = duty.get("F_MODIFYRESULT");
                Double owTime = duty.get("F_OWTIME") == null ? null : Double.valueOf(duty.get("F_OWTIME"));
                String isModifyResult = duty.get("F_ISMODIFYRESULT");

                if (beginWork != null && beginRest != null && beginWork.after(beginRest)) {
                    beginRest.setTime(beginRest.getTime() + 24 * 60 * 60 * 1000L);
                }

                if (beginRest != null && endRest != null && beginRest.after(endRest)) {
                    endRest.setTime(endRest.getTime() + 24 * 60 * 60 * 1000L);
                }

                if (endRest != null && endWork != null && endRest.after(endWork)) {
                    endWork.setTime(endWork.getTime() + 24 * 60 * 60 * 1000L);
                }

                if (endWork != null && beginOvertime != null && endWork.after(beginOvertime)) {
                    beginOvertime.setTime(beginOvertime.getTime() + 24 * 60 * 60 * 1000L);
                }

                if (beginOvertime != null && endOvertime != null && beginOvertime.after(endOvertime)) {
                    endOvertime.setTime(endOvertime.getTime() + 24 * 60 * 60 * 1000L);
                }

                HrEtBackupDuty backupDuty = new HrEtBackupDuty();
                backupDuty.setEmployeeNo(employeeNo);
                backupDuty.setEmployeeName(employeeName);
                backupDuty.setEmployeeSite(employeeSite);
                backupDuty.setEmployeeDepartName(employeeDepartName);
                backupDuty.setEmployeeClassNo(employeeClassNo);
                backupDuty.setWorkDate(workDate);
                backupDuty.setBeginWork(beginWork);
                backupDuty.setBeginRest(beginRest);
                backupDuty.setEndRest(endRest);
                backupDuty.setEndWork(endWork);
                backupDuty.setBeginOvertime(beginOvertime);
                backupDuty.setEndOvertime(endOvertime);
                backupDuty.setOvertimeCount(overtimeCount);
                backupDuty.setWorkResult(workResult);
                backupDuty.setOwTime(owTime);
                backupDuty.setIsModifyResult(isModifyResult);

                backupDutyList.add(backupDuty);
            }

            saveAllBackupDutyData(backupDutyList);
        }
    }

    @Override
    public void backupOvertimeData() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll().stream().filter(e -> e.getLeaveDate().after(cal.getTime())).collect(Collectors.toList());

        for (HrEmployeeTrackingPersonInfo personInfo : personInfoList) {

            List<Map<String, String>> overtimeRawList = new ArrayList<>();
            int tryTime = 0;
            while (tryTime < 10) {
                try {
                    overtimeRawList = hrEtWorkCountService.overtimeData(personInfo.getEmpNo(), "" , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    tryTime += 100;
                } catch (Exception e) {
                    tryTime++;
                }
            }
//            overtimeRawList = hrEtWorkCountService.overtimeData(personInfo.getEmpNo(), "" , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            List<HrEtBackupOvertimeData> overtimeDataList = new ArrayList<>();

            for (Map<String, String> overtimeRaw : overtimeRawList) {
                HrEtBackupOvertimeData overtimeData = new HrEtBackupOvertimeData();
                overtimeData.setEmployeeNo(overtimeRaw.get("ID"));
                overtimeData.setEmployeeName(overtimeRaw.get("NAME"));
                overtimeData.setEmployeeDepartName(overtimeRaw.get("DEPT"));
                overtimeData.setWorkDate(new java.sql.Date(sdf.parse(overtimeRaw.get("DUTYDATE")).getTime()));
                overtimeData.setIsOvertime(overtimeRaw.get("ISOVERTIME").equalsIgnoreCase("True") ? true : false);
                if (!StringUtils.isEmpty(overtimeRaw.get("ACTUALOVERTIMECOUNT"))) {
                    overtimeData.setActualOvertimeCount(Double.valueOf(overtimeRaw.get("ACTUALOVERTIMECOUNT")));
                }
                if (!StringUtils.isEmpty(overtimeRaw.get("OVERTIME_S"))) {
                    overtimeData.setBeginOvertime(sdfDatetime.parse(overtimeRaw.get("OVERTIME_S")));
                }
                if (!StringUtils.isEmpty(overtimeRaw.get("OVERTIME_E"))) {
                    overtimeData.setEndOvertime(sdfDatetime.parse(overtimeRaw.get("OVERTIME_E")));
                }

                overtimeDataList.add(overtimeData);
            }

            overtimeDataList = overtimeDataList.stream().filter(e -> e.getIsOvertime() == true).collect(Collectors.toList());

            saveAllBackupOvertimeData(overtimeDataList);
        }

    }

    public int[] saveAllBackupDutyData(List<HrEtBackupDuty> dutyList) {
        if (dutyList.isEmpty()) {
            log.error("### Hr backup duty data empty");
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "MERGE INTO hr_et_backup_duty_data AS target\n" +
                        "USING (SELECT employee_no = ?, employee_name = ?, employee_site = ?, employee_depart_name = ?, employee_class_no = ?, work_date = ?, begin_work = ?, begin_rest = ?, end_rest = ?, end_work = ?, begin_overtime = ?, end_overtime = ?, overtime_count = ?, work_result = ?, ow_time = ?, is_modify_result = ?) AS source\n" +
                        "ON target.employee_no = source.employee_no AND target.work_date = source.work_date\n" +
                        "WHEN MATCHED THEN\n" +
                        "\tUPDATE SET\n" +
                        "\t\ttarget.employee_name = source.employee_name,\n" +
                        "\t\ttarget.employee_site = source.employee_site,\n" +
                        "\t\ttarget.employee_depart_name = source.employee_depart_name,\n" +
                        "\t\ttarget.employee_class_no = source.employee_class_no,\n" +
                        "\t\ttarget.begin_work = source.begin_work,\n" +
                        "\t\ttarget.begin_rest = source.begin_rest,\n" +
                        "\t\ttarget.end_rest = source.end_rest,\n" +
                        "\t\ttarget.end_work = source.end_work,\n" +
                        "\t\ttarget.begin_overtime = source.begin_overtime,\n" +
                        "\t\ttarget.end_overtime = source.end_overtime,\n" +
                        "\t\ttarget.overtime_count = source.overtime_count,\n" +
                        "\t\ttarget.work_result = source.work_result,\n" +
                        "\t\ttarget.ow_time = source.ow_time,\n" +
                        "\t\ttarget.is_modify_result = source.is_modify_result\n" +
                        "WHEN NOT MATCHED THEN\n" +
                        "\tINSERT(employee_no, employee_name, employee_site, employee_depart_name, employee_class_no, work_date, begin_work, begin_rest, end_rest, end_work, begin_overtime, end_overtime, overtime_count, work_result, ow_time, is_modify_result)\n" +
                        "\tVALUES(source.employee_no, source.employee_name, source.employee_site, source.employee_depart_name, source.employee_class_no, source.work_date, source.begin_work, source.begin_rest, source.end_rest, source.end_work, source.begin_overtime, source.end_overtime, source.overtime_count, source.work_result, source.ow_time, source.is_modify_result);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        HrEtBackupDuty backupDuty = dutyList.get(i);
                        preparedStatement.setObject(1, backupDuty.getEmployeeNo());
                        preparedStatement.setObject(2, backupDuty.getEmployeeName());
                        preparedStatement.setObject(3, backupDuty.getEmployeeSite());
                        preparedStatement.setObject(4, backupDuty.getEmployeeDepartName());
                        preparedStatement.setObject(5, backupDuty.getEmployeeClassNo());
                        preparedStatement.setObject(6, backupDuty.getWorkDate());
                        preparedStatement.setObject(7, backupDuty.getBeginWork());
                        preparedStatement.setObject(8, backupDuty.getBeginRest());
                        preparedStatement.setObject(9, backupDuty.getEndRest());
                        preparedStatement.setObject(10, backupDuty.getEndWork());
                        preparedStatement.setObject(11, backupDuty.getBeginOvertime());
                        preparedStatement.setObject(12, backupDuty.getEndOvertime());
                        preparedStatement.setObject(13, backupDuty.getOvertimeCount());
                        preparedStatement.setObject(14, backupDuty.getWorkResult());
                        preparedStatement.setObject(15, backupDuty.getOwTime());
                        preparedStatement.setObject(16, backupDuty.getIsModifyResult());
                    }

                    @Override
                    public int getBatchSize() {
                        return dutyList.size();
                    }
                });
    }

    public int[] saveAllBackupOvertimeData(List<HrEtBackupOvertimeData> overtimeDataList) {
        if (overtimeDataList.isEmpty()) {
            log.error("### Hr backup overtime data empty");
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "MERGE INTO hr_et_backup_overtime_data AS target\n" +
                        "USING (SELECT employee_no = ?, employee_name = ?, employee_depart_name = ?, work_date = ?, is_overtime = ?, actual_overtime_count = ?, begin_overtime = ?, end_overtime = ?) AS source\n" +
                        "ON target.employee_no = source.employee_no AND target.work_date = source.work_date\n" +
                        "WHEN MATCHED THEN\n" +
                        "\tUPDATE SET\n" +
                        "\t\ttarget.employee_name = source.employee_name,\n" +
                        "\t\ttarget.employee_depart_name = source.employee_depart_name,\n" +
                        "\t\ttarget.is_overtime = source.is_overtime,\n" +
                        "\t\ttarget.actual_overtime_count = source.actual_overtime_count,\n" +
                        "\t\ttarget.begin_overtime = source.begin_overtime,\n" +
                        "\t\ttarget.end_overtime = source.end_overtime\n" +
                        "WHEN NOT MATCHED THEN\n" +
                        "\tINSERT(employee_no, employee_name, employee_depart_name, work_date, is_overtime, actual_overtime_count, begin_overtime, end_overtime)\n" +
                        "\tVALUES(source.employee_no, source.employee_name, source.employee_depart_name, source.work_date, source.is_overtime, source.actual_overtime_count, source.begin_overtime, source.end_overtime);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        HrEtBackupOvertimeData backupOvertimeData = overtimeDataList.get(i);
                        preparedStatement.setObject(1, backupOvertimeData.getEmployeeNo());
                        preparedStatement.setObject(2, backupOvertimeData.getEmployeeName());
                        preparedStatement.setObject(3, backupOvertimeData.getEmployeeDepartName());
                        preparedStatement.setObject(4, backupOvertimeData.getWorkDate());
                        preparedStatement.setObject(5, backupOvertimeData.getIsOvertime());
                        preparedStatement.setObject(6, backupOvertimeData.getActualOvertimeCount());
                        preparedStatement.setObject(7, backupOvertimeData.getBeginOvertime());
                        preparedStatement.setObject(8, backupOvertimeData.getEndOvertime());
                    }

                    @Override
                    public int getBatchSize() {
                        return overtimeDataList.size();
                    }
                }
        );
    }

}
