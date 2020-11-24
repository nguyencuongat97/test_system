package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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
public class SfcTestGroupRepository {

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
    @Qualifier("b04sfcNamedJdbcTemplate")
    private NamedParameterJdbcTemplate b04NamedJdbcTemplate;

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
        } else if (Factory.B04.equalsIgnoreCase(factory)){
            return b04NamedJdbcTemplate;
        }
        return null;
    }

    public List<Map<String, Object>> findByLineNameAndWorkDateAndShiftType(String factory, String customer, String lineName, Date workDate, ShiftType shiftType) {
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
            parameters.put("lineName", lineName);
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT work_date, work_section, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE line_name = :lineName AND " + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY work_date, work_section, model_name, group_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findByLineNameAndWorkDateAndShiftType lineName {}, workDate {}, shiftType {}", lineName, workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByModelNameAndWorkDateAndShiftType(String factory, String customer, List<String> modelList, Date workDate, ShiftType shiftType) {
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

            return namedJdbcTemplate.queryForList("SELECT mo_number, work_date, work_section, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " + (modelList.isEmpty() ? "" : "model_name in (:modelList) AND ") + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY mo_number, work_date, work_section, line_name, model_name, group_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findByModelNameAndWorkDateAndShiftType modelName {}, workDate {}, shiftType {}", modelList, workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByWorkDateAndShiftType(String factory, String customer, Date workDate, ShiftType shiftType) {
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
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY model_name, group_name",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType workDate {}, shiftType {}", workDate, shiftType, e);
            return Collections.emptyList();
        }
    }


    public List<TestGroupDaily> findByWorkDateBetween(String factory, String customer, List<String> modelList, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " +
                            (modelList != null && !modelList.isEmpty() ? "model_name in (:modelList) AND " : "") +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroupDaily> findByWorkDateBetween(String factory, String customer, String modelName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroupDaily> findByWorkDateBetween(String factory, String customer, String sectionName, List<String> modelList, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("sectionName", sectionName);
            parameters.put("modelList", modelList);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " +
                            (modelList != null && !modelList.isEmpty() ? "model_name in (:modelList) AND section_name = :sectionName AND " : "") +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroupDaily> findByWorkDateBetween(String factory, String customer, String sectionName, String modelName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("sectionName", sectionName);
            parameters.put("modelName", modelName);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE section_name = :sectionName AND model_name = :modelName AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroup> findHourlyByWorkDateBetween(String factory, String customer, String modelName, String groupName, Date startDate, Date endDate) {
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

            return namedJdbcTemplate.queryForList("SELECT work_date, work_section, line_name, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND group_name = :groupName AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY work_date, work_section, line_name, model_name, group_name ORDER BY work_date, work_section",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mappingToTestGroup(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findStationHourlyByWorkDateBetween startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestStationDaily> findStationHourlyByWorkDateBetween(String factory, String customer, String modelName, String groupName, Date startDate, Date endDate) {
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

            return namedJdbcTemplate.queryForList("SELECT work_date, work_section, line_name, model_name, group_name, station_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND group_name = :groupName AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY work_date, work_section, line_name, model_name, group_name, station_name ORDER BY work_date, work_section",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapToTestStation(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findStationHourlyByWorkDateBetween startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestStationDaily> findStationByWorkDateBetween(String factory, String customer, String modelName, String groupName, Date startDate, Date endDate) {
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

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, station_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND group_name = :groupName AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name, station_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapToTestStation(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findStationByWorkDateBetween startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestStationDaily> findStationByWorkDateBetween(String factory, String customer, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
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

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, line_name, model_name, group_name, station_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T " +
                            "WHERE model_name = :modelName AND group_name = :groupName AND station_name = :stationName AND " + TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", line_name, model_name, group_name, station_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapToTestStation(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### findByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByMoNumber(String factory, String customer, String mo) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("mo_number", mo);

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkTimeColumn() + " as work_time, model_name, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE mo_number = :mo_number " +
                            "GROUP BY " + TestUtils.getWorkTimeColumn() + ", model_name, group_name ORDER BY work_time",
                    parameters);
        } catch (Exception e) {
            log.error("### findByMoNumber mo {}", mo, e);
            return Collections.emptyList();
        }
    }

    public List<String> getStationNameByModelNameAndWorkDateAndShiftType(String factory, String customer, String modelName, String groupName, Date workDate, ShiftType shiftType) {
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
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT station_name " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND group_name = :groupName AND " + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY station_name",
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getStationNameByModelNameAndWorkDateAndShiftType modelName {}, workDate {}, shiftType {}", modelName, workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<String> getGroupNameByModelNameAndWorkDateAndShiftType(String factory, String customer, String modelName, Date workDate, ShiftType shiftType) {
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
            parameters.put("modelName", modelName);
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT group_name " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name = :modelName AND " + TestUtils.getWorkSectionCondition(shiftType) +
                            "GROUP BY group_name",
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### findByModelNameAndWorkDateAndShiftType modelName {}, workDate {}, shiftType {}", modelName, workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<String> getLineNameByWorkDateAndShiftType(String factory, String customer, List<String> modelList, Date workDate, ShiftType shiftType) {
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

            return namedJdbcTemplate.queryForList("SELECT DISTINCT(line_name) " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name in (:modelList) AND " + TestUtils.getWorkSectionCondition(shiftType),
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getLineNameByWorkDateAndShiftType workDate {}, shiftType {}", workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<String> getModelNameByWorkDateAndShiftType(String factory, String customer, Date workDate, ShiftType shiftType) {
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
            parameters.put("current", df.format(workDate));
            parameters.put("next", df.format(calendar.getTime()));

            return namedJdbcTemplate.queryForList("SELECT DISTINCT(model_name) " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " + TestUtils.getWorkSectionCondition(shiftType),
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getModelNameByWorkDateAndShiftType workDate {}, shiftType {}", workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    public List<String> getModelNameByWorkDateBetween(String factory, String customer, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT DISTINCT(model_name) " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE " + TestUtils.getWorkDateBetween(),
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getModelNameByWorkDateAndShiftType startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<String> getMoByWorkDateAndShiftType(String factory, String customer, List<String> modelList, Date workDate, ShiftType shiftType) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelList", modelList);
            if (workDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(workDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);

                parameters.put("current", df.format(workDate));
                parameters.put("next", df.format(calendar.getTime()));
            }

            return namedJdbcTemplate.queryForList("SELECT DISTINCT(mo_number) " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE model_name in (:modelList) " + (workDate != null ? "AND " + TestUtils.getWorkSectionCondition(shiftType) : ""),
                    parameters, String.class);
        } catch (Exception e) {
            log.error("### getModelNameByWorkDateAndShiftType workDate {}, shiftType {}", workDate, shiftType, e);
            return Collections.emptyList();
        }
    }

    //TUNG modify QUAlity
    public List<TestGroupDaily> getWorkDateByGroupNameByDay(String factory, String customer, List<String> listModel, String sectionName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("sectionName", sectionName);
            parameters.put("listModel", listModel);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkDateWorkSectionByDay() + " as work_time, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE (mo_number like '2151%' or mo_number like '2150%') AND section_name = :sectionName AND model_name IN (:listModel) AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkDateWorkSectionByDay() + ", group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getWorkDateByGroupNameByMonth startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroupDaily> getWorkDateByGroupNameByDay(String factory, String customer, List<String> listModel, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("sectionName", sectionName);
            parameters.put("listModel", listModel);
            parameters.put("current", df.format(startDate));
            parameters.put("next", df.format(endDate));

            return namedJdbcTemplate.queryForList("SELECT " + TestUtils.getWorkDateWorkSectionByDay() + " as work_time, group_name, SUM(wip_qty) wip_qty, SUM(pass_qty) pass_qty, SUM(fail_qty) fail_qty, SUM(first_fail_qty) first_fail_qty, SUM(repair_qty) repair_qty, SUM(retest_qty) retest_qty, SUM(repass_qty) repass_qty " +
                            "FROM SFISM4.R_STATION_ATE_T WHERE (mo_number like '2151%' or mo_number like '2150%') AND model_name IN (:listModel) AND " +
                            TestUtils.getWorkDateBetween() +
                            "GROUP BY " + TestUtils.getWorkDateWorkSectionByDay() + ", group_name ORDER BY work_time",
                    parameters)
                    .stream().map(map -> SfcTestGroupRepository.mapping(factory, map)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getWorkDateByGroupNameByMonth startDate {}, endDate {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }


    public static TestGroupDaily mapping(String factory, Map<String, Object> objectMap) {
        TestGroupDaily ins = new TestGroupDaily();
        ins.setFactory(factory);
        ins.setWorkDate((String) objectMap.get("work_time"));
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String) objectMap.get("model_name"));
        ins.setGroupName((String) objectMap.get("group_name"));

        ins.setWip(((Number) objectMap.get("wip_qty")).intValue());
        ins.setPass(((Number) objectMap.get("pass_qty")).intValue());
        ins.setFail(((Number) objectMap.get("fail_qty")).intValue());
        ins.setFirstFail(((Number) objectMap.get("first_fail_qty")).intValue());
        ins.setSecondFail(((Number) objectMap.get("repair_qty")).intValue());
        ins.setRetest(((Number) objectMap.get("retest_qty")).intValue());
        ins.setRepass(((Number) objectMap.get("repass_qty")).intValue());

        ins.setMo((String)objectMap.get("mo_number"));

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

        return ins;
    }

    public static TestStationDaily mapToTestStation(String factory, Map<String, Object> objectMap) {
        TestStationDaily ins = new TestStationDaily();
        ins.setFactory(factory);
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String) objectMap.get("model_name"));
        ins.setGroupName((String) objectMap.get("group_name"));
        ins.setStationName((String) objectMap.get("station_name"));

        ins.setWip(((Number) objectMap.get("wip_qty")).intValue());
        ins.setPass(((Number) objectMap.get("pass_qty")).intValue());
        ins.setFail(((Number) objectMap.get("fail_qty")).intValue());
        ins.setFirstFail(((Number) objectMap.get("first_fail_qty")).intValue());
        ins.setSecondFail(((Number) objectMap.get("repair_qty")).intValue());
        ins.setRetest(((Number) objectMap.get("retest_qty")).intValue());
        ins.setRepass(((Number) objectMap.get("repass_qty")).intValue());

        ins.setMo((String)objectMap.get("mo_number"));

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

        return ins;
    }

    public static TestGroup mappingToTestGroup(String factory, Map<String, Object> objectMap) {
        TestGroup ins = new TestGroup();
        ins.setFactory(factory);
        ins.setLineName((String)objectMap.get("line_name"));
        ins.setModelName((String) objectMap.get("model_name"));
        ins.setGroupName((String) objectMap.get("group_name"));

        ins.setWip(((Number) objectMap.get("wip_qty")).intValue());
        ins.setPass(((Number) objectMap.get("pass_qty")).intValue());
        ins.setFail(((Number) objectMap.get("fail_qty")).intValue());
        ins.setFirstFail(((Number) objectMap.get("first_fail_qty")).intValue());
        ins.setSecondFail(((Number) objectMap.get("repair_qty")).intValue());
        ins.setRetest(((Number) objectMap.get("retest_qty")).intValue());
        ins.setRepass(((Number) objectMap.get("repass_qty")).intValue());

        ins.setMo((String)objectMap.get("mo_number"));

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

        return ins;
    }
}
