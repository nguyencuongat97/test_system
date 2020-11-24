package com.foxconn.fii.data.nbbtefii.repository;

import com.foxconn.fii.data.nbbtefii.model.NbbTestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NbbTestLogRepository extends JpaRepository<NbbTestLog, Long> {

    List<NbbTestLog> findAllByModelNameAndTestItemAndDateTimeBetween(String modelName, String testItem, Timestamp startDate, Timestamp endDate);

    List<NbbTestLog> findAllByModelNameAndGroupNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String testItem, Timestamp startDate, Timestamp endDate);

    List<NbbTestLog> findAllByModelNameAndGroupNameAndStationNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String stationName, String testItem, Timestamp startDate, Timestamp endDate);

    List<NbbTestLog> findAllByModelNameAndGroupNameAndStationNameAndDateTimeBetween(String modelName, String groupName, String stationName, Timestamp startDate, Timestamp endDate);

    @Query("SELECT DISTINCT modelName as modelName, testItem as parameter, lLimit as lLimit, hLimit as hLimit FROM NbbTestLog WHERE dateTime BETWEEN :startDate AND :endDate")
    List<Map<String, String>> getParameters(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<NbbTestLog> findAllByModelNameAndIdGreaterThanAndDateTimeBetween(String modelName, Long syncedLatestId, Date startDate, Date endDate, Pageable pageable);

    Page<NbbTestLog> findAllByModelNameAndIdGreaterThan(String modelName, Long syncedLatestId, Pageable pageable);

    @Query("SELECT pValue FROM NbbTestLog WHERE modelName = :modelName AND groupName = :groupName AND testItem = :testItem AND dateTime BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("testItem") String testItem, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT pValue FROM NbbTestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND testItem = :testItem AND dateTime BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndStationNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("testItem") String testItem, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT testItem, pValue FROM NbbTestLog WHERE modelName = :modelName AND groupName = :groupName AND dateTime BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT testItem, pValue FROM NbbTestLog WHERE customer = :customer AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByCustomerAndGroupNameAndFullWorkDateBetween(
            @Param("customer") String customer, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM NbbTestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND dateTime BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndStationNameAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT testItem, pValue FROM NbbTestLog WHERE customer = :customer AND groupName = :groupName AND stationName = :stationName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByCustomerAndGroupNameAndStationNameAndFullWorkDateBetween(
            @Param("customer") String customer, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT customer FROM NbbTestLog WHERE fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findCustomerByFullWorkDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT groupName FROM NbbTestLog WHERE customer = :customer AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findGroupNameByCustomerFullWorkDateBetween(@Param("customer") String customer, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT stationName FROM NbbTestLog WHERE customer = :customer AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findStationNameByCustomerAndGroupNameFullWorkDateBetween(@Param("customer") String customer, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
