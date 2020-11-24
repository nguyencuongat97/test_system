package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestFixedHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestFixedHistoryRepository extends JpaRepository<TestFixedHistory, Long> {

    List<TestFixedHistory> findByFactoryAndModelNameAndCreatedAtBetween(String factory, String modelName, Date startDate, Date endDate, Pageable pageable);

    List<TestFixedHistory> findByFactoryAndModelNameAndGroupNameAndCreatedAtBetween(String factory, String modelName, String groupName, Date startDate, Date endDate, Pageable pageable);

    List<TestFixedHistory> findByFactoryAndModelNameAndGroupNameAndStationNameAndCreatedAtBetween(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, Pageable pageable);

    List<TestFixedHistory> findByStatus(TestFixedHistory.Status status);
}
