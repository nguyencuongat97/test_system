package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Calendar;

@Data
@Entity
@Table(name = "test_maintain_schedule")
public class TestMaintainSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "type")
    private Type type;

    @Column(name = "next_time")
    private Date nextTime;

    public enum Type {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    public void updateNextTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextTime.getTime());
        if (type == Type.DAILY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            nextTime.setTime(calendar.getTimeInMillis());
        } else if (type == Type.WEEKLY) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            nextTime.setTime(calendar.getTimeInMillis());
        } else if (type == Type.MONTHLY) {
            calendar.add(Calendar.MONTH, 1);
            nextTime.setTime(calendar.getTimeInMillis());
        }
    }
}
