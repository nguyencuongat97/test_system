//package com.foxconn.fii.data.b06ds02.model;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(schema = "TEA02", name = "TEST_LOGS_DATA")
//@IdClass(B06TestLogData.B06TestLogDataId.class)
//public class B06TestLogData {
//
//    @Id
//    @Column(name = "model_name")
//    private String modelName;
//
//    @Id
//    @Column(name = "mo")
//    private String mo;
//
//    @Id
//    @Column(name = "group_name")
//    private String groupName;
//
//    @Id
//    @Column(name = "SN")
//    private String serial;
//
//    @Id
//    @Column(name = "TIME_START")
//    private Date startDate;
//
//    @Column(name = "TIME_END")
//    private Date endDate;
//
//    @Column(name = "TEST_TIME")
//    private int testTime;
//
//    @Column(name = "error_code")
//    private String errorCode;
//
//    @Column(name = "equipment")
//    private String equipment;
//
//    @Data
//    public static class B06TestLogDataId implements Serializable {
//        private String modelName;
//
//        private String mo;
//
//        private String groupName;
//
//        private String serial;
//
//        private Date startDate;
//    }
//}
