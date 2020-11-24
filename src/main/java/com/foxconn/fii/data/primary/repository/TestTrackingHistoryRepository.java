package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TestTrackingHistoryRepository extends JpaRepository<TestTrackingHistory, Long> {

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE t.id = ?1")
    List<TestTrackingHistory> findAllByTrackingId(Long trackingId);

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE th.employee = ?1 AND th.actionAt BETWEEN ?2 AND ?3")
    List<TestTrackingHistory> findAllByEmployeeAndActionAtBetween(String employee, Date startDate, Date endDate);

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE t.factory = ?1 AND th.actionAt BETWEEN ?2 AND ?3")
    List<TestTrackingHistory> findAllByFactoryAndActionAtBetween(String factory, Date startDate, Date endDate);

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE t.factory = ?1 AND t.modelName = ?2 AND th.actionAt BETWEEN ?3 AND ?4")
    List<TestTrackingHistory> findAllByFactoryAndModelNameAndActionAtBetween(String factory, String modelName, Date startDate, Date endDate);

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE t.factory = ?1 AND t.modelName = ?2 AND t.groupName = ?3  AND t.stationName = ?4 AND th.actionAt BETWEEN ?5 AND ?6")
    List<TestTrackingHistory> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndActionAtBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    @Query("SELECT th FROM TestTrackingHistory th JOIN FETCH th.tracking t WHERE t.factory = ?1 AND t.modelName in ?2 AND th.actionAt BETWEEN ?3 AND ?4")
    List<TestTrackingHistory> findAllByFactoryAndModelNameInAndActionAtBetween(String factory, List<String> modelList, Date startDate, Date endDate);
}
