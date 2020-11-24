package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TemperatureData;
import com.foxconn.fii.data.primary.model.entity.TemperatureDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TemperatureDataRepository extends JpaRepository<TemperatureData, Integer> {
    TemperatureData findTop1ByDeviceCodeOrderByCreatedAtDesc(String code);
    @Query("SELECT d FROM TemperatureData d WHERE deviceCode = :deviceCode AND createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt ASC")
    List<TemperatureData> getAllByCodeDeviceAndTimeBetween(
            @Param("deviceCode") String deviceCode,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
