package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestMoTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestMoTypeConfigRepository extends JpaRepository<TestMoTypeConfig, Integer> {

    Optional<TestMoTypeConfig> findByFactoryAndMoType(String factory, MoType moType);
}
