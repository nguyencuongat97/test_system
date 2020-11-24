package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeFixtureRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeFixtureRentHistoryRepository extends JpaRepository<TeFixtureRentHistory, Long> {

    List<TeFixtureRentHistory> findAllByFixtureIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeFixtureRentHistory> findTop1ByFixtureIdOrderByReceiveTimeAsc(long equipmentId);

    Optional<TeFixtureRentHistory> findTop1ByIdAndFixtureId(long id, long equipmentId);

}
