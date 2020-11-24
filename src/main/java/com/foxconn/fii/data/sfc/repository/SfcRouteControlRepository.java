package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SfcRouteControlRepository {

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

    public List<String> getGroupListByMoNumber(String factory, String customer, String moNumber) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moNumber", moNumber);

            return namedJdbcTemplate.queryForList("SELECT rc.group_name " +
                            "FROM SFISM4.R_MO_BASE_T mo " +
                            "JOIN SFIS1.C_ROUTE_CONTROL_T rc ON rc.route_code = mo.route_code " +
                            "WHERE mo.mo_number = :moNumber and rc.state_flag = 0 " +
                            "GROUP BY rc.group_name " +
                            "ORDER BY MIN(rc.step_sequence)",
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getGroupListByMoNumber moNumber {}", moNumber, e);
            return Collections.emptyList();
        }
    }

    public List<String> getGroupListOrderByMo(String factory, String customer, List<String> moList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        if (moList.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moList", moList);

            return namedJdbcTemplate.queryForList("SELECT rc.group_name " +
                            "FROM SFISM4.R_MO_BASE_T mo " +
                            "JOIN SFIS1.C_ROUTE_CONTROL_T rc ON rc.route_code = mo.route_code " +
                            "WHERE mo.mo_number in (:moList) and rc.state_flag = 0 and group_name != '0'" +
                            "GROUP BY rc.group_name " +
                            "ORDER BY MIN(rc.step_sequence)",
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getGroupListOrderByMo moList {}", moList, e);
            return Collections.emptyList();
        }
    }

    public List<String> getGroupListOrder(String factory, String customer, List<String> modelList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);

            return namedJdbcTemplate.queryForList("SELECT rc.group_name " +
                            "FROM SFIS1.C_MODEL_DESC_T md " +
                            "JOIN SFIS1.C_ROUTE_CONTROL_T rc ON rc.route_code = md.route_code " +
                            "WHERE md.model_name in (:modelList) and rc.state_flag = 0 and group_name != '0' " +
                            "GROUP BY rc.group_name " +
                            "ORDER BY MIN(rc.step_sequence)",
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getGroupListByMoNumber modelList {}", modelList, e);
            return Collections.emptyList();
        }
    }
}
