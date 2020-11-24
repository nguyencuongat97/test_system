package com.foxconn.fii.data.primary.model;

import lombok.Data;

import java.util.Date;

@Data
public class TeCftTaskDailyConfirmData {

    private String projectOwner;

    private Integer projectQuantity = 0;

    private String timeSpan;

    private Date duedate;

    private Integer status = 0;

    private Integer abnormal = 0;

    private String improveProject = "";

    private Integer bonusScore = 0;

    public int getScore() {
        return status + abnormal + bonusScore;
    }
}
