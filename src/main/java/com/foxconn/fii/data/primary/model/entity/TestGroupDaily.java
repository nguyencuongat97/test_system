package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "test_group_daily")
public class TestGroupDaily {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "wip")
    private int wip = 0;

    @Column(name = "first_fail")
    private int firstFail = 0;

    @Column(name = "second_fail")
    private int secondFail = 0;

    @Column(name = "pass")
    private int pass = 0;

    @Column(name = "fail")
    private int fail = 0;

    @Column(name = "mo_number")
    private String mo;

    @Transient
    private int plan;

    @Transient
    private String workDate;

    @Transient
    private String stage;

    @Transient
    private int retest;

    @Transient
    private int repass;

    @Transient
    private List<TestStationDaily> stationList;

    @Transient
    private Map<String, TestErrorDaily> errorMetaMap;

    @JsonIgnore
    @Transient
    private TestGroupMeta meta;

    @JsonProperty
    public String getShiftTime() {
        if (startDate == null || endDate == null) {
            return "";
        }
        return TIME_FORMAT.format(startDate) + " - " + TIME_FORMAT.format(endDate);
    }

    public float getRetestRate() {
        return (wip != 0) && ((firstFail - secondFail) > 0) ? (firstFail - secondFail) * 100.0f / wip : 0f;
    }

    public float getFirstPassRate() {
        return (wip != 0) && ((wip - firstFail) > 0) ? (wip - firstFail) * 100.0f / wip : 0f;
    }

    public float getYieldRate() {
        return wip != 0 ? pass * 100.0f / wip : 0f;
    }

    public float getETE() {
        return pass != 0 && pass > secondFail ? (pass - secondFail) * 100.0f / pass : 0f;
    }

    public float getFwrate() {
        return wip != 0 ? 100 - (pass * 100.0f / wip) : 0f;
    }

    public String getStatus() {
        if (meta == null) {
            meta = new TestGroupMeta();
        }
        if (getRetestRate() > meta.getErrorRetestRate()) {
            return "error";
        }
        if (getRetestRate() > meta.getTargetRetestRate()) {
            return "warning";
        }
        return "normal";
    }

    public static TestGroupDaily clone(TestGroupDaily groupDaily) {
        TestGroupDaily testGroup = new TestGroupDaily();
        BeanUtils.copyProperties(groupDaily, testGroup);
        return testGroup;
    }

    public static TestGroupDaily merge(TestGroupDaily group, TestGroupDaily tmp) {
        group.setWip(group.getWip() + tmp.getWip());
        group.setFirstFail(group.getFirstFail() + tmp.getFirstFail());
        group.setSecondFail(group.getSecondFail() + tmp.getSecondFail());
        group.setPass(group.getPass() + tmp.getPass());
        group.setFail(group.getFail() + tmp.getFail());
        return group;
    }

}
