package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "test_station_meta")
public class TestStationMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "tester")
    private String tester;

    @Column(name = "chamber")
    private Integer chamber;

    @Column(name = "ip")
    private String ip;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public String getTesterName() {
        return StringUtils.isEmpty(tester) && StringUtils.isEmpty(chamber) ? stationName : (StringUtils.isEmpty(tester) ? "" : tester) + "-"+ (StringUtils.isEmpty(chamber) ? "" : chamber);
    }
}
