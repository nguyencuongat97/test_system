package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.b04.model.B04TestLock;
import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestLock;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestLockService {

    List<TestLock> getAllLockedStation(String factory);

    List<String> getAllLockedStationName(String factory, Date startDate, ShiftType shiftType);

    TestLock getLockStatusStation(String factory, String modelName, String groupName, String stationName, Date startDate, ShiftType shiftType);

    Map<String, Long> getDowntimeByStationMap(String factory, Date startDate, ShiftType shiftType);

    List<B04TestLock> getLockedHistory(String factory, String modelName, String groupName, String stationName, Date startDate, ShiftType shiftType);

    TestLock getLock(String factory, String modelName, String groupName, String stationName);

    TestLock save(TestLock locked);

    void unlock(String factory, String modelName, String groupName, String stationName, String employee);
}
