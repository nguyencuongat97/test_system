package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import com.foxconn.fii.data.primary.model.entity.TestNoteError;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestErrorService {

    int[] saveAll(List<TestError> errorList);

    List<TestError> getErrorByCodeList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestErrorDaily> getErrorDailyByCodeList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, String mode);

    Map<String, TestError> getErrorByTimeMap(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate, ShiftType shiftType);

    Map<String, Map<String, TestErrorDaily>> getErrorDailyByDayMap(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, MoType moType);

    Map<String, String> getErrorMetaMap(String factory, String modelName);

    List<TestErrorMeta> getErrorMetaList(String factory, String modelName);

    List<String> getErrorCodeList(String factory, String modelName);

    TestError getError(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate);

    Map<String, TestErrorDaily> getErrorDailyByDayMap(String factory, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate, MoType moType);

    List<TestErrorDaily> getErrorDailyList(String factory, String modelName, String errorCode, Date startDate, Date endDate, MoType moType);

    List<TestErrorDaily> getErrorDailyListFromDB(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestErrorDaily> getErrorDailyList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    Map<String, TestError> getTopErrorCode(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, Integer limit);

    Map<String, TestErrorDaily> getErrorDailyTopErrorCode(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, Integer limit, String mode, MoType moType);

    Object saveNoteErrorService(TestNoteError noteError)  throws IOException;

    TestErrorMeta getErrorMeta(String errorCode);

    List<String> getModelErrorList(String factory, Date startDate, Date endDate);

    List<String> getGroupErrorList(String factory, String modelName, Date startDate, Date endDate);

    List<String> getStationErrorList(String factory, String modelName, String groupName, Date startDate, Date endDate);
}
