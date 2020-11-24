package com.foxconn.fii.data.primary.model;

import lombok.Data;

@Data
public class IOResponse {

    private int input = 0;

    private int output = 0;

    private int rate = 0;
}
