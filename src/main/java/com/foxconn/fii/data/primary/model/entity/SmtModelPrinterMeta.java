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
@Table(name = "smt_model_printer_meta")
public class SmtModelPrinterMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "side")
    private String side;

    @Column(name = "printer_check_rate")
    private Integer printerCheckRate;

    @Column(name = "printer_clean_rate")
    private Integer printerCleanRate;

}
