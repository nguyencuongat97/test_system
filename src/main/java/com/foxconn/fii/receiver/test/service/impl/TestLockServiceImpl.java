package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.b04.model.B04TestLock;
import com.foxconn.fii.data.b04.repository.B04TestLockRepository;
import com.foxconn.fii.data.b06.repository.B06TestLockRepository;
import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestLock;
import com.foxconn.fii.data.primary.repository.TestLockRepository;
import com.foxconn.fii.receiver.test.service.TestLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestLockServiceImpl implements TestLockService {

    @Autowired
    private TestLockRepository testLockRepository;

    @Autowired
    private B04TestLockRepository b04TestLockRepository;

    @Autowired
    private B06TestLockRepository b06TestLockRepository;

    @Override
    public List<TestLock> getAllLockedStation(String factory) {
        if ("B04".equalsIgnoreCase(factory)) {
            return testLockRepository.findAllByLockStatus("LOCK");
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllLockedStationName(String factory, Date startDate, ShiftType shiftType) {
        if ("B04".equalsIgnoreCase(factory)) {
            return b04TestLockRepository.findAllByLockStatusAndShiftTimeAndShift("LOCK", new java.sql.Date(startDate.getTime()), shiftType)
                    .stream().map(lock -> lock.getModelName() + "_" + lock.getGroupName() + "_" + lock.getStationName())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public TestLock getLockStatusStation(String factory, String modelName, String groupName, String stationName, Date startDate, ShiftType shiftType) {
        if ("B04".equalsIgnoreCase(factory)) {
            return testLockRepository.findTop1ByModelNameAndGroupNameAndStationNameAndShiftTimeAndShiftOrderByIdDesc(
                    modelName, groupName, stationName, new java.sql.Date(startDate.getTime()), shiftType);
        }
        return null;
    }

    @Override
    public Map<String, Long> getDowntimeByStationMap(String factory, Date startDate, ShiftType shiftType) {
        if ("B04".equalsIgnoreCase(factory)) {
            return b04TestLockRepository.findAllByLockStatusAndShiftTimeAndShiftAndLockHistoryIsNotNull("UNLOCK", new java.sql.Date(startDate.getTime()), shiftType).stream().collect(
                    Collectors.groupingBy(lock -> lock.getModelName() + "_" + lock.getGroupName() + "_" + lock.getStationName(),
                    Collectors.summingLong(lock -> lock.getDateTimeUnlock().getTime() - lock.getDateTimeLock().getTime()))
            );
        }
        return testLockRepository.findAllByLockStatusAndShiftTimeAndShiftAndLockHistoryIsNotNull("UNLOCK", new java.sql.Date(startDate.getTime()), shiftType).stream().collect(
                Collectors.groupingBy(lock -> lock.getModelName() + "_" + lock.getGroupName() + "_" + lock.getStationName(),
                Collectors.summingLong(lock -> lock.getDateTimeUnlock().getTime() - lock.getDateTimeLock().getTime()))
        );
    }

    @Override
    public List<B04TestLock> getLockedHistory(String factory, String modelName, String groupName, String stationName, Date startDate, ShiftType shiftType) {
        if ("B04".equalsIgnoreCase(factory)) {
            return b04TestLockRepository.findAllByModelNameAndGroupNameAndStationNameAndShiftTimeAndShift(
                    modelName, groupName, stationName, new java.sql.Date(startDate.getTime()), shiftType);
        }
        return Collections.emptyList();
    }

    @Override
    public TestLock getLock(String factory, String modelName, String groupName, String stationName) {
//        if ("B06".equalsIgnoreCase(factory)) {
//            b06TestLockRepository.findTop1ByModelNameAndGroupNameAndStationNameOrderByDateTimeLockDesc(modelName, groupName, stationName);
//        }

        return testLockRepository.findTop1ByModelNameAndGroupNameAndStationNameOrderByIdDesc(modelName, groupName, stationName);
    }

    @Override
    public TestLock save(TestLock locked) {
        return testLockRepository.save(locked);
    }

    @Override
    public void unlock(String factory, String modelName, String groupName, String stationName, String employee) {
        if ("B06".equalsIgnoreCase(factory)) {
            b06TestLockRepository.unlock(modelName, groupName, stationName, employee, new Timestamp(System.currentTimeMillis()));
        }
    }
}
