package com.foxconn.fii.data.b06.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "te_abnormal")
@IdClass(B06TestLock.B06TestLockId.class)
public class B06TestLock {

    @Id
    @Column(name = "model_name")
    private String modelName;

    @Id
    @Column(name = "test_station")
    private String groupName;

    @Id
    @Column(name = "station_name")
    private String stationName;

    @Id
    @Column(name = "abnormal_begin")
    private Timestamp dateTimeLock;

    @Column(name = "abnormal_end")
    private Timestamp dateTimeUnlock;

    @Column(name = "abnormal_desc")
    private String lockDetail;

    @Column(name = "unlock_id")
    private String employee;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "root_cause")
    private String why;

    @Column(name = "action")
    private String action;

    @Data
    public static class B06TestLockId implements Serializable {
        private String modelName;

        private String groupName;

        private String stationName;

        private Timestamp dateTimeLock;
    }
}

