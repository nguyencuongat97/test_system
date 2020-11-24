package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.b06ds03.repository.B06TestSerialErrorRepository;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestRepairSerialErrorRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestSerialErrorRepository;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestRepairSerialErrorServiceImpl implements TestRepairSerialErrorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private B06TestSerialErrorRepository b06TestSerialErrorRepository;

    @Autowired
    private SfcTestSerialErrorRepository sfcTestSerialErrorRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Override
    public void saveAll(List<TestRepairSerialError> repairSerialErrorList) {
        if (repairSerialErrorList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into test_repair_serial_error as target " +
                        "using(select factory=?, model_name=?, mo=?, section_name=?, group_name=?, station_name=?, serial_number=?, tester=?, repairer=?, test_time=?, error_code=?, test_location_code=?, repair_time=?, reason_code=?, location_code=?, workdate=?, shift=?, status=?, created_at=?, updated_at=?) as source " +
                        "   on target.model_name=source.model_name and target.group_name=source.group_name and target.station_name=source.station_name and target.serial_number=source.serial_number and target.test_time=source.test_time and target.error_code=source.error_code and target.test_location_code=source.test_location_code " +
                        "when matched then " +
                        "   update set " +
                        "   target.factory=source.factory, " +
                        "   target.mo=source.mo, " +
                        "   target.tester=source.tester, " +
                        "   target.test_location_code=source.test_location_code, " +
                        "   target.repairer=source.repairer, " +
                        "   target.repair_time=source.repair_time, " +
                        "   target.reason_code=source.reason_code, " +
                        "   target.location_code=source.location_code, " +
                        "   target.workdate=source.workdate, " +
                        "   target.shift=source.shift, " +
                        "   target.status=source.status, " +
                        "   target.section_name=source.section_name, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, mo, section_name, group_name, station_name, serial_number, tester, repairer, test_time, error_code, test_location_code, repair_time, reason_code, location_code, workdate, shift, status, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.mo, source.section_name, source.group_name, source.station_name, source.serial_number, source.tester, source.repairer, source.test_time, source.error_code, source.test_location_code, source.repair_time, source.reason_code, source.location_code, source.workdate, source.shift, source.status, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestRepairSerialError notify = repairSerialErrorList.get(i);
                        preparedStatement.setString(1, notify.getFactory());
                        preparedStatement.setString(2, notify.getModelName());
                        preparedStatement.setString(3, notify.getMo());
                        preparedStatement.setString(4, notify.getSectionName());
                        preparedStatement.setString(5, notify.getGroupName());
                        preparedStatement.setString(6, notify.getStationName());
                        preparedStatement.setString(7, notify.getSerialNumber());
                        preparedStatement.setString(8, notify.getTester());
                        preparedStatement.setString(9, notify.getRepairer());
                        preparedStatement.setTimestamp(10, new Timestamp(notify.getTestTime().getTime()));
                        preparedStatement.setString(11, notify.getErrorCode());
                        preparedStatement.setString(12, notify.getTestLocationCode());
                        preparedStatement.setTimestamp(13, notify.getRepairTime() != null ? new Timestamp(notify.getRepairTime().getTime()) : null);
                        preparedStatement.setString(14, notify.getReason());
                        preparedStatement.setString(15, notify.getLocationCode());
                        preparedStatement.setString(16, notify.getWorkdate());
                        preparedStatement.setString(17, notify.getShift());
                        preparedStatement.setInt(18, notify.getStatus().ordinal());
                        preparedStatement.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(20, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return repairSerialErrorList.size();
                    }
                });
    }

    @Override
    public List<Object[]> countByErrorCodeAndReason(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        if ("B06".equalsIgnoreCase(factory)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            if (StringUtils.isEmpty(stationName)) {
                return b06TestSerialErrorRepository.countByErrorCodeAndReason(modelName, groupName, startDate, endDate);
            }
            return b06TestSerialErrorRepository.countByErrorCodeAndReason(modelName, groupName, stationName, startDate, endDate);
        }
        if (StringUtils.isEmpty(stationName)) {
            return testRepairSerialErrorRepository.countByErrorCodeAndReason(factory, modelName, groupName, startDate, endDate);
        }
        return testRepairSerialErrorRepository.countByErrorCodeAndReason(factory, modelName, groupName, stationName, startDate, endDate);
    }

    @Override
    public List<Object[]> countByReasonCodeAndLocationCode(String factory, Date startDate, Date endDate) {
        return testRepairSerialErrorRepository.countByErrorCodeAndReasonAndLocationCode(factory, startDate, endDate);
    }

    @Override
    public List<Object[]> countByReason(String factory, Date startDate, Date endDate) {
        return testRepairSerialErrorRepository.countByReason(factory, startDate, endDate);
    }

    @Override
    public List<Object[]> countByReason(String factory, String modelName, String sectionName, String errorCode, Date startDate, Date endDate) {
        if (Factory.B06.equalsIgnoreCase(factory)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return b06TestSerialErrorRepository.countByReason(modelName, errorCode, startDate, endDate);
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            List<Map<String, Object>> result;
            if (modelList.contains(modelName)) {
                result = sfcTestSerialErrorRepository.countReasonByModelNameAndErrorCode(factory, "UI", modelName, errorCode, startDate, endDate);
            } else {
                result = sfcTestSerialErrorRepository.countReasonByModelNameAndErrorCode(factory, "", modelName, errorCode, startDate, endDate);
            }
            return result.stream().map(map -> {
                        Object[] objs = new Object[map.size()];
                        int i = 0;
                        for (Object obj : map.values()) {
                            objs[i++] = obj;
                        }
                        return objs;
                    }).collect(Collectors.toList());
        } else if (Factory.C03.equalsIgnoreCase(factory)){
            List<Map<String, Object>> result = sfcTestSerialErrorRepository.countReasonByModelNameAndErrorCode(factory, "", modelName, errorCode, startDate, endDate);
            return result.stream().map(map -> {
                        Object[] objs = new Object[map.size()];
                        int i = 0;
                        for (Object obj : map.values()) {
                            objs[i++] = obj;
                        }
                        return objs;
                    }).collect(Collectors.toList());
        }

        return testRepairSerialErrorRepository.countByReason(factory, modelName, "SI", errorCode, startDate, endDate);
    }

    @Override
    public List<Object[]> countByStation(String factory, String modelName, String sectionName, String errorCode, String reason, Date startDate, Date endDate) {
        if (Factory.B06.equalsIgnoreCase(factory)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return b06TestSerialErrorRepository.countByStation(modelName, errorCode, reason, startDate, endDate);
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            List<String> modelList = testModelMetaRepository.findModelByFactoryAndCustomer(factory, "UI");
            List<Map<String, Object>> result;
            if (modelList.contains(modelName)) {
                result = sfcTestSerialErrorRepository.countStationByModelNameAndErrorCodeAndReason(factory, "UI", modelName, errorCode, reason, startDate, endDate);
            } else {
                result = sfcTestSerialErrorRepository.countStationByModelNameAndErrorCodeAndReason(factory, "", modelName, errorCode, reason, startDate, endDate);
            }
            return result.stream().map(map -> {
                        Object[] objs = new Object[map.size()];
                        int i = 0;
                        for (Object obj : map.values()) {
                            objs[i++] = obj;
                        }
                        return objs;
                    }).collect(Collectors.toList());
        } else if (Factory.C03.equalsIgnoreCase(factory)) {
            List<Map<String, Object>> result = sfcTestSerialErrorRepository.countStationByModelNameAndErrorCodeAndReason(factory, "", modelName, errorCode, reason, startDate, endDate);
            return result.stream().map(map -> {
                        Object[] objs = new Object[map.size()];
                        int i = 0;
                        for (Object obj : map.values()) {
                            objs[i++] = obj;
                        }
                        return objs;
                    }).collect(Collectors.toList());
        }

        return testRepairSerialErrorRepository.countByStation(factory, modelName, "SI", errorCode, reason, startDate, endDate);
    }

    @Override
    public List<Object[]> countByModelNameAndTime(String factory, Date startDate, Date endDate) {
        return testRepairSerialErrorRepository.countByModelNameAndTime(factory, startDate, endDate);
    }

    @Override
    public List<Object[]> countByModelNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate) {
        if ("B06".equalsIgnoreCase(factory)) {
            return b06TestSerialErrorRepository.countByModelNameAndErrorCode(startDate, endDate);
        }

        if (status == null) {
            return testRepairSerialErrorRepository.countByModelNameAndErrorCode(factory, startDate, endDate);
        }
        return testRepairSerialErrorRepository.countByModelNameAndErrorCode(factory, status, startDate, endDate);
    }

    @Override
    public List<Object[]> countByGroupNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate) {
        if ("B06".equalsIgnoreCase(factory)) {
            return b06TestSerialErrorRepository.countByGroupNameAndErrorCode(startDate, endDate);
        }

        if (status == null) {
            return testRepairSerialErrorRepository.countByGroupNameAndErrorCode(factory, startDate, endDate);
        }
        return testRepairSerialErrorRepository.countByGroupNameAndErrorCode(factory, status, startDate, endDate);
    }

    @Override
    public List<Object[]> countByGroupNameAndLocationCode(String factory, Date startDate, Date endDate) {
        if ("B06".equalsIgnoreCase(factory)) {
            return b06TestSerialErrorRepository.countByGroupNameAndLocationCode(startDate, endDate);
        }

        return testRepairSerialErrorRepository.countByGroupNameAndErrorCode(factory, startDate, endDate);
    }

    @Override
    public List<Object[]> countByModelNameAndSectionNameAndStatus(String factory, Date startDate, Date endDate) {
        return testRepairSerialErrorRepository.countByModelNameAndSectionNameAndStatus(factory, startDate, endDate);
    }

    @Override
    public List<TestRepairSerialError> findByFactoryAndStatus(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate) {
        return testRepairSerialErrorRepository.findByFactoryAndStatusAndUpdatedAtBetween(factory, status, startDate, endDate);
    }

    @Override
    public void outdatedRepair(Date expiredDate) {
        testRepairSerialErrorRepository.outdatedRepair(expiredDate);
    }
}
