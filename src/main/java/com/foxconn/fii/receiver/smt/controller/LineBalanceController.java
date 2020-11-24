package com.foxconn.fii.receiver.smt.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.response.MapResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.common.utils.CommonUtils;
import com.foxconn.fii.common.utils.ExcelUtils;
import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.SmtLineMeta;
import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import com.foxconn.fii.data.primary.model.entity.SmtModelOnline;
import com.foxconn.fii.data.primary.model.entity.SmtModelPrinterMeta;
import com.foxconn.fii.data.primary.model.entity.SmtMounterMeta;
import com.foxconn.fii.data.primary.model.entity.SmtMps;
import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;
import com.foxconn.fii.data.primary.model.entity.TestPcEmailList;
import com.foxconn.fii.data.primary.repository.SmtLineMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtModelMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtModelPrinterMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtMounterMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtPcasCycleTimeRepository;
import com.foxconn.fii.data.primary.repository.TestPcEmailListRepository;
import com.foxconn.fii.receiver.smt.service.SmtModelOnlineService;
import com.foxconn.fii.receiver.smt.service.SmtMpsService;
import com.foxconn.fii.receiver.smt.service.SmtPcasCycleTimeService;
import com.foxconn.fii.receiver.test.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/smt")
public class LineBalanceController {

    @Autowired
    private SmtLineMetaRepository smtLineMetaRepository;

    @Autowired
    private SmtModelMetaRepository smtModelMetaRepository;

    @Autowired
    private SmtModelPrinterMetaRepository smtModelPrinterMetaRepository;

    @Autowired
    private SmtMounterMetaRepository smtMounterMetaRepository;

    @Autowired
    private SmtPcasCycleTimeRepository smtPcasCycleTimeRepository;

    @Autowired
    private SmtMpsService smtMpsService;

    @Autowired
    private SmtModelOnlineService smtModelOnlineService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private SmtPcasCycleTimeService smtPcasCycleTimeService;

    @Autowired
    private TestPcEmailListRepository testPcEmailListRepository;

