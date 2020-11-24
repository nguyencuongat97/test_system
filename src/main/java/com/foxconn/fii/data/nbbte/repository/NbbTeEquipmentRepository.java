//package com.foxconn.fii.data.nbbte.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Deprecated
//@Slf4j
//@Component
//public class NbbTeEquipmentRepository {
//
//    @Autowired
//    @Qualifier("nbbteNamedJdbcTemplate")
//    private NamedParameterJdbcTemplate namedJdbcTemplate;
//
//    public List<Map<String, Object>> stationLastConnectedTime(String tableName) {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, END_TIME\n" +
//                            "FROM\n" +
//                            "  (\n" +
//                            "    SELECT t.STATION_ID, t.END_TIME, rank() OVER (PARTITION BY t.STATION_ID ORDER BY t.END_TIME DESC) as rnk\n" +
//                            "    FROM "+tableName+" t\n" +
//                            "  )\n" +
//                            "WHERE rnk = 1\n" +
//                            "ORDER BY END_TIME DESC",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't get station latest connected time from TENBB."+tableName+" ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countSmaMlbRfUsedTime() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                        "  FROM M71_TEST_MB_RF\n" +
//                        "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count SMA_MLB_RF equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countFatpQuickTestUsedTime() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                            "  FROM M71_TEST_QT\n" +
//                            "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count FATP_QuickTest equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countFatpRunInCheckOutTime() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                            "  FROM M71_TEST_RI_CO\n" +
//                            "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count FATP_Run_In_Check_Out equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countFatpRfOtaTime() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                            "  FROM M71_TEST_RF_OTA\n" +
//                            "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count FATP_RF_OTA equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countPackingProvi() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                            "  FROM M71_TEST_PROVI\n" +
//                            "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count PACKING_PROVI equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countPackingFinal() {
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            return namedJdbcTemplate.queryForList("SELECT STATION_ID, COUNT(*) as usedTime, SUM(ELAPSED_TIME) as elapsedTime, TRUNC(START_TIME, 'J') as workDate\n" +
//                            "  FROM M71_TEST_FINAL\n" +
//                            "  GROUP BY STATION_ID, TRUNC(START_TIME, 'J')",
//                    parameters);
//        } catch (Exception e) {
//            log.info("### can't count PACKING_PROVI equipment used times ###", e);
//            return Collections.emptyList();
//        }
//    }
//}
