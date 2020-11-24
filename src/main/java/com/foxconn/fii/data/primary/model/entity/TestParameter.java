package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "test_cpk_parameter")
public class TestParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "customer")
    private String customer;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "parameter")
    private String parameters;

    @Column(name = "l_limit")
    private Double lowSpec;

    @Column(name = "h_limit")
    private Double highSpec;

    @Column(name = "schedule_time")
    private String scheduleTime;

    @Column(name = "schedule_mail_list")
    private String scheduleMailList;

    @Column(name = "schedule_all_station")
    private Boolean scheduleAllStation;

    @Column(name = "alarm_time_interval")
    private Integer alarmTimeInterval;

    @Column(name = "alarm_cpk_target")
    private Float alarmCpkTarget = 1.33f;

    @Column(name = "alarm_mail_list")
    private String alarmMailList;

    @Column(name = "alarm_last_time")
    private Date alarmLastTime;

    @Column(name = "alarm_all_station")
    private Boolean alarmAllStation;

    public boolean getStatus() {
        return !StringUtils.isEmpty(scheduleTime) || alarmTimeInterval != null;
    }
}
