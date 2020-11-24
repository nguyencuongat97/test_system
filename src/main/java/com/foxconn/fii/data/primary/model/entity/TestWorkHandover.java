package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxconn.fii.common.ShiftType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_work_handover")
public class TestWorkHandover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName = "";

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "shift_time")
    private Date workDate;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private ShiftType shift;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "employee_id")
    private String owner;

    @Column(name = "station_ok")
    private String stationOK;

    @Column(name = "station_ng")
    private String stationNG;

    @Column(name = "station_ng_top")
    private String stationNGTop;

    @Column(name = "setup_or_add")
    private String setupOrAdd;

    @Column(name = "repair")
    private String repair;

    @Column(name = "abnormal")
    private String abnormal;

    @Column(name = "notice")
    private String notice;

    @Transient
    private String employeeNo;
}
