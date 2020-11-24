package com.foxconn.fii.data.b04ds02.repository;

import com.foxconn.fii.data.TestCycleTime;
import com.foxconn.fii.data.b04ds02.model.B04DS02ErrorLog;
import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestErrorDaily;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestStation;
import com.foxconn.fii.data.primary.model.entity.TestStationDaily;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class B04DS02ErrorLogRepository {

    @Autowired
    @Qualifier("b04ds02JdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<B04DS02ErrorLog> getLogList(
            String modelName, String groupName, String errorCode, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`sfc_tester`AS station_name, c.tester, c.chamber, yr.`time`, yr.`serial`, e.`name` AS error_description, e.`error_define` AS error_code, e.lsl, e.usl, yr.`value`, yr.`cycle`\n" +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "JOIN `error` e ON yr.error = e.error_id " +
                            "WHERE p.part_number = ? " +
                            "AND c.sfc_station = ? " +
                            "AND e.name = ? " +
                            "AND yr.`time` BETWEEN ? AND ? " +
                            "ORDER BY yr.`time` DESC;",
                    modelName, groupName, errorCode, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));

            return rows.stream().map(B04DS02ErrorLog::of).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getLogList modelName {}, groupName {}, errorCode {}, startDate {}, endDate {}", modelName, groupName, errorCode, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<B04DS02ErrorLog> getLogList(
            String modelName, String groupName, String stationName, String errorCode, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`sfc_tester`AS station_name, c.tester, c.chamber, yr.`time`, yr.`serial`, e.`name` AS error_description, e.`error_define` AS error_code, e.lsl, e.usl, yr.`value`, yr.`cycle`\n" +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "JOIN `error` e ON yr.error = e.error_id " +
                            "WHERE p.part_number = ? " +
                            "AND c.sfc_station = ? " +
                            "AND c.sfc_tester = ? " +
                            "AND e.name = ? " +
                            "AND yr.`time` BETWEEN ? AND ? " +
                            "ORDER BY yr.`time` DESC;",
                    modelName, groupName, stationName, errorCode, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));

            return rows.stream().map(B04DS02ErrorLog::of).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getLogList modelName {}, groupName {}, stationName {}, errorCode {}, startDate {}, endDate {}", modelName, groupName, stationName, errorCode, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<B04DS02ErrorLog> getHistoryOfSerial(String serial) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`sfc_tester`AS station_name, c.tester, c.chamber, yr.`time`, yr.`serial`, e.`name` AS error_description, e.`error_define` AS error_code, e.lsl, e.usl, yr.`value`, yr.`cycle`\n" +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "JOIN `error` e ON yr.error = e.error_id " +
                            "WHERE yr.`serial` = ? " +
                            "ORDER BY yr.`time` DESC;",
                    serial);

            return rows.stream().map(B04DS02ErrorLog::of).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getHistoryOfSerial serial {}", serial, e);
            return Collections.emptyList();
        }
    }


    public List<TestCycleTime> getCycleTimeByGroup(Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, " +
                            "sum(yr.`cycle`) AS total_cycle, count(*) as total_number, " +
                            "sum(case when yr.error = 1 then yr.`cycle` else 0 end) AS pass_cycle, sum(case when yr.error = 1 then 1 else 0 end) AS pass_number, " +
                            "sum(case when yr.error = 1 then 0 else yr.`cycle` end) AS fail_cycle, sum(case when yr.error = 1 then 0 else 1 end) AS fail_number " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "GROUP BY p.`part_number`, c.`sfc_station`",
                    startDate, endDate);

            return rows.stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getCycleTimeByGroup {} {} {} {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestCycleTime> getCycleTimeByStation(String modelName, String groupName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`sfc_tester`AS station_name, c.`tester`, c.chamber, " +
                            "sum(yr.`cycle`) AS total_cycle, count(*) as total_number, " +
                            "sum(case when yr.error = 1 then yr.`cycle` else 0 end) AS pass_cycle, sum(case when yr.error = 1 then 1 else 0 end) AS pass_number, " +
                            "sum(case when yr.error = 1 then 0 else yr.`cycle` end) AS fail_cycle, sum(case when yr.error = 1 then 0 else 1 end) AS fail_number " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE p.part_number = ? AND c.station = ? AND yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "GROUP BY p.`part_number`, c.`sfc_station`, c.`tester`, c.`sfc_tester`, c.chamber;",
                    modelName, groupName, startDate, endDate);

            return rows.stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getCycleTimeByStation {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }


    public List<TestGroup> getGroupListByTime(String modelName, String groupName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name " +
                            ", date_add(date_format(yr.`time`, '%Y/%m/%d %H:00:00'), interval if(minute(yr.`time`) < 30, 0, 1) hour) as start_date " +
                            ", date_add(date_format(yr.`time`, '%Y/%m/%d %H:00:00'), interval if(minute(yr.`time`) < 30, 1, 2) hour) as end_date " +
                            ", sum(if(yr.error = 1, 1, 0)) as pass, sum(if(yr.error != 1, 1, 0)) as fail" +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE p.part_number = ? AND c.sfc_station = ? AND yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "group by model_name, group_name, start_date, end_date",
                    modelName, groupName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestGroup).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestGroupDaily> getGroupList(String modelName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name " +
                            ", sum(if(yr.error = 1, 1, 0)) as pass, sum(if(yr.error != 1, 1, 0)) as fail " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE p.part_number = ? AND yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "group by model_name, group_name",
                    modelName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestGroupDaily).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {}", modelName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestStation> getStationListByTime(String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`tester`, c.`sfc_tester`AS station_name, c.chamber " +
                            ", date_add(date_format(yr.`time`, '%Y/%m/%d %H:00:00'), interval if(minute(yr.`time`) < 30, 0, 1) hour) as start_date " +
                            ", date_add(date_format(yr.`time`, '%Y/%m/%d %H:00:00'), interval if(minute(yr.`time`) < 30, 1, 2) hour) as end_date " +
                            ", sum(if(yr.error = 1, 1, 0)) as pass, sum(if(yr.error != 1, 1, 0)) as fail " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE p.part_number = ? AND c.sfc_station = ? AND c.`sfc_tester` = ? AND yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "group by model_name, group_name, tester, station_name, chamber, start_date, end_date",
                    modelName, groupName, stationName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestStation).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {} {} {}", modelName, groupName, stationName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestStationDaily> getStationList(String modelName, String groupName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`tester`, c.`sfc_tester`AS station_name, c.chamber " +
                            ", sum(if(yr.error = 1, 1, 0)) as pass, sum(if(yr.error != 1, 1, 0)) as fail " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "WHERE p.part_number = ? AND c.sfc_station = ? AND yr.`time` BETWEEN ? AND ? AND offline = 0 " +
                            "group by model_name, group_name, tester, station_name, chamber",
                    modelName, groupName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestStationDaily).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> getErrorList(String modelName, String groupName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, e.`error_define` AS error_code, e.`name` AS error_description " +
                            ", count(*) as test_fail " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "JOIN `error` e ON yr.error = e.error_id " +
                            "WHERE p.part_number = ? AND c.sfc_station = ? AND yr.`time` BETWEEN ? AND ? AND yr.error != 1 AND offline = 0 " +
                            "group by model_name, group_name, error_code, error_description",
                    modelName, groupName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestErrorDaily).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestErrorDaily> getErrorList(String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT p.`part_number` AS model_name, c.`sfc_station`AS group_name, c.`tester`, c.`sfc_tester`AS station_name, c.chamber, e.`error_define` AS error_code, e.`name` AS error_description " +
                            ", count(*) as test_fail " +
                            "FROM `yield_rate` yr " +
                            "JOIN `chamber` c ON yr.chamber = c.chamber_id " +
                            "JOIN `product` p ON yr.exx = p.product_id " +
                            "JOIN `error` e ON yr.error = e.error_id " +
                            "WHERE p.part_number = ? AND c.sfc_station = ? AND c.`sfc_tester` = ? AND yr.`time` BETWEEN ? AND ? AND yr.error != 1 AND offline = 0 " +
                            "group by model_name, group_name, tester, station_name, chamber, error_code, error_description",
                    modelName, groupName, stationName, startDate, endDate);

            return rows.stream().map(B04DS02ErrorLogRepository::mapToTestErrorDaily).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getGroupList {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public static TestGroupDaily mapToTestGroupDaily(Map<String, Object> map) {
        TestGroupDaily group = new TestGroupDaily();
        group.setFactory("B04");
        group.setModelName((String) map.getOrDefault("model_name", ""));
        group.setGroupName((String) map.getOrDefault("group_name", ""));
        group.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        group.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        group.setPass(((Number) map.getOrDefault("pass", 0)).intValue());
        group.setFirstFail(((Number) map.getOrDefault("fail", 0)).intValue());
        group.setWip(group.getPass() + group.getFail());
        group.setSecondFail(group.getFail());
        return group;
    }

    public static TestGroup mapToTestGroup(Map<String, Object> map) {
        TestGroup group = new TestGroup();
        group.setFactory("B04");
        group.setModelName((String) map.getOrDefault("model_name", ""));
        group.setGroupName((String) map.getOrDefault("group_name", ""));
        group.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        group.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        group.setPass(((Number) map.getOrDefault("pass", 0)).intValue());
        group.setFirstFail(((Number) map.getOrDefault("fail", 0)).intValue());
        group.setWip(group.getPass() + group.getFail());
        group.setSecondFail(group.getFail());
        return group;
    }

    public static TestStationDaily mapToTestStationDaily(Map<String, Object> map) {
        TestStationDaily station = new TestStationDaily();
        station.setFactory("B04");
        station.setModelName((String) map.getOrDefault("model_name", ""));
        station.setGroupName((String) map.getOrDefault("group_name", ""));
        station.setStationName((String) map.getOrDefault("station_name", ""));
        station.setTesterName((String) map.getOrDefault("tester", "") + "-" + (String) map.getOrDefault("chamber", ""));
        station.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        station.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        station.setPass(((Number) map.getOrDefault("pass", 0)).intValue());
        station.setFirstFail(((Number) map.getOrDefault("fail", 0)).intValue());
        station.setWip(station.getPass() + station.getFail());
        station.setSecondFail(station.getFail());
        return station;
    }

    public static TestStation mapToTestStation(Map<String, Object> map) {
        TestStation station = new TestStation();
        station.setFactory("B04");
        station.setModelName((String) map.getOrDefault("model_name", ""));
        station.setGroupName((String) map.getOrDefault("group_name", ""));
        station.setStationName((String) map.getOrDefault("station_name", ""));
        station.setTesterName((String) map.getOrDefault("tester", "") + "-" + (String) map.getOrDefault("chamber", ""));
        station.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        station.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        station.setPass(((Number) map.getOrDefault("pass", 0)).intValue());
        station.setFirstFail(((Number) map.getOrDefault("fail", 0)).intValue());
        station.setWip(station.getPass() + station.getFail());
        station.setSecondFail(station.getFail());
        return station;
    }

    public static TestErrorDaily mapToTestErrorDaily(Map<String, Object> map) {
        TestErrorDaily error = new TestErrorDaily();
        error.setFactory("B04");
        error.setModelName((String) map.getOrDefault("model_name", ""));
        error.setGroupName((String) map.getOrDefault("group_name", ""));
        error.setStationName((String) map.getOrDefault("station_name", ""));
        error.setErrorCode(((String) map.getOrDefault("error_code", "")).toUpperCase());
        error.setErrorDescription(((String) map.getOrDefault("error_description", "")).toUpperCase());
        error.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        error.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        error.setTestFail(((Number) map.getOrDefault("test_fail", 0)).intValue());
        return error;
    }

    public static TestError mapToTestError(Map<String, Object> map) {
        TestError error = new TestError();
        error.setFactory("B04");
        error.setModelName((String) map.getOrDefault("model_name", ""));
        error.setGroupName((String) map.getOrDefault("group_name", ""));
        error.setStationName((String) map.getOrDefault("station_name", ""));
        error.setErrorCode(((String) map.getOrDefault("error_code", "")).toUpperCase());
        error.setErrorDescription(((String) map.getOrDefault("error_description", "")).toUpperCase());
        error.setStartDate((Date) map.getOrDefault("start_date", new Date()));
        error.setEndDate((Date) map.getOrDefault("start_date", new Date()));
        error.setTestFail(((Number) map.getOrDefault("test_fail", 0)).intValue());
        return error;
    }
}
