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
@Table(name = "test_group_meta")
public class TestGroupMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "customer")
    private String customer;

    @Column(name = "stage")
    private String stage;

    @Column(name = "sub_stage")
    private String subStage;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "cycle_time")
    private int cycleTime = 0;

    @Column(name = "operator_time")
    private int operatorTime = 0;

    @Column(name = "parameter", columnDefinition = "TINYINT(2)")
    private Boolean parameter;

    @Column(name = "number_equipment")
    private Integer equipment = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "step")
    private Integer step = 999;

    @Column(name = "visible", columnDefinition = "TINYINT(2)")
    private Boolean visible = true;

    /** IN, OUT, WIP */
    @Column(name = "remark2")
    private String remark = "";

    @Column(name = "warning_retest_rate")
    private Float errorRetestRate = 10.0f;

    @Column(name = "target_retest_rate")
    private Float targetRetestRate = 4.0f;

    @Column(name = "bonus_retest_rate")
    private Float bonusRetestRate = 3.0f;
}
