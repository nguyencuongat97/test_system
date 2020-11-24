package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeEquipmentCalibrationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeEquipmentCalibrationHistoryRepository extends JpaRepository<TeEquipmentCalibrationHistory, Long> {

    List<TeEquipmentCalibrationHistory> findAllByEquipmentIdOrderByStartTimeAsc(long equipmentId);

    Optional<TeEquipmentCalibrationHistory> findTop1ByEquipmentIdOrderByStartTimeAsc(long equipmentId);

    Optional<TeEquipmentCalibrationHistory> findTop1ByIdAndEquipmentId(long id, long equipmentId);

}
