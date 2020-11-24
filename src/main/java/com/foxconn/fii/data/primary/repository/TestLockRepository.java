package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestLock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TestLockRepository extends JpaRepository<TestLock, Integer> {

    List<TestLock> findAllByLockStatus(String lockStatus);

    List<TestLock> findAllByLockStatusAndShiftTimeAndShiftAndLockHistoryIsNotNull(String lockStatus, Date shiftTime, ShiftType shiftType);

    TestLock findTop1ByModelNameAndGroupNameAndStationNameAndShiftTimeAndShiftOrderByIdDesc(String modelName, String groupName, String stationName, Date shiftTime, ShiftType shiftType);

    List<TestLock> findAllByModelNameAndGroupNameAndStationNameAndLockStatusAndLockHistoryIsNotNull(String modelName, String groupName, String stationName, String lockStatus, Pageable pageable);

    TestLock findTop1ByModelNameAndGroupNameAndStationNameOrderByIdDesc(String modelName, String groupName, String stationName);
}
