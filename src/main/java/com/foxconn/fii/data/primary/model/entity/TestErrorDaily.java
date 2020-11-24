package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "test_error_daily")
public class TestErrorDaily {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM/dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "test_fail")
    private int testFail = 0;

    @Column(name = "fail")
    private int fail = 0;

    @Column(name = "mo_number")
    private String mo;

    @Basic
    private int dppm = 0;

    @Transient
    private String lineName;

    @JsonProperty
    public String getShiftTime() {
        return startDate == null || endDate == null ? "" : TIME_FORMAT.format(startDate) + " - " + TIME_FORMAT.format(endDate);
    }

    @Transient
    private String errorDescription;

    @Transient
    private int wip;

    @Transient
    private int pass;

    @Transient
    private int totalTestFail;

    @Transient
    private Long trackingId;

    @Transient
    private String owner;

    @Transient
    private String rootCause;

    @Transient
    private String action;

    @Transient
    private String description;

    @Transient
    private Date duedate;

    @Transient
    private TestNoteError.Status status;

    @Transient
    private int idNoteError;

    @Transient
    private String customerName;

    public static TestErrorDaily clone(TestErrorDaily error) {
        TestErrorDaily testError = new TestErrorDaily();
        BeanUtils.copyProperties(error, testError);
        return testError;
    }

    public static TestErrorDaily merge(TestErrorDaily error, TestErrorDaily tmp) {
        error.setWip(error.getWip() + tmp.getWip());
        error.setTestFail(error.getTestFail() + tmp.getTestFail());
        error.setFail(error.getFail() + tmp.getFail());
        return error;
    }
}
