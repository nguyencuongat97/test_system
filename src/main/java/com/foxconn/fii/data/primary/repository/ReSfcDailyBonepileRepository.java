package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.ReSfcDailyBonepile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReSfcDailyBonepileRepository extends JpaRepository<ReSfcDailyBonepile, Integer> {
    List<ReSfcDailyBonepile> findByDateTimeBetweenAndFactory(Date startDate, Date endDate, String factory);
}
