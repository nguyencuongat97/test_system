package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestPartErrorStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestPartErrorStatisticsRepository extends JpaRepository<TestPartErrorStatistics, Integer>  {

    List<TestPartErrorStatistics> findByFactoryAndWorkDateBetween(String factory, Date startDate, Date endDate);

    TestPartErrorStatistics findByFactoryAndModelNameAndPartNumberAndWorkDateAndShiftType(String factory, String modelName, String partNumber, Date workDate, ShiftType shiftType);
}
