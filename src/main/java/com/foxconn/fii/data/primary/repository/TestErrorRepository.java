package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestErrorRepository extends JpaRepository<TestError, Integer> {

    List<TestError> findAllByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndGroupNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String groupName, String errorCode, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndErrorCodeAndStartDateAndEndDate(String factory, String modelName, String errorCode, Date startDate, Date endDate);

    List<TestError> findAllByFactoryAndModelNameAndErrorCodeAndStartDateBetween(String factory, String modelName, String errorCode, Date startDate, Date endDate);
}
