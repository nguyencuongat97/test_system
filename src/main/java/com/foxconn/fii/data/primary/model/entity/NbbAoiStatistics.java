package com.foxconn.fii.data.primary.model.entity;

import com.foxconn.fii.common.TimeSpan;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "aoi_statistics_nbb")
public class NbbAoiStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "mo_number")
    private String moNumber;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "side")
    private String side;

    @Column(name = "machine_type")
    private String machineType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "work_date")
    private String workDate;

    @Column(name = "work_section")
    private Integer workSection;

    @Column(name = "total_qty")
    private int total = 0;

    @Column(name = "pass_qty")
    private int pass = 0;

    @Column(name = "aoi_pass_qty")
    private int aoiPass = 0;

    @Column(name = "worker_pass_qty")
    private int wkPass = 0;

    @Column(name = "worker_fail_qty")
    private int wkFail = 0;

    @Column(name = "repass_qty")
    private int repass = 0;

    @Column(name = "refail_qty")
    private int refail = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public float getYieldRate() {
        return getWip() != 0 ? pass * 100.0f / getWip() : 0;
    }

    public int getWip() {
        return pass + wkFail /*+ repass + refail*/;
    }

    @Transient
    private String timeSpan;

    public String getTimeSpan() {
        if (StringUtils.isEmpty(timeSpan)) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            return startDate != null && endDate != null ? df.format(startDate) + " - " + df.format(endDate) : "";
        }
        return timeSpan;
    }
}
