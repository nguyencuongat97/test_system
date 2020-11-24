package com.foxconn.fii.data;

import lombok.Data;

import java.util.Map;

@Data
public class TestCycleTime {

    private String factory;

    private String modelName;

    private String groupName;

    private String stationName;

    private String tester;

    private String chamber;

    private int totalCycle;

    private int totalNumber;

    private int passCycle;

    private int passNumber;

    private int failCycle;

    private int failNumber;


    private int stationNumber = 0;

    private int plan = 0;

    private int realPlan = 0;

    private float workTime = 8;

    private int testerExist = 0;

    private float operationTime = 0;

    private float pingTime = 0;

    private float cycle = 0;

    private int realOutput = 0;


    public float getTotalAvgCycle() {
        return totalNumber == 0 ? 0 : totalCycle * 1.0f / totalNumber;
    }

    public float getPassAvgCycle() {
        return passNumber == 0 ? 0 : passCycle * 1.0f / passNumber;
    }

    public float getFailAvgCycle() {
        return failNumber == 0 ? 0 : failCycle * 1.0f / failNumber;
    }

    public int getTesterDemand() {
        return getPlanUph() != 0 ? (int)Math.ceil((getPassAvgCycle() + operationTime + pingTime) / (3600/getPlanUph()) / 0.9 / 0.95) : 0;
    }

    public int getTesterDiff() {
        return getTesterDemand() > testerExist ?  getTesterDemand() - testerExist : 0;
    }

    public float getPlanUph() {
        return workTime != 0 ? plan/workTime : 0;
    }

    public float getUph() {
        return cycle != 0 ? 3600/cycle : 0;
    }

    public int getUpd() {
        return (int)Math.floor(getUph() * workTime * 0.95 * 0.9f);
    }

    public int getAbility() {
        return getUpd() != 0 ? realOutput/getUpd() : 0;
    }

    public float getRealCycleTime() {
        return testerExist != 0 ? (getPassAvgCycle() + operationTime + pingTime) / testerExist : 0;
    }

    public int getRealUph() {
        return getRealCycleTime() != 0 ? (int)Math.floor(3600/getRealCycleTime()) : 0;
    }

    public int getRealUpd() {
        return (int)Math.floor(getRealUph() * workTime * 0.95 * 0.9f);
    }

    public int getRealAbility() {
        return getRealUpd() != 0 ? realOutput/getRealUpd() : 0;
    }

    public static TestCycleTime mapToTestCycleTime(Map<String, Object> map) {
        TestCycleTime cycleTime = new TestCycleTime();
//        cycleTime.setFactory("B04");
        cycleTime.setModelName((String) map.getOrDefault("model_name", ""));
        cycleTime.setGroupName((String) map.getOrDefault("group_name", ""));
        cycleTime.setStationName((String) map.getOrDefault("station_name", ""));
        cycleTime.setTester((String) map.getOrDefault("tester", ""));
        cycleTime.setChamber((String) map.getOrDefault("chamber", ""));
        cycleTime.setTotalCycle(((Number) map.getOrDefault("total_cycle", "0")).intValue());
        cycleTime.setTotalNumber(((Number) map.getOrDefault("total_number", "0")).intValue());
        cycleTime.setPassCycle(((Number) map.getOrDefault("pass_cycle", "0")).intValue());
        cycleTime.setPassNumber(((Number) map.getOrDefault("pass_number", "0")).intValue());
        cycleTime.setFailCycle(((Number) map.getOrDefault("fail_cycle", "0")).intValue());
        cycleTime.setFailNumber(((Number) map.getOrDefault("fail_number", "0")).intValue());
        return cycleTime;
    }
}
