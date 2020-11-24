package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.model.entity.TestPlanTmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestPlanTmpRepository extends JpaRepository<TestPlanTmp, Integer> {

    List<TestPlanTmp> findByFactoryAndTypeAndStartDateBetween(String factory, TestPlanTmp.Type type, Date startDate, Date endDate);

    List<TestPlanTmp> findByFactoryAndSectionNameAndTypeAndStartDateBetween(String factory, String sectionName, TestPlanTmp.Type type, Date startDate, Date endDate);

    List<TestPlanTmp> findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(String factory, String sectionName, String modelName, TestPlanTmp.Type type, Date startDate, Date endDate);

    TestPlanTmp findTop1ByFactoryAndModelNameAndShiftAndShiftTimeAndSectionNameOrderByIdDesc(String factory, String modelName, ShiftType shiftType, Date shiftTime, String sectionName);

    TestPlanTmp findById(int id);

    List<TestPlanTmp> findByFactoryAndMo(String factory, String mo);

    List<TestPlanTmp> findByFactoryAndMoAndStartDateBetweenOrderByStartDateDesc(String factory, String mo, Date startDate, Date endDate);
}
