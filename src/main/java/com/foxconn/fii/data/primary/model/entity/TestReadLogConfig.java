package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "test_read_log_config")
public class TestReadLogConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "pattern")
    private String pattern;

    @Column(name = "expect_value_list")
    private String expectValueJson;

    @Column(name = "sample_file")
    private String sampleFile;

    @Column(name = "pattern_converted", columnDefinition = "TINYINT")
    private boolean patternConverted = false;

    @Transient
    private MultipartFile file;

    @Transient
    private List<String> sampleFileLineList;
}
