package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "test_model")
public class TestModel {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

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
    private Map<String, TestErrorDaily> errorMetaMap;

    @Transient
    private String lineName = "";

    @Transient
    private String customer = "";

    @Transient
    private String customerName = "";

    @Transient
    private int plan;

    @Transient
    private List<TestGroup> groupList;

    @Transient
    private int outputWeekly;

    @Transient
    private int planMonth;

    @Transient
    private int output;

    @Transient
    private float retestRate;

    @Transient
    private float firstPassRate;

    @Transient
    private float ete;

    @Transient
    private float retestRateTarget = 10;

    public float getRetestRate() {
        return (wip > 100 || (startDate != null && endDate != null && TimeSpan.of(startDate, endDate).isNow())) ? retestRate : 0;
    }

//    @JsonProperty
//    public float getETE() {
//        return pass != 0 && (pass > secondFail) ? (pass - secondFail) * 100.0f / pass : 0f;
//    }

    @JsonProperty
    public float getHitRate() {
        return (plan != 0) ? output * 100.0f / plan : 0f;
    }

    @JsonProperty
    public float getHitRateMonth() {
        return (planMonth != 0) ? outputWeekly * 100.0f / planMonth : 0f;
    }

    @JsonProperty
    public String getShiftTime() {
        if (startDate == null || endDate == null) {
            return "";
        }
        return TIME_FORMAT.format(startDate) + " - " + TIME_FORMAT.format(endDate);
    }

//    public void setGroupList(List<TestGroup> groupList) {
//        this.groupList = new ArrayList<>();
//
//        int input = 0;
//        int output = -1;
//        int outputFTRC = -1;
//        int totalFF = 0;
//        int totalSF = 0;
//        float retestRate = 0;
//        float firstPassRate = 100.0f;
//        float ete = 100.0f;
//        for (TestGroup group : groupList) {
//            if (group.getGroupName().startsWith("UK") || group.getGroupName().startsWith("UP") || group.getGroupName().contains("OBA") || group.getGroupName().contains("VI")) {
//                continue;
//            }
//            this.groupList.add(group);
//
//            if (input < group.getWip()) {
//                input = group.getWip();
//            }
//
//            if (output == -1 || group.getPass() < output) {
//                output = group.getPass();
//            }
//            if ((group.getGroupName().startsWith("FT") || group.getGroupName().startsWith("RC")) && (outputFTRC == -1 || group.getPass() < outputFTRC)) {
//                outputFTRC = group.getPass();
//            }
//
//            totalFF += group.getFirstFail();
//            totalSF += group.getSecondFail();
//            retestRate += group.getRetestRate();
//            firstPassRate *= (group.getFirstPassRate() / 100.0f);
//            ete *= (group.getYieldRate() / 100.0f);
//        }
//        if (outputFTRC > -1) {
//            output = outputFTRC;
//        }
//        if (output < 0) {
//            output = 0;
//        }
//
//        this.wip = input;
//        this.pass = output;
//        this.output = output;
//        this.firstFail = totalFF;
//        this.secondFail = totalSF;
//        this.retestRate = retestRate;
//        this.firstPassRate = firstPassRate;
//        this.ete = ete;
//    }

    public void setGroupList(List<TestGroup> groupList, Map<String, TestGroupMeta> groupMetaMap) {
        this.groupList = new ArrayList<>();

        int input = 0;
        int output = -1;
        int outputDefault = -1;
        int totalFF = 0;
        int totalSF = 0;
        float retestRate = 0;
        float firstPassRate = 100.0f;
        float ete = 100.0f;
        float retestRateTarget = 0;
        for (TestGroup group : groupList) {
//            if (group.getGroupName().startsWith("UK") || group.getGroupName().startsWith("UP") || group.getGroupName().contains("OBA") || group.getGroupName().contains("VI")) {
//                continue;
//            }
            if (!groupMetaMap.containsKey(group.getGroupName())) {
                continue;
            }

            this.groupList.add(group);

            if (input < group.getWip()) {
                input = group.getWip();
            }

            if (output == -1 || group.getPass() < output) {
                outputDefault = group.getPass();
            }
            if (groupMetaMap.get(group.getGroupName()) != null &&
                    groupMetaMap.get(group.getGroupName()).getRemark() != null &&
                    groupMetaMap.get(group.getGroupName()).getRemark().contains("OUT")) {
                output = group.getPass();
            }

            totalFF += group.getFirstFail();
            totalSF += group.getSecondFail();
            retestRate += group.getRetestRate();
            firstPassRate *= (group.getFirstPassRate() / 100.0f);
            ete *= (group.getYieldRate() / 100.0f);
            retestRateTarget += groupMetaMap.get(group.getGroupName()).getTargetRetestRate();
        }

        if (output == -1 && outputDefault >= 0) {
            output = outputDefault;
        }
        if (output < 0) {
            output = 0;
        }

        this.wip = input;
        this.pass = output;
        this.output = output;
        this.firstFail = totalFF;
        this.secondFail = totalSF;
        this.retestRate = retestRate;
        this.firstPassRate = firstPassRate;
        this.ete = ete;
        this.retestRateTarget = retestRateTarget;
    }

//    public void setGroupDailyList(List<TestGroupDaily> groupDailyList) {
//        List<TestGroup> groupList = groupDailyList.stream().map(group -> {
//            TestGroup tmp = new TestGroup();
//            BeanUtils.copyPropertiesIgnoreNull(group, tmp);
//            return tmp;
//        }).collect(Collectors.toList());
//
//        setGroupList(groupList);
//    }

    public void setGroupDailyList(List<TestGroupDaily> groupDailyList, Map<String, TestGroupMeta> groupMetaMap) {
        List<TestGroup> groupList = groupDailyList.stream().map(group -> {
            TestGroup tmp = new TestGroup();
            BeanUtils.copyPropertiesIgnoreNull(group, tmp);
            return tmp;
        }).collect(Collectors.toList());

        setGroupList(groupList, groupMetaMap);
    }
}
