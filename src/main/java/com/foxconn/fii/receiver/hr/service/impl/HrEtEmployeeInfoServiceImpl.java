package com.foxconn.fii.receiver.hr.service.impl;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingPersonInfo;
import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingPersonWorkingStatus;
import com.foxconn.fii.data.primary.model.entity.HrEtEmployeeCounting;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingPersonInfoRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingPersonWorkingStatusRepository;
import com.foxconn.fii.data.primary.repository.HrEtEmployeeCountingRepository;
import com.foxconn.fii.receiver.hr.service.HrEtEmployeeInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrEtEmployeeInfoServiceImpl implements HrEtEmployeeInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HrEmployeeTrackingPersonWorkingStatusRepository hrEmployeeTrackingPersonWorkingStatusRepository;

    @Autowired
    private HrEmployeeTrackingPersonInfoRepository hrEmployeeTrackingPersonInfoRepository;

    @Autowired
    private HrEtEmployeeCountingRepository hrEtEmployeeCountingRepository;

    @Override
    public EmployeeInfo employeeInfo(String cardId) throws ParserConfigurationException, IOException, SAXException {
        String url = "http://10.132.37.98:8006/personal/ElistQuery.asmx?op=ByUserID";
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <ByUserID xmlns=\"http://tempuri.org/\">\n" +
                "      <EmployeeID>" + cardId + "</EmployeeID>\n" +
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

        EmployeeInfo employeeInfo = new EmployeeInfo();

        try {
            String userId = document.getElementsByTagName("USER_ID").getLength() > 0 ? document.getElementsByTagName("USER_ID").item(0).getTextContent() : null;
            String leaveDateStr = document.getElementsByTagName("LEAVEDAY").getLength() > 0 ? document.getElementsByTagName("LEAVEDAY").item(0).getTextContent() : null;
            String hireDateStr = document.getElementsByTagName("HIREDATE").getLength() > 0 ? document.getElementsByTagName("HIREDATE").item(0).getTextContent() : null;
            String userName = "";
            if (document.getElementsByTagName("USER_NAME").getLength() > 0) {
                userName = document.getElementsByTagName("USER_NAME").item(0).getTextContent();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            if (!StringUtils.isEmpty(hireDateStr)) {
                Calendar hireDate = Calendar.getInstance();
                hireDate.setTime(sdf.parse(hireDateStr));
                employeeInfo.setHireDate(hireDate);
            }

            if (!StringUtils.isEmpty(leaveDateStr)) {
                Calendar leaveDate = Calendar.getInstance();
                leaveDate.setTime(sdf.parse(leaveDateStr));
                employeeInfo.setLeaveDate(leaveDate);
            }

            employeeInfo.setUserId(userId);
            employeeInfo.setUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeInfo;
    }

    @Override
    public Object countWorkingStatusByDay(String startDateStr, String endDateStr, String departName, Set<String> departList) throws ParseException {
        Map<String, Object> result = new HashMap<>();

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        java.sql.Date startDate;
        java.sql.Date endDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        if (StringUtils.isEmpty(startDateStr) || StringUtils.isEmpty(endDateStr)) {
            endDate = new java.sql.Date(calendar.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_YEAR, -6);
            startDate = new java.sql.Date(calendar.getTimeInMillis());
        } else {
            startDate = new java.sql.Date(sdf.parse(startDateStr).getTime());
            endDate = new java.sql.Date(sdf.parse(endDateStr).getTime());
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

        List<Map<String, Object>> workingList = new ArrayList<>();
        List<Map<String, Object>> dayWorkingList = new ArrayList<>();
        List<Map<String, Object>> nightWorkingList = new ArrayList<>();
        List<Map<String, Object>> missingList = new ArrayList<>();
        List<Map<String, Object>> alertList = new ArrayList<>();


        List<HrEtEmployeeCounting> countingList = hrEtEmployeeCountingRepository.findByWorkDateBetween(startDate, endDate);
        if (departList.isEmpty()) {
            countingList = countingList.stream().filter(e -> e.getDepartName().startsWith(departName)).collect(Collectors.toList());
        } else {
            countingList = countingList.stream().filter(e -> departList.contains(e.getDepartName())).collect(Collectors.toList());
        }

        while (!startCal.after(endCal)) {
            Date tmpDate = new Date(startCal.getTimeInMillis());
            List<HrEtEmployeeCounting> tmpCountingList = countingList.stream().filter(e -> e.getWorkDate().equals(tmpDate)).collect(Collectors.toList());

            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("workDate", sdf.format(startCal.getTime()));
            tmpMap.put("qty", tmpCountingList.stream().filter(e -> e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("FULL")).mapToInt(e -> e.getCountQty()).sum());

            workingList.add(tmpMap);

            tmpMap = new HashMap<>();
            tmpMap.put("workDate", sdf.format(startCal.getTime()));
            tmpMap.put("qty", tmpCountingList.stream().filter(e -> e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("DAY")).mapToInt(e -> e.getCountQty()).sum());

            dayWorkingList.add(tmpMap);

            tmpMap = new HashMap<>();
            tmpMap.put("workDate", sdf.format(startCal.getTime()));
            tmpMap.put("qty", tmpCountingList.stream().filter(e -> e.getCountType().equalsIgnoreCase("WORKING") && e.getWorkShift().equalsIgnoreCase("NIGHT")).mapToInt(e -> e.getCountQty()).sum());

            nightWorkingList.add(tmpMap);

            tmpMap = new HashMap<>();
            tmpMap.put("workDate", sdf.format(startCal.getTime()));
            tmpMap.put("qty", tmpCountingList.stream().filter(e -> e.getCountType().equalsIgnoreCase("ALERT") && e.getWorkShift().equalsIgnoreCase("FULL")).mapToInt(e -> e.getCountQty()).sum());

            alertList.add(tmpMap);

            tmpMap = new HashMap<>();
            tmpMap.put("workDate", sdf.format(startCal.getTime()));
            tmpMap.put("qty", tmpCountingList.stream().filter(e -> e.getCountType().equalsIgnoreCase("MISSING") && e.getWorkShift().equalsIgnoreCase("FULL")).mapToInt(e -> e.getCountQty()).sum());

            missingList.add(tmpMap);

            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        result.put("workingList", workingList);
        result.put("dayWorkingList", dayWorkingList);
        result.put("nightWorkingList", nightWorkingList);
        result.put("missingList", missingList);
        result.put("alertList", alertList);

        return result;
    }

    @Override
    public Object listEmployee(String dateStr, String shift, String departName, Set<String> departList, String status) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Date date = new Date(sdf.parse(dateStr).getTime());

        List<HrEmployeeTrackingPersonWorkingStatus> workingStatusList = hrEmployeeTrackingPersonWorkingStatusRepository.findByWorkDate(date).stream().filter(e -> e.getWorkStatus().equalsIgnoreCase(status)).collect(Collectors.toList());

        if (!departList.isEmpty()) {
            workingStatusList = workingStatusList.stream().filter(e -> departList.contains(e.getDepartName())).collect(Collectors.toList());
        }
        if (!StringUtils.isEmpty(shift)) {
            workingStatusList = workingStatusList.stream().filter(e -> e.getWorkShift().equalsIgnoreCase(shift)).collect(Collectors.toList());
        }

        Map<String, HrEmployeeTrackingPersonInfo> personInfoMap = hrEmployeeTrackingPersonInfoRepository.findAll().stream().collect(Collectors.toMap(e -> e.getEmpNo(), e -> e));

        return workingStatusList.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            if (personInfoMap.containsKey(e.getEmpNo())) {
                map.put("name", personInfoMap.get(e.getEmpNo()).getName());
            } else {
                map.put("name", "");
            }
            map.put("empNo", e.getEmpNo());
            map.put("officeUnitName", e.getDepartName());
            return map;
        }).collect(Collectors.toList());
    }

//    @Override
//    public Object listMissing(String dateStr, String departName, Set<String> departList) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//        Date date = new Date(sdf.parse(dateStr).getTime());
//
//        List<HrEmployeeTrackingPersonWorkingStatus> workingStatusList = hrEmployeeTrackingPersonWorkingStatusRepository.findByWorkDate(date);
//        if (departList.isEmpty()) {
//            workingStatusList = workingStatusList.stream().filter(e -> e.getDepartName().startsWith(departName)).collect(Collectors.toList());
//        } else {
//            workingStatusList = workingStatusList.stream().filter(e -> departList.contains(e.getDepartName())).collect(Collectors.toList());
//        }
//
//        Set<String> empNoSet = workingStatusList.stream().map(e -> e.getEmpNo()).collect(Collectors.toSet());
//
//        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll();
//        if (departList.isEmpty()) {
//            personInfoList = personInfoList.stream().filter(e -> e.getOfficeName().startsWith(departName)).collect(Collectors.toList());
//        } else {
//            personInfoList = personInfoList.stream().filter(e -> departList.contains(e.getOfficeName())).collect(Collectors.toList());
//        }
//
//        return personInfoList.stream().filter(e -> !empNoSet.contains(e.getEmpNo()) && e.getLeaveDate().after(date)).map(e -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", e.getName());
//            map.put("empNo", e.getEmpNo());
//            map.put("officeUnitName", e.getOfficeName());
//            map.put("officeUnitCode", e.getOfficeCode());
//            return map;
//        }).collect(Collectors.toList());
//    }
//
//    @Override
//    public Object listAlert(String dateStr, String departName, Set<String> departList) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//        Date endDate = new Date(sdf.parse(dateStr).getTime());
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(endDate.getTime());
//        calendar.add(Calendar.DAY_OF_YEAR, -4);
//
//        Date startDate = new Date(calendar.getTimeInMillis());
//
//        List<HrEmployeeTrackingPersonWorkingStatus> workingStatusList = hrEmployeeTrackingPersonWorkingStatusRepository.findByWorkDateBetween(startDate, endDate);
//
//        if (departList.isEmpty()) {
//            workingStatusList = workingStatusList.stream().filter(e -> e.getDepartName().startsWith(departName)).collect(Collectors.toList());
//        } else {
//            workingStatusList = workingStatusList.stream().filter(e -> departList.contains(e.getDepartName())).collect(Collectors.toList());
//        }
//
//        Set<String> empNoSet = new HashSet<>();
//        while (startDate.compareTo(endDate) <= 0) {
//            empNoSet.addAll(workingStatusList.stream().filter(e -> e.getWorkDate().compareTo(startDate) == 0).map(e -> e.getEmpNo()).collect(Collectors.toSet()));
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//            startDate.setTime(calendar.getTimeInMillis());
//        }
//
//        List<HrEmployeeTrackingPersonInfo> personInfoList = hrEmployeeTrackingPersonInfoRepository.findAll();
//        if (departList.isEmpty()) {
//            personInfoList = personInfoList.stream().filter(e -> e.getOfficeName().startsWith(departName)).collect(Collectors.toList());
//        } else {
//            personInfoList = personInfoList.stream().filter(e -> departList.contains(e.getOfficeName())).collect(Collectors.toList());
//        }
//
//        return personInfoList.stream().filter(e -> !empNoSet.contains(e.getEmpNo()) && e.getLeaveDate().after(endDate)).map(e -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", e.getName());
//            map.put("empNo", e.getEmpNo());
//            map.put("officeUnitName", e.getOfficeName());
//            map.put("officeUnitCode", e.getOfficeCode());
//            return map;
//        }).collect(Collectors.toList());
//    }

    @Data
    public static class EmployeeInfo {

        private String userId;

        private String userName;

        private Calendar hireDate;

        private Calendar leaveDate;
    }
}
