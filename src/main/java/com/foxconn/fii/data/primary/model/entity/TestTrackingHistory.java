package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "test_tracking_history")
public class TestTrackingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tracking_id")
    private TestTracking tracking;

    @Column(name = "employee")
    private String employee;

    @Column(name = "action")
    private TestTracking.Status action;

    @Column(name = "action_at")
    private Date actionAt;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private TestSolutionMeta solution;

    @PrePersist
    public void prePersist() {
        if (actionAt == null) {
            actionAt = new Date();
        }
    }
}
