package com.foxconn.fii.data.primary.model.entity;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.TrackingResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_resource")
public class TestResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_no")
    private String employeeNo;

    @Column(name = "vn_name")
    private String name;

    @Column(name = "cn_name")
    private String chineseName;

    @Column(name = "dem")
    private String dem;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "shift")
    private ShiftType shift;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "phone")
    private String phone;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "experience")
    private Date experience;

    @Column(name = "experience_detail")
    private String experienceDetail;

    @Column(name = "level")
    private String level;

    @Column(name = "resource_group")
    private String group = "ALL";

    @Column(name = "group_level")
    private Integer groupLevel;

    @Column(name = "work_status")
    private Boolean workStatus = true;

    @Column(name = "work_off_day")
    private Integer workOffDay;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "status")
    @Enumerated
    private Status status = Status.FREE;

    @Column(name = "tracking_id")
    private Long trackingId;

    @Column(name = "avatar")
    private String avatar;

    @Transient
    private TrackingResponse tracking;

    @Transient
    private int taskNumber;

    @Transient
    private int taskSuccess;

    @Transient
    private long processingTime;

    @Transient
    private int totalScore;

    public long getExperienceYears() {
        if (experience == null) {
            return 0;
        }
        return (System.currentTimeMillis() - experience.getTime()) / (365 * 86400000L);
    }

    public enum Status {
        FREE,
        ON_DUTY
    }
}