    @GetMapping("/lineMeta/status")
    public ListResponse<SmtLineMeta> getLineMetaStatus(String factory) {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        List<SmtLineMeta> result = smtLineMetaRepository.findByFactory(factory);

        Map<String, List<SmtMounterMeta>> mounterMetaMap = smtMounterMetaRepository.findByFactoryAndUpdatedAtAfter(factory, timeSpan.getStartDate()).stream().collect(Collectors.groupingBy(SmtMounterMeta::getLineName));

        for (SmtLineMeta lineMeta : result) {
            SmtModelOnline modelOnline = smtModelOnlineService.findModelOnline(factory, "SMT", lineMeta.getLineName(), timeSpan.getEndDate());

            if (modelOnline == null) {
                continue;
            }

            lineMeta.setModelName(modelOnline.getModelName());
            lineMeta.setSide(modelOnline.getSide());

            lineMeta.setMounterMetaList(mounterMetaMap.getOrDefault(lineMeta.getLineName(), new ArrayList<>()));

            SmtModelMeta modelMeta = smtModelMetaRepository.findTop1ByFactoryAndModelName(factory, modelOnline.getModelName());
            if (modelMeta != null) {
                lineMeta.setModelMeta(modelMeta);
                lineMeta.setCft(modelMeta.getCft());
                lineMeta.setLinkQty(modelMeta.getLinkQty());
            }

            SmtModelPrinterMeta modelPrinterMeta = smtModelPrinterMetaRepository.findTop1ByFactoryAndModelNameAndSide(factory, modelOnline.getModelName(), modelOnline.getSide());
            if (modelPrinterMeta != null) {
                lineMeta.setModelPrinterMeta(modelPrinterMeta);
            }

            SmtPcasCycleTime pcasCycleTime = smtPcasCycleTimeRepository.findByFactoryAndLineNameAndModelNameAndSide(factory, lineMeta.getLineName(), modelOnline.getModelName(), modelOnline.getSide());
            if (pcasCycleTime != null && pcasCycleTime.getCycleTime() != null) {
                lineMeta.setPcasCycleTime(pcasCycleTime.getCycleTime());
                lineMeta.setModelLineMeta(pcasCycleTime);
            }

            lineMeta.calculateLineBalance();
        }

        return ListResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @GetMapping("/lineMeta")
    public ListResponse<SmtLineMeta> getLineMeta(String factory) {
        List<SmtLineMeta> result = smtLineMetaRepository.findByFactory(factory);
        return ListResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @PostMapping("/lineMeta")
    public CommonResponse<Boolean> createNewLine(@RequestBody SmtLineMeta smtLineMeta) {
        if (StringUtils.isEmpty(smtLineMeta.getFactory()) || StringUtils.isEmpty(smtLineMeta.getLineName())) {
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "null value factory or line name", false);
        }

        long count = smtLineMetaRepository.countByFactoryAndLineName(smtLineMeta.getFactory(), smtLineMeta.getLineName());
        if (count > 0) {
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "line is exist", false);
        }

        SmtLineMeta lineMeta = new SmtLineMeta();
        BeanUtils.copyPropertiesIgnoreNull(smtLineMeta, lineMeta, "id");
        smtLineMetaRepository.save(lineMeta);

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PutMapping("/lineMeta/{lineMetaId}")
    public CommonResponse<Boolean> updateLineMeta(@PathVariable Integer lineMetaId, @RequestBody SmtLineMeta smtLineMeta) {
        Optional<SmtLineMeta> lineMetaOptional = smtLineMetaRepository.findById(lineMetaId);
        if (!lineMetaOptional.isPresent()) {
            log.error("### updateLineMeta error not found {}", lineMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "line not found", false);
        }

        BeanUtils.copyPropertiesIgnoreNull(smtLineMeta, lineMetaOptional.get(), "id");
        smtLineMetaRepository.save(lineMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @DeleteMapping("/lineMeta/{lineMetaId}")
    public CommonResponse<Boolean> deleteLineMeta(@PathVariable Integer lineMetaId) {
        Optional<SmtLineMeta> lineMetaOptional = smtLineMetaRepository.findById(lineMetaId);
        if (!lineMetaOptional.isPresent()) {
            log.error("### deleteLineMeta error not found {}", lineMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "line not found", false);
        }

        smtLineMetaRepository.delete(lineMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @GetMapping("/mps")
    public MapResponse<Map<String, SmtMps>> getMPS(String factory, String cft, String timeSpan) {
//        timeSpan = "2019/08/15 07:30 - 2019/10/22 07:30";
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.MONTH, 2);
            fullTimeSpan.setEndDate(calendar.getTime());
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        Map<String, SmtLineMeta> lineMap = smtLineMetaRepository.findByFactory(factory).stream()
                .collect(Collectors.toMap(SmtLineMeta::getCft, line -> line, (l1, l2) -> {
                    l1.setAbility(l1.getAbility() + l2.getAbility());
                    l1.setTotalLine(l1.getTotalLine() + 1);
                    return l1;
                }));

        Map<String, SmtModelMeta> modelMap = smtModelMetaRepository.findByFactory(factory).stream()
                .collect(Collectors.toMap(SmtModelMeta::getModelName, model -> model, (m1, m2) -> m1));

        List<SmtMps> smtMpsList = smtMpsService.getSmtMpsList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        for (SmtMps mps : smtMpsList) {
            SmtModelMeta modelMeta = modelMap.get(mps.getModelName());
            int totalPart = 0;
            if (modelMeta != null) {
                totalPart = modelMeta.getTotalPart() != null ? modelMeta.getTotalPart() : 0;
            }

            mps.setPlanPart(mps.getPlan() * totalPart);
        }

        Map<String, Map<String, List<SmtMps>>> mpsMap = smtMpsList.stream()
                .sorted(Comparator.comparing(SmtMps::getStartDate))
                .collect(Collectors.groupingBy(mps -> df.format(mps.getStartDate()),
                        LinkedHashMap::new,
                        Collectors.groupingBy(SmtMps::getCft)));

        Map<String, Map<String, SmtMps>> result = smtMpsList.stream()
                .sorted(Comparator.comparing(SmtMps::getStartDate))
                .collect(Collectors.groupingBy(mps -> df.format(mps.getStartDate()),
                        LinkedHashMap::new,
                        Collectors.toMap(
                                SmtMps::getCft,
                                mps -> {
                                    SmtMps mps1 = new SmtMps();
                                    BeanUtils.copyPropertiesIgnoreNull(mps, mps1);
                                    return mps1;
                                },
                                (m1, m2) -> {
                                    m1.setPlan(m1.getPlan() + m2.getPlan());
                                    m1.setPlanPart(m1.getPlanPart() + m2.getPlanPart());
                                    return m1;
                                })));

        for (Map.Entry<String, Map<String, SmtMps>> entry : result.entrySet()) {
            SmtMps total = new SmtMps();
            for (Map.Entry<String, SmtMps> mpsEntry : entry.getValue().entrySet()) {
                SmtMps mps = mpsEntry.getValue();
                mps.setMpsList(mpsMap.get(entry.getKey()).get(mpsEntry.getKey()).stream().filter(m -> m.getPlan() > 0 && m.getPlanPart() > 0).collect(Collectors.toList()));

                mps.setPlan(mps.getPlan() / 1000);
                mps.setPlanPart(mps.getPlanPart() / 1000000);

                SmtLineMeta lineMeta = lineMap.get(mps.getCft());
                double avgAbilityLine = 0.0d;
                double totalLine = 0;
                if (lineMeta != null && lineMeta.getTotalLine() != 0) {
                    avgAbilityLine = lineMeta.getAbility() / lineMeta.getTotalLine();
                    totalLine = lineMeta.getTotalLine();
                }

                mps.setPlanLine(avgAbilityLine != 0 && mps.getRunningDay() != 0 ? mps.getPlanPart() / (avgAbilityLine * mps.getRunningDay()) : 0);
                mps.setTotalLine(totalLine);
                mps.setDiff(mps.getTotalLine() - mps.getPlanLine());

                total.setFactory(mps.getFactory());
                total.setCft("TTL");
                total.setStartDate(mps.getStartDate());
                total.setRunningDay(mps.getRunningDay());
                total.setPlanLine(total.getPlanLine() + mps.getPlanLine());
                total.setTotalLine(total.getTotalLine() + mps.getTotalLine());
                total.setDiff(total.getTotalLine() - total.getPlanLine());
            }
            entry.getValue().put("TTL", total);
        }

        return MapResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @GetMapping("/mps/detail")
    public MapResponse<Map<String, SmtMps>> getMpsDetail(String factory, String cft, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.MONTH, 2);
            fullTimeSpan.setEndDate(calendar.getTime());
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        Map<String, SmtLineMeta> lineMap = smtLineMetaRepository.findByFactory(factory).stream()
                .collect(Collectors.toMap(SmtLineMeta::getCft, line -> line, (l1, l2) -> {
                    l1.setAbility(l1.getAbility() + l2.getAbility());
                    l1.setTotalLine(l1.getTotalLine() + 1);
                    return l1;
                }));

        SmtLineMeta lineMeta = lineMap.get(cft);
        double avgAbilityLine = 0.0d;
        double totalLine = 0;
        if (lineMeta != null && lineMeta.getTotalLine() != 0) {
            avgAbilityLine = lineMeta.getAbility() / lineMeta.getTotalLine();
            totalLine = lineMeta.getTotalLine();
        }

        Map<String, SmtModelMeta> modelMap = smtModelMetaRepository.findByFactory(factory).stream()
                .collect(Collectors.toMap(SmtModelMeta::getModelName, model -> model, (m1, m2) -> m1));

        List<SmtMps> smtMpsList = smtMpsService.getSmtMpsList(factory, cft, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        for (SmtMps mps : smtMpsList) {
            SmtModelMeta modelMeta = modelMap.get(mps.getModelName());
            int totalPart = 0;
            if (modelMeta != null) {
                totalPart = modelMeta.getTotalPart() != null ? modelMeta.getTotalPart() : 0;
            }

            mps.setPlanPart(mps.getPlan() * totalPart);

            mps.setPlan(mps.getPlan() / 1000);
            mps.setPlanPart(mps.getPlanPart() / 1000000);

            mps.setPlanLine(avgAbilityLine != 0 && mps.getRunningDay() != 0 ? mps.getPlanPart() / (avgAbilityLine * mps.getRunningDay()) : 0);
            mps.setTotalLine(totalLine);
            mps.setDiff(mps.getTotalLine() - mps.getPlanLine());
        }

        Map<String, Map<String, SmtMps>> result = smtMpsList.stream()
                .filter(mps -> mps.getPlan() > 0)
                .sorted(Comparator.comparing(SmtMps::getStartDate))
                .collect(Collectors.groupingBy(mps -> df.format(mps.getStartDate()),
                        LinkedHashMap::new,
                        Collectors.toMap(
                                SmtMps::getModelName,
                                mps -> mps,
                                (m1, m2) -> m1)));

        return MapResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @PostMapping("/mps/upload")
    public CommonResponse<Boolean> uploadFileMPS(
            String factory,
            @RequestPart MultipartFile file) {

        Workbook workbook;
        try {
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("upload mps file support only xls and xlsx");
            }

            Map<String, SmtMps> mpsMap = new HashMap<>();

            Sheet sheet = workbook.getSheetAt(0);

            List<Date> workingDateList = new ArrayList<>();
            Row rowWorkingDate = sheet.getRow(1);
            for (int i = 3; i < 13 && i < rowWorkingDate.getLastCellNum(); i++) {
                Cell cell = rowWorkingDate.getCell(i);
                if (DateUtil.isCellDateFormatted(cell)) {
                    workingDateList.add(cell.getDateCellValue());
                }
            }

            List<Double> workingDayList = new ArrayList<>();
            Row rowWorkingDay = sheet.getRow(0);
            for (int i = 3; i < 13 && i < rowWorkingDate.getLastCellNum(); i++) {
                Cell cell = rowWorkingDay.getCell(i);
                if (cell.getCellType() == CellType.NUMERIC) {
                    workingDayList.add(cell.getNumericCellValue());
                }
            }

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String modelNameSI = ExcelUtils.getStringValue(cell);

                cell = row.getCell(1);
                String modelName = ExcelUtils.getStringValue(cell);

                cell = row.getCell(2);
                String cft = ExcelUtils.getStringValue(cell);

                if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(cft)) {
                    continue;
                }

                for (int j = 3; j < 13 && j < rowWorkingDate.getLastCellNum(); j++) {
                    cell = row.getCell(j);

                    String key = factory + "_" + cft + "_" + modelName + "_" + (j - 3);
                    SmtMps mps = mpsMap.get(key);

                    if (mps == null) {
                        mps = new SmtMps();
                        mps.setFactory(factory);
                        mps.setCft(cft);
                        mps.setModelName(modelName);
                        mps.setModelNameSI(modelNameSI);
                        mps.setStartDate(workingDateList.get(j - 3));
                        mps.setRunningDay(workingDayList.get(j - 3));
                        mps.setPlan(0.0);
                    }

                    mps.setPlan(mps.getPlan() + cell.getNumericCellValue());
                    mpsMap.put(key, mps);
                }
            }

            log.debug("### upload mps save data {}", mpsMap.size());
            smtMpsService.saveAll(new ArrayList<>(mpsMap.values()));


            List<TestPcEmailList> emailList = testPcEmailListRepository.findByFactoryAndDepartmentAndGroupName(factory, "IE", "PUBLISH");
            String email = emailList.stream().map(TestPcEmailList::getEmail).distinct().collect(Collectors.joining(","));

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            byte[] encoded = Base64.encodeBase64(file.getBytes());

//            String body = "<html><body><b>Dear Users,</b><br/>Please go to <a href=\"http://10.224.81.70:8888/calc-line-balance?factory="+factory+"\">Smart Factory Management</a> to check line and man calculation.<br/><br/><br/>This message is automatically sent, please do not reply directly!<br/>Ext: 26152</body></html>";
            String body = "您好！VN JBD SMT 線體需求評估已更新請登錄系統查看資料！謝謝";
            MailMessage message = MailMessage.of("[IE TEAM] 您好！VN JBD SMT 線體需求評估已更新請登錄系統查看資料！謝謝", body, new String(encoded, StandardCharsets.US_ASCII), "daily-production-plan-" + df.format(new Date()) + ".xlsx");
            notifyService.notifyToMail(message, "", email);
        } catch (IOException e) {
            log.error("### uploadFileMPS error", e);
            return null;
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @GetMapping(value = "/mps/download", produces = "text/csv; charset=utf-8")
    public void downloadCalculatedLineBalance(String factory, @RequestParam(required = false) String timeSpan, HttpServletResponse httpServletResponse) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.MONTH, 2);
            fullTimeSpan.setEndDate(calendar.getTime());
        }

        Map<String, SmtLineMeta> lineMap = smtLineMetaRepository.findByFactory(factory).stream().collect(Collectors.toMap(SmtLineMeta::getCft, line -> line, (l1, l2) -> {
            l1.setAbility(l1.getAbility() + l2.getAbility());
            l1.setTotalLine(l1.getTotalLine() + 1);
            return l1;
        }));

        Map<String, SmtModelMeta> modelMap = smtModelMetaRepository.findByFactory(factory).stream().collect(Collectors.toMap(SmtModelMeta::getModelName, model -> model, (m1, m2) -> m1));

        List<SmtMps> smtMpsList = smtMpsService.getSmtMpsList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        for (SmtMps mps : smtMpsList) {
            SmtModelMeta modelMeta = modelMap.get(mps.getModelName());
            int totalPart = 0;
            if (modelMeta != null) {
                totalPart = modelMeta.getTotalPart() != null ? modelMeta.getTotalPart() : 0;
            }

            mps.setPlanPart(mps.getPlan() * totalPart);
        }

        Map<String, Map<String, SmtMps>> result = smtMpsService.getSmtMpsList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream()
                .sorted(Comparator.comparing(SmtMps::getStartDate))
                .collect(Collectors.groupingBy(mps -> df.format(mps.getStartDate()),
                        LinkedHashMap::new,
                        Collectors.toMap(
                                SmtMps::getCft,
                                mps -> mps,
                                (m1, m2) -> {
                                    m1.setPlan(m1.getPlan() + m2.getPlan());
                                    m1.setPlanPart(m1.getPlanPart() + m2.getPlanPart());
                                    return m1;
                                })));

        for (Map.Entry<String, Map<String, SmtMps>> entry : result.entrySet()) {
            SmtMps total = new SmtMps();
            for (Map.Entry<String, SmtMps> mpsEntry : entry.getValue().entrySet()) {
                SmtMps mps = mpsEntry.getValue();

                mps.setPlan(mps.getPlan() / 1000);
                mps.setPlanPart(mps.getPlanPart() / 1000000);

                SmtLineMeta lineMeta = lineMap.get(mps.getCft());
                double avgAbilityLine = 0.0d;
                double totalLine = 1;
                if (lineMeta != null && lineMeta.getTotalLine() != 0) {
                    avgAbilityLine = lineMeta.getAbility() / lineMeta.getTotalLine();
                    totalLine = lineMeta.getTotalLine();
                }

                mps.setPlanLine(avgAbilityLine != 0 && mps.getRunningDay() != 0 ? mps.getPlanPart() / (avgAbilityLine * mps.getRunningDay()) : 0);
                mps.setTotalLine(totalLine);
                mps.setDiff(mps.getTotalLine() - mps.getPlanLine());

                total.setFactory(mps.getFactory());
                total.setCft("TTL");
                total.setStartDate(mps.getStartDate());
                total.setRunningDay(mps.getRunningDay());
                total.setPlanLine(total.getPlanLine() + mps.getPlanLine());
                total.setTotalLine(total.getTotalLine() + mps.getTotalLine());
                total.setDiff(total.getTotalLine() - total.getPlanLine());
            }
            entry.getValue().put("TTL", total);
        }

        String fileName = "calc-line-balance-" + df.format(fullTimeSpan.getStartDate()) + "-" + df.format(fullTimeSpan.getEndDate()) + ".xls";
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        httpServletResponse.setCharacterEncoding("UTF-8");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("calc-line-balance");

        if (!result.isEmpty()) {
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setWrapText(true);

            Row headerRow = sheet.createRow(0);
            Row headerRow2 = sheet.createRow(1);
            headerRow2.setHeight((short) 600);

            Cell cell = headerRow.createCell(0);
            cell.setCellValue("CFT");
            cell.setCellStyle(headerStyle);

            cell = headerRow.createCell(1);
            cell.setCellValue("Item");
            cell.setCellStyle(headerStyle);

            cell = headerRow2.createCell(1);
            cell.setCellValue("Manufacturing days\nSố ngày sản xuất");
            cell.setCellStyle(headerStyle);

            String lastDate = "";
            int iHeader = 2;
            for (String header : result.keySet()) {
                lastDate = header;
                cell = headerRow.createCell(iHeader);
                cell.setCellValue(header);
                cell.setCellStyle(headerStyle);

                cell = headerRow2.createCell(iHeader);
                cell.setCellValue(result.get(header).get("TTL").getRunningDay());
                cell.setCellStyle(headerStyle);

                iHeader++;
            }

            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            CellStyle styleDouble = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            styleDouble.setDataFormat(format.getFormat("#,##0.00"));
            int i = 2;
            for (String cft : result.get(lastDate).keySet()) {
                Row row = sheet.createRow(i++);
                row.setHeight((short) 600);
                Row row2 = sheet.createRow(i++);
                row2.setHeight((short) 600);
                Row row3 = sheet.createRow(i++);
                row3.setHeight((short) 600);
                Row row4 = sheet.createRow(i++);
                row4.setHeight((short) 600);
                Row row5 = sheet.createRow(i++);
                row5.setHeight((short) 600);

                Cell cellNo = row.createCell(0);
                cellNo.setCellValue(cft);
                cellNo.setCellStyle(style);

                cell = row.createCell(1);
                cell.setCellValue("TTL forecast(Kpcs)");
                cell.setCellStyle(style);

                cell = row2.createCell(1);
                cell.setCellValue("Placement Requirements\nNhu cầu dán kiện");
                cell.setCellStyle(style);

                cell = row3.createCell(1);
                cell.setCellValue("Line Requirements\nSố chuyền nhu cầu");
                cell.setCellStyle(style);

                cell = row4.createCell(1);
                cell.setCellValue("Existing Line\nSố chuyền hiện có");
                cell.setCellStyle(style);

                cell = row5.createCell(1);
                cell.setCellValue("Diff\nChênh lệch");
                cell.setCellStyle(style);

                int j = 2;
                for (String dateTime : result.keySet()) {
                    cell = row.createCell(j);
                    cell.setCellValue(result.get(dateTime).get(cft).getPlan());
                    cell.setCellStyle(styleDouble);

                    cell = row2.createCell(j);
                    cell.setCellValue(result.get(dateTime).get(cft).getPlanPart());
                    cell.setCellStyle(styleDouble);

                    cell = row3.createCell(j);
                    cell.setCellValue(result.get(dateTime).get(cft).getPlanLine());
                    cell.setCellStyle(styleDouble);

                    cell = row4.createCell(j);
                    cell.setCellValue(result.get(dateTime).get(cft).getTotalLine());
                    cell.setCellStyle(styleDouble);

                    cell = row5.createCell(j);
                    cell.setCellValue(result.get(dateTime).get(cft).getDiff());
                    cell.setCellStyle(styleDouble);

                    j++;
                }
            }
        }

        sheet.autoSizeColumn(1);

        try {
            workbook.write(httpServletResponse.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("### export mps error", e);
        }
    }

    @GetMapping("/modelMeta")
    public ListResponse<SmtModelMeta> getModelMeta(String factory) {
        List<SmtModelMeta> result = smtModelMetaRepository.findByFactory(factory);
        return ListResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @PostMapping("/modelMeta")
    public CommonResponse<Boolean> createModelMeta(@RequestBody SmtModelMeta smtModelMeta) {
        SmtModelMeta modelMeta = new SmtModelMeta();
        BeanUtils.copyPropertiesIgnoreNull(smtModelMeta, modelMeta, "id");
        smtModelMetaRepository.save(modelMeta);

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/modelMeta/upload")
    public CommonResponse<Boolean> uploadModelMeta(String factory, @RequestPart MultipartFile file) {
        Workbook workbook;
        try {
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("upload model meta file support only xls and xlsx");
            }

            List<SmtModelMeta> modelMetaList = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(1);
                String modelNameSI = ExcelUtils.getStringValue(cell);

                cell = row.getCell(2);
                String modelName = ExcelUtils.getStringValue(cell);

                cell = row.getCell(3);
                String cft = ExcelUtils.getStringValue(cell);

                cell = row.getCell(4);
                int totalPart = ExcelUtils.getIntegerValue(cell);

                if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(cft)) {
                    continue;
                }

                SmtModelMeta modelMeta = new SmtModelMeta();
                modelMeta.setFactory(factory);
                modelMeta.setModelName(modelName);
                modelMeta.setModelNameSI(modelNameSI);
                modelMeta.setCft(cft);
                modelMeta.setTotalPart(totalPart);
                modelMetaList.add(modelMeta);
            }

            log.debug("### upload model meta save data {}", modelMetaList.size());
            smtModelOnlineService.saveAllMeta(modelMetaList);

        } catch (IOException e) {
            log.error("### upload model meta error", e);
            return CommonResponse.of(HttpStatus.OK, ResponseCode.FAILED, "Upload model meta failed", false);
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PutMapping("/modelMeta/{modelMetaId}")
    public CommonResponse<Boolean> updateModelMeta(@PathVariable Integer modelMetaId, @RequestBody SmtModelMeta smtModelMeta) {

        Optional<SmtModelMeta> modelMetaOptional = smtModelMetaRepository.findById(modelMetaId);
        if (!modelMetaOptional.isPresent()) {
            log.error("### updateModelMeta error not found {}", modelMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        BeanUtils.copyPropertiesIgnoreNull(smtModelMeta, modelMetaOptional.get(), "id");
        smtModelMetaRepository.save(modelMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @DeleteMapping("/modelMeta/{modelMetaId}")
    public CommonResponse<Boolean> deleteModelMeta(@PathVariable Integer modelMetaId) {
        Optional<SmtModelMeta> modelMetaOptional = smtModelMetaRepository.findById(modelMetaId);
        if (!modelMetaOptional.isPresent()) {
            log.error("### updateModelMeta error not found {}", modelMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        smtModelMetaRepository.delete(modelMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @GetMapping("/modelPrinterMeta")
    public ListResponse<SmtModelPrinterMeta> getModelPrinterMeta(String factory) {
        List<SmtModelPrinterMeta> result = smtModelPrinterMetaRepository.findByFactory(factory);
        return ListResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }

    @PostMapping("/modelPrinterMeta")
    public CommonResponse<Boolean> createModelMeta(@RequestBody SmtModelPrinterMeta smtModelPrinterMeta) {
        SmtModelPrinterMeta modelMeta = new SmtModelPrinterMeta();
        BeanUtils.copyPropertiesIgnoreNull(smtModelPrinterMeta, modelMeta, "id");
        smtModelPrinterMetaRepository.save(modelMeta);

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/modelPrinterMeta/upload")
    public CommonResponse<Boolean> uploadModelPrinterMeta(String factory, @RequestPart MultipartFile file) {
        Workbook workbook;
        try {
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("upload model meta file support only xls and xlsx");
            }

            List<SmtModelMeta> modelMetaList = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(1);
                String modelNameSI = ExcelUtils.getStringValue(cell);

                cell = row.getCell(2);
                String modelName = ExcelUtils.getStringValue(cell);

                cell = row.getCell(3);
                String cft = ExcelUtils.getStringValue(cell);

                cell = row.getCell(4);
                int totalPart = ExcelUtils.getIntegerValue(cell);

                if (StringUtils.isEmpty(modelName) || StringUtils.isEmpty(cft)) {
                    continue;
                }

                SmtModelMeta modelMeta = new SmtModelMeta();
                modelMeta.setFactory(factory);
                modelMeta.setModelName(modelName);
                modelMeta.setModelNameSI(modelNameSI);
                modelMeta.setCft(cft);
                modelMeta.setTotalPart(totalPart);
                modelMetaList.add(modelMeta);
            }

            log.debug("### upload model meta save data {}", modelMetaList.size());
            smtModelOnlineService.saveAllMeta(modelMetaList);

        } catch (IOException e) {
            log.error("### upload model meta error", e);
            return CommonResponse.of(HttpStatus.OK, ResponseCode.FAILED, "Upload model meta failed", false);
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PutMapping("/modelPrinterMeta/{modelPrinterMetaId}")
    public CommonResponse<Boolean> updateModelPrinterMeta(@PathVariable Integer modelPrinterMetaId, @RequestBody SmtModelPrinterMeta modelPrinterMeta) {

        Optional<SmtModelPrinterMeta> modelMetaOptional = smtModelPrinterMetaRepository.findById(modelPrinterMetaId);
        if (!modelMetaOptional.isPresent()) {
            log.error("### updateModelMeta error not found {}", modelPrinterMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        BeanUtils.copyPropertiesIgnoreNull(modelPrinterMeta, modelMetaOptional.get(), "id");
        smtModelPrinterMetaRepository.save(modelMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @DeleteMapping("/modelPrinterMeta/{modelPrinterMetaId}")
    public CommonResponse<Boolean> deleteModelPrinterMeta(@PathVariable Integer modelPrinterMetaId) {
        Optional<SmtModelPrinterMeta> modelMetaOptional = smtModelPrinterMetaRepository.findById(modelPrinterMetaId);
        if (!modelMetaOptional.isPresent()) {
            log.error("### updateModelMeta error not found {}", modelPrinterMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        smtModelPrinterMetaRepository.delete(modelMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @GetMapping("/modelLineMeta")
    public ListResponse<SmtPcasCycleTime> getModelLineMeta(
            @RequestParam String factory,
            @RequestParam String sectionName,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<SmtPcasCycleTime> result;
        if (StringUtils.isEmpty(query)) {
            result = smtPcasCycleTimeRepository.findByFactoryAndSectionNameAndVisibleIsTrue(factory, sectionName, PageRequest.of(page, size));
        } else {
            result = smtPcasCycleTimeRepository.findByFactoryAndSectionNameAndModelNameLikeAndVisibleIsTrue(factory, sectionName, query, PageRequest.of(page, size));
        }
        return ListResponse.success(result);
    }

    @PostMapping("/modelLineMeta")
    public CommonResponse<Boolean> createModelLineMeta(@RequestBody SmtPcasCycleTime smtModelLineMeta) {
        SmtPcasCycleTime modelLineMeta = new SmtPcasCycleTime();
        BeanUtils.copyPropertiesIgnoreNull(smtModelLineMeta, modelLineMeta, "id");
        log.info("dad");
        smtPcasCycleTimeRepository.save(modelLineMeta);

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PutMapping("/modelLineMeta/{modelLineMetaId}")
    public CommonResponse<Boolean> updateModelLineMeta(@PathVariable Integer modelLineMetaId, @RequestBody SmtPcasCycleTime smtModelLineMeta) {

        Optional<SmtPcasCycleTime> modelLineMetaOptional = smtPcasCycleTimeRepository.findById(modelLineMetaId);
        if (!modelLineMetaOptional.isPresent()) {
            log.error("### updateModelLineMeta error not found {}", modelLineMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        BeanUtils.copyPropertiesIgnoreNull(smtModelLineMeta, modelLineMetaOptional.get(), "id");
        smtPcasCycleTimeRepository.save(modelLineMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @DeleteMapping("/modelLineMeta/{modelLineMetaId}")
    public CommonResponse<Boolean> deleteModelLineMeta(@PathVariable Integer modelLineMetaId) {
        Optional<SmtPcasCycleTime> modelLineMetaOptional = smtPcasCycleTimeRepository.findById(modelLineMetaId);
        if (!modelLineMetaOptional.isPresent()) {
            log.error("### updateModelMeta error not found {}", modelLineMetaId);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", true);
        }

        smtPcasCycleTimeRepository.delete(modelLineMetaOptional.get());

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/upload_si_meta")
    public Object uploadFileSiLineMeta(String factory, @RequestPart MultipartFile file)throws Exception, FileNotFoundException {
        Workbook workbook = null;
        try {
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("upload SI file support only xls and xlsx");
            }
            Sheet sheet = workbook.getSheetAt(0);
            List<SmtPcasCycleTime> data = new ArrayList<>();
            List<String> workingList = new ArrayList<>();
            Row rowWorkingDate = sheet.getRow(0);
            for (int i = 0; i < 13 && i < rowWorkingDate.getLastCellNum(); i++) {
                Cell cell = rowWorkingDate.getCell(i);
                workingList.add(cell.getStringCellValue());
            }

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                Cell cell = row.getCell(0);
                String stt = ExcelUtils.getStringValue(cell);

                 cell = row.getCell(1);
                String cft = ExcelUtils.getStringValue(cell);

                cell = row.getCell(2);
                String modelName = ExcelUtils.getStringValue(cell);

                cell = row.getCell(3);
                String cycleTimeSi = ExcelUtils.getStringValue(cell);

                cell = row.getCell(4);
                String manPowerSi = ExcelUtils.getStringValue(cell);

                cell = row.getCell(5);
                String cycleTimePTH = ExcelUtils.getStringValue(cell);

                cell = row.getCell(6);
                String manPowerPTH = ExcelUtils.getStringValue(cell);

                SmtPcasCycleTime modelLineMetaSI = new SmtPcasCycleTime();
                SmtPcasCycleTime modelLineMetaPTH = new SmtPcasCycleTime();

                if (StringUtils.isEmpty(cycleTimePTH) || StringUtils.isEmpty(manPowerPTH)){
                    modelLineMetaSI.setCycleTime(Float.parseFloat(cycleTimeSi));
                    modelLineMetaSI.setFactory(factory);
                    modelLineMetaSI.setManPower((int) Double.parseDouble(manPowerSi));
                    modelLineMetaSI.setModelName(modelName);
                    modelLineMetaSI.setSectionName("SI");
                    modelLineMetaSI.setVisible(true);
                    data.add(modelLineMetaSI);
                }else if (StringUtils.isEmpty(cycleTimeSi) || StringUtils.isEmpty(manPowerSi)){
                    modelLineMetaPTH.setCycleTime(Float.parseFloat(cycleTimePTH));
                    modelLineMetaPTH.setFactory(factory);
                    modelLineMetaPTH.setManPower((int) Double.parseDouble(manPowerPTH));
                    modelLineMetaPTH.setModelName(modelName);
                    modelLineMetaPTH.setSectionName("PTH");
                    modelLineMetaPTH.setVisible(true);
                    data.add(modelLineMetaPTH);

                }else {
                    modelLineMetaSI.setCycleTime(Float.parseFloat(cycleTimeSi));
                    modelLineMetaSI.setFactory(factory);
                    modelLineMetaSI.setManPower((int) Double.parseDouble(manPowerSi));
                    modelLineMetaSI.setModelName(modelName);
                    modelLineMetaSI.setSectionName("SI");
                    modelLineMetaSI.setVisible(true);
                    data.add(modelLineMetaSI);

                    modelLineMetaPTH.setCycleTime(Float.parseFloat(cycleTimePTH));
                    modelLineMetaPTH.setFactory(factory);
                    modelLineMetaPTH.setManPower((int) Double.parseDouble(manPowerPTH));
                    modelLineMetaPTH.setModelName(modelName);
                    modelLineMetaPTH.setSectionName("PTH");
                    modelLineMetaPTH.setVisible(true);
                    data.add(modelLineMetaPTH);
                }
            }
            smtPcasCycleTimeService.saveAllSi(data);
            return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
        }catch (IOException e){
            log.error("### uploadFileMPS error", e);
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "failed", false);
        }


    }
}
