package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "test_pc_email_list")
public class TestPcEmailList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "department")
    private String department;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled", columnDefinition = "TINYINT(2)")
    private Boolean enabled = true;

    @Transient
    private String emailList;
}
