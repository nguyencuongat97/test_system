package com.foxconn.fii.receiver.smt.config;

import com.foxconn.fii.receiver.smt.service.PcasDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class PcasSchedulerConfig {

    @Autowired
    private PcasDataService pcasDataService;

    @Scheduled(cron = "15 30 7,19 * * *")
    public void syncDataPcas() {
        pcasDataService.syncDataPcas();
    }
}
