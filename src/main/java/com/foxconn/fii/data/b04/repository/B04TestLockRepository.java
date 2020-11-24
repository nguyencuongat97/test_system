package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04TestLock;
import com.foxconn.fii.common.ShiftType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface B04TestLockRepository extends JpaRepository<B04TestLock, Integer> {

    List<B04TestLock> findAllByLockStatusAndShiftTimeAndShift(String lockStatus, Date shiftTime, ShiftType shiftType);

    List<B04TestLock> findAllByLockStatusAndShiftTimeAndShiftAndLockHistoryIsNotNull(String lockStatus, Date shiftTime, ShiftType shiftType);

    B04TestLock findTop1ByModelNameAndGroupNameAndStationNameAndShiftTimeAndShiftOrderByIdDesc(String modelName, String groupName, String stationName, Date shiftTime, ShiftType shiftType);

    List<B04TestLock> findAllByModelNameAndGroupNameAndStationNameAndLockStatusAndLockHistoryIsNotNull(String modelName, String groupName, String stationName, String lockStatus, Pageable pageable);

    List<B04TestLock> findAllByModelNameAndGroupNameAndStationNameAndShiftTimeAndShift(String modelName, String groupName, String stationName, Date shiftTime, ShiftType shiftType);

    B04TestLock findTop1ByModelNameAndGroupNameAndStationNameOrderByIdDesc(String modelName, String groupName, String stationName);

}
