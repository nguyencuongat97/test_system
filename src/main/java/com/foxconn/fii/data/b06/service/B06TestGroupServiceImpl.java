package com.foxconn.fii.data.b06.service;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestUphRealtime;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.data.primary.repository.TestUphRealtimeRepository;
import com.foxconn.fii.data.primary.repository.TestUphTargetRepository;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class B06TestGroupServiceImpl implements B06TestGroupService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestUphTargetRepository testUphTargetRepository;

    @Autowired
    private TestUphRealtimeRepository testUphRealtimeRepository;

    @Override
    public Map<String, Map<String, Integer>> getUPH(String modelName, String workDate, ShiftType shiftType) {
        TimeSpan timeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.set(Calendar.MINUTE, 30);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        Date start = calendar.getTime();

        List<TestStation> stationList = new ArrayList<>();
        while (start.before(timeSpan.getEndDate())) {
            calendar.setTime(start);
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            stationList.addAll(findStationByModelNameAndWorkDateBetween(Arrays.asList(modelName), start, calendar.getTime()));
            start = calendar.getTime();
        }

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
        Map<String, Map<String, Integer>> result = stationList.stream()
                .collect(Collectors.groupingBy(TestStation::getGroupName,
                        Collectors.toMap(
                                station -> DATE_FORMAT.format(station.getStartDate()) + " - " + DATE_FORMAT.format(station.getEndDate()),
                                TestStation::getPass,
                                (g1, g2) -> g1+g2,
                                TreeMap::new)));

        int index = 24;
        if (shiftType != null) {
            index = 12;
        }
        if (timeSpan.isNow()) {
            TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
            index = hourlyTimeSpan.getShiftIndex() + 1;
            if (shiftType == null && hourlyTimeSpan.getShiftType() == ShiftType.NIGHT) {
                index += 12;
            }
        }

        Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomerAndModelName("B06", "", modelName)
                .stream().collect(Collectors.toMap(
                        TestUphTarget::getGroupName,
                        TestUphTarget::getUph,
                        (u1, u2) -> u1 + u2));

        for (Map.Entry<String, Map<String, Integer>> entry : result.entrySet()) {
            int accumulate = entry.getValue().values().stream().mapToInt(value -> value).sum();
            entry.getValue().put("accumulate", accumulate);
            entry.getValue().put("uph-average", accumulate / index);
            entry.getValue().put("uph-target", uphTargetMap.getOrDefault(entry.getKey(), 0));
        }

        return result;
    }

    private List<TestStation> findStationByModelNameAndWorkDateBetween(List<String> modelList, Date start, Date end) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HHmm");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Start_Date", DATE_FORMAT.format(start));
        map.add("End_Date", DATE_FORMAT.format(end));
        map.add("Start_hh", HOUR_FORMAT.format(start));
        map.add("End_hh", HOUR_FORMAT.format(end));
        map.add("model_name", String.join(",", modelList));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.69.100/Data_ATE_A02/Servicepostdata.asmx/Export_Data_Ate", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### syncDataFromB06 error ", e);
            return Collections.emptyList();
        }

        List<TestStation> stationList = parseStationFromXmlB06(responseEntity.getBody(), start, end);

        return stationList;
    }

    private List<TestStation> parseStationFromXmlB06(String xml, Date start, Date end) {
        if (StringUtils.isEmpty(xml)) {
            return Collections.emptyList();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Mytable");

            Map<String, TestStation> stationMap = new HashMap<>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String groupName = eElement.getElementsByTagName("GROUP_NAME").item(0).getTextContent();
                    String stationName = eElement.getElementsByTagName("STATION_NAME").item(0).getTextContent();
                    String lineName = eElement.getElementsByTagName("LINE_NAME").item(0).getTextContent();

                    String factoryName = "B06";

                    String key = factoryName + "_" + modelName + "_" + groupName + "_" + stationName;
                    TestStation station = stationMap.getOrDefault(key, new TestStation());

                    station.setFactory(factoryName);
                    station.setStartDate(start);
                    station.setEndDate(end);

                    station.setModelName(modelName);
                    station.setGroupName(groupName);
                    station.setStationName(stationName);
                    station.setLineName(lineName);
                    station.setWip(station.getWip() + Integer.parseInt(eElement.getElementsByTagName("WIP_QTY").item(0).getTextContent()));
                    station.setFirstFail(station.getFirstFail() + Integer.parseInt(eElement.getElementsByTagName("FIRST_FAIL_QTY").item(0).getTextContent()));
                    station.setSecondFail(station.getSecondFail() + Integer.parseInt(eElement.getElementsByTagName("REPAIR_QTY").item(0).getTextContent()));
                    station.setPass(station.getPass() + Integer.parseInt(eElement.getElementsByTagName("PASS_QTY").item(0).getTextContent()));
                    station.setFail(station.getFail() + Integer.parseInt(eElement.getElementsByTagName("FAIL_QTY").item(0).getTextContent()));

                    stationMap.put(key, station);
                }
            }

            return new ArrayList<>(stationMap.values());
        } catch (Exception e) {
            log.error("### parseStationFromXml error", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Object getAllUPH(List<String> modelList, String workDate, ShiftType shiftType) {
        TimeSpan timeSpan = TimeSpan.from(workDate + " " + (StringUtils.isEmpty(shiftType) ? "FULL" : shiftType.toString()), TimeSpan.Type.DAILY);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.set(Calendar.MINUTE, 30);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        Date start = calendar.getTime();

        List<TestStation> stationList = new ArrayList<>();
        while (start.before(timeSpan.getEndDate())) {
            calendar.setTime(start);
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            stationList.addAll(findStationByModelNameAndWorkDateBetween(modelList, start, calendar.getTime()));
            start = calendar.getTime();
        }

        List<TestUphRealtime> realtimeList = testUphRealtimeRepository.findByFactoryOrderByWorkSectionAsc("B06");

        Map<String, Map<String, Map<String, Object>>> totalResult = new HashMap<>();
        for (String modelName : modelList) {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
            Map<String, Map<String, Object>> result = stationList.stream()
                    .filter(e -> e.getModelName().equalsIgnoreCase(modelName))
                    .collect(Collectors.groupingBy(TestStation::getGroupName,
                            Collectors.toMap(
                                    station -> DATE_FORMAT.format(station.getStartDate()) + " - " + DATE_FORMAT.format(station.getEndDate()),
                                    TestStation::getPass,
                                    (g1, g2) -> (int)g1+(int)g2,
                                    TreeMap::new)));

            int index = 24;
            if (shiftType != null) {
                index = 12;
            }
            if (timeSpan.isNow()) {
                TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
                index = hourlyTimeSpan.getShiftIndex() + 1;
                if (shiftType == null && hourlyTimeSpan.getShiftType() == ShiftType.NIGHT) {
                    index += 12;
                }
            }

            Map<String, Integer> uphTargetMap = testUphTargetRepository.findByFactoryAndCustomerAndModelName("B06", "", modelName)
                    .stream().collect(Collectors.toMap(
                            TestUphTarget::getGroupName,
                            TestUphTarget::getUph,
                            (u1, u2) -> u1 + u2));

            for (Map.Entry<String, Map<String, Object>> entry : result.entrySet()) {
                int accumulate = entry.getValue().values().stream().mapToInt(value -> (int)value).sum();
                int uphTarget = uphTargetMap.getOrDefault(entry.getKey(), 0);
                for (Map.Entry<String, Object> subEntry : entry.getValue().entrySet()) {
                    Map<String, Object> map = new HashMap<>();
                    int pass = (int) subEntry.getValue();
                    Integer workSection = Integer.valueOf(subEntry.getKey().substring(0, 2)) + 1;
                    Double realTime = 1D;
                    OptionalDouble realTimeOptional = realtimeList.stream().filter(e -> e.getWorkSection().equals(workSection)).mapToDouble(e -> e.getRealTime()).findFirst();
                    if (realTimeOptional.isPresent()) {
                        realTime = realTimeOptional.getAsDouble();
                    }

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
                entry.getValue().put("uph-target", uphTargetMap.getOrDefault(entry.getKey(), 0));
            }

            totalResult.put(modelName, result);
        }

        return totalResult;
    }
}
