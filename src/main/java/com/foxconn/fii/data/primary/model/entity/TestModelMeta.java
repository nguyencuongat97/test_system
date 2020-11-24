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
@Table(name = "test_model_meta")
public class TestModelMeta {

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

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "parameter", columnDefinition = "TINYINT(2)")
    private Boolean parameter;

    @Column(name = "visible", columnDefinition = "TINYINT(2)")
    private Boolean visible = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
