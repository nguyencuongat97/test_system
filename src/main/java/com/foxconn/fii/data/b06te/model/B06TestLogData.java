package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(schema = "TEA02", name = "TE_TEST_DATA")
public class B06TestLogData {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "mo")
    private String mo;

    @Column(name = "model")
    private String modelName;

    @Column(name = "station")
    private String groupName;

    @Column(name = "ate")
    private String stationName;

    @Column(name = "SN")
    private String serial;

    @Column(name = "START_TIME")
    private Date startDate;

    @Column(name = "END_TIME")
    private Date endDate;

    @Column(name = "ELAPSE_TIME")
    private int testTime;

    @Column(name = "status")
    private String status;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "value")
    private String value;

    @Column(name = "lsl")
    private String lsl;

    @Column(name = "usl")
    private String usl;

}
