package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "test_spec_lock")
public class TestSpecLock {

    @Id
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "wip")
    private Integer wip;

    @Column(name = "retest_rate")
    private Float retestRate;

    @Column(name = "first_pass_rate")
    private Float firstPassRate;

    public static final TestSpecLock DEFAULT_SPEC = new TestSpecLock();
    static {
        DEFAULT_SPEC.setWip(50);
        DEFAULT_SPEC.setRetestRate(10.0f);
        DEFAULT_SPEC.setFirstPassRate(90.0f);
    }
}
