package com.foxconn.fii.data.b04wip.repository;

import com.foxconn.fii.data.b04.model.B04RepairCheckIn;
import com.foxconn.fii.data.b04wip.model.B04ReCheckInWip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B04ReCheckInWipRepository extends JpaRepository<B04ReCheckInWip, Integer> {

    @Query("SELECT modelName, count(*) as count FROM B04ReCheckInWip WHERE inputTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countByModelName(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName, 'SI' as sectionName, count(*) as count FROM B04ReCheckInWip WHERE inputTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countByModelNameAndSection(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<B04ReCheckInWip> findByInputTimeBetween(Date startDate, Date endDate);

    List<B04ReCheckInWip> findByInputTimeBetweenAndStationNameNotInAndErrorCodeNotInOrderByInputTimeAsc(Date startDate, Date endDate, List<String> stationName, List<String> errorCode);

    @Query(value = "SELECT CONVERT(varchar, DATE_TIME_INPUT, 111) as ok, count(ID) as coun " +
            "FROM RE_CPEI_WIP " +
            "WHERE DATE_TIME_INPUT BETWEEN :startDate AND :endDate " +
            "GROUP BY CONVERT(varchar, DATE_TIME_INPUT, 111)", nativeQuery = true)
    List<Object[]> countModelNameByDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT ERROR_CODE, count(id) as qty FROM RE_CPEI_WIP " +
            "WHERE DATE_TIME_INPUT BETWEEN :startDate AND :endDate AND ERROR_CODE != '' AND PRODUCT = :modelName  " +
            "GROUP BY ERROR_CODE", nativeQuery = true)
    List<Object[]> countErrorCodeByModelName(Date startDate, Date endDate, String modelName);

    @Query(value = "SELECT CASE WHEN CONVERT(varchar(8), DATE_TIME_INPUT, 108) < '07:30:00' THEN CONVERT(varchar(10), DATEADD(day, -1, DATE_TIME_INPUT), 111) ELSE CONVERT(varchar(10), DATE_TIME_INPUT, 111) END as 'ok', count(id) as 'coun' " +
            "FROM RE_CPEI_WIP " +
            "WHERE DATE_TIME_INPUT BETWEEN :startDate AND :endDate " +
            "GROUP BY CASE WHEN CONVERT(varchar(8), DATE_TIME_INPUT, 108) < '07:30:00' THEN CONVERT(varchar(10), DATEADD(day, -1, DATE_TIME_INPUT), 111) ELSE CONVERT(varchar(10), DATE_TIME_INPUT, 111) END ", nativeQuery = true)
    List<Object[]> countModelNameBySectionName(Date startDate, Date endDate);
}
