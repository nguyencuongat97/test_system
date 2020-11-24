package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04RepairBc8m;
import com.foxconn.fii.data.b04.model.B04RepairCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B04RepairBc8mRepository extends JpaRepository<B04RepairBc8m, Integer> {

    @Query("SELECT modelName, count(*) as count FROM B04RepairBc8m WHERE inputTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countInputByModelName(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName, count(*) as count FROM B04RepairBc8m WHERE outputTime BETWEEN :startDate AND :endDate GROUP BY modelName")
    List<Object[]> countOutputByModelName(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<B04RepairCheckIn> findByInputTimeBetween(Date startDate, Date endDate);
}
