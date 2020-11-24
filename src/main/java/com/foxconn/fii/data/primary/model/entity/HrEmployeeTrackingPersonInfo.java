package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_person_info")
public class HrEmployeeTrackingPersonInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_no")
    private String empNo;

//    @Column(name = "card_no")
//    private String cardNo;

    @Column(name = "name")
    private String name;

//    @Column(name = "detected_date")
//    private java.sql.Date detectedDate;

//    @Column(name = "id_office_unit")
//    private Long idOfficeUnit;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "office_code")
    private String officeCode;

    @Column(name = "hire_date")
    private java.sql.Date hireDate;

    @Column(name = "leave_date")
    private java.sql.Date leaveDate;

    @Column(name = "stop_tracking_date")
    private java.sql.Date stopTrackingDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
