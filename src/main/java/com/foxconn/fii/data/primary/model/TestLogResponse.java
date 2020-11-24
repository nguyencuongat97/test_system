package com.foxconn.fii.data.primary.model;

import lombok.Data;

import java.util.Date;

@Data
public class TestLogResponse {

    private String factory;

    private String modelName;

    private String groupName;

    private String stationName;

    private Date time;

    private String serial;

    private String errorCode;

    private String errorDescription;

    private String spec;

    private float value;

    private int cycleTime;
}
