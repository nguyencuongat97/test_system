package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04RepairSMTCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B04RepairSMTCheckInRepository extends JpaRepository<B04RepairSMTCheckIn, Integer> {

    @Query(value = "SELECT product, 'SMT' as section, sum(cast(qty as int)) as count FROM RE_CPEI_SMT_WIP WHERE date_time_input BETWEEN :startDate AND :endDate GROUP BY product", nativeQuery = true)
    List<Object[]> countByModelNameAndSection(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<B04RepairSMTCheckIn> findByInputTimeBetweenOrderByIdDesc(Date startDate, Date endDate);

    @Query(value = "SELECT CASE WHEN CONVERT(varchar(8), DATE_TIME_INPUT, 108) < '07:30:00' THEN CONVERT(varchar(10), DATEADD(day, -1, DATE_TIME_INPUT), 111) ELSE CONVERT(varchar(10), DATE_TIME_INPUT, 111) END as 'ok', SUM(CAST(QTY AS int)) " +
            "FROM RE_CPEI_SMT_WIP " +
            "WHERE DATE_TIME_INPUT BETWEEN :startDate AND :endDate " +
            "GROUP BY CASE WHEN CONVERT(varchar(8), DATE_TIME_INPUT, 108) < '07:30:00' THEN CONVERT(varchar(10), DATEADD(day, -1, DATE_TIME_INPUT), 111) ELSE CONVERT(varchar(10), DATE_TIME_INPUT, 111) END ", nativeQuery = true)
    List<Object[]> countInputBySectionName(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
