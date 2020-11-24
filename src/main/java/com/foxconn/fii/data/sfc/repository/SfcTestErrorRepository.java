package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SfcTestErrorRepository {

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

    public List<Map<String, Object>> findByModel(String factory, String customer, List<String> modelList, Date workDate, ShiftType shiftType) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(workDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT mo_number, work_date, work_section, line_name, model_name, group_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae, SFIS1.C_ERROR_CODE_T ec WHERE ae.error_code = ec.error_code and model_name in (:modelList) AND " + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY mo_number, work_date, work_section, line_name, model_name, group_name, ae.error_code, ec.error_desc",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> findByModelAndGroup(String factory, String customer, String modelName, String groupName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT ae.model_name, ae.group_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae LEFT JOIN SFIS1.C_ERROR_CODE_T ec ON ae.error_code = ec.error_code " +
                            "WHERE ae.model_name = :modelName AND ae.group_name = :groupName AND " + TestUtils.getWorkDateBetween() +
                            "GROUP BY ae.model_name, ae.group_name, ae.error_code, ec.error_desc",
                    parameters)
                    .stream().map(map -> SfcTestErrorRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> findByModelAndGroupAndStation(String factory, String customer, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("stationName", stationName);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae LEFT JOIN SFIS1.C_ERROR_CODE_T ec ON ae.error_code = ec.error_code " +
                            "WHERE ae.model_name = :modelName AND ae.group_name = :groupName AND ae.station_name = :stationName AND " + TestUtils.getWorkDateBetween() +
                            "GROUP BY ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc",
                    parameters)
                    .stream().map(map -> SfcTestErrorRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> findByModelAndErrorCode(String factory, String customer, String modelName, String errorCode, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("errorCode", errorCode);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae LEFT JOIN SFIS1.C_ERROR_CODE_T ec ON ae.error_code = ec.error_code " +
                            "WHERE ae.model_name = :modelName AND ae.error_code = :errorCode AND " + TestUtils.getWorkDateBetween() +
                            "GROUP BY ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc",
                    parameters)
                    .stream().map(map -> SfcTestErrorRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<TestError> findHourlyByModelAndGroupAndStationAndErrorCode(String factory, String customer, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("stationName", stationName);
            parameters.put("errorCode", errorCode);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT ae.work_date, ae.work_section, ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae LEFT JOIN SFIS1.C_ERROR_CODE_T ec ON ae.error_code = ec.error_code " +
                            "WHERE ae.model_name = :modelName AND ae.group_name = :groupName AND " +
                            (StringUtils.isEmpty(stationName) ? "" : "ae.station_name = :stationName AND ") +
                            "ae.error_code = :errorCode AND " + TestUtils.getWorkDateBetween() +
                            "GROUP BY ae.work_date, ae.work_section, ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc",
                    parameters)
                    .stream().map(map -> SfcTestErrorRepository.mapToTestError(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> findByModelAndGroupAndStationAndErrorCode(String factory, String customer, String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("stationName", stationName);
            parameters.put("errorCode", errorCode);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, ae.group_name, ae.station_name, ae.error_code, ec.error_desc, sum(fail_qty) fail_qty, sum(test_fail_qty) test_fail_qty " +
                            "FROM SFISM4.R_ATE_ERRCODE_T ae LEFT JOIN SFIS1.C_ERROR_CODE_T ec ON ae.error_code = ec.error_code " +
                            "WHERE ae.model_name = :modelName AND ae.group_name = :groupName AND " +
                            (StringUtils.isEmpty(stationName) ? "" : "ae.station_name = :stationName AND ") +
                            (StringUtils.isEmpty(errorCode) ? "" : "ae.error_code = :errorCode AND ") + TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", ae.model_name, ae.group_name, ae.station_name, ae.error_code, ec.error_desc",
                    parameters)
                    .stream().map(map -> SfcTestErrorRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public static TestErrorDaily mapping(String factory, Map<String, Object> objectMap) {
        TestErrorDaily ins = new TestErrorDaily();
        ins.setFactory(factory);
//        ins.setMo((String) objectMap.get("mo_number"));
//        ins.setLineName((String) objectMap.get("line_name"));
        ins.setModelName((String) objectMap.get("model_name"));
        ins.setGroupName((String) objectMap.get("group_name"));
        ins.setStationName((String) objectMap.get("station_name"));

        ins.setErrorCode((String) objectMap.get("error_code"));
        ins.setErrorDescription((String) objectMap.get("error_desc"));

        String workDate = (String) objectMap.get("work_date");
        if (objectMap.get("work_section") != null) {
            int workSection = ((Number) objectMap.get("work_section")).intValue();

            TimeSpan timeSpan = TimeSpan.from(workDate, workSection);
            if (timeSpan != null) {
                ins.setStartDate(timeSpan.getStartDate());
                ins.setEndDate(timeSpan.getEndDate());
            }
        }

        if (objectMap.get("work_time") != null) {
            try {
                TimeSpan timeSpan = TimeSpan.from((String)objectMap.get("work_time"), "yyyyMMdd", TimeSpan.Type.DAILY);
                if (timeSpan != null) {
                    ins.setStartDate(timeSpan.getStartDate());
                    ins.setEndDate(timeSpan.getEndDate());
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        ins.setFail(((Number) objectMap.get("fail_qty")).intValue());
        ins.setTestFail(((Number) objectMap.get("test_fail_qty")).intValue());

        return ins;
    }

    public static TestError mapToTestError(String factory, Map<String, Object> objectMap) {
        TestError ins = new TestError();
        ins.setFactory(factory);
//        ins.setMo((String) objectMap.get("mo_number"));
//        ins.setLineName((String) objectMap.get("line_name"));
        ins.setModelName((String) objectMap.get("model_name"));
        ins.setGroupName((String) objectMap.get("group_name"));
        ins.setStationName((String) objectMap.get("station_name"));

        ins.setErrorCode((String) objectMap.get("error_code"));
        ins.setErrorDescription((String) objectMap.get("error_desc"));

        String workDate = (String) objectMap.get("work_date");
        if (objectMap.get("work_section") != null) {
            int workSection = ((Number) objectMap.get("work_section")).intValue();

            TimeSpan timeSpan = TimeSpan.from(workDate, workSection);
            if (timeSpan != null) {
                ins.setStartDate(timeSpan.getStartDate());
                ins.setEndDate(timeSpan.getEndDate());
            }
        }

        ins.setFail(((Number) objectMap.get("fail_qty")).intValue());
        ins.setTestFail(((Number) objectMap.get("test_fail_qty")).intValue());

        return ins;
    }
}
