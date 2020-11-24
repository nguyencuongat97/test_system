package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestStationMetaRepository extends JpaRepository<TestStationMeta, Integer> {

    TestStationMeta findByFactoryAndModelNameAndGroupNameAndStationName(String factory, String modelName, String groupName, String stationName);

    List<TestStationMeta> findAllByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);

    List<TestStationMeta> findAllByFactoryAndModelName(String factory, String modelName);

    List<TestStationMeta> findAllByFactory(String factory);

}
