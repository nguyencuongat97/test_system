package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "test_station_equipment_daily_info")
public class TestStationEquipmentDailyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "equipment_id")
    private int equipmentId;

    @Column(name = "work_date")
    private Date workDate;

    @Column(name = "number_used")
    private int numberUsed = 0;

    @Column(name = "elapsed_time")
    private double elapsedTime = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private java.util.Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

}
