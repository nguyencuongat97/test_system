package com.foxconn.fii.data.nbbtefii.repository;

import com.foxconn.fii.data.TestCycleTime;
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
public class NbbTeFiiEquipmentRepository {

    @Autowired
    @Qualifier("nbbtefiiNamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public Map<String, Object> stationLastConnectedTime(String stationId) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("stationId", stationId);

            return namedJdbcTemplate.queryForMap("SELECT LATEST_ONLINE_TIME\n" +
                            "FROM TE_ATE_METADATA\n" +
                            "WHERE ATE = :stationId",
                    parameters);
        } catch (Exception e) {
            log.info("### can't get station latest connected time from TE_NBB_FII for " + stationId +"  ###", e);
            return Collections.emptyMap();
        }
    }

    public List<TestCycleTime> getCycleTimeByGroup(Date startDate, Date endDate) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);

            List<Map<String, Object>> rows = namedJdbcTemplate.queryForList("SELECT project_id AS model_name, station AS group_name, " +
                            "sum(elapse_time) AS total_cycle, count(*) as total_number, " +
                            "sum(case when status = 'PASS' then elapse_time else 0 end) AS pass_cycle, sum(case when status = 'PASS' then 1 else 0 end) AS pass_number, " +
                            "sum(case when status = 'PASS' then 0 else elapse_time end) AS fail_cycle, sum(case when status = 'PASS' then 0 else 1 end) AS fail_number " +
                            "FROM TE_NBB_FII.TE_TEST_DATA " +
                            "WHERE test_mode = 'PRODUCTION' AND start_time BETWEEN :startDate and :endDate " +
                            "GROUP BY project_id, station",
                    parameters);

            return rows.stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getCycleTimeByGroup {} {} {} {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }

    public List<TestCycleTime> getCycleTimeByStation(String modelName, String groupName, Date startDate, Date endDate) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("groupName", groupName);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);

            List<Map<String, Object>> rows = namedJdbcTemplate.queryForList("SELECT project_id AS model_name, station AS group_name, ate AS station_name, " +
                            "sum(elapse_time) AS total_cycle, count(*) as total_number, " +
                            "sum(case when status = 'PASS' then elapse_time else 0 end) AS pass_cycle, sum(case when status = 'PASS' then 1 else 0 end) AS pass_number, " +
                            "sum(case when status = 'PASS' then 0 else elapse_time end) AS fail_cycle, sum(case when status = 'PASS' then 0 else 1 end) AS fail_number " +
                            "FROM TE_NBB_FII.TE_TEST_DATA " +
                            "WHERE project_id = :modelName AND station = :groupName AND test_mode = 'PRODUCTION' AND start_time BETWEEN :startDate and :endDate " +
                            "GROUP BY project_id, station, ate",
                    parameters);

            return rows.stream().map(TestCycleTime::mapToTestCycleTime).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("### getCycleTimeByStation {} {} {} {}", modelName, groupName, startDate, endDate, e);
            return Collections.emptyList();
        }
    }

}
