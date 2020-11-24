package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestModelService {

    void saveAll(List<TestModel> modelList);

    void saveAllMeta(List<TestModel> modelList);

    void updateAllMeta(List<TestModelMeta> modelMetaList);

    void save(TestModelMeta modelMeta);

    void delete(TestModelMeta modelMeta);

    List<TestModelMeta> getModelMetaList();

    List<TestModelMeta> getModelMetaList(String factory);

    List<TestModelMeta> getModelMetaList(String factory, Boolean parameter);

    List<TestModelMeta> getModelMetaList(String factory, String customer, String stage);

    Map<String, TestModelMeta> getModelMetaMap();

    TestModelMeta getModelMeta(String factory, String modelName);

    List<TestModel> getModelList(String factory, Date startDate, Date endDate, ShiftType shiftType, MoType moType);


    TestModel getModel(String factory, String modelName, TimeSpan timeSpan, Boolean customer, MoType moType);

    List<TestModel> getAllModelDaily(String factory, String customer, TimeSpan timeSpan, Boolean isCustomer, MoType moType);

    Map<String, TestModel> getModelWeekly(String factory, String modelName, TimeSpan timeSpan, Boolean customer, MoType moType);

    Map<String, Object> getRetestRateAllStationNameWeekly(String factory, String modelName, String timeSpan, Boolean customer, MoType moType);

    List<String> getModelMetaOnlineList(String factory, Date startDate, Date endDate);
}
