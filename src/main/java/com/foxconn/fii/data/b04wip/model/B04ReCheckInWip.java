package com.foxconn.fii.data.b04wip.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "RE_CPEI_WIP")
public class B04ReCheckInWip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_label")
    private String serial;

    @Column(name = "mo")
    private String mo;

    @Column(name = "product")
    private String modelName;

    @Column(name = "r_station")
    private String stationName;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "date_time_input")
    private Date inputTime;

    @Column(name = "date_time_in_line")
    private Date timeInLine;

    @Column(name = "date_time_output")
    private Date outputTime;

    @Column(name = "id_name")
    private String owner;

    @Column(name = "remark")
    private String remark;

}
