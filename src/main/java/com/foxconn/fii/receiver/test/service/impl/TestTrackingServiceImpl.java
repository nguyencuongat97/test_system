package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.ConfirmTaskRequest;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestTaskDaily;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.model.entity.TestTrackingHistory;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaRepository;
import com.foxconn.fii.data.primary.repository.TestTaskDailyRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingHistoryRepository;
import com.foxconn.fii.data.primary.repository.TestTrackingRepository;
import com.foxconn.fii.receiver.test.service.NotifyService;
import com.foxconn.fii.receiver.test.service.TestLockService;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.receiver.test.service.TestStationService;
import com.foxconn.fii.receiver.test.service.TestTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestTrackingServiceImpl implements TestTrackingService {

    @Autowired
    private TestTrackingRepository testTrackingRepository;

    @Autowired
    private TestTrackingHistoryRepository testTrackingHistoryRepository;

    @Autowired
    private TestSolutionMetaRepository testSolutionMetaRepository;

    @Autowired
    private TestResourceService testResourceService;

    @Autowired
    private TestStationService testStationService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private TestLockService testLockService;

    @Autowired
    private TestTaskDailyRepository testTaskDailyRepository;

    @Value("${path.data}")
    private String dataPath;

    @Override
    public void save(TestTracking tracking) {
        testTrackingRepository.save(tracking);
    }

    @Override
    public void saveAll(List<TestTracking> trackingList) {
        testTrackingRepository.saveAll(trackingList);
    }

    @Override
    public Map<String, TestTracking> getValidatedTrackingMap(String factory, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndCreatedAtBetween(factory, startDate, endDate)
                .stream().filter(TestTracking::isValidated)
                .collect(Collectors.toMap(
                        tracking -> tracking.getModelName() + "_" + tracking.getGroupName() + "_" + tracking.getStationName() + "_" + tracking.getType(),
                        tracking -> tracking,
                        (t1, t2) -> t1.getCreatedAt().after(t2.getCreatedAt()) ? t1 : t2,
                        HashMap::new));
    }

    @Override
    public Map<String, TestTracking> getClosedTrackingMap(String factory, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndCreatedAtBetween(factory, startDate, endDate)
                .stream().filter(tracking -> !tracking.isValidated())
                .collect(Collectors.toMap(
                        tracking -> tracking.getModelName() + "_" + tracking.getGroupName() + "_" + tracking.getStationName() + "_" + tracking.getType(),
                        tracking -> tracking,
                        (t1, t2) -> t1.getCreatedAt().after(t2.getCreatedAt()) ? t1 : t2,
                        HashMap::new));
    }

    @Override
    public TestTracking getTracking(long trackingId) {
        return testTrackingRepository.findById(trackingId).orElse(null);
    }

    @Override
    public TestTracking getTracking(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        List<TestTracking> trackingList = testTrackingRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndCreatedAtBetween(factory, modelName, groupName, stationName, startDate, endDate)
                .stream()
                .filter(tracking -> tracking.isValidated() && tracking.getType() != TestTracking.Type.WARNING_IDLE)
                .sorted(Comparator.comparing(TestTracking::isLockedType, Comparator.reverseOrder()).thenComparing(TestTracking::getCreatedAt, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return trackingList.isEmpty() ? null : trackingList.get(0);
    }

    @Override
    public List<TestTracking> getTrackingList(TestTracking.Status status) {
        return testTrackingRepository.findAllByStatus(status);
    }

    @Override
    public List<TestTracking> getTrackingList(String factory, TestTracking.Status status, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndStatusAndCreatedAtBetween(factory, status, startDate, endDate);
    }

    @Override
    public List<TestTracking> getTrackingList(String factory, String modelName, String groupName, String stationName, TestTracking.Status status, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndStatusAndCreatedAtBetween(factory, modelName, groupName, stationName, status, startDate, endDate);
    }

    @Override
    public List<TestTracking> getTrackingList(Date startDate, Date endDate) {
        return testTrackingRepository.findAllByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<TestTracking> getTrackingList(String factory, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndCreatedAtBetween(factory, startDate, endDate);
    }

    @Override
    public List<TestTracking> getTrackingList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByFactoryAndModelNameAndGroupNameAndStationNameAndCreatedAtBetween(factory, modelName, groupName, stationName, startDate, endDate);
    }

    @Override
    public List<TestTracking> getTrackingListByEmployee(String employee, Date startDate, Date endDate) {
        return testTrackingRepository.findAllByEmployeeAndCreatedAtBetween(employee, startDate, endDate);
    }


    @Override
    public Map<String, Map<String, Integer>> countNotificationByFactory(Date startDate, Date endDate) {
        Map<String, Map<String, Integer>> result = new HashMap<>();

        List<Object[]> data = testTrackingRepository.countByFactory(startDate, endDate);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        for (Object[] values : data) {
            String factory = (String) values[0];
            Date start = (Date) values[1];
            Date end = (Date) values[2];
            Number count = (Number) values[3];

            String key = df.format(start) + " - " + df.format(end);
            Map<String, Integer> dataOfDate = result.computeIfAbsent(key, k -> new HashMap<>());

            dataOfDate.put(factory, dataOfDate.getOrDefault(factory, 0) + count.intValue());
        }

        return result;
    }

    /**
     * LOGIC
     */
    @Override
    public String handleTask(Long trackingId, String employee) {
        TestTracking tracking = getTracking(trackingId);
        if (tracking != null && !StringUtils.isEmpty(employee) &&
                (tracking.getStatus() == TestTracking.Status.NOTIFIED || tracking.getStatus() == TestTracking.Status.TIMEOUT)) {

            TestResource resource = testResourceService.findByEmployeeNo(employee);
            if (resource == null) {
                log.error("### handleTask employee {} is not found", employee);
                return "failed";
            }

            TestTrackingHistory trackingHistory = new TestTrackingHistory();
            trackingHistory.setTracking(tracking);
            trackingHistory.setEmployee(employee.toUpperCase());
            trackingHistory.setAction(TestTracking.Status.ASSIGNED);
            testTrackingHistoryRepository.save(trackingHistory);

            tracking.setEmployee(employee);
            tracking.setStatus(TestTracking.Status.ASSIGNED);
            save(tracking);

            resource.setTrackingId(tracking.getId());
            resource.setStatus(TestResource.Status.ON_DUTY);
            testResourceService.save(resource);

            notifyService.notifyToIcivet(String.format("%s is handled by %s(%s)", tracking.getTrackingCode(), resource.getName(), employee), tracking);
            return "success";
        } else {
            return "failed";
        }
    }

    @Override
    public String arriveTask(Long trackingId, String employee) {
        TestTracking tracking = getTracking(trackingId);
        if (tracking != null && tracking.getStatus() == TestTracking.Status.ASSIGNED && !StringUtils.isEmpty(employee)) {
            TestResource resource = testResourceService.findByEmployeeNo(employee);
            if (resource == null) {
                log.error("### arriveTask employee {} is not found", employee);
                return "failed";
            }

            if (!employee.equalsIgnoreCase(tracking.getEmployee()) || tracking.getId() != resource.getTrackingId()) {
                log.error("### arriveTask employee {} is not handled task {}", employee, trackingId);
                return "failed";
            }

            TestTrackingHistory trackingHistory = new TestTrackingHistory();
            trackingHistory.setTracking(tracking);
            trackingHistory.setEmployee(employee.toUpperCase());
            trackingHistory.setAction(TestTracking.Status.ARRIVED);
            testTrackingHistoryRepository.save(trackingHistory);

            tracking.setStatus(TestTracking.Status.ARRIVED);
            testTrackingRepository.save(tracking);

            notifyService.notifyToIcivet(String.format("%s is started by %s(%s)", tracking.getTrackingCode(), resource.getName(), employee), tracking);

            testLockService.unlock(tracking.getFactory(), tracking.getModelName(), tracking.getGroupName(), tracking.getStationName(), employee);

            return "success";
        } else {
            return "failed";
        }
    }

    @Override
    public String confirmTask(ConfirmTaskRequest confirmRequest) {
        if (StringUtils.isEmpty(confirmRequest.getEmployee())) {
            log.error("### confirmTask employee blank");
            return "failed";
        }

        TestResource resource = testResourceService.findByEmployeeNo(confirmRequest.getEmployee());
        if (resource == null) {
            log.error("### confirmTask employee {} is not found", confirmRequest.getEmployee());
            return "failed";
        }

        if (confirmRequest.getTrackingId() == null) {
            log.error("### confirmTask tracking is null");
            return "failed";
        }

        TestTracking tracking = getTracking(confirmRequest.getTrackingId());
        if (tracking == null) {
            log.error("### confirmTask tracking {} not found", confirmRequest.getTrackingId());
            return "failed";
        }

        if (!tracking.isValidated() || (tracking.isLockedType() && tracking.getStatus() != TestTracking.Status.UNLOCKED)) {
            log.error("### confirmTask tracking {} status {}", confirmRequest.getTrackingId(), tracking.getStatus());
            return "failed";
        }

        TestSolutionMeta solutionMeta;
        if (confirmRequest.getSolutionId() != null) {
            solutionMeta = testSolutionMetaRepository.findById(confirmRequest.getSolutionId()).orElse(null);
            if (solutionMeta == null) {
                log.error("### confirmTask solution {} not found", confirmRequest.getSolutionId());
                return "failed";
            }
            testSolutionMetaRepository.increasingNumberUsed(confirmRequest.getSolutionId());
        } else {
            solutionMeta = new TestSolutionMeta();
            solutionMeta.setFactory(tracking.getFactory());
            solutionMeta.setModelName(tracking.getModelName());
            solutionMeta.setErrorCode(confirmRequest.getErrorCode());
            solutionMeta.setAuthor(confirmRequest.getEmployee());
            solutionMeta.setSolution(confirmRequest.getSolutionName());
            solutionMeta.setAction(confirmRequest.getAction());

            List<TestSolutionMeta.GuidingStep> steps = new ArrayList<>();
            for (int i = 0; confirmRequest.getCaptionFiles() != null && i < confirmRequest.getCaptionFiles().size(); i++) {
                MultipartFile uploadedFile = null;
                if (confirmRequest.getUploadingFiles() != null && i < confirmRequest.getUploadingFiles().size()) {
                    uploadedFile = confirmRequest.getUploadingFiles().get(i);
                }
                if (uploadedFile == null) {
                    steps.add(TestSolutionMeta.GuidingStep.of(confirmRequest.getCaptionFiles().get(i), ""));
                    continue;
                }

                String fileName = System.currentTimeMillis() + '-' + new Random().nextInt(100) + ".jpg";
                File file = new File(dataPath + "image/" + fileName);
                try {
                    uploadedFile.transferTo(file);
                } catch (IOException e) {
                    log.error("### upload error", e);
                }
                steps.add(TestSolutionMeta.GuidingStep.of(confirmRequest.getCaptionFiles().get(i), "/ws-data/image/" + fileName));
            }

            solutionMeta.setSteps(steps);
            solutionMeta.setNumberUsed(1);
            testSolutionMetaRepository.save(solutionMeta);
        }

        TestTrackingHistory trackingHistory = new TestTrackingHistory();
        trackingHistory.setTracking(tracking);
        trackingHistory.setEmployee(confirmRequest.getEmployee());
        trackingHistory.setAction(TestTracking.Status.CONFIRMED);
        trackingHistory.setSolution(solutionMeta);
        testTrackingHistoryRepository.save(trackingHistory);

        if (tracking.getType() == TestTracking.Type.WARNING_B) {
            TestStation station = testStationService.getStation(tracking.getFactory(), tracking.getModelName(), tracking.getGroupName(), tracking.getStationName(), tracking.getStartDate(), tracking.getEndDate(), MoType.ALL);
            if (station != null) {
                tracking.setWip(station.getWip());
                tracking.setFirstFail(station.getFirstFail());
                tracking.setSecondFail(station.getSecondFail());
            }
        }

        tracking.setEmployee(confirmRequest.getEmployee());
        tracking.setStatus(TestTracking.Status.CONFIRMED);
        testTrackingRepository.save(tracking);

        resource.setTrackingId(null);
        resource.setStatus(TestResource.Status.FREE);
        testResourceService.save(resource);

        if (confirmRequest.getAuto() == null || !confirmRequest.getAuto()) {
            TestTaskDaily taskDaily = new TestTaskDaily();
            taskDaily.setFactory(tracking.getFactory());
            taskDaily.setEmployeeId(resource.getEmployeeNo());
            taskDaily.setEmployeeName(resource.getName());
            taskDaily.setTaskTitle(String.format("%s | %s | %s <br>%s AT %s<br>%s", tracking.getModelName(), tracking.getGroupName(), tracking.getStationName(), tracking.getType().toString(), tracking.getCreatedAt(), StringUtils.isEmpty(tracking.getMessage()) ? "" : tracking.getMessage()));
            taskDaily.setTaskContent(String.format("ERROR CODE: %s <br>ROOT CAUSE: %s <br>ACTION: %s", solutionMeta.getErrorCode(), solutionMeta.getSolution(), solutionMeta.getAction()));
            taskDaily.setStatus(TestTaskDaily.Status.DONE);
            testTaskDailyRepository.save(taskDaily);
        }

        notifyService.notifyToIcivet(String.format("%s is confirmed by %s(%s)", tracking.getTrackingCode(), resource.getName(), confirmRequest.getEmployee()), tracking);

        return "success";
    }

    @Override
    public String giveUpTask(Long trackingId, String employee) {
        TestTracking tracking = getTracking(trackingId);
        if (tracking != null && tracking.getStatus() == TestTracking.Status.ARRIVED && !StringUtils.isEmpty(employee)) {
            TestResource resource = testResourceService.findByEmployeeNo(employee);
            if (resource == null) {
                log.error("### giveUpTask employee {} is not found", employee);
                return "failed";
            }

            TestTrackingHistory trackingHistory = new TestTrackingHistory();
            trackingHistory.setTracking(tracking);
            trackingHistory.setEmployee(employee.toUpperCase());
            trackingHistory.setAction(TestTracking.Status.GIVEN_UP);
            testTrackingHistoryRepository.save(trackingHistory);

            tracking.setEmployee(null);
            tracking.setStatus(TestTracking.Status.PENDING);
            testTrackingRepository.save(tracking);

            resource.setTrackingId(null);
            resource.setStatus(TestResource.Status.FREE);
            testResourceService.save(resource);

            notifyService.notifyToIcivet(String.format("%s is given up by %s(%s)", tracking.getTrackingCode(), resource.getName(), employee), tracking);
            return "success";
        } else {
            return "failed";
        }
    }
}
