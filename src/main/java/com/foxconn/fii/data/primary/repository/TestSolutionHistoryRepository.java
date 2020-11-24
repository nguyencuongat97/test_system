package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestSolutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TestSolutionHistoryRepository extends JpaRepository<TestSolutionHistory, Long> {

    List<TestSolutionHistory> findByFactoryAndModelNameAndGroupNameAndStationNameAndStartDateBetween(
            String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    Optional<TestSolutionHistory> findByFactoryAndModelNameAndGroupNameAndStationNameAndErrorCodeAndStartDateAndEndDate(
            String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate);
}
