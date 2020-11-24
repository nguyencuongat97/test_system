package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TestStationDailyRepository extends JpaRepository<TestStationDaily, Integer> {

    List<TestStationDaily> findAllByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    List<TestStationDaily> findAllByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    List<TestStationDaily> findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);

    List<TestStationDaily> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);


    @Query("SELECT stationName FROM TestStation WHERE factory = :factory AND modelName = :modelName AND groupName = :groupName AND startDate BETWEEN :startDate AND :endDate GROUP BY stationName")
    List<String> countOnline(@Param("factory") String factory, @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT stationName FROM TestStation WHERE factory = :factory AND modelName = :modelName AND startDate BETWEEN :startDate AND :endDate GROUP BY stationName")
    List<String> countOnline(@Param("factory") String factory, @Param("modelName") String modelName, @Param("startDate") Date startDate, @Param("startDate") Date endDate);

}
