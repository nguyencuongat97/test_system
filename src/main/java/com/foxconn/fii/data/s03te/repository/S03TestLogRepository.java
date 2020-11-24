package com.foxconn.fii.data.s03te.repository;

import com.foxconn.fii.data.s03te.model.S03TestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface S03TestLogRepository extends JpaRepository<S03TestLog, Long> {

    List<S03TestLog> findAllByModelNameAndTestItemAndDateTimeBetween(String modelName, String testItem, Timestamp startDate, Timestamp endDate);

    List<S03TestLog> findAllByModelNameAndGroupNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String testItem, Timestamp startDate, Timestamp endDate);

    List<S03TestLog> findAllByModelNameAndGroupNameAndStationNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String stationName, String testItem, Timestamp startDate, Timestamp endDate);

    List<S03TestLog> findAllByModelNameAndGroupNameAndStationNameAndDateTimeBetween(String modelName, String groupName, String stationName, Timestamp startDate, Timestamp endDate);

    @Query("SELECT DISTINCT modelName as modelName, testItem as parameter, lLimit as lLimit, hLimit as hLimit FROM S03TestLog WHERE dateTime BETWEEN :startDate AND :endDate")
    List<Map<String, String>> getParameters(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<S03TestLog> findAllByModelNameAndIdGreaterThanAndDateTimeBetween(String modelName, Long syncedLatestId, Date startDate, Date endDate, Pageable pageable);

    Page<S03TestLog> findAllByModelNameAndIdGreaterThan(String modelName, Long syncedLatestId, Pageable pageable);

    @Query("SELECT pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND testItem = :testItem AND workDate BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("testItem") String testItem, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND testItem = :testItem AND workDate BETWEEN :startDate AND :endDate")
    List<Double> findPValueByModelNameAndGroupNameAndStationNameAndTestItemAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("testItem") String testItem, @Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query("SELECT testItem, pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND workDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndFullWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND workDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndStationNameAndWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT testItem, pValue FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<Object[]> findByModelNameAndGroupNameAndStationNameAndFullWorkDateBetween(
            @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query("SELECT DISTINCT modelName FROM S03TestLog WHERE fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findModelNameByFullWorkDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT groupName FROM S03TestLog WHERE modelName = :modelName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findGroupNameByModelNameFullWorkDateBetween(@Param("modelName") String modelName, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT DISTINCT stationName FROM S03TestLog WHERE modelName = :modelName AND groupName = :groupName AND fullWorkDate BETWEEN :startDate AND :endDate")
    List<String> findStationNameByModelNameAndGroupNameFullWorkDateBetween(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
