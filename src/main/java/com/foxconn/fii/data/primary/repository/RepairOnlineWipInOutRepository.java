package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.RepairOnlineWipInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RepairOnlineWipInOutRepository extends JpaRepository<RepairOnlineWipInOut, Long> {

    @Query("SELECT modelName, count(*) as count FROM RepairOnlineWipInOut WHERE factory = :factory AND checkInTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countByModelName(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM RepairOnlineWipInOut WHERE factory = :factory AND repairTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countOutputModelName(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM RepairOnlineWipInOut WHERE factory = :factory AND repairTime IS NULL  GROUP BY modelName")
    List<Object[]> countInputModelName(String factory);

    @Query("SELECT al FROM RepairOnlineWipInOut as al WHERE al.factory = :factory AND al.repairTime IS NULL ORDER BY checkInTime asc")
    List<RepairOnlineWipInOut> listInputRemain(String factory);

    @Query("SELECT a FROM RepairOnlineWipInOut as a WHERE a.factory = :factory AND a.modelName = :modelName AND a.repairTime IS NULL order by checkInTime asc")
    List<RepairOnlineWipInOut> listInputByModelName(String factory, String modelName);

    @Query(value = "SELECT model_name, repairer, diff, count(*) FROM " +
                        "(SELECT model_name, repairer, datediff(hour, checkin_time, getdate()) as diff " +
                        "FROM re_online_wip_in_out WHERE factory = :factory AND repair_time IS NULL AND checkin_time BETWEEN :startDate AND :endDate) T " +
                    "GROUP BY model_name, repairer, diff", nativeQuery = true)
    List<Object[]> countByModelNameAndHourTime(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT errorCode, count(re) as count FROM RepairOnlineWipInOut re WHERE factory = :factory AND repairTime IS NULL AND modelName = :modelName GROUP BY  errorCode")
    List<Object[]> countByModelNameAndErrorCode(String factory, String modelName);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, checkin_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, checkin_time)) ELSE CONVERT(date, checkin_time) END as 'ok', count(ID) as coun \n" +
            "    FROM re_online_wip_in_out \n" +
            "    WHERE factory = :factory And checkin_time BETWEEN :startDate AND :endDate \n" +
            "    GROUP BY CASE WHEN CONVERT(time, checkin_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, checkin_time)) ELSE CONVERT(date, checkin_time) END \n" +
            "    order by CASE WHEN CONVERT(time, checkin_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, checkin_time)) ELSE CONVERT(date, checkin_time) END", nativeQuery = true)
    List<Object[]> inputTrendChart (@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END as 'ok', count(ID) as coun \n" +
            "    FROM re_online_wip_in_out \n" +
            "    WHERE factory = :factory And repair_time BETWEEN :startDate AND :endDate \n" +
            "    GROUP BY CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END \n" +
            "    order by CASE WHEN CONVERT(time, repair_time) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, repair_time)) ELSE CONVERT(date, repair_time) END", nativeQuery = true)
    List<Object[]> outputTrendChart (@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
