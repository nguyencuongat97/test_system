package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class B04LogRepository {

    @Autowired
    @Qualifier("b04JdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<Parameter> getParametersByModelName(String modelName) {
        try {
            return jdbcTemplate.query("SELECT * FROM " + modelName + "Parameter", new BeanPropertyRowMapper<>(Parameter.class));
        } catch (Exception e) {
            log.error("### getParametersByModelName modelName {}", modelName, e);
            return Collections.emptyList();
        }
    }

    public Parameter getParameterByModelNameAndParameter(String modelName, String parameter) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + modelName + "Parameter WHERE parameters=?", new BeanPropertyRowMapper<>(Parameter.class), parameter);
        } catch (Exception e) {
            log.error("### getParameterByModelNameAndParameter modelName {} parameter {}", modelName, parameter, e);
            return null;
        }
    }

    public <T> List<T> getValueOfParameterByModelNameAndStationNameAndDateTimeBetween(
            String parameter, String modelName, String stationName, Date startDate, Date endDate, Class<T> elementType) {
        try {
            if (StringUtils.isEmpty(stationName)) {
                return jdbcTemplate.query("SELECT " + parameter + " FROM " + modelName + "Log WHERE ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ?;",
                        new SingleColumnRowMapper<>(elementType), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()))
                        .stream().filter(Objects::nonNull).collect(Collectors.toList());
            }
            return jdbcTemplate.query("SELECT " + parameter + " FROM " + modelName + "Log WHERE ATE_NAME=? AND ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ?;",
                    new SingleColumnRowMapper<>(elementType), stationName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()))
                    .stream().filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getValueOfParameterByModelNameAndStationNameAndDateTimeBetween modelName {}, stationName {} startDate {} endDate {}", modelName, stationName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getByModelNameAndDateTimeBetween(
            String modelName, Date startDate, Date endDate) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM " + modelName + "Log WHERE ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ?;",
                    new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
        } catch (Exception e) {
            log.error("### getByModelNameAndGroupNameAndStationNameAndDateTimeBetween modelName {}, startDate {}, endDate {}", modelName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getByModelNameAndDateTimeBetween(
            String modelName, Date startDate, Date endDate, int pageNumber, int pageSize) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY (SELECT NULL)) AS r, * FROM " + modelName + "Log WHERE ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ?) as tbl WHERE r > ? AND r <= ?",
                    new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), pageNumber * pageSize, (pageNumber + 1)*pageSize);
        } catch (Exception e) {
            log.error("### getByModelNameAndGroupNameAndStationNameAndDateTimeBetween modelName {}, startDate {}, endDate {}, pageNumber {}, pageSize {}", modelName, startDate, endDate, pageNumber, pageSize, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getByModelNameAndGroupNameAndStationNameAndDateTimeBetween(
            String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM " + modelName + "Log WHERE STATION_NAME=? AND ATE_NAME=? AND ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ?;",
                    groupName, stationName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
        } catch (Exception e) {
            log.error("### getByModelNameAndGroupNameAndStationNameAndDateTimeBetween modelName {}, groupName {}, stationName {}, startDate {}, endDate {}", modelName, groupName, stationName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListByModelNameAndGroupNameAndStationNameAndDateTimeBetweenAndIdGreaterThan(
            String modelName, String groupName, String stationName, Date startDate, Date endDate, Long latestSyncId) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM " + modelName + "Log WHERE STATION_NAME=? AND ATE_NAME=? AND ERROR_CODE='PASS' AND DATE_TIME BETWEEN ? AND ? AND ID > ? ORDER BY ID ASC;",
                    groupName, stationName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), latestSyncId);
        } catch (Exception e) {
            log.error("### getByModelNameAndGroupNameAndStationNameAndDateTimeBetween modelName {}, groupName {}, stationName {}, startDate {}, endDate {}, latestSyncId {}", modelName, groupName, stationName, startDate, endDate, latestSyncId, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListByModelNameAndIdGreaterThanAndDateTimeBetween(
            String modelName, Long latestSyncId, Date startDate, Date endDate) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM " + modelName + "Log WHERE ERROR_CODE='PASS' AND ID > ? AND DATE_TIME BETWEEN ? AND ? ORDER BY ID ASC;",
                    latestSyncId, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
        } catch (Exception e) {
            log.error("### getByModelNameAndGroupNameAndStationNameAndDateTimeBetween modelName {}, latestSyncId {}, startDate {}, endDate {}", modelName, latestSyncId, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<Timestamp> getCalibrationHistory(String modelName, String groupName, String stationName, Integer limit) {
        if (limit == null) {
            limit = 10;
        }

        try {
            return jdbcTemplate.query("SELECT TOP " + limit + " date_time FROM " + modelName + "Log WHERE station_name=? AND ate_name=? AND error_code='GOLDEN_SAMPLE' ORDER BY date_time DESC;",
                    new SingleColumnRowMapper<>(Timestamp.class),
                    groupName, stationName);
        } catch (Exception e) {
            log.error("### getCalibrationHistory modelName {}, groupName {}, stationName {}", modelName, groupName, stationName, e);
            return Collections.emptyList();
        }
    }
}
