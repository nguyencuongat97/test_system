package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "test_station_equipment")
public class TestStationEquipment {

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

    @Column(name = "equipment")
    private String equipment;

    @Column(name = "status")
    private int status;

    @Column(name = "number_used")
    private int numberUsed;

    @Column(name = "elapsed_time")
    private double elapsedTime;

    @Column(name = "spec")
    private int spec;

    //Modified by Giang for NBB test equipment
    @Column(name = "customer")
    private String customer;

    @Column(name = "stage_name")
    private String stageName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "station_order")
    private Integer stationOrder;

    @Column(name = "equipment_order")
    private Integer equipmentOrder;

    @Column(name = "station_ip_address")
    private String stationIpAddress;

    @Column(name = "station_mac_address")
    private String stationMacAddress;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
