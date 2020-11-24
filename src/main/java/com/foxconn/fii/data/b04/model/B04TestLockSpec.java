package com.foxconn.fii.data.b04.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TESTER_LOCK_SPEC")
public class B04TestLockSpec {

    @Id
    private int id;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "station_name")
    private String groupName;

    @Column(name = "error_code_loop")
    private int errorLoop;

    @Column(name = "wip_qty")
    private Integer wip;

    @Column(name = "retest_rate")
    private Float retestRate;

    @Column(name = "first_pr")
    private Float fistPassRate;
}
