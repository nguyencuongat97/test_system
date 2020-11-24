package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeEquipmentRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeEquipmentRentHistoryRepository extends JpaRepository<TeEquipmentRentHistory, Long> {

    List<TeEquipmentRentHistory> findAllByEquipmentIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeEquipmentRentHistory> findTop1ByEquipmentIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeEquipmentRentHistory> findTop1ByIdAndEquipmentId(long id, long equipmentId);

}
