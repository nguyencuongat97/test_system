package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04RepairOnlineWip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B04RepairOnlineWipRepository extends JpaRepository<B04RepairOnlineWip, Integer> {
    List<B04RepairOnlineWip> findAll();

    @Query("SELECT product as product, count(re) as count FROM B04RepairOnlineWip re WHERE timeInput BETWEEN :startDate AND :endDate GROUP BY product ")
    List<Object[]> countByProductInput(Date startDate, Date endDate);

    @Query("SELECT product as product, count(re) as count FROM B04RepairOnlineWip re WHERE timeOutput BETWEEN :startDate AND :endDate GROUP BY product ")
    List<Object[]> countByproductOutput(Date startDate, Date endDate);

    List<B04RepairOnlineWip> findByTimeInputBetween(Date startDate, Date endDate);

    List<B04RepairOnlineWip> findByTimeOutputBetween(Date startDate, Date endDate);

    @Query(value = "SELECT CONVERT(varchar, DATE_TIME_INPUT, 111) as ok, count(ID) as coun " +
            "FROM RE_CPEI_ONLINE_WIP " +
            "WHERE DATE_TIME_INPUT BETWEEN :startDate AND :endDate " +
            "GROUP BY CONVERT(varchar, DATE_TIME_INPUT, 111)", nativeQuery = true)
    List<Object[]> countModelNameByDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
