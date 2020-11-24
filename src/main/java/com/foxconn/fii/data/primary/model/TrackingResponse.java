package com.foxconn.fii.data.primary.model;

import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.model.entity.TestTrackingHistory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Map;

@Data
public class TrackingResponse {

    private long id;

    private String factory;

    private String sectionName;

    private String modelName;

    private String lineName;

    private String groupName;

    private String stationName;

    private Date startDate;

    private Date endDate;

    private TestTracking.Type type;

    private TestTracking.Status status;

    private Date createdAt;

    private Date updatedAt;

    private String message;

    private int wip;

    private int firstFail;

    private int secondFail;

    private String top3ErrorCode;

    private Map<String, TestError> errorMetaMap;

    private Date notifiedAt;

    private String employee;

    private Date assignedAt;

    private Date arrivedAt;

    private Date unlockedAt;

    private String errorCode;

    private Integer solutionId;

    private String why;

    private String action;

    private Date confirmedAt;

    private Date closedAt;

    private String result = "-";

    public boolean isLockedType() {
        return type == TestTracking.Type.LOCKED_A || type == TestTracking.Type.LOCKED_B || type == TestTracking.Type.LOCKED_C;
    }

    public boolean isValidated() {
        return status != TestTracking.Status.CONFIRMED &&
                status != TestTracking.Status.CLOSED &&
                status != TestTracking.Status.OUTDATED &&
                status != TestTracking.Status.AUTO_CLOSED &&
                status != TestTracking.Status.REOPEN;
    }

    public String getDowntime() {
        return confirmedAt != null && assignedAt != null ? String.valueOf((confirmedAt.getTime() - assignedAt.getTime())/(60*1000)) : "N/A";
    }

    public static TrackingResponse of (TestTracking tracking) {
        TrackingResponse result = new TrackingResponse();
        if (tracking == null) {
            return result;
        }
        BeanUtils.copyProperties(tracking, result);
        return result;
    }

    public static void loadHistory(TrackingResponse tracking, TestTrackingHistory history) {
        switch (history.getAction()) {
            case NOTIFIED:
                tracking.setNotifiedAt(history.getActionAt());
                break;
            case ASSIGNED:
                tracking.setEmployee(history.getEmployee());
                tracking.setAssignedAt(history.getActionAt());
                break;
            case ARRIVED:
                tracking.setEmployee(history.getEmployee());
                tracking.setArrivedAt(history.getActionAt());
                break;
            case UNLOCKED:
                tracking.setUnlockedAt(history.getActionAt());
                break;
            case CONFIRMED:
                if (history.getSolution() != null) {
                    tracking.setSolutionId(history.getSolution().getId());
                    tracking.setErrorCode(history.getSolution().getErrorCode());
                    tracking.setWhy(history.getSolution().getSolution());
                    tracking.setAction(history.getSolution().getAction());
                }
                tracking.setEmployee(history.getEmployee());
                tracking.setConfirmedAt(history.getActionAt());
                break;
            case CLOSED:
                tracking.setResult("OK");
                tracking.setClosedAt(history.getActionAt());
                break;
            case REOPEN:
                tracking.setResult("NG");
                tracking.setClosedAt(history.getActionAt());
                break;
            case AUTO_CLOSED:
            case OUTDATED:
            case GIVEN_UP:
                tracking.setClosedAt(history.getActionAt());
                break;

        }
    }
}
