package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestMaintainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TestMaintainScheduleRepository extends JpaRepository<TestMaintainSchedule, Integer> {

    List<TestMaintainSchedule> findAllByFactoryAndNextTime(String factory, Date nextTime);
}
