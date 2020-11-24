package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtLineProductivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SmtLineProductivityRepository extends JpaRepository<SmtLineProductivity, Integer> {

    List<SmtLineProductivity> findAllByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    List<SmtLineProductivity> findAllByFactoryAndLineNameAndStartDateBetween(String factory, String lineName, Date startDate, Date endDate);

}
