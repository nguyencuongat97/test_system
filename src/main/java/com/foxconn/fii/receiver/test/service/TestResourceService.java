package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestResource;

import java.util.Date;
import java.util.List;

public interface TestResourceService {

    TestResource findById(int id);

    TestResource findByEmployeeNo(String employeeNo);

    List<TestResource> findByFactory(String factory);

    List<TestResource> findByFactoryAndDem(String factory, String dem);

    List<TestResource> findByFactoryAndGroupLevel(String factory, int level);

    List<TestResource> findByFactoryAndGroupLevel(String factory, int level, Integer solutionId, Date startDate, Date endDate);

    List<TestResource> getResourceStatus(String factory);

    TestResource save(TestResource resource);

    void remove(int id);
}
