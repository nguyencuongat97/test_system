package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestModelBomMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestModelBomMetaRepository extends JpaRepository<TestModelBomMeta, Integer> {

    List<TestModelBomMeta> findByFactoryAndModelName(String factory, String modelName);
}
