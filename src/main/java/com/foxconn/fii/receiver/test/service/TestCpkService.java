package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestCpk;
import com.foxconn.fii.data.primary.model.entity.TestCpkSyncConfig;
import com.foxconn.fii.data.primary.model.entity.TestParameter;

import java.util.Date;
import java.util.List;

public interface TestCpkService {

    int[] saveAll(List<TestCpk> cpkList);

    void saveAllConfig(List<TestCpkSyncConfig> cpkSyncConfigList);

    void saveAllParameter(List<TestParameter> parameterList);

    List<TestCpk> getCpkList(String factory, String modelName, Date startDate, Date endDate);
}
