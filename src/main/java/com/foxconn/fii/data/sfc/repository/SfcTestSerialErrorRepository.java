package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SfcTestSerialErrorRepository {

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
    ///////////////////////////////////
    public List<Map<String, Object>> countNtfByModelNameAndBeetwens(String factory, String customer, String modelName,String testGroup, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("testGroup", testGroup);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT TO_DATE(test_time) as day,  SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END) as error_NTF, COUNT(*) as error, ROUND((SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END)*100/COUNT(*)),2) as Phan_Tram_NTF\n" +
                            "FROM  \"SFISM4\".\"R_REPAIR_T\"\n" +
                            "WHERE MODEL_NAME = :modelName  \n" +
                            "and test_group = :testGroup \n" +
                            "and test_time  BETWEEN :current AND :next\n" +
                            "GROUP BY TO_DATE(test_time)\n" +
                            "ORDER BY TO_DATE(test_time) ASC",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfByModelNameAndBeetwens", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getTestGroupBeetwens(String factory, String customer, String modelName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT TEST_GROUP FROM \"SFISM4\".\"R_REPAIR_T\" \n" +
                            "Where test_time  BETWEEN :current AND :next \n" +
                            "AND MODEL_NAME = :modelName \n" +
                            "GROUP BY TEST_GROUP",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfByModelNameAndBeetwens", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getNtfDayBeetwens(String factory, String customer, String modelName, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT TO_DATE(test_time) as Day FROM \"SFISM4\".\"R_REPAIR_T\" \n" +
                            "Where test_time  BETWEEN :current AND :next\n" +
                            "AND MODEL_NAME = :modelName \n" +
                            "GROUP BY TO_DATE(test_time)\n" +
                            "ORDER BY TO_DATE(test_time) ASC",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfByModelNameAndBeetwens", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countNtfErrorCode(String factory, String customer, String modelName, String testGroup) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("testGroup", testGroup);
            return namedJdbcTemplate.queryForList("SELECT TEST_CODE,SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END) as error_NTF, COUNT(*) as error,ROUND((SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END)*100/COUNT(*)),2) as Phan_Tram_NTF FROM  \"SFISM4\".\"R_REPAIR_T\" " +
                            "where MODEL_NAME = :modelName \n" +
                            "and test_group = :testGroup \n" +
                            "Group by  TEST_CODE",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfErrorCode", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countNtfStation(String factory, String customer, String modelName, String testGroup) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("testGroup", testGroup);
            return namedJdbcTemplate.queryForList("SELECT TEST_STATION,SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END) as error_NTF, COUNT(*) as error,ROUND((SUM(CASE WHEN REASON_CODE = 'H003' THEN 1 ELSE 0 END)*100/COUNT(*)),2) as Phan_Tram_NTF FROM  \"SFISM4\".\"R_REPAIR_T\" \n" +
                            "WHERE MODEL_NAME= :modelName\n" +
                            "and test_group = :testGroup\n" +
                            "Group by  TEST_STATION",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfStation", e);
            return Collections.emptyList();
        }
    }


    public List<Map<String, Object>> getMachineOfErrorCode(String factory, String customer, String modelName, String testGroup , String testCode) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("testGroup", testGroup);
            parameters.put("testCode", testCode);
            return namedJdbcTemplate.queryForList("SELECT TEST_STATION, COUNT(*) as QTY FROM  \"SFISM4\".\"R_REPAIR_T\" \n" +
                            "WHERE REASON_CODE\t= 'H003'\n" +
                            "and MODEL_NAME\t\t= :modelName \n" +
                            "and test_group = :testGroup  \n" +
                            "and TEST_CODE = :testCode\n" +
                            "Group by  TEST_STATION",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfStation", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getErrorCodeOfMachine(String factory, String customer, String modelName, String testGroup , String testStation) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("testGroup", testGroup);
            parameters.put("testStation", testStation);
            return namedJdbcTemplate.queryForList("SELECT TEST_CODE, COUNT(*) as QTY  FROM  \"SFISM4\".\"R_REPAIR_T\" \n" +
                            "WHERE ROWNUM<=500 \n" +
                            "and REASON_CODE ='H003' \n" +
                            "and MODEL_NAME  = :modelName \n" +
                            "and TEST_STATION= :testStation\n" +
                            "Group by  TEST_CODE",
                    parameters);
        } catch (Exception e) {
            log.error("### countNtfStation", e);
            return Collections.emptyList();
        }
    }

