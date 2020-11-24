package com.foxconn.fii.data.b06te.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "TE_F_CALIBRATION_HISTORY")
public class TeFixtureCalibrationHistory {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeFixtureCalibrationHistory_SEQ")
    @SequenceGenerator(sequenceName = "TE_F_CALIBRATION_HISTORY_SEQ", allocationSize = 1, name = "TeFixtureCalibrationHistory_SEQ")
    private long id;

    @Column(name = "FIXTURE_ID")
    private Long fixtureId;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "START_EMPLOYEE_ID")
    private String startEmployeeId;

    @Column(name = "START_NOTE")
    private String startNote;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "END_EMPLOYEE_ID")
    private String endEmployeeId;

    @Column(name = "END_NOTE")
    private String endNote;

    public TeFixtureCalibrationHistory(){}

    public TeFixtureCalibrationHistory(Map<String, Object> data) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (data.containsKey("fixtureId")) {
            this.setFixtureId(Long.valueOf((int)data.get("fixtureId")));
        }

        if (data.containsKey("startTime") && !StringUtils.isEmpty(data.get("startTime"))) {
            this.setStartTime(sdf.parse((String) data.get("startTime")));
        } else {
            this.setStartTime(null);
        }

        if (data.containsKey("startEmployeeId")) {
            this.setStartEmployeeId((String) data.get("startEmployeeId"));
        }

        if (data.containsKey("startNote")) {
            this.setStartNote((String) data.get("startNote"));
        }

        if (data.containsKey("endTime") && !StringUtils.isEmpty(data.get("endTime"))) {
            this.setEndTime(sdf.parse((String) data.get("endTime")));
        } else {
            this.setEndTime(null);
        }

        if (data.containsKey("endEmployeeId")) {
            this.setEndEmployeeId((String) data.get("endEmployeeId"));
        }

        if (data.containsKey("endNote")) {
            this.setEndNote((String) data.get("endNote"));
        }
    }

    public void readData(Map<String, Object> data) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (data.containsKey("fixtureId")) {
            this.setFixtureId(Long.valueOf((int)data.get("fixtureId")));
        }

        if (data.containsKey("startTime") && !StringUtils.isEmpty(data.get("startTime"))) {
            this.setStartTime(sdf.parse((String) data.get("startTime")));
        } else {
            this.setStartTime(null);
        }

        if (data.containsKey("startEmployeeId")) {
            this.setStartEmployeeId((String) data.get("startEmployeeId"));
        }

        if (data.containsKey("startNote")) {
            this.setStartNote((String) data.get("startNote"));
        }

        if (data.containsKey("endTime") && !StringUtils.isEmpty(data.get("endTime"))) {
            this.setEndTime(sdf.parse((String) data.get("endTime")));
        } else {
            this.setEndTime(null);
        }

        if (data.containsKey("endEmployeeId")) {
            this.setEndEmployeeId((String) data.get("endEmployeeId"));
        }

        if (data.containsKey("endNote")) {
            this.setEndNote((String) data.get("endNote"));
        }
    }

}
