package com.foxconn.fii.data.primary.model;

import lombok.Data;

import java.util.List;

@Data
public class IgnoreModelRequest {

    private String factory;

    private List<String> models;
}
