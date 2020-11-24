package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_office_unit_info")
public class HrEmployeeTrackingOfficeUnitInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "office_unit_code")
    private String officeUnitCode;

    @Column(name = "office_unit_name")
    private String officeUnitName;

    @Column(name = "factory_name")
    private String factoryName = "UNKNOWN";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
