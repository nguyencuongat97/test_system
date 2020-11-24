package com.foxconn.fii.data.primary.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "test_note_error")
public class TestNoteError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "error")
    private String error;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "root_cause")
    private String rootCause;

    @Column(name = "attach_file")
    private String attachFile;

    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Status status;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "owner")
    private String owner;

    @Transient
    private MultipartFile uploadedFile;

    @Transient
    private String timeSpan;

    public enum Status {
        ON_GOING,
        DONE,
        PENDING,
        CLOSE
    }
}
