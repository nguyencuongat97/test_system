package com.foxconn.fii;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.ExcelUtils;
import com.foxconn.fii.data.b06.service.B06TestGroupService;
import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMetaNew;
import com.foxconn.fii.data.primary.repository.RepairIODailyRepository;
import com.foxconn.fii.data.primary.repository.TestErrorMetaRepository;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaNewRepository;
import com.foxconn.fii.data.primary.repository.TestStationRepository;
import com.foxconn.fii.receiver.hr.service.HrEtGetDataService;
import com.foxconn.fii.receiver.re.service.RepairCapacityService;
import com.foxconn.fii.receiver.re.service.RepairOnlineWipService;
import com.foxconn.fii.receiver.re.service.RepairSyncDataService;
import com.foxconn.fii.receiver.test.config.TmpSchedulerConfig;
import com.foxconn.fii.receiver.test.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@RestController
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Primary
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @RequestMapping("/greeting")
    public String greeting(HttpServletRequest request) {
        return "Welcome to warning system!\n--- VN FII Team ---";
    }

    @RequestMapping("api/time/now")
    public String getNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

//    @PostMapping("/api/ehs/counting")
//    public Boolean saveTmpPlan(@RequestBody String body) {
//        log.info("### counting {}", body);
//        return true;
//    }

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private RepairSyncDataService repairSyncDataService;

    @Autowired
    private RepairIODailyRepository repairIODailyRepository;

    @Autowired
    private B06TestGroupService b06TestGroupService;

    @Autowired
    private RepairCapacityService repairCapacityService;

    @Autowired
    private RepairOnlineWipService repairOnlineWipService;

    @Autowired
    private HrEtGetDataService hrEtGetDataService;

    @Autowired
    private TestStationRepository testStationRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TmpSchedulerConfig tmpSchedulerConfig;

    @Autowired
    private TestErrorMetaRepository testErrorMetaRepository;

    @Autowired
    private TestSolutionMetaNewRepository testSolutionMetaNewRepository;

    @Override
    public void run(String... args) throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        repairOnlineWipService.syncDataBC8MCheckIn(simpleDateFormat.parse("2020/01/01"), simpleDateFormat.parse("2020/02/13"));
       // repairOnlineWipService.saveDataCheckInToDataRaw();
      //  repairOnlineWipService.getDataAll();
//        syncDataService.syncData(timeSpan.getStartDate(), timeSpan.getEndDate());
//        syncDataService.syncDataFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());

//        checkNotifyService.checkLockedNotify();

//        Map<String, ListResponse> result = stationService.getStationIssue("B06", "U10C147.00");
//        log.info("{}", result.size());

//        syncDataService.syncDataErrorFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//        syncDataService.checkMaintainSpec("B04", timeSpan.getStartDate(), timeSpan.getEndDate());

//        checkNotifyService.checkWarningNotify();
//        syncDataService.syncDataRepairFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//        checkNotifyService.checkLockedNotify();

//        List<B04DS02ErrorLog> errorLogList = b04DS02ErrorLogRepository.getLogList("40-1563-0003", "PT", "10.224.83.125", "LPT09", timeSpan.getStartDate(), timeSpan.getEndDate());
//        List<B04DS02ErrorLog> errorLogList = b04DS02ErrorLogRepository.getHistoryOfSerial("E221D161900000678");
//        log.info("{}", errorLogList.size());

//        syncDataService.syncCpkByModelFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());

//        checkNotifyService.checkLockedNotify();

//        TestNotify notify = new TestNotify();
//        notify.setFactory("B06");
//        notify.setModelName("U10C135.10");
//        notify.setGroupName("FT");
//        notify.setStationName("L04C135FT024");
//        notify.setDetail("Got 3 error in a row!");
//
//        TestTracking tracking = new TestTracking();
//        tracking.setType(TestTracking.Type.LOCKED_B);
//        tracking.setId(6772);
//
//        notify.setTracking(tracking);

