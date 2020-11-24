package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TemperatureDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemperatureDeviceRepository extends JpaRepository<TemperatureDevice, Integer> {

    List<TemperatureDevice> findByLocation(String location);

    Optional<TemperatureDevice> findByCode(String code);
}
