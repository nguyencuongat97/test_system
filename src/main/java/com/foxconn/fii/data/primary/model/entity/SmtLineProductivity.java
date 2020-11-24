package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "smt_line_productivity")
public class SmtLineProductivity {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM/dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "mo_number")
    private String moNumber;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "wip")
    private int wip = 0;

    @Column(name = "fail")
    private int fail = 0;

    @Column(name = "uph")
    private int uph = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    private String customerName = "";

    @Transient
    private float accumulate = 0;

    public float getYieldRate() {
        return wip > 0 && wip > fail ? (wip - fail) * 100.0f / wip : 0;
    }

    public float getHitRate() {
        return uph > 0 ? wip * 100.0f / uph : 0;
    }

    @JsonProperty
    public String getTimeSpan() {
        if (startDate == null || endDate == null) {
            return "";
        }
        return TIME_FORMAT.format(startDate) + " - " + TIME_FORMAT.format(endDate);
    }

    public void setTimeSpan(String timeSpan) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        if (StringUtils.isEmpty(timeSpan)) {
            return;
        }

        String[] datepart = timeSpan.split(" ");
        if (datepart.length < 2) {
            return;
        }

        try {
            String[] range = datepart[1].split("-");
            if (range.length < 2) {
                return;
            }
            this.startDate = df.parse(datepart[0] + " " + range[0]);
            this.endDate = df.parse(datepart[0] + " " + range[1]);
        } catch (Exception e) {
        }
    }

    public static SmtLineProductivity of (Map<String, Object> objectMap) {
        SmtLineProductivity ins = new SmtLineProductivity();
        ins.setTimeSpan((String) objectMap.get("DATE_TIME"));
        ins.setModelName((String) objectMap.get("MODEL_NAME"));
        ins.setGroupName((String) objectMap.get("GROUP_NAME"));
        ins.setMoNumber((String) objectMap.get("MO_NUMBER"));

        Number wip = (Number) objectMap.get("ACTUAL_PCB_QTY");
        if (wip != null) {
            ins.setWip(wip.intValue());
        }

        Number fail = (Number) objectMap.get("NG_PCB_QTY");
        if (fail != null) {
            ins.setFail(fail.intValue());
        }

        String uph = (String) objectMap.get("UPH");
        if (!StringUtils.isEmpty(uph)) {
            try {
                ins.setWip(Integer.parseInt(uph));
            } catch (Exception e) {

            }
        }

        return ins;
    }
}
