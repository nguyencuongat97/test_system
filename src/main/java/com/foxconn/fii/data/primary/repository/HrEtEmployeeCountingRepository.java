package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEtEmployeeCounting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface HrEtEmployeeCountingRepository extends JpaRepository<HrEtEmployeeCounting, Long> {

    List<HrEtEmployeeCounting> findByWorkDateBetween(Date startDate, Date endDate);
}
