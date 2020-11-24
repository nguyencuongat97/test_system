package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResourceRepository extends JpaRepository<TestResource, Integer> {

    List<TestResource> findAllByShift(ShiftType shift);

    List<TestResource> findAllByEmployeeNo(String employeeNo);

    List<TestResource> findAllByDem(String dem);

    List<TestResource> findAllByFactory(String factory);

    List<TestResource> findAllByFactoryAndDem(String factory, String dem);

    List<TestResource> findAllByFactoryAndGroupLevel(String factory, int groupLevel);

    List<TestResource> findAllByFactoryAndGroupLevelAndShift(String factory, int groupLevel, ShiftType shiftType);
}
