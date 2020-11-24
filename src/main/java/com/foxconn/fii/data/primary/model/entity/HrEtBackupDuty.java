package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_backup_duty_data")
public class HrEtBackupDuty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_site")
    private String employeeSite;

    @Column(name = "employee_depart_name")
    private String employeeDepartName;

    @Column(name = "employee_class_no")
    private String employeeClassNo;

    @Column(name = "work_date")
    private java.sql.Date workDate;

    @Column(name = "begin_work")
    private Date beginWork;

    @Column(name = "begin_rest")
    private Date beginRest;

    @Column(name = "end_rest")
    private Date endRest;

    @Column(name = "end_work")
    private Date endWork;

    @Column(name = "begin_overtime")
    private Date beginOvertime;

    @Column(name = "end_overtime")
    private Date endOvertime;

    @Column(name = "overtime_count")
    private Double overtimeCount;

    @Column(name = "work_result")
    private String workResult;

    @Column(name = "ow_time")
    private Double owTime;

    @Column(name = "is_modify_result")
    private String isModifyResult;

}
