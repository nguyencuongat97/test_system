package com.foxconn.fii.data.primary.model.entity;


import com.foxconn.fii.common.ShiftType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Data
@Entity
@Table(name = "test_part_error_statistics")
public class TestPartErrorStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "root_cause")
    @Enumerated(EnumType.STRING)
    private PartRootCause rootCause;

    @Column(name = "work_date")
    private Date workDate;

    @Column(name = "shift_type")
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Column(name = "quantity")
    private int quantity;

    @Transient
    private Map<String, Integer> workDateMap = new TreeMap<>();

    public enum PartRootCause {
        LOI_LIEU_DEN,
        LOI_DO_CHUYEN,
        LOI_DO_MAY
    }
}
