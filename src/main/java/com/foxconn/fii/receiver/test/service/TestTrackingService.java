package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.ConfirmTaskRequest;
import com.foxconn.fii.data.primary.model.entity.TestTracking;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestTrackingService {

    void save(TestTracking tracking);

    void saveAll(List<TestTracking> trackingList);

    Map<String, TestTracking> getValidatedTrackingMap(String factory, Date startDate, Date endDate);

    Map<String, TestTracking> getClosedTrackingMap(String factory, Date startDate, Date endDate);

    TestTracking getTracking(long trackingId);

    TestTracking getTracking(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);


    List<TestTracking> getTrackingList(TestTracking.Status status);

    List<TestTracking> getTrackingList(String factory, TestTracking.Status status, Date startDate, Date endDate);

    List<TestTracking> getTrackingList(String factory, String modelName, String groupName, String stationName, TestTracking.Status status, Date startDate, Date endDate);


    List<TestTracking> getTrackingList(Date startDate, Date endDate);

    List<TestTracking> getTrackingList(String factory, Date startDate, Date endDate);

    List<TestTracking> getTrackingList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestTracking> getTrackingListByEmployee(String employee, Date startDate, Date endDate);


    Map<String, Map<String, Integer>> countNotificationByFactory(Date startDate, Date endDate);


    /** LOGIC */

    String handleTask(Long trackingId, String employee);

    String arriveTask(Long trackingId, String employee);

    String confirmTask(ConfirmTaskRequest solutionRequest);

    String giveUpTask(Long trackingId, String employee);
}
