package com.foxconn.fii.receiver.hr.config;

import com.foxconn.fii.data.primary.model.entity.RepairTimeDataBeta;
import com.foxconn.fii.data.primary.repository.RepairTimeDataBetaRepository;
import com.foxconn.fii.receiver.hr.service.HrEmployeeMealTypeVnService;
import com.foxconn.fii.receiver.hr.service.HrEtGetDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@Configuration
@Component
public class HrSchedulerConfig {

    @Autowired
    private HrEtGetDataService hrEtGetDataService;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    private HrEmployeeMealTypeVnService hrEmployeeMealTypeVnService;

    @Scheduled(fixedDelayString = "${batch.fixed.hr-card-history}", initialDelayString = "${batch.initial.hr-card-history}")
    public void hrCardHistoryGetDataScheduled() throws ParserConfigurationException, SAXException, ParseException, IOException {
        log.info("### Hr dashboard get data BEGIN ###");
        hrEtGetDataService.getDailyData(0, 0);
        log.info("### Hr dashboard get data END ###");
    }

    @Scheduled(cron = "${batch.cron.backup-hr-duty-data}")
    public void hrDutyDataBackup() throws SAXException, ParserConfigurationException, ParseException, IOException {
        log.info("### Backup HR duty data BEGIN ###");
        hrEtGetDataService.backupDutyData();
        hrEtGetDataService.backupOvertimeData();
        log.info("### Backup HR duty data END ###");
    }

    //TUNG
    @Scheduled(cron = "${batch.cron.sync-hr-meal}")
    public void syncDataHrMeal() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 15);
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        log.info("###DuongTungSyncDataHR StartTime "+ calendar.getTime());
        hrEmployeeMealTypeVnService.HrSyncDataCanteenFromAPI(calendar.getTime());
        RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
        repairTimeDataBeta.setAction("HR-MEAl");
        repairTimeDataBeta.setStartDate(calendar.getTime());
        repairTimeDataBeta.setFactory("HR");
        repairTimeDataBetaRepository.save(repairTimeDataBeta);
    }

//    @Scheduled(cron = "${batch.cron.send-mail-hr-meal}")
//    public void sendMailHrMeal()throws Exception{
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        calendar.set(Calendar.HOUR_OF_DAY, 4);
//        calendar.set(Calendar.MINUTE, 15);
//
//        Calendar calendar1 = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 4);
//        calendar.set(Calendar.MINUTE, 15);
//        log.info("###DuongTungSendMail StartTime "+ calendar.getTime()+"EndTime: "+ calendar1.getTime());
//        hrEmployeeMealTypeVnService.getDataMealBNAndBN3SendMail(calendar.getTime(), calendar1.getTime());
//    }
    /// END TUNG

}