package com.foxconn.fii.data.primary.model.entity;

import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.Data;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "test_cpk_monthly")
public class TestCpkMonthly {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "parameter")
    private String parameter;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "cpk")
    private double cpk = 0;

    @Column(name = "variance")
    private double variance = 0;

    @Column(name = "average")
    private double average = 0;

    @Column(name = "number_value")
    private int numberOfValue = 0;

    @Column(name = "cpk_new")
    private double newCpk = 0;

    @Column(name = "min_value")
    private Double minValue = 0D;

    @Column(name = "max_value")
    private Double maxValue = 0D;

    @Column(name = "lsl")
    private Double lsl = 0D;

    @Column(name = "usl")
    private Double usl = 0D;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public void calculate(double[] values, Double lsl, Double usl) {
        if (values.length == 0) {
            return;
        }

        if (numberOfValue == 0) {
            for (double value : values) {
                average = (average * numberOfValue + value) / (numberOfValue + 1);
                numberOfValue++;
            }
            Variance varianceFactory = new Variance();
            variance = varianceFactory.evaluate(values, average);
        } else {
            for (double value : values) {
                variance = (numberOfValue * 1.0 / (numberOfValue + 1)) * (variance + Math.pow((value - average), 2) / ((numberOfValue + 1)));
                average = (average * numberOfValue + value) / (numberOfValue + 1);
                numberOfValue++;
            }
        }

        newCpk = TestUtils.calculateCPK(variance, average, lsl, usl);
    }
}
