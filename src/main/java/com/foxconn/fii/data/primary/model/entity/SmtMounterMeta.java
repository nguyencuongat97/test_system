package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "smt_mounter_meta")
public class SmtMounterMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "machine_type")
    private String machineType;

    @Column(name = "layout_slot")
    private String layoutSlot;

    @Column(name = "cycle_time")
    private Float cycleTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
