package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeEquipmentBorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeEquipmentBorrowHistoryRepository extends JpaRepository<TeEquipmentBorrowHistory, Long> {

    List<TeEquipmentBorrowHistory> findAllByEquipmentIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeEquipmentBorrowHistory> findTop1ByEquipmentIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeEquipmentBorrowHistory> findTop1ByIdAndEquipmentId(long id, long equipmentId);

}
