package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.RepairBC8MCheckInIndexUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RepairBC8MCheckInInUniqueRepository extends JpaRepository<RepairBC8MCheckInIndexUnique, Long> {
//    @Query(value = "INSERT INTO re_bc8m_check_in_unique_index (FACTORY, SERIAL_NUMBER, MO_NUMBER, MODEL_NAME, VERSION_CODE) FROM test_repair_serial_error WHERE factory = :factory AND repairer = '' AND updated_at BETWEEN :startDate AND :endDate AND checkin_time IS NOT NULL GROUP BY CONVERT(date, updated_at)", nativeQuery = true)
//    List<Object[]> getCountDataRemainDaily(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT modelName, COUNT(id) as coun FROM RepairBC8MCheckInIndexUnique WHERE factory = :factory AND inStationTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countModelName(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName, COUNT(id) as coun FROM RepairBC8MCheckInIndexUnique WHERE factory = :factory AND inStationTime BETWEEN :startDate AND :endDate AND outStationTime IS NULL GROUP BY modelName")
    List<Object[]> countModelNameRemain(String factory, Date startDate, Date endDate);

    @Query("SELECT modelName as modelName, COUNT(id) AS qty FROM RepairBC8MCheckInIndexUnique WHERE factory = :factory AND outStationTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countOutputModelName(String factory, Date startDate, Date endDate);

    @Query("SELECT testCode, COUNT(id) as coun FROM RepairBC8MCheckInIndexUnique WHERE factory = :factory AND modelName = :modelName AND outStationTime IS NULL GROUP BY testCode")
    List<Object[]> countErrorCode(String factory, String modelName);

    @Query(value = "SELECT CONVERT(varchar, IN_STATION_TIME, 111) as ok, count(ID) as coun " +
            "FROM re_bc8m_check_in_unique_index " +
            "WHERE FACTORY = :factory AND OUT_STATION_TIME IS NULL AND IN_STATION_TIME BETWEEN :startDate AND :endDate " +
            "GROUP BY CONVERT(varchar, IN_STATION_TIME, 111)", nativeQuery = true)
    List<Object[]> countModelNameInputByDay(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT CONVERT(varchar, IN_STATION_TIME, 111) as ok, count(ID) as coun " +
            "FROM re_bc8m_check_in_unique_index " +
            "WHERE FACTORY = :factory AND OUT_STATION_TIME BETWEEN :startDate AND :endDate " +
            "GROUP BY CONVERT(varchar, IN_STATION_TIME, 111)", nativeQuery = true)
    List<Object[]> countModelNameOutputByDay(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, IN_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, IN_STATION_TIME)) ELSE CONVERT(date, IN_STATION_TIME) END as 'ok', count(ID) as coun " +
            "    FROM re_bc8m_check_in_unique_index " +
            "    WHERE factory = :factory And IN_STATION_TIME BETWEEN :startDate AND :endDate " +
            "    GROUP BY CASE WHEN CONVERT(time, IN_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, IN_STATION_TIME)) ELSE CONVERT(date, IN_STATION_TIME) END " +
            "    order by CASE WHEN CONVERT(time, IN_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, IN_STATION_TIME)) ELSE CONVERT(date, IN_STATION_TIME) END ", nativeQuery = true)
    List<Object[]> countModelNameStatusByDay(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT  CASE WHEN CONVERT(time, OUT_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, OUT_STATION_TIME)) ELSE CONVERT(date, OUT_STATION_TIME) END as 'ok', count(ID) as coun " +
            "    FROM re_bc8m_check_in_unique_index " +
            "    WHERE factory = :factory And OUT_STATION_TIME BETWEEN :startDate AND :endDate " +
            "    GROUP BY CASE WHEN CONVERT(time, OUT_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, OUT_STATION_TIME)) ELSE CONVERT(date, OUT_STATION_TIME) END " +
            "    order by CASE WHEN CONVERT(time, OUT_STATION_TIME) < '07:30:00' THEN CONVERT(date, DATEADD(day, -1, OUT_STATION_TIME)) ELSE CONVERT(date, OUT_STATION_TIME) END ", nativeQuery = true)
    List<Object[]> countModelNameStatusByDayCheckOut(@Param("factory") String factory, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<RepairBC8MCheckInIndexUnique> findByFactoryAndInStationTimeBetween(String factory, Date startDate, Date endDate);

    List<RepairBC8MCheckInIndexUnique> findAllByFactoryAndModelNameInAndOutStationTimeIsNull(String factory, List<String> modelName);

}
