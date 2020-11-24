package com.foxconn.fii.data.sfc.repository;

import com.foxconn.fii.data.Factory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SfcWipKeyPartsRepository {

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

    public List<Map<String, Object>> exportByKeyPartLikeIndex(String factory, String customer, String keyPart, String serialNumber, Date startDate, Date endDate, List<String> dataList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        String keyPart2 = keyPart+"%";
        String serialNumber2 = serialNumber+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("keyPart2", keyPart2);
            parameters.put("serialNumber2", serialNumber2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("dataList", dataList);

            return namedJdbcTemplate.queryForList("SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER " +
                            "FROM (SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER, ROW_NUMBER() OVER (ORDER BY WORK_TIME ASC) position FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND SERIAL_NUMBER like :serialNumber2 AND KEY_PART_NO IN (:dataList) AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD')) a " +
                            "WHERE a.position >=  AND a.position < 65530",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> findByKeyPartLikeIndex(String factory, String customer, String keyPart, Date startDate, Date endDate, Integer before, Integer after) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        String keyPart2 = keyPart+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("keyPart2", keyPart2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("before", before);
            parameters.put("after", after);


            return namedJdbcTemplate.queryForList("SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER " +
                            "FROM (SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER, ROW_NUMBER() OVER (ORDER BY WORK_TIME ASC) position FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD')) a " +
                            "WHERE a.position >= :before AND a.position < :after",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> findByKeyPartLikeIndex1(String factory, String customer, String keyPart, Date startDate, Date endDate, Integer before, Integer after, List<String> dataList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        if (namedJdbcTemplate == null) {
            return Collections.emptyList();
        }

        String keyPart2 = keyPart+"%";
     //   String serialNumber2 = serialNumber+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
         //   parameters.put("serialNumber2", serialNumber2);
            parameters.put("keyPart2", keyPart2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("before", before);
            parameters.put("after", after);
            parameters.put("dataList", dataList);

            return namedJdbcTemplate.queryForList("SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER " +
                            "FROM (SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER, ROW_NUMBER() OVER (ORDER BY WORK_TIME ASC) position FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND KEY_PART_NO IN (:dataList) AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD')) a " +
                            "WHERE a.position >= :before AND a.position < :after",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }
    public List<Map<String, Object>> findByKeyPartLikeIndexNoSerialNumber2(String factory, String customer, String keyPart, Date startDate, Date endDate, Integer before, Integer after, List<String> dataList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        String keyPart2 = keyPart+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("keyPart2", keyPart2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("before", before);
            parameters.put("after", after);
            parameters.put("dataList", dataList);

            return namedJdbcTemplate.queryForList("SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER " +
                            "FROM (SELECT EMP_NO, SERIAL_NUMBER, KEY_PART_NO, KEY_PART_SN, KP_RELATION, GROUP_NAME, CARTON_NO, WORK_TIME, VERSION, PART_MODE, KP_CODE, MO_NUMBER, ROW_NUMBER() OVER (ORDER BY WORK_TIME ASC) position FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND KEY_PART_NO IN (:dataList) AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD')) a " +
                            "WHERE a.position >= :before AND a.position < :after",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }
    public Object countPageIndex(String factory, String customer, String keyPart, Date startDate, Date endDate, List<String> dataList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        String keyPart2 = keyPart+"%";
      //  String serialNumber2 = serialNumber+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("keyPart2", keyPart2);
          //  parameters.put("serialNumber2", serialNumber2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("dataList", dataList);

            return namedJdbcTemplate.queryForList("SELECT COUNT(GROUP_NAME) qty " +
                            "FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND KEY_PART_NO IN (:dataList) AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD') ",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }
    public Object countPageIndexNoSerialNumber(String factory, String customer, String keyPart, Date startDate, Date endDate, List<String> dataList) {
        NamedParameterJdbcTemplate namedJdbcTemplate = getJdbcTemplate(factory, customer);
        String keyPart2 = keyPart+"%";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("keyPart2", keyPart2);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("dataList", dataList);

            return namedJdbcTemplate.queryForList("SELECT COUNT(GROUP_NAME) qty " +
                            "FROM SFISM4.R_WIP_KEYPARTS_T " +
                            "WHERE KEY_PART_SN like :keyPart2 AND KEY_PART_NO IN (:dataList) AND WORK_TIME BETWEEN TO_DATE(TO_CHAR(:startDate, 'YYYY/MM/DD'), 'YYYY/MM/DD') AND TO_DATE(TO_CHAR(:endDate, 'YYYY/MM/DD'),'YYYY/MM/DD') ",
                    parameters);
        } catch (Exception e) {
            log.error("### findByWorkDateBetween", e);
            return Collections.emptyList();
        }
    }

}
