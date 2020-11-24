package com.foxconn.fii.receiver.re.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.b04.model.B04Resource;
import com.foxconn.fii.data.b04.repository.B04ResourceRepository;
import com.foxconn.fii.data.b06ds03.model.B06User;
import com.foxconn.fii.data.b06ds03.repository.B06UserRepository;
import com.foxconn.fii.data.primary.model.entity.ReInfoResource;
import com.foxconn.fii.data.primary.repository.ReInfoResourceRepository;
import com.foxconn.fii.data.primary.repository.TestRepairSerialErrorRepository;
import com.foxconn.fii.data.sfc.repository.SfcCapacityRepairRepository;
import com.foxconn.fii.receiver.re.service.RepairCapacityService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RepairCapacityServiceImpl implements RepairCapacityService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private B06UserRepository b06UserRepository;

    @Autowired
    private ReInfoResourceRepository reInfoResourceRepository;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private B04ResourceRepository b04ResourceRepository;

    @Autowired
    private SfcCapacityRepairRepository sfcCapacityRepairRepository;

    @Override
    public Object getTotalSiSmtRcviCapacity(String factory, String timeSpan) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, String> info = new HashMap<>();
        Set<String> empNoSet = new HashSet<>();
        if (factory.equalsIgnoreCase(Factory.B06)){
            List<B06User> dataEmpNo = b06UserRepository.findByUserLikeAndUserNameIsNotNull("V%");
            empNoSet = dataEmpNo.stream().map(B06User :: getUser).collect(Collectors.toSet());
            info = dataEmpNo.stream().collect(Collectors.toMap(B06User :: getUser, B06User::getUserName));
        }else
        {
//            List<B04Resource> userRe = b04ResourceRepository.findByDem("RE");
            List<ReInfoResource> userRe = reInfoResourceRepository.findByDepartment("RE");
            empNoSet = userRe.stream().map(element -> {return element.getEmpNo();}).collect(Collectors.toSet());
            info = userRe.stream().collect(Collectors.toMap(ReInfoResource :: getEmpNo, ReInfoResource::getNameEse));
        }

        String customer = (factory.equalsIgnoreCase(Factory.S03)) ? "UI" : "";

        List<Map<String, Object>> data = sfcCapacityRepairRepository.countQtyEngineerRepairer(factory,customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        Set<String> finalEmpNoSet = empNoSet;
        data = data.stream().filter(e -> (finalEmpNoSet.contains(e.get("repairer")))).collect(Collectors.toList());
        Map<String, List<Map<String, Object>>> mData = data.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("repairer"))));
