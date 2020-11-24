package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtMounterMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SmtMounterMetaRepository extends JpaRepository<SmtMounterMeta, Integer> {

    List<SmtMounterMeta> findByFactoryAndUpdatedAtAfter(String factory, Date startDate);

    List<SmtMounterMeta> findByFactoryAndLineName(String factory, String lineName);
}
