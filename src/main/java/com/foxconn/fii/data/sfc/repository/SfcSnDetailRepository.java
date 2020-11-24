package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.sfc.model.TestWipTracking;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SfcSnDetailRepository {

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

    public List<Map<String, Object>> findByModelNameAndInStationTimeBetween(String factory, String customer, List<String> modelList, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT to_char(in_station_time, 'yyyymmdd') as work_date, to_char(round(in_station_time, 'hh24'), 'hh24') as work_section, " +
                            "mo_number, model_name, line_name, group_name, sum(case when error_flag = 0 then 1 else 0 end) as pass_qty " +
                            "FROM SFISM4.R_SN_DETAIL_T WHERE group_name = 'CTN_WEIGHT   ' AND " + (modelList.isEmpty() ? "" : "model_name in (:modelList) AND ") + "in_station_time between :current and :next " +
                            "GROUP BY to_char(in_station_time, 'yyyymmdd'), to_char(round(in_station_time, 'hh24'), 'hh24'), mo_number, model_name, line_name, group_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findByModelNameAndWorkDateAndShiftType modelName {}, startDate {}, endDate {}", modelList, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findGroupQuarterByModelNameAndInStationTimeBetween(String factory, String customer, List<String> modelList, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("select trunc(in_station_time, 'HH') + floor(to_char(in_station_time, 'MI')/15)*15/1440 as work_time, " +
                            "mo_number, model_name, line_name, group_name, sum(case when error_flag = 0 then 1 else 0 end) as pass_qty, sum(case when error_flag = 1 then 1 else 0 end) as fail_qty " +
                            "from SFISM4.R_SN_DETAIL_T where model_name in (:modelList) AND in_station_time between :current and :next " +
                            "group by trunc(in_station_time, 'HH') + floor(to_char(in_station_time, 'MI')/15)*15/1440, mo_number, model_name, line_name, group_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findByModelNameAndWorkDateAndShiftType modelName {}, startDate {}, endDate {}", modelList, startDate, endDate, e);
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
            parameters.put("serial", serialNumber);

            return namedJdbcTemplate.queryForList("SELECT *" +
                            "FROM SFISM4.R_SN_DETAIL_T WHERE serial_number = :serial",
                    parameters);
        } catch (Exception e) {
            log.error("### findBySerialNumber serial number {}", serialNumber, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoAndGroupName(String factory, String customer, String mo, String groupName) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("mo", mo);
            parameters.put("groupName", groupName);

            return namedJdbcTemplate.queryForList("SELECT *" +
                            "FROM SFISM4.R_SN_DETAIL_T WHERE mo_number = :mo AND group_name = :groupName",
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoAndGroupName mo number {} groupName, {}", mo, groupName, e);
            return Collections.emptyList();
        }
    }

    public static TestGroup mapping(String factory, Map<String, Object> objectMap) {
        TestGroup ins = new TestGroup();
        ins.setFactory(factory);
        ins.setMo((String)objectMap.get("mo_number"));
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String)objectMap.get("model_name"));
        ins.setGroupName((String)objectMap.get("group_name"));

        ins.setWip(((Number)objectMap.get("pass_qty")).intValue());
        ins.setPass(((Number)objectMap.get("pass_qty")).intValue());
        ins.setFail(((Number)objectMap.get("fail_qty")).intValue());
        String workDate = (String) objectMap.get("work_date");

        if (objectMap.get("work_section") != null) {
            try {
                int workSection = Integer.parseInt((String) objectMap.get("work_section"));

                TimeSpan timeSpan = TimeSpan.from(workDate, workSection);
                if (timeSpan != null) {
                    ins.setStartDate(timeSpan.getStartDate());
                    ins.setEndDate(timeSpan.getEndDate());
                }
            } catch (Exception e) {
                log.error("### mapping error", e);
            }
        }

        if (objectMap.get("work_time") != null) {
            try {
                Date startDate = new Date(((Timestamp) objectMap.get("work_time")).getTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.MINUTE, 15);

                ins.setStartDate(startDate);
                ins.setEndDate(calendar.getTime());
            } catch (Exception e) {
                log.error("### mapping error", e);
            }
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
