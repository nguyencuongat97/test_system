package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.nbbtefii.repository.NbbTeFiiEquipmentRepository;
import com.foxconn.fii.data.primary.model.entity.TestStationEquipment;
import com.foxconn.fii.data.primary.model.entity.TestStationMetaNbb;
import com.foxconn.fii.data.primary.repository.TestGroupMetaNbbRepository;
import com.foxconn.fii.data.primary.repository.TestStationEquipmentDailyInfoRepository;
import com.foxconn.fii.data.primary.repository.TestStationEquipmentRepository;
import com.foxconn.fii.data.primary.repository.TestStationMetaNbbRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipKeyPartsRepository;
import com.foxconn.fii.receiver.test.service.NbbTeOeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NbbTeOeeServiceImpl implements NbbTeOeeService {

//    @Autowired
//    private NbbTeEquipmentRepository nbbTeEquipmentRepository;

    @Autowired
    private NbbTeFiiEquipmentRepository nbbTeFiiEquipmentRepository;

    @Autowired
    private TestStationEquipmentRepository testStationEquipmentRepository;

    @Autowired
    private TestStationEquipmentDailyInfoRepository testStationEquipmentDailyInfoRepository;

    @Autowired
    private TestGroupMetaNbbRepository testGroupMetaNbbRepository;

    @Autowired
    private TestStationMetaNbbRepository testStationMetaNbbRepository;

    @Autowired
    private SfcWipKeyPartsRepository sfcWipKeyPartsRepository;

    @Override
    public Object getOverviewData(String customer, String stage, String group, String line, String station, String startDate, String endDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfWorkDate = new SimpleDateFormat("yyyy-MM-dd");

        Date startQueryDate = (StringUtils.isEmpty(startDate)) ? null : new Date(sdf.parse(startDate).getTime());
        Date endQueryDate = (StringUtils.isEmpty(endDate)) ? new Date((new java.util.Date()).getTime()) : new Date(sdf.parse(endDate).getTime());

        Calendar calTmp = Calendar.getInstance();
        if (startQueryDate == null) {
            calTmp.setTime(endQueryDate);
            calTmp.add(Calendar.MONTH, -3);
        } else {
            calTmp.setTime(startQueryDate);
        }

        List<String> workDateList = new ArrayList<>();
        while (calTmp.getTime().compareTo(endQueryDate) <= 0) {
            workDateList.add(sdfWorkDate.format(calTmp.getTime()));
            calTmp.add(Calendar.DAY_OF_YEAR, 1);
        }

        List<TestStationEquipment> equipmentList = testStationEquipmentRepository.findAllByFactory("NBB");

        equipmentList = equipmentList.stream()
                .filter(e -> e.getCustomer().equalsIgnoreCase(customer)
                        && (StringUtils.isEmpty(stage) || e.getStageName().equalsIgnoreCase(stage))
                        && (StringUtils.isEmpty(group) || e.getGroupName().equalsIgnoreCase(group))
                        && (StringUtils.isEmpty(line) || e.getLineName().equalsIgnoreCase(line))
                        && (StringUtils.isEmpty(station) || e.getStationName().equalsIgnoreCase(station)))
                .collect(Collectors.toList());

        Map<String, Map<String, Map<String, Map<String, List<TestStationEquipment>>>>> equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(TestStationEquipment::getStageName, Collectors.groupingBy(TestStationEquipment::getGroupName, Collectors.groupingBy(TestStationEquipment::getLineName, Collectors.groupingBy(TestStationEquipment::getStationName)))));

        Map<String, List<Map<String, Object>>> countData = new HashMap();

//        List<Map<String, Object>> mlbRfCount = nbbTeEquipmentRepository.countSmaMlbRfUsedTime();
//        countData.put("MB_RF", mlbRfCount);
//
//        List<Map<String, Object>> qtCount = nbbTeEquipmentRepository.countFatpQuickTestUsedTime();
//        countData.put("QT", qtCount);
//
//        List<Map<String, Object>> ricoCount = nbbTeEquipmentRepository.countFatpRunInCheckOutTime();
//        countData.put("RI_CO", ricoCount);
//
//        List<Map<String, Object>> rfOtaCount = nbbTeEquipmentRepository.countFatpRfOtaTime();
//        countData.put("RF_OTA", rfOtaCount);
//
//        List<Map<String, Object>> proviCount = nbbTeEquipmentRepository.countPackingProvi();
//        countData.put("PROVI", proviCount);
//
//        List<Map<String, Object>> finalCount = nbbTeEquipmentRepository.countPackingFinal();
//        countData.put("FINAL", finalCount);

        Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> dataResult = new HashMap<>();

        for (Map.Entry<String, Map<String, Map<String, Map<String, List<TestStationEquipment>>>>> entryStage : equipmentMap.entrySet()) {
            dataResult.put(entryStage.getKey(), new HashMap<>());
            for (Map.Entry<String, Map<String, Map<String, List<TestStationEquipment>>>> entryGroup : entryStage.getValue().entrySet()) {
                dataResult.get(entryStage.getKey()).put(entryGroup.getKey(), new HashMap<>());
                for (Map.Entry<String, Map<String, List<TestStationEquipment>>> entryLine : entryGroup.getValue().entrySet()) {
                    dataResult.get(entryStage.getKey()).get(entryGroup.getKey()).put(entryLine.getKey(), new HashMap<>());
                    for (Map.Entry<String, List<TestStationEquipment>> entryStation : entryLine.getValue().entrySet()) {
                        dataResult.get(entryStage.getKey()).get(entryGroup.getKey()).get(entryLine.getKey()).put(entryStation.getKey(), new HashMap<>());
                        Map<String, Map<String, Object>> mapTmp = null;
                        if (countData.containsKey(entryGroup.getKey())) {
                            mapTmp = countData.get(entryGroup.getKey()).stream().filter(e -> ((String) e.get("STATION_ID")).equalsIgnoreCase(entryStation.getKey())).collect(Collectors.toMap(e -> sdfWorkDate.format(e.get("workDate")), e -> e, (eOld, eNew) -> eOld));
                        }
                        for (TestStationEquipment equipmentTmp : entryStation.getValue()) {
                            List<Map<String, Object>> listTmp = new ArrayList<>();
                            for (String workDate : workDateList) {
                                Map<String, Object> emptyMap = new HashMap<>();
                                emptyMap.put("usedTime", 0);
                                emptyMap.put("elapsedTime", 0.0D);
                                emptyMap.put("workDate", workDate);
                                if (mapTmp.containsKey(workDate)) {
                                    listTmp.add(mapTmp.get(workDate));
                                }
                                listTmp.add(emptyMap);
                            }
                            dataResult.get(entryStage.getKey()).get(entryGroup.getKey()).get(entryLine.getKey()).get(entryStation.getKey()).put(equipmentTmp.getEquipment(), listTmp);
                        }
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataResult);

        return result;

    }

//    @Override
//    public Object countData(String factory, String customer) {
//        /* enter equipment meta data by hand
//        int from, int to, String source, String oldLine, String newLine
//        List<TestStationEquipment> equipmentList = testStationEquipmentRepository.findAll().stream().filter(e -> e.getStationName().equalsIgnoreCase(source)).collect(Collectors.toList());
//        List<String> ipList = Arrays.asList("10.239.22.235","10.239.23.152","10.239.22.216","10.239.23.142","10.239.22.202","10.239.23.111","10.239.22.221");
//        List<String> macList = Arrays.asList("10-E7-C6-42-42-BF","10-E7-C6-42-3F-6A","00-E0-4C-36-05-B7","00-E0-4C-36-01-46","00-E0-4C-36-02-D9","C8-D9-D2-01-55-C9","C8-D9-D2-01-55-E0");
//        if (macList.size() != ipList.size() || macList.size() != (to - from + 1)) {
//            return false;
//        }
//        int index = 0;
//        List<TestStationEquipment> equipmentListNew = new ArrayList<>();
//        for (int i = from; i <= to; i++) {
//            for (TestStationEquipment equipmentSource : equipmentList) {
//                TestStationEquipment equipmentTmp = new TestStationEquipment();
//                org.springframework.beans.BeanUtils.copyProperties(equipmentSource, equipmentTmp, "id", "stationIpAddress", "stationMacAddress");
//                String stationNumber = String.valueOf(i);
//                int len = equipmentTmp.getStationName().length();
//                equipmentTmp.setStationName(equipmentTmp.getStationName().substring(0, len - stationNumber.length()).replace(oldLine, newLine) + stationNumber);
//                equipmentTmp.setLineName(newLine);
//                equipmentTmp.setStationIpAddress(ipList.get(index));
//                equipmentTmp.setStationMacAddress(macList.get(index));
//                equipmentListNew.add(equipmentTmp);
//            }
//            index++;
//        }
//        return equipmentListNew;
//        enter equipment meta data by hand */
//
//        List<TestStationEquipment> equipmentList = testStationEquipmentRepository.findAllByFactory(factory);
//        equipmentList = equipmentList.stream().filter(e -> e.getCustomer().equalsIgnoreCase(customer)).collect(Collectors.toList());
//
//        Map<String, Map<String, Map<String, Map<String, List<TestStationEquipment>>>>> equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(TestStationEquipment::getStageName, Collectors.groupingBy(TestStationEquipment::getGroupName, Collectors.groupingBy(TestStationEquipment::getLineName, Collectors.groupingBy(TestStationEquipment::getStationName)))));
//
//        Map<String, List<Map<String, Object>>> countData = new HashMap();
//
//        List<Map<String, Object>> mlbRfCount = nbbTeEquipmentRepository.countSmaMlbRfUsedTime();
//        List<Map<String, Object>> qtCount = nbbTeEquipmentRepository.countFatpQuickTestUsedTime();
//        List<Map<String, Object>> ricoCount = nbbTeEquipmentRepository.countFatpRunInCheckOutTime();
//        List<Map<String, Object>> rfOtaCount = nbbTeEquipmentRepository.countFatpRfOtaTime();
//        List<Map<String, Object>> proviCount = nbbTeEquipmentRepository.countPackingProvi();
//        List<Map<String, Object>> finalCount = nbbTeEquipmentRepository.countPackingFinal();
//
//        countData.put("MB_RF", mlbRfCount);
//        countData.put("QT", qtCount);
//        countData.put("RI_CO", ricoCount);
//        countData.put("RF_OTA", rfOtaCount);
//        countData.put("PROVI", proviCount);
//        countData.put("FINAL", finalCount);
//
//        List<TestStationEquipment> updateList = new ArrayList<>();
//        List<TestStationEquipmentDailyInfo> updateDailyList = new ArrayList<>();
//
//        for (Map.Entry<String, Map<String, Map<String, Map<String, List<TestStationEquipment>>>>> entryStage : equipmentMap.entrySet()) {
//            for (Map.Entry<String, Map<String, Map<String, List<TestStationEquipment>>>> entryGroup : entryStage.getValue().entrySet()) {
//                for (Map.Entry<String, Map<String, List<TestStationEquipment>>> entryLine : entryGroup.getValue().entrySet()) {
//                    for (Map.Entry<String, List<TestStationEquipment>> entryStation : entryLine.getValue().entrySet()) {
////                        Map<String, Object> mapTmp = null;
//                        List<Map<String, Object>> listTmp = null;
//                        if (countData.containsKey(entryGroup.getKey())) {
////                            mapTmp = countData.get(entryGroup.getKey()).stream().filter(e -> ((String) e.get("STATION_ID")).equalsIgnoreCase(entryStation.getKey())).findFirst().orElse(null);
//                            listTmp = countData.get(entryGroup.getKey()).stream().filter(e -> ((String) e.get("STATION_ID")).equalsIgnoreCase(entryStation.getKey())).collect(Collectors.toList());
//                        }
//                        for (TestStationEquipment equipmentTmp : entryStation.getValue()) {
//                            if (!listTmp.isEmpty()) {
//                                int usedTime = listTmp.stream().mapToInt(e -> ((BigDecimal) e.get("usedTime")).intValue()).sum();
//                                double elapsedTime = listTmp.stream().mapToDouble(e -> ((BigDecimal) e.get("elapsedTime")).doubleValue()).sum();
//                                equipmentTmp.setNumberUsed(usedTime);
//                                equipmentTmp.setElapsedTime(elapsedTime);
//                                updateList.add(equipmentTmp);
//                            }
//                            List<TestStationEquipmentDailyInfo> dailyListTmp = testStationEquipmentDailyInfoRepository.findAllByEquipmentId(equipmentTmp.getId());
//                            if (listTmp != null) {
//                                for (Map<String, Object> mapTmp : listTmp) {
//                                    TestStationEquipmentDailyInfo dailyTmp = dailyListTmp.stream().filter(e -> e.getWorkDate().getTime() == ((Timestamp) mapTmp.get("workDate")).getTime()).findFirst().orElse(null);
//                                    if (dailyTmp == null) {
//                                        dailyTmp = new TestStationEquipmentDailyInfo();
//                                        dailyTmp.setEquipmentId(equipmentTmp.getId());
//                                        dailyTmp.setWorkDate(new java.sql.Date(((Timestamp) mapTmp.get("workDate")).getTime()));
//                                    }
//                                    dailyTmp.setNumberUsed(((BigDecimal) mapTmp.get("usedTime")).intValue());
//                                    dailyTmp.setElapsedTime(((BigDecimal) mapTmp.get("elapsedTime")).doubleValue());
//                                    updateDailyList.add(dailyTmp);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        testStationEquipmentRepository.saveAll(updateList);
//        testStationEquipmentDailyInfoRepository.saveAll(updateDailyList);
//
//        return true;
//    }

//    @Override
//    public Object stationStatus(String customer, String stage, String group) {
//
//        //station last connected time
//        TestGroupMetaNbb groupMetaNbb = testGroupMetaNbbRepository.findByFactoryAndCustomerAndStageAndGroupName("NBB", customer, stage, group).stream().findFirst().orElse(null);
//
//        Map<String, Object> dataResult = new HashMap<>();
//        List<String> lineList = new ArrayList<>();
//        Map<String, Integer> statusQty = new HashMap<>();
//        for (stationStatusType status : stationStatusType.values()) {
//            statusQty.put(status.name(), 0);
//        }
//        statusQty.put("total", 0);
//
//        List<Map<String, Object>> stationLastConnectList = new ArrayList<>();
//        if (groupMetaNbb != null && !StringUtils.isEmpty(groupMetaNbb.getTeNbbDbTableName())) {
//            stationLastConnectList.addAll(nbbTeEquipmentRepository.stationLastConnectedTime(groupMetaNbb.getTeNbbDbTableName()));
//        } else {
//            Map<String, Object> result = new HashMap<>();
//
//            result.put("data", Collections.EMPTY_MAP);
//            result.put("lineList", Collections.EMPTY_LIST);
//            result.put("statusQty", statusQty);
//
//            return result;
//        }
//
//        //station list
//        Map<String, List<TestStationMetaNbb>> stationMetaNbbList = testStationMetaNbbRepository.findAllByFactoryAndCustomerAndStageNameAndGroupNameOrderByStationIdAsc("NBB", customer, stage, group).stream().collect(Collectors.groupingBy(e -> e.getLineName()));
//
//        java.util.Date nowTime = new java.util.Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//
//        int totalStation = 0;
//        for (Map.Entry<String, List<TestStationMetaNbb>> entryLine : stationMetaNbbList.entrySet()) {
//            List<Map<String, Object>> lineResult = new ArrayList<>();
//
//            for (TestStationMetaNbb stationMetaNbb : entryLine.getValue()) {
//                Map<String, Object> mapTmp = new HashMap<>();
//                mapTmp.put("stationId", stationMetaNbb.getStationId());
//                mapTmp.put("ipAddress", stationMetaNbb.getIpAddress());
//                mapTmp.put("macAddress", stationMetaNbb.getMacAddress());
//                mapTmp.put("status", stationStatusType.SHUTDOWN.name());
//                mapTmp.put("lastConnectedTime", null);
//                mapTmp.put("group", group);
//                Map<String, Object> lastConnectTmp = stationLastConnectList.stream().filter(e -> ((String) e.get("STATION_ID")).equalsIgnoreCase(stationMetaNbb.getStationId())).findFirst().orElse(Collections.EMPTY_MAP);
//                if (!lastConnectTmp.isEmpty()) {
//                    java.util.Date lastConnectTimeTmp = new Date(((Timestamp) lastConnectTmp.get("END_TIME")).getTime());
//                    mapTmp.put("lastConnectedTime", sdf.format(lastConnectTimeTmp));
//                    if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 0.5 * 60 * 60 * 1000L) {
//                        mapTmp.put("status", stationStatusType.RUNNING.name());
//                    } else if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 2 * 60 * 60 * 1000L) {
//                        mapTmp.put("status", stationStatusType.WAITING.name());
//                    } else if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 12 * 60 * 60 * 1000L) {
//                        mapTmp.put("status", stationStatusType.DOWNTIME.name());
//                    } else {
//                        mapTmp.put("status", stationStatusType.DISCONNECTED.name());
//                    }
//                }
//
//                lineResult.add(mapTmp);
//                statusQty.put((String) mapTmp.get("status"), statusQty.get((String) mapTmp.get("status")) + 1);
//                totalStation++;
//            }
//
//            dataResult.put(entryLine.getKey(), lineResult);
//            lineList.add(entryLine.getKey());
//        }
//        statusQty.put("total", totalStation);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("data", dataResult);
//        result.put("lineList", lineList);
//        result.put("statusQty", statusQty);
//
//        return result;
//    }

    @Override
    public Object stationStatusV2(String customer, String stage, String group) {

        //station last connected time
//        TestGroupMetaNbb groupMetaNbb = testGroupMetaNbbRepository.findByFactoryAndCustomerAndStageAndGroupName("NBB", customer, stage, group).stream().findFirst().orElse(null);

        Map<String, Object> dataResult = new HashMap<>();
        List<String> lineList = new ArrayList<>();
        Map<String, Integer> statusQty = new HashMap<>();
        for (stationStatusType status : stationStatusType.values()) {
            statusQty.put(status.name(), 0);
        }
        statusQty.put("total", 0);

//        List<Map<String, Object>> stationLastConnectList = new ArrayList<>();
//        if (groupMetaNbb != null && !StringUtils.isEmpty(groupMetaNbb.getTeNbbDbTableName())) {
//            stationLastConnectList.addAll(nbbTeEquipmentRepository.stationLastConnectedTime(groupMetaNbb.getTeNbbDbTableName()));
//        } else {
//            Map<String, Object> result = new HashMap<>();
//
//            result.put("data", Collections.EMPTY_MAP);
//            result.put("lineList", Collections.EMPTY_LIST);
//            result.put("statusQty", statusQty);
//
//            return result;
//        }

        //station list
        Map<String, List<TestStationMetaNbb>> stationMetaNbbList = testStationMetaNbbRepository.findAllByFactoryAndCustomerAndStageNameAndGroupNameOrderByStationIdAsc("NBB", customer, stage, group).stream().collect(Collectors.groupingBy(e -> e.getLineName()));

        java.util.Date nowTime = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        int totalStation = 0;
        for (Map.Entry<String, List<TestStationMetaNbb>> entryLine : stationMetaNbbList.entrySet()) {
            List<Map<String, Object>> lineResult = new ArrayList<>();

            for (TestStationMetaNbb stationMetaNbb : entryLine.getValue()) {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("stationId", stationMetaNbb.getStationId());
                mapTmp.put("ipAddress", stationMetaNbb.getIpAddress());
                mapTmp.put("macAddress", stationMetaNbb.getMacAddress());
                mapTmp.put("status", stationStatusType.SHUTDOWN.name());
                mapTmp.put("lastConnectedTime", null);
                mapTmp.put("group", group);
//                Map<String, Object> lastConnectTmp = stationLastConnectList.stream().filter(e -> ((String) e.get("STATION_ID")).equalsIgnoreCase(stationMetaNbb.getStationId())).findFirst().orElse(Collections.EMPTY_MAP);
                Map<String, Object> lastConnectTmp = nbbTeFiiEquipmentRepository.stationLastConnectedTime(stationMetaNbb.getStationId());
                if (!lastConnectTmp.isEmpty()) {
                    java.util.Date lastConnectTimeTmp = new Date(((Timestamp) lastConnectTmp.get("LATEST_ONLINE_TIME")).getTime());
                    mapTmp.put("lastConnectedTime", sdf.format(lastConnectTimeTmp));
                    if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 0.5 * 60 * 60 * 1000L) {
                        mapTmp.put("status", stationStatusType.RUNNING.name());
                    } else if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 2 * 60 * 60 * 1000L) {
                        mapTmp.put("status", stationStatusType.WAITING.name());
                    } else if (nowTime.getTime() - lastConnectTimeTmp.getTime() < 12 * 60 * 60 * 1000L) {
                        mapTmp.put("status", stationStatusType.DOWNTIME.name());
                    } else {
                        mapTmp.put("status", stationStatusType.DISCONNECTED.name());
                    }
                }

                lineResult.add(mapTmp);
                statusQty.put((String) mapTmp.get("status"), statusQty.get((String) mapTmp.get("status")) + 1);
                totalStation++;
            }

            dataResult.put(entryLine.getKey(), lineResult);
            lineList.add(entryLine.getKey());
        }
        statusQty.put("total", totalStation);

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataResult);
        result.put("lineList", lineList);
        result.put("statusQty", statusQty);

        return result;
    }

    public enum stationStatusType {
        RUNNING,
        WAITING,
        FAILURE,
        DOWNTIME,
        DISCONNECTED,
        SHUTDOWN
    }

    @Override
    public void exportDataKeyPart(HttpServletResponse resonse, String keyPart, String keyPartNo, java.util.Date startDate, java.util.Date endDate) throws ParseException, IOException {
        String factory = Factory.NBB;
        List<String> dataList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<String> listKeyPartNo = Arrays.asList(keyPartNo.split(";"));
        if (keyPartNo.equalsIgnoreCase("")) {
            switch (keyPart) {
                case "0DU":
                    dataList.add("G852-01064-01");
                    dataList.add("G852-01237-01");
                    dataList.add("G852-00739-01");
                    dataList.add("G852-00741-01");

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
                    break;
                case "0ET":
                    dataList.add("G804-000370-01");
                default:
                    System.out.println("dataList.size == 0");
            }
        }
        List<Map<String, Object>> data  = new ArrayList<>();
        if (dataList.size() == 0) {

            data = sfcWipKeyPartsRepository.findByKeyPartLikeIndex1(factory, "", keyPart, startDate, endDate, 0, 65530, listKeyPartNo);
        }else{
            data = sfcWipKeyPartsRepository.findByKeyPartLikeIndex1(factory, "", keyPart,startDate,endDate, 0,65530, dataList);

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\vlrrdownloaddir\\";
        String[] arrTitle  = {"EMP_NO"
                ,"SERIAL_NUMBER"
                ,"KEY_PART_NO"
                ,"KEY_PART_SN"
                ,"KP_RELATION"
                ,"GROUP_NAME"
                ,"CARTON_NO"
                ,"WORK_TIME"
                ,"VERSION"
                ,"PART_MODE"
                ,"KP_CODE"
                ,"MO_NUMBER"};
        String fileName = (new java.util.Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle normalStyle = createStyle(workbook);
        HSSFCellStyle abnormalStyle = createStyle(workbook);
        HSSFFont abnormalFont = workbook.createFont();
        abnormalFont.setFontName("Times New Roman");
        abnormalFont.setColor(IndexedColors.RED.getIndex());
        abnormalStyle.setFont(abnormalFont);

        HSSFCellStyle restDayStyle = createStyle(workbook);
        restDayStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        restDayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFSheet sheet = workbook.createSheet("data-"+keyPart);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;
        row = sheet.createRow(rowNum);
        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (Map<String, Object> dt: data) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("EMP_NO"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SERIAL_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("KEY_PART_NO"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("KEY_PART_SN"));
            cellNum++;

            BigDecimal bigDecimal = (BigDecimal) dt.get("KP_RELATION");
            Integer qty = bigDecimal.intValue();

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(qty);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("GROUP_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("CARTON_NO"));
            cellNum++;

            String strDate = simpleDateFormat.format(dt.get("WORK_TIME"));
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) strDate);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("VERSION"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("PART_MODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("KP_CODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MO_NUMBER"));
            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        file.delete();
    }
    private HSSFCellStyle createStyle(HSSFWorkbook workbook) {

        HSSFFont font = workbook.createFont();
        font.setFontName("Arial");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }


}