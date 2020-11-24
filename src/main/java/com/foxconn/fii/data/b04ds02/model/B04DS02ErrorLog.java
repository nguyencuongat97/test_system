package com.foxconn.fii.data.b04ds02.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

@Data
public class B04DS02ErrorLog {

    private String modelName;

    private String groupName;

    private String stationName;

    private String tester;

    private Integer chamber;

    private Timestamp time;

    private String serial;

    private String errorCode;

    private String errorDescription;

    private String lsl;

    private String usl;

    private String value;

    private Integer cycle;

    public static B04DS02ErrorLog of (Map<String, Object> row) {
        B04DS02ErrorLog errorLog = new B04DS02ErrorLog();
        errorLog.setModelName((String)row.get("model_name"));
        errorLog.setGroupName((String)row.get("group_name"));
        errorLog.setStationName((String)row.get("station_name"));
        errorLog.setTester((String)row.get("tester"));
        errorLog.setChamber((Integer)row.get("chamber"));
        errorLog.setTime((Timestamp) row.get("time"));
        errorLog.setSerial((String)row.get("serial"));
        errorLog.setErrorCode((String)row.get("error_code"));
        errorLog.setErrorDescription((String)row.get("error_description"));
        errorLog.setLsl((String) row.get("lsl"));
        errorLog.setUsl((String)row.get("usl"));
        errorLog.setValue((String)row.get("value"));
        errorLog.setCycle((Integer) row.get("cycle"));
        return errorLog;
    }
}
