package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_fixed_history")
public class TestFixedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "notify_id")
    private long notifyId;

    @Column(name = "solution_id")
    private int solutionId;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "employee")
    private String employee;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "notified_at", updatable = false)
    private Date notifiedAt;

    @CreationTimestamp
    @Column(name = "confirmed_at", updatable = false)
    private Date confirmedAt;

    @Column(name = "pass")
    private int pass = 0;

    @Column(name = "fail")
    private int fail = 0;

    @Column(name = "end_tracking_at")
    private Date endTrackingAt;

    @Column(name = "status")
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Transient
    private String why;

    @Transient
    private String action;

    public enum Status {
        TRACKING,
        DONE
    }
}
