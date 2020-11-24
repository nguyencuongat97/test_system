package com.foxconn.fii.data.b05.repository;

import com.foxconn.fii.data.b05.model.B05TestLock;
import com.foxconn.fii.common.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface B05TestLockRepository extends JpaRepository<B05TestLock, Integer> {

    List<B05TestLock> findAllByLockStatus(String lockStatus);

    List<B05TestLock> findAllByLockStatusAndShiftTimeAndShift(String lockStatus, Date shiftTime, ShiftType shiftType);

    List<B05TestLock> findAllByLockStatusAndShiftTimeAndShiftAndLockHistoryIsNotNull(String lockStatus, Date shiftTime, ShiftType shiftType);

    List<B05TestLock> findAllByModelNameAndGroupNameAndStationNameAndShiftTimeAndShift(String modelName, String groupName, String stationName, Date shiftTime, ShiftType shiftType);
}
