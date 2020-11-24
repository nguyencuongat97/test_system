package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.sfc.model.TestMoBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SfcMoBaseRepository {

    @Autowired
    @Qualifier("nbbsfcNamedJdbcTemplate")
    private NamedParameterJdbcTemplate nbbNamedJdbcTemplate;

    @Autowired
    @Qualifier("qcsfcNamedJdbcTemplate")
    private NamedParameterJdbcTemplate qcNamedJdbcTemplate;

    @Autowired
    @Qualifier("c03sfcNamedJdbcTemplate")
    private NamedParameterJdbcTemplate c03NamedJdbcTemplate;

    @Autowired
    @Qualifier("b06sfcNamedJdbcTemplate")
    private NamedParameterJdbcTemplate b06NamedJdbcTemplate;

    private NamedParameterJdbcTemplate getJdbcTemplate(String factory, String customer) {
        if (Factory.NBB.equalsIgnoreCase(factory)) {
            return nbbNamedJdbcTemplate;
        } else if (Factory.S03.equalsIgnoreCase(factory)) {
            if ("UI".equalsIgnoreCase(customer)) {
                return c03NamedJdbcTemplate;
            }
            return qcNamedJdbcTemplate;
        } else if (Factory.C03.equalsIgnoreCase(factory)) {
            return c03NamedJdbcTemplate;
        } else if (Factory.B06.equalsIgnoreCase(factory)) {
            return b06NamedJdbcTemplate;
        }
        return null;
    }

    public List<TestMoBase> getMoDetail(String factory, String customer, String moNumber) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moNumber", moNumber);

            return namedJdbcTemplate.queryForList("SELECT mo_number, mo_type, model_name, target_qty, mo_start_date, mo_close_date " +
                            "FROM SFISM4.R_MO_BASE_T " +
                            "WHERE mo_number = :moNumber",
                    parameters)
                    .stream().map(map -> SfcMoBaseRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupListByMoNumber moNumber {}", moNumber, e);
            return Collections.emptyList();
        }
    }

    public static TestMoBase mapping(String factory, Map<String, Object> objectMap) {
        TestMoBase ins = new TestMoBase();
        ins.setFactory(factory);
        ins.setMo((String) objectMap.get("mo_number"));
        ins.setMoType((String) objectMap.get("mo_type"));
        ins.setModelName((String) objectMap.get("target_qty"));

        if (objectMap.get("target_qty") != null) {
            ins.setTargetQty(((Number) objectMap.get("target_qty")).intValue());
        }

        ins.setStartDate((Date) objectMap.get("mo_start_date"));
        ins.setEndDate((Date) objectMap.get("mo_close_date"));
        return ins;
    }
}
