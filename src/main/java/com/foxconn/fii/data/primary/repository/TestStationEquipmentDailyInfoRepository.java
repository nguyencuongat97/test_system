package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationEquipmentDailyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface TestStationEquipmentDailyInfoRepository extends JpaRepository<TestStationEquipmentDailyInfo, Long> {

    List<TestStationEquipmentDailyInfo> findAllByEquipmentId(int equipmentId);

}
