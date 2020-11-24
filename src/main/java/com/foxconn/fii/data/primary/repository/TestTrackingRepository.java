package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TestTrackingRepository extends JpaRepository<TestTracking, Long> {

    List<TestTracking> findAllByStatus(TestTracking.Status status);


    List<TestTracking> findAllByCreatedAtBetween(Date startDate, Date endDate);

    List<TestTracking> findAllByFactoryAndCreatedAtBetween(String factory, Date startDate, Date endDate);

    List<TestTracking> findAllByFactoryAndStatusAndCreatedAtBetween(String factory, TestTracking.Status status, Date startDate, Date endDate);

    List<TestTracking> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndCreatedAtBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);


    List<TestTracking> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStatusAndCreatedAtBetween(String factory, String modelName, String groupName, String stationName, TestTracking.Status status, Date startDate, Date endDate);


    List<TestTracking> findAllByEmployeeAndCreatedAtBetween(String employee, Date startDate, Date endDate);

    @Query("SELECT factory, startDate, endDate, count(*) as count FROM TestTracking WHERE createdAt BETWEEN :startDate AND :endDate GROUP BY factory, startDate, endDate")
    List<Object[]> countByFactory(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
