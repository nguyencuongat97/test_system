package com.foxconn.fii.data.primary.model;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class PcasManpower {

    private String factory;

    private String cft;

    private String modelName;

    private String sectionName;

    private Float cycleTime;

    private Integer pcasManPower;

    private Map<String, ManPowerItem> itemList = new TreeMap<>();

    public double getDailyOutput() {
        return 3600 / cycleTime * 18 * 0.9;
    }

    @Data
    public static class ManPowerItem {
        private String datetime;

        private Double runningDay;

        private Double forecast;

        private Double mPoint;

        private Double manPower;
    }
}
