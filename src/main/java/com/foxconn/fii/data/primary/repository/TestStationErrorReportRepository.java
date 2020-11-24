package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationErrorReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestStationErrorReportRepository extends JpaRepository<TestStationErrorReport, Integer> {
    List<TestStationErrorReport>findByFactory(String factory);

    TestStationErrorReport findTop1ByFactoryAndModelNameAndErrorCode(String factory, String modelName, String error);

    TestStationErrorReport findTop1ByFactoryAndModelNameAndGroupNameAndStationName(String factory, String modelName, String groupName, String stationName);

    List<TestStationErrorReport> findByFactoryAndModelNameAndGroupNameAndStartDateBetween(String factory, String modelName, String groupName, Date startDate, Date endDate);
}
