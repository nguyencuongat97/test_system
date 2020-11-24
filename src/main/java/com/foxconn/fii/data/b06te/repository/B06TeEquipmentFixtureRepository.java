package com.foxconn.fii.data.b06te.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class B06TeEquipmentFixtureRepository {

    @Autowired
    @Qualifier("b06teNamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    @Qualifier("b06teJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    public void testProcedure() {
        try {
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("test");
            SqlParameterSource input = new MapSqlParameterSource().addValue("INVAR", "test");
            Map<String, Object> output = simpleJdbcCall.execute(input);

            log.info(output.toString());
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.testProcedure error : ", e);
        }
    }

    public List<Map<String, Object>> ateMetaDataByStation() {
        try {
            Map<String, Object> parameters = new HashMap<>();

            return namedJdbcTemplate.queryForList("SELECT *\n" +
                            "FROM TE_ATE_METADATA\n" +
                            "ORDER BY STATION, LINE, ATE",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.ateListByStation error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> equipmentStatusSumary(double onlineLimit) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("onlineLimit", onlineLimit);

            return namedJdbcTemplate.queryForList("SELECT *\n" +
                            "FROM te_equipment_fixture_location t1\n" +
                            "RIGHT JOIN (\n" +
                            "  SELECT LOCATION_ID, \"ONLINE\", COUNT(*) as qty, OWNER_TYPE_ID\n" +
                            "  FROM (\n" +
                            "    SELECT LOCATION_ID, CASE WHEN (sysdate - latest_online_time)  <= :onlineLimit/24 THEN 1 ELSE 0 END as \"ONLINE\", OWNER_TYPE_ID\n" +
                            "    FROM TE_EQUIPMENT\n" +
                            "  )\n" +
                            "  GROUP BY LOCATION_ID, \"ONLINE\", OWNER_TYPE_ID\n" +
                            ") t2\n" +
                            "ON t1.\"ID\" = t2.LOCATION_ID",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentStatusSumary error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureStatusSumary(double onlineLimit) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("onlineLimit", onlineLimit);

            return namedJdbcTemplate.queryForList("SELECT *\n" +
                            "FROM te_equipment_fixture_location t1\n" +
                            "RIGHT JOIN (\n" +
                            "  SELECT LOCATION_ID, \"ONLINE\", COUNT(*) as qty, OWNER_TYPE_ID\n" +
                            "  FROM (\n" +
                            "    SELECT LOCATION_ID, CASE WHEN (sysdate - latest_online_time)  <= :onlineLimit/24 THEN 1 ELSE 0 END as \"ONLINE\", OWNER_TYPE_ID\n" +
                            "    FROM TE_FIXTURE\n" +
                            "  )\n" +
                            "  GROUP BY LOCATION_ID, \"ONLINE\", OWNER_TYPE_ID\n" +
                            ") t2\n" +
                            "ON t1.\"ID\" = t2.LOCATION_ID",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureStatusSumary error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> equipmentUseTimeCount(Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT t3.*, t3.EQUIPMENT_NAME as NAME, CASE WHEN tmpUseTime is null THEN 0 ELSE tmpUseTime END as useTime\n" +
                            "FROM (\n" +
                            "  SELECT *\n" +
                            "  FROM (\n" +
                            "    SELECT EQUIPMENT_ID, COUNT(*) as tmpUseTime\n" +
                            "    FROM TE_TEST_EQUIPMENT\n" +
                            "    WHERE DATE_TIME BETWEEN :startTime and :endTime\n" +
                            "    GROUP BY EQUIPMENT_ID\n" +
                            "  ) t1\n" +
                            "  RIGHT JOIN (\n" +
                            "    SELECT t4.*\n" +
                            "    FROM TE_EQUIPMENT t4\n" +
                            "    LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t5\n" +
                            "    ON t4.LOCATION_ID = t5.\"ID\"\n" +
                            "    WHERE t4.LOCATION_ID is null\n" +
                            "    OR t5.LOCATION_NAME = 'line'\n" +
                            "  ) t2\n" +
                            "  ON t1.equipment_id = t2.\"ID\"\n" +
                            ") t3\n" +
                            "  WHERE tmpUseTime > 0\n" +
                            "ORDER BY useTime DESC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentUseTimeCount error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> equipmentUsingTrend(long id, Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", id);
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT t2.start_time as startTime, t2.end_time as endTime, NVL(t3.qty, 0) as usedTime\n" +
                            "FROM TE_TEST_WORK_SECTION t2\n" +
                            "LEFT JOIN (\n" +
                            "  SELECT \n" +
                            "    count(*) as qty, CASE WHEN TO_NUMBER(TO_CHAR(t1.DATE_TIME, 'mi')) >= 30 THEN trunc(t1.date_time, 'hh24') + 0.5/24 ELSE trunc(t1.date_time, 'hh24') - 0.5/24 END as section\n" +
                            "  FROM TE_TEST_EQUIPMENT t1\n" +
                            "  WHERE EQUIPMENT_ID = :id\n" +
                            "  AND date_time between :startTime AND :endTime\n" +
                            "  GROUP BY CASE WHEN TO_NUMBER(TO_CHAR(t1.DATE_TIME, 'mi')) >= 30 THEN trunc(t1.date_time, 'hh24') + 0.5/24 ELSE trunc(t1.date_time, 'hh24') - 0.5/24 END\n" +
                            ") t3\n" +
                            "ON (t2.start_time = t3.section)\n" +
                            "WHERE t2.start_time between :startTime AND :endTime - 1/24\n" +
                            "ORDER BY startTime ASC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentUsingTrend error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureUseTimeCount(Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT t3.*, t3.FIXTURE_CODE as NAME, CASE WHEN tmpUseTime is null THEN 0 ELSE tmpUseTime END as useTime\n" +
                            "FROM (\n" +
                            "  SELECT *\n" +
                            "  FROM (\n" +
                            "    SELECT FIXTURE_CODE as FIXTURE_CODE_CLONE, COUNT(*) as tmpUseTime\n" +
                            "    FROM TE_TEST_DATA\n" +
                            "    WHERE START_TIME BETWEEN :startTime and :endTime\n" +
                            "    GROUP BY FIXTURE_CODE\n" +
                            "  ) t1\n" +
                            "  RIGHT JOIN (\n" +
                            "    SELECT t4.*\n" +
                            "    FROM TE_FIXTURE t4\n" +
                            "    LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t5\n" +
                            "    ON t4.LOCATION_ID = t5.\"ID\"\n" +
                            "    WHERE t4.LOCATION_ID is null\n" +
                            "    OR t5.LOCATION_NAME = 'line'\n" +
                            "  ) t2\n" +
                            "  ON t1.FIXTURE_CODE_CLONE = t2.fixture_code\n" +
                            ") t3\n" +
                            "  WHERE tmpUseTime > 0\n" +
                            "ORDER BY useTime DESC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureUseTimeCount error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureUsingTrend(String fixtureCode, Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fixtureCode", fixtureCode);
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT t1.start_time as startTime, t1.end_time as endTime, NVL(t2.qty, 0) as usedTime\n" +
                            "FROM TE_TEST_WORK_SECTION t1\n" +
                            "LEFT JOIN (\n" +
                            "  SELECT\n" +
                            "    COUNT(*) as qty, CASE WHEN TO_NUMBER(TO_CHAR(START_TIME, 'mi')) >= 30 THEN trunc(START_TIME, 'hh24') + 0.5/24 ELSE trunc(START_TIME, 'hh24') - 0.5/24 END as section\n" +
                            "  FROM TE_TEST_DATA\n" +
                            "  WHERE FIXTURE_CODE = :fixtureCode\n" +
                            "  AND START_TIME between :startTime AND :endTime\n" +
                            "  GROUP BY CASE WHEN TO_NUMBER(TO_CHAR(START_TIME, 'mi')) >= 30 THEN trunc(START_TIME, 'hh24') + 0.5/24 ELSE trunc(START_TIME, 'hh24') - 0.5/24 END\n" +
                            ") t2\n" +
                            "ON t1.start_time = t2.section\n" +
                            "WHERE t1.start_time between :startTime AND :endTime - 1/24\n" +
                            "ORDER BY startTime ASC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureUsingTrend error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> equipmentQuantityOnlineByType(double timeLimit) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("timeLimit", timeLimit);

            return namedJdbcTemplate.queryForList("SELECT \"TYPE\"\n" +
                            ", count(*) as qty\n" +
                            "FROM (\n" +
                            "  SELECT *\n" +
                            "  FROM TE_EQUIPMENT t1\n" +
                            "  LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t2\n" +
                            "  ON t1.location_id = t2.\"ID\"\n" +
                            "  WHERE t1.location_id is null\n" +
                            "  OR t2.LOCATION_NAME = 'line'\n" +
                            ")\n" +
                            "WHERE (sysdate - latest_online_time)  <= :timeLimit/24\n" +
                            "GROUP BY \"TYPE\"",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentQuantityOnlineByType error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureQuantityOnlineByType(double timeLimit) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("timeLimit", timeLimit);

            return namedJdbcTemplate.queryForList("SELECT \"TYPE\"\n" +
                            ", count(*) as qty\n" +
                            "FROM (\n" +
                            "  SELECT *\n" +
                            "  FROM TE_FIXTURE t1\n" +
                            "  LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t2\n" +
                            "  ON t1.location_id = t2.\"ID\"\n" +
                            "  WHERE t1.location_id is null\n" +
                            "  OR t2.LOCATION_NAME = 'line'\n" +
                            ")\n" +
                            "WHERE (sysdate - latest_online_time)  <= :timeLimit/24\n" +
                            "GROUP BY \"TYPE\"",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureQuantityOnlineByType error : ", e);
            return Collections.emptyList();
        }
    }

//    public List<Map<String, Object>> equipmentCountByOwner() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//
//            return namedJdbcTemplate.queryForList("SELECT OWNER_TYPE_ID, COUNT(*) as qty\n" +
//                            "FROM TE_EQUIPMENT\n" +
//                            "GROUP BY OWNER_TYPE_ID",
//                    parameters);
//        } catch (Exception e) {
//            log.info("B06TeEquipmentFixtureRepository.equipmentCountByOwner error : ", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> fixtureCountByOwner() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//
//            return namedJdbcTemplate.queryForList("SELECT OWNER_TYPE_ID, COUNT(*) as qty\n" +
//                            "FROM TE_FIXTURE\n" +
//                            "GROUP BY OWNER_TYPE_ID",
//                    parameters);
//        } catch (Exception e) {
//            log.info("B06TeEquipmentFixtureRepository.fixtureCountByOwner error : ", e);
//            return Collections.emptyList();
//        }
//    }

    public List<Map<String, Object>> equipmentUseHistoryBetween(Long equipmentId, Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("equipmentId", equipmentId);
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT EQUIPMENT_ID, start_time, end_time, T_ID\n" +
                            "FROM TE_TEST_EQUIPMENT\n" +
                            "WHERE start_time >= :startTime\n" +
                            "AND start_time <= :endTime\n" +
                            "AND EQUIPMENT_ID = :equipmentId\n" +
                            "ORDER BY start_time asc, end_time asc",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentUseHistoryBetween error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureUseHistoryBetween(String fixtureCode, Date startTime, Date endTime) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fixtureCode", fixtureCode);
            parameters.put("startTime", startTime);
            parameters.put("endTime", endTime);

            return namedJdbcTemplate.queryForList("SELECT FIXTURE_CODE, start_time, end_time, ATE\n" +
                            "FROM TE_TEST_DATA\n" +
                            "WHERE START_TIME >= :startTime\n" +
                            "AND START_TIME <= :endTime\n" +
                            "AND FIXTURE_CODE = :fixtureCode\n" +
                            "ORDER BY start_time asc, end_time asc",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureUseHistoryBetween error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> ownerEquipmentList() {
        try {
            Map<String, Object> parameters = new HashMap<>();

            return namedJdbcTemplate.queryForList("SELECT *\n" +
                            "FROM TE_EQUIPMENT t1\n" +
                            "LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t2\n" +
                            "ON t1.LOCATION_ID = t2.\"ID\"\n" +
                            "LEFT JOIN TE_EF_OWNER_TYPE t3\n" +
                            "ON t1.OWNER_TYPE_ID = t3.\"ID\"\n" +
                            "ORDER BY \"TYPE\" ASC, EQUIPMENT_NAME ASC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.ownerEquipmentList error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> ownerFixtureList() {
        try {
            Map<String, Object> parameters = new HashMap<>();

            return namedJdbcTemplate.queryForList("SELECT *\n" +
                            "FROM TE_FIXTURE t1\n" +
                            "LEFT JOIN TE_EQUIPMENT_FIXTURE_LOCATION t2\n" +
                            "ON t1.LOCATION_ID = t2.\"ID\"\n" +
                            "LEFT JOIN TE_EF_OWNER_TYPE t3\n" +
                            "ON t1.OWNER_TYPE_ID = t3.\"ID\"\n" +
                            "ORDER BY \"TYPE\" ASC, FIXTURE_CODE ASC",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.ownerFixtureList error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> equipmentOnlineBetween(Date startDate, Date endDate) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);

            return namedJdbcTemplate.queryForList("SELECT t2.*, t1.*\n" +
                            "FROM TE_EQUIPMENT t1\n" +
                            "LEFT JOIN (\n" +
                            "  SELECT EQUIPMENT_ID\n" +
                            "  FROM TE_TEST_EQUIPMENT\n" +
                            "  WHERE START_TIME BETWEEN :startDate AND :endDate\n" +
                            "  GROUP BY EQUIPMENT_ID\n" +
                            ") t2 ON t2.EQUIPMENT_ID = t1.\"ID\"",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.equipmentOnlineBetween error : ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> fixtureOnlineBetween(Date startDate, Date endDate) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);

            return namedJdbcTemplate.queryForList("SELECT t2.*, t1.*\n" +
                            "FROM TE_FIXTURE t1\n" +
                            "LEFT JOIN (\n" +
                            "  SELECT FIXTURE_CODE as CLONE_FIXTURE_CODE\n" +
                            "  FROM TE_TEST_DATA\n" +
                            "  WHERE START_TIME BETWEEN :startDate AND :endDate\n" +
                            "  GROUP BY FIXTURE_CODE\n" +
                            ") t2 ON t2.CLONE_FIXTURE_CODE = t1.FIXTURE_CODE",
                    parameters);
        } catch (Exception e) {
            log.info("B06TeEquipmentFixtureRepository.fixtureOnlineBetween error : ", e);
            return Collections.emptyList();
        }
    }

}
