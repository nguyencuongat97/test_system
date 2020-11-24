package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04RepairPTHCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B04RepairPTHCheckInRepository extends JpaRepository<B04RepairPTHCheckIn, Integer> {

    @Query(value = "SELECT product, 'PTH' as section, sum(cast(qty as int)) as count FROM RE_CPEI_PTH_WIP WHERE date_time_input BETWEEN :startDate AND :endDate GROUP BY product", nativeQuery = true)
    List<Object[]> countByModelNameAndSection(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<B04RepairPTHCheckIn> findByInputTimeBetweenOrderByIdDesc(Date startDate, Date endDate);

}
