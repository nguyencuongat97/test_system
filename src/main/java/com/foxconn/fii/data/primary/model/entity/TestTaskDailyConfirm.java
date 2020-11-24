package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.TeCftTaskDailyConfirmData;
import com.foxconn.fii.data.primary.model.TeOnlineTaskDailyConfirmData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "test_task_daily_confirm")
public class TestTaskDailyConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "dem")
    private String dem;

    @Column(name = "input_date")
    private Date inputDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "shift")
    private ShiftType shift;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "resource_group")
    private String resourceGroup;

    @Column(name = "line")
    private String line;

    @Column(name = "project")
    private String project;

    @Column(name = "detail")
    private String detail;

    @Column(name = "work_off_day")
    private Integer workOffDay;

    @Column(name = "leader_confirm")
    private String leaderConfirm;

    @Column(name = "score")
    private Integer score;
//
//    @CreationTimestamp
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt;

    @Transient
    private TeOnlineTaskDailyConfirmData teOnlineData;

    @Transient
    private TeCftTaskDailyConfirmData teCftData;

    public void parseData(ObjectMapper objectMapper, String dem) {
        try {
            if ("TE-ONLINE".equalsIgnoreCase(dem)) {
                teOnlineData = objectMapper.readValue(this.detail, TeOnlineTaskDailyConfirmData.class);
            } else if ("TE-CFT".equalsIgnoreCase(dem)) {
                teCftData = objectMapper.readValue(this.detail, TeCftTaskDailyConfirmData.class);
            }
        } catch (Exception e) {
            log.error("parse data error", e);
        }
    }

    public enum Status {
        ON_GOING,
        DONE,
        PENDING,
        CLOSED
    }
}
