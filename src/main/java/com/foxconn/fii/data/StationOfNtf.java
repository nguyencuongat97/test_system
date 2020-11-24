package com.foxconn.fii.data;

import lombok.Data;

@Data
public class StationOfNtf {

    private String testStation;

    private Integer qtyErrorNtf;

    private Integer qtyError;

    private Float ratioNtf ;
}
