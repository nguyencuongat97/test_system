package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_solution_history")
public class TestSolutionHistory {

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

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "error_code")
    private String errorCode;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private TestSolutionMetaNew solution;

    @Column(name = "run_correlation_test", columnDefinition = "TINYINT(2)")
    private Boolean runCorrelationTest = false;

    @Transient
    private String timeSpan;

    @Transient
    private String errorDescription;

    @Transient
    private String component;
}
