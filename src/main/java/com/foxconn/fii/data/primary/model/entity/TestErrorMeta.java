package com.foxconn.fii.data.primary.model.entity;

import com.foxconn.fii.data.b04.model.B04TestErrorMeta;
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
@Table(name = "test_error_meta")
public class TestErrorMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "name")
    private String errorCode;

    @Column(name = "description")
    private String description;

    @Column(name = "component")
    private String component;

    public static TestErrorMeta of(B04TestErrorMeta b04TestErrorMeta) {
        TestErrorMeta errorMeta = new TestErrorMeta();
        errorMeta.setFactory("B04");
        errorMeta.setModelName(b04TestErrorMeta.getModelName());
        errorMeta.setErrorCode(b04TestErrorMeta.getErrorCode());
        errorMeta.setDescription(b04TestErrorMeta.getDescription());
        return errorMeta;
    }
}
