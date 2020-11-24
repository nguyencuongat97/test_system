package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.TestStationDailyReport;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.data.primary.model.entity.TestStationMeta;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestStationService {

    int[] saveAll(List<TestStation> stationList);

    int[] saveAllMeta(List<TestStation> stationList);

    int countOnline(String factory, String modelName, String groupName, Date startDate, Date endDate);


    List<TestStationMeta> getStationMetaList(String factory, String modelName, String groupName);

    TestStationMeta getStationMeta(String factory, String modelName, String groupName, String stationName);


    Map<String, TestStation> getStationByHourlyMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, ShiftType shiftType);

    Map<String, TestStationDaily> getStationByWeeklyMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, ShiftType shiftType);


    Map<String, Map<String, TestStation>> getStationByGroupMap(String factory, Date startDate, Date endDate, MoType moType);


    List<TestStationDailyReport> getStationDailyReportList(String factory, Date startDate, Date endDate, String mode, MoType moType);

    List<TestStationDaily> getStationDailyList(String factory, String modelName, String groupName, Date startDate, Date endDate, String mode, MoType moType);

    TestStation getStation(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType);

    TestStationDaily getStationDaily(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType);

    Map<String, ListResponse> getStationIssue2(String factory, String modelName, String timeSpan);
}
