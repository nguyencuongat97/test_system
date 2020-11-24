//package com.foxconn.fii.data.b06ds02.model;
//
//import lombok.Data;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(schema = "SFCA02", name = "TE_RETEST_RATE_TRACKING")
//public class B06TestSolutionMeta {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "action_id")
//    private int id;
//
//    @Column(name = "model_name")
//    private String modelName;
//
//    @Column(name = "error_code")
//    private String errorCode;
//
//    @Column(name = "root_cause")
//    private String solution;
//
//    @Column(name = "owner")
//    private String author;
//
//    @Column(name = "vote")
//    private int numberSuccess;
//
//    @Column(name = "action")
//    private String action;
//
//    @Column(name = "date_time")
//    private Date createdAt;
//}
