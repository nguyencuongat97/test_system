package com.foxconn.fii.receiver.ie.config;

import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.TestPcEmailList;
import com.foxconn.fii.data.primary.repository.TestPcEmailListRepository;
import com.foxconn.fii.receiver.test.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class IeSchedulerConfig {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private TestPcEmailListRepository testPcEmailListRepository;

    @Scheduled(cron = "0 30 8 * * THU")
    public void syncWeeklyData() {
        log.info("### notify to ie team START");
        String factory = "B06";
        try {
            List<TestPcEmailList> emailList = testPcEmailListRepository.findByFactoryAndDepartmentAndGroupName(factory, "IE", "NOTIFY");
            String email = emailList.stream().map(TestPcEmailList::getEmail).distinct().collect(Collectors.joining(","));

//            String body = "<html><body><b>Dear Users,</b><br/>Please go to <a href=\"http://10.224.81.70:8888/calc-line-balance?factory=" + factory + "\">Smart Factory System</a> to check line and man calculation.<br/><br/><br/>This message is automatically sent, please do not reply directly!<br/>Ext: 26152</body></html>";
            String body = "您好！請登錄FII系統維護Line Balance Report資料!謝謝";
            MailMessage message = MailMessage.of("[FII SW] 您好！請登錄FII系統維護Line Balance Report資料!謝謝", body, null, null);
            notifyService.notifyToMail(message, "", email);
        } catch (Exception e) {
            log.error("### notify to ie team error", e);
        }

        log.info("### notify to ie team END");
    }
}
