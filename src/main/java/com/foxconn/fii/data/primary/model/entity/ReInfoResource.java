package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_info_resource")
public class ReInfoResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "emp_no")
    private String empNo;

    @Column(name = "name_ese")
    private String nameEse;

    @Column(name = "name_china")
    private String nameChina;

    @Column(name = "factory")
    private String factory;

    @Column(name = "department")
    private String department;

    @Column(name = "role")
    private String role;

    @Column(name = "domicile")
    private String domicile;

    @Column(name = "bu")
    private String bu;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
