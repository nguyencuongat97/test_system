package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeFixtureBorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeFixtureBorrowHistoryRepository extends JpaRepository<TeFixtureBorrowHistory, Long> {

    List<TeFixtureBorrowHistory> findAllByFixtureIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeFixtureBorrowHistory> findTop1ByFixtureIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeFixtureBorrowHistory> findTop1ByIdAndFixtureId(long id, long equipmentId);

}
