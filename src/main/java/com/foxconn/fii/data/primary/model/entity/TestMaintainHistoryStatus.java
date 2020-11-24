package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "test_maintain_history_status")
public class TestMaintainHistoryStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "maintain_history_id")
    private TestMaintainHistory maintainHistory;

    @Column(name = "station_equipment_id")
    private int stationEquipmentId;

    @Column(name = "status")
    private int status;

    @Column(name = "action")
    private String action;
}
