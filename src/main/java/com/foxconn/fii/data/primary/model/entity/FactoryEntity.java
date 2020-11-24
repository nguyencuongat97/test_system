package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "re_cl_factory")
public class FactoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;
}
