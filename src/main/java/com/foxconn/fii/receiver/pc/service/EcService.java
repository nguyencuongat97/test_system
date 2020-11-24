package com.foxconn.fii.receiver.pc.service;

import com.foxconn.fii.data.primary.model.entity.PcEcn;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface EcService {

    @Async
    void evaluatedEffectiveTime(List<PcEcn> ecnList);

    List<PcEcn> getEcnList(String factory);

    void saveAll(List<PcEcn> ecnList);
}
