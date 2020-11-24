package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestReadLogConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestReadLogConfigRepository extends JpaRepository<TestReadLogConfig, Long> {

    List<TestReadLogConfig> findByFactoryAndModelName(String factory, String modelName);

    Optional<TestReadLogConfig> findByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);

}
