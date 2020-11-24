package com.foxconn.fii.data.primary.model;

import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpcData {

    private String parameter;

    private int sampleSize;

    private double average;

    private Double min;

    private Double max;

    private Double lsl;

    private Double usl;


    private double xBar;

    private double sigmaWithin;

    private Double cp;

    private Double cpl;

    private Double cpu;

    private double cpk;


    private double sigmaOverall;

    private Double pp;

    private Double ppl;

    private Double ppu;

    private double ppk;

    private Double cpm;


    private List<Double> values;

    public List<Point> getPointCpkList() {
        List<Point> points = new ArrayList<>();
        for (double z = -3.0; z <= 3.0; z += 0.1) {
            double x = xBar + z * sigmaWithin;
            double y = Math.exp(-1.0f * Math.pow(x - xBar, 2) / (2 * Math.pow(sigmaWithin, 2))) / (Math.sqrt(2 * Math.PI) * sigmaWithin);
            points.add(Point.of(x, y));
        }
        return points;
    }

    @Value(staticConstructor = "of")
    public static class Point {
        private double x;
        private double y;
    }
}
