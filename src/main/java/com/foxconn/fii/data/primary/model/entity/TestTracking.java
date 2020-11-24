package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "test_tracking")
public class TestTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "type")
    private Type type;

    @Column(name = "message")
    private String message;

    @Column(name = "wip")
    private int wip;

    @Column(name = "first_fail")
    private int firstFail;

    @Column(name = "second_fail")
    private int secondFail;

    @Column(name = "top3_error_code")
    private String top3ErrorCode;

    @Column(name = "error_map")
    private String errorMap;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "fail")
    private int errorFail;

    @Column(name = "status")
    private Status status;

    @Column(name = "employee")
    private String employee;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public enum Type {
        LOCKED_A,
        LOCKED_B,
        LOCKED_C,
        WARNING_B,
        WARNING_CPK,
        WARNING_LOCKED_TIMEOUT,
        WARNING_IDLE,
        WARNING_A,
        WARNING_C,
        LOCKED_D
    }

    public enum Status {
        PENDING,
        NOTIFIED,
        TIMEOUT,
        ASSIGNED,
        ARRIVED,
        GIVEN_UP,
        CONFIRMED,
        REOPEN,
        OUTDATED,
        CLOSED,
        AUTO_CLOSED,
        UNLOCKED
    }

    public static TestTracking of(String factory, String modelName, String lineName, String groupName, String stationName, Date startDate, Date endDate) {
        TestTracking tracking = new TestTracking();
        tracking.setFactory(factory);
        tracking.setSectionName("SI");
        tracking.setLineName(lineName);
        tracking.setModelName(modelName);
        tracking.setGroupName(groupName);
        tracking.setStationName(stationName);
        tracking.setStartDate(startDate);
        tracking.setEndDate(endDate);
        tracking.setStatus(Status.PENDING);
        return tracking;
    }

    public String getTrackingCode() {
        return "WS_" + factory + '_' + id;
    }

    public boolean isLockedType() {
        return type == TestTracking.Type.LOCKED_A ||
                type == TestTracking.Type.LOCKED_B ||
                type == TestTracking.Type.LOCKED_C;
    }

    public boolean isValidated() {
        return status != Status.CONFIRMED &&
                status != Status.CLOSED &&
                status != Status.OUTDATED &&
                status != Status.AUTO_CLOSED &&
                status != Status.REOPEN;
    }
}
