package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_backup_overtime_data")
public class HrEtBackupOvertimeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_depart_name")
    private String employeeDepartName;

    @Column(name = "work_date")
    private java.sql.Date workDate;

    @Column(name = "is_overtime")
    private Boolean isOvertime;

    @Column(name = "actual_overtime_count")
    private Double actualOvertimeCount;

    @Column(name = "begin_overtime")
    private Date beginOvertime;

    @Column(name = "end_overtime")
    private Date endOvertime;



}
