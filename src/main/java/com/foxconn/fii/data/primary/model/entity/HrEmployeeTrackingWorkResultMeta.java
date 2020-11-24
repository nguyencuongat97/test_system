package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_work_result_meta")
public class HrEmployeeTrackingWorkResultMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_result_cn")
    private String workResultCn;

    @Column(name = "work_result_en")
    private String workResultEn;

    @Column(name = "work_result_vi")
    private String workResultVi;

    @Column(name = "work_result_factor")
    private Double workResultFactor;

    @Column(name = "is_work_day")
    private Boolean isWorkDay;

    @Column(name = "none_work_day")
    private Boolean noneWorkDay;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
