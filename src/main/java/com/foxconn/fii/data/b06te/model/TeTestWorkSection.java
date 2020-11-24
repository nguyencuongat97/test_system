package com.foxconn.fii.data.b06te.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TE_TEST_WORK_SECTION")
public class TeTestWorkSection {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeTestWorkSection_SEQ")
    @SequenceGenerator(sequenceName = "TE_TEST_TIME_SECTION_SEQ", allocationSize = 1, name = "TeTestWorkSection_SEQ")
    private long id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

}
