package com.foxconn.fii.data.primary.model.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Entity
@Table(name = "test_line_meta")
public class TestLineMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "y")
    private Integer top = 0;

    @Column(name = "x")
    private Integer left = 0;

    @Column(name = "width")
    private Integer width = 0;

    @Column(name = "height")
    private Integer height = 0;

    @Column(name = "icon_y")
    private Integer iconTop = 0;

    @Column(name = "icon_x")
    private Integer iconLeft = 0;

    @Column(name = "side")
    private String side;

    @Transient
    public List<TestGroup> groupList;

    @Transient
    public List<TestResource> resourceList;
}
