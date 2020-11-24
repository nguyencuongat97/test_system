package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "ehs_temperature_data")
public class TemperatureData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "device_id")
    private int deviceId;

    @Column(name = "device_code")
    private String deviceCode;

    @Column(name = "device_location")
    private String deviceLocation;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "humidity")
    private Float humidity;

    @Column(name = "created_at")
    private Date createdAt;
}
