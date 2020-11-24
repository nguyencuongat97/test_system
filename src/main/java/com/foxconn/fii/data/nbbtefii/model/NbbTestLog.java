package com.foxconn.fii.data.nbbtefii.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(schema = "TE_NBB_FII", name = "TE_CPK_DATA")
public class NbbTestLog {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "project_id")
    private String customer;

    @Column(name = "model")
    private String modelName;

    @Column(name = "station")
    private String groupName;

    @Column(name = "ate")
    private String stationName;

    @Column(name = "SN")
    private String serial;

    @Column(name = "START_TIME")
    private Date dateTime;

    @Column(name = "full_work_date")
    private String fullWorkDate;

    @Column(name = "status")
    private String status;

    @Column(name = "step_number")
    private int stepNumber;

    @Column(name = "test_item")
    private String testItem;

    @Column(name = "value")
    private String pValue;

    @Column(name = "lsl")
    private String lLimit;

    @Column(name = "usl")
    private String hLimit;

    public enum Status {
        PASS,
        FAIL
    }
}
