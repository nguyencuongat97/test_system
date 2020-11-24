package com.foxconn.fii.receiver.re.config;

import com.foxconn.fii.data.primary.model.entity.ReDailyRemain;
import com.foxconn.fii.data.primary.model.entity.RepairOnlineWipInOut;
import com.foxconn.fii.data.primary.model.entity.RepairTimeDataBeta;
import com.foxconn.fii.data.primary.repository.ReDailyRemainRepository;
import com.foxconn.fii.data.primary.repository.RepairOnlineWipInOutRepository;
import com.foxconn.fii.data.primary.repository.RepairTimeDataBetaRepository;
import com.foxconn.fii.data.primary.repository.TestRepairSerialErrorRepository;
import com.foxconn.fii.receiver.re.service.ReCheckInOutSfcDataService;
import com.foxconn.fii.receiver.re.service.RepairOnlineWipService;
import com.foxconn.fii.receiver.re.service.RepairSyncDataService;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@Component
public class ReSchedulerConfig {

    @Autowired
    private RepairOnlineWipService repairOnlineWipService;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    private RepairSyncDataService repairSyncDataService;

    @Autowired
    private RepairOnlineWipInOutRepository repairOnlineWipInOutRepository;

    @Autowired
    private ReDailyRemainRepository reDailyRemainRepository;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private ReCheckInOutSfcDataService reCheckInOutSfcDataService;

    //fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}"
    @Scheduled(fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}")
    public void reGetDataRepairFromWebService() throws Exception {
        Date time = new Date();
        RepairTimeDataBeta timeStartEnd = repairTimeDataBetaRepository.findTop1ByActionOrderByIdDesc("B06-REPAIRER");

            repairSyncDataService.B06ReSyncDataFromWebService(timeStartEnd.getStartDate(), time);
            log.info("###DuongTung RE-B06 timeStart" + timeStartEnd.getStartDate() + "timeEnd"+ time);
            RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
            repairTimeDataBeta.setFactory("B06");
            repairTimeDataBeta.setAction("B06-REPAIRER");
            repairTimeDataBeta.setStartDate(time);
            repairTimeDataBetaRepository.save(repairTimeDataBeta);

    }

    @Scheduled(fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}")
    public void reGetTimeCheckInDataScheduled() throws ParserConfigurationException, SAXException, ParseException, IOException {
        log.info("### re update data raw ###");
        repairOnlineWipService.saveDataCheckInToDataRaw();
    }

    // fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}"
    @Scheduled(fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}")
    public void reGetDataBC8MFromWebService() throws InterruptedException, ParserConfigurationException, SAXException, ParseException, IOException {
        log.info("### Get data from web service RE_BC8M by DuongTung ###");
        Date time = new Date();
        RepairTimeDataBeta timeStartEnd = repairTimeDataBetaRepository.findTop1ByActionOrderByIdDesc("BC8M");

        repairOnlineWipService.syncDataBC8MCheckIn(timeStartEnd.getStartDate(), time);
        log.info("### DuongTung startTime"+ timeStartEnd.getStartDate() + "endTime"+ time);
        repairOnlineWipService.syncDataBC8MCheckOut(timeStartEnd.getStartDate(), time);
        RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
        repairTimeDataBeta.setAction("BC8M");
        repairTimeDataBeta.setFactory("B04");
        repairTimeDataBeta.setStartDate(time);
        repairTimeDataBetaRepository.save(repairTimeDataBeta);
    }

    @Scheduled(fixedDelayString = "${batch.fixed.re-card-checkin}", initialDelayString = "${batch.initial.re-card-checkin}")
    public void syncDataCheckInOutOnlineWip() throws  Exception{
        Date time = new Date();
        RepairTimeDataBeta timeStartEnd = repairTimeDataBetaRepository.findTop1ByActionOrderByIdDesc("ONLINE-WIP");
        repairSyncDataService.syncDataOnlineWipIn(timeStartEnd.getStartDate(), time);
        log.info("### DuongTung-OnlineWip startTime"+ timeStartEnd.getStartDate() + "endTime"+ time);
        repairSyncDataService.syncDataRepairOnlineWipFromB04(timeStartEnd.getStartDate(), time);
        RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
        repairTimeDataBeta.setAction("ONLINE-WIP");
        repairTimeDataBeta.setStartDate(time);
        repairTimeDataBeta.setFactory("B04");
        repairTimeDataBetaRepository.save(repairTimeDataBeta);
    }

    @Scheduled(cron = "${batch.cron.sync-daily-remain-re}")
    public void syncDailyRemainReOnlineWip() throws Exception{
        Date time = new Date();
        String mo = "2279%";
        List<Object[]> dataRemainWip = repairOnlineWipInOutRepository.countInputModelName("B04");
        List<Object[]> dataRemainRMA = testRepairSerialErrorRepository.countRMARemainByModelName("B04", "", mo);

        Long sumRemainWip = dataRemainWip.stream().mapToLong(val ->(long)  val[1]).sum();
        Long sumRemainRMA = dataRemainRMA.stream().mapToLong(val ->(long)  val[1]).sum();

        long sumCountRemain = 0;
        if (dataRemainWip.size() > 0){
            for (Object[] tmp:dataRemainWip){
                sumCountRemain += (long)tmp[1];
            }
        }
        log.info("DuongTungRemainDaily:"+ sumRemainWip +"-"+sumCountRemain+"-"+sumRemainRMA);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        ReDailyRemain reDailyRemain = new ReDailyRemain();
        reDailyRemain.setDailyRemain(sumRemainWip);
        reDailyRemain.setFactory("B04");
        reDailyRemain.setStatus(ReDailyRemain.Status.ONLINE_WIP);
        reDailyRemain.setStartDate(calendar.getTime());
        reDailyRemain.setEndDate(time);
        reDailyRemainRepository.save(reDailyRemain);

        ReDailyRemain reDailyRemain1 = new ReDailyRemain();
        reDailyRemain1.setFactory("B04");
        reDailyRemain1.setDailyRemain(sumRemainRMA);
        reDailyRemain1.setStatus(ReDailyRemain.Status.RMA);
        reDailyRemain1.setStartDate(calendar.getTime());
        reDailyRemain1.setEndDate(time);
        reDailyRemainRepository.save(reDailyRemain1);

    }

    @Scheduled(cron = "${batch.cron.sync-daily-balance-re}")
    public void syncDailyBalance() throws Exception{
        Boolean check = reCheckInOutSfcDataService.checkBalance("B04");
        if (check){
            RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
            repairTimeDataBeta.setAction("BALANCE");
            repairTimeDataBeta.setStartDate(new Date());
            repairTimeDataBeta.setFactory("B04");
            repairTimeDataBetaRepository.save(repairTimeDataBeta);
        }

    }

}
