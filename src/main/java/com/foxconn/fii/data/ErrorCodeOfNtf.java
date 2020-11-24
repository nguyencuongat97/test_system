package com.foxconn.fii.data;

import lombok.Data;

@Data
public class ErrorCodeOfNtf {

    private String testCode;

    private Integer qtyErrorNtf;

    private Integer qtyError;

    private Float ratioNtf ;
}
