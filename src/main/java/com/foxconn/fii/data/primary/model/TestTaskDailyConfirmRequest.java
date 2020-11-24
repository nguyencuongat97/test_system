package com.foxconn.fii.data.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TestTaskDailyConfirmRequest {

    private long id;

    private String factory;

    private String employeeId;

    private String leaderConfirm;

    private String score;


    private Integer retestRate;

    private Integer ntf;

    private Integer hitRate;

    private Integer fiveS;

    private Integer workInteractive;


    private String projectOwner;

    private Integer projectQuantity;

    private String timeSpan;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date duedate;

    private Integer status;

    private Integer abnormal;


    private String improveProject;

    private Integer bonusScore;
}
