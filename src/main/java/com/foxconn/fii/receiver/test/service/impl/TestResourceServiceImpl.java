package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.b04.model.B04Resource;
import com.foxconn.fii.data.b04.repository.B04ResourceRepository;
import com.foxconn.fii.data.b06.model.B06Resource;
import com.foxconn.fii.data.b06.repository.B06ResourceRepository;
import com.foxconn.fii.data.primary.model.TrackingResponse;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.repository.TestResourceRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingHistoryRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingRepository;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestResourceServiceImpl implements TestResourceService {

    @Autowired
    private TestResourceRepository testResourceRepository;

    @Autowired
    private B04ResourceRepository b04ResourceRepository;

    @Autowired
    private B06ResourceRepository b06ResourceRepository;

    @Autowired
    private TestTrackingRepository testTrackingRepository;

    @Autowired
    private TestTrackingHistoryRepository testTrackingHistoryRepository;

    @Override
    public TestResource findById(int id) {
        return testResourceRepository.findById(id).orElse(null);
    }

    @Override
    public TestResource findByEmployeeNo(String employeeNo) {
        if (StringUtils.isEmpty(employeeNo)) {
            return null;
        }

        List<TestResource> resourceList = testResourceRepository.findAllByEmployeeNo(employeeNo);
        if (!resourceList.isEmpty()) {
            return resourceList.get(0);
        }

        B04Resource b04Resource = b04ResourceRepository.findByEmployeeNo(employeeNo);
        if (b04Resource != null)
        return B04Resource.toTestResource(b04Resource);

//        B06Resource b06Resource = b06ResourceRepository.findByEmployeeNo(employeeNo);
//        return b06Resource != null ? B06Resource.toTestResource(b06Resource) : null;
        return null;
    }

    @Override
    public List<TestResource> findByFactory(String factory) {
        return testResourceRepository.findAllByFactory(factory);
    }

    @Override
    public List<TestResource> findByFactoryAndDem(String factory, String dem) {
        return testResourceRepository.findAllByFactoryAndDem(factory, dem);
    }

    @Override
    public List<TestResource> findByFactoryAndGroupLevel(String factory, int level) {
        return testResourceRepository.findAllByFactoryAndGroupLevel(factory, level);
    }

    @Override
    public List<TestResource> findByFactoryAndGroupLevel(String factory, int level, Integer solutionId, Date startDate, Date endDate) {
        Integer[] tasks = {10, 5, 6, 14, 3, 9, 7, 10, 12, 1, 4, 7, 8, 7, 9, 13, 10};
        Integer[] success = {9, 5, 3, 7, 3, 7, 3, 6, 12, 0, 3, 5, 7, 7, 4, 9, 9};
        Integer[] times = {60, 30, 20, 120, 30, 50, 70, 100, 150, 30, 60, 100, 150, 100, 120, 180};
        int i = 0;

        Map<String, List<TrackingResponse>> trackingMap;
        Map<Long, TrackingResponse> result = new HashMap<>();
        testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(factory, startDate, endDate)
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });
        trackingMap = result.values().stream()
                .filter(tracking -> tracking.getEmployee() != null)
                .sorted(Comparator.comparing(TrackingResponse::getUpdatedAt, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TrackingResponse::getEmployee));

        List<TestResource> resources = testResourceRepository.findAllByFactoryAndGroupLevel(factory, level);
        for (TestResource resource : resources) {
            List<TrackingResponse> trackingList = trackingMap.get(resource.getEmployeeNo());
            if (trackingList != null && !trackingList.isEmpty()) {
                resource.setTaskNumber(trackingList.size());

                long processingTime = 0;
                int taskSuccess = 0;
                for (TrackingResponse tracking : trackingList) {
                    if (tracking.getConfirmedAt() != null && tracking.getAssignedAt() != null) {
                        processingTime += (tracking.getConfirmedAt().getTime() - tracking.getAssignedAt().getTime()) / (60 * 1000);
                    }

                    if (tracking.getStatus() == TestTracking.Status.CLOSED) {
                        taskSuccess ++;
                    }
                }

                resource.setProcessingTime(trackingList.size() > 0 ? processingTime/trackingList.size() : 0);
                resource.setTaskSuccess(taskSuccess);
                resource.setTracking(trackingList.get(0));
            }

            if (trackingMap.isEmpty()) {
                resource.setTaskNumber(solutionId == null ? tasks[i] : tasks[i] / 2);
                resource.setTaskSuccess(solutionId == null ? success[i] : success[i] / 2);
                resource.setProcessingTime(solutionId == null ? times[i] : times[i] / 2);
                i++;
                if (i > 15) {
                    i = 0;
                }
            }
        }

        return resources;
    }

    @Override
    public List<TestResource> getResourceStatus(String factory) {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);

        Map<String, List<TrackingResponse>> trackingMap;
        Map<Long, TrackingResponse> result = new HashMap<>();

        testTrackingHistoryRepository.findAllByFactoryAndActionAtBetween(factory, timeSpan.getStartDate(), timeSpan.getEndDate())
                .forEach(history -> {
                    TrackingResponse tracking = result.getOrDefault(history.getTracking().getId(), TrackingResponse.of(history.getTracking()));

                    TrackingResponse.loadHistory(tracking, history);

                    result.put(history.getTracking().getId(), tracking);
                });
        trackingMap = result.values().stream()
                .filter(tracking -> tracking.getEmployee() != null)
                .sorted(Comparator.comparing(TrackingResponse::getUpdatedAt, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TrackingResponse::getEmployee));

        List<TestResource> resources = testResourceRepository.findAllByFactoryAndGroupLevelAndShift(factory, 1, timeSpan.getShiftType());
        for (TestResource resource : resources) {
            List<TrackingResponse> trackingList = trackingMap.get(resource.getEmployeeNo());
            if (trackingList != null && !trackingList.isEmpty()) {
                resource.setTaskNumber(trackingList.size());

                long processingTime = 0;
                int taskSuccess = 0;
                for (TrackingResponse tracking : trackingList) {
                    if (tracking.getConfirmedAt() != null && tracking.getAssignedAt() != null) {
                        processingTime += (tracking.getConfirmedAt().getTime() - tracking.getAssignedAt().getTime()) / (60 * 1000);
                    }

                    if (tracking.getStatus() == TestTracking.Status.CLOSED) {
                        taskSuccess ++;
                    }
                }

                resource.setProcessingTime(trackingList.size() > 0 ? processingTime/trackingList.size() : 0);
                resource.setTaskSuccess(taskSuccess);
                TrackingResponse tracking = trackingList.get(0);
                if (tracking.isValidated()) {
                    resource.setTracking(tracking);
                }
            }
        }

        return resources;
    }

    @Override
    public TestResource save(TestResource resource) {
        return testResourceRepository.save(resource);
    }

    @Override
    public void remove(int id) {
        testResourceRepository.deleteById(id);
    }
}
