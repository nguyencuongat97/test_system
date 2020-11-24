package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "test_station_meta_nbb")
public class TestStationMetaNbb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "customer")
    private String customer;

    @Column(name = "stage_name")
    private String stageName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "station_id")
    private String stationId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "mac_address")
    private String macAddress;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
