package com.foxconn.fii.data.primary.repository;


import com.foxconn.fii.data.primary.model.entity.SmtModelOnline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface SmtModelOnlineRepository extends JpaRepository<SmtModelOnline, Integer> {
    SmtModelOnline findTop1ByLineNameAndStartDateBetween(String lineName, Date startDate, Date endDate);

    SmtModelOnline findTop1ByFactoryAndSectionNameAndLineNameAndCreatedAtBeforeOrderByCreatedAtDesc(
            String factory, String sectionName, String lineName, Date fdate);
}
