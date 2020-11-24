package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TE_TEST_DATA")
public class TeTestData {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeTestData_SEQ")
    @SequenceGenerator(sequenceName = "TE_TEST_DATA_SEQ", allocationSize = 1, name = "TeTestData_SEQ")
    private long id;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "STATION")
    private String station;

    @Column(name = "ATE")
    private String ate;

    @Column(name = "FIXTURE_CODE")
    private String fixtureCode;

    @Column(name = "MO")
    private String mo;

    @Column(name = "SN")
    private String sn;

    @Column(name = "START_TIME")
    private String startTime;

    @Column(name = "END_TIME")
    private String endTime;

    @Column(name = "ELAPSE_TIME")
    private String elapseTime;

    @Column(name = "TEST_MODE")
    private String testMode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ERROR_CODE")
    private String errorCode;

    @Column(name = "TEST_ITEM")
    private String testItem;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "LSL")
    private String lsl;

    @Column(name = "USL")
    private String usl;

    @Column(name = "TEST_VERSION")
    private String testVersion;

    @Column(name = "CLIFFORD_VERSION")
    private String cliffordVersion;

    @Column(name = "DEVICE_CONFIG")
    private String deviceConfig;

    @Column(name = "BUILD_PHASE")
    private String buildPhase;

    @Column(name = "SN_LOADING_TME")
    private String snLoadingTime;

}
