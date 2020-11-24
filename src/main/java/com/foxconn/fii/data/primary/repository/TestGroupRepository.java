package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestGroupRepository extends JpaRepository<TestGroup, Integer> {

    TestGroup findByFactoryAndModelNameAndGroupNameAndStartDateAndEndDate(String factory, String modelName, String groupName, Date startDate, Date endDates);

 ///   List<Map<String, Object>> findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);

    List<TestGroup> findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);

    List<TestGroup> findAllByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    List<TestGroup> findAllByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    @Query(value = "SELECT  model_name, group_name, pass FROM test_group " +
            "WHERE model_name = :modelName AND group_name = :groupName AND " +
            "start_date BETWEEN :startDate AND :endDate AND factory = :factory", nativeQuery = true)
    List<Object[]> getByFactoryAndModelNameAndGroupNameAndStartDateBetween(@Param("factory") String factory,@Param("modelName") String modelName,@Param("groupName") String groupName,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

}
