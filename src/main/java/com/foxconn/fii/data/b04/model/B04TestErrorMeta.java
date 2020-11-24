package com.foxconn.fii.data.b04.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "b04_error_code")
public class B04TestErrorMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_detail")
    private String description;
}
