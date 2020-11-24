//package com.foxconn.fii.data.b06ds02.model;
//
//import lombok.Data;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(schema = "TEA02", name = "TE_CPK_DATA")
//public class B06TestLog {
//
//    @Id
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "model_name")
//    private String modelName;
//
//    @Column(name = "station")
//    private String groupName;
//
//    @Column(name = "ate_station")
//    private String stationName;
//
//    @Column(name = "SN")
//    private String serial;
//
//    @Column(name = "date_time")
//    private Date dateTime;
//
//    @Column(name = "test_items")
//    private String testItem;
//
//    @Column(name = "step_number")
//    private int stepNumber;
//
//    @Column(name = "p_value")
//    private Double pValue;
//
//    @Column(name = "l_limit")
//    private Double lLimit;
//
//    @Column(name = "h_limit")
//    private Double hLimit;
//
//    @Column(name = "status")
//    private String status;
//
//    public enum Status {
//        PASS,
//        FAIL
//    }
//}
