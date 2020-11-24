package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestLineMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestLineMetaRepository extends JpaRepository<TestLineMeta, Integer> {

    List<TestLineMeta> findByFactory(String factory);
}
