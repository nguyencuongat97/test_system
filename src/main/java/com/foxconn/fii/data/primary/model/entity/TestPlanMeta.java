package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxconn.fii.common.ShiftType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "test_plan_meta")
public class TestPlanMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "mo")
    private String mo;

    @Column(name = "line_name")
    private String lineName;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Column(name = "shift_time")
    private Date shiftTime;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private ShiftType shift;

    @Column(name = "plan_qty")
    private int plan;

    @Column(name = "total")
    private int total = 0;

    @Column(name = "output_qty")
    private int output = 0;

    @Column(name = "total_output_qty")
    private int totalOutput = 0;

    @Column(name = "work_time")
    private float workTime = 12;

    @Column(name = "demand")
    private int demand = 0;

    @Column(name = "note")
    private String note = "";

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "type")
    private Type type;

    @Column(name = "remark")
    private Remark remark = Remark.ONLINE;


    @Transient
    private int moQty = 0;

    @Transient
    private int uph = 0;

    @Transient
    private int planMonth;

    @Transient
    private int shiftTimeMonth;

    public enum Type {
        HOURLY,
        DAILY,
        MONTHLY
    }

    public enum Remark {
        ONLINE,
        OPEN,
        CLOSE
    }

}
