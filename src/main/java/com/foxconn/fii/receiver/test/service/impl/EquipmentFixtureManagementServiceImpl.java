package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.b06te.model.*;
import com.foxconn.fii.data.b06te.repository.*;
import com.foxconn.fii.data.primary.model.entity.TestOeeSpec;
import com.foxconn.fii.data.primary.repository.TestOeeSpecRepository;
import com.foxconn.fii.receiver.test.service.EquipmentFixtureManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EquipmentFixtureManagementServiceImpl implements EquipmentFixtureManagementService {

    @Autowired
    private B06TeEquipmentFixtureRepository b06TeEquipmentFixtureRepository;

    @Autowired
    private TeEquipmentRepository teEquipmentRepository;

    @Autowired
    private TeFixtureRepository teFixtureRepository;

    @Autowired
    private TestOeeSpecRepository testOeeSpecRepository;

    @Autowired
    private TeTestWorkSectionRepository teTestWorkSectionRepository;

    @Autowired
    private TeEquipmentFixtureLocationRepository teEquipmentFixtureLocationRepository;

    @Autowired
    private TeEFOwnerTypeRepository teEFOwnerTypeRepository;

    @Autowired
    private TeEquipmentCalibrationHistoryRepository teEquipmentCalibrationHistoryRepository;

    @Autowired
    private TeFixtureCalibrationHistoryRepository teFixtureCalibrationHistoryRepository;

    @Autowired
    private TeEquipmentBorrowHistoryRepository teEquipmentBorrowHistoryRepository;

    @Autowired
    private TeFixtureBorrowHistoryRepository teFixtureBorrowHistoryRepository;

    @Autowired
    private TeEquipmentRentHistoryRepository teEquipmentRentHistoryRepository;

    @Autowired
    private TeFixtureRentHistoryRepository teFixtureRentHistoryRepository;

    @Autowired
    private TeTestDataRepository teTestDataRepository;

    @Override
    public Map<String, Object> b06OeeAteStatus() {

        List<TestOeeSpec> listTestOeeSpec = testOeeSpecRepository.findAll();
        Double ateRunningLimit = listTestOeeSpec.stream().filter(e -> e.getItem().equalsIgnoreCase("ate_running_limit")).findFirst().orElse(new TestOeeSpec()).doubleValue();
        Double ateWaitingLimit = listTestOeeSpec.stream().filter(e -> e.getItem().equalsIgnoreCase("ate_waiting_limit")).findFirst().orElse(new TestOeeSpec()).doubleValue();
        Double ateDowntimeLimit = listTestOeeSpec.stream().filter(e -> e.getItem().equalsIgnoreCase("ate_downtime_limit")).findFirst().orElse(new TestOeeSpec()).doubleValue();

        Map<String, Object> dataResult = new HashMap<>();
        Map<String, Integer> statusQtyResult = new HashMap<>();
        for (AteStatusType status : AteStatusType.values()) {
            statusQtyResult.put(status.name(), 0);
        }
        statusQtyResult.put("total", 0);

        List<Map<String, Object>> ateMetaData = b06TeEquipmentFixtureRepository.ateMetaDataByStation();

        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        for (Map<String, Object> ate : ateMetaData) {
            Map<String, Object> mapTmp = new HashMap<>();
            mapTmp.put("ate", ate.get("ATE"));
            mapTmp.put("ipAddress", ate.get("IP_ADDRESS"));
            mapTmp.put("macAddress", ate.get("MAC_ADDRESS"));
            mapTmp.put("lastOnlineTime", sdf.format(ate.get("LATEST_ONLINE_TIME")));
            mapTmp.put("station", ate.get("STATION"));
            mapTmp.put("line", ate.get("LINE"));
            mapTmp.put("status", AteStatusType.SHUTDOWN.name());

            if (nowTime.getTime() - ((Date) ate.get("LATEST_ONLINE_TIME")).getTime() < ateRunningLimit * 60 * 60 * 1000L) {
                mapTmp.put("status", AteStatusType.RUNNING.name());
            } else if (nowTime.getTime() - ((Date) ate.get("LATEST_ONLINE_TIME")).getTime() < ateWaitingLimit * 60 * 60 * 1000L) {
                mapTmp.put("status", AteStatusType.WAITING.name());
            } else if (nowTime.getTime() - ((Date) ate.get("LATEST_ONLINE_TIME")).getTime() < ateDowntimeLimit * 60 * 60 * 1000L) {
                mapTmp.put("status", AteStatusType.DOWNTIME.name());
            } else {
                mapTmp.put("status", AteStatusType.DISCONNECTED.name());
            }

            statusQtyResult.put((String) mapTmp.get("status"), statusQtyResult.get(mapTmp.get("status")) + 1);

            String tmpStation = StringUtils.isEmpty(ate.get("STATION")) ? "UNKNOWN" : (String) ate.get("STATION");
            if (!dataResult.containsKey(tmpStation)) {
                dataResult.put(tmpStation, new HashMap<>());
                ((Map) dataResult.get(tmpStation)).put("data", new HashMap<>());
                ((Map) dataResult.get(tmpStation)).put("statusQty", new HashMap<>());
                for (AteStatusType status : AteStatusType.values()) {
                    ((Map) ((Map) dataResult.get(tmpStation)).get("statusQty")).put(status.name(), 0);
                }
                ((Map) ((Map) dataResult.get(tmpStation)).get("statusQty")).put("total", 0);
            }
            String tmpLine = StringUtils.isEmpty(ate.get("LINE")) ? "UNKNOWN" : (String) ate.get("LINE");
            if (!((Map) ((Map) dataResult.get(tmpStation)).get("data")).containsKey(tmpLine)) {
                ((Map) ((Map) dataResult.get(tmpStation)).get("data")).put(tmpLine, new ArrayList<>());
            }

            ((List) ((Map) ((Map) dataResult.get(tmpStation)).get("data")).get(tmpLine)).add(mapTmp);
            ((Map)(((Map) dataResult.get(tmpStation))).get("statusQty")).put(mapTmp.get("status"), (int) ((Map)(((Map) dataResult.get(tmpStation))).get("statusQty")).get(mapTmp.get("status")) + 1);
        }

        statusQtyResult.put("total", ateMetaData.size());

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataResult);
        result.put("statusQty", statusQtyResult);

        return result;
    }

    // E%F overall API
    @Override
    public Map<String, Map<String, Object>> b06EquipmentStatusSumaryV2(String ownerType) {

        Date nowDate = new Date();
        Double efUsingLimit = testOeeSpecRepository.findTop1ByItem("ef_using_limit").doubleValue();

        List<Map<String, Object>> equipmentList = teEquipmentRepository.jpqlEquipmentWithLocation();

        int total = equipmentList.size();
        int using = 0;
        int offline = 0;
        int calibrating = 0;
        int repairing = 0;

        for (Map<String, Object> equipmentData : equipmentList) {
            TeEquipment equipment = (TeEquipment) equipmentData.get("equipment");
            TeEquipmentFixtureLocation location = (TeEquipmentFixtureLocation) equipmentData.get("location");

            if (location == null || location.getLocationName().equalsIgnoreCase("line")) {
                if (equipment.getLatestOnlineTime() != null) {
                    if (nowDate.getTime() - equipment.getLatestOnlineTime().getTime() > efUsingLimit * 60 * 60 * 1000L) {
                        offline++;
                    } else {
                        using++;
                    }
                } else {
                    offline++;
                }
            } else if (location.getLocationName().equalsIgnoreCase("calibrating")) {
                calibrating++;
            } else if (location.getLocationName().equalsIgnoreCase("repairing")) {
                repairing++;
            } else {
                total--;
            }
        }

        Map<String, Map<String, Object>> result = new HashMap<>();

        result.put("using", new HashMap<>());
        result.get("using").put("qty", using);
        result.get("using").put("total", total);
        result.get("using").put("rate", 100D * using / total);

        result.put("offline", new HashMap<>());
        result.get("offline").put("qty", offline);
        result.get("offline").put("total", total);
        result.get("offline").put("rate", 100D * offline / total);

        result.put("calibrating", new HashMap<>());
        result.get("calibrating").put("qty", calibrating);
        result.get("calibrating").put("total", total);
        result.get("calibrating").put("rate", 100D * calibrating / total);

        result.put("repairing", new HashMap<>());
        result.get("repairing").put("qty", repairing);
        result.get("repairing").put("total", total);
        result.get("repairing").put("rate", 100D * repairing / total);

        return result;
    }

    @Override
    public Map<String, Map<String, Object>> b06FixtureStatusSumaryV2(String ownerType) {

        Date nowDate = new Date();
        Double efUsingLimit = testOeeSpecRepository.findTop1ByItem("ef_using_limit").doubleValue();

        List<Map<String, Object>> equipmentList = teFixtureRepository.jpqlFixtureWithLocation();

        int total = equipmentList.size();
        int using = 0;
        int offline = 0;
        int calibrating = 0;
        int repairing = 0;

        for (Map<String, Object> equipmentData : equipmentList) {
            TeFixture fixture = (TeFixture) equipmentData.get("fixture");
            TeEquipmentFixtureLocation location = (TeEquipmentFixtureLocation) equipmentData.get("location");

            if (location == null || location.getLocationName().equalsIgnoreCase("line")) {
                if (fixture.getLatestOnlineTime() != null) {
                    if (nowDate.getTime() - fixture.getLatestOnlineTime().getTime() > efUsingLimit * 60 * 60 * 1000L) {
                        offline++;
                    } else {
                        using++;
                    }
                } else {
                    offline++;
                }
            } else if (location.getLocationName().equalsIgnoreCase("calibrating")) {
                calibrating++;
            } else if (location.getLocationName().equalsIgnoreCase("repairing")) {
                repairing++;
            } else {
                total--;
            }
        }

        Map<String, Map<String, Object>> result = new HashMap<>();

        result.put("using", new HashMap<>());
        result.get("using").put("qty", using);
        result.get("using").put("total", total);
        result.get("using").put("rate", 100D * using / total);

        result.put("offline", new HashMap<>());
        result.get("offline").put("qty", offline);
        result.get("offline").put("total", total);
        result.get("offline").put("rate", 100D * offline / total);

        result.put("calibrating", new HashMap<>());
        result.get("calibrating").put("qty", calibrating);
        result.get("calibrating").put("total", total);
        result.get("calibrating").put("rate", 100D * calibrating / total);

        result.put("repairing", new HashMap<>());
        result.get("repairing").put("qty", repairing);
        result.get("repairing").put("total", total);
        result.get("repairing").put("rate", 100D * repairing / total);

        return result;
    }

    @Override
    public Map<String, Map<String, Object>> b06EquipmentPerformanceTrend(String timeSpanStr) {
        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }
        timeSpan.getEndDate().setTime(timeSpan.getEndDate().getTime() + 60 * 60 * 1000L);

        List<Map<String, Object>> listEquipmentUsingCount = b06TeEquipmentFixtureRepository.equipmentUseTimeCount(timeSpan.getStartDate(), timeSpan.getEndDate());

        Map<String, Map<String, Object>> result = new HashMap<>();

        if (!listEquipmentUsingCount.isEmpty()) {
            Map<String, Object> highestEquipment = new HashMap<>();
            highestEquipment.put("meta", listEquipmentUsingCount.get(0));
            highestEquipment.put("data", b06TeEquipmentFixtureRepository.equipmentUsingTrend(((BigDecimal) listEquipmentUsingCount.get(0).get("ID")).longValue(), timeSpan.getStartDate(), timeSpan.getEndDate()));
            result.put("highestEquipment", highestEquipment);

            Map<String, Object> lowestEquipment = new HashMap<>();
            lowestEquipment.put("meta", listEquipmentUsingCount.get(listEquipmentUsingCount.size() -1));
            lowestEquipment.put("data", b06TeEquipmentFixtureRepository.equipmentUsingTrend(((BigDecimal) listEquipmentUsingCount.get(listEquipmentUsingCount.size() -1).get("ID")).longValue(), timeSpan.getStartDate(), timeSpan.getEndDate()));
            result.put("lowestEquipment", lowestEquipment);
        }

        return result;
    }

    @Override
    public Map<String, Map<String, Object>> b06FixturePerformanceTrend(String timeSpanStr) {
        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }
        timeSpan.getEndDate().setTime(timeSpan.getEndDate().getTime() + 60 * 60 * 1000L);

        List<Map<String, Object>> listFixtureUsingCount = b06TeEquipmentFixtureRepository.fixtureUseTimeCount(timeSpan.getStartDate(), timeSpan.getEndDate());

        Map<String, Map<String, Object>> result = new HashMap<>();

        if (!listFixtureUsingCount.isEmpty()) {
            Map<String, Object> highestFixture = new HashMap<>();
            highestFixture.put("meta", listFixtureUsingCount.get(0));
            highestFixture.put("data", b06TeEquipmentFixtureRepository.fixtureUsingTrend((String) listFixtureUsingCount.get(0).get("FIXTURE_CODE"), timeSpan.getStartDate(), timeSpan.getEndDate()));
            result.put("highestFixture", highestFixture);

            Map<String, Object> lowestFixture = new HashMap<>();
            lowestFixture.put("meta", listFixtureUsingCount.get(listFixtureUsingCount.size() - 1));
            lowestFixture.put("data", b06TeEquipmentFixtureRepository.fixtureUsingTrend((String) listFixtureUsingCount.get(listFixtureUsingCount.size() - 1).get("FIXTURE_CODE"), timeSpan.getStartDate(), timeSpan.getEndDate()));
            result.put("lowestFixture", lowestFixture);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> b06EquipmentStatusOnlineByType(String timeSpanStr) {

        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream().filter(e -> e.getLocationName().equalsIgnoreCase("line")).map(e -> e.getId()).collect(Collectors.toList());

        List<Map<String, Object>> onlineDataList = b06TeEquipmentFixtureRepository.equipmentOnlineBetween(timeSpan.getStartDate(), timeSpan.getEndDate());

        Map<String, Map<String, Integer>> typeQty = new HashMap<>();

        for (Map<String, Object> onlineData : onlineDataList) {

            Long equipmentId = onlineData.get("EQUIPMENT_ID") != null ? ((BigDecimal) onlineData.get("EQUIPMENT_ID")).longValue() : null;

            String type = StringUtils.isEmpty(onlineData.get("TYPE")) ? "null" : (String) onlineData.get("TYPE");

            boolean onlineFlag = true;

            if (equipmentId == null) {
                onlineFlag = false;
            }

            Long locationId = onlineData.get("LOCATION_ID") != null ? ((BigDecimal) onlineData.get("LOCATION_ID")).longValue() : null;

            if (locationId != null && !locationIdList.contains(locationId)) {
                continue;
            }

            if (!typeQty.containsKey(type)) {
                Map<String, Integer> tmpMap = new HashMap<>();
                tmpMap.put("onlineQty", onlineFlag ? 1 : 0);
                tmpMap.put("totalQty", 1);

                typeQty.put(type, tmpMap);
            } else {
                int onlineQty = typeQty.get(type).get("onlineQty") + (onlineFlag ? 1 : 0);
                int totalQty = typeQty.get(type).get("totalQty") + 1;

                Map<String, Integer> tmpMap = new HashMap<>();
                tmpMap.put("onlineQty", onlineQty);
                tmpMap.put("totalQty", totalQty);

                typeQty.put(type, tmpMap);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        int total = typeQty.values().stream().mapToInt(e -> e.get("onlineQty")).sum();

        for (Map.Entry<String, Map<String, Integer>> map : typeQty.entrySet()) {
            int qty = map.getValue().get("onlineQty");
            int totalTypeQty = map.getValue().get("totalQty");

            String type = map.getKey();

            Map<String, Object> tmpMap = new HashMap<>();

            tmpMap.put("TYPE", type);
            tmpMap.put("QTY", qty);
//            tmpMap.put("TOTAL", total);
            tmpMap.put("TOTAL", totalTypeQty);
//            tmpMap.put("RATE", 100D * qty / total);
            tmpMap.put("RATE", 100D * qty / totalTypeQty);

            result.add(tmpMap);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> b06FixtureStatusOnlineByType(String timeSpanStr) {

        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream().filter(e -> e.getLocationName().equalsIgnoreCase("line")).map(e -> e.getId()).collect(Collectors.toList());

        List<Map<String, Object>> onlineDataMap = b06TeEquipmentFixtureRepository.fixtureOnlineBetween(timeSpan.getStartDate(), timeSpan.getEndDate());

        Map<String, Map<String, Integer>> typeQty = new HashMap<>();

        for (Map<String, Object> onlineData : onlineDataMap) {

            String cloneFixtureCode = onlineData.get("CLONE_FIXTURE_CODE") != null ? (String) onlineData.get("CLONE_FIXTURE_CODE") : null;

            String type = StringUtils.isEmpty(onlineData.get("TYPE")) ? "null" : (String) onlineData.get("TYPE");

            boolean onlineFlag = true;

            if (StringUtils.isEmpty(cloneFixtureCode)) {
                onlineFlag = false;
            }

            Long locationId = onlineData.get("LOCATION_ID") != null ? ((BigDecimal) onlineData.get("LOCATION_ID")).longValue() : null;

            if (locationId != null && !locationIdList.contains(locationId)) {
                continue;
            }

            if (!typeQty.containsKey(type)) {
                Map<String, Integer> tmpMap = new HashMap<>();
                tmpMap.put("onlineQty", onlineFlag ? 1 : 0);
                tmpMap.put("totalQty", 1);

                typeQty.put(type, tmpMap);
            } else {
                int onlineQty = typeQty.get(type).get("onlineQty") + (onlineFlag ? 1 : 0);
                int totalQty = typeQty.get(type).get("totalQty") + 1;

                Map<String, Integer> tmpMap = new HashMap<>();
                tmpMap.put("onlineQty", onlineQty);
                tmpMap.put("totalQty", totalQty);

                typeQty.put(type, tmpMap);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        int total = typeQty.values().stream().mapToInt(e -> e.get("onlineQty")).sum();

        for (Map.Entry<String, Map<String, Integer>> map : typeQty.entrySet()) {
            int qty = map.getValue().get("onlineQty");
            int totalTypeQty = map.getValue().get("totalQty");

            String type = map.getKey();

            Map<String, Object> tmpMap = new HashMap<>();

            tmpMap.put("TYPE", type);
            tmpMap.put("QTY", qty);
//            tmpMap.put("TOTAL", total);
            tmpMap.put("TOTAL", totalTypeQty);
//            tmpMap.put("RATE", 100D * qty / total);
            tmpMap.put("RATE", 100D * qty / totalTypeQty);

            result.add(tmpMap);
        }

        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> b06EquipmentDetailListV2(String timeSpanStr) {

        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }

        Map<Long, String> locationMap = teEquipmentFixtureLocationRepository.findAll().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getLocationName()));
        Map<Long, String> ownerMap = teEFOwnerTypeRepository.findAll().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getOwnerType()));

        Double efUsingLimit = testOeeSpecRepository.findTop1ByItem("ef_using_limit").doubleValue();
        Date nowTime = new Date();
        Date limitTime = nowTime.before(timeSpan.getEndDate()) ? nowTime : timeSpan.getEndDate();

        List<TeEquipment> equipmentList = teEquipmentRepository.findAll().stream().sorted((e1, e2) -> e1.getEquipmentName().compareTo(e2.getEquipmentName())).collect(Collectors.toList());

        List<Map<String, Object>> equipmentResult = new ArrayList<>();

        for (TeEquipment equipment : equipmentList) {
            List<Map<String, Object>> useHistory = b06TeEquipmentFixtureRepository.equipmentUseHistoryBetween(equipment.getId(), timeSpan.getStartDate(), timeSpan.getEndDate());
            if (useHistory.isEmpty()) {
                continue;
            }

            Date tmpStartTime = null;
            Date tmpEndTime = null;
            Date tmpBeginTime = null;
            Date tmpFinishTime = null;

            long tmpTotalTime = 0;
            long tmpOnlineTime = 0;
            long tmpOfflineTime = 0;
            long tmpUseTime = 0;
            long tmpFreeTime = 0;

            int index = 0;

            long lastTid = 0;

            for (Map<String, Object> useMap : useHistory) {
                if (tmpBeginTime == null) {
                    tmpBeginTime = (Date) useMap.get("START_TIME");
                }

                if (tmpFinishTime == null && useMap.get("END_TIME") != null) {
                    tmpFinishTime = (Date) useMap.get("END_TIME");
                } else if (tmpFinishTime != null && useMap.get("END_TIME") != null && tmpFinishTime.before((Date) useMap.get("END_TIME"))) {
                    tmpFinishTime = (Date) useMap.get("END_TIME");
                }

                if ( useMap.get("START_TIME") != null && useMap.get("END_TIME") != null ) {
                    if (tmpStartTime == null && tmpEndTime == null) {
                        tmpStartTime = (Date) useMap.get("START_TIME");
                        tmpEndTime = (Date) useMap.get("END_TIME");
                    } else {
                        Date startTime = (Date) useMap.get("START_TIME");
                        Date endTime = (Date) useMap.get("END_TIME");

                        if (startTime.compareTo(tmpEndTime) <= 0 && endTime.compareTo(tmpEndTime) > 0) {
                            tmpEndTime = endTime;
                        } else if (startTime.compareTo(tmpEndTime) >= 0) {
                            tmpUseTime += tmpEndTime.getTime() - tmpStartTime.getTime();
                            tmpStartTime = startTime;
                            tmpEndTime = endTime;
                        }
                    }
                }

                if (index > 0) {
                    Date tmpPrevEndTime = (Date) useHistory.get(index).get("END_TIME");
                    Date tmpCurrentStartTime = (Date) useMap.get("START_TIME");

                    if (tmpPrevEndTime != null && tmpCurrentStartTime != null && tmpPrevEndTime.before(tmpCurrentStartTime)) {
                        long tmpTimeGap = tmpCurrentStartTime.getTime() - tmpPrevEndTime.getTime();
                        if (tmpTimeGap > efUsingLimit * 60 * 60 * 1000) {
                            tmpOfflineTime += tmpTimeGap;
                        }
                    }
                }

                lastTid = ((BigDecimal) useMap.get("T_ID")).longValue();

                index++;
            }

            TeTestData teTestData = teTestDataRepository.findById(lastTid).orElse(null);

            if (tmpEndTime != null) {
                long tmpTimeGap = limitTime.getTime() - tmpEndTime.getTime();
                if (tmpTimeGap > efUsingLimit * 60 * 60 * 1000) {
                    tmpOfflineTime += tmpTimeGap;
                }
            }

            if (tmpStartTime != null && tmpEndTime != null) {
                tmpUseTime += tmpEndTime.getTime() - tmpStartTime.getTime();
            }

            tmpTotalTime = tmpBeginTime != null ? (limitTime.getTime() - tmpBeginTime.getTime()) : 0;
            tmpOnlineTime = tmpTotalTime - tmpOfflineTime;
            tmpFreeTime = tmpOnlineTime - tmpUseTime;

            Map<String, Object> tmpMap = new HashMap<>();

            String tmpOwner = "buy";
            if (equipment.getOwnerTypeId() != null && ownerMap.containsKey(equipment.getOwnerTypeId())) {
                tmpOwner = ownerMap.get(equipment.getOwnerTypeId());
            }

            String tmpLocation = "line";
            if (equipment.getLocationId() != null && locationMap.containsKey(equipment.getLocationId())) {
                tmpLocation = locationMap.get(equipment.getLocationId());
            }

            double tmpPerformance = tmpOnlineTime > 0 ? 100D * tmpUseTime / tmpOnlineTime : 100D;

            tmpMap.put("beginTime", tmpBeginTime);
            tmpMap.put("latestOnlineTime", tmpFinishTime);
            tmpMap.put("useTime", tmpUseTime / (60 * 60 * 1000D));
            tmpMap.put("totalTime", tmpTotalTime / (60 * 60 * 1000D));
            tmpMap.put("offlineTime", tmpOfflineTime / (60 * 60 * 1000D));
            tmpMap.put("onlineTime", tmpOnlineTime / (60 * 60 * 1000D));
            tmpMap.put("freeTime", tmpFreeTime / (60 * 60 * 1000D));
            tmpMap.put("performance", tmpPerformance);

            tmpMap.put("name", equipment.getEquipmentName());
            tmpMap.put("type", equipment.getType());
            tmpMap.put("ownner", tmpOwner);
            tmpMap.put("location", tmpLocation);
            tmpMap.put("line", equipment.getLine());
            tmpMap.put("model", equipment.getModel());
            tmpMap.put("station", equipment.getStation());
            tmpMap.put("ate", teTestData != null ? teTestData.getAte() : "");

            if ( equipment.getLocationId() != null ) {
                String tmpStatus = locationMap.get(equipment.getLocationId());
                if (StringUtils.isEmpty(tmpStatus) || !tmpStatus.equalsIgnoreCase("line")) {
                    continue;
                }
            }

            if ( equipment.getLatestOnlineTime() != null && ( nowTime.getTime() - equipment.getLatestOnlineTime().getTime() <= efUsingLimit * 60 * 60 * 1000 ) ){
                tmpMap.put("status", "online");
            } else {
                tmpMap.put("status", "offline");
            }

            equipmentResult.add(tmpMap);
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("equipmentResult", equipmentResult);

        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> b06FixtureDetailListV2(String timeSpanStr) {

        TimeSpan timeSpan;
        if (StringUtils.isEmpty(timeSpanStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        } else {
            timeSpan = TimeSpan.from(timeSpanStr);
        }

        Map<Long, String> locationMap = teEquipmentFixtureLocationRepository.findAll().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getLocationName()));
        Map<Long, String> ownerMap = teEFOwnerTypeRepository.findAll().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getOwnerType()));

        Double efUsingLimit = testOeeSpecRepository.findTop1ByItem("ef_using_limit").doubleValue();
        Date nowTime = new Date();
        Date limitTime = nowTime.before(timeSpan.getEndDate()) ? nowTime : timeSpan.getEndDate();

        List<TeFixture> fixtureList = teFixtureRepository.findAll().stream().sorted((e1, e2) -> e1.getFixtureCode().compareTo(e2.getFixtureCode())).collect(Collectors.toList());

        List<Map<String, Object>> fixtureResult = new ArrayList<>();

        for (TeFixture fixture : fixtureList) {
            List<Map<String, Object>> useHistory = b06TeEquipmentFixtureRepository.fixtureUseHistoryBetween(fixture.getFixtureCode(), timeSpan.getStartDate(), timeSpan.getEndDate());
            if (useHistory.isEmpty()) {
                continue;
            }

            Date tmpStartTime = null;
            Date tmpEndTime = null;
            Date tmpBeginTime = null;
            Date tmpFinishTime = null;

            long tmpTotalTime = 0;
            long tmpOnlineTime = 0;
            long tmpOfflineTime = 0;
            long tmpUseTime = 0;
            long tmpFreeTime = 0;

            int index = 0;

            String lastAte = "";

            for (Map<String, Object> useMap : useHistory) {
                if (tmpBeginTime == null) {
                    tmpBeginTime = (Date) useMap.get("START_TIME");
                }

                if (tmpFinishTime == null && useMap.get("END_TIME") != null) {
                    tmpFinishTime = (Date) useMap.get("END_TIME");
                } else if (tmpFinishTime != null && useMap.get("END_TIME") != null && tmpFinishTime.before((Date) useMap.get("END_TIME"))) {
                    tmpFinishTime = (Date) useMap.get("END_TIME");
                }

                if ( useMap.get("START_TIME") != null && useMap.get("END_TIME") != null ) {
                    if (tmpStartTime == null && tmpEndTime == null) {
                        tmpStartTime = (Date) useMap.get("START_TIME");
                        tmpEndTime = (Date) useMap.get("END_TIME");
                    } else {
                        Date startTime = (Date) useMap.get("START_TIME");
                        Date endTime = (Date) useMap.get("END_TIME");

                        if (startTime.compareTo(tmpEndTime) <= 0 && endTime.compareTo(tmpEndTime) > 0) {
                            tmpEndTime = endTime;
                        } else if (startTime.compareTo(tmpEndTime) >= 0) {
                            tmpUseTime += tmpEndTime.getTime() - tmpStartTime.getTime();
                            tmpStartTime = startTime;
                            tmpEndTime = endTime;
                        }
                    }
                }

                if (index > 0) {
                    Date tmpPrevEndTime = (Date) useHistory.get(index).get("END_TIME");
                    Date tmpCurrentStartTime = (Date) useMap.get("START_TIME");

                    if (tmpPrevEndTime != null && tmpCurrentStartTime != null && tmpPrevEndTime.before(tmpCurrentStartTime)) {
                        long tmpTimeGap = tmpCurrentStartTime.getTime() - tmpPrevEndTime.getTime();
                        if (tmpTimeGap > efUsingLimit * 60 * 60 * 1000) {
                            tmpOfflineTime += tmpTimeGap;
                        }
                    }
                }

                lastAte = StringUtils.isEmpty(useMap.get("ATE")) ? "" : (String) useMap.get("ATE");

                index++;
            }

            if (tmpEndTime != null) {
                long tmpTimeGap = limitTime.getTime() - tmpEndTime.getTime();
                if (tmpTimeGap > efUsingLimit * 60 * 60 * 1000) {
                    tmpOfflineTime += tmpTimeGap;
                }
            }

            if (tmpStartTime != null && tmpEndTime != null) {
                tmpUseTime += tmpEndTime.getTime() - tmpStartTime.getTime();
            }

            tmpTotalTime = tmpBeginTime != null ? (limitTime.getTime() - tmpBeginTime.getTime()) : 0;
            tmpOnlineTime = tmpTotalTime - tmpOfflineTime;
            tmpFreeTime = tmpOnlineTime - tmpUseTime;

            Map<String, Object> tmpMap = new HashMap<>();

            String tmpOwner = "buy";
            if (fixture.getOwnerTypeId() != null && ownerMap.containsKey(fixture.getOwnerTypeId())) {
                tmpOwner = ownerMap.get(fixture.getOwnerTypeId());
            }

            String tmpLocation = "line";
            if (fixture.getLocationId() != null && locationMap.containsKey(fixture.getLocationId())) {
                tmpLocation = locationMap.get(fixture.getLocationId());
            }

            double tmpPerformance = tmpOnlineTime > 0 ? 100D * tmpUseTime / tmpOnlineTime : 100D;

            tmpMap.put("beginTime", tmpBeginTime);
            tmpMap.put("latestOnlineTime", tmpFinishTime);
            tmpMap.put("useTime", tmpUseTime / (60 * 60 * 1000D));
            tmpMap.put("totalTime", tmpTotalTime / (60 * 60 * 1000D));
            tmpMap.put("offlineTime", tmpOfflineTime / (60 * 60 * 1000D));
            tmpMap.put("onlineTime", tmpOnlineTime / (60 * 60 * 1000D));
            tmpMap.put("freeTime", tmpFreeTime / (60 * 60 * 1000D));
            tmpMap.put("performance", tmpPerformance);

            tmpMap.put("name", fixture.getFixtureCode());
            tmpMap.put("type", fixture.getType());
            tmpMap.put("ownner", tmpOwner);
            tmpMap.put("location", tmpLocation);
            tmpMap.put("line", fixture.getLine());
            tmpMap.put("model", fixture.getModel());
            tmpMap.put("station", fixture.getStation());
            tmpMap.put("ate", lastAte);

            if ( fixture.getLocationId() != null ) {
                String tmpStatus = locationMap.get(fixture.getLocationId());
                if (StringUtils.isEmpty(tmpStatus) || !tmpStatus.equalsIgnoreCase("line")) {
                    continue;
                }
            }

            if ( fixture.getLatestOnlineTime() != null && ( nowTime.getTime() - fixture.getLatestOnlineTime().getTime() <= efUsingLimit * 60 * 60 * 1000 ) ){
                tmpMap.put("status", "online");
            } else {
                tmpMap.put("status", "offline");
            }

            fixtureResult.add(tmpMap);
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("fixtureResult", fixtureResult);

        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> b06EquipmentDueDateReminder() {

        Date nowDate = new Date();

        List<TeEFOwnerType> ownerTypeList = teEFOwnerTypeRepository.findAll();

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream()
                .filter(e -> e.getLocationName().equalsIgnoreCase("line") || e.getLocationName().equalsIgnoreCase("tool_room"))
                .map(e -> e.getId()).collect(Collectors.toList());

        Long buyId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("buy")).map(e -> e.getId()).findFirst().orElse(0L);
        Long borrowId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("borrow")).map(e -> e.getId()).findFirst().orElse(0L);
        Long rentId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("rent")).map(e -> e.getId()).findFirst().orElse(0L);

        List<TeEquipment> equipmentList = teEquipmentRepository.findAll();

        List<Map<String, Object>> buyResult = new ArrayList<>();
        List<Map<String, Object>> borrowResult = new ArrayList<>();
        List<Map<String, Object>> rentResult = new ArrayList<>();

        for (TeEquipment equipment : equipmentList) {
            if (equipment.getLocationId() != null && !locationIdList.contains(equipment.getLocationId())) {
                continue;
            }

            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("name", equipment.getEquipmentName());
            tmpMap.put("type", equipment.getType());
            tmpMap.put("receiveTime", null);
            tmpMap.put("returnTime", null);
            tmpMap.put("reminder", "normal");
            tmpMap.put("EF", "equipment");

            if (buyId.equals(equipment.getOwnerTypeId()) || equipment.getOwnerTypeId() == null) {
                tmpMap.put("receiveTime", equipment.getCalibrationTime());
                if (equipment.getCalibrationTime() != null && equipment.getCalibrationExpiredDay() != null) {
                    Calendar tmpCal = Calendar.getInstance();
                    tmpCal.setTime(equipment.getCalibrationTime());
                    tmpCal.add(Calendar.DAY_OF_YEAR, equipment.getCalibrationExpiredDay().intValue());

                    tmpMap.put("returnTime", tmpCal.getTime());

                    if (tmpCal.getTimeInMillis() - nowDate.getTime() <= 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "alert");
                    } else if (tmpCal.getTimeInMillis() - nowDate.getTime() <= 3 * 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "warning");
                    }
                }

                buyResult.add(tmpMap);
            } else {
                tmpMap.put("receiveTime", equipment.getLicenseReceiveTime());
                tmpMap.put("returnTime", equipment.getLicenseExpiredTime());

                if (equipment.getLicenseExpiredTime() != null) {
                    if (equipment.getLicenseExpiredTime().getTime() - nowDate.getTime() <= 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "alert");
                    } else if (equipment.getLicenseExpiredTime().getTime() - nowDate.getTime() <= 3 * 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "warning");
                    }
                }

                if (borrowId.equals(equipment.getOwnerTypeId())) {
                    borrowResult.add(tmpMap);
                } else if (rentId.equals(equipment.getOwnerTypeId())) {
                    rentResult.add(tmpMap);
                }
            }
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        result.put("buy", buyResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        result.put("borrow", borrowResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        result.put("rent", rentResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> b06FixtureDueDateReminder() {

        Date nowDate = new Date();

        List<TeEFOwnerType> ownerTypeList = teEFOwnerTypeRepository.findAll();

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream()
                .filter(e -> e.getLocationName().equalsIgnoreCase("line") || e.getLocationName().equalsIgnoreCase("tool_room"))
                .map(e -> e.getId()).collect(Collectors.toList());

        Long buyId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("buy")).map(e -> e.getId()).findFirst().orElse(0L);
        Long borrowId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("borrow")).map(e -> e.getId()).findFirst().orElse(0L);
        Long rentId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("rent")).map(e -> e.getId()).findFirst().orElse(0L);

        List<TeFixture> fixtureList = teFixtureRepository.findAll();

        List<Map<String, Object>> buyResult = new ArrayList<>();
        List<Map<String, Object>> borrowResult = new ArrayList<>();
        List<Map<String, Object>> rentResult = new ArrayList<>();

        for (TeFixture fixture : fixtureList) {
            if (fixture.getLocationId() != null && !locationIdList.contains(fixture.getLocationId())) {
                continue;
            }

            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("name", fixture.getFixtureCode());
            tmpMap.put("type", fixture.getType());
            tmpMap.put("receiveTime", null);
            tmpMap.put("returnTime", null);
            tmpMap.put("reminder", "normal");
            tmpMap.put("EF", "fixture");

            if (buyId.equals(fixture.getOwnerTypeId()) || fixture.getOwnerTypeId() == null) {
                tmpMap.put("receiveTime", fixture.getCalibrationTime());
                if (fixture.getCalibrationTime() != null && fixture.getCalibrationExpiredDay() != null) {
                    Calendar tmpCal = Calendar.getInstance();
                    tmpCal.setTime(fixture.getCalibrationTime());
                    tmpCal.add(Calendar.DAY_OF_YEAR, fixture.getCalibrationExpiredDay().intValue());

                    tmpMap.put("returnTime", tmpCal.getTime());

                    if (tmpCal.getTimeInMillis() - nowDate.getTime() <= 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "alert");
                    } else if (tmpCal.getTimeInMillis() - nowDate.getTime() <= 3 * 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "warning");
                    }
                }

                buyResult.add(tmpMap);
            } else {
                tmpMap.put("receiveTime", fixture.getLicenseReceiveTime());
                tmpMap.put("returnTime", fixture.getLicenseExpiredTime());

                if (fixture.getLicenseExpiredTime() != null) {
                    if (fixture.getLicenseExpiredTime().getTime() - nowDate.getTime() <= 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "alert");
                    } else if (fixture.getLicenseExpiredTime().getTime() - nowDate.getTime() <= 3 * 24 * 60 * 60 * 1000) {
                        tmpMap.put("reminder", "warning");
                    }
                }

                if (borrowId.equals(fixture.getOwnerTypeId())) {
                    borrowResult.add(tmpMap);
                } else if (rentId.equals(fixture.getOwnerTypeId())) {
                    rentResult.add(tmpMap);
                }
            }
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        result.put("buy", buyResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        result.put("borrow", borrowResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        result.put("rent", rentResult.stream().sorted((e1, e2) -> {
            if (e1.get("receiveTime") != null && e2.get("receiveTime") == null) {
                return -1;
            } else if (e1.get("receiveTime") == null && e2.get("receiveTime") != null) {
                return 1;
            } else if (e1.get("receiveTime") != null && e2.get("receiveTime") != null) {
                ((Date) e1.get("receiveTime")).compareTo((Date) e2.get("receiveTime"));
            }
            return 0;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Map<String, Map<String, Object>> b06EquipmentOwnerSumary() {

        List<TeEFOwnerType> ownerTypeList = teEFOwnerTypeRepository.findAll();

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream()
                .filter(e -> e.getLocationName().equalsIgnoreCase("line")
                        || e.getLocationName().equalsIgnoreCase("calibrating")
                        || e.getLocationName().equalsIgnoreCase("repairing")
                )
                .map(e -> e.getId()).collect(Collectors.toList());

        Long buyId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("buy")).map(e -> e.getId()).findFirst().orElse(0L);
        Long borrowId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("borrow")).map(e -> e.getId()).findFirst().orElse(0L);
        Long rentId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("rent")).map(e -> e.getId()).findFirst().orElse(0L);

        List<TeEquipment> equipmentList = teEquipmentRepository.findAll();

        long buyCount = 0;
        long borrowCount = 0;
        long rentCount = 0;

        for (TeEquipment equipment : equipmentList) {
            if (equipment.getLocationId() != null && !locationIdList.contains(equipment.getLocationId())) {
                continue;
            }

            if (equipment.getOwnerTypeId() == null || equipment.getOwnerTypeId() == buyId) {
                buyCount++;
            } else if (equipment.getOwnerTypeId() == borrowId) {
                borrowCount++;
            } else if (equipment.getOwnerTypeId() == rentId) {
                rentCount++;
            }
        }

        long total = buyCount + borrowCount + rentCount;

        Map<String, Map<String, Object>> result = new HashMap<>();

        Map<String, Object> buyResult = new HashMap<>();
        buyResult.put("owner", "buy");
        buyResult.put("qty", buyCount);
        buyResult.put("total", total);
        result.put("buy", buyResult);

        Map<String, Object> borrowResult = new HashMap<>();
        borrowResult.put("owner", "borrow");
        borrowResult.put("qty", borrowCount);
        borrowResult.put("total", total);
        result.put("borrow", borrowResult);

        Map<String, Object> rentResult = new HashMap<>();
        rentResult.put("owner", "rent");
        rentResult.put("qty", rentCount);
        rentResult.put("total", total);
        result.put("rent", rentResult);

        return result;

    }

    @Override
    public Map<String, Map<String, Object>> b06FixtureOwnerSumary() {

        List<TeEFOwnerType> ownerTypeList = teEFOwnerTypeRepository.findAll();

        List<Long> locationIdList = teEquipmentFixtureLocationRepository.findAll().stream()
                .filter(e -> e.getLocationName().equalsIgnoreCase("line")
                        || e.getLocationName().equalsIgnoreCase("calibrating")
                        || e.getLocationName().equalsIgnoreCase("repairing")
                )
                .map(e -> e.getId()).collect(Collectors.toList());

        Long buyId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("buy")).map(e -> e.getId()).findFirst().orElse(0L);
        Long borrowId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("borrow")).map(e -> e.getId()).findFirst().orElse(0L);
        Long rentId = ownerTypeList.stream().filter(e -> e.getOwnerType().equalsIgnoreCase("rent")).map(e -> e.getId()).findFirst().orElse(0L);

        List<TeFixture> fixtureList = teFixtureRepository.findAll();

        long buyCount = 0;
        long borrowCount = 0;
        long rentCount = 0;

        for (TeFixture fixture : fixtureList) {
            if (fixture.getLocationId() != null && !locationIdList.contains(fixture.getLocationId())) {
                continue;
            }

            if (fixture.getOwnerTypeId() == null || fixture.getOwnerTypeId() == buyId) {
                buyCount++;
            } else if (fixture.getOwnerTypeId() == borrowId) {
                borrowCount++;
            } else if (fixture.getOwnerTypeId() == rentId) {
                rentCount++;
            }
        }

        long total = buyCount + borrowCount + rentCount;

        Map<String, Map<String, Object>> result = new HashMap<>();

        Map<String, Object> buyResult = new HashMap<>();
        buyResult.put("owner", "buy");
        buyResult.put("qty", buyCount);
        buyResult.put("total", total);
        result.put("buy", buyResult);

        Map<String, Object> borrowResult = new HashMap<>();
        borrowResult.put("owner", "borrow");
        borrowResult.put("qty", borrowCount);
        borrowResult.put("total", total);
        result.put("borrow", borrowResult);

        Map<String, Object> rentResult = new HashMap<>();
        rentResult.put("owner", "rent");
        rentResult.put("qty", rentCount);
        rentResult.put("total", total);
        result.put("rent", rentResult);

        return result;

    }

    // E&F provider & supplier

    @Override
    public Object b06OwnerEquipmentList() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<Map<String, Object>> equipmentData = b06TeEquipmentFixtureRepository.ownerEquipmentList();

        List<Map<String, Object>> unknownList = new ArrayList<>();
        List<Map<String, Object>> buyList = new ArrayList<>();
        List<Map<String, Object>> borrowList = new ArrayList<>();
        List<Map<String, Object>> rentList = new ArrayList<>();

        for (Map<String, Object> equipment : equipmentData) {
            Map<String, Object> tmpMap = new HashMap<>();

            String tmpOwner = (String) equipment.get("OWNER_TYPE");

            tmpMap.put("id", equipment.get("ID"));
            tmpMap.put("name", equipment.get("EQUIPMENT_NAME"));
            tmpMap.put("type", equipment.get("TYPE"));
            tmpMap.put("model", equipment.get("MODEL"));
            tmpMap.put("station", equipment.get("STATION"));
            tmpMap.put("line", equipment.get("LINE"));

            if (tmpOwner == null) {
                unknownList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("buy")) {
                tmpMap.put("calibrationTime", equipment.get("CALIBRATION_TIME"));
                tmpMap.put("calibrationCycle", equipment.get("CALIBRATION_EXPIRED_DAY"));
                buyList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("borrow")) {
                tmpMap.put("borrowTime", equipment.get("LICENSE_RECEIVE_TIME"));
                tmpMap.put("returnTime", equipment.get("LICENSE_EXPIRED_TIME"));
                borrowList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("rent")) {
                tmpMap.put("receiveTime", equipment.get("LICENSE_RECEIVE_TIME"));
                tmpMap.put("expiredTime", equipment.get("LICENSE_EXPIRED_TIME"));
                rentList.add(tmpMap);
            }
        }

        Map<String, List<Map<String, Object>>> resultData = new HashMap<>();

        resultData.put("unknown", unknownList);
        resultData.put("buy", buyList);
        resultData.put("borrow", borrowList);
        resultData.put("rent", rentList);

        Map<String, Object> result = new HashMap<>();

        result.put("data", resultData);

        return result;
    }

    @Override
    public Object b06OwnerFixtureList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<Map<String, Object>> fixtureData = b06TeEquipmentFixtureRepository.ownerFixtureList();

        List<Map<String, Object>> unknownList = new ArrayList<>();
        List<Map<String, Object>> buyList = new ArrayList<>();
        List<Map<String, Object>> borrowList = new ArrayList<>();
        List<Map<String, Object>> rentList = new ArrayList<>();

        for (Map<String, Object> fixture : fixtureData) {
            Map<String, Object> tmpMap = new HashMap<>();

            String tmpOwner = (String) fixture.get("OWNER_TYPE");

            tmpMap.put("id", fixture.get("ID"));
            tmpMap.put("name", fixture.get("FIXTURE_CODE"));
            tmpMap.put("type", fixture.get("TYPE"));
            tmpMap.put("model", fixture.get("MODEL"));
            tmpMap.put("station", fixture.get("STATION"));
            tmpMap.put("line", fixture.get("LINE"));

            if (tmpOwner == null) {
                unknownList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("buy")) {
                tmpMap.put("calibrationTime", fixture.get("CALIBRATION_TIME"));
                tmpMap.put("calibrationCycle", fixture.get("CALIBRATION_EXPIRED_DAY"));
                buyList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("borrow")) {
                tmpMap.put("borrowTime", fixture.get("LICENSE_RECEIVE_TIME"));
                tmpMap.put("returnTime", fixture.get("LICENSE_EXPIRED_TIME"));
                borrowList.add(tmpMap);
            } else if (tmpOwner.equalsIgnoreCase("rent")) {
                tmpMap.put("receiveTime", fixture.get("LICENSE_RECEIVE_TIME"));
                tmpMap.put("expiredTime", fixture.get("LICENSE_EXPIRED_TIME"));
                rentList.add(tmpMap);
            }
        }

        Map<String, List<Map<String, Object>>> resultData = new HashMap<>();

        resultData.put("unknown", unknownList);
        resultData.put("buy", buyList);
        resultData.put("borrow", borrowList);
        resultData.put("rent", rentList);

        Map<String, Object> result = new HashMap<>();

        result.put("data", resultData);

        return result;
    }

    @Override
    public boolean b06EquipmentOwnerUpdate(Map<String, Object> data) throws ParseException {

        long equipmentId = (int) data.get("equipmentId");

        String equipmentName = (String) data.get("equipmentName");

        String ownerType = (String) data.get("ownerType");

        Long calibrationExpiredDay = data.containsKey("calibrationExpiredDay") ? Long.valueOf((int) data.get("calibrationExpiredDay")) : null;

        TeEquipment equipment = teEquipmentRepository.findTop1ByEquipmentName(equipmentName).orElse(null);

        if (equipment == null || equipment.getId() != equipmentId) {
            return false;
        }

        TeEFOwnerType efOwnerType = teEFOwnerTypeRepository.findByOwnerType(ownerType).orElse(null);

        if (efOwnerType == null) {
            if (StringUtils.isEmpty(ownerType)) {
                equipment.setOwnerTypeId(null);
            } else {
                return false;
            }
        } else {
            equipment.setOwnerTypeId(efOwnerType.getId());
        }

        if (calibrationExpiredDay != null) {
            equipment.setCalibrationExpiredDay(calibrationExpiredDay);
        }

        teEquipmentRepository.save(equipment);

        return true;
    }

    @Override
    public boolean b06FixtureOwnerUpdate(Map<String, Object> data) throws ParseException {

        long fixtureId = (int) data.get("fixtureId");

        String fixtureCode = (String) data.get("fixtureCode");

        String ownerType = (String) data.get("ownerType");

        Long calibrationExpiredDay = data.containsKey("calibrationExpiredDay") ? Long.valueOf((int) data.get("calibrationExpiredDay")) : null;

        TeFixture fixture = teFixtureRepository.findTop1ByFixtureCode(fixtureCode).orElse(null);

        if (fixture == null || fixture.getId() != fixtureId) {
            return false;
        }

        TeEFOwnerType efOwnerType = teEFOwnerTypeRepository.findByOwnerType(ownerType).orElse(null);

        if (efOwnerType == null) {
            if (StringUtils.isEmpty(ownerType)) {
                fixture.setOwnerTypeId(null);
            } else {
                return false;
            }
        } else {
            fixture.setOwnerTypeId(efOwnerType.getId());
        }

        if (calibrationExpiredDay != null) {
            fixture.setCalibrationExpiredDay(calibrationExpiredDay);
        }

        teFixtureRepository.save(fixture);

        return true;
    }

    @Override
    public Object b06EquipmentBuyHistoryList(String equipmentName) {

        TeEquipment equipment = teEquipmentRepository.findTop1ByEquipmentName(equipmentName).orElse(null);

        if (equipment == null) {
            return Collections.emptyList();
        }

        List<TeEquipmentCalibrationHistory> historyList = teEquipmentCalibrationHistoryRepository.findAllByEquipmentIdOrderByStartTimeAsc(equipment.getId());

        return historyList;
    }

    @Override
    public Object b06EquipmentBuyHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long equipmentId = (int) data.get("equipmentId");

        // check info valid
        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);

        if (equipment == null) {
            result.put("message", "Equipment ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("buy").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (equipment.getOwnerTypeId() == null || equipment.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Equipment owner type not matched");
            return result;
        }

        // check calibration time valid
        TeEquipmentCalibrationHistory newHistory = new TeEquipmentCalibrationHistory(data);

        if (newHistory.getStartTime() == null) {
            result.put("message", "Calibration start time not found");
            return result;
        }

        if (newHistory.getEndTime() != null && newHistory.getEndTime().before(newHistory.getStartTime())) {
            result.put("message", "Calibration time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentCalibrationHistory> historyList = teEquipmentCalibrationHistoryRepository.findAllByEquipmentIdOrderByStartTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getEndTime() == null) {
                if (e.getEndTime() == null) {
                    return true;
                }

                if (newHistory.getStartTime().getTime() >= e.getStartTime().getTime() && newHistory.getStartTime().getTime() <= e.getEndTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getStartTime().getTime() >= newHistory.getStartTime().getTime() && e.getStartTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }

                if (e.getEndTime() != null && e.getEndTime().getTime() >= newHistory.getStartTime().getTime() && e.getEndTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent calibration history");
            return result;
        }

        // save history
        teEquipmentCalibrationHistoryRepository.save(newHistory);

        TeEquipmentCalibrationHistory newestHistory = teEquipmentCalibrationHistoryRepository.findTop1ByEquipmentIdOrderByStartTimeAsc(equipmentId).orElse(null);

        equipment.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentBuyHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentCalibrationHistory newHistory = teEquipmentCalibrationHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isEndTime = newHistory.getEndTime() != null;

        newHistory.readData(data);

        // check calibration time valid
        if (newHistory.getStartTime() == null) {
            result.put("message", "Calibration start time not found");
            return result;
        }

        if (isEndTime && newHistory.getEndTime() == null) {
            result.put("message", "Calibration end time not found");
            return result;
        }

        if (newHistory.getEndTime() != null && newHistory.getEndTime().before(newHistory.getStartTime())) {
            result.put("message", "Calibration time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentCalibrationHistory> historyList = teEquipmentCalibrationHistoryRepository.findAllByEquipmentIdOrderByStartTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getEndTime() == null) {
                if (e.getEndTime() == null) {
                    return true;
                }

                if (newHistory.getStartTime().getTime() >= e.getStartTime().getTime() && newHistory.getStartTime().getTime() <= e.getEndTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getStartTime().getTime() >= newHistory.getStartTime().getTime() && e.getStartTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }

                if (e.getEndTime() != null && e.getEndTime().getTime() >= newHistory.getStartTime().getTime() && e.getEndTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent calibration history");
            return result;
        }

        // save history
        teEquipmentCalibrationHistoryRepository.save(newHistory);

        TeEquipmentCalibrationHistory newestHistory = teEquipmentCalibrationHistoryRepository.findTop1ByEquipmentIdOrderByStartTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentBuyHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentCalibrationHistory history = teEquipmentCalibrationHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (history == null) {
            return false;
        }

        teEquipmentCalibrationHistoryRepository.delete(history);

        TeEquipmentCalibrationHistory newestHistory = teEquipmentCalibrationHistoryRepository.findTop1ByEquipmentIdOrderByStartTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teEquipmentRepository.save(equipment);

        return true;

    }

    @Override
    public Object b06FixtureBuyHistoryList(String fixtureCode) {

        TeFixture fixture = teFixtureRepository.findTop1ByFixtureCode(fixtureCode).orElse(null);

        if (fixture == null) {
            return Collections.emptyList();
        }

        List<TeFixtureCalibrationHistory> historyList = teFixtureCalibrationHistoryRepository.findAllByFixtureIdOrderByStartTimeAsc(fixture.getId());

        return historyList;

    }

    @Override
    public Object b06FixtureBuyHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long fixtureId = (int) data.get("fixtureId");

        // check info valid
        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);

        if (fixture == null) {
            result.put("message", "Fixture ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("buy").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (fixture.getOwnerTypeId() == null || fixture.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Fixture owner type not matched");
            return result;
        }

        // check calibration time valid
        TeFixtureCalibrationHistory newHistory = new TeFixtureCalibrationHistory(data);

        if (newHistory.getStartTime() == null) {
            result.put("message", "Calibration start time not found");
            return result;
        }

        if (newHistory.getEndTime() != null && newHistory.getEndTime().before(newHistory.getStartTime())) {
            result.put("message", "Calibration time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureCalibrationHistory> historyList = teFixtureCalibrationHistoryRepository.findAllByFixtureIdOrderByStartTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getEndTime() == null) {
                if (e.getEndTime() == null) {
                    return true;
                }

                if (newHistory.getStartTime().getTime() >= e.getStartTime().getTime() && newHistory.getStartTime().getTime() <= e.getEndTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getStartTime().getTime() >= newHistory.getStartTime().getTime() && e.getStartTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }

                if (e.getEndTime() != null && e.getEndTime().getTime() >= newHistory.getStartTime().getTime() && e.getEndTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent calibration history");
            return result;
        }

        // save history
        teFixtureCalibrationHistoryRepository.save(newHistory);

        TeFixtureCalibrationHistory newestHistory = teFixtureCalibrationHistoryRepository.findTop1ByFixtureIdOrderByStartTimeAsc(fixtureId).orElse(null);

        fixture.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureBuyHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureCalibrationHistory newHistory = teFixtureCalibrationHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isEndTime = newHistory.getEndTime() != null;

        newHistory.readData(data);

        // check calibration time valid
        if (newHistory.getStartTime() == null) {
            result.put("message", "Calibration start time not found");
            return result;
        }

        if (isEndTime && newHistory.getEndTime() == null) {
            result.put("message", "Calibration end time not found");
            return result;
        }

        if (newHistory.getEndTime() != null && newHistory.getEndTime().before(newHistory.getStartTime())) {
            result.put("message", "Calibration time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureCalibrationHistory> historyList = teFixtureCalibrationHistoryRepository.findAllByFixtureIdOrderByStartTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getEndTime() == null) {
                if (e.getEndTime() == null) {
                    return true;
                }

                if (newHistory.getStartTime().getTime() >= e.getStartTime().getTime() && newHistory.getStartTime().getTime() <= e.getEndTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getStartTime().getTime() >= newHistory.getStartTime().getTime() && e.getStartTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }

                if (e.getEndTime() != null && e.getEndTime().getTime() >= newHistory.getStartTime().getTime() && e.getEndTime().getTime() <= newHistory.getEndTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent calibration history");
            return result;
        }

        // save history
        teFixtureCalibrationHistoryRepository.save(newHistory);

        TeFixtureCalibrationHistory newestHistory = teFixtureCalibrationHistoryRepository.findTop1ByFixtureIdOrderByStartTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureBuyHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureCalibrationHistory history = teFixtureCalibrationHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (history == null) {
            return false;
        }

        teFixtureCalibrationHistoryRepository.delete(history);

        TeFixtureCalibrationHistory newestHistory = teFixtureCalibrationHistoryRepository.findTop1ByFixtureIdOrderByStartTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setCalibrationTime(newestHistory.getEndTime() == null ? newestHistory.getStartTime() : newestHistory.getEndTime());
        teFixtureRepository.save(fixture);

        return true;

    }

    @Override
    public Object b06EquipmentBorrowHistoryList(String equipmentName) {

        TeEquipment equipment = teEquipmentRepository.findTop1ByEquipmentName(equipmentName).orElse(null);

        if (equipment == null) {
            return Collections.emptyList();
        }

        List<TeEquipmentBorrowHistory> historyList = teEquipmentBorrowHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipment.getId());

        return historyList;

    }

    @Override
    public Object b06EquipmentBorrowHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long equipmentId = (int) data.get("equipmentId");

        // check info valid
        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);

        if (equipment == null) {
            result.put("message", "Equipment ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("borrow").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (equipment.getOwnerTypeId() == null || equipment.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Equipment owner type not matched");
            return result;
        }

        // check borrow time valid
        TeEquipmentBorrowHistory newHistory = new TeEquipmentBorrowHistory(data);

        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Receive start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentBorrowHistory> historyList = teEquipmentBorrowHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent borrow history");
            return result;
        }

        // save history
        teEquipmentBorrowHistoryRepository.save(newHistory);

        TeEquipmentBorrowHistory newestHistory = teEquipmentBorrowHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentBorrowHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentBorrowHistory newHistory = teEquipmentBorrowHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isReturnTime = newHistory.getReturnTime() != null;

        newHistory.readData(data);

        // check borrow time valid
        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Borrow start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (isReturnTime && newHistory.getReturnTime() == null) {
            result.put("message", "Borrow end time not found");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentBorrowHistory> historyList = teEquipmentBorrowHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent borrow history");
            return result;
        }

        // save history
        teEquipmentBorrowHistoryRepository.save(newHistory);

        TeEquipmentBorrowHistory newestHistory = teEquipmentBorrowHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentBorrowHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentBorrowHistory history = teEquipmentBorrowHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (history == null) {
            return false;
        }

        teEquipmentBorrowHistoryRepository.delete(history);

        TeEquipmentBorrowHistory newestHistory = teEquipmentBorrowHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        return true;

    }

        // Fixture borrow
    @Override
    public Object b06FixtureBorrowHistoryList(String fixtureCode) {

        TeFixture fixture = teFixtureRepository.findTop1ByFixtureCode(fixtureCode).orElse(null);

        if (fixture == null) {
            return Collections.emptyList();
        }

        List<TeFixtureBorrowHistory> historyList = teFixtureBorrowHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixture.getId());

        return historyList;

    }

    @Override
    public Object b06FixtureBorrowHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long fixtureId = (int) data.get("fixtureId");

        // check info valid
        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);

        if (fixture == null) {
            result.put("message", "Fixture ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("borrow").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (fixture.getOwnerTypeId() == null || fixture.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Fixture owner type not matched");
            return result;
        }

        // check borrow time valid
        TeFixtureBorrowHistory newHistory = new TeFixtureBorrowHistory(data);

        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Receive start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureBorrowHistory> historyList = teFixtureBorrowHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent borrow history");
            return result;
        }

        // save history
        teFixtureBorrowHistoryRepository.save(newHistory);

        TeFixtureBorrowHistory newestHistory = teFixtureBorrowHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureBorrowHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureBorrowHistory newHistory = teFixtureBorrowHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isReturnTime = newHistory.getReturnTime() != null;

        newHistory.readData(data);

        // check borrow time valid
        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Borrow start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (isReturnTime && newHistory.getReturnTime() == null) {
            result.put("message", "Borrow end time not found");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureBorrowHistory> historyList = teFixtureBorrowHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent borrow history");
            return result;
        }

        // save history
        teFixtureBorrowHistoryRepository.save(newHistory);

        TeFixtureBorrowHistory newestHistory = teFixtureBorrowHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureBorrowHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureBorrowHistory history = teFixtureBorrowHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (history == null) {
            return false;
        }

        teFixtureBorrowHistoryRepository.delete(history);

        TeFixtureBorrowHistory newestHistory = teFixtureBorrowHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        return true;

    }

        // Equipment Rent
    @Override
    public Object b06EquipmentRentHistoryList(String equipmentName) {

        TeEquipment equipment = teEquipmentRepository.findTop1ByEquipmentName(equipmentName).orElse(null);

        if (equipment == null) {
            return Collections.emptyList();
        }

        List<TeEquipmentRentHistory> historyList = teEquipmentRentHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipment.getId());

        return historyList;

    }

    @Override
    public Object b06EquipmentRentHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long equipmentId = (int) data.get("equipmentId");

        // check info valid
        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);

        if (equipment == null) {
            result.put("message", "Equipment ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("rent").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (equipment.getOwnerTypeId() == null || equipment.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Equipment owner type not matched");
            return result;
        }

        // check rent time valid
        TeEquipmentRentHistory newHistory = new TeEquipmentRentHistory(data);

        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Receive start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentRentHistory> historyList = teEquipmentRentHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent rent history");
            return result;
        }

        // save history
        teEquipmentRentHistoryRepository.save(newHistory);

        TeEquipmentRentHistory newestHistory = teEquipmentRentHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentRentHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentRentHistory newHistory = teEquipmentRentHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isReturnTime = newHistory.getReturnTime() != null;

        newHistory.readData(data);

        // check rent time valid
        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Rent start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (isReturnTime && newHistory.getReturnTime() == null) {
            result.put("message", "Rent end time not found");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeEquipmentRentHistory> historyList = teEquipmentRentHistoryRepository.findAllByEquipmentIdOrderByReceiveTimeAsc(equipmentId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent rent history");
            return result;
        }

        // save history
        teEquipmentRentHistoryRepository.save(newHistory);

        TeEquipmentRentHistory newestHistory = teEquipmentRentHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06EquipmentRentHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long equipmentId = (int) data.get("equipmentId");

        TeEquipmentRentHistory history = teEquipmentRentHistoryRepository.findTop1ByIdAndEquipmentId(historyId, equipmentId).orElse(null);

        if (history == null) {
            return false;
        }

        teEquipmentRentHistoryRepository.delete(history);

        TeEquipmentRentHistory newestHistory = teEquipmentRentHistoryRepository.findTop1ByEquipmentIdOrderByReceiveTimeAsc(equipmentId).orElse(null);

        TeEquipment equipment = teEquipmentRepository.findById(equipmentId).orElse(null);
        equipment.setLicenseReceiveTime(newestHistory.getReceiveTime());
        equipment.setLicenseExpiredTime(newestHistory.getDueDate());
        teEquipmentRepository.save(equipment);

        return true;

    }

    // Fixture rent
    @Override
    public Object b06FixtureRentHistoryList(String fixtureCode) {

        TeFixture fixture = teFixtureRepository.findTop1ByFixtureCode(fixtureCode).orElse(null);

        if (fixture == null) {
            return Collections.emptyList();
        }

        List<TeFixtureRentHistory> historyList = teFixtureRentHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixture.getId());

        return historyList;

    }

    @Override
    public Object b06FixtureRentHistoryAdd(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long fixtureId = (int) data.get("fixtureId");

        // check info valid
        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);

        if (fixture == null) {
            result.put("message", "Fixture ID not found");
            return result;
        }

        TeEFOwnerType ownerType = teEFOwnerTypeRepository.findByOwnerType("rent").orElse(null);

        if (ownerType == null) {
            result.put("message", "System error!!! OwnerType not found");
            return result;
        } else if (fixture.getOwnerTypeId() == null || fixture.getOwnerTypeId() != ownerType.getId()) {
            result.put("message", "Fixture owner type not matched");
            return result;
        }

        // check rent time valid
        TeFixtureRentHistory newHistory = new TeFixtureRentHistory(data);

        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Receive start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureRentHistory> historyList = teFixtureRentHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent rent history");
            return result;
        }

        // save history
        teFixtureRentHistoryRepository.save(newHistory);

        TeFixtureRentHistory newestHistory = teFixtureRentHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureRentHistoryUpdate(Map<String, Object> data) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        result.put("message", "");
        result.put("result", false);

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureRentHistory newHistory = teFixtureRentHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (newHistory == null) {
            result.put("message", "History Id not found");
            return result;
        }

        boolean isReturnTime = newHistory.getReturnTime() != null;

        newHistory.readData(data);

        // check rent time valid
        if (newHistory.getReceiveTime() == null) {
            result.put("message", "Rent start time not found");
            return result;
        }

        if (newHistory.getDueDate() == null) {
            result.put("message", "Duedate start time not found");
            return result;
        }

        if (newHistory.getDueDate() != null && newHistory.getDueDate().before(newHistory.getReceiveTime())) {
            result.put("message", "Duedate time not valid");
            return result;
        }

        if (isReturnTime && newHistory.getReturnTime() == null) {
            result.put("message", "Rent end time not found");
            return result;
        }

        if (newHistory.getReturnTime() != null && newHistory.getReturnTime().before(newHistory.getReceiveTime())) {
            result.put("message", "Return time not valid");
            return result;
        }

        // check history valid
        List<TeFixtureRentHistory> historyList = teFixtureRentHistoryRepository.findAllByFixtureIdOrderByReceiveTimeAsc(fixtureId);

        long invalidCount = historyList.stream().filter(e -> {
            if (e.getId() == historyId) {
                return false;
            }

            if (newHistory.getReturnTime() == null) {
                if (e.getReturnTime() == null) {
                    return true;
                }

                if (newHistory.getReceiveTime().getTime() >= e.getReceiveTime().getTime() && newHistory.getReceiveTime().getTime() <= e.getReturnTime().getTime()) {
                    return true;
                }
            } else {
                if (e.getReceiveTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReceiveTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }

                if (e.getReturnTime() != null && e.getReturnTime().getTime() >= newHistory.getReceiveTime().getTime() && e.getReturnTime().getTime() <= newHistory.getReturnTime().getTime()) {
                    return true;
                }
            }

            return false;
        }).count();

        if (invalidCount > 0) {
            result.put("message", "Violent rent history");
            return result;
        }

        // save history
        teFixtureRentHistoryRepository.save(newHistory);

        TeFixtureRentHistory newestHistory = teFixtureRentHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        result.put("result", true);

        return result;

    }

    @Override
    public Object b06FixtureRentHistoryDelete(Map<String, Object> data) {

        long historyId = (int) data.get("historyId");
        long fixtureId = (int) data.get("fixtureId");

        TeFixtureRentHistory history = teFixtureRentHistoryRepository.findTop1ByIdAndFixtureId(historyId, fixtureId).orElse(null);

        if (history == null) {
            return false;
        }

        teFixtureRentHistoryRepository.delete(history);

        TeFixtureRentHistory newestHistory = teFixtureRentHistoryRepository.findTop1ByFixtureIdOrderByReceiveTimeAsc(fixtureId).orElse(null);

        TeFixture fixture = teFixtureRepository.findById(fixtureId).orElse(null);
        fixture.setLicenseReceiveTime(newestHistory.getReceiveTime());
        fixture.setLicenseExpiredTime(newestHistory.getDueDate());
        teFixtureRepository.save(fixture);

        return true;

    }

    // util class
    public enum AteStatusType {
        RUNNING,
        WAITING,
        FAILURE,
        DOWNTIME,
        DISCONNECTED,
        SHUTDOWN
    }

}