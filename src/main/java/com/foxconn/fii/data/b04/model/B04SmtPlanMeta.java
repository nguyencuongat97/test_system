package com.foxconn.fii.data.b04.model;

import com.foxconn.fii.common.ShiftType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@Table(name = "PLAN_B04_SMT_LG")
public class B04SmtPlanMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model")
    private String modelName;

    @Column(name = "mo")
    private String mo;

    @Column(name = "line")
    private String lineName;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private ShiftType shift;

    @Column(name = "date")
    private Date shiftTime;

    @Column(name = "qty_plan")
    private int plan;
}
