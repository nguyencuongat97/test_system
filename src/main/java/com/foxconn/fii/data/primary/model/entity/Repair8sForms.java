package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_cl_forms")
public class Repair8sForms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "id_user")
//    private Long idUser;

    @Column(name = "id_user_leader")
    private Long idUserLeader;

    @Column(name = "id_user_owner")
    private Long idUserOwner;

//    @Column(name = "owner")
//    private String owner;

//    @Column(name = "location")
//    private String location;

    @Column(name = "id_location")
    private Long idLocation;

    @Column(name = "id_shift")
    private Long idShift;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
