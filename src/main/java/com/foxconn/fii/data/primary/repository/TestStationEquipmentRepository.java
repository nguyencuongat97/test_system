package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestStationEquipmentRepository extends JpaRepository<TestStationEquipment, Integer> {

    List<TestStationEquipment> findAllByFactoryAndModelNameAndGroupNameAndStationName(String factory, String modelName, String groupName, String stationName);

    List<TestStationEquipment> findAllByFactory(String factory);

}
