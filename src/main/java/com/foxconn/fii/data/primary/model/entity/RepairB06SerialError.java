package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name =  "reb06_repair_serial_error")
public class RepairB06SerialError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "mo_number")
    private String mo;

    @Column(name = "test_group")
    private String testGroup;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "test_time")
    private Date testTime;

    @Column(name = "emp_no")
    private String empNo;

    @Column(name = "repairer")
    private String repairer;

    @Column(name = "repair_time")
    private Date repairTime;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "reason_desc_e")
    private String reasonDescE;

    @Column(name = "location")
    private String location;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
