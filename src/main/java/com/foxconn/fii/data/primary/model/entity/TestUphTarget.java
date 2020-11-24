package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxconn.fii.common.ShiftType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
@Table(name = "test_uph_target")
public class TestUphTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "customer")
    private String customer;

    @Column(name = "line_name")
    private String lineName = "";

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "work_date")
    private Date workDate;

    @Column(name = "shift_type")
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Column(name = "work_time")
    private int workTime;

    @Column(name = "uph")
    private int uph;

    @Column(name = "time")
    private String time;
}
