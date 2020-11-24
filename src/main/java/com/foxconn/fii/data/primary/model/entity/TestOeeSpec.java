package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Data
@Entity
@Table(name = "test_oee_spec")
public class TestOeeSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item")
    private String item;

    @Column(name = "[value]")
    private String value;

    public Integer integerValue() {
        try {
            return Integer.valueOf(this.value);
        } catch (Exception e) {
            log.error("TestOeeSpec parse to Integer error : ", e);
            return null;
        }
    }

    public Double doubleValue() {
        try {
            return Double.valueOf(this.value);
        } catch (Exception e) {
            log.error("TestOeeSpec parse to Double error : ", e);
            return null;
        }
    }

}
