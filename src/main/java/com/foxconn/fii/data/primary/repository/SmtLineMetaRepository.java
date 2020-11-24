package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtLineMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmtLineMetaRepository extends JpaRepository<SmtLineMeta, Integer> {

    List<SmtLineMeta> findByFactory(String factory);

    long countByFactoryAndLineName(String factory, String lineName);
}
