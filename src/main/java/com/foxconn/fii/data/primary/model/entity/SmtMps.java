package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "smt_mps")
public class SmtMps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "cft")
    private String cft;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_name_si")
    private String modelNameSI;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "running_day")
    private Double runningDay;

    @Column(name = "plan_qty")
    private Double plan = 0.0;

    @Transient
    private Double planPart = 0.0;

    @Transient
    private Double planLine = 0.0;

    @Transient
    private Double totalLine = 0.0;

    @Transient
    private Double diff = 0.0;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    private List<SmtMps> mpsList;
}
