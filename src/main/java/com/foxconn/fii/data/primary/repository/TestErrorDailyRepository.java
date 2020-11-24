package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TestErrorDailyRepository extends JpaRepository<TestErrorDaily, Integer> {

    List<TestErrorDaily> findAllByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndGroupNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String groupName, String errorCode, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndErrorCodeAndStartDateAndEndDate(String factory, String modelName, String errorCode, Date startDate, Date endDate);

    List<TestErrorDaily> findAllByFactoryAndModelNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String errorCode, Date startDate, Date endDate);

    @Query("SELECT DISTINCT t.modelName FROM TestErrorDaily t WHERE t.factory = :factory AND t.startDate BETWEEN :startDate AND :endDate")
    List<String> findDistinctModelNameByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    @Query("SELECT DISTINCT t.groupName FROM TestErrorDaily t WHERE t.factory = :factory AND t.modelName = :modelName AND t.startDate BETWEEN :startDate AND :endDate")
    List<String> findDistinctGroupNameByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    @Query("SELECT DISTINCT t.stationName FROM TestErrorDaily t WHERE t.factory = :factory AND t.modelName = :modelName AND t.groupName = :groupName AND t.startDate BETWEEN :startDate AND :endDate")
    List<String> findDistinctStationNameByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);
}
