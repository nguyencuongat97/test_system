package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestCpkSyncConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCpkSyncConfigRepository extends JpaRepository<TestCpkSyncConfig, Long> {

    List<TestCpkSyncConfig> findAllByFactory(String factory);
}
