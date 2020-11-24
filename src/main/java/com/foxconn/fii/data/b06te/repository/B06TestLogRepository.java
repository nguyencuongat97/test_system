package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.B06TestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface B06TestLogRepository extends JpaRepository<B06TestLog, Long> {

    List<B06TestLog> findAllByModelNameAndTestItemAndDateTimeBetween(String modelName, String testItem, Timestamp startDate, Timestamp endDate);

    List<B06TestLog> findAllByModelNameAndGroupNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String testItem, Timestamp startDate, Timestamp endDate);

    List<B06TestLog> findAllByModelNameAndGroupNameAndStationNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String stationName, String testItem, Timestamp startDate, Timestamp endDate);

    List<B06TestLog> findAllByModelNameAndGroupNameAndStationNameAndDateTimeBetween(String modelName, String groupName, String stationName, Timestamp startDate, Timestamp endDate);

    @Query("SELECT DISTINCT modelName as modelName, testItem as parameter, lLimit as lLimit, hLimit as hLimit FROM B06TestLog WHERE dateTime BETWEEN :startDate AND :endDate")
    List<Map<String, String>> getParameters(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<B06TestLog> findAllByModelNameAndIdGreaterThanAndDateTimeBetween(String modelName, Long syncedLatestId, Date startDate, Date endDate, Pageable pageable);

    Page<B06TestLog> findAllByModelNameAndIdGreaterThan(String modelName, Long syncedLatestId, Pageable pageable);

    @Query("SELECT pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND testItem = :testItem AND workDate BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("testItem") String testItem, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND testItem = :testItem AND workDate BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndStationNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("testItem") String testItem, @Param("startDate") String startDate, @Param("endDate") String endDate);


//    @Query("SELECT testItem, pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND workDate BETWEEN :startDate AND :endDate")
//    List<Object[]> findByModelNameAndGroupNameAndWorkDateBetween(
//            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndFullWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate AND testItem IN :itemList")
    List<Object[]> findByModelNameAndGroupNameAndFullWorkDateBetweenAndParametersIn(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("itemList") List<String> itemList);

//    @Query("SELECT testItem, pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND workDate BETWEEN :startDate AND :endDate")
//    List<Object[]> findByModelNameAndGroupNameAndStationNameAndWorkDateBetween(
//            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndStationNameAndFullWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query("SELECT DISTINCT modelName FROM B06TestLog WHERE fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findModelNameByFullWorkDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT groupName FROM B06TestLog WHERE modelName = :modelName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findGroupNameByModelNameFullWorkDateBetween(@Param("modelName") String modelName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT stationName FROM B06TestLog WHERE modelName = :modelName AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findStationNameByModelNameAndGroupNameFullWorkDateBetween(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
