package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtMps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SmtMpsRepository extends JpaRepository<SmtMps, Integer> {

    List<SmtMps> findByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    List<SmtMps> findByFactoryAndCftAndStartDateBetween(String factory, String cft, Date startDate, Date endDate);
}
