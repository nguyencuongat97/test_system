package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestPcEmailList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestPcEmailListRepository extends JpaRepository<TestPcEmailList, Integer> {

    List<TestPcEmailList> findByFactoryAndDepartment(String factory, String department);

    List<TestPcEmailList> findByFactoryAndDepartmentAndGroupName(String factory, String department, String group);

    TestPcEmailList findByFactoryAndDepartmentAndGroupNameAndEmail(String factory, String department, String group, String email);
}
