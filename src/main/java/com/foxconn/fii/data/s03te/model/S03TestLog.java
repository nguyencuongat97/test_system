package com.foxconn.fii.data.s03te.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "TE_CPK_DATA")
public class S03TestLog {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "model")
    private String modelName;

    @Column(name = "station")
    private String groupName;

    @Column(name = "ate")
    private String stationName;

    @Column(name = "SN")
    private String serial;

    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "work_date")
    private String workDate;

    @Column(name = "full_work_date")
    private String fullWorkDate;

    @Column(name = "status")
    private String status;

    @Column(name = "step_number")
    private int stepNumber;

    @Column(name = "test_item")
    private String testItem;

    @Column(name = "value")
    private Double pValue;

    @Column(name = "lsl")
    private Double lLimit;

    @Column(name = "usl")
    private Double hLimit;

    public enum Status {
        PASS,
        FAIL
    }
}
