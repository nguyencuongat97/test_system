package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "test_solution_meta_new")
public class TestSolutionMetaNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String factory;

    @Column(name = "customer")
    private String customer;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "root_cause")
    private String rootCause;

    @Column(name = "action")
    private String action;

    @Column(name = "official", columnDefinition = "TINYINT(2)")
    private Boolean official = false;

}
