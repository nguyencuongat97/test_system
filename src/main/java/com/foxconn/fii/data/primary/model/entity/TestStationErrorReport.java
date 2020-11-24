package com.foxconn.fii.data.primary.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
@Table(name = "test_station_error_report")
public class TestStationErrorReport {
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

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "root_cause")
    private String rootCause = "";

    @Column(name = "action")
    private String action = "";

    @Column(name = "status")
    private Status status;

    @Column(name = "owner")
    private String owner = "";

    @Column(name = "attach_file")
    private String attachFile;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Column(name = "due_date")
    private Date dueDate;

    @Transient
    private int wip;

    @Transient
    private int pass;

    @Transient
    private int firstFail;

    @Transient
    private int secondFail;

    @Transient
    private MultipartFile uploadedFile;

    @Transient
    private String timeSpan;

    public float getRetestRate() {
        return (wip != 0) && ((firstFail - secondFail) > 0) ? (firstFail - secondFail) * 100.0f / wip : 0f;
    }

    public float getFirstPassRate() {
        return (wip != 0) && ((wip - firstFail) > 0) ? (wip - firstFail) * 100.0f / wip : 0f;
    }

    public float getYieldRate() {
        return wip != 0 ? pass * 100.0f / wip : 0f;
    }

    public enum Status {
        ON_GOING,
        DONE,
        PENDING,
        CLOSE
    }
}
