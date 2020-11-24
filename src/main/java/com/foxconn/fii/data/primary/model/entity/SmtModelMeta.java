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
@Table(name = "smt_model_meta")
public class SmtModelMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "cft")
    private String cft;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_name_si")
    private String modelNameSI;

    @Column(name = "model_name_mounter")
    private String modelNameMounter;

    @Column(name = "model_name_aoi")
    private String modelNameAoi;

    @Column(name = "link_qty")
    private Integer linkQty;

    @Column(name = "panel_length")
    private Float panelLength;

    @Column(name = "total_part")
    private Integer totalPart;

//    @Column(name = "printer_check_rate")
//    private Integer printerCheckRate;
//
//    @Column(name = "printer_clean_rate")
//    private Integer printerCleanRate;

    @Column(name = "customer_name")
    private String customerName;
}
