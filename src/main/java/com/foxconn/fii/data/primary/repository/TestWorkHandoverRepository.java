package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestWorkHandover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TestWorkHandoverRepository extends JpaRepository<TestWorkHandover, Integer> {

    List<TestWorkHandover> findByFactory(String factory);

    List<TestWorkHandover> findByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    Optional<TestWorkHandover> findByFactoryAndLineNameAndModelNameAndGroupNameAndOwnerAndWorkDateAndShift(String factory, String lineName, String modelName, String groupName, String owner, Date workDate, ShiftType shiftType);
}
