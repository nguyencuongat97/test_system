package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTaskDaily;
import com.foxconn.fii.data.primary.model.entity.TestTaskDailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface TestTaskDailyHistoryRepository extends JpaRepository<TestTaskDailyHistory, Long> {

    List<TestTaskDailyHistory> findByFactoryAndCreatedAtBetween(String factory, Date startDate, Date endDate);

    List<TestTaskDailyHistory> findByFactoryAndEmployeeIdAndCreatedAtBetween(String factory, String employeeId, Date startDate, Date endDate);

}
