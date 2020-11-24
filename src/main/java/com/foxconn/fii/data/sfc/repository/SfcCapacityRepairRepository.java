package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.rmi.MarshalledObject;
import java.util.*;

@Slf4j
@Component
public class SfcCapacityRepairRepository {

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

    public List<Map<String, Object>> countQtyEngineerRepairer(String factory, String customer, Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
//            map.put("modelName", modelName);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("SELECT " +
//                    "rio.SERIAL_NUMBER\n" +
                    "rwt.repairer\n" +
                    ",rio.STATION_NAME\n" +
//                    ",rio.MO_NUMBER\n" +
//                    ",rio.IN_DATETIME\n" +
//                    ",rio.OUT_DATETIME\n" +
                    ", rwt.test_section\n" +
                    ",MAX(rwt.test_time)\n" +
                    ",count(rio.SERIAL_NUMBER) as qty\n" +
                    "FROM SFISM4.R_REPAIR_IN_OUT_T  rio \n" +
                    "left join SFISM4.R_REPAIR_T rwt on rio.serial_number = rwt.serial_number\n" +
                    "WHERE OUT_DATETIME between :startDate AND :endDate\n" +
                    "group by  " +
//                    "rio.SERIAL_NUMBER\n" +
                    "rwt.repairer\n" +
                    ",rio.STATION_NAME\n" +
//                    ",rio.MO_NUMBER\n" +
//                    ",rio.IN_DATETIME\n" +
//                    ",rio.OUT_DATETIME\n" +
                    ",rwt.test_section\n" +
                    "\t", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countQtyEngineerRepairerByShift(String factory, String customer, Date startDate, Date endDate, String shift){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
//            map.put("modelName", modelName);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("shift", shift);
            return namedJdbcTemplate.queryForList("SELECT " +
                    "rwt.repairer\n" +
                    ",rio.STATION_NAME\n" +
                    ", rwt.test_section\n" +
                    ",MAX(rwt.test_time)\n" +
                    ",count(rio.SERIAL_NUMBER) as qty\n" +
                    "FROM SFISM4.R_REPAIR_IN_OUT_T  rio \n" +
                    "left join SFISM4.R_REPAIR_T rwt on rio.serial_number = rwt.serial_number\n" +
                    "WHERE OUT_DATETIME between :startDate AND :endDate AND rwt.R_CLASS = :shift\n" +
                    "group by  " +
                    "rwt.repairer\n" +
                    ",rio.STATION_NAME\n" +
                    ",rwt.test_section\n" +
                    "\t", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countQtyEngineerRepairerByMonth(String factory, String customer, String testSection,Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("SELECT repairer, count(serial_number) as qty, SUBSTR(R_CLASS_DATE, 1, 6) as yearmonth\n" +
                    "\tFROM SFISM4.R_REPAIR_T " +
                    "WHERE repair_time BETWEEN :startDate AND :endDate AND TEST_SECTION = '"+ testSection +"' " +
                    "GROUP BY repairer, SUBSTR(R_CLASS_DATE, 1, 6) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> countQtyEngineerRepairerByMonthSmt(String factory, String customer, String testSection,Date startDate, Date endDate){
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            return namedJdbcTemplate.queryForList("SELECT repairer, count(serial_number) as qty, SUBSTR(R_CLASS_DATE, 1, 6) as yearmonth\n" +
                    "\tFROM SFISM4.R_REPAIR_T " +
                    "WHERE repair_time BETWEEN :startDate AND :endDate AND TEST_SECTION != '"+ testSection +"' " +
                    "GROUP BY repairer, SUBSTR(R_CLASS_DATE, 1, 6) ", map);
        }catch (Exception e){
            log.error("### ", e);
            return Collections.emptyList();
        }
    }
}
