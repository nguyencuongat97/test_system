package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.response.MapResponse;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.b04ds02.model.B04DS02ErrorLog;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.data.primary.model.entity.TestStationMeta;
import com.foxconn.fii.receiver.test.service.TestMobileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test/mobile")
public class TestApiMobileController {
    @Autowired
    private TestMobileService testMobileService;

    @Autowired
    private TestApiController testApiController;

    @Autowired
    private TestApiModelController testApiModelController;

    @Autowired
    private TestApiStationController testApiStationController;


    @RequestMapping("/getNotifyAllFactory")
    public Object getNotifyAllFactory(
            @RequestParam TimeSpan timeSpan) {
        return testMobileService.getListNotifyAllFactory(timeSpan);
    }

    @RequestMapping("/getNotifyModelFactory")
    public Object getNotifyModelFactory(
            @RequestParam String factory,
            @RequestParam String model,
            @RequestParam TimeSpan timeSpan) {
        return testMobileService.getNotifyModelFactory(factory, model, timeSpan);
    }

    @RequestMapping("/getListModelByFactoryWithShift")
    public Object getListModelByFactoryWithShift(
            @RequestParam String factory,
            @RequestParam TimeSpan timeSpan) {
        return testMobileService.getListModelByFactoryWithShift(factory, timeSpan);
    }

    @RequestMapping("/getCapacityAndTotalModelFactory")
    public Object getCapacityAndTotalModelFactory(@RequestParam TimeSpan timeSpan) {
        return testMobileService.getCapacityAndTotalModelFactory(timeSpan);
    }


    @GetMapping("/model")
    public ListResponse<TestModelMeta> getModelMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {
        return ListResponse.success(testApiController.getModelMetaList(factory, customer, stage, parameter, timeSpan, all, workDate, shiftType));
    }

    @GetMapping("/group")
    public ListResponse<TestGroupMeta> getGroupMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam String modelName,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {
        return ListResponse.success(testApiController.getGroupMetaList(factory, customer, modelName, parameter, timeSpan, all, workDate, shiftType));
    }

    @GetMapping("/station")
    public ListResponse<TestStationMeta> getStationMetaList(
            @RequestParam String factory,
            @RequestParam(required = false) String customer,
            @RequestParam String modelName,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) Boolean parameter,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean all,
            @RequestParam(required = false) String workDate,
            @RequestParam(required = false) ShiftType shiftType) {
        return ListResponse.success(testApiController.getStationMetaList(factory, customer, modelName, groupName, parameter, timeSpan, all, workDate, shiftType));
    }

    @GetMapping("/employee")
    public TestResource getInformationEmployee(String employeeNo) {
        return testApiController.getInformationEmployee(employeeNo);
    }


    @GetMapping("/model/daily")
    public ListResponse<TestModel> getDailyModel(
            @RequestParam String factory,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean customer,
            @RequestParam(required = false, defaultValue = "ALL") MoType moType) {
        return ListResponse.success(testApiModelController.getDailyModel(factory, customerName, timeSpan, customer, moType));
    }

    @GetMapping("/group/total")
    public ListResponse<TestGroupDaily> getGroupTotal(String factory, String modelName, String timeSpan, Boolean customer, Boolean includeStation, String mode, MoType moType) {
        return ListResponse.success(testApiController.getGroupTotal(factory, modelName, timeSpan, customer, includeStation, mode, moType));
    }

    @RequestMapping("/station/total")
    public ListResponse<TestStationDaily> getStationTotal(String factory, String modelName, String groupName, String timeSpan, String mode, MoType moType) {
        return ListResponse.success(testApiController.getStationTotal(factory, modelName, groupName, timeSpan, mode, moType));
    }

    @GetMapping("/station/error-history")
    public ListResponse<B04DS02ErrorLog> getErrorHistory(String factory, String modelName, String groupName, String stationName, String errorCode, String timeSpan) {
        return ListResponse.success(testApiStationController.getErrorHistory(factory, modelName, groupName, stationName, errorCode, timeSpan));
    }

    @GetMapping("/station/error-detail")
    public MapResponse<TestErrorDaily> getErrorDetailByModel(String factory, String modelName, String groupName, String stationName, String timeSpan, Boolean customer, String mode, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        return MapResponse.success(testApiStationController.getErrorDetailByModel(factory, modelName, groupName, stationName, timeSpan, customer, mode, moType));
    }

    @GetMapping("/error/station")
    public ListResponse<TestErrorDaily> getByError(String factory, String modelName, String groupName, String errorCode, String timeSpan, MoType moType) {
        if (moType == null) {
            moType = MoType.ALL;
        }
        return ListResponse.success(testApiStationController.getByError(factory, modelName, groupName, errorCode, timeSpan, moType));
    }

    @GetMapping("/error/station-ntf-issue")
    public MapResponse<Long> getNtfByStation(String factory, String modelName, String errorCode, String timeSpan) {
        return MapResponse.success(testApiController.getNtfByStation(factory, modelName, errorCode, timeSpan));
    }

    @GetMapping("/error/defected-issue")
    public MapResponse<Long> getDefectedIssueByError(String factory, String modelName, String errorCode, String timeSpan) {
        return MapResponse.success(testApiController.getDefectedIssueByError(factory, modelName, errorCode, timeSpan));
    }
}
