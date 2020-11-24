package com.foxconn.fii.data.ap.repository;

import com.foxconn.fii.data.ap.model.CustKpOnline;
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
public class NbbCustKpOnlineRepository {

    @Autowired
    @Qualifier("nbbapNamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public List<Map<String, Object>> findByWorkTimeBetween(Date startDate, Date endDate) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT v_wo, " +
                            "listagg(wr.wo, ',') within group (order by wr.wo) as group_wo, " +
                            "cust_kp_no, standard_qty, sum(wo_request) as wo_request, sum(deliver_qty) as deliver_qty, sum(checkout_qty) as checkout_qty, sum(return_qty) as return_qty " +
                            "FROM MES4.R_WO_REQUEST wr left join MES4.R_V_WO w on wr.wo = t_wo " +
                            "WHERE wr.wo in (" +
                            "(select t_wo from MES4.R_V_WO where v_wo in (SELECT V_WO FROM MES4.R_V_WO WHERE t_wo in (select distinct(wo) from MES4.R_TR_SN_DETAIL WHERE work_time between :current and :next)))" +
                            "union (select distinct(wo) from MES4.R_TR_SN_DETAIL WHERE work_time between :current and :next)" +
                            ") and standard_qty > 0" +
                            "group by v_wo, cust_kp_no, standard_qty",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoNumber(List<String> moList) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            if (moList != null) {
                moList = moList.stream().map(mo -> "00" + mo).collect(Collectors.toList());
                parameters.put("moList", moList);
            }

            return namedJdbcTemplate.queryForList("select cust_kp_no, sum(qty) as deliver_qty from MES4.R_KITTING_SCAN_DETAIL " +
                            "where move_type = 'b' " + (moList != null ? "and to_location in (:moList) " : "")+
                            "group by cust_kp_no" ,
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getKittingStored() {
        try {
            Map<String, Object> parameters = new HashMap<>();
            return namedJdbcTemplate.queryForList("select cust_kp_no, sum(ext_qty) as deliver_qty from MES4.R_TR_SN " +
                            "where location_flag = 1 and work_flag = 0 and  kitting_flag = 'a' " +
                            "group by cust_kp_no" ,
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getWhsStored() {
        try {
            Map<String, Object> parameters = new HashMap<>();
            return namedJdbcTemplate.queryForList("select cust_kp_no, sum(ext_qty) as deliver_qty from MES4.R_TR_SN " +
                            "where location_flag = 0 and work_flag = 0 " +
                            "group by cust_kp_no" ,
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber", e);
            return Collections.emptyList();
        }
    }

    public static CustKpOnline mapping(Map<String, Object> objectMap) {
        CustKpOnline ins = new CustKpOnline();
        ins.setGroupWo((String)objectMap.get("group_wo"));
        ins.setCustKpNo((String)objectMap.get("cust_kp_no"));

        if (objectMap.containsKey("standard_qty")) {
            ins.setStandardQty(((Number) objectMap.get("standard_qty")).intValue());
        }

        if (objectMap.containsKey("wo_request")) {
            ins.setWoRequest(((Number) objectMap.get("wo_request")).intValue());
        }

        if (objectMap.containsKey("deliver_qty")) {
            ins.setDeliverQty(((Number) objectMap.get("deliver_qty")).intValue());
        }

        if (objectMap.containsKey("checkout_qty")) {
            ins.setCheckoutQty(((Number) objectMap.get("checkout_qty")).intValue());
        }

        if (objectMap.containsKey("return_qty")) {
            ins.setReturnQty(((Number) objectMap.get("return_qty")).intValue());
        }

        ins.setCheckoutPH(200);

        return ins;
    }
}
