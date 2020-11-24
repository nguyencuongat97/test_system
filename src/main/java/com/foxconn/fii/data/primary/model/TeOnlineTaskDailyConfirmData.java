package com.foxconn.fii.data.primary.model;

import lombok.Data;

@Data
public class TeOnlineTaskDailyConfirmData {

    private int retestRate = 0;

    private String retestRateNote = "";

    private int ntf = 0;

    private String ntfNote = "";

    private int hitRate = 0;

    private String hitRateNote = "";

    private int fiveS = 0;

    private String fiveSNote = "";

    private int workInteractive = 0;

    private String workInteractiveNote = "";

    private String improveProject = "";

    private Integer bonusScore = 0;

    public int getScore() {
        return retestRate + ntf + hitRate + fiveS + workInteractive + bonusScore;
    }
}
