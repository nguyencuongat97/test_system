//package com.foxconn.fii.data.b06ds02.repository;
//
//import com.foxconn.fii.data.b06ds02.model.B06TestLog;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public interface B06TestLogRepository extends JpaRepository<B06TestLog, Long> {
//
//    List<B06TestLog> findAllByModelNameAndTestItemAndDateTimeBetween(String modelName, String testItem, Timestamp startDate, Timestamp endDate);
//
//    List<B06TestLog> findAllByModelNameAndGroupNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String testItem, Timestamp startDate, Timestamp endDate);
//
//    List<B06TestLog> findAllByModelNameAndGroupNameAndStationNameAndTestItemAndDateTimeBetween(String modelName, String groupName, String stationName, String testItem, Timestamp startDate, Timestamp endDate);
//
//    List<B06TestLog> findAllByModelNameAndGroupNameAndStationNameAndDateTimeBetween(String modelName, String groupName, String stationName, Timestamp startDate, Timestamp endDate);
//
//    @Query("SELECT DISTINCT modelName as modelName, testItem as parameter, lLimit as lLimit, hLimit as hLimit FROM B06TestLog WHERE dateTime BETWEEN :startDate AND :endDate")
//    List<Map<String, String>> getParameters(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
//
//    List<B06TestLog> findAllByModelNameAndIdGreaterThanAndDateTimeBetween(String modelName, Long syncedLatestId, Date startDate, Date endDate, Pageable pageable);
//
//    Page<B06TestLog> findAllByModelNameAndIdGreaterThan(String modelName, Long syncedLatestId, Pageable pageable);
//}
