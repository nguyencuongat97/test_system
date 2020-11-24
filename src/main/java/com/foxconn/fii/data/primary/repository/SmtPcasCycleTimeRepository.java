package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmtPcasCycleTimeRepository extends JpaRepository<SmtPcasCycleTime, Integer> {

    List<SmtPcasCycleTime> findByFactoryAndVisibleIsTrue(String factory);

    Page<SmtPcasCycleTime> findByFactoryAndVisibleIsTrue(String factory, Pageable pageable);

    Page<SmtPcasCycleTime> findByFactoryAndModelNameLikeAndVisibleIsTrue(String factory, String modelName, Pageable pageable);

    List<SmtPcasCycleTime> findByFactoryAndLineName(String factory, String lineName);

    SmtPcasCycleTime findByFactoryAndLineNameAndModelNameAndSide(String factory, String lineName, String modelName, String side);

    Page<SmtPcasCycleTime> findByFactoryAndSectionNameAndVisibleIsTrue(String factory, String sectionName, Pageable pageable);

    Page<SmtPcasCycleTime> findByFactoryAndSectionNameAndModelNameLikeAndVisibleIsTrue(String factory, String sectionName, String modelName, Pageable pageable);

}
