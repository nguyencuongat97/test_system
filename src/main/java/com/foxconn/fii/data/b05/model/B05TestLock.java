package com.foxconn.fii.data.b05.model;

import com.foxconn.fii.common.ShiftType;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "SPREADSHEETDATA")
public class B05TestLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "station_name")
    private String groupName;

    @Column(name = "ate_name")
    private String stationName;

    @Column(name = "date_time")
    private Date shiftTime;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private ShiftType shift;

    @Column(name = "lock_status")
    private String lockStatus;

    @Column(name = "lock_history")
    private String lockHistory;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "lock_detail")
    private String lockDetail;

    @Column(name = "error_loop")
    private String errorLoop;

    @Column(name = "wip_qty")
    private Float wip;

    @Column(name = "first_fail")
    private Float firstFail;

    @Column(name = "repair_qty")
    private Float repair;

    @Column(name = "pass_qty")
    private Float pass;

    @Basic
    private String why;

    @Basic
    private String action;

    @Basic
    private String owner;

    @Column(name = "date_time_lock")
    private Timestamp dateTimeLock;

    @Column(name = "date_time_unlock")
    private Timestamp dateTimeUnlock;

    @Column(name = "date_time_first_dut")
    private Timestamp dateTimeFirstDUT;

}
