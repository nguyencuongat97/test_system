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
@Table(name = "test_resource_group_model")
public class TestResourceGroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "group_name")
    private String resourceGroupName;

    @Column(name = "model_name")
    private String modelName;
}
