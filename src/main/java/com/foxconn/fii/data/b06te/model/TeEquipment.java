package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TE_EQUIPMENT")
public class TeEquipment {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeEquipment_SEQ")
    @SequenceGenerator(sequenceName = "TE_EQUIPMENT_SEQ", allocationSize = 1, name = "TeEquipment_SEQ")
    private long id;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "STATION")
    private String station;

    @Column(name = "LINE")
    private String line;

    @Column(name = "EQUIPMENT_NAME")
    private String equipmentName;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CALIBRATION_TIME")
    private Date calibrationTime;

    @Column(name = "CALIBRATION_EXPIRED_DAY")
    private Long calibrationExpiredDay;

    @Column(name = "LICENSE_RECEIVE_TIME")
    private Date licenseReceiveTime;

    @Column(name = "LICENSE_EXPIRED_TIME")
    private Date licenseExpiredTime;

    @Column(name = "EQUIPMENT_VERSION")
    private String equipmentVersion;

    @Column(name = "LATEST_ONLINE_TIME")
    private Date latestOnlineTime;

    @Column(name = "LOCATION_ID")
    private Long locationId;

    @Column(name = "OWNER_TYPE_ID")
    private Long ownerTypeId;

}
