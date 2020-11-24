package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "smt_pcas_cycle_time")
public class SmtPcasCycleTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "cycle_time")
    private Float cycleTime;

    @Column(name = "man_power")
    private Integer manPower;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "pcas_line_name")
    private String pcasLineName;

    @Column(name = "side")
    private String side;

    @Column(name = "reflow_speed")
    private Float reflowSpeed;

    @Column(name = "visible")
    private boolean visible = true;
}
