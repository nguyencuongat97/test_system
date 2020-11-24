package com.foxconn.fii.data.sfc.model;

import lombok.Data;

import java.util.Date;

@Data
public class TestMoBase {

    private String factory;

    private String mo;

    private String moType;

    private String modelName;

    private int targetQty;

    private Date startDate;

    private Date endDate;
}
