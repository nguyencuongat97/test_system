package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.sfc.model.TestWipTracking;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SfcWipTrackingRepository {

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
    ///////////////////////////
    public List<Map<String, Object>> getQtyWip(String factory, String model) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);

            return namedJdbcTemplate.queryForList("SELECT Count(*) as qty\n" +
                            "\tFROM \"SFISM4\".\"R_WIP_TRACKING_T\" \n" +
                            "\tWHERE MODEL_NAME = :model\n" +
                            "\tand WIP_GROUP != 'STOCKIN'\n" +
                            "\tand WIP_GROUP != 'FG'",
                    parameters);
        } catch (Exception e) {
            log.error("### getQtyWip", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyWipByTime(String factory, String model) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);

            return namedJdbcTemplate.queryForList("SELECT SUM(CASE WHEN ((SYSDATE-IN_STATION_TIME)*24 >= 48 and (SYSDATE-IN_STATION_TIME)*24 <72) THEN 1 ELSE 0 END) as time1,\n" +
                            "  SUM(CASE WHEN ((SYSDATE-IN_STATION_TIME)*24 >=72 and (SYSDATE-IN_STATION_TIME)*24 <168) THEN 1 ELSE 0 END) as time2,\n" +
                            "  SUM(CASE WHEN ((SYSDATE-IN_STATION_TIME)*24 >=168) THEN 1 ELSE 0 END) as time3\n" +
                            "    FROM \"SFISM4\".\"R_WIP_TRACKING_T\"\n" +
                            "  WHERE MODEL_NAME = :model\n" +
                            "  and WIP_GROUP != 'STOCKIN'\n" +
                            "    and WIP_GROUP != 'FG'",
                    parameters);
        } catch (Exception e) {
            log.error("### getQtyWipByTime", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyWipGroupByMoStarTime(String factory, String model) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);

            return namedJdbcTemplate.queryForList("SELECT TO_DATE(IN_STATION_TIME) as day ,MO_NUMBER FROM \"SFISM4\".\"R_WIP_TRACKING_T\" \n" +
                            "\t\tWHERE MODEL_NAME = :model\n" +
                            "\t\tand WIP_GROUP != 'STOCKIN'\n" +
                            "\t\tand WIP_GROUP != 'FG'\n" +
                            "\t\tGROUP BY TO_DATE(IN_STATION_TIME),MO_NUMBER\n" +
                            "\t\tORDER BY TO_DATE(IN_STATION_TIME)",
                    parameters);
        } catch (Exception e) {
            log.error("### getQtyWipGroupByMoStarTime", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getQtyWipGroupStation(String factory, String model ,String mo ,Date startDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);
            parameters.put("moNumber", mo);
            parameters.put("current", startDate);

            return namedJdbcTemplate.queryForList("SELECT STATION_NAME,count(STATION_NAME) as qty FROM \"SFISM4\".\"R_WIP_TRACKING_T\" \n" +
                            "\t\tWHERE WIP_GROUP != 'STOCKIN'\n" +
                            "\t\tand WIP_GROUP != 'FG'\n" +
                            "\t\tand STATION_NAME is not NULL\n" +
                            "\t\tand MODEL_NAME = :model\n" +
                            "\t\tAND MO_NUMBER = :moNumber\n" +
                            "\t\tand TO_DATE(IN_STATION_TIME) = :current\n" +
                            "\t\tGROUP BY STATION_NAME",
                    parameters);
        } catch (Exception e) {
            log.error("### getQtyWipGroupStation", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getGroupLine(String factory, String model ) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);

            return namedJdbcTemplate.queryForList("SELECT LINE_NAME FROM \"SFISM4\".\"R_WIP_TRACKING_T\" \n" +
                            "\t\tWHERE MODEL_NAME = :model \n" +
                            "\t\tand WIP_GROUP != 'STOCKIN'\n" +
                            "\t\tand WIP_GROUP != 'FG'\n" +
                            "\t\tGROUP BY LINE_NAME",
                    parameters);
        } catch (Exception e) {
            log.error("### getQtyWipGroupLine", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getStationByModel(String factory, String model ) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, model);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("model", model);
            return namedJdbcTemplate.queryForList("SELECT STATION_NAME,count(STATION_NAME) as qty FROM \"SFISM4\".\"R_WIP_TRACKING_T\" \n" +
                            "\t\tWHERE WIP_GROUP != 'STOCKIN'\n" +
                            "\t\tand WIP_GROUP != 'FG'\n" +
                            "\t\tand MODEL_NAME = :model\n" +
                            "\t\tand STATION_NAME is not NULL\n" +
                            "\t\tGROUP BY STATION_NAME \n"+
                            "\t\torder by qty DESC" ,
                    parameters);
        } catch (Exception e) {
            log.error("### getStationGroupLine", e);
            return Collections.emptyList();
        }
    }
    ///////////////////
    public List<Map<String, Object>> findByModel(String factory, String customer, List<String> modelList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);

            return namedJdbcTemplate.queryForList("SELECT mo_number, line_name, model_name, wip_group, count(*) wip_qty " +
                            "FROM SFISM4.R_WIP_TRACKING_T WHERE model_name in (:modelList) " +
                            "GROUP BY mo_number, line_name, model_name, wip_group",
                    parameters);
        } catch (Exception e) {
            log.error("### findByModel", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoNumber(String factory, String customer, List<String> moList, String serial) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moList", moList);
            parameters.put("serial", serial);

            return namedJdbcTemplate.queryForList("SELECT wp.mo_number, wp.line_name, wp.model_name, wp.wip_group, mo.target_qty, count(*) wip_qty " +
                            "FROM SFISM4.R_WIP_TRACKING_T wp JOIN SFISM4.R_MO_BASE_T mo ON wp.mo_number = mo.mo_number WHERE " +
                            (moList.isEmpty() ? "" : "wp.mo_number in (:moList) ") +
                            (moList.isEmpty() || StringUtils.isEmpty(serial) ? "" : "AND ") +
                            (StringUtils.isEmpty(serial) ? "" : "wp.serial_number = :serial ") +
                            "GROUP BY wp.mo_number, wp.line_name, wp.model_name, wp.wip_group, mo.target_qty",
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoList(String factory, String customer, List<String> moList, List<String> groupList, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moList", moList);
            parameters.put("groupList", groupList);
            parameters.put("endDate", endDate);

            return namedJdbcTemplate.queryForList("SELECT wp.mo_number, wp.line_name, wp.model_name, wp.group_name, wp.wip_group, mo.target_qty, count(*) wip_qty " +
                            "FROM SFISM4.R_WIP_TRACKING_T wp JOIN SFISM4.R_MO_BASE_T mo ON wp.mo_number = mo.mo_number WHERE " +
                            (moList.isEmpty() ? "" : "wp.mo_number in (:moList) ") +
                            (moList.isEmpty() || groupList.isEmpty() ? "" : "AND ") +
                            (groupList.isEmpty() ? "" : "wp.wip_group in (:groupList) ") +
                            (!moList.isEmpty() || !groupList.isEmpty() ? "AND " : "") +
                            "wp.in_station_time < :endDate " +
                            "GROUP BY wp.mo_number, wp.line_name, wp.model_name, wp.group_name, wp.wip_group, mo.target_qty",
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoNumberAndWipGroup(String factory, String customer, List<String> moList, List<String> wipGroupList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("moList", moList);
            parameters.put("wipGroupList", wipGroupList);

            return namedJdbcTemplate.queryForList("SELECT * " +
                            "FROM SFISM4.R_WIP_TRACKING_T WHERE mo_number in (:moList) AND wip_group IN (:wipGroupList)",
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumberAndWipGroup", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findBySerialNumber(String factory, String customer, String serialNumber) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("serialNumber", serialNumber);

            return namedJdbcTemplate.queryForList("SELECT * " +
                            "FROM SFISM4.R_WIP_TRACKING_T WHERE serial_number = :serialNumber",
                    parameters);
        } catch (Exception e) {
            log.error("### findBySerialNumber", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findWipLinkByModel(String factory, String customer, List<String> modelList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);

            return namedJdbcTemplate.queryForList("select wt.mo_number, mb.target_qty, wt.model_name, count(*) as total_qty, " +
                            "sum(case when wip_group = 'STOCKIN' and wk.key_part_sn is not null then 1 else 0 end) as link_qty, " +
                            "sum(case when wip_group = 'STOCKIN' then 1 else 0 end) as stockin_qty " +
                            "from SFISM4.R_WIP_TRACKING_T wt " +
                            "join SFISM4.r_mo_base_t mb on wt.mo_number = mb.mo_number and wt.model_name = mb.model_name " +
                            "left join SFISM4.r_wip_keyparts_t wk on wt.serial_number = wk.key_part_sn " +
                            "where wt.model_name in (:modelList) and wt.mo_number like '256%' and mb.mo_close_date is null " +
                            "group by wt.mo_number, mb.target_qty, wt.model_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findWipLinkByModel", e);
            return Collections.emptyList();
        }
    }

    public static TestGroup mapping(Map<String, Object> objectMap) {
        TestGroup ins = new TestGroup();
        ins.setMo((String)objectMap.get("mo_number"));
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String)objectMap.get("model_name"));
        ins.setGroupName((String)objectMap.get("wip_group"));

        if (objectMap.containsKey("total_qty")) {
            ins.setPlan(((Number) objectMap.get("total_qty")).intValue());
        }

        if (objectMap.containsKey("wip_qty")) {
            ins.setWip(((Number) objectMap.get("wip_qty")).intValue());
        }

        if (objectMap.containsKey("total")) {
            ins.setWip(((Number) objectMap.get("total")).intValue());
        }

        if (objectMap.containsKey("link_qty")) {
            ins.setPass(((Number) objectMap.get("link_qty")).intValue());
        }

        if (objectMap.containsKey("target_qty")) {
            ins.setPlan(((Number) objectMap.get("target_qty")).intValue());
        }

        return ins;
    }

    public static TestGroup mappingWip(Map<String, Object> objectMap) {
        TestGroup ins = new TestGroup();
        ins.setMo((String)objectMap.get("mo_number"));
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String)objectMap.get("model_name"));
        ins.setGroupName((String)objectMap.get("wip_group"));
//        ins.setLineName((String)objectMap.get("wip_group"));

        if (objectMap.containsKey("total_qty")) {
            ins.setPlan(((Number) objectMap.get("total_qty")).intValue());
        }

        if (objectMap.containsKey("wip_qty")) {
            ins.setWip(((Number) objectMap.get("wip_qty")).intValue());
        }

        if (objectMap.containsKey("total")) {
            ins.setWip(((Number) objectMap.get("total")).intValue());
        }

        if (objectMap.containsKey("link_qty")) {
            ins.setPass(((Number) objectMap.get("link_qty")).intValue());
        }

        if (objectMap.containsKey("target_qty")) {
            ins.setPlan(((Number) objectMap.get("target_qty")).intValue());
        }

        return ins;
    }

    public static TestWipTracking mappingWipTracking(Map<String, Object> objectMap) {
        TestWipTracking ins = new TestWipTracking();
        ins.setSerialNumber((String)objectMap.get("serial_number"));
        ins.setMo((String)objectMap.get("mo_number"));
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String)objectMap.get("model_name"));
        ins.setGroupName((String)objectMap.get("group_name"));
        ins.setStationName((String)objectMap.get("station_name"));

        ins.setInStationTime((Date)objectMap.get("in_station_time"));
        ins.setWipGroup((String)objectMap.get("wip_group"));


        return ins;
    }
}
