package com.foxconn.fii.receiver.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.ConfirmTaskRequest;
import com.foxconn.fii.data.primary.model.TeCftTaskDailyConfirmData;
import com.foxconn.fii.data.primary.model.TeOnlineTaskDailyConfirmData;
import com.foxconn.fii.data.primary.model.TestTaskDailyConfirmRequest;
import com.foxconn.fii.data.primary.model.entity.TemperatureDevice;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.data.primary.model.entity.TestTaskComment;
import com.foxconn.fii.data.primary.model.entity.TestTaskDaily;
import com.foxconn.fii.data.primary.model.entity.TestTaskDailyConfirm;
import com.foxconn.fii.data.primary.model.entity.TestTaskDailyHistory;
import com.foxconn.fii.data.primary.model.entity.TestWorkHandover;
import com.foxconn.fii.data.primary.repository.TestTaskDailyConfirmRepository;
import com.foxconn.fii.data.primary.repository.TestTaskDailyHistoryRepository;
import com.foxconn.fii.data.primary.repository.TestTaskDailyRepository;
import com.foxconn.fii.data.primary.repository.TestWorkHandoverRepository;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.receiver.test.service.TestStationService;
import com.foxconn.fii.receiver.test.service.TestTaskDailyService;
import com.foxconn.fii.receiver.test.service.TestTrackingService;
import com.foxconn.fii.security.model.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.status.StatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestApiTaskDailyController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestStationService testStationService;

    @Autowired
    private TestResourceService testResourceService;

    @Autowired
    private TestTaskDailyRepository testTaskDailyRepository;

    @Autowired
    private TestTaskDailyService testTaskDailyService;

    @Autowired
    private TestTaskDailyConfirmRepository testTaskDailyConfirmRepository;

    @Autowired
    private TestTaskDailyHistoryRepository taskDailyHistoryRepository;

    @Autowired
    private TestWorkHandoverRepository testWorkHandoverRepository;

    @Autowired
    private TestTrackingService testTrackingService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/task-daily")
    public List<TestTaskDaily> getTaskDailyList(String factory, String employee, String resourceGroup, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        TestResource resource = testResourceService.findByEmployeeNo(employee);
        if (resource == null) {
            return Collections.emptyList();
        }

        List<TestTaskDaily> taskList;
        if (StringUtils.isEmpty(resourceGroup)) {
            if ("TE-ONLINE".equalsIgnoreCase(resource.getDem())) {
                taskList = testTaskDailyRepository.findByFactoryAndResourceGroupAndCreatedAtBetween(factory, resource.getDem(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            } else {
                taskList = testTaskDailyRepository.findByFactoryAndResourceGroupAndCreatedAtBetween(factory, resource.getDem(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestTaskDaily.Status.DONE);
            }
        } else {
            if ("TE-ONLINE".equalsIgnoreCase(resourceGroup)) {
                taskList = testTaskDailyRepository.findByFactoryAndResourceGroupAndCreatedAtBetween(factory, resourceGroup, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            } else {
                taskList = testTaskDailyRepository.findByFactoryAndResourceGroupAndCreatedAtBetween(factory, resourceGroup, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), TestTaskDaily.Status.DONE);
            }
        }

//        if (resource == null || (!"TE-ONLINE".equalsIgnoreCase(resource.getDem()) && !"TE-CFT".equalsIgnoreCase(resource.getDem()))) {
//            taskList = testTaskDailyRepository.findByFactoryAndCreatedAtBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        } else {
//            if (resource.getGroupLevel() != null && resource.getGroupLevel() >= 4) {
//                taskList = testTaskDailyRepository.findByFactoryAndResourceGroupAndCreatedAtBetween(factory, resource.getDem(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//            } else {
//                taskList = testTaskDailyRepository.findByFactoryAndEmployeeIdAndCreatedAtBetween(factory, employee, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//            }
//        }

        taskList.sort(Comparator.comparing(TestTaskDaily::getCreatedAt));
        return taskList;
    }

    @RequestMapping("/task-daily/{id}")
    public TestTaskDaily getTaskDaily(@PathVariable("id") Long id) {
        return testTaskDailyRepository.findById(id).orElseThrow(() -> CommonException.of("task daily {} not found", id));
    }

    @PostMapping("/task-daily")
    public Boolean createTaskDaily(@RequestBody TestTaskDaily taskDaily) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestResource testResource = testResourceService.findByEmployeeNo(employee);
        if (testResource == null) {
            return false;
        }

        TestResource targetResource = testResourceService.findByEmployeeNo(taskDaily.getEmployeeId());
        if (targetResource == null) {
            return false;
        }

        TestTaskDaily taskTarget;
        if (taskDaily.getId() != 0) {
            taskTarget = testTaskDailyRepository.findById(taskDaily.getId())
                    .orElseThrow(() -> CommonException.of(String.format("task daily id %d not found", taskDaily.getId())));

            BeanUtils.copyPropertiesIgnoreNull(taskDaily, taskTarget, "id", "creator");
            if (taskDaily.getTrackingId() != null) {
                taskTarget.setCreator("FOXCONN");
            }
        } else {
            taskTarget = new TestTaskDaily();
            BeanUtils.copyPropertiesIgnoreNull(taskDaily, taskTarget, "id", "creator");
            if (taskDaily.getTrackingId() != null) {
                taskTarget.setCreator("FOXCONN");
                taskTarget.setResourceGroup("TE-ONLINE");
            } else {
                taskTarget.setCreator(employee);
                taskTarget.setResourceGroup(targetResource.getDem());
            }
        }

        if (StringUtils.isEmpty(taskTarget.getEmployeeName())) {
            taskTarget.setEmployeeName(testResource.getName());
        }
        testTaskDailyRepository.save(taskTarget);

        TestTaskDailyHistory taskHistory = new TestTaskDailyHistory();
        BeanUtils.copyPropertiesIgnoreNull(taskTarget, taskHistory, "id");
        taskHistory.setTaskId(taskTarget.getId());
        taskHistory.setModifier(employee);
        taskDailyHistoryRepository.save(taskHistory);

        if (taskTarget.getTrackingId() != null) {
            switch (taskDaily.getStatus()) {
                case ON_GOING:
                    testTrackingService.handleTask(taskTarget.getTrackingId(), taskTarget.getEmployeeId());
                    break;
                case PENDING:
                    break;
                case DONE:
                    ConfirmTaskRequest request = new ConfirmTaskRequest();
                    request.setTrackingId(taskTarget.getTrackingId());
                    request.setEmployee(taskTarget.getEmployeeId());
                    request.setAuto(true);
                    if (!StringUtils.isEmpty(taskTarget.getTaskContent())) {
                        String[] contents = taskTarget.getTaskContent().split("\n");
                        if (contents.length >= 3) {
                            request.setErrorCode(contents[0]);
                            request.setSolutionName(contents[1]);
                            request.setAction(contents[2]);
                        }
                    }
                    testTrackingService.confirmTask(request);
            }
        }

        return true;
    }

    @PutMapping("/task-daily/{id}")
    public Boolean updateTaskDaily(@PathVariable("id") Long id, @RequestBody TestTaskDaily taskDaily) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestTaskDaily taskTarget = testTaskDailyRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(taskDaily, taskTarget, "id", "creator");
        testTaskDailyRepository.save(taskTarget);

        TestTaskDailyHistory taskHistory = new TestTaskDailyHistory();
        BeanUtils.copyPropertiesIgnoreNull(taskTarget, taskHistory, "id");
        taskHistory.setTaskId(taskTarget.getId());
        taskHistory.setModifier(employee);
        taskDailyHistoryRepository.save(taskHistory);

        if (taskTarget.getTrackingId() != null) {
            switch (taskTarget.getStatus()) {
                case ON_GOING:
                    testTrackingService.handleTask(taskTarget.getTrackingId(), taskTarget.getEmployeeId());
                    break;
                case PENDING:
                    break;
                case DONE:
                    ConfirmTaskRequest request = new ConfirmTaskRequest();
                    request.setTrackingId(taskTarget.getTrackingId());
                    request.setEmployee(taskTarget.getEmployeeId());
                    request.setAuto(true);
                    if (!StringUtils.isEmpty(taskTarget.getTaskContent())) {
                        String[] contents = taskTarget.getTaskContent().split("-");
                        if (contents.length >= 3) {
                            request.setErrorCode(contents[0].replace("ERROR CODE: ", ""));
                            request.setSolutionName(contents[1].replace("ROOT CAUSE: ", ""));
                            request.setAction(contents[2].replace("ACTION: ", ""));

                            testTrackingService.confirmTask(request);
                        }
                    }
            }
        }
        return true;
    }

    @PutMapping("/task-daily/{id}/assigned")
    public Boolean assignedTaskDaily(
            @PathVariable("id") Long id,
            @RequestParam("employeeId") String employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestTaskDaily taskTarget = testTaskDailyRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily %d not found", id)));

        if (taskTarget.getStatus() == TestTaskDaily.Status.DONE) {
            return false;
        }

        TestResource resource = testResourceService.findByEmployeeNo(employeeId);
        if (resource == null) {
            return false;
        }

        taskTarget.setStatus(TestTaskDaily.Status.ON_GOING);
        taskTarget.setEmployeeId(employeeId);
        taskTarget.setEmployeeName(resource.getName());
        testTaskDailyRepository.save(taskTarget);

        TestTaskDailyHistory taskHistory = new TestTaskDailyHistory();
        BeanUtils.copyPropertiesIgnoreNull(taskTarget, taskHistory, "id");
        taskHistory.setTaskId(taskTarget.getId());
        taskHistory.setModifier(employee);
        taskDailyHistoryRepository.save(taskHistory);

        testTrackingService.handleTask(taskTarget.getTrackingId(), taskTarget.getEmployeeId());

        return true;
    }

    @PutMapping("/task-daily/{id}/received")
    public Boolean receivedTaskDaily(
            @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestTaskDaily taskTarget = testTaskDailyRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily %d not found", id)));

        if (taskTarget.getStatus() == TestTaskDaily.Status.DONE) {
            return false;
        }

        TestResource resource = testResourceService.findByEmployeeNo(employee);
        if (resource == null) {
            return false;
        }

        taskTarget.setStatus(TestTaskDaily.Status.ON_GOING);
        taskTarget.setEmployeeId(employee);
        taskTarget.setEmployeeName(resource.getName());
        testTaskDailyRepository.save(taskTarget);

        TestTaskDailyHistory taskHistory = new TestTaskDailyHistory();
        BeanUtils.copyPropertiesIgnoreNull(taskTarget, taskHistory, "id");
        taskHistory.setTaskId(taskTarget.getId());
        taskHistory.setModifier(employee);
        taskDailyHistoryRepository.save(taskHistory);

        testTrackingService.handleTask(taskTarget.getTrackingId(), taskTarget.getEmployeeId());

        return true;
    }

    @PutMapping("/task-daily/{id}/done")
    public Boolean doneTaskDaily(
            @PathVariable("id") Long id,
            String taskContent, String errorCode, String rootCause, String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestTaskDaily taskTarget = testTaskDailyRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily %d not found", id)));

        if (taskTarget.getStatus() == TestTaskDaily.Status.DONE || StringUtils.isEmpty(employee) || !employee.equalsIgnoreCase(taskTarget.getEmployeeId())) {
            return false;
        }

        TestResource resource = testResourceService.findByEmployeeNo(employee);
        if (resource == null) {
            return false;
        }

        if (taskTarget.getTrackingId() != null) {
            ConfirmTaskRequest request = new ConfirmTaskRequest();
            request.setTrackingId(taskTarget.getTrackingId());
            request.setEmployee(taskTarget.getEmployeeId());
            if (!StringUtils.isEmpty(taskTarget.getTaskContent())) {
                String[] contents = taskTarget.getTaskContent().split("-");
                if (contents.length >= 3) {
                    errorCode = contents[0].replace("ERROR CODE: ", "");
                    rootCause = contents[1].replace("ROOT CAUSE: ", "");
                    action = contents[2].replace("ACTION: ", "");
                }
            } else {
                taskContent = String.format("ERROR CODE: %s-ROOT CAUSE: %s-ACTION: %s", errorCode, rootCause, action);
            }

            request.setErrorCode(errorCode);
            request.setSolutionName(rootCause);
            request.setAction(action);
            testTrackingService.confirmTask(request);
        }

        taskTarget.setStatus(TestTaskDaily.Status.DONE);
        taskTarget.setEmployeeId(employee);
        taskTarget.setEmployeeName(resource.getName());
        taskTarget.setTaskContent(taskContent);
        testTaskDailyRepository.save(taskTarget);

        TestTaskDailyHistory taskHistory = new TestTaskDailyHistory();
        BeanUtils.copyPropertiesIgnoreNull(taskTarget, taskHistory, "id");
        taskHistory.setTaskId(taskTarget.getId());
        taskHistory.setModifier(employee);
        taskDailyHistoryRepository.save(taskHistory);

        return true;
    }

    @DeleteMapping("/task-daily/{id}")
    public Boolean removeTaskDaily(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestResource resource = testResourceService.findByEmployeeNo(employee);
        if (resource == null) {
            throw CommonException.of(String.format("resource %s not found", employee));
        }

        TestTaskDaily taskTarget = testTaskDailyRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily %d not found", id)));

        if (!employee.equalsIgnoreCase(taskTarget.getCreator()) && resource.getGroupLevel() < 4){
            throw CommonException.of(String.format("you are not permission delete task daily %d", id));
        }

        testTaskDailyRepository.delete(taskTarget);
        return true;
    }

    @RequestMapping("/task-comment")
    public List<TestTaskComment> getTaskComment(Long taskId) {
        return testTaskDailyService.getListCommentByTaskId(taskId);
    }

    @PostMapping("/task-comment")
    public Boolean createTaskComment(@RequestBody TestTaskComment taskComment) {

        TestResource testResource = testResourceService.findByEmployeeNo(taskComment.getEmployeeId());
        if (testResource == null) {
            return false;
        }

        TestTaskComment taskTarget;
        if (taskComment.getId() != 0) {
            taskTarget = testTaskDailyService.getCommentById(taskComment.getId())
                    .orElseThrow(() -> CommonException.of(String.format("task comment id %d not found", taskComment.getId())));

            BeanUtils.copyPropertiesIgnoreNull(taskComment, taskTarget, "id");
            testTaskDailyService.increasingCommentNumber(taskComment.getTaskId());
        } else {
            taskTarget = new TestTaskComment();
            BeanUtils.copyPropertiesIgnoreNull(taskComment, taskTarget, "id");
        }

        taskTarget.setEmployeeName(testResource.getName());
        testTaskDailyService.saveComment(taskTarget);
        return true;
    }

    @PutMapping("/task-comment/{id}")
    public Boolean updateTaskComment(@PathVariable("id") Long id, @RequestBody TestTaskComment taskComment) {
        TestTaskComment taskTarget = testTaskDailyService.getCommentById(id)
                .orElseThrow(() -> CommonException.of(String.format("task comment %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(taskComment, taskTarget, "id");
        testTaskDailyService.saveComment(taskTarget);
        return true;
    }

    @DeleteMapping("/task-comment/{id}")
    public Boolean removeTaskComment(@PathVariable("id") Long id) {
        TestTaskComment taskComment = testTaskDailyService.getCommentById(id)
                .orElseThrow(() -> CommonException.of(String.format("task comment %d not found", id)));

        testTaskDailyService.removeComment(taskComment);
        testTaskDailyService.increasingCommentNumber(taskComment.getTaskId());
        return true;
    }

    @RequestMapping("/task-daily-confirm")
    public List<TestTaskDailyConfirm> getTaskDailyConfirm(String factory, String dem, String employee, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        if (!StringUtils.isEmpty(employee)) {
            List<TestTaskDailyConfirm> taskList = testTaskDailyConfirmRepository.findByFactoryAndEmployeeIdAndInputDateBetween(factory, employee, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            taskList.sort(Comparator.comparing(TestTaskDailyConfirm::getEmployeeId));
            taskList.forEach(task -> task.parseData(objectMapper, task.getResourceGroup()));
            return taskList;
        }

        if (!StringUtils.isEmpty(dem)) {
            List<TestTaskDailyConfirm> taskList = testTaskDailyConfirmRepository.findByFactoryAndResourceGroupAndInputDateBetween(factory, dem, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            taskList.sort(Comparator.comparing(TestTaskDailyConfirm::getEmployeeId));
            taskList.forEach(task -> task.parseData(objectMapper, dem));
            return taskList;
        }

        return new ArrayList<>();
    }

    @PostMapping("/task-daily-confirm")
    public Boolean createTaskDailyConfirm(@RequestBody TestTaskDailyConfirmRequest taskDaily) {

        TestResource testResource = testResourceService.findByEmployeeNo(taskDaily.getEmployeeId());
        if (testResource == null) {
            return false;
        }

        TestTaskDailyConfirm taskTarget;
        if (taskDaily.getId() != 0) {
            taskTarget = testTaskDailyConfirmRepository.findById(taskDaily.getId())
                    .orElseThrow(() -> CommonException.of(String.format("task daily id %d not found", taskDaily.getId())));
            taskTarget.parseData(objectMapper, taskTarget.getResourceGroup());
        } else {
            taskTarget = new TestTaskDailyConfirm();

            taskTarget.setInputDate(new Date());
            taskTarget.setEmployeeId(testResource.getEmployeeNo());
            taskTarget.setEmployeeName(testResource.getName());
            taskTarget.setLine(testResource.getLineName());
            taskTarget.setShift(testResource.getShift());
            taskTarget.setProject(testResource.getModelName());
            taskTarget.setWorkOffDay(testResource.getWorkOffDay());
            taskTarget.setResourceGroup(testResource.getDem());
        }

        try {
            if ("TE-ONLINE".equalsIgnoreCase(testResource.getDem())) {
                TeOnlineTaskDailyConfirmData data = taskTarget.getTeOnlineData() != null ? taskTarget.getTeOnlineData() : new TeOnlineTaskDailyConfirmData();
                BeanUtils.copyPropertiesIgnoreNull(taskDaily, data);

                taskTarget.setDetail(objectMapper.writeValueAsString(data));
                taskTarget.setScore(data.getScore());
            } else if ("TE-CFT".equalsIgnoreCase(testResource.getDem())) {
                TeCftTaskDailyConfirmData data = taskTarget.getTeCftData() != null ? taskTarget.getTeCftData() : new TeCftTaskDailyConfirmData();
                BeanUtils.copyPropertiesIgnoreNull(taskDaily, data);

                taskTarget.setDetail(objectMapper.writeValueAsString(data));
                taskTarget.setScore(data.getScore());
            }
        } catch (Exception e) {
            log.error("parse data error", e);
        }

        testTaskDailyConfirmRepository.save(taskTarget);
        return true;
    }

    @PutMapping("/task-daily-confirm/{id}")
    public Boolean updateTaskDailyConfirm(@PathVariable("id") Long id, @RequestBody TestTaskDailyConfirmRequest taskDaily) {
        TestTaskDailyConfirm taskTarget = testTaskDailyConfirmRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("task daily confirm %d not found", id)));
        taskTarget.parseData(objectMapper, taskTarget.getResourceGroup());

        TestResource testResource = testResourceService.findByEmployeeNo(taskDaily.getEmployeeId());
        if (testResource == null) {
            return false;
        }

        try {
            if ("TE-ONLINE".equalsIgnoreCase(testResource.getDem())) {
                TeOnlineTaskDailyConfirmData data = taskTarget.getTeOnlineData() != null ? taskTarget.getTeOnlineData() : new TeOnlineTaskDailyConfirmData();
                BeanUtils.copyPropertiesIgnoreNull(taskDaily, data);

                taskTarget.setDetail(objectMapper.writeValueAsString(data));
                taskTarget.setScore(data.getScore());
            } else if ("TE-CFT".equalsIgnoreCase(testResource.getDem())) {
                TeCftTaskDailyConfirmData data = taskTarget.getTeCftData() != null ? taskTarget.getTeCftData() : new TeCftTaskDailyConfirmData();
                BeanUtils.copyPropertiesIgnoreNull(taskDaily, data);

                taskTarget.setDetail(objectMapper.writeValueAsString(data));
                taskTarget.setScore(data.getScore());
            }
        } catch (Exception e) {
            log.error("parse data error", e);
        }

        testTaskDailyConfirmRepository.save(taskTarget);
        return true;
    }

    @DeleteMapping("/task-daily-confirm/{id}")
    public Boolean removeTaskDailyConfirm(@PathVariable("id") Long id) {
        testTaskDailyConfirmRepository.deleteById(id);
        return true;
    }

    @RequestMapping("/work-handover")
    public List<TestWorkHandover> getWorkHandover(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR_OF_DAY, -12);
        TimeSpan ts2 = TimeSpan.of(calendar.getTime(), fullTimeSpan.getEndDate());
        calendar.add(Calendar.HOUR_OF_DAY, -12);
        TimeSpan ts1 = TimeSpan.of(calendar.getTime(), ts2.getStartDate());
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, TestGroupMeta> groupMetaMap = testGroupService.getGroupMetaList(factory, null, null)
                .stream().filter(TestGroupMeta::getVisible).collect(Collectors.toMap(meta -> meta.getModelName() + "_" + meta.getGroupName(), meta -> meta, (m1, m2) -> m1));

        List<TestResource> resourceList = testResourceService.findByFactoryAndDem(factory, "TE-ONLINE")
                .stream().filter(resource -> resource.getGroupLevel() == 1 && !StringUtils.isEmpty(resource.getModelName()) && (fullTimeSpan.getType() == TimeSpan.Type.FULL || resource.getShift() == fullTimeSpan.getShiftType()))
                .collect(Collectors.toList());

        List<TestWorkHandover> workHandoverList = testWorkHandoverRepository.findByFactoryAndStartDateBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, TestWorkHandover> result = workHandoverList
                .stream().collect(Collectors.toMap(
                        work -> work.getLineName() + "_" + work.getShift().toString() + "_" + work.getOwner() + "_" + work.getModelName() + "_" + work.getGroupName(),
                        work -> work,
                        (w1, w2) -> w1));

        Map<String, List<TestGroupDaily>> modelMap1 = testGroupService.getGroupDailyList(factory, ts1.getStartDate(), ts1.getEndDate())
                .stream().collect(Collectors.groupingBy(TestGroupDaily::getModelName));

//        Map<String, List<TestModel>> modelMap2 = testModelService.getAllModelDaily(factory, ts2, false)
//                .stream().collect(Collectors.groupingBy(TestModel::getModelName));

        Map<String, List<TestGroupDaily>> modelMap2 = testGroupService.getGroupDailyList(factory, ts2.getStartDate(), ts2.getEndDate())
                .stream().collect(Collectors.groupingBy(TestGroupDaily::getModelName));

        Map<String, List<String>> stationMap = new HashMap<>();
        List<TestWorkHandover> handoverList = new ArrayList<>();
        Map<String, Map<String, Integer>> modelLineMap = new HashMap<>();

        resourceList.forEach(employee -> {
            TimeSpan ts = fullTimeSpan;
            Map<String, List<TestGroupDaily>> modelMap = new HashMap<>();
            if (ts1.getShiftType() == employee.getShift()/* && modelMap1.containsKey(employee.getLineName())*/) {
                ts = ts1;
                modelMap = modelMap1;
            } else if (ts2.getShiftType() == employee.getShift()/* && modelMap2.containsKey(employee.getLineName())*/) {
                ts = ts2;
                modelMap = modelMap2;
            }

            if (modelMap.isEmpty()) {
                return;
            }

            boolean flag = false;
            for (Map.Entry<String, List<TestGroupDaily>> modelEntry : modelMap.entrySet()) {
                if (!employee.getModelName().contains(modelEntry.getKey())) {
                    continue;
                }

                for (TestGroupDaily group : modelEntry.getValue()) {
                    Map<String, Integer> outputLineMap = modelLineMap.getOrDefault(group.getModelName(), new HashMap<>());
                    if (outputLineMap.isEmpty()) {
                        outputLineMap.put(group.getLineName(), group.getPass());
                    } else {
                        Map.Entry<String, Integer> entry = outputLineMap.entrySet().iterator().next();
                        if (entry.getValue() < group.getPass()) {
                            outputLineMap.remove(entry.getKey());
                            outputLineMap.put(group.getLineName(), group.getPass());
                        }
                    }
                    modelLineMap.put(group.getModelName(), outputLineMap);

                    if (employee.getLineName().contains(group.getLineName())) {
                        flag = true;
                    }
                    TestGroupMeta groupMeta = groupMetaMap.getOrDefault(group.getModelName() + "_" + group.getGroupName(), new TestGroupMeta());
                    if (result.containsKey(group.getLineName() + "_" + ts.getShiftType() + "_" + employee.getEmployeeNo() + "_" + group.getModelName() + "_" + group.getGroupName())) {
                        TestWorkHandover work = result.get(group.getLineName() + "_" + ts.getShiftType() + "_" + employee.getEmployeeNo() + "_" + group.getModelName() + "_" + group.getGroupName());
                        work.setEmployeeNo(employee.getName());

                        List<String> stationStatusList = stationMap.get(String.join("_", work.getFactory(), work.getModelName(), work.getGroupName()));
                        if (stationStatusList == null) {
                            stationStatusList = new ArrayList<>();
                            List<TestStationDaily> stationDailyList = testStationService.getStationDailyList(work.getFactory(), work.getModelName(), work.getGroupName(), ts.getStartDate(), ts.getEndDate(), "", MoType.ALL);
                            stationDailyList.sort(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()));

                            StringBuilder ok = new StringBuilder();
                            StringBuilder ng = new StringBuilder();
                            StringBuilder top = new StringBuilder();
                            int index = 0;
                            for (TestStationDaily station : stationDailyList) {
                                if (index++ < 3) {
                                    top.append(station.getStationName()).append(",");
                                }
                                if (station.getRetestRate() > 10) {
                                    ng.append(station.getStationName()).append(",");
                                } else {
                                    ok.append(station.getStationName()).append(",");
                                }
                            }

                            stationStatusList.add(ok.toString());
                            stationStatusList.add(ng.toString());
                            stationStatusList.add(top.toString());
                            stationMap.put(String.join("_", work.getFactory(), work.getModelName(), work.getGroupName()), stationStatusList);
                        }

                        work.setStationOK(stationStatusList.get(0));
                        work.setStationNG(stationStatusList.get(1));
                        work.setStationNGTop(stationStatusList.get(2));
                        handoverList.add(work);
                    } else {
                        if (!groupMeta.getVisible()) {
                            continue;
                        }
                        if (employee.getLineName().contains(group.getLineName())) {
                            flag = true;
                        }

                        TestWorkHandover work = new TestWorkHandover();
                        work.setFactory(factory);
                        work.setLineName(group.getLineName());
                        work.setWorkDate(ts.getStartDate());
                        work.setShift(ts.getShiftType());
                        work.setModelName(group.getModelName());
                        work.setGroupName(group.getGroupName());
                        work.setOwner(employee.getEmployeeNo());
                        work.setEmployeeNo(employee.getName());
                        work.setStartDate(ts.getStartDate());
                        work.setEndDate(ts.getEndDate());

                        List<String> stationStatusList = stationMap.get(String.join("_", work.getFactory(), work.getModelName(), work.getGroupName()));
                        if (stationStatusList == null) {
                            stationStatusList = new ArrayList<>();
                            List<TestStationDaily> stationDailyList = testStationService.getStationDailyList(work.getFactory(), work.getModelName(), work.getGroupName(), ts.getStartDate(), ts.getEndDate(), "", MoType.ALL);
                            stationDailyList.sort(Comparator.comparing(TestStationDaily::getRetestRate, Comparator.reverseOrder()));

                            StringBuilder ok = new StringBuilder();
                            StringBuilder ng = new StringBuilder();
                            StringBuilder top = new StringBuilder();
                            int index = 0;
                            for (TestStationDaily station : stationDailyList) {
                                if (index++ < 3) {
                                    top.append(station.getStationName()).append(",");
                                }
                                if (station.getRetestRate() > 10) {
                                    ng.append(station.getStationName()).append(",");
                                } else {
                                    ok.append(station.getStationName()).append(",");
                                }
                            }

                            stationStatusList.add(ok.toString());
                            stationStatusList.add(ng.toString());
                            stationStatusList.add(top.toString());
                            stationMap.put(String.join("_", work.getFactory(), work.getModelName(), work.getGroupName()), stationStatusList);
                        }

                        work.setStationOK(stationStatusList.get(0));
                        work.setStationNG(stationStatusList.get(1));
                        work.setStationNGTop(stationStatusList.get(2));
                        handoverList.add(work);
                    }
                }
            }

            if (!flag && !StringUtils.isEmpty(employee.getLineName())) {
                String[] lineList = employee.getLineName().split(",");
                for (String lineName : lineList) {
                    TestWorkHandover work;
                    if (result.containsKey(lineName + "_" + ts.getShiftType() + "_" + employee.getEmployeeNo() + "_" + "" + "_" + "")) {
                        work = result.get(lineName + "_" + ts.getShiftType() + "_" + employee.getEmployeeNo() + "_" + "" + "_" + "");
                    } else {
                        work = new TestWorkHandover();
                        work.setFactory(factory);
                        work.setLineName(lineName);
                        work.setWorkDate(ts.getStartDate());
                        work.setShift(ts.getShiftType());
                        work.setModelName("");
                        work.setGroupName("");
                        work.setOwner(employee.getEmployeeNo());
                        work.setStartDate(ts.getStartDate());
                        work.setEndDate(ts.getEndDate());
                    }
                    work.setEmployeeNo(employee.getName());
                    work.setStationOK("");
                    work.setStationNG("");
                    work.setStationNGTop("");
                    handoverList.add(work);
                }
            }
        });

        handoverList.forEach(handover -> {
            if (modelLineMap.containsKey(handover.getModelName())) {
                Map<String, Integer> outputLineMap = modelLineMap.get(handover.getModelName());
                if (!outputLineMap.isEmpty()) {
                    Map.Entry<String, Integer> entry = outputLineMap.entrySet().iterator().next();
                    if (!entry.getKey().equalsIgnoreCase(handover.getLineName())) {
                        handover.setLineName(entry.getKey());
                    }
                }
            }
        });

        handoverList.sort(Comparator.comparing(TestWorkHandover::getLineName)
                .thenComparing(TestWorkHandover::getShift)
                .thenComparing(TestWorkHandover::getOwner));

        return handoverList;
    }

    @PostMapping("/work-handover")
    public Boolean createWorkHandover(@RequestBody TestWorkHandover tmp) {
        TestWorkHandover target = testWorkHandoverRepository.findByFactoryAndLineNameAndModelNameAndGroupNameAndOwnerAndWorkDateAndShift(tmp.getFactory(), tmp.getLineName(), tmp.getModelName(), tmp.getGroupName(), tmp.getOwner(), tmp.getWorkDate(), tmp.getShift())
                .orElse(new TestWorkHandover());
        BeanUtils.copyPropertiesIgnoreNull(tmp, target, "id");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        TimeSpan timeSpan = TimeSpan.from(df.format(target.getWorkDate()) + " " + target.getShift(), TimeSpan.Type.DAILY);
        Calendar calendar = Calendar.getInstance();
        if (timeSpan != null) {
            calendar.setTime(timeSpan.getStartDate());
            calendar.add(Calendar.MILLISECOND, 1);
            target.setStartDate(calendar.getTime());

            calendar.setTime(timeSpan.getEndDate());
            calendar.add(Calendar.MILLISECOND, 1);
            target.setEndDate(calendar.getTime());
        }

        testWorkHandoverRepository.save(target);
        return true;
    }

    @PutMapping("/work-handover/{id}")
    public Boolean updateWorkHandover(@PathVariable("id") Integer id, @RequestBody TemperatureDevice tmp) {
        TestWorkHandover target = testWorkHandoverRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("work hanover id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(tmp, target, "id");
        testWorkHandoverRepository.save(target);
        return true;
    }

    @DeleteMapping("/work-handover/{id}")
    public Boolean removeWorkHandover(@PathVariable("id") Integer id) {
        testWorkHandoverRepository.deleteById(id);
        return true;
    }
}
