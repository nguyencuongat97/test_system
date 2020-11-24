package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.ReDailyRemain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReDailyRemainRepository extends JpaRepository<ReDailyRemain, Long> {

    List<ReDailyRemain> findByFactoryAndStartDateBetweenAndStatus(String factory, Date startDate, Date endDate, ReDailyRemain.Status status);

}
