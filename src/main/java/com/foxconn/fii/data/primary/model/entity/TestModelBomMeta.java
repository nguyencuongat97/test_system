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
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@Table(name = "test_model_bom_meta")
public class TestModelBomMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "output_group_name")
    private String outputGroupName;

    @Column(name = "kp_no")
    private String kpNo;

    @Column(name = "qty")
    private Integer standardQty = 0;

    @Transient
    private int checkoutQty;

    @Transient
    private int deliverQty;

    @Transient
    private int kittingQty;

    @Transient
    private int whsQty;

    public int getRemainQty() {
        return deliverQty - checkoutQty;
    }
}
