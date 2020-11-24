package com.foxconn.fii.data.b04.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "RE_CPEI_PTH_WIP")
public class B04RepairPTHCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product")
    private String modelName;

    @Column(name = "date_time_input")
    private Date inputTime;

    @Column(name = "qty")
    private Integer inputQty;

}
