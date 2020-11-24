package com.foxconn.fii.data.nbbtefii.repository;

import com.foxconn.fii.data.nbbtefii.model.NbbTestLogData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NbbTestLogDataRepository extends JpaRepository<NbbTestLogData, Long> {

    @Query(value = "SELECT model AS model_name, station AS group_name, " +
            "sum(elapse_time) AS total_cycle, count(*) as total_number, " +
            "sum(case when status = 'PASS' then elapse_time else 0 end) AS pass_cycle, sum(case when status = 'PASS' then 1 else 0 end) AS pass_number, " +
            "sum(case when status = 'PASS' then 0 else elapse_time end) AS fail_cycle, sum(case when status = 'PASS' then 0 else 1 end) AS fail_number " +
            "FROM TE_TEST_DATA " +
            "WHERE test_mode = 'ONLINE' AND start_time BETWEEN :startDate and :endDate " +
            "GROUP BY model, station", nativeQuery = true)
    List<Map<String, Object>> getCycleTimeByGroup(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT model AS model_name, station AS group_name, ate AS station_name, " +
            "sum(elapse_time) AS total_cycle, count(*) as total_number, " +
            "sum(case when status = 'PASS' then elapse_time else 0 end) AS pass_cycle, sum(case when status = 'PASS' then 1 else 0 end) AS pass_number, " +
            "sum(case when status = 'PASS' then 0 else elapse_time end) AS fail_cycle, sum(case when status = 'PASS' then 0 else 1 end) AS fail_number " +
            "FROM TE_TEST_DATA " +
            "WHERE model = :modelName AND station = :groupName AND test_mode = 'ONLINE' AND start_time BETWEEN :startDate and :endDate " +
            "GROUP BY model, station, ate", nativeQuery = true)
    List<Map<String, Object>> getCycleTimeByStation(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<NbbTestLogData> findByModelNameAndGroupNameAndErrorCodeAndStartDateBetween(String modelName, String groupName, String errorCode, Date startDate, Date endDate);

    List<NbbTestLogData> findByModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate);

}
