package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

@Data
public class TestEquipment {

    private int id;

    private String factory;

    private String modelName;

    private String equipmentName;

    private int spec;
}
