package com.foxconn.fii.data.b04.model;

import lombok.Data;

@Data
public class Parameter {

    private String parameters;

    private Double lowSpec;

    private Double highSpec;

    public static Parameter of(String parameters, Double lowSpec, Double highSpec) {
        Parameter parameter = new Parameter();
        parameter.setParameters(parameters);
        parameter.setLowSpec(lowSpec);
        parameter.setHighSpec(highSpec);

        return parameter;
    }
}
