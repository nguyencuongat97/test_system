package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "test_repair_serial_error")
public class TestRepairSerialError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(name = "tester")
    private String tester;

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

    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "workdate")
    private String workdate;

    @Column(name = "shift")
    private String shift;

    @Column(name = "status")
    private Status status;

    @Column(name = "checkin_time")
    private Date checkInTime;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public enum Status {
        UNDER_REPAIR,
        REPAIRED,
        BC8M,
        BC2M,
        RMA,
        OTHER
    }
}
