package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

@Data
@Entity
@Table(name = "test_station")
public class TestStation {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM/dd HH:mm");

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

    @Column(name = "station_name")
    private String stationName;

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

    @Column(name = "re_pass")
    private Integer repass = 0;

    @Column(name = "re_fail")
    private Integer refail = 0;

    @Column(name = "mo_number")
    private String mo;

    @Transient
    private int retest;

    @Transient
    private long downTime = 0;

    @Transient
    private float hitRate = 0;

    @Transient
    private int plan = 0;

    @Transient
    private int totalOutput;

    @Transient
    private int totalPlan;

    @Transient
    private Map<String, TestError> errorMetaMap;

    @Transient
    private String status = "WARNING";

    @Transient
    private Long trackingId;

    @Transient
    private Date notifyTime;

    @Transient
    private List<Integer> notifyShiftIndexes = new ArrayList<>();

    @Transient
    private String ip;

    @Transient
    private String testerName;

    @JsonProperty
    public int getTestFail() {
        return (firstFail - secondFail) > 0 ? (firstFail - secondFail) : 0;
    }

    @JsonProperty
    public String getShiftTime() {
        if (startDate == null || endDate == null) {
            return "";
        }
        return TIME_FORMAT.format(startDate) + " - " + TIME_FORMAT.format(endDate);
    }

    public float getRetestRate() {
        return (wip != 0) && ((firstFail - secondFail) > 0) ? (firstFail - secondFail) * 100.0f / wip : 0f;
//        if((wip != 0) && ((firstFail - secondFail) > 0)){
//            if (wip < 100){
//                return 0f;
//            }
//            return (firstFail - secondFail) * 100.0f / wip;
//        }else{
//            return 0f;
//        }
    }

    public float getFirstPassRate() {
        return (wip != 0) && ((wip - firstFail) > 0) ? (wip - firstFail) * 100.0f / wip : 0f;
    }

    public float getYieldRate() {
        return wip != 0 ? pass * 100.0f / wip : 0f;
    }

    public float getETE() {
        return pass != 0 ? (pass - secondFail) * 100.0f / pass : 0f;
    }

    public void calculateHitRate(int countStation) {
        hitRate = plan == 0 ? 0 : (wip * countStation * 12 * 100.0f) / plan;
    }

    public String getUniqueKey() {
        return factory + '_' + modelName + '_' + groupName + '_' + stationName;
    }

    public TestStation clone() {
        try {
            return (TestStation) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
