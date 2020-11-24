package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_work_shift_meta")
public class HrEmployeeTrackingWorkShiftMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shift_name")
    private String shiftName;

    @Column(name = "shift_code")
    private String shiftCode;

    @Column(name = "shift_section_cn")
    private String shiftSectionCn;

    @Column(name = "shift_section_en")
    private String shiftSectionEn;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "begin_work")
    private Time beginWork;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "begin_rest")
    private Time beginRest;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "end_rest")
    private Time endRest;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "end_work")
    private Time endWork;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "begin_over_time")
    private Time beginOverTime;

//    @Column(name = "work_time")
//    private Double workTime;
//
//    @Column(name = "normal_rest_limit")
//    private Double normalRestLimit;
//
//    @Column(name = "ot_rest_limit")
//    private Double otRestLimit;
//
//    @Column(name = "ot_time_limit")
//    private Double otTimeLimit;

    @Column(name = "lunch_time")
    private Integer lunchTime;

    @Column(name = "dinner_time")
    private Integer dinnerTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
