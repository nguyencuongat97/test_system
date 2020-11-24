package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TE_EQUIPMENT_FIXTURE_LOCATION")
public class TeEquipmentFixtureLocation {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeEquipmentFixtureLocation_SEQ")
    @SequenceGenerator(sequenceName = "TE_EF_LOCATION_SEQ", allocationSize = 1, name = "TeEquipmentFixtureLocation_SEQ")
    private long id;

    @Column(name = "LOCATION_NAME")
    private String locationName;

}
