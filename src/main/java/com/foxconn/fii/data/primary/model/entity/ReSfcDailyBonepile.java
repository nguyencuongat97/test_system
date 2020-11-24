package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_sfc_daily_bonepile")
public class ReSfcDailyBonepile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "balance_qty")
    private Integer balanceQty;

    @Column(name = "over_time_8h")
    private Integer overTime8h;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Date updatedAt;

}
