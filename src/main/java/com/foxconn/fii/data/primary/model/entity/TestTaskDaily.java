package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_task_daily")
public class TestTaskDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "creator")
    private String creator;

    @Column(name = "resource_group")
    private String resourceGroup;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_content")
    private String taskContent;

    @Column(name = "status")
    private Status status;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Column(name = "duedate")
    private Date duedate;

    @Column(name = "note")
    private String note;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "tracking_id")
    private Long trackingId;

    @Column(name = "comment_number")
    private int commentNumber;

    @Column(name = "task_url")
    private String taskUrl;

    public enum Status {
        ON_GOING,
        PENDING,
        DONE
    }
}
