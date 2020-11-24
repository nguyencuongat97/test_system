package com.foxconn.fii.data.sfc.model;

import lombok.Data;

import java.util.Date;

@Data
public class TestWipTracking {

    private String serialNumber;

    private String mo;

    private String lineName;

    private String modelName;

    private String groupName;

    private String stationName;

    private Date inStationTime;

    private String wipGroup;
}
