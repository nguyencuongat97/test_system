package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "hr_et_employee_counting")
public class HrEtEmployeeCounting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "depart_name")
    private String departName;

    @Column(name = "count_type")
    private String countType;

    @Column(name = "count_qty")
    private Integer countQty;

    @Column(name = "work_shift")
    private String workShift;

    @Column(name = "work_date")
    private Date workDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private java.util.Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

}
