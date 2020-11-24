package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTaskDailyConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestTaskDailyConfirmRepository extends JpaRepository<TestTaskDailyConfirm, Long> {

    List<TestTaskDailyConfirm> findByFactoryAndInputDateBetween(String factory, Date startDate, Date endDate);

    List<TestTaskDailyConfirm> findByFactoryAndResourceGroupAndInputDateBetween(String factory, String resourceGroup, Date startDate, Date endDate);

    List<TestTaskDailyConfirm> findByFactoryAndEmployeeIdAndInputDateBetween(String factory, String employeeId, Date startDate, Date endDate);
}
