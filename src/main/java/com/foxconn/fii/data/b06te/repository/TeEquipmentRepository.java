package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeEquipmentRepository extends JpaRepository<TeEquipment, Long> {

    @Query("SELECT t1 as equipment, t2 as location FROM " +
            "TeEquipment t1 " +
            "LEFT JOIN TeEquipmentFixtureLocation t2 " +
            "ON t1.locationId = t2.id")
    List<Map<String, Object>> jpqlEquipmentWithLocation();

    Optional<TeEquipment> findTop1ByEquipmentName(String equipmentName);

}
