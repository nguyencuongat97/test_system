package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.RepairIODaily;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RepairIODailyRepository extends JpaRepository<RepairIODaily, Integer> {

    List<RepairIODaily> findByFactoryAndStatusAndStartDateBetween(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    @Query("SELECT SUM(total) as total, SUM(input) AS input, SUM(output) AS output, SUM(remain) AS remain FROM RepairIODaily io WHERE factory = :factory AND status = :status AND startDate < :startDate")
    List<Object[]> findByFactoryAndStatusAndStartDateBefore(String factory, TestRepairSerialError.Status status, Date startDate);
}
