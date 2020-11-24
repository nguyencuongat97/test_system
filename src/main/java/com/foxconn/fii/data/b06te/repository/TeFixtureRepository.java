package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeFixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeFixtureRepository extends JpaRepository<TeFixture, Long> {

    @Query("SELECT t1 as fixture, t2 as location FROM " +
            "TeFixture t1 " +
            "LEFT JOIN TeEquipmentFixtureLocation t2 " +
            "ON t1.locationId = t2.id")
    List<Map<String, Object>> jpqlFixtureWithLocation();

    Optional<TeFixture> findTop1ByFixtureCode(String fixtureCode);

}
