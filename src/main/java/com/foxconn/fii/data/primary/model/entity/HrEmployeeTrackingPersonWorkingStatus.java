package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "hr_et_person_working_status")
public class HrEmployeeTrackingPersonWorkingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "id_person")
//    private Long idPerson;

    @Column(name = "emp_no")
    private String empNo;

    @Column(name = "work_date")
    private Date workDate;

    @Column(name = "work_status")
    private String workStatus;

    @Column(name = "depart_name")
    private String departName;

    @Column(name = "work_shift")
    private String workShift;

//    @Column(name = "work_result_cn")
//    private String workResultCn;

//    @Column(name = "work_shift_code")
//    private String workShiftCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private java.util.Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

//    public enum WorkStatus {
//        WORKING,
//        MISSING,
//        UNKNOWN
//    }

}
