package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TemperatureDevice;

import java.util.List;
import java.util.Optional;

public interface TemperatureDeviceService {

    Optional<TemperatureDevice> findById(int deviceId);

    TemperatureDevice save(TemperatureDevice device);

    List<TemperatureDevice> findAll();

    List<TemperatureDevice> findByLocation(String location);

    void delete(int id);

    Optional<TemperatureDevice> findByCode(String code);
}
