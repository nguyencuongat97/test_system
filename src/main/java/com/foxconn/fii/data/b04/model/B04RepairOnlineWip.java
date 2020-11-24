package com.foxconn.fii.data.b04.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "RE_CPEI_ONLINE_WIP")
public class B04RepairOnlineWip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_label")
    private String serialLabel; // serialNumber

    @Column(name = "PRODUCT")
    private String product; // modelName

    @Column(name = "R_STATION")
    private String groupStation; // GroupName

    @Column(name = "ERROR_CODE")
    private String errorCode; // errorCode

    @Column(name = "MO")
    private String mo; // mo

    @Column(name = "DATE_TIME_INPUT")
    private Date timeInput; // testTime

    @Column(name = "DATE_TIME_OUTPUT")
    private Date timeOutput;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DATE_TIME_IN_LINE")
    private Date timeInLine;

    @Column(name = "REPAIRER")
    private String repairer;

    @Column(name = "REMARK")
    private String reamrk;

    @Column(name = "SHIFT")
    private String shift;

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Column(name = "REASON_CODE")
    private String reasonCode;

    @Column(name = "ID_NAME")
    private String idName;

}
