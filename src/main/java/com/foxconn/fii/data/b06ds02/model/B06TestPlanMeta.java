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
//import java.text.SimpleDateFormat;
//
//@Data
//@Entity
//@Table(schema = "SFCA02", name = "SI_TARGET_PRODUCTION")
//@IdClass(B06TestPlanMeta.B06TestPlanMetaId.class)
//public class B06TestPlanMeta {
//
//    public static final SimpleDateFormat WORK_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
//
//    @Id
//    @Column(name = "model_name")
//    private String modelName;
//
//    @Id
//    @Column(name = "work_date")
//    private String workDate;
//
//    @Id
//    @Column(name = "line")
//    private String line;
//
//    @Column(name = "target")
//    private Integer target;
//
//    @Column(name = "p_time_day")
//    private Float pTimeDay;
//
//    @Column(name = "p_time_night")
//    private Float pTimeNight;
//
//    @Column(name = "uph")
//    private Integer uph;
//
//    @Column(name = "op_target")
//    private Integer opTarget;
//
//    @Column(name = "op_use_day")
//    private Integer opUseDay;
//
//    @Column(name = "op_use_night")
//    private Integer opUseNight;
//
//    @Data
//    public static class B06TestPlanMetaId implements Serializable {
//        private String modelName;
//
//        private String workDate;
//
//        private String line;
//    }
//
//}
