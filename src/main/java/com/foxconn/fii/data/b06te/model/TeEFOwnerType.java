package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TE_EF_OWNER_TYPE")
public class TeEFOwnerType {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeEFOwnerType_SEQ")
    @SequenceGenerator(sequenceName = "TE_EF_OWNER_TYPE_SEQ", allocationSize = 1, name = "TeEFOwnerType_SEQ")
    private long id;

    @Column(name = "OWNER_TYPE")
    private String ownerType;

}
