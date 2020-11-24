package com.foxconn.fii.receiver.re.service;

import com.foxconn.fii.data.primary.model.entity.RepairIODaily;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;

import java.util.Date;
import java.util.List;

public interface RepairIODailyService {
    void saveAll(List<RepairIODaily> repairIODailyList);

    List<RepairIODaily> findByFactoryAndInputTimeBetween(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate);

    List<Object[]> findByFactoryAndInputTimeBefore(String factory, TestRepairSerialError.Status status, Date startDate);
}
