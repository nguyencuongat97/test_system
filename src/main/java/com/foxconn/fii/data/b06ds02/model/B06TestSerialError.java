//package com.foxconn.fii.data.b06ds02.model;
//
//import lombok.Data;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.Table;
//import java.io.Serializable;
//import java.util.Date;
//
//@Data
////@Entity
////@Table(schema = "SFCA02", name = "B06_RE_GET_LIST_SN_ERROR")
////@IdClass(B06TestSerialError.B06TestSerialErrorId.class)
//public class B06TestSerialError {
//
//    @Id
//    @Column(name = "serial_number")
//    private String serialNumber;
//
//    @Column(name = "model_name")
//    private String modelName;
//
//    @Column(name = "test_station")
//    private String groupName;
//
//    @Column(name = "ate_station_no")
//    private String stationName;
//
//    @Column(name = "test_time")
//    private String testTime;
//
//    @Column(name = "test_code")
//    private String errorCode;
//
//    @Id
//    @Column(name = "repair_time")
//    private String repairTime;
//
//    @Column(name = "reason_code")
//    private String reason;
//
//    @Column(name = "working_date")
//    private Date workingDate;
//
//    @Column(name = "shift")
//    private String shift;
//
//    @Data
//    public static class B06TestSerialErrorId implements Serializable {
//        private String serialNumber;
//
//        private String repairTime;
//    }
//}
