package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "re_time_data_beta")
public class RepairTimeDataBeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "action")
    private String action;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Date createdAt;
}
