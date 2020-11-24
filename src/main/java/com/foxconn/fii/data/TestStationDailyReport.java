package com.foxconn.fii.data;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TestStationDailyReport {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM/dd HH:mm");

    private int id;

    private String factory;

    private String modelName;

    private String lineName;

    private String groupName;

    private String stationName;

    private Date startDate;

    private Date endDate;

    private int wip = 0;

    private int firstFail = 0;

    private int secondFail = 0;

    private int pass = 0;

    private int fail = 0;

    private String mo;

    private int retest;

    private int repass;

    private long downTime = 0;

    private float hitRate = 0;

    private int plan = 0;

    private int totalOutput;

    private int totalPlan;

    private String status;

    private String owner;

    private String rootCause;

    private String action;

    private Date duedate;

    public int getTestFail() {
        return (firstFail - secondFail) > 0 ? (firstFail - secondFail) : 0;
    }

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
        return pass != 0 ? (pass - secondFail) * 100.0f / pass : 0f;
    }

    public void calculateHitRate(int countStation) {
        hitRate = plan == 0 ? 0 : (wip * countStation * 12 * 100.0f) / plan;
    }

    public TestStationDailyReport clone() {
        try {
            return (TestStationDailyReport) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
