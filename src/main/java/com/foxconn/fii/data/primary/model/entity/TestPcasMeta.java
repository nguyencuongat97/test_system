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
@Table(name = "test_pcas_meta")
public class TestPcasMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "cycle_time")
    private float cycleTime = 0;

    @Column(name = "work_time")
    private float workTime = 0;

    @Column(name = "operator_time")
    private float operatorTime = 0;

    @Column(name = "ping_time")
    private float pingTime = 0;

    @Column(name = "tester_exist")
    private int testerExist = 0;
}
