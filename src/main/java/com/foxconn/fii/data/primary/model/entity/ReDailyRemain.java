package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_daily_remain")
public class ReDailyRemain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "status")
    private Status status;

    @Column(name = "daily_remain")
    private long dailyRemain;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public enum Status {
        ONLINE_WIP,
        RMA,
        BC8M,
        OTHER
    }
}
