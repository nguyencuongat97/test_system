package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestPlanMetaRepository extends JpaRepository<TestPlanMeta, Integer> {

    List<TestPlanMeta> findByFactoryAndTypeAndStartDateBetween(String factory, TestPlanMeta.Type type, Date startDate, Date endDate);

    List<TestPlanMeta> findByFactoryAndSectionNameAndTypeAndStartDateBetween(String factory, String sectionName, TestPlanMeta.Type type, Date startDate, Date endDate);

    List<TestPlanMeta> findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(String factory, String sectionName, String modelName, TestPlanMeta.Type type, Date startDate, Date endDate);

    TestPlanMeta findTop1ByFactoryAndModelNameAndShiftAndShiftTimeAndSectionNameOrderByIdDesc(String factory, String modelName, ShiftType shiftType, Date shiftTime, String sectionName);

    TestPlanMeta findById(int id);

    List<TestPlanMeta> findByFactoryAndMo(String factory, String mo);

    List<TestPlanMeta> findByFactoryAndMoAndStartDateBetweenOrderByStartDateDesc(String factory, String mo, Date startDate, Date endDate);
}
