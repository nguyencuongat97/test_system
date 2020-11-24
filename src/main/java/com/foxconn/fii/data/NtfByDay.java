package com.foxconn.fii.data;

import lombok.Data;

import java.util.Date;

@Data
public class NtfByDay {

    private String day;

    private Integer qtyErrorNtf;

    private Integer qtyError;

    private Float ratioNtf ;

}
