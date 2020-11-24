package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "pc_ecn")
public class PcEcn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "ecn_number")
    private String ecnNumber;

    @Column(name = "old_material")
    private String oldMaterial;

    @Column(name = "new_material")
    private String newMaterial;

    @Column(name = "model_list")
    private String modelList;

    @Column(name = "description")
    private String description;

    @Column(name = "ecn_date")
    private String ecnDate;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
