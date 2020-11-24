package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;

import java.util.Date;
import java.util.List;

public interface TestRepairSerialErrorService {

    void saveAll(List<TestRepairSerialError> repairSerialErrorList);

    List<Object[]> countByErrorCodeAndReason(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<Object[]> countByReasonCodeAndLocationCode(String factory, Date startDate, Date endDate);

    List<Object[]> countByReason(String factory, Date startDate, Date endDate);

    List<Object[]> countByReason(String factory, String modelName, String sectionName, String errorCode, Date startDate, Date endDate);

    List<Object[]> countByStation(String factory, String modelName, String sectionName, String errorCode, String reason, Date startDate, Date endDate);


    List<Object[]> countByModelNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    List<Object[]> countByGroupNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    List<Object[]> countByGroupNameAndLocationCode(String factory, Date startDate, Date endDate);

    List<Object[]> countByModelNameAndSectionNameAndStatus(String factory, Date startDate, Date endDate);


    List<Object[]> countByModelNameAndTime(String factory, Date startDate, Date endDate);

    List<TestRepairSerialError> findByFactoryAndStatus(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    void outdatedRepair(Date expiredDate);
}
