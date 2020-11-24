package com.foxconn.fii.data.b06.repository;

import com.foxconn.fii.data.b06.model.B06TestLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface B06TestLockRepository extends JpaRepository<B06TestLock, B06TestLock.B06TestLockId> {

    List<B06TestLock> findAllByDateTimeLockAfter(Timestamp dateTimeLock);

    List<B06TestLock> findAllByDateTimeLockAfterAndDateTimeUnlockIsNull(Timestamp dateTimeLock);

    B06TestLock findTop1ByModelNameAndGroupNameAndStationNameOrderByDateTimeLockDesc(String modelName, String groupName, String stationName);

    @Modifying
    @Transactional
    @Query("UPDATE B06TestLock SET employee = :employee, dateTimeUnlock = :dateTimeUnlock WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND dateTimeUnlock IS NULL")
    void unlock(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("employee") String employee, @Param("dateTimeUnlock") Timestamp dateTimeUnlock);
}
