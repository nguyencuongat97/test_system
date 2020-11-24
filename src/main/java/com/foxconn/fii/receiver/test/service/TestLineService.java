package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestLineMeta;

import java.util.List;

public interface TestLineService {

    void saveAllMeta(List<TestGroup> groupList);

    List<TestLineMeta> getLineList(String factory);
}