//        notifyService.notifyToIcivet(notify);

//        syncDataService.syncDataFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//        syncDataService.syncDataErrorFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//            syncDataService.syncDataRepairFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());

//        repairSyncDataService.syncIODaily();
//        automationService.checkNotify();
//        checkNotifyService.checkLockedNotify();

//        syncDataService.syncCpkFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//        syncDataService.syncCpkByModelFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());
//        }

//        repairSyncDataService.syncIODaily(timeSpan.getStartDate(), timeSpan.getEndDate());


        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Date now = df.parse("2019/09/11 11:00:00");

        Date limit = df.parse("2019/09/10 07:30:00");
        calendar.setTime(limit);
        while (calendar.getTime().before(now)) {
            TimeSpan timeSpan = TimeSpan.from(calendar, TimeSpan.Type.HOURLY);
//            repairSyncDataService.syncIODaily(timeSpan.getStartDate(), timeSpan.getEndDate());
//            syncDataService.syncDataRepairFromB04(timeSpan.getStartDate(), timeSpan.getEndDate());

            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
////        syncDataService.syncCpkFromB06New(timeSpan.getStartDate(), timeSpan.getEndDate());
//
//        List<CustKpOnline> onlineList = nbbCustKpOnlineRepository.findByWorkTimeBetween(timeSpan.getStartDate(), timeSpan.getEndDate())
//                .stream().map(NbbCustKpOnlineRepository::mapping)
//                .sorted(Comparator.comparing(CustKpOnline::getTime))
//                .collect(Collectors.toList());

//        List<TestGroup> groupList =  nbbSnDetailRepository.findByModelNameAndInStationTimeBetween(Arrays.asList("GA00595-GB", "GA00822-US"), timeSpan.getStartDate(), timeSpan.getEndDate()).stream()
//                .map(NbbSnDetailRepository::mapping)
//                .collect(Collectors.toList());

//        Date start = df.parse("2019/10/16 07:00:00");
//        Date end = df.parse("2019/10/16 19:30:00");

        //hrEtGetDataService.getDailyData(0, 0);

        //List<TestStation> stationList = testStationRepository.getStationGroupByTime("B06", "U10C100.30", "FT", timeSpan.getStartDate(), timeSpan.getEndDate());

//        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\tiennguyenduc\\Desktop\\BG Input&Output Forecast 10.03.2020.xlsx.xlsx");
//        Sheet sheet = workbook.getSheet("SMT PTH");
//
//        for (int i = 5; i <= sheet.getLastRowNum(); i++) {
//
//            String model = ExcelUtils.getStringValue(sheet.getRow(i).getCell(4));
//            List<String> groupNameList = new ArrayList<>();
//            for (int j =5; j<9; j++) {
//                String group = ExcelUtils.getStringValue(sheet.getRow(i).getCell(j));
//                groupNameList.add(group);
//            }
//
//            for (int j =0; j<groupNameList.size(); j++) {
//                if (StringUtils.isEmpty(groupNameList.get(j))) {
//                    continue;
//                }
//                TestGroupMeta groupMeta = new TestGroupMeta();
//                if (i < 52) {
//                    groupMeta.setFactory("B01");
//                } else {
//                    groupMeta.setFactory("A02");
//                }
//                groupMeta.setModelName(model.toUpperCase());
//                groupMeta.setGroupName(groupNameList.get(j).toUpperCase());
//
//                if (j<2) {
//                    groupMeta.setStage("SMT");
//                    groupMeta.setSubStage("SMT");
//                } else {
//                    groupMeta.setStage("SMA");
//                    groupMeta.setSubStage("SMA");
//                }
//
//                if (j%2 == 0) {
//                    groupMeta.setRemark(1);
//                } else {
//                    groupMeta.setRemark(3);
//                }
//
//                testGroupMetaRepository.save(groupMeta);
//            }
//        }


//        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\tiennguyenduc\\Desktop\\UI MODEL LIST.xlsx");
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//
//            String customerName = ExcelUtils.getStringValue(sheet.getRow(i).getCell(0));
//            List<String> modelNameList = new ArrayList<>();
//            modelNameList.add(ExcelUtils.getStringValue(sheet.getRow(i).getCell(1)));
//            modelNameList.add(ExcelUtils.getStringValue(sheet.getRow(i).getCell(3)));
//
//            for (int j =0; j<modelNameList.size(); j++) {
//                if (StringUtils.isEmpty(modelNameList.get(j))) {
//                    continue;
//                }
//                TestModelMeta groupMeta = new TestModelMeta();
//                groupMeta.setFactory("S03");
//                groupMeta.setModelName(modelNameList.get(j).toUpperCase());
//                groupMeta.setCustomerName(customerName.toUpperCase());
//                groupMeta.setCustomer("UI");
//
//                if (j<1) {
//                    groupMeta.setStage("FAT");
//                    groupMeta.setSubStage("FAT");
//                } else {
//                    groupMeta.setStage("MAIN-PACK");
//                    groupMeta.setSubStage("MAIN-PACK");
//                }
//
//                if (testModelMetaRepository.findByFactoryAndModelName(groupMeta.getFactory(), groupMeta.getModelName()) == null) {
//                    testModelMetaRepository.save(groupMeta);
//                }
//            }
//        }

//        tmpSchedulerConfig.syncWeeklyData();

//        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\tiennguyenduc\\Desktop\\Phan tich loi fail fii_.xlsx.xlsx");
//        for (int is = 0; is < 4; is ++) {
//            Sheet sheet = workbook.getSheetAt(is);
//            String customer = sheet.getSheetName();
//
//            List<TestSolutionMetaNew> solutionMetaList = new ArrayList<>();
//            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
//                CellRangeAddress address = sheet.getMergedRegion(i);
//                if (address.getFirstColumn() == 0) {
//                    String errorDescription = ExcelUtils.getStringValue(sheet.getRow(address.getFirstRow()).getCell(1));
//                    String[] error = errorDescription.split("\n");
//                    List<TestErrorMeta> errorMetaList = new ArrayList<>();
//                    for (String s : error) {
//                        List<TestErrorMeta> errorMetas = testErrorMetaRepository.findAllByFactoryAndDescription("B04", s);
//                        if (!errorMetas.isEmpty()) {
//                            errorMetaList.add(errorMetas.get(0));
//                            errorMetas.get(0).setComponent(ExcelUtils.getStringValue(sheet.getRow(address.getFirstRow()).getCell(2)));
//                            testErrorMetaRepository.save(errorMetas.get(0));
//                        } else {
//                            log.info("error {} not found", s);
//                        }
//                    }
//
//                    for (int j = address.getFirstRow(); j < address.getLastRow(); j++) {
//                        String rootCause = ExcelUtils.getStringValue(sheet.getRow(j).getCell(4));
//                        String action = ExcelUtils.getStringValue(sheet.getRow(j).getCell(5));
//                        for (TestErrorMeta errorMeta : errorMetaList) {
//                            TestSolutionMetaNew solutionMetaNew = new TestSolutionMetaNew();
//                            solutionMetaNew.setFactory("B04");
//                            solutionMetaNew.setCustomer(customer);
//                            solutionMetaNew.setErrorCode(errorMeta.getErrorCode());
//                            solutionMetaNew.setErrorDescription(errorMeta.getDescription());
//                            solutionMetaNew.setRootCause(rootCause);
//                            solutionMetaNew.setAction(action);
//                            solutionMetaList.add(solutionMetaNew);
//                        }
//                    }
//                }
//            }
//            log.info("### solutionMetaList {}", solutionMetaList.size());
////            for (TestSolutionMetaNew solutionMetaNew : solutionMetaList) {
////                testSolutionMetaNewRepository.save(solutionMetaNew);
////            }
//        }

        log.info("### run end");
    }

}
