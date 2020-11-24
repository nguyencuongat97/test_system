package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TE_FIXTURE")
public class TeFixture {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeFixture_SEQ")
    @SequenceGenerator(sequenceName = "TE_FIXTURE_SEQ", allocationSize = 1, name = "TeFixture_SEQ")
    private long id;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "STATION")
    private String station;

    @Column(name = "LINE")
    private String line;

    @Column(name = "FIXTURE_CODE")
    private String fixtureCode;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "RNR_DATA")
    private String rnrData;

    @Column(name = "LATEST_ONLINE_TIME")
    private Date latestOnlineTime;

    @Column(name = "LOCATION_ID")
    private Long locationId;

    @Column(name = "OWNER_TYPE_ID")
    private Long ownerTypeId;

    @Column(name = "CALIBRATION_TIME")
    private Date calibrationTime;

    @Column(name = "CALIBRATION_EXPIRED_DAY")
    private Long calibrationExpiredDay;

    @Column(name = "LICENSE_RECEIVE_TIME")
    private Date licenseReceiveTime;

    @Column(name = "LICENSE_EXPIRED_TIME")
    private Date licenseExpiredTime;

}
