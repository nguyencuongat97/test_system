package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


@Data
@Entity
@Table(name = "re_io_daily")
public class RepairIODaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private TestRepairSerialError.Status status;

    @Column(name = "total_qty")
    private int total;

    @Column(name = "input_qty")
    private int input;

    @Column(name = "output_qty")
    private int output;

    @Column(name = "remain_qty")
    private int remain;

    @Transient
    public float tat;

    public float getYieldRate() {
        return input != 0 ? output * 100.0f / input : 0.0f;
    }

}
