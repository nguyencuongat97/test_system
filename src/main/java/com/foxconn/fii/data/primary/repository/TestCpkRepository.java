package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestCpk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestCpkRepository extends JpaRepository<TestCpk, Long> {

    List<TestCpk> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndParameterAndStartDateBetween(
            String factory, String modelName, String groupName, String stationName, String parameter, Date start, Date end);

    List<TestCpk> findByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date start, Date end);

    // BEGIN Giang modified
    @Query( "SELECT t1 AS cpk, t2 AS spec " +
            "FROM TestCpk t1 " +
            "INNER JOIN TestParameter t2 " +
            "ON (t1.factory = t2.factory AND t1.modelName = t2.modelName AND t1.parameter = t2.parameters AND t1.groupName = t2.groupName) " +
            "WHERE t1.startDate = :startDate " +
            "AND t1.endDate = :endDate " +
            "AND t1.factory = :factory " +
            "AND t1.modelName = :modelName " +
            "AND t1.groupName = :groupName " +
            "AND t1.stationName = :stationName " +
            "ORDER BY t1.parameter ASC "
    )
    List<Map<String, Object>> jpqlGetCpkAndSpec(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    @Query( "SELECT t1 AS cpk, t2 AS spec " +
            "FROM TestCpk t1 " +
            "INNER JOIN TestParameter t2 " +
            "ON (t1.factory = t2.factory AND t1.modelName = t2.modelName AND t1.parameter = t2.parameters AND t1.groupName = t2.groupName) " +
            "WHERE t1.startDate = :startDate " +
            "AND t1.endDate = :endDate " +
            "AND t1.factory = :factory " +
            "AND t1.modelName = :modelName " +
            "AND t1.groupName is null " +
            "AND t1.stationName = :stationName " +
            "ORDER BY t1.parameter ASC "
    )
    List<Map<String, Object>> jpqlGetCpkAndSpecGroupNameIsNull(String factory, String modelName, String stationName, Date startDate, Date endDate);
    // END Giang modified
}
