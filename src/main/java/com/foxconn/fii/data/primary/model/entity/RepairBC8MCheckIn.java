package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_bc8m_check_in")
public class RepairBC8MCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "FACTORY")
    private String factory;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "MO_NUMBER")
    private String moNumber;

    @Column(name = "MODEL_NAME")
    private String modelName;

    @Column(name = "VERSION_CODE")
    private String versionCode;

    @Column(name = "LINE_NAME")
    private String lineName;

    @Column(name = "SECTION_NAME")
    private String sectionName;

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "STATION_NAME")
    private String stationName;

    @Column(name = "STATION_SEQ")
    private Integer stationSEQ;

    @Column(name = "ERROR_FLAG")
    private Integer errorFlag;

    @Column(name = "IN_STATION_TIME")
    private Date inStationTime;

    @Column(name = "IN_LINE_TIME")
    private Date inLineTime;

    @Column(name = "SCRAP_FLAG")
    private Integer scrapFlag;

    @Column(name = "CUSTOMER_NO")
    private String customerNo;

    @Column(name = "BOM_NO")
    private String bomNo;

    @Column(name = "KEY_PART_NO")
    private String keyPartNo;

    @Column(name = "EMP_NO")
    private String empNo;

    @Column(name = "WIP_GROUP")
    private String wipGroup;

    @Column(name = "TEST_CODE")
    private String testCode;

    @Column(name = "DESCRIP")
    private String description;

    @Column(name = "TEST_GROUP")
    private String testGroup;

    @Column(name = "TEST_TIME")
    private Date testTime;

    @Column(name = "LOCATION")
    private String location;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
