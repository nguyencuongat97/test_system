package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeFixtureCalibrationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeFixtureCalibrationHistoryRepository extends JpaRepository<TeFixtureCalibrationHistory, Long> {

    List<TeFixtureCalibrationHistory> findAllByFixtureIdOrderByStartTimeAsc(long equipmentId);

    Optional<TeFixtureCalibrationHistory> findTop1ByFixtureIdOrderByStartTimeAsc(long equipmentId);

    Optional<TeFixtureCalibrationHistory> findTop1ByIdAndFixtureId(long id, long equipmentId);

}
