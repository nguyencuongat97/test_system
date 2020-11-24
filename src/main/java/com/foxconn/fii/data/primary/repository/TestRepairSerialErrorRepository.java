package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestRepairSerialErrorRepository extends JpaRepository<TestRepairSerialError, Long> {

    @Query("SELECT errorCode, reason, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND modelName = :modelName AND groupName = :groupName AND updated_at BETWEEN :startDate AND :endDate GROUP BY errorCode, reason")
    List<Object[]> countByErrorCodeAndReason(@Param("factory") String factory, @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT errorCode, reason, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND updated_at BETWEEN :startDate AND :endDate GROUP BY errorCode, reason")
    List<Object[]> countByErrorCodeAndReason(@Param("factory") String factory, @Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT reason, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND updated_at BETWEEN :startDate AND :endDate GROUP BY reason")
    List<Object[]> countByReason(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT reason, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND modelName = :modelName AND sectionName = :sectionName AND errorCode = :errorCode AND updated_at BETWEEN :startDate AND :endDate GROUP BY reason")
    List<Object[]> countByReason(@Param("factory") String factory, @Param("modelName") String modelName, @Param("sectionName") String sectionName, @Param("errorCode") String errorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT groupName, stationName, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND modelName = :modelName AND sectionName = :sectionName AND errorCode = :errorCode AND reason = :reason AND updated_at BETWEEN :startDate AND :endDate GROUP BY groupName, stationName")
    List<Object[]> countByStation(@Param("factory") String factory, @Param("modelName") String modelName, @Param("sectionName") String sectionName, @Param("errorCode") String errorCode, @Param("reason") String reason, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT errorCode, reason, locationCode, modelName, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND updated_at BETWEEN :startDate AND :endDate GROUP BY errorCode, reason, locationCode, modelName")
    List<Object[]> countByErrorCodeAndReasonAndLocationCode(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT modelName, sectionName, status, count(id) as count FROM TestRepairSerialError re WHERE factory = :factory AND updatedAt BETWEEN :startDate AND :endDate GROUP BY modelName, sectionName, status")
    List<Object[]> countByModelNameAndSectionNameAndStatus(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName, errorCode, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND testTime BETWEEN :startDate AND :endDate GROUP BY modelName, errorCode")
    List<Object[]> countByModelNameAndErrorCode(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName, groupName, errorCode, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND testTime BETWEEN :startDate AND :endDate GROUP BY modelName, groupName, errorCode")
    List<Object[]> countByGroupNameAndErrorCode(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName, groupName, locationCode, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND testTime BETWEEN :startDate AND :endDate GROUP BY modelName, groupName, locationCode")
    List<Object[]> countByGroupNameAndLocationCode(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName, errorCode, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND status = :status AND updatedAt BETWEEN :startDate AND :endDate GROUP BY modelName, errorCode")
    List<Object[]> countByModelNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    @Query("SELECT modelName, groupName, errorCode, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND status = :status AND updatedAt BETWEEN :startDate AND :endDate GROUP BY modelName, groupName, errorCode")
    List<Object[]> countByGroupNameAndErrorCode(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);


    @Query(value = "SELECT model_name, repairer, diff, count(*) FROM (SELECT model_name, repairer, datediff(hour, created_at, getdate()) as diff FROM test_repair_serial_error WHERE factory = :factory AND updated_at BETWEEN :startDate AND :endDate) T GROUP BY model_name, repairer, diff", nativeQuery = true)
    List<Object[]> countByModelNameAndTime(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<TestRepairSerialError> findByFactoryAndStatusAndUpdatedAtBetween(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    List<TestRepairSerialError> findByFactoryAndStatusAndRepairTimeBetween(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    @Query("SELECT groupName as groupName, repairer as repairer, sectionName as sectionName, count(id) as count FROM TestRepairSerialError WHERE factory = :factory AND repairer != '' AND updatedAt BETWEEN :startDate AND :endDate GROUP BY groupName, repairer, sectionName")
    List<Map<String, Object>> countRepairerAndSectionName(String factory, Date startDate, Date endDate);

    @Query("SELECT groupName as groupName, repairer as repairer, sectionName as sectionName, count(id) as count FROM TestRepairSerialError WHERE shift = :shift AND factory = :factory AND  repairer != '' AND updatedAt BETWEEN :startDate AND :endDate GROUP BY groupName, repairer, sectionName")
    List<Map<String, Object>> countRepairerAndSectionNameByShift(String factory, String shift, Date startDate, Date endDate);

    @Query("SELECT repairer as repairer, COUNT(id) AS qty, SUBSTRING(workdate, 1, 6) as yearmonth FROM TestRepairSerialError as trse WHERE repairer != '' AND sectionName = 'SI' AND factory = :factory GROUP BY SUBSTRING(trse.workdate, 1, 6), repairer ORDER BY SUBSTRING(trse.workdate, 1, 6), repairer")
    List<Map<String, String>> countRepairerQtyByMonthSI(String factory);

    @Query("SELECT repairer as repairer, COUNT(id) AS qty, SUBSTRING(workdate, 1, 6) as yearmonth FROM TestRepairSerialError as trse WHERE repairer != '' AND sectionName != 'SI' AND factory = :factory GROUP BY SUBSTRING(trse.workdate, 1, 6), repairer ORDER BY SUBSTRING(trse.workdate, 1, 6), repairer")
    List<Map<String, String>> countRepairerQtyByMonthSMTPTH(String factory);
//    @Query("SELECT trse.groupName as groupName, trse.repairer as repairer, trse.sectionName as sectionName, count(trse.id) as count FROM TestRepairSerialError as trse WHERE trse.factory = :factory AND trse.repairer != '' AND trse.updatedAt BETWEEN :startDate AND :endDate GROUP BY trse.groupName, trse.repairer, trse.sectionName")
//    List<Map<String, Object>> countRepairerAndSectionName(String factory, Date startDate, Date endDate);

    @Query("SELECT tu FROM TestRepairSerialError as tu WHERE tu.sectionName = :sectionName AND tu.factory = :factory AND tu.modelName = :modelName AND (tu.status = 0 OR tu.status = 2) ORDER BY id DESC ")
    List<TestRepairSerialError> getModelnameBySI(String factory,String modelName, String sectionName);

    @Query("SELECT modelName as modelName, status as status, sectionName as sectionName, COUNT(id) AS qty FROM TestRepairSerialError WHERE repairer != '' AND sectionName = :sectionName AND status = 1 AND factory = :factory AND repairTime BETWEEN :startDate AND :endDate GROUP BY modelName, status, sectionName")
    List<Object[]> countModelNameByRepairer(String factory, Date startDate, Date endDate, String sectionName);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM TestRepairSerialError WHERE factory = :factory AND sectionName = :sectionName AND repairTime BETWEEN :startDate AND :endDate AND repairer IN (:arrUser) GROUP BY modelName")
    List<Object[]> countModelNameByInRepairer(String factory, String sectionName, Date startDate, Date endDate, List<String> arrUser);

    TestRepairSerialError findTop1BySerialNumberOrderByIdDesc(String serialNumber);
    List<TestRepairSerialError> findBySerialNumberOrderByIdDesc(String serialNumber);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM TestRepairSerialError WHERE factory = :factory AND repairer = '' AND checkInTime IS NOT NULL AND status = 0 GROUP BY modelName")
    List<Object[]> countInputModelName(String factory);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM TestRepairSerialError WHERE factory = :factory AND repairer != '' AND status = 1 AND checkInTime IS NOT NULL AND repairTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countOutputModelName(String factory, Date startDate, Date endDate);

    List<TestRepairSerialError> findByFactoryAndRepairerAndCheckInTimeBetween(String factory, String repairer, Date startDate, Date endDate);

    @Query(value = "SELECT CONVERT(date, updated_at) ,count(id) as coun FROM test_repair_serial_error WHERE factory = :factory AND repairer = '' AND updated_at BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL GROUP BY CONVERT(date, updated_at)", nativeQuery = true)
    List<Object[]> getCountDataRemainDaily(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT model_name, repairer, diff, count(*) FROM (SELECT model_name, repairer, datediff(hour, created_at, getdate()) as diff FROM test_repair_serial_error WHERE factory = :factory AND repairer = '' AND created_at BETWEEN :startDate AND :endDate) T GROUP BY model_name, repairer, diff", nativeQuery = true)
    List<Object[]> countByModelNameAndHourTime(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "SELECT CONVERT(date, repair_time) ,count(id) as coun FROM test_repair_serial_error WHERE factory = :factory AND repair_time BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL GROUP BY CONVERT(date, repair_time)", nativeQuery = true)
    List<Object[]> countOutputByRepairTimeDaily2(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT reason, count(re) as count FROM TestRepairSerialError re WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL GROUP BY reason")
    List<Object[]> countByReasonByT(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND checkInTime IS NOT NULL AND reason NOT IN (:listReason) GROUP BY modelName")
    List<Object[]> countModelNameByReason(String factory, Date startDate, Date endDate, List<String> listReason);

    @Query("SELECT modelName, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND checkInTime IS NOT NULL AND reason = :reasonC GROUP BY modelName")
    List<Object[]> countModelNameByReasonDetail(String factory, Date startDate, Date endDate, String reasonC);

    @Query("SELECT reason, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND checkInTime IS NOT NULL AND reason NOT IN (:listReason) AND modelName = :modelName GROUP BY reason")
    List<Object[]> countModelNameByReasonByProcess(String factory, Date startDate, Date endDate, List<String> listReason, String modelName);

    @Query("SELECT reason, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND checkInTime IS NOT NULL AND reason = :reasonC AND modelName = :modelName GROUP BY reason")
    List<Object[]> countModelNameByReasonDetail2(String factory, Date startDate, Date endDate, String reasonC, String modelName);

    @Query(value = "SELECT model_name, CONVERT(date, repair_time) ,count(id) as coun FROM test_repair_serial_error WHERE factory = :factory AND repair_time BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL GROUP BY model_name, CONVERT(date, repair_time)", nativeQuery = true)
    List<Object[]> countOutputByModelNameByDaily(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT model_name, CONVERT(date, updated_at) ,count(id) as coun FROM test_repair_serial_error WHERE factory = :factory AND updated_at BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL AND repairer = '' GROUP BY model_name, CONVERT(date, updated_at)", nativeQuery = true)
    List<Object[]> countInputByModelNameByDaily(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT error_code, count(id) as coun FROM test_repair_serial_error " +
            "WHERE factory = :factory AND model_name = :modelName AND updated_at BETWEEN :startDate AND :endDate AND error_code != '' " +
            "AND checkin_time IS NOT NULL AND reason_code = :listReason AND repairer != '' " +
            "GROUP BY error_code", nativeQuery = true)
    List<Object[]> countErrorCodeByModelNameByProcess(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modelName") String modelName, @Param("listReason") String listReason);

    @Query(value = "SELECT reason_code, count(id) as coun FROM test_repair_serial_error " +
            "WHERE factory = :factory AND model_name = :modelName AND updated_at BETWEEN :startDate AND :endDate AND reason_code != '' " +
            "AND checkin_time IS NOT NULL AND reason_code NOT IN ('H003', 'B000', 'C001') AND repairer != '' " +
            "GROUP BY reason_code", nativeQuery = true)
    List<Object[]> countReasonCodeByModelNameByProcess(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modelName") String modelName);

    @Query(value = "SELECT location_code, count(id) as coun FROM test_repair_serial_error " +
            "WHERE factory = :factory AND model_name = :modelName AND updated_at BETWEEN :startDate AND :endDate AND location_code != '' " +
            "AND checkin_time IS NOT NULL AND reason_code = 'B000' AND repairer != '' " +
            "GROUP BY location_code", nativeQuery = true)
    List<Object[]> countLocationCodeByModelNameByProcess(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modelName") String modelName);

    @Query("SELECT testLocationCode, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND createdAt BETWEEN :startDate AND :endDate AND modelName = :modelName AND reason = :reasonC AND testLocationCode != '' GROUP BY testLocationCode")
    List<Object[]> countLocationCodeByModelNameReasonDetail(String factory, Date startDate, Date endDate, String modelName, String reasonC);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END as 'ok', count(ID) as coun " +
            "FROM test_repair_serial_error " +
            "WHERE factory = :factory And test_time BETWEEN :startDate AND :endDate AND section_name = :sectionName " +
            "GROUP BY CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END " +
            "order by CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END", nativeQuery = true)
   List<Object[]>  countInputByModelNameAndSection(@Param("factory") String factory, @Param("sectionName") String sectionName, @Param("startDate") Date startDate, @Param("endDate")Date endDate);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END as 'ok', count(ID) as coun " +
            "FROM test_repair_serial_error " +
            "WHERE factory = :factory And repair_time BETWEEN :startDate AND :endDate AND section_name = :sectionName " +
            "GROUP BY CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END " +
            "order by CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END", nativeQuery = true)
   List<Object[]>  countByModelNameAndSection(@Param("factory") String factory, @Param("sectionName") String sectionName, @Param("startDate") Date startDate, @Param("endDate")Date endDate);

    @Query("SELECT modelName, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairer = :repairer AND mo like :mo GROUP BY modelName")
    List<Object[]> countRMARemainByModelName(String factory, String repairer, String mo);

    @Query("SELECT modelName, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND testTime BETWEEN :startDate AND :endDate AND mo like :mo GROUP BY modelName")
    List<Object[]> countRMAInputByModelName(String factory, String mo, Date startDate, Date endDate);

    @Query("SELECT modelName, COUNT(id) as coun FROM TestRepairSerialError WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate AND mo like :mo GROUP BY modelName")
    List<Object[]> countRMAOutputByModelName(String factory, String mo, Date startDate, Date endDate);

    @Query("SELECT errorCode, COUNT(id) as coun FROM TestRepairSerialError " +
            "WHERE mo like :mo and factory = :factory and repairer = :repairer and modelName = :modelName GROUP BY errorCode")
    List<Object[]> countErrorCodeRMAByModelName(String mo, String factory, String repairer, String modelName);

    @Query(value = " SELECT  CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END as 'output', count(*) as coun " +
            "FROM test_repair_serial_error " +
            "WHERE  mo like :mo and factory = :factory  and repair_time between :startDate and :endDate " +
            "GROUP BY CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END " +
            "order by CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END", nativeQuery = true)
    List<Object[]> countTrendChartRMAOutput(@Param("factory") String factory, @Param("mo") String mo, @Param("startDate") Date StartDate, @Param("endDate") Date endDate);

    @Query(value = " SELECT  CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END as 'output', count(*) as coun " +
            "FROM test_repair_serial_error " +
            "WHERE  mo like :mo and factory = :factory and test_time between :startDate and :endDate " +
            "GROUP BY CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END " +
            "order by CASE WHEN CONVERT(time, test_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, test_time)) ELSE CONVERT(date, test_time) END", nativeQuery = true)
    List<Object[]> countTrendChartRMAInput(@Param("factory") String factory, @Param("mo") String mo, @Param("startDate") Date StartDate, @Param("endDate") Date endDate);

    List<TestRepairSerialError> findByFactoryAndRepairerAndMoLike(String factory, String repairer, String mo);

    @Query(value = "select * from test_repair_serial_error\n" +
            "\tWHERE factory = :factory AND repairer != :repairer and status = 1 order by repair_time asc ", nativeQuery = true)
    List<TestRepairSerialError> findDataRepairer(String factory, String repairer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE test_repair_serial_error SET status = 5 WHERE status = 0 AND status = 0 AND test_time <= :expiredDate AND (section_name = 'SMT' OR section_name = 'PTH')", nativeQuery = true)
    void outdatedRepair(@Param("expiredDate") Date expiredDate);
}
