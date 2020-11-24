package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestStation;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestGroupService {
    int[] saveAll(List<TestGroup> groupList);

    int[] saveAllMeta(List<TestGroup> groupList);

    List<TestGroupMeta> getGroupMetaList(String factory, String modelName, Boolean parameter);

    List<TestGroupMeta> getGroupMetaList(String factory, String customer, String stage, String modelName, Boolean parameter);

    TestGroupMeta getGroupMeta(String factory, String modelName, String groupName);

    Map<String, List<TestGroup>> getGroupMapLineName(String factory, Date startDate, Date endDate);

    List<TestGroup> getGroupHourlyListFromDB(String factory, String modelName, String groupName, Date startDate, Date endDate);


    List<TestGroupDaily> getGroupDailyListFromDB(String factory, String modelName, Date startDate, Date endDate);

    List<TestGroupDaily> getGroupDailyList(String factory, Date startDate, Date endDate);

    List<TestGroupDaily> getGroupDailyList(String factory, String modelName, Date startDate, Date endDate, String mode, MoType moType);

    Map<String, Map<String, Map<String, TestGroupDaily>>> getGroupDailyMapByModelAndTime(String factory, Date startDate, Date endDate);

    Map<String, Map<String, TestGroupDaily>> getGroupDailyMapByTime(String factory, String modelName, Date startDate, Date endDate, MoType moType);

    Map<String, Map<String, TestGroupDaily>> getGroupDailyMapByModel(String factory, Date startDate, Date endDate, MoType moType);

    List<TestGroup> getGroupDailyBG(String factory, List<String> modelList, Date startDate, Date endDate);
}