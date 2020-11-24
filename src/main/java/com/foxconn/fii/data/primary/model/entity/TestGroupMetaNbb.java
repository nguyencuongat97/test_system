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
@Table(name = "test_group_meta_nbb")
public class TestGroupMetaNbb {

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

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "step")
    private Integer step;

    @Column(name = "remark")
    private Integer remark;

    @Column(name = "te_nbb_db_table_name")
    private String teNbbDbTableName;

}
