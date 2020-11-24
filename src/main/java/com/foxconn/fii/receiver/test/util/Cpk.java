package com.foxconn.fii.receiver.test.util;

import lombok.Data;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.Date;

@Data
public class Cpk {

    private StandardDeviation standardDeviation;

    private Double lsl;

    private Double usl;

    private double sum;

    private long n;

    private Date endDate;

    public void increment(double d) {
        standardDeviation.increment(d);
    }

    public double getResult() {
        if (n <= 0) {
            return 10000;
        }
        double average = sum / n;
        double sigma = standardDeviation.getResult();
        double cpk = 10000;
        if (sigma != 0) {
            double cpkLower = Double.MAX_VALUE;
            if (lsl != null) {
                cpkLower = (average - lsl) / (sigma * 3);
            }

            double cpkHigher = Double.MAX_VALUE;
            if (usl != null) {
                cpkHigher = (usl - average) / (sigma * 3);
            }
            cpk = cpkLower < cpkHigher ? cpkLower : cpkHigher;
        }

        return cpk;
    }
}
