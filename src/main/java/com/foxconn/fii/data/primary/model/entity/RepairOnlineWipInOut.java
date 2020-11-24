package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_online_wip_in_out")
public class RepairOnlineWipInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "mo")
    private String mo;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "repairer")
    private String repairer;

    @Column(name = "test_time")
    private Date testTime;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "test_location_code")
    private String testLocationCode;

    @Column(name = "repair_time")
    private Date repairTime;

    @Column(name = "reason_code")
    private String reason;

    @Column(name = "workdate")
    private String workdate;

    @Column(name = "shift")
    private String shift;

    @Column(name = "checkin_time")
    private Date checkInTime;
}
