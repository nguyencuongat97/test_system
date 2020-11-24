package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(schema = "TEA02", name = "COPYIC_DATA")
public class CopyIcData {

    @Id
    private String id;

    @Column(name = "machine")
    private String machine;

    @Column(name = "activities")
    private String activities;

    @Column(name = "time")
    private Date time;

    @Column(name = "IPQC_ID")
    private String ipqcId;

    @Column(name = "OP_ID")
    private String opId;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "PART_NUMBER")
    private String partNumber;

    @Column(name = "CHECKSUM")
    private String checksum;

    @Column(name = "IC_COLOR")
    private String icColor;

    @Column(name = "BIN_FILE")
    private String binFile;

    @Column(name = "PASS_QTY")
    private Integer passQty;

    @Column(name = "FAIL_QTY")
    private Integer failQty;

    @Column(name = "TOTAL_QTY")
    private Integer totalQty;

    @Transient
    private Integer plan;

    @Transient
    private int machineNumber;
}