//        for (int i = 0; i < mData.size(); i++) {
//            mData.get(mData.keySet().toArray())[i]
//        }
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : mData.entrySet()) {
            String keyd = entry.getKey();
            List<Map<String, Object>> value = entry.getValue();
            Map<String, Map<String, Object>> a = new HashMap<>();
            Integer countSI = 0, countSMT = 0, countRCVI = 0;
            Map<String, Object> b = null;
            for (Map<String, Object> val : value) {
                b = new HashMap<>();
                if (val.get("TEST_SECTION") == null){
                    continue;
                }
                if (val.get("TEST_SECTION").equals("SMT") || val.get("TEST_SECTION").equals("PTH")) {
                    countSMT = countSMT + ((BigDecimal) val.get("QTY")).intValue();
                }
                if (factory.equalsIgnoreCase(Factory.B06)){
                    if (val.get("TEST_SECTION").equals("TEST")) {
//                        if (val.get("STATION_NAME").equals("RCVI")) {
//                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
//                        }else{
                            countSI += ((BigDecimal) val.get("QTY")).intValue();
//                        }
                    }
                }else {
                    if (val.get("TEST_SECTION").equals("SI")) {
                        if (val.get("STATION_NAME").equals("RCVI")) {
                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
                        }else{
                            countSI += ((BigDecimal) val.get("QTY")).intValue();
                        }
                    }
                }

                b.put("si", countSI);
                b.put("smt", countSMT);
                b.put("rcvi", countRCVI);
                b.put("tong", (countSI+countSMT+countRCVI));
                a.put(info.get(keyd), b);
            }
            result.add(a);
        }
        result = result.stream().sorted((e1, e2) -> {
            return ((Integer)((Map)(((Map) e2).values().toArray()[0])).get("tong")).compareTo((Integer)((Map)(((Map) e1).values().toArray()[0])).get("tong"));
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public Map<String, List<Object>> getTotalSiSmtRcviCapacityByShift(String factory, String timeSpan) throws ParseException {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        String shiftDay = "D";
        String shiftNight = "N";
        String customer = (factory.equalsIgnoreCase(Factory.S03)) ? "UI" : "";

        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, String> info = new HashMap<>();
        Set<String> empNoSet = new HashSet<>();
        if (factory.equalsIgnoreCase(Factory.B06)){
            List<B06User> dataEmpNo = b06UserRepository.findByUserLikeAndUserNameIsNotNull("V%");
            empNoSet = dataEmpNo.stream().map(B06User :: getUser).collect(Collectors.toSet());
            info = dataEmpNo.stream().collect(Collectors.toMap(B06User :: getUser, B06User::getUserName));
        }else
        {
            List<ReInfoResource> userRe = reInfoResourceRepository.findByDepartment("RE");
//            List<B04Resource> userRe = b04ResourceRepository.findByDem("RE");
            empNoSet = userRe.stream().map(element -> {return element.getEmpNo();}).collect(Collectors.toSet());
            info = userRe.stream().collect(Collectors.toMap(ReInfoResource :: getEmpNo, ReInfoResource::getNameEse));
        }

        List<Map<String, Object>> dataByShiftDay = sfcCapacityRepairRepository.countQtyEngineerRepairerByShift(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate(), shiftDay);
        List<Map<String, Object>> dataByShiftNight = sfcCapacityRepairRepository.countQtyEngineerRepairerByShift(factory, customer, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate(),shiftNight);
        Set<String> finalEmpNoSet = empNoSet;
        dataByShiftDay = dataByShiftDay.stream().filter(e -> (finalEmpNoSet.contains(e.get("repairer")))).collect(Collectors.toList());
        dataByShiftNight = dataByShiftNight.stream().filter(e -> (finalEmpNoSet.contains(e.get("repairer")))).collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> mDataDay = dataByShiftDay.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("repairer"))));
        Map<String, List<Map<String, Object>>> mDataNight = dataByShiftNight.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("repairer"))));
        List<Object> resultDay = new ArrayList<>();
        List<Object> resultNight = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : mDataDay.entrySet()) {
            String keyd = entry.getKey();
            List<Map<String, Object>> value = entry.getValue();
            Map<String, Map<String, Object>> a = new HashMap<>();
            Integer countSI = 0, countSMT = 0, countRCVI = 0;
            Map<String, Object> b = null;
            for (Map<String, Object> val : value) {
                b = new HashMap<>();
                if (val.get("TEST_SECTION") == null){
                    continue;
                }
                if (val.get("TEST_SECTION").equals("SMT")|| val.get("TEST_SECTION").equals("PTH")) {
                    countSMT = countSMT + ((BigDecimal) val.get("QTY")).intValue();
                }

                if (factory.equalsIgnoreCase(Factory.B06)){
                    if (val.get("TEST_SECTION").equals("TEST")) {
//                        if (val.get("STATION_NAME").equals("RCVI")) {
//                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
//                        }else{
                            countSI += ((BigDecimal) val.get("QTY")).intValue();
//                        }
                    }
                }else {
                    if (val.get("TEST_SECTION").equals("SI")) {
                        if (val.get("STATION_NAME").equals("RCVI")) {
                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
                        }else{
                            countSI += ((BigDecimal) val.get("QTY")).intValue();
                        }
                    }
                }

                b.put("si", countSI);
                b.put("smt", countSMT);
                b.put("rcvi", countRCVI);
                b.put("tong", (countSI+countSMT+countRCVI));
                a.put(info.get(keyd), b);
            }
            resultDay.add(a);
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : mDataNight.entrySet()) {
            String keyd = entry.getKey();
            List<Map<String, Object>> value = entry.getValue();
            Map<String, Map<String, Object>> a = new HashMap<>();
            Integer countSI = 0, countSMT = 0, countRCVI = 0;
            Map<String, Object> b = null;
            for (Map<String, Object> val : value) {
                b = new HashMap<>();
                if (val.get("TEST_SECTION") == null){
                    continue;
                }
                if (val.get("TEST_SECTION").equals("SMT") || val.get("TEST_SECTION").equals("PTH")) {
                    countSMT = countSMT + ((BigDecimal) val.get("QTY")).intValue();
                }
                if (factory.equalsIgnoreCase(Factory.B06)){
                    if (val.get("TEST_SECTION").equals("TEST")) {
//                        if (val.get("STATION_NAME").equals("RCVI")) {
//                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
//                        }else{
                        countSI += ((BigDecimal) val.get("QTY")).intValue();
//                        }
                    }
                }else {
                    if (val.get("TEST_SECTION").equals("SI")) {
                        if (val.get("STATION_NAME").equals("RCVI")) {
                            countRCVI += ((BigDecimal) val.get("QTY")).intValue();
                        }else{
                            countSI += ((BigDecimal) val.get("QTY")).intValue();
                        }
                    }
                }
                b.put("si", countSI);
                b.put("smt", countSMT);
                b.put("rcvi", countRCVI);
                b.put("tong", (countSI+countSMT+countRCVI));
                a.put(info.get(keyd), b);
            }
            resultNight.add(a);
        }
        resultDay = resultDay.stream().sorted((e1, e2) -> {
            return ((Integer)((Map)(((Map) e2).values().toArray()[0])).get("tong")).compareTo((Integer)((Map)(((Map) e1).values().toArray()[0])).get("tong"));
        }).collect(Collectors.toList());
        resultNight = resultNight.stream().sorted((e1, e2) -> {
            return ((Integer)((Map)(((Map) e2).values().toArray()[0])).get("tong")).compareTo((Integer)((Map)(((Map) e1).values().toArray()[0])).get("tong"));
        }).collect(Collectors.toList());
        Map<String, List<Object>> response = new HashMap<>();
        response.put("dataDay", resultDay);
        response.put("dataNight", resultNight);
        return response;
    }

    @Override
    public File listUserExport(String fileName) throws IOException {
      //  Map<String, Object> workData = new HashMap<>();
//         List<B04Resource> test = b04ResourceRepository.findByDem("RE");
        List<ReInfoResource> test = reInfoResourceRepository.findByDepartment("RE");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        HSSFWorkbook workbook = new HSSFWorkbook();

        fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-" + fileName;
        log.info("pause");
        return null;
    }

    @Override
    public Map<String, Object> getDataUserByMonth(String factory) {
        String customer = (factory.equalsIgnoreCase(Factory.S03)) ? "UI" : "";
        String testSection = (factory.equalsIgnoreCase(Factory.B04)) ? "SI" : "TEST";
        Calendar calendarS = Calendar.getInstance();
        calendarS.add(Calendar.MONTH, -3);
        calendarS.set(Calendar.DAY_OF_MONTH, 1);
        calendarS.set(Calendar.MINUTE, 0);
        calendarS.set(Calendar.HOUR_OF_DAY, 0);
        List<Map<String, Object>> dataSI = sfcCapacityRepairRepository.countQtyEngineerRepairerByMonth(factory, customer, testSection, calendarS.getTime(), new Date());
        List<Map<String, Object>> dataSMT = sfcCapacityRepairRepository.countQtyEngineerRepairerByMonthSmt(factory, customer, testSection, calendarS.getTime(), new Date());
//        List<Map<String, String>> dataSI = testRepairSerialErrorRepository.countRepairerQtyByMonthSI(factory);
//        List<Map<String, String>> dataSMT = testRepairSerialErrorRepository.countRepairerQtyByMonthSMTPTH(factory);
        Map<String, String> info = new HashMap<>();
        Set<String> empNoSet = new HashSet<>();
        List<B06User> dataEmpNo = new ArrayList<>();
        List<ReInfoResource> mData = new ArrayList<>();
        if (factory.equalsIgnoreCase(Factory.B06)){
             dataEmpNo = b06UserRepository.findByUserLikeAndUserNameIsNotNull("V%");
            empNoSet = dataEmpNo.stream().map(B06User :: getUser).collect(Collectors.toSet());
            Map<String, B06User> mDataMap = dataEmpNo.stream().collect(Collectors.toMap(e ->(String) e.getUser(), e -> e));
            Set<String> finalEmpNoSet = empNoSet;
            dataSI = dataSI.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());
            dataSMT = dataSMT.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());
            dataSI = dataSI.stream().map(e -> {
                String empNo = (String) e.get("repairer");
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("repairer", e.get("repairer"));
                tmpMap.put("yearmonth", e.get("yearmonth"));
                tmpMap.put("qty", e.get("qty"));
                tmpMap.put("name", mDataMap.get(empNo).getUserName());
                return tmpMap;
            }).collect(Collectors.toList());

            dataSMT = dataSMT.stream().map(e -> {
                String empNo = (String) e.get("repairer");
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("repairer", e.get("repairer"));
                tmpMap.put("yearmonth", e.get("yearmonth"));
                tmpMap.put("qty", e.get("qty"));
                tmpMap.put("name", mDataMap.get(empNo).getUserName());
                return tmpMap;
            }).collect(Collectors.toList());

        }else {
//             mData =  b04ResourceRepository.findByDem("RE");
             mData = reInfoResourceRepository.findByDepartment("RE");
             empNoSet = mData.stream().map(element -> {return element.getEmpNo();}).collect(Collectors.toSet());
            Map<String, ReInfoResource> mDataMap = mData.stream().collect(Collectors.toMap(e -> e.getEmpNo(), e -> e));
            Set<String> finalEmpNoSet = empNoSet;
            dataSI = dataSI.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());
            dataSMT = dataSMT.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());
            dataSI = dataSI.stream().map(e -> {
                String empNo = (String) e.get("repairer");
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("repairer", e.get("repairer"));
                tmpMap.put("yearmonth", e.get("yearmonth"));
                tmpMap.put("qty", e.get("qty"));

                tmpMap.put("name", mDataMap.get(empNo).getNameEse());
                return tmpMap;
            }).collect(Collectors.toList());

            dataSMT = dataSMT.stream().map(e -> {
                String empNo = (String) e.get("repairer");
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("repairer", e.get("repairer"));
                tmpMap.put("yearmonth", e.get("yearmonth"));
                tmpMap.put("qty", e.get("qty"));
                tmpMap.put("name", mDataMap.get(empNo).getNameEse());
                return tmpMap;
            }).collect(Collectors.toList());


        }
     //   List<B04Resource> mData =  b04ResourceRepository.findByDem("RE");
    //    Set<String> empNoSet = mData.stream().map(element -> {return element.getEmployeeNo();}).collect(Collectors.toSet());
        Set<String> finalEmpNoSet = empNoSet;
        dataSI = dataSI.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());
        dataSMT = dataSMT.stream().filter(e -> (finalEmpNoSet.contains((String) e.get("repairer")))).collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Map<String,  List<Map<String, Object>>> resultSI = dataSI.stream().filter(e -> e.get("yearmonth") != null).collect(Collectors.groupingBy( e ->(String) e.get("yearmonth")));
        Map<String,  List<Map<String, Object>>> resultSMTPTH = dataSMT.stream().filter(e -> e.get("yearmonth") != null).collect(Collectors.groupingBy( e ->(String) e.get("yearmonth")));

        resultSI = resultSI.entrySet().stream().filter(e ->{
            try {
                Date date1 = sdf.parse(e.getKey());
                   return date1.compareTo(calendar.getTime()) >= 0;
            } catch (ParseException e1) {
                e1.printStackTrace();
                return true;
            }
        }).sorted((e1, e2) -> {
            try {
                Date date1 = sdf.parse(e1.getKey());
                Date date2 = sdf.parse(e2.getKey());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        resultSMTPTH = resultSMTPTH.entrySet().stream().filter(e ->{
            try {
                Date date1 = sdf.parse(e.getKey());
                return date1.compareTo(calendar.getTime()) >= 0;
            } catch (ParseException e1) {
                e1.printStackTrace();
                return true;
            }
        }).sorted((e1, e2) -> {
            try {
                Date date1 = sdf.parse(e1.getKey());
                Date date2 = sdf.parse(e2.getKey());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (Map.Entry<String,  List<Map<String, Object>>> entry : resultSI.entrySet()) {
            Set<String> tmpCurrentUser = entry.getValue().stream().map(e -> {
                return (String) e.get("repairer");
            }).collect(Collectors.toSet());
            //empNoSet
            Set<String> tmpMissingUser = empNoSet.stream().filter(e ->{
               return !tmpCurrentUser.contains(e);
            }).collect(Collectors.toSet());
            for (String user : tmpMissingUser){
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("yearmonth", entry.getKey());
                tmpMap.put("repairer", user);
                tmpMap.put("qty", "0");
                if (factory.equalsIgnoreCase(Factory.B04)){
                    tmpMap.put("name",  mData.stream().filter(e->e.getEmpNo().equalsIgnoreCase(user)).map(e->e.getNameEse()).findFirst().orElse("null"));
                }else {
                    tmpMap.put("name",  dataEmpNo.stream().filter(e->e.getUser().equalsIgnoreCase(user)).map(e->e.getUserName()).findFirst().orElse("null"));
                }

                resultSI.get(entry.getKey()).add(tmpMap);
            }
        }
        for (Map.Entry<String,  List<Map<String, Object>>> entry : resultSMTPTH.entrySet()) {
            Set<String> tmpCurrentUser = entry.getValue().stream().map(e -> {
                return  (String)e.get("repairer");
            }).collect(Collectors.toSet());
            Set<String> tmpMissingUser = empNoSet.stream().filter(e ->{
                return !tmpCurrentUser.contains(e);
            }).collect(Collectors.toSet());
            for (String user : tmpMissingUser){
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("yearmonth", entry.getKey());
                tmpMap.put("repairer", user);
                tmpMap.put("qty", "0");
                if (factory.equalsIgnoreCase(Factory.B04)){
                    tmpMap.put("name", mData.stream().filter(e->e.getEmpNo().equalsIgnoreCase(user)).map(e->e.getNameEse()).findFirst().orElse("null"));
                }else{
                    tmpMap.put("name", dataEmpNo.stream().filter(e->e.getUser().equalsIgnoreCase(user)).map(e->e.getUserName()).findFirst().orElse("null"));
                }

                resultSMTPTH.get(entry.getKey()).add(tmpMap);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("si", resultSI);
        result.put("smt", resultSMTPTH);
       return result;
    }

    @Override
    public void sendMail(String listUser, String title, String content) throws JsonProcessingException {
        // String listUser = "cpe-vn-fii-sw@mail.foxconn.com,cpe-vn-fii-app@mail.foxconn.com";
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("title",title);
        tmpMap.put("body", content);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("system", "MAIL");
        bodyMap.put("type", "TEXT");
        bodyMap.put("source", "WS");
        bodyMap.put("from", "");
        bodyMap.put("toUser", listUser);
        bodyMap.put("toGroup", null);
        bodyMap.put("message", objectMapper.writeValueAsString(tmpMap));
        String body = objectMapper.writeValueAsString(bodyMap);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.81.120:8888/notify-service/api/notify", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### uploadFile error ", e);
            //return false;
        }
    }

    @Override
    public void leanrTestStream() {
        List<Integer> numbers = Arrays.asList(7,2,5,4,2,1);
        long count = 0;
        for (Integer number: numbers) {
            if (number % 2 == 0){
                count++;
            }
        }
        System.out.println("Bien count dung for thuong %d la:" + count);

        long countS = numbers.stream().filter(e -> ( e % 2 == 0)
        ).count();
        System.out.println("Gia tri countS dung stream:"+ countS);

    }

}
