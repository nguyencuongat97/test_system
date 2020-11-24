package com.foxconn.fii.receiver.smt.service;

import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;

import java.util.List;

public interface SmtPcasCycleTimeService {

    void saveAll(List<SmtPcasCycleTime> pcasCycleTimeList);

    void saveAllSi(List<SmtPcasCycleTime> pcasCycleTimeList);
}