//    public List<Map<String, Object>> countNtfErrorCode(String factory, String customer, String modelName,String reasonCode, String testGroup,  Date getDay) {
//        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
//        if (namedJdbcTemplate == null) {
//            return Collections.emptyList();
//        }
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("modelName", modelName);
//            parameters.put("reasonCode", reasonCode);
//            parameters.put("testGroup", testGroup);
//            parameters.put("Day", getDay);
//            return namedJdbcTemplate.queryForList("SELECT TEST_CODE, COUNT(*) FROM  \"SFISM4\".\"R_REPAIR_T\" \n" +
//                            "WHERE REASON_CODE = :reasonCode \n" +
//                            "and MODEL_NAME = :modelName \n" +
//                            "and test_group = :testGroup \n" +
//                            "and TO_DATE(test_time) = :Day \n" +
//                            "Group by  TEST_CODE",
//                    parameters);
//        } catch (Exception e) {
//            log.error("### countNtfErrorCode", e);
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Map<String, Object>> countNtfStation(String factory, String customer, String modelName,String reasonCode, String testGroup,  Date getDay) {
//        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
//        if (namedJdbcTemplate == null) {
//            return Collections.emptyList();
//        }
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("modelName", modelName);
//            parameters.put("reasonCode", reasonCode);
//            parameters.put("testGroup", testGroup);
//            parameters.put("Day", getDay);
//            return namedJdbcTemplate.queryForList("SELECT TEST_STATION, COUNT(*) FROM  \"SFISM4\".\"R_REPAIR_T\" \n" +
//                            "WHERE REASON_CODE = :reasonCode \n" +
//                            "and MODEL_NAME = :modelName \n" +
//                            "and test_group = :testGroup \n" +
//                            "and TO_DATE(test_time) = :Day \n" +
//                            "Group by  TEST_STATION",
//                    parameters);
//        } catch (Exception e) {
//            log.error("### countNtfErrorCode", e);
//            return Collections.emptyList();
//        }
//    }
    ///////////////////////////
    public List<Map<String, Object>> countReasonByModelNameAndErrorCode(String factory, String customer, String modelName, String errorCode, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("errorCode", errorCode);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT reason_code, COUNT(*) count " +
                            "FROM SFISM4.R_REPAIR_T " +
                            "WHERE model_name = :modelName AND test_code = :errorCode AND test_section = 'TEST' " +
                            "AND test_time BETWEEN :current AND :next " +
                            "GROUP BY reason_code",
                    parameters);
        } catch (Exception e) {
            log.error("### countReasonByModelNameAndErrorCode", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countStationByModelNameAndErrorCodeAndReason(String factory, String customer, String modelName, String errorCode, String reasonCode, Date startDate, Date endDate) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("modelName", modelName);
            parameters.put("errorCode", errorCode);
            parameters.put("reasonCode", reasonCode);
            parameters.put("current", startDate);
            parameters.put("next", endDate);

            return namedJdbcTemplate.queryForList("SELECT test_group, test_station, COUNT(*) count " +
                            "FROM SFISM4.R_REPAIR_T " +
                            "WHERE model_name = :modelName AND test_code = :errorCode AND reason_code = :reasonCode AND test_section = 'TEST' " +
                            "AND test_time BETWEEN :current AND :next " +
                            "GROUP BY test_group, test_station",
                    parameters);
        } catch (Exception e) {
            log.error("### countStationByModelNameAndErrorCodeAndReason", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getListInputDailyRe1(String factory, String customer, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("SELECT model_name, count(*) as qty " +
                    "from SFISM4.R_REPAIR_IN_OUT_T rrt\n" +
                    "INNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                    "where IN_DATETIME BETWEEN :startDate AND :endDate AND model_name IS NOT NULL " +
                    "group by model_name ", map);
        }catch (Exception e){
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListInputDailyRe(String factory, String customer, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory)){
                return namedJdbcTemplate.queryForList("SELECT rrt.SERIAL_NUMBER " +
                        ",rwt.model_name " +
                        ",rwt.WIP_GROUP " +
                        "\t\t,rrt.LINE_NAME " +
                        "\t\t,rrt.STATION_NAME\n" +
                        "\t\t,rrt.MO_NUMBER\n" +
                        "\t\t,rrt.P_SENDER\n" +
                        "\t\t,rrt.R_RECEIVER\n" +
                        "\t\t,rrt.REMARK\n" +
                        "\t\t,rrt.IN_DATETIME\n" +
                        "\t\t,rrt.R_SENDER\n" +
                        "\t\t,rrt.P_RECEIVER\n" +
                        "\t\t,rrt.OUT_DATETIME\n" +
                        "\t\t,rrt.FAIL_LOCATION\n" +
                        "\t\t,rrt.FAIL_ACTION\n" +
                        "\t\t,rrt.REPAIRER\n" +
                        "\t\t,rrt.STATUS\n" +
                        "\t\t,rrt.B_TYPE\n" +
                        "\t\t,rrt.RE_TYPE\n" +
                        "\tFROM SFISM4.R_REPAIR_IN_OUT_T  rrt\n" +
                        "\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\tWHERE rrt.IN_DATETIME BETWEEN :startDate and :endDate and rwt.model_name IN (:listModel) ", map);
            }else {
                return namedJdbcTemplate.queryForList("SELECT to_char(tb.t1, 'dd/mm/yyyy') as timer, tb.model_name, COUNT(tb.serial_number) as total\n" +
                        "\tFROM (\n" +
                        "\t\tSELECT rwt.serial_number, rwt.model_name, (CASE WHEN to_char(IN_DATETIME, 'hh24:mi:ss') < '07:30:00' THEN IN_DATETIME - 1 ELSE IN_DATETIME END) AS t1\n" +
                        "\t\tfrom SFISM4.R_REPAIR_IN_OUT_T rrt\n" +
                        "\t\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\t\twhere rrt.IN_DATETIME between :startDate and :endDate AND rwt.model_name IN (:listModel)\n" +
                        "\t) tb\n" +
                        "\tGROUP BY to_char(tb.t1, 'dd/mm/yyyy'), tb.model_name " +
                        " ORDER BY to_date(timer, 'dd/mm/yyyy') ", map);
            }

        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListOutputDailyRe(String factory, String customer, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            if (Factory.B04.equalsIgnoreCase(factory) || Factory.B06.equalsIgnoreCase(factory)){
                return namedJdbcTemplate.queryForList("SELECT rrt.SERIAL_NUMBER " +
                        ",rwt.model_name " +
                        ",rwt.WIP_GROUP " +
                        "\t\t,rrt.LINE_NAME\n" +
                        "\t\t,rrt.STATION_NAME\n" +
                        "\t\t,rrt.MO_NUMBER\n" +
                        "\t\t,rrt.P_SENDER\n" +
                        "\t\t,rrt.R_RECEIVER\n" +
                        "\t\t,rrt.REMARK\n" +
                        "\t\t,rrt.IN_DATETIME\n" +
                        "\t\t,rrt.R_SENDER\n" +
                        "\t\t,rrt.P_RECEIVER\n" +
                        "\t\t,rrt.OUT_DATETIME\n" +
                        "\t\t,rrt.FAIL_LOCATION\n" +
                        "\t\t,rrt.FAIL_ACTION\n" +
                        "\t\t,rrt.REPAIRER\n" +
                        "\t\t,rrt.STATUS\n" +
                        "\t\t,rrt.B_TYPE\n" +
                        "\t\t,rrt.RE_TYPE\n" +
                        "\tFROM SFISM4.R_REPAIR_IN_OUT_T  rrt\n" +
                        "\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\tWHERE rrt.OUT_DATETIME BETWEEN :startDate and :endDate and rwt.model_name IN (:listModel) ", map);
            }else {
                return namedJdbcTemplate.queryForList("SELECT to_char(tb.t1, 'dd/mm/yyyy') as timer, tb.model_name, COUNT(tb.serial_number) as total\n" +
                        "\tFROM (\n" +
                        "\t\tSELECT rwt.serial_number, rwt.model_name, (CASE WHEN to_char(OUT_DATETIME, 'hh24:mi:ss') < '07:30:00' THEN OUT_DATETIME - 1 ELSE OUT_DATETIME END) AS t1\n" +
                        "\t\tfrom SFISM4.R_REPAIR_IN_OUT_T rrt\n" +
                        "\t\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\t\twhere rrt.OUT_DATETIME between :startDate and :endDate AND rwt.model_name IN (:listModel)\n" +
                        "\t) tb\n" +
                        "\tGROUP BY to_char(tb.t1, 'dd/mm/yyyy'), tb.model_name " +
                        " ORDER BY to_date(timer, 'dd/mm/yyyy') ", map);
            }

        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getCheckInCheckOutReport(String factory, String customer, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", df.format(startDate));
            map.put("endDate", df.format(endDate));
            map.put("listModel", listModel);
            if (factory.equalsIgnoreCase(Factory.B04)){
                return namedJdbcTemplate.queryForList("select  REPORT_DATE, sum(CHECKIN_QTY) as QTY_IN, sum(CHECKOUT_QTY) as QTY_OUT " +
                        "\t FROM SFISM4.R_REPAIR_DAILYREPORT " +
                        " WHERE to_date(REPORT_DATE, 'yyyy-mm-dd') BETWEEN TO_DATE(:startDate, 'yyyy-mm-dd') AND TO_DATE(:endDate, 'yyyy-mm-dd') AND  p_no in (:listModel) " +
                        "\tGROUP BY  REPORT_DATE " +
                        "\torder by REPORT_DATE asc", map);
            }else{
                return namedJdbcTemplate.queryForList("select  REPORT_DATE, sum(CHECKIN_QTY) as QTY_IN, sum(CHECKOUT_QTY) as QTY_OUT " +
                        "\t FROM SFISM4.R_REPAIR_DAILYREPORT " +
                        " WHERE REPORT_DATE BETWEEN:startDate AND :endDate AND p_no in (:listModel) " +
                        "\tGROUP BY  REPORT_DATE " +
                        "\torder by REPORT_DATE asc", map);
            }

        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>>
    getCheckInCheckOutReportWeekly(String factory, String customer, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", df.format(startDate));
            map.put("endDate", df.format(endDate));
            map.put("listModel", listModel);
            if (factory.equalsIgnoreCase(Factory.B04)){
                return namedJdbcTemplate.queryForList("select  sum(CHECKIN_QTY) as QTY_IN, sum(CHECKOUT_QTY) as QTY_OUT " +
                        "\t FROM SFISM4.R_REPAIR_DAILYREPORT " +
                        " WHERE to_date(REPORT_DATE, 'yyyy-mm-dd') BETWEEN TO_DATE(:startDate, 'yyyy-mm-dd') AND TO_DATE(:endDate, 'yyyy-mm-dd') AND  p_no in (:listModel) " +
                        "\torder by REPORT_DATE asc", map);
            }else {
                return namedJdbcTemplate.queryForList("select  sum(CHECKIN_QTY) as QTY_IN, sum(CHECKOUT_QTY) as QTY_OUT " +
                        "\t FROM SFISM4.R_REPAIR_DAILYREPORT " +
                        " WHERE REPORT_DATE BETWEEN :startDate AND :endDate AND  p_no in (:listModel) " +
                        "\torder by REPORT_DATE asc", map);
            }

        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListErrorCodeByModelName(String factory, String customer, String modelName, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("modelName", modelName);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("select  test_group, test_code, to_char(test_time, 'yyyy/mm/dd') as timer, count(*) as qty \t\n" +
                    "\tFROM SFISM4.R_REPAIR_T " +
                    " WHERE model_name = :modelName and test_time BETWEEN :startDate and :endDate\n" +
                    "\tGROUP BY  test_group, test_code, to_char(test_time, 'yyyy/mm/dd')" +
                    "\torder by qty desc ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListReasonCodeByModelName(String factory, String customer, String modelName, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("modelName", modelName);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("select  test_group, reason_code, to_char(REPAIR_TIME, 'yyyy/mm/dd') as timer, count(*) as qty \t\n" +
                    "\tFROM SFISM4.R_REPAIR_T " +
                    " WHERE model_name = :modelName and REPAIR_TIME BETWEEN :startDate and :endDate\n" +
                    "\tGROUP BY  test_group, reason_code, to_char(REPAIR_TIME, 'yyyy/mm/dd') " +
                    "\torder by qty desc", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getCheckoutByDayReportDaily(String factory, Date startDate, List<String> listModel){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getJdbcTemplate(factory, "");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (namedParameterJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", df.format(startDate));
            map.put("listModel", listModel);
            return namedParameterJdbcTemplate.queryForList("select REPORT_DATE,p_no,CHECKOUT_QTY " +
                    "FROM SFISM4.R_REPAIR_DAILYREPORT " +
                    "where REPORT_DATE = :startDate and p_no in (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getCheckoutByDay(String factory, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedParameterJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedParameterJdbcTemplate.queryForList("SELECT rt.MODEL_NAME\n" +
                    ",COUNT(ROWNUM) as qty" +
                    "\tFROM SFISM4.R_REPAIR_IN_OUT_T rit\n" +
                    "\tINNER JOIN SFISM4.R_REPAIR_T rt ON rt.SERIAL_NUMBER =  rit.SERIAL_NUMBER\n" +
                    "\tWHERE  rit.OUT_DATETIME between :startDate and :endDate and rt.model_name IN (:listModel) " +
                    "GROUP BY rt.MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getReasonCodeByDay(String factory, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedParameterJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedParameterJdbcTemplate.queryForList("SELECT rt.REASON_CODE\n" +
                    ",COUNT(ROWNUM) as qty" +
                    "\tFROM SFISM4.R_REPAIR_IN_OUT_T rit\n" +
                    "\tINNER JOIN SFISM4.R_REPAIR_T rt ON rt.SERIAL_NUMBER =  rit.SERIAL_NUMBER\n" +
                    "\tWHERE rt.REASON_CODE IS NOT NULL AND rit.OUT_DATETIME between :startDate and :endDate and rt.model_name IN (:listModel) " +
                    "GROUP BY rt.REASON_CODE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListInputDailyReBySection(String factory, String customer, Date startDate, Date endDate, List<String> listModel, String section){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            map.put("section", section);
            if (factory.equalsIgnoreCase("B04")){
                return namedJdbcTemplate.queryForList("SELECT rrt.SERIAL_NUMBER " +
                        ",rwt.model_name " +
                        ",rwt.WIP_GROUP " +
                        ",rt.TEST_SECTION " +
                        "\t\t,rrt.LINE_NAME " +
                        "\t\t,rrt.STATION_NAME\n" +
                        "\t\t,rrt.MO_NUMBER\n" +
                        "\t\t,rrt.P_SENDER\n" +
                        "\t\t,rrt.R_RECEIVER\n" +
                        "\t\t,rrt.REMARK\n" +
                        "\t\t,rrt.IN_DATETIME\n" +
                        "\t\t,rrt.R_SENDER\n" +
                        "\t\t,rrt.P_RECEIVER\n" +
                        "\t\t,rrt.OUT_DATETIME\n" +
                        "\t\t,rrt.FAIL_LOCATION\n" +
                        "\t\t,rrt.FAIL_ACTION\n" +
                        "\t\t,rrt.REPAIRER\n" +
                        "\t\t,rrt.STATUS\n" +
                        "\t\t,rrt.B_TYPE\n" +
                        "\t\t,rrt.RE_TYPE\n" +
                        "\tFROM SFISM4.R_REPAIR_IN_OUT_T  rrt\n" +
                        "\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\tINNER JOIN SFISM4.R_REPAIR_T rt ON rt.serial_number = rrt.serial_number\n" +
                        "\tWHERE rrt.IN_DATETIME BETWEEN :startDate and :endDate and rwt.model_name IN (:listModel) AND rt.TEST_SECTION = :section ", map);
            }else {
                return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER\n" +
                        "\t\t,MO_NUMBER\n" +
                        "\t\t,MODEL_NAME\n" +
                        "\t\t,TEST_TIME\n" +
                        "\t\t,TEST_CODE\n" +
                        "\t\t,TEST_STATION\n" +
                        "\t\t,TEST_GROUP\n" +
                        "\t\t,TEST_SECTION\n" +
                        "\t\t,TEST_LINE\n" +
                        "\t\t,TESTER\n" +
                        "\t\t,REPAIRER\n" +
                        "\t\t,REPAIR_TIME\n" +
                        "\t\t,REASON_CODE\n" +
                        "\tFROM SFISM4.R_REPAIR_T WHERE test_time BETWEEN :startDate and :endDate AND model_name IN (:listModel) AND TEST_SECTION = :section ", map);
            }


        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getListOutputDailyReBySection(String factory, String customer, Date startDate, Date endDate, List<String> listModel, String section){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            map.put("section", section);
            if (factory.equalsIgnoreCase("B04")){
                return namedJdbcTemplate.queryForList("SELECT rrt.SERIAL_NUMBER " +
                        ",rwt.model_name " +
                        ",rwt.WIP_GROUP " +
                        ",rt.TEST_SECTION " +
                        "\t\t,rrt.LINE_NAME\n" +
                        "\t\t,rrt.STATION_NAME\n" +
                        "\t\t,rrt.MO_NUMBER\n" +
                        "\t\t,rrt.P_SENDER\n" +
                        "\t\t,rrt.R_RECEIVER\n" +
                        "\t\t,rrt.REMARK\n" +
                        "\t\t,rrt.IN_DATETIME\n" +
                        "\t\t,rrt.R_SENDER\n" +
                        "\t\t,rrt.P_RECEIVER\n" +
                        "\t\t,rrt.OUT_DATETIME\n" +
                        "\t\t,rrt.FAIL_LOCATION\n" +
                        "\t\t,rrt.FAIL_ACTION\n" +
                        "\t\t,rrt.REPAIRER\n" +
                        "\t\t,rrt.STATUS\n" +
                        "\t\t,rrt.B_TYPE\n" +
                        "\t\t,rrt.RE_TYPE\n" +
                        "\tFROM SFISM4.R_REPAIR_IN_OUT_T  rrt\n" +
                        "\tINNER JOIN SFISM4.R_WIP_TRACKING_T rwt ON rwt.serial_number = rrt.serial_number\n" +
                        "\tINNER JOIN SFISM4.R_REPAIR_T rt ON rt.serial_number = rrt.serial_number\n" +
                        "\tWHERE rrt.OUT_DATETIME BETWEEN :startDate and :endDate and rwt.model_name IN (:listModel) AND rt.TEST_SECTION = :section ", map);
            }else {
                return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER\n" +
                        "\t\t,MO_NUMBER\n" +
                        "\t\t,MODEL_NAME\n" +
                        "\t\t,TEST_TIME\n" +
                        "\t\t,TEST_CODE\n" +
                        "\t\t,TEST_STATION\n" +
                        "\t\t,TEST_GROUP\n" +
                        "\t\t,TEST_SECTION\n" +
                        "\t\t,TEST_LINE\n" +
                        "\t\t,TESTER\n" +
                        "\t\t,REPAIRER\n" +
                        "\t\t,REPAIR_TIME\n" +
                        "\t\t,REASON_CODE\n" +
                        "\tFROM SFISM4.R_REPAIR_T WHERE REPAIR_TIME BETWEEN :startDate and :endDate AND model_name IN (:listModel) AND TEST_SECTION = :section ", map);
            }


        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getDataBySectionCheckInAndCheckOutWeekly(String factory, String customer, Date startDate, Date endDate, String snType, List<String> listModel){
        String testSection = "SI";
        if (factory.equalsIgnoreCase(Factory.B06)){
            testSection = "TEST";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", df.format(startDate));
            map.put("endDate", df.format(endDate));
            map.put("listModel", listModel);
            map.put("snType", snType);
            map.put("testSection", testSection);
            return namedJdbcTemplate.queryForList("SELECT  count(sn) as QTY\n" +
                    "\tFROM (SELECT rds.REPORT_DATE\n" +
                    "\t\t,rds.SN\n" +
                    "\t\t,rds.P_NO\n" +
                    "\t\t,wtt.TEST_SECTION\n" +
                    "\tFROM SFISM4.R_REPAIR_DAILYREPORT_SN rds\n" +
                    "\tleft join SFISM4.R_REPAIR_T wtt ON rds.sn = wtt.serial_number\n" +
                    "\tWHERE to_date(rds.REPORT_DATE, 'yyyy-mm-dd') BETWEEN  TO_DATE(:startDate, 'yyyy-mm-dd') AND TO_DATE(:endDate, 'yyyy-mm-dd') " +
                    "and wtt.TEST_SECTION = :testSection and rds.sn_type = :snType and rds.p_no in (:listModel) " +
                    "group by rds.sn, rds.P_NO, wtt.TEST_SECTION, rds.REPORT_DATE\n" +
                    "\torder by rds.REPORT_DATE asc\n" +
                    "\t) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getDataBySectionCheckInAndCheckOut(String factory, String customer, Date startDate, Date endDate, String snType, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        String testSection = "SI";
        if (factory.equalsIgnoreCase(Factory.B06)){
            testSection = "TEST";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", df.format(startDate));
            map.put("endDate", df.format(endDate));
            map.put("listModel", listModel);
            map.put("snType", snType);
            map.put("testSection", testSection);
            return namedJdbcTemplate.queryForList("SELECT REPORT_DATE, count(sn) as QTY\n" +
                    "\tFROM (SELECT rds.REPORT_DATE\n" +
                    "\t\t,rds.SN\n" +
                    "\t\t,rds.P_NO\n" +
                    "\t\t,wtt.TEST_SECTION\n" +
                    "\tFROM SFISM4.R_REPAIR_DAILYREPORT_SN rds\n" +
                    "\tleft join SFISM4.R_REPAIR_T wtt ON rds.sn = wtt.serial_number\n" +
                    "\tWHERE to_date(rds.REPORT_DATE, 'yyyy-mm-dd') BETWEEN  TO_DATE(:startDate, 'yyyy-mm-dd') AND TO_DATE(:endDate, 'yyyy-mm-dd') " +
                    "and wtt.TEST_SECTION = :testSection AND rds.sn_type = :snType and rds.p_no in (:listModel) " +
                    "group by rds.sn, rds.P_NO, wtt.TEST_SECTION, rds.REPORT_DATE\n" +
                    "\torder by rds.REPORT_DATE asc\n" +
                    "\t)\n" +
                    "\tgroup by REPORT_DATE\n" +
                    "\torder by REPORT_DATE asc", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getBalancaRe(String factory, Date startDate, Date endDate, List<String> listModel){ // B04
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT rit.SERIAL_NUMBER, " +
                    "rt.MODEL_NAME,\n" +
                    "rt.MO_NUMBER\n" +
                    ",MAX(rt.\"TEST_TIME\") as test_time\n" +
                    "FROM SFISM4.R_REPAIR_IN_OUT_T  rit\n" +
                    "INNER JOIN  SFISM4.R_REPAIR_T  rt ON rt.SERIAL_NUMBER =  rit.SERIAL_NUMBER\n" +
                    "WHERE rit.IN_DATETIME between :startDate and :endDate and rt.REPAIR_TIME IS NULL " +
                    " and rt.model_name IN (:listModel) " +
                    "GROUP BY rit.SERIAL_NUMBER, rt.MODEL_NAME, rt.MO_NUMBER ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getOverTime8h(String factory, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,MODEL_NAME\n" +
                    ",MAX(TEST_TIME) as test_time\n" +
                    "\tFROM SFISM4.R_REPAIR_T " +
                    "WHERE TEST_TIME BETWEEN :startDate and :endDate and REPAIR_TIME IS NULL AND MODEL_NAME IN (:listModel) " +
                    "GROUP BY SERIAL_NUMBER, MO_NUMBER, MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> qtyReportBalanceOver8h(String factory, String reportDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("reportDate", reportDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT \"REPORT_DATE\"\n" +
                    "\t\t,\"BU\"\n" +
                    "\t\t,\"SERIES\"\n" +
                    "\t\t,\"P_NO\"\n" +
                    "\t\t,\"BALANCE_QTY\"\n" +
                    "\t\t,\"CHECKIN_QTY\"\n" +
                    "\t\t,\"CHECKOUT_QTY\"\n" +
                    "\t\t,\"OVERTIME4H_QTY\"\n" +
                    "\t\t,\"OVERTIME8H_QTY\"\n" +
                    "\t\t,\"SEND_FLAG\"\n" +
                    "\t\t,\"SEND_DATE\"\n" +
                    "\t\t,\"LASTEDITBY\"\n" +
                    "\t\t,\"LASTEDITDT\"\n" +
                    "\t\t,\"LAST_BALANCE_QTY\"\n" +
                    "\tFROM \"SFISM4\".\"R_REPAIR_DAILYREPORT\" WHERE\n" +
                    "\tREPORT_DATE = :reportDate AND P_NO IN (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countReport(String factory, String parameter, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("parameter", parameter);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
//            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("select  REPORT_DATE, sum("+parameter+") as qty\n" +
                    "\tfrom SFISM4.R_REPAIR_DAILYREPORT\n" +
                    "\twhere LASTEDITDT BETWEEN :startDate AND :endDate " +
                    "AND P_NO IN (:listModel) \n" +
                    "\tgroup by  REPORT_DATE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getCheckOut(String factory, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
//            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT \"tmp1\".\"SERIAL_NUMBER\"\n" +
                    "\t,\"tmp1\".\"LINE_NAME\"\n" +
                    "\t,\"tmp1\".\"STATION_NAME\"\n" +
                    "\t,\"tmp1\".\"MO_NUMBER\"\n" +
                    "\t,\"tmp1\".\"IN_DATETIME\"\n" +
                    "\t,\"tmp1\".\"OUT_DATETIME\"\n" +
                    "\t,\"tmp1\".\"FAIL_LOCATION\"\n" +
                    "\t,\"tmp1\".\"REPAIRER\"\n" +
                    "\t,\"tmp1\".\"out_time\"\n" +
                    "\t,\"tmp2\".\"MODEL_NAME\"\n" +
                    "\t,\"tmp2\".\"REASON_CODE\"\n" +
                    "\t,\"tmp2\".\"TEST_CODE\"\n" +
                    "\t,\"tmp2\".\"LOCATION_CODE\"\n" +
                    "FROM (\n" +
                    "\tSELECT SERIAL_NUMBER\n" +
                    "\t\t,LINE_NAME\n" +
                    "\t\t,STATION_NAME\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,IN_DATETIME\n" +
                    "\t\t,OUT_DATETIME\n" +
                    "\t\t,FAIL_LOCATION\n" +
                    "\t\t,REPAIRER\n" +
                    "\t\t,max(OUT_DATETIME) as \"out_time\"\n" +
                    "\tFROM SFISM4.R_REPAIR_IN_OUT_T \n" +
                    "\tWHERE OUT_DATETIME BETWEEN :startDate AND :endDate \n" +
                    "\tGROUP BY SERIAL_NUMBER\n" +
                    "\t\t,LINE_NAME\n" +
                    "\t\t,STATION_NAME\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,IN_DATETIME\n" +
                    "\t\t,OUT_DATETIME\n" +
                    "\t\t,FAIL_LOCATION\n" +
                    "\t\t,REPAIRER\n" +
                    ") \"tmp1\"\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT SERIAL_NUMBER\n" +
                    "\t\t,\"MODEL_NAME\"\n" +
                    "\t\t,\"TEST_TIME\"\n" +
                    "\t\t,\"TEST_CODE\"\n" +
                    "\t\t,\"TEST_GROUP\"\n" +
                    "\t\t,\"TEST_SECTION\"\n" +
                    "\t\t,\"REPAIRER\"\n" +
                    "\t\t,\"REPAIR_TIME\"\n" +
                    "\t\t,\"REASON_CODE\"\n" +
                    "\t\t,\"REPAIR_STATION\"\n" +
                    "\t\t,\"REPAIR_GROUP\"\n" +
                    "\t\t,\"REPAIR_SECTION\"\n" +
                    "\t\t,\"ERROR_ITEM_CODE\"\n" +
                    "\t\t,\"LOCATION_CODE\"\n" +
                    "\t\t,MAX(\"TEST_TIME\") AS \"test_time\"\n" +
                    "\tFROM SFISM4.R_REPAIR_T WHERE repair_time  BETWEEN :startDate AND :endDate\n" +
                    "\tGROUP BY SERIAL_NUMBER\n" +
                    "\t\t,\"MODEL_NAME\"\n" +
                    "\t\t,\"TEST_TIME\"\n" +
                    "\t\t,\"TEST_CODE\"\n" +
                    "\t\t,\"TEST_GROUP\"\n" +
                    "\t\t,\"TEST_SECTION\"\n" +
                    "\t\t,\"REPAIRER\"\n" +
                    "\t\t,\"REPAIR_TIME\"\n" +
                    "\t\t,\"REASON_CODE\"\n" +
                    "\t\t,\"REPAIR_STATION\"\n" +
                    "\t\t,\"REPAIR_GROUP\"\n" +
                    "\t\t,\"REPAIR_SECTION\"\n" +
                    "\t\t,\"ERROR_ITEM_CODE\"\n" +
                    "\t\t,\"LOCATION_CODE\"\n" +
                    "\t) \"tmp2\" ON \"tmp1\".\"SERIAL_NUMBER\" = \"tmp2\".\"SERIAL_NUMBER\"", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }


    public List<Map<String, Object>> getCheckIn(String factory, Date startDate, Date endDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
//            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT \"tmp1\".\"SERIAL_NUMBER\"\n" +
                    "\t,\"tmp1\".\"LINE_NAME\"\n" +
                    "\t,\"tmp1\".\"STATION_NAME\"\n" +
                    "\t,\"tmp1\".\"MO_NUMBER\"\n" +
                    "\t,\"tmp1\".\"IN_DATETIME\"\n" +
                    "\t,\"tmp1\".\"OUT_DATETIME\"\n" +
                    "\t,\"tmp1\".\"REPAIRER\"\n" +
                    "\t,\"tmp1\".\"out_time\"\n" +
                    "\t,\"tmp2\".\"MODEL_NAME\"\n" +
                    "\t,\"tmp2\".\"TEST_CODE\"\n" +
                    "\t,\"tmp2\".\"LOCATION_CODE\"\n" +
                    "FROM (\n" +
                    "\tSELECT SERIAL_NUMBER\n" +
                    "\t\t,LINE_NAME\n" +
                    "\t\t,STATION_NAME\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,IN_DATETIME\n" +
                    "\t\t,OUT_DATETIME\n" +
                    "\t\t,FAIL_LOCATION\n" +
                    "\t\t,REPAIRER\n" +
                    "\t\t,max(OUT_DATETIME) as \"out_time\"\n" +
                    "\tFROM SFISM4.R_REPAIR_IN_OUT_T \n" +
                    "\tWHERE IN_DATETIME BETWEEN :startDate AND :endDate AND OUT_DATETIME IS NULL\n" +
                    "\tGROUP BY SERIAL_NUMBER\n" +
                    "\t\t,LINE_NAME\n" +
                    "\t\t,STATION_NAME\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,IN_DATETIME\n" +
                    "\t\t,OUT_DATETIME\n" +
                    "\t\t,FAIL_LOCATION\n" +
                    "\t\t,REPAIRER\n" +
                    ") \"tmp1\"\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT SERIAL_NUMBER\n" +
                    "\t\t,\"MODEL_NAME\"\n" +
                    "\t\t,\"TEST_TIME\"\n" +
                    "\t\t,\"TEST_CODE\"\n" +
                    "\t\t,\"TEST_GROUP\"\n" +
                    "\t\t,\"TEST_SECTION\"\n" +
                    "\t\t,\"REPAIRER\"\n" +
                    "\t\t,\"REPAIR_TIME\"\n" +
                    "\t\t,\"REASON_CODE\"\n" +
                    "\t\t,\"REPAIR_STATION\"\n" +
                    "\t\t,\"REPAIR_GROUP\"\n" +
                    "\t\t,\"REPAIR_SECTION\"\n" +
                    "\t\t,\"ERROR_ITEM_CODE\"\n" +
                    "\t\t,\"LOCATION_CODE\"\n" +
                    "\t\t,MAX(\"TEST_TIME\") AS \"test_time\"\n" +
                    "\tFROM SFISM4.R_REPAIR_T WHERE test_time  BETWEEN :startDate AND :endDate AND repair_time IS NULL AND MODEL_NAME IN (:listModel)\n" +
                    "\tGROUP BY SERIAL_NUMBER\n" +
                    "\t\t,\"MODEL_NAME\"\n" +
                    "\t\t,\"TEST_TIME\"\n" +
                    "\t\t,\"TEST_CODE\"\n" +
                    "\t\t,\"TEST_GROUP\"\n" +
                    "\t\t,\"TEST_SECTION\"\n" +
                    "\t\t,\"REPAIRER\"\n" +
                    "\t\t,\"REPAIR_TIME\"\n" +
                    "\t\t,\"REASON_CODE\"\n" +
                    "\t\t,\"REPAIR_STATION\"\n" +
                    "\t\t,\"REPAIR_GROUP\"\n" +
                    "\t\t,\"REPAIR_SECTION\"\n" +
                    "\t\t,\"ERROR_ITEM_CODE\"\n" +
                    "\t\t,\"LOCATION_CODE\"\n" +
                    "\t) \"tmp2\" ON \"tmp1\".\"SERIAL_NUMBER\" = \"tmp2\".\"SERIAL_NUMBER\"" +
                    " WHERE MODEL_NAME IS NOT NULL ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getDetailBonePileByDayReport(String factory, List<String> action, String reportDate, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("action", action);
            map.put("listModel", listModel);
            map.put("reportDate", reportDate);

            return namedJdbcTemplate.queryForList("SELECT t.REPORT_DATE \n" +
                    "\t\t,t.SN \n" +
                    "\t\t,t.SN_TYPE\n" +
                    "\t\t,t.CHECKIN_DATE\n" +
                    "\t\t,t.CHECKOUT_DATE\n" +
                    "\t\t,t.FAILTIME\n" +
                    "\t\t,t.FIRST_FAILTIME\n" +
                    "\t\t,t.P_NO \n" +
                    "\t\t,w.WIP_GROUP\n" +
                    "\tFROM SFISM4.R_REPAIR_DAILYREPORT_SN t\n" +
                    "\tLEFT JOIN SFISM4.R_WIP_TRACKING_T w ON t.sn = w.serial_number\n" +
                    "\tWHERE t.REPORT_DATE = :reportDate AND t.SN_TYPE IN (:action) AND t.P_NO IN (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getErrorCodeBySN(String factory, List<String> listSN){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("listSN", listSN);

            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE " +
                    ",MAX(TEST_TIME) as test " +
                    "FROM SFISM4.R_REPAIR_T " +
                    "WHERE SERIAL_NUMBER IN (:listSN) " +
                    "GROUP BY SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getErrorCodeBySN(String factory, String action, String modelName, String reportDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("action", action);
            map.put("modelName", modelName);
            map.put("reportDate", reportDate);

            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE " +
                    ",MAX(TEST_TIME) as test " +
                    "FROM SFISM4.R_REPAIR_T " +
                    "WHERE SERIAL_NUMBER IN (SELECT SN FROM SFISM4.R_REPAIR_DAILYREPORT_SN " +
                    " WHERE REPORT_DATE = :reportDate and p_no = :modelName and sn_type = :action ) " +
                    "GROUP BY SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getErrorCodeBySN(String factory, List<String> action, String modelName, String reportDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("action", action);
            map.put("modelName", modelName);
            map.put("reportDate", reportDate);

            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE " +
                    ",MAX(TEST_TIME) as test " +
                    "FROM SFISM4.R_REPAIR_T " +
                    "WHERE SERIAL_NUMBER IN (SELECT SN FROM SFISM4.R_REPAIR_DAILYREPORT_SN " +
                    " WHERE REPORT_DATE = :reportDate and p_no = :modelName and sn_type IN (:action) ) " +
                    "GROUP BY SERIAL_NUMBER " +
                    ",TEST_TIME " +
                    ",TEST_CODE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getRemainBc8m(String factory, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("listModel", listModel);

            return namedJdbcTemplate.queryForList("SELECT MODEL_NAME " +
                    ",COUNT(MODEL_NAME) as qty " +
                    "FROM SFISM4.R_WIP_TRACKING_T " +
                    " WHERE WIP_GROUP = 'BC8M' AND MODEL_NAME IN (:listModel)  " +
                    "GROUP BY MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> getDetailRemainBc8m(String factory, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT MODEL_NAME " +
                    ",SERIAL_NUMBER, MO_NUMBER, LINE_NAME, SECTION_NAME, IN_STATION_TIME, WIP_GROUP " +
                    "FROM SFISM4.R_WIP_TRACKING_T " +
                    " WHERE WIP_GROUP = 'BC8M' AND MODEL_NAME IN (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getCheckInBc8m(String factory, List<String> listModel, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("listModel", listModel);
            map.put("startDate", startDate);
            map.put("endDate", endDate);

            return namedJdbcTemplate.queryForList("SELECT MODEL_NAME " +
                    ",COUNT(MODEL_NAME) as qty " +
                    "FROM SFISM4.R_SN_DETAIL_T " +
                    " WHERE WIP_GROUP = 'BC8M' AND MODEL_NAME IN (:listModel) AND IN_STATION_TIME BETWEEN :startDate AND :endDate  " +
                    "GROUP BY MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }


    public List<Map<String, Object>> getCheckOutBc8m(String factory, List<String> listModel, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("listModel", listModel);
            map.put("startDate", startDate);
            map.put("endDate", endDate);

            return namedJdbcTemplate.queryForList("SELECT MODEL_NAME " +
                    ",COUNT(MODEL_NAME) as qty " +
                    "FROM SFISM4.R_SN_DETAIL_T " +
                    " WHERE GROUP_NAME = 'BC8M' AND MODEL_NAME IN (:listModel) AND IN_STATION_TIME BETWEEN :startDate AND :endDate  " +
                    "GROUP BY MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getErrorCodeBc8m(String factory, String modelName){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("modelName", modelName);
            return namedJdbcTemplate.queryForList("select  rt.test_code, MAX(rt.test_time) as d, count( rt.test_code) as qty\n" +
                    "from SFISM4.R_WIP_TRACKING_T rwt\n" +
                    "left join SFISM4.R_REPAIR_T rt ON  rwt.serial_number = rt.serial_number\n" +
                    "where rwt.WIP_GROUP = 'BC8M' and rwt.model_name = :modelName  and rt.test_code is not null\n" +
                    "group by  rt.test_code ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getTimeHourBc8m(String factory, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER, MODEL_NAME " +
                    ",IN_STATION_TIME " +
                    "FROM SFISM4.R_WIP_TRACKING_T " +
                    " WHERE WIP_GROUP = 'BC8M' AND MODEL_NAME IN (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getRemainRMA(String factory, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT SERIAL_NUMBER\n" +
                    "\t\t,MO_NUMBER\n" +
                    "\t\t,MODEL_NAME\n" +
                    "\t\t,TEST_TIME\n" +
                    "\t\t,TEST_CODE\n" +
                    "\t\t,TEST_STATION\n" +
                    "\t\t,TEST_GROUP\n" +
                    "\t\t,TEST_SECTION\n" +
                    "\t\t,TEST_LINE\n" +
                    "\t\t,TESTER\n" +
                    "\t\t,REPAIRER\n" +
                    "\t\t,REPAIR_TIME\n" +
                    "\t\t,REASON_CODE\n" +
                    "\tFROM SFISM4.R_REPAIR_T WHERE mo_number like '2279%' and repair_time is null AND MODEL_NAME IN (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyRemainRMA(String factory, List<String> listModel){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT model_name, COUNT(model_name) as qty \n" +
                    "FROM  SFISM4.R_REPAIR_T  WHERE mo_number like '2279%' and repair_time is null AND MODEL_NAME IN (:listModel)\n" +
                    "GROUP BY model_name ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyRemainRMAByTime(String factory, Date startDate, Date endDate, List<String> listModel, String parameter){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT model_name, COUNT(model_name) as qty \n" +
                    "FROM  SFISM4.R_REPAIR_T  WHERE mo_number like '2279%' AND MODEL_NAME IN (:listModel)\n" +
                    "AND "+parameter+ " BETWEEN :startDate AND :endDate " +
                    "GROUP BY model_name ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyErrorCodeByModelName(String factory, String modelName){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("modelName", modelName);
//            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT TEST_CODE, COUNT(TEST_CODE) as qty \n" +
                    "FROM SFISM4.R_REPAIR_T  WHERE mo_number like '2279%' AND repair_time is null AND MODEL_NAME = :modelName\n" +
                    "GROUP BY TEST_CODE ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getQtyTrendChartInOut(String factory, List<String> listModel, Date startDate, Date endDate, String parmeter){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, "");
        if (namedJdbcTemplate == null){
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("listModel", listModel);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("SELECT to_char(tb.t1, 'yyyy/mm/dd') as timer,  COUNT(tb.serial_number) as total\n" +
                    "FROM (\n" +
                    "SELECT serial_number, model_name, (CASE WHEN to_char("+parmeter+", 'hh24:mi:ss') < '07:30:00' THEN "+parmeter+" - 1 ELSE "+parmeter+" END) AS t1\n" +
                    "from SFISM4.R_REPAIR_T \n" +
                    "where "+parmeter+" BETWEEN :startDate and :endDate AND model_name IN (:listModel) AND mo_number like '2279%'\n" +
                    ") tb\n" +
                    "GROUP BY to_char(tb.t1, 'yyyy/mm/dd')\n" +
                    "ORDER BY to_date(timer, 'yyyy/mm/dd') ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

/// PE C03

    public List<Map<String, Object>> qtyPEModelName(String factory, Date startDate, Date endDate, List<String> listModel, String parameter, String testTime){ // C03
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            map.put("parameter", parameter);
            return namedJdbcTemplate.queryForList("SELECT "+parameter+", COUNT(rownum) as qty " +
                    "FROM SFISM4.R_REPAIR_T \n" +
                    "WHERE "+testTime+" between :startDate and :endDate and "+parameter+" IS NOT NULL " +
                    " and model_name IN (:listModel) " +
                    "GROUP BY "+parameter, map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> detailModelNamePE(String factory, Date startDate, Date endDate, List<String> listModel){ // C03 PE
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("SELECT MODEL_NAME " +
                    ",TEST_CODE " +
                    ",REASON_CODE " +
                    ",LOCATION_CODE " +
                     ",count(rownum) as qty " +
                    "FROM SFISM4.R_REPAIR_T "+
                    "WHERE TEST_TIME between :startDate and :endDate and LOCATION_CODE != 'N/A' AND REASON_CODE IS NOT NULL AND MODEL_NAME IS NOT NULL AND TEST_CODE IS NOT NULL " +
                    "and model_name IN (:listModel) " +
                    "group by MODEL_NAME, TEST_CODE, REASON_CODE, LOCATION_CODE " +
                    "ORDER BY MODEL_NAME ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countQtySNTestError(String factory, Date startDate, Date endDate, List<String> listModel){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            return namedJdbcTemplate.queryForList("select count(serial_number) as QTY\n" +
                    "from SFISM4.R_REPAIR_T\n" +
                    "where repair_time BETWEEN :startDate and :endDate and model_name in (:listModel) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getSNErrorCodeNTF(String factory, Date startDate, Date endDate, List<String> listModel, String reasonCode){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
            map.put("reasonCode", reasonCode);
            return namedJdbcTemplate.queryForList("select serial_number, model_name\n" +
                    "from SFISM4.R_REPAIR_T\n" +
                    "where repair_time BETWEEN :startDate and :endDate and model_name in (:listModel) \n" +
                    "and reason_code = :reasonCode ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countErrorCode(String factory, Date startDate, Date endDate, List<String> listModel){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("listModel", listModel);
                return namedJdbcTemplate.queryForList("select model_name, reason_code, count(serial_number) as qty\n" +
                        "from SFISM4.R_REPAIR_T\n" +
                        "where repair_time BETWEEN :startDate and :endDate and model_name in (:listModel) and reason_code is not null \n" +
                        "GROUP BY model_name, reason_code ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countErrorCodeByDefected(String factory, Date startDate, Date endDate, String modelName, String reasonCode){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        String parameeter = "";
        if (reasonCode.equalsIgnoreCase("H003")){
            parameeter = "TEST_CODE";
        }
        if (reasonCode.equalsIgnoreCase("B000")){
            parameeter = "ERROR_ITEM_CODE";
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("modelName", modelName);
            map.put("reasonCode", reasonCode);
            map.put("parameeter", parameeter);
                return namedJdbcTemplate.queryForList("select "+parameeter+", count(serial_number) as qty\n" +
                        "from SFISM4.R_REPAIR_T\n" +
                        "where repair_time BETWEEN :startDate and :endDate and model_name = :modelName and reason_code = :reasonCode and "+parameeter+" is not null \n" +
                        "GROUP BY "+parameeter+" ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countErrorCodeByDefected(String factory, Date startDate, Date endDate, String modelName, List<String> listReasonCode){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        String parameeter = "";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("modelName", modelName);
            map.put("listReasonCode", listReasonCode);
            map.put("parameeter", parameeter);
            return namedJdbcTemplate.queryForList("select reason_code, count(serial_number) as qty\n" +
                    "from SFISM4.R_REPAIR_T\n" +
                    "where repair_time BETWEEN :startDate and :endDate and model_name = :modelName and reason_code not in (:listReasonCode) and test_code is not null\n" +
                    "GROUP BY reason_code ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countErrorCodeByReasonCode(String factory, Date startDate, Date endDate, String modelName, String reasonCode){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        String parameeter = "";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("modelName", modelName);
            map.put("reasonCode", reasonCode);
            map.put("parameeter", parameeter);
            return namedJdbcTemplate.queryForList("select test_code, count(serial_number) as qty\n" +
                    "from SFISM4.R_REPAIR_T\n" +
                    "where repair_time BETWEEN :startDate and :endDate and model_name = :modelName and reason_code = :reasonCode and test_code is not null\n" +
                    "GROUP BY test_code ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getLogErrorCodeNTF(String factory, List<String> listSn, Integer testFlag){
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("testFlag", testFlag);
            map.put("listSn", listSn);
            return namedJdbcTemplate.queryForList("select model_name, group_name, error_code, count(error_code) as qty\n" +
                    "FROM SFISM4.R_TMP_ATEDATA_T_BAK WHERE serial_number in (:listSn) " +
                    "AND TEST_FLAG = :testFlag " +
                    "GROUP BY model_name, group_name, error_code " +
                    "order by qty desc ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

}
