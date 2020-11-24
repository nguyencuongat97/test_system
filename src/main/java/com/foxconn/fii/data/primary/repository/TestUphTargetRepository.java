package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestUphTargetRepository extends JpaRepository<TestUphTarget, Integer> {

    List<TestUphTarget> findByFactory(String factory);

    List<TestUphTarget> findByFactoryAndCustomer(String factory, String customer);

    List<TestUphTarget> findByFactoryAndCustomerAndModelName(String factory, String customer, String modelName);

    List<TestUphTarget> findByFactoryAndWorkDateAndShiftTypeAndModelName(String factory, Date workDate, ShiftType shiftType, String modelName);

    List<TestUphTarget> findByFactoryAndWorkDateAndShiftTypeAndCustomer(String factory, Date workDate, ShiftType shiftType, String customer);

    List<TestUphTarget> findByFactoryAndWorkDateAndShiftTypeAndCustomerAndLineNameAndModelNameAndGroupName(String factory, Date workDate, ShiftType shiftType, String customer, String lineName, String modelName, String groupName);

    List<TestUphTarget> findByFactoryAndCustomerAndLineNameAndModelNameAndGroupName(String factory, String customer, String lineName, String modelName, String groupName);

    List<TestUphTarget> findByFactoryAndCustomerAndLineNameAndModelNameAndGroupNameAndTime(String factory, String customer, String lineName, String modelName, String groupName, String time);
}
