package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.primary.model.entity.TemperatureDevice;
import com.foxconn.fii.data.primary.repository.TemperatureDeviceRepository;
import com.foxconn.fii.receiver.test.service.TemperatureDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TemperatureDeviceServiceImpl implements TemperatureDeviceService {

    @Autowired
    private TemperatureDeviceRepository deviceRepository;

    @Override
    public Optional<TemperatureDevice> findById(int deviceId) {
        return deviceRepository.findById(deviceId);
    }

    @Override
    public TemperatureDevice save(TemperatureDevice device) {
        return deviceRepository.save(device);
    }

    @Override
    public List<TemperatureDevice> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<TemperatureDevice> findByLocation(String location) {
        return deviceRepository.findByLocation(location);
    }

    @Override
    public void delete(int id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public Optional<TemperatureDevice> findByCode(String code) {
        return deviceRepository.findByCode(code);
    }
}
