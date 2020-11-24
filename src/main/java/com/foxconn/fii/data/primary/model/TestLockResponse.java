package com.foxconn.fii.data.primary.model;

import com.foxconn.fii.data.b06.model.B06TestLock;
import com.foxconn.fii.data.primary.model.entity.TestLock;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TestLockResponse {

    private int id;

    private String factory;

    private String modelName;

    private String groupName;

    private String stationName;

    private Timestamp dateTimeLock;

    private String lockDetail;

    private String detail;

    private String suggestion;

    public static TestLockResponse of(TestLock entity) {
        TestLockResponse lockResponse = new TestLockResponse();
        lockResponse.setId(entity.getId());
        lockResponse.setModelName(entity.getModelName());
        lockResponse.setGroupName(entity.getGroupName());
        lockResponse.setStationName(entity.getStationName());
        lockResponse.setDateTimeLock(entity.getDateTimeLock());
        lockResponse.setLockDetail(entity.getLockDetail());
        return lockResponse;
    }

    public static TestLockResponse of(B06TestLock entity) {
        TestLockResponse lockResponse = new TestLockResponse();
        lockResponse.setModelName(entity.getModelName());
        lockResponse.setGroupName(entity.getGroupName());
        lockResponse.setStationName(entity.getStationName());
        lockResponse.setDateTimeLock(entity.getDateTimeLock());
        lockResponse.setLockDetail(entity.getLockDetail());
        return lockResponse;
    }
}
