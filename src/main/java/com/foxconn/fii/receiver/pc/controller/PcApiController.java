package com.foxconn.fii.receiver.pc.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.common.utils.CommonUtils;
import com.foxconn.fii.common.utils.ExcelUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestPcEmailList;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestPcEmailListRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.receiver.pc.model.Shortage;
import com.foxconn.fii.receiver.test.service.NotifyService;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class PcApiController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private TestPcEmailListRepository testPcEmailListRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    /**
     * SFC
     */
    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Value("${path.data}")
    private String dataPath;

    @PostMapping("/model/plan/from-file")
    public CommonResponse<Boolean> uploadPlan(@RequestPart MultipartFile file, String factory) {
        savePlanFromFile(file, factory);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/model/plan/publish")
    public CommonResponse<Boolean> publishPlan(@RequestPart MultipartFile file, String factory) {
        savePlanFromFile(file, factory);

        List<TestPcEmailList> emailList = testPcEmailListRepository.findByFactoryAndDepartmentAndGroupName(factory, "PC", "PUBLISH");
        String email = emailList.stream().map(TestPcEmailList::getEmail).distinct().collect(Collectors.joining(","));

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            byte[] encoded = Base64.encodeBase64(file.getBytes());

            String body = "<html><body><b>Dear Users,</b><br/>Above is daily plan information.<br/><br/><br/>This message is automatically sent, please do not reply directly!<br/>Ext: 26152</body></html>";

            MailMessage message = MailMessage.of("[PC TEAM] Daily Production Plan", body, new String(encoded, StandardCharsets.US_ASCII), "daily-production-plan-" + df.format(new Date()) + ".xlsx");
            notifyService.notifyToMail(message, "", email);
        } catch (Exception e) {
            throw CommonException.of(String.format("publish plan error %s", e.getMessage()));
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    private void savePlanFromFile(MultipartFile file, String factory) {
        try {
//            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);

            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.FORMULA) {
                            try {
                                evaluator.evaluateInCell(cell);
                            } catch (Exception e) {
                                log.error("### savePlanFromFile error", e);
                            }
                        }
                    }
                }
            }

            loadPlan(workbook, factory, "SMT");
            loadPlan(workbook, factory, "PTH");
            loadPlan(workbook, factory, "TEST");

        } catch (Exception e) {
            log.error("### savePlanFromFile error", e);
            throw new CommonException(String.format("savePlanFromFile error %s", e.getMessage()));
        }
    }

    private void loadPlan(XSSFWorkbook workbook, String factory, String sectionName) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Sheet sheet = workbook.getSheet(sectionName);
            if (sheet == null) {
                return;
            }

            List<TestPlanMeta> data = new ArrayList<>();
            Map<String, Map<String, Object>> workTimeData = new HashMap<>();

            String lastModel = "";
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                int j = 0;
                Row row = sheet.getRow(i);

//                if (i == 0) {
////                    for (j = 6; j < row.getLastCellNum(); j+=5) {
////                        String workdate = ExcelUtils.getStringValue(sheet.getRow(i).getCell(j));
////                        log.info(workdate);
////                        data.put(workdate, new ArrayList<>());
////                    }
//                } else
                if (i > 1) {
                    int lastRowOfLineMerge = -1;
                    for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
                        CellRangeAddress region = sheet.getMergedRegion(k);
                        if (i == region.getFirstRow() && j == region.getFirstColumn()) {
                            lastRowOfLineMerge = region.getLastRow();
                            break;
                        }
                    }

                    if (lastRowOfLineMerge == -1) {
                        continue;
                    }

                    String line = ExcelUtils.getStringValue(sheet.getRow(i).getCell(j));
//                    log.debug(line);

                    for (; i <= lastRowOfLineMerge; ) {
                        j = 1;
                        int lastRowOfShiftMerge = i;
                        for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
                            CellRangeAddress region = sheet.getMergedRegion(k);
                            if (i == region.getFirstRow() && j == region.getFirstColumn()) {
                                lastRowOfShiftMerge = region.getLastRow();
                                break;
                            }
                        }
                        String shift = ExcelUtils.getStringValue(sheet.getRow(i).getCell(j));
//                        log.debug(shift);
                        ShiftType shiftType = !StringUtils.isEmpty(shift) && shift.contains("N") ? ShiftType.NIGHT : ShiftType.DAY;

                        for (j = 2; j < row.getLastCellNum(); j += 5) {
                            String workdate = ExcelUtils.getStringValue(sheet.getRow(0).getCell(j + 4));
//                            log.debug(workdate);

                            Map<String, Object> planMap = workTimeData.computeIfAbsent(line + "_" + shiftType + "_" + workdate, k -> new HashMap<>());

                            for (int k = i; k <= lastRowOfShiftMerge; k++) {
                                String model = ExcelUtils.getStringValue(sheet.getRow(k).getCell(j));
                                String mo = ExcelUtils.getStringValue(sheet.getRow(k).getCell(j + 1));
                                String plan = ExcelUtils.getStringValue(sheet.getRow(k).getCell(j + 2));
                                String total = ExcelUtils.getStringValue(sheet.getRow(k).getCell(j + 3));
                                String demand = ExcelUtils.getStringValue(sheet.getRow(k).getCell(j + 4));

                                if (StringUtils.isEmpty(mo)) {
                                    continue;
                                }

                                Pattern pattern = Pattern.compile("^(\\d*)$");
                                Pattern pattern2 = Pattern.compile("^(\\d*\\.?\\d*)$");
                                Matcher matcher = pattern.matcher(mo);
                                if (!matcher.find()) {
                                    if (mo.endsWith("H")) {
                                        mo = mo.replace("H", "");
                                        Matcher matcher2 = pattern2.matcher(mo);
                                        if (matcher2.find()) {
                                            planMap.put("workTime", mo);
                                            continue;
                                        }
                                    }
                                    planMap.put("note", planMap.getOrDefault("note", "") + " " + mo);
                                    continue;
                                }

                                if (StringUtils.isEmpty(model) && !StringUtils.isEmpty(lastModel)) {
                                    model = lastModel;
                                }

                                if (!StringUtils.isEmpty(model) && !StringUtils.isEmpty(mo)) {
//                                    log.debug(model);
//                                    log.debug(mo);
//                                    log.debug(plan);
//                                    log.debug(total);
//                                    log.debug(demand);
                                    try {
                                        TestPlanMeta planMeta = new TestPlanMeta();
                                        planMeta.setFactory(factory);
                                        if ("TEST".equalsIgnoreCase(sectionName)) {
                                            planMeta.setSectionName("SI");
                                        } else {
                                            planMeta.setSectionName(sectionName);
                                        }
                                        planMeta.setLineName(line);
                                        planMeta.setShiftTime(df.parse(workdate));
                                        planMeta.setShift(shiftType);
                                        TimeSpan timeSpan = TimeSpan.from(workdate + " " + planMeta.getShift(), "yyyy-MM-dd", TimeSpan.Type.DAILY);
                                        if (timeSpan == null) {
                                            continue;
                                        }
                                        planMeta.setStartDate(timeSpan.getStartDate());
                                        planMeta.setType(TestPlanMeta.Type.DAILY);
                                        planMeta.setModelName(model);
                                        planMeta.setMo(mo);
                                        if (!StringUtils.isEmpty(plan)) {
                                            if (pattern2.matcher(plan).find()) {
                                                planMeta.setPlan(((Float) Float.parseFloat(plan)).intValue());
                                            } else if (plan.contains("清尾")) {
                                                planMeta.setRemark(TestPlanMeta.Remark.CLOSE);
                                            } else {
                                                planMeta.setNote(planMeta.getNote() + " plan: " + plan);
                                            }
                                        }
                                        if (!StringUtils.isEmpty(total)) {
                                            if (pattern2.matcher(total).find()) {
                                                planMeta.setTotal(((Float) Float.parseFloat(total)).intValue());
                                            } else {
                                                planMeta.setNote(planMeta.getNote() + " total: " + total);
                                            }
                                        }
                                        if (!StringUtils.isEmpty(demand)) {
                                            if (pattern2.matcher(demand).find()) {
                                                planMeta.setDemand(((Float) Float.parseFloat(demand)).intValue());
                                            } else {
                                                planMeta.setNote(planMeta.getNote() + " demand: " + demand);
                                            }
                                        }
                                        data.add(planMeta);
                                        lastModel = model;
                                    } catch (Exception e) {
                                        log.error("### loadPlan error", e);
                                    }
                                }
                            }
                        }
                        i = lastRowOfShiftMerge + 1;
                    }
                    i = lastRowOfLineMerge;
                }
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -3);
            Date now = calendar.getTime();
            List<TestPlanMeta> savedData = new ArrayList<>();
            data.forEach(plan -> {
//                if (now.after(plan.getStartDate())) {
//                    return;
//                }

                Map<String, Object> m = workTimeData.get(plan.getLineName() + "_" + plan.getShift() + "_" + df.format(plan.getShiftTime()));
                if (m != null) {
                    plan.setWorkTime(Float.parseFloat((String) m.getOrDefault("workTime", "0")));
                    plan.setNote(m.getOrDefault("note", "") + " " + plan.getNote());
                }

                savedData.add(plan);
            });

            log.debug("### loadPlanFromFile save {} data", savedData.size());
            testPlanService.saveAll(savedData);
        } catch (Exception e) {
            log.error("### loadPlan error", e);
            throw new CommonException(String.format("loadPlan error %s", e.getMessage()));
        }
        log.debug("### loadPlanFromFile END");
    }

    @PostMapping("/model/plan/copy")
    public CommonResponse<Boolean> copyPlan(
            @RequestParam String factory,
            @RequestParam(required = false) String sectionName,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String srcWorkDate,
            @RequestParam(required = false) String targetWorkDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        if (!StringUtils.isEmpty(srcWorkDate)) {
            try {
                calendar.setTime(df.parse(srcWorkDate));
            } catch (Exception e) {
                log.error("### copyPlan error", e);
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date from = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date to = calendar.getTime();

        Date target = calendar.getTime();
        if (!StringUtils.isEmpty(targetWorkDate)) {
            try {
                calendar.setTime(df.parse(targetWorkDate));
                target = calendar.getTime();
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 29);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
            } catch (Exception e) {
                log.error("### copyPlan error", e);
            }
        }

        Date fromTarget = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date toTarget = calendar.getTime();

        List<TestPlanMeta> planMetaList = testPlanService.getPlanList(factory, sectionName, modelName, fromTarget, toTarget, TestPlanMeta.Type.DAILY);
        if (!planMetaList.isEmpty()) {
            testPlanService.deleteAll(planMetaList);
        }

        planMetaList = testPlanService.getPlanList(factory, sectionName, modelName, from, to, TestPlanMeta.Type.DAILY);
        planMetaList.sort(Comparator.comparing(TestPlanMeta::getStartDate));

        Map<String, TestPlanMeta> latestPlanMapByMo = planMetaList.stream().collect(Collectors.toMap(TestPlanMeta::getMo, plan -> plan, (p1, p2) -> p1.getStartDate().after(p2.getStartDate()) ? p1 : p2));

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        List<TestPlanMeta> copiedPlanMetaList = new ArrayList<>();
        for (TestPlanMeta plan : planMetaList) {
            TestPlanMeta copiedPlan = new TestPlanMeta();
            BeanUtils.copyPropertiesIgnoreNull(plan, copiedPlan);

            copiedPlan.setShiftTime(target);

            calendar.set(Calendar.MINUTE, 30);
            if (copiedPlan.getShift() == ShiftType.DAY) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 19);
            }
            copiedPlan.setStartDate(calendar.getTime());

            TestPlanMeta latestPlan = latestPlanMapByMo.get(plan.getMo());
            if (latestPlan != null) {
                copiedPlan.setTotal(copiedPlan.getPlan() + latestPlan.getTotal());
                latestPlan.setTotal(copiedPlan.getTotal());
            }

            copiedPlanMetaList.add(copiedPlan);
        }

        testPlanService.saveAll(copiedPlanMetaList);

        latestPlanMapByMo = copiedPlanMetaList.stream().collect(Collectors.toMap(TestPlanMeta::getMo, plan -> plan, (p1, p2) -> p1.getStartDate().after(p2.getStartDate()) ? p1 : p2));
        for (TestPlanMeta plan : latestPlanMapByMo.values()) {
            List<TestPlanMeta> changedPlanMetaList = new ArrayList<>();
            List<TestPlanMeta> planMetaByMoList = testPlanService.findByFactoryAndMo(plan.getFactory(), plan.getMo());
            if (!planMetaByMoList.isEmpty()) {
                planMetaByMoList.sort(Comparator.comparing(TestPlanMeta::getStartDate));
                int newTotalPlan = plan.getTotal();
                for (TestPlanMeta planTmp : planMetaByMoList) {
                    if (plan.getStartDate().before(planTmp.getStartDate())) {
                        newTotalPlan = newTotalPlan + planTmp.getPlan();
                        planTmp.setTotal(newTotalPlan);
                        changedPlanMetaList.add(planTmp);
                    }
                }
                testPlanService.saveAll(changedPlanMetaList);
            }
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/model/plan/generate")
    public CommonResponse<Boolean> generateNewPlan(@RequestBody TestPlanMeta planMeta) {
        if (planMeta.getMoQty() == 0) {
            throw CommonException.of("generate new plan error cause by mo quantity is zero");
        }
        if (planMeta.getUph() == 0) {
            throw CommonException.of("generate new plan error cause by UPH is zero");
        }
        if (planMeta.getShiftTime() == null) {
            throw CommonException.of("generate new plan error cause by shift time is null");
        }
        if (StringUtils.isEmpty(planMeta.getSectionName())) {
            planMeta.setSectionName("SI");
        }

        int workDay = (int) Math.ceil(planMeta.getMoQty() / (planMeta.getUph() * planMeta.getWorkTime()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(planMeta.getShiftTime());

        int plan = (int) Math.floor(planMeta.getUph() * planMeta.getWorkTime());
        int moQty = planMeta.getMoQty();
        int total = 0;
        List<TestPlanMeta> planMetaList = new ArrayList<>();
        for (int i = 0; i < workDay; i++) {
            TestPlanMeta tmp = new TestPlanMeta();
            BeanUtils.copyPropertiesIgnoreNull(planMeta, tmp, "id");
            tmp.setShiftTime(calendar.getTime());

            if (plan < moQty) {
                tmp.setPlan(plan);
            } else {
                tmp.setPlan(moQty);
            }
            moQty -= plan;

            total += tmp.getPlan();
            tmp.setTotal(total);
            tmp.setType(TestPlanMeta.Type.DAILY);

            Calendar calendarTmp = (Calendar) calendar.clone();
            if (planMeta.getShift() == ShiftType.ALL) {
                calendarTmp.set(Calendar.MINUTE, 30);

                tmp.setShift(ShiftType.DAY);
                calendarTmp.set(Calendar.HOUR_OF_DAY, 7);
                tmp.setStartDate(calendarTmp.getTime());
                planMetaList.add(tmp);

                if (moQty > 0) {
                    TestPlanMeta tmpNight = new TestPlanMeta();
                    BeanUtils.copyPropertiesIgnoreNull(tmp, tmpNight, "id");

                    if (plan < moQty) {
                        tmpNight.setPlan(plan);
                    } else {
                        tmpNight.setPlan(moQty);
                    }
                    moQty -= plan;

                    total += tmpNight.getPlan();
                    tmpNight.setTotal(total);

                    tmpNight.setShift(ShiftType.NIGHT);
                    calendarTmp.set(Calendar.HOUR_OF_DAY, 19);
                    tmpNight.setStartDate(calendarTmp.getTime());

                    planMetaList.add(tmpNight);
                    i++;
                }
            } else {
                calendarTmp.set(Calendar.MINUTE, 30);

                if (planMeta.getShift() == ShiftType.DAY) {
                    calendarTmp.set(Calendar.HOUR_OF_DAY, 7);
                    tmp.setStartDate(calendarTmp.getTime());
                } else {
                    calendarTmp.set(Calendar.HOUR_OF_DAY, 19);
                    tmp.setStartDate(calendarTmp.getTime());
                }
                planMetaList.add(tmp);
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (!planMetaList.isEmpty()) {
            planMetaList.get(0).setRemark(TestPlanMeta.Remark.OPEN);
            planMetaList.get(planMetaList.size() - 1).setRemark(TestPlanMeta.Remark.CLOSE);
            testPlanService.saveAll(planMetaList);
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/model/plan/output")
    public CommonResponse<Boolean> updateOutput(String factory, String customer, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<String> modelList = testModelService.getModelMetaList(factory, customer, "").stream().map(TestModelMeta::getModelName).collect(Collectors.toList());
        List<String> modelOnlineList = sfcSmtGroupRepository.getModelNameByWorkDateBetween(factory, customer, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().filter(modelList::contains).collect(Collectors.toList());

        List<TestGroupMeta> groupMetaList = testGroupMetaRepository.findGroupOutputByFactory(factory, modelList);
        Map<String, String> groupMapByModel = groupMetaList.stream().collect(Collectors.toMap(TestGroupMeta::getModelName, TestGroupMeta::getGroupName, (g1, g2) -> g1));
        List<String> groupList = groupMetaList.stream().map(TestGroupMeta::getGroupName).collect(Collectors.toList());

        Map<String, Integer> outputMap;
        if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
            outputMap = testGroupService.getGroupDailyBG(factory, modelOnlineList, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate()).stream()
                    .filter(group -> group.getGroupName().equalsIgnoreCase(groupMapByModel.get(group.getModelName())))
                    .collect(Collectors.toMap(TestGroup::getMo, TestGroup::getPass, (o1, o2) -> o1 + o2));
        } else {
            List<TestGroup> tmp = sfcSmtGroupRepository.findByWorkDateBetween(factory, customer, modelOnlineList, groupList, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map)).collect(Collectors.toList());
            outputMap = tmp.stream()
                    .filter(group -> group.getGroupName().equalsIgnoreCase(groupMapByModel.get(group.getModelName())))
                    .collect(Collectors.toMap(TestGroup::getMo, TestGroup::getPass, (o1, o2) -> o1 + o2));
        }

        for (Map.Entry<String, Integer> entry : outputMap.entrySet()) {
            List<TestPlanMeta> planList = testPlanService.findByFactoryAndMo(factory, entry.getKey(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            if (planList.isEmpty()) {
                log.error("### sync output plan error {} {} {} {}", factory, entry.getKey(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
                continue;
            }

            int oldPlan = planList.stream().mapToInt(TestPlanMeta::getPlan).sum();

            TestPlanMeta plan = planList.get(0);
            plan.setOutput(entry.getValue());
            int newTotalOutput = plan.getTotal() - oldPlan + entry.getValue();
//            plan.setTotal(newTotalOutput);
            plan.setTotalOutput(newTotalOutput);

            testPlanService.savePlan(plan);

            List<TestPlanMeta> planMetaList = testPlanService.findByFactoryAndMo(factory, entry.getKey());
            if (!planMetaList.isEmpty()) {
                planMetaList.sort(Comparator.comparing(TestPlanMeta::getStartDate));
                for (TestPlanMeta planTmp : planMetaList) {
                    if (fullTimeSpan.getEndDate().before(planTmp.getStartDate())) {
                        newTotalOutput = newTotalOutput + planTmp.getPlan();
                        planTmp.setTotal(newTotalOutput);
                    }
                }

                testPlanService.saveAll(planMetaList);
            }
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @PostMapping("/model/plan/total-output")
    public CommonResponse<Boolean> updateTotalOutput(String factory, String customer, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<TestPlanMeta> planList = testPlanService.findByFactory(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<String> modelOnlineList = planList.stream().map(TestPlanMeta::getModelName).distinct().collect(Collectors.toList());
        List<String> moList = planList.stream().map(TestPlanMeta::getMo).distinct().collect(Collectors.toList());

        if (modelOnlineList.isEmpty()) {
            return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
        }

        List<TestGroupMeta> groupMetaList = testGroupMetaRepository.findGroupOutputByFactory(factory, modelOnlineList);
        Map<String, String> groupMapByModel = groupMetaList.stream().collect(Collectors.toMap(TestGroupMeta::getModelName, TestGroupMeta::getGroupName, (g1, g2) -> g1));
        List<String> groupList = groupMetaList.stream().map(TestGroupMeta::getGroupName).distinct().collect(Collectors.toList());

        Map<String, Integer> outputMap;
        List<TestGroup> tmp;
        if (Factory.B01.equalsIgnoreCase(factory) || Factory.A02.equalsIgnoreCase(factory)) {
            outputMap = new HashMap<>();
            tmp = new ArrayList<>();
        } else {
            tmp = sfcSmtGroupRepository.findByMoList(factory, customer, moList, groupList, fullTimeSpan.getEndDate())
                    .stream().map(map -> SfcSmtGroupRepository.mapping(factory, map)).collect(Collectors.toList());
            outputMap = tmp.stream()
                    .filter(group -> group.getGroupName().equalsIgnoreCase(groupMapByModel.get(group.getModelName())))
                    .collect(Collectors.toMap(TestGroup::getMo, TestGroup::getPass, (o1, o2) -> o1 + o2));
        }

        for (Map.Entry<String, Integer> entry : outputMap.entrySet()) {
            List<TestPlanMeta> moPlanList = planList.stream()
                    .filter(plan -> entry.getKey().equalsIgnoreCase(plan.getMo()))
                    .sorted(Comparator.comparing(TestPlanMeta::getStartDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            if (moPlanList.isEmpty()) {
                log.error("### sync output plan error {} {} {} {}", factory, entry.getKey(), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
                continue;
            }

//            int oldPlan = moPlanList.stream().mapToInt(TestPlanMeta::getPlan).sum();
            int output = tmp.stream()
                    .filter(group -> group.getMo().equalsIgnoreCase(entry.getKey()) &&
                            !group.getStartDate().before(fullTimeSpan.getStartDate()) && !group.getStartDate().after(fullTimeSpan.getEndDate()))
                    .mapToInt(TestGroup::getPass).sum();
            TestPlanMeta plan = moPlanList.get(0);
            plan.setOutput(output);
            plan.setTotalOutput(entry.getValue());
//            int newTotalOutput = plan.getTotal() - oldPlan + entry.getValue();
//            plan.setTotal(newTotalOutput);
            int newTotalOutput = entry.getValue();
//            plan.setTotal(newTotalOutput);

            testPlanService.savePlan(plan);

            List<TestPlanMeta> planMetaList = testPlanService.findByFactoryAndMo(factory, entry.getKey());
            if (!planMetaList.isEmpty()) {
                planMetaList.sort(Comparator.comparing(TestPlanMeta::getStartDate));
                for (TestPlanMeta planTmp : planMetaList) {
                    if (fullTimeSpan.getEndDate().before(planTmp.getStartDate())) {
                        newTotalOutput = newTotalOutput + planTmp.getPlan();
                        planTmp.setTotal(newTotalOutput);
                    }
                }

                testPlanService.saveAll(planMetaList);
            }
        }

        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }


    @RequestMapping("/model/plan/prd")
    public Map<String, Map<String, Map<String, List<TestPlanMeta>>>> getDetailPlanProduct(String factory, String sectionName, String modelName, TestPlanMeta.Type type, String timeSpan) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();

        if (type == TestPlanMeta.Type.MONTHLY) {
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            fullTimeSpan.setStartDate(calendar.getTime());
        }

        List<String> dateList = new ArrayList<>();
        for (Date tmp = fullTimeSpan.getStartDate(); tmp.before(fullTimeSpan.getEndDate()); ) {
            calendar.setTime(tmp);
            dateList.add(df.format(tmp));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tmp = calendar.getTime();
        }

        Map<String, Map<String, Map<String, List<TestPlanMeta>>>> result = testPlanService.getPlanList(factory, sectionName, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), type)
                .stream().sorted(Comparator.comparing(TestPlanMeta::getLineName).thenComparing(TestPlanMeta::getShiftTime)
                        .thenComparing(TestPlanMeta::getModelName).thenComparing(TestPlanMeta::getPlan, Comparator.reverseOrder()).thenComparing(TestPlanMeta::getDemand, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TestPlanMeta::getLineName, LinkedHashMap::new,
                        Collectors.groupingBy(plan -> df.format(plan.getShiftTime()), TreeMap::new,
                                Collectors.groupingBy(plan -> plan.getShift().toString()))));

        Map<String, List<TestPlanMeta>> defaultValue = new LinkedHashMap<>();
        defaultValue.put("DAY", Collections.emptyList());
        defaultValue.put("NIGHT", Collections.emptyList());
        result.forEach((line, lineMap) -> {
            for (String date : dateList) {
                if (!lineMap.containsKey(date)) {
                    lineMap.put(date, defaultValue);
                }
            }
        });

        return result;
    }

    @GetMapping("/model/plan/{id}")
    public TestPlanMeta getPlanModelName(@PathVariable int id) {
        return testPlanService.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("test plan meta %d not found", id)));
    }

    @RequestMapping("/model/plan")
    public List<TestPlanMeta> getDetailPlan(String factory, String sectionName, String modelName, TestPlanMeta.Type type, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        if (type == TestPlanMeta.Type.MONTHLY) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            fullTimeSpan.setStartDate(calendar.getTime());
        }
        return testPlanService.getPlanList(factory, sectionName, modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), type);
    }

    @PostMapping("/model/plan")
    public Boolean savePlanModelName(@RequestBody TestPlanMeta planMeta) {
        testPlanService.savePlan(planMeta);
        return true;
    }

    @PutMapping("/model/plan/{id}")
    public Boolean savePlanModelName(@PathVariable int id, @RequestBody TestPlanMeta planMeta) {
        TestPlanMeta plan = testPlanService.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("test plan meta %d not found", id)));

        int oldPlan = plan.getPlan();
        int oldTotalPlan = plan.getTotal();
        int oldTotalOutput = plan.getTotalOutput();
        BeanUtils.copyPropertiesIgnoreNull(planMeta, plan, "id");
        int newTotalPlan = plan.getTotal();
        int newTotalOutput = plan.getTotalOutput();

        if (oldPlan != plan.getPlan()) {
            newTotalPlan = oldTotalPlan - oldPlan + plan.getPlan();
            plan.setTotal(newTotalPlan);
        }
        testPlanService.savePlan(plan);

        if (oldTotalOutput != newTotalOutput || oldTotalPlan != newTotalPlan) {
            List<TestPlanMeta> planMetaList = testPlanService.findByFactoryAndMo(plan.getFactory(), plan.getMo());
            if (!planMetaList.isEmpty()) {
                planMetaList.sort(Comparator.comparing(TestPlanMeta::getStartDate));
                if (oldTotalOutput != newTotalOutput) {
                    for (TestPlanMeta planTmp : planMetaList) {
                        if (plan.getStartDate().before(planTmp.getStartDate())) {
                            newTotalOutput = newTotalOutput + planTmp.getPlan();
                            planTmp.setTotal(newTotalOutput);
                        }
                    }
                } else {
                    for (TestPlanMeta planTmp : planMetaList) {
                        if (plan.getStartDate().before(planTmp.getStartDate())) {
                            newTotalPlan = newTotalPlan + planTmp.getPlan();
                            planTmp.setTotal(newTotalPlan);
                        }
                    }
                }

                testPlanService.saveAll(planMetaList);
            }
        }

        return true;
    }

    @DeleteMapping("/model/plan/{id}")
    public Boolean deletePlan(@PathVariable int id) {
        TestPlanMeta plan = testPlanService.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("test plan meta %d not found", id)));

        testPlanService.deleteById(id);

        int newTotalPlan = plan.getTotal() - plan.getPlan();
        List<TestPlanMeta> planMetaList = testPlanService.findByFactoryAndMo(plan.getFactory(), plan.getMo());
        if (!planMetaList.isEmpty()) {
            planMetaList.sort(Comparator.comparing(TestPlanMeta::getStartDate));

            for (TestPlanMeta planTmp : planMetaList) {
                if (plan.getStartDate().before(planTmp.getStartDate())) {
                    newTotalPlan = newTotalPlan + planTmp.getPlan();
                    planTmp.setTotal(newTotalPlan);
                }
            }

            testPlanService.saveAll(planMetaList);
        }

        return true;
    }

    @RequestMapping("/email-list")
    public List<TestPcEmailList> getEmailList(String factory, String department, String group) {
        if (StringUtils.isEmpty(group)) {
            return testPcEmailListRepository.findByFactoryAndDepartment(factory, department);
        }
        return testPcEmailListRepository.findByFactoryAndDepartmentAndGroupName(factory, department, group);
    }

    @PostMapping("/email-list")
    public Boolean createEmailList(@RequestBody TestPcEmailList tmp) {
        if (!StringUtils.isEmpty(tmp.getEmail())) {
            tmp.setEmail(tmp.getEmail().toLowerCase());

            if (!CommonUtils.validateEmail(tmp.getEmail())) {
                return false;
            }

            TestPcEmailList email = testPcEmailListRepository.findByFactoryAndDepartmentAndGroupNameAndEmail(tmp.getFactory(), tmp.getDepartment(), tmp.getGroupName(), tmp.getEmail());
            if (email == null) {
                email = new TestPcEmailList();
            }
            BeanUtils.copyPropertiesIgnoreNull(tmp, email, "id");

            testPcEmailListRepository.save(email);
            return true;
        } else if (!StringUtils.isEmpty(tmp.getEmailList())) {
            tmp.setEmailList(tmp.getEmailList().toLowerCase());

            String[] emailList = tmp.getEmailList().split(",");
            if (emailList.length > 0) {
                for (String email : emailList) {
                    if (!CommonUtils.validateEmail(email)) {
                        continue;
                    }

                    TestPcEmailList emailExist = testPcEmailListRepository.findByFactoryAndDepartmentAndGroupNameAndEmail(tmp.getFactory(), tmp.getDepartment(), tmp.getGroupName(), email);
                    if (emailExist == null) {
                        emailExist = new TestPcEmailList();
                    }
                    BeanUtils.copyPropertiesIgnoreNull(tmp, emailExist, "id");
                    emailExist.setEmail(email);

                    testPcEmailListRepository.save(emailExist);
                }
                return true;
            }
        }

        return false;
    }

    @PutMapping("/email-list/{id}")
    public Boolean updatedEmailList(@PathVariable("id") Integer id, @RequestBody TestPcEmailList tmp) {
        if (!CommonUtils.validateEmail(tmp.getEmail())) {
            return false;
        }

        TestPcEmailList email = testPcEmailListRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("email id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(tmp, email, "id");
        testPcEmailListRepository.save(email);
        return true;
    }

    @DeleteMapping("/email-list/{id}")
    public Boolean removeEmailList(@PathVariable("id") Integer id) {
        testPcEmailListRepository.deleteById(id);
        return true;
    }

    @PostMapping(value = "/shortage/upload", produces = "text/csv; charset=utf-8")
    public void ciscoShortageUpload(
            String factory,
            @RequestPart MultipartFile ciscoShortageFile,
            @RequestPart MultipartFile missingPartFile,
            @RequestPart MultipartFile displayAmlFile,
            @RequestPart MultipartFile quotaArrangeFile,
            @RequestPart MultipartFile grStockFile,
            @RequestPart MultipartFile scheduleFile,
            HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=resource-value.xls");
            httpServletResponse.setCharacterEncoding("UTF-8");

            Path tmpPath = Paths.get(dataPath + "tmp/" + System.currentTimeMillis() + '-' + new Random().nextInt(100) + ".xlsx");
            Files.copy(new ClassPathResource("file/shortage.xlsx").getInputStream(), tmpPath, StandardCopyOption.REPLACE_EXISTING);
            XSSFWorkbook workbook = new XSSFWorkbook(tmpPath.toFile());

            Map<String, Map<String, Integer>> modelListMap = new HashMap<>();
            Map<String, Integer> materialQuotaMap = new HashMap<>();
            Map<String, String> materialAmlMap = new HashMap<>();
            Map<String, Map<String, Integer>> grStockMap = new HashMap<>();
            Map<String, String> scheduleMap = new HashMap<>();

            processCiscoShortageFile(ciscoShortageFile, workbook, modelListMap);

            processQuotaArrangeFile(quotaArrangeFile, materialQuotaMap);
            processDisplayAmlFile(displayAmlFile, materialQuotaMap, materialAmlMap);

            processGrStockFile(grStockFile, factory, grStockMap);
            processScheduleFile(scheduleFile, scheduleMap);

            processMissingPartFile(missingPartFile, workbook, modelListMap, materialAmlMap, grStockMap, scheduleMap);

            // evaluate formula
            FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);
            for (Sheet iSheet : workbook) {
                for (Row iRow : iSheet) {
                    for (Cell iCell : iRow) {
                        if (iCell.getCellType() == CellType.FORMULA) {
                            try {
                                evaluator.evaluateFormulaCell(iCell);
                            } catch (Exception e) {
                                log.error("### SD upload evaluateFormulaCell error", e);
                            }
                        }
                    }
                }
            }

            workbook.removeSheetAt(0);
//            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\tiennguyenduc\\Desktop\\sd-output.xlsx");
            workbook.write(httpServletResponse.getOutputStream());
            workbook.close();
            httpServletResponse.getOutputStream().close();
            Files.delete(tmpPath);

        } catch (Exception e) {
            log.error("### SD upload error", e);
            throw new CommonException(String.format("SD upload error %s %s", e.getCause(), e.getMessage()));
        }

//        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    private void processCiscoShortageFile(MultipartFile file, XSSFWorkbook workbook, Map<String, Map<String, Integer>> modelListMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            XSSFSheet sheetTemplate = workbook.getSheetAt(0);
            XSSFSheet sheet = workbook.createSheet("shortage");
            ConditionalFormattingRule rule = sheetTemplate.getSheetConditionalFormatting().getConditionalFormattingAt(0).getRule(0);

            // copy data
            ExcelUtils.copyRow(sheetTemplate, sheet, 0, 0, 2);
            sheet.createFreezePane(0, 2);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM");

            int i = 0;
            int ir = 2;
            String row;
            String part = "";
            StringBuilder models = new StringBuilder();
            while ((row = reader.readLine()) != null) {
                if (StringUtils.isEmpty(row)) {
                    continue;
                }

                String[] cells = row.split("\t");

                if ("Plan Date:".equalsIgnoreCase(cells[0])) {
                    for (int l = 1; l < cells.length; l++) {
                        if (StringUtils.isEmpty(cells[l])) {
                            continue;
                        }
                        calendar.setTime(df.parse(cells[l]));
                        if (calendar.get(Calendar.DAY_OF_WEEK) < 4) {
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            calendar.add(Calendar.WEEK_OF_YEAR, -1);
                        } else {
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        }
                        int k = 6;
                        for (int j = 0; j <= 12; j++) {
                            sheet.getRow(0).getCell(k).setCellValue(calendar.getTime());
                            sheet.getRow(1).getCell(k).setCellValue("Wk" + calendar.get(Calendar.WEEK_OF_YEAR));
                            k++;
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        }
                        break;
                    }
                    continue;
                }

                if (i == 0) {
                    if (!cells[1].trim().matches("\\d")) {
                        continue;
                    }
                    i = 1;
                }

                if (i == 2) {
                    if (cells[1].trim().matches("")) {
                        sheet.getRow(ir + 3).getCell(2).setCellValue(models.toString());
                        i = 1;
                        ir += 5;
                    }
                }

                if (i == 1) {
                    ExcelUtils.copyRow(sheetTemplate, sheet, 2, ir, 5);
                    CellRangeAddress[] range = {new CellRangeAddress(ir + 3, ir + 3, 3, 18)};

                    // copy style
                    sheet.getSheetConditionalFormatting().addConditionalFormatting(range, rule);
                    for (int l = 0; l < sheetTemplate.getNumMergedRegions(); l++) {
                        CellRangeAddress region = sheetTemplate.getMergedRegion(l);
                        if (region.getFirstRow() >= 2) {
                            sheet.addMergedRegion(new CellRangeAddress(region.getFirstRow() + ir - 2, region.getLastRow() + ir - 2, region.getFirstColumn(), region.getLastColumn()));
                        }
                    }

                    models = new StringBuilder();

                    int seq = Integer.parseInt(cells[1].trim());
                    part = cells[6].trim();
                    String desc = cells[7].trim();
                    int leadTime = 1;
                    if (!StringUtils.isEmpty(cells[8].trim())) {
                        leadTime = (int) Math.round(Double.parseDouble(cells[8].trim()));
                    }
                    String supplier = cells[9].trim();
                    String buyerCode = cells[10].trim();
                    models.append(String.format("/%s*%s ", cells[2].trim(), cells[11].trim()));
                    modelListMap.put(part, new LinkedHashMap<>());
                    modelListMap.get(part).put(cells[2].trim(), (int) Math.round(Double.parseDouble(cells[11].trim())));

                    int stock = 0;
                    if (cells.length > 13 && !StringUtils.isEmpty(cells[13].trim())) {
                        stock = (int) Math.round(Double.parseDouble(cells[13].trim()));
                    }
                    int grStock = 0;
                    if (cells.length > 14 && !StringUtils.isEmpty(cells[14].trim())) {
                        grStock = (int) Math.round(Double.parseDouble(cells[14].trim()));
                    }

                    sheet.getRow(ir).getCell(0).setCellValue(buyerCode);
                    sheet.getRow(ir).getCell(1).setCellValue(seq);
                    sheet.getRow(ir).getCell(2).setCellValue(part);
                    if (stock == 0) {
                        sheet.getRow(ir).getCell(4).setBlank();
                    } else {
                        sheet.getRow(ir).getCell(4).setCellValue(stock);
                    }
                    if (grStock == 0) {
                        sheet.getRow(ir).getCell(5).setBlank();
                    } else {
                        sheet.getRow(ir).getCell(5).setCellValue(grStock);
                    }
                    sheet.getRow(ir + 1).getCell(2).setCellValue(desc);
                    sheet.getRow(ir + 2).getCell(2).setCellValue(supplier);
                    sheet.getRow(ir + 4).getCell(2).setCellValue(leadTime);

                    i = 2;
                } else {
                    if (cells.length > 12 && "Requirements".equalsIgnoreCase(cells[12])) {
                        int k = 6;
                        for (int j = 15; j < cells.length && j <= 27; j++) {
                            if (sheet.getRow(ir + 1).getCell(k + 1) == null) {
                                sheet.getRow(ir + 1).createCell(k + 1);
                            }
                            if (StringUtils.isEmpty(cells[j])) {
                                sheet.getRow(ir + 1).getCell(k++).setBlank();
                            } else {
                                sheet.getRow(ir + 1).getCell(k++).setCellValue((int) Double.parseDouble(cells[j].trim()));
                            }
                        }
                    }
                    if (cells.length > 12 && "Supply(deliveries)".equalsIgnoreCase(cells[12])) {
                        int k = 6;
                        for (int j = 15; j < cells.length && j <= 27; j++) {
                            if (sheet.getRow(ir + 2).getCell(k + 1) == null) {
                                sheet.getRow(ir + 2).createCell(k + 1);
                            }
                            if (StringUtils.isEmpty(cells[j])) {
                                sheet.getRow(ir + 2).getCell(k++).setBlank();
                            } else {
                                sheet.getRow(ir + 2).getCell(k++).setCellValue((int) Double.parseDouble(cells[j].trim()));
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(cells[2])) {
                        models.append(String.format("/%s*%s ", cells[2].trim(), cells[11].trim()));
                        modelListMap.get(part).put(cells[2].trim(), (int) Math.round(Double.parseDouble(cells[11].trim())));
                    }
                }

                /*if (i == 0 || i == 2 || i == 3 || i == 4) {
                    i++;
                    continue;
                } else if (i == 1) {
                    calendar.setTime(df.parse(cells[1]));
                    if (calendar.get(Calendar.DAY_OF_WEEK) < 4) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    } else {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    }
                    int k = 6;
                    for (int j = 0; j <= 12; j++) {
                        if (sheet.getRow(ir).getCell(k) == null) {
                            sheet.getRow(ir).createCell(k);
                        }
                        sheet.getRow(ir).getCell(k).setCellValue(calendar.getTime());
                        sheet.getRow(ir + 1).getCell(k).setCellValue("Wk" + calendar.get(Calendar.WEEK_OF_YEAR));
                        k++;
                    }
                    i ++;
                    ir += 2;
//                } else if (i == 3) {
//                    int k = 6;
//                    for (int j = 12; j < cells.length && j <= 24; j++) {
//                        if (cells[j].matches(".+-Ju$")) {
//                            cells[j] = cells[j].replace("Ju", "Jun");
//                        }
//                        Date datetime = df2.parse(cells[j]);
//                        if (sheet.getRow(ir).getCell(k + 1) == null) {
//                            sheet.getRow(ir).createCell(k + 1);
//                        }
//                        sheet.getRow(ir).getCell(k++).setCellValue(datetime);
//                    }
//                    i++;
//                    ir++;
//                } else if (i == 4) {
//                    int k = 6;
//                    for (int j = 12; j < cells.length && j <= 24; j++) {
//                        if (sheet.getRow(ir).getCell(k + 1) == null) {
//                            sheet.getRow(ir).createCell(k + 1);
//                        }
//                        sheet.getRow(ir).getCell(k++).setCellValue(cells[j]);
//                    }
//                    i++;
//                    ir++;
                } else {
                    ExcelUtils.copyRow(sheetTemplate, sheet, 2, ir, 5);
                    CellRangeAddress[] range = {new CellRangeAddress(ir + 3, ir + 3, 3, 18)};
                    sheet.getSheetConditionalFormatting().addConditionalFormatting(range, rule);
                    StringBuilder models = new StringBuilder();

                    int seq = Integer.parseInt(cells[0]);
                    String part = cells[3];
                    String desc = cells[4];
                    String supplier = cells[6];
                    String buyerCode = cells[7];
                    models.append(String.format("/%s*%s ", cells[1], cells[8]));
                    int stock = 0;
                    if (cells.length > 10 && !StringUtils.isEmpty(cells[10])) {
                        stock = Integer.parseInt(cells[10]);
                    }
                    int grStock = 0;
                    if (cells.length > 11 && !StringUtils.isEmpty(cells[11])) {
                        grStock = Integer.parseInt(cells[11]);
                    }

                    sheet.getRow(ir).getCell(0).setCellValue(buyerCode);
                    sheet.getRow(ir).getCell(1).setCellValue(seq);
                    sheet.getRow(ir).getCell(2).setCellValue(part);
                    if (stock == 0) {
                        sheet.getRow(ir).getCell(4).setBlank();
                    } else {
                        sheet.getRow(ir).getCell(4).setCellValue(stock);
                    }
                    if (grStock == 0) {
                        sheet.getRow(ir).getCell(5).setBlank();
                    } else {
                        sheet.getRow(ir).getCell(5).setCellValue(grStock);
                    }
                    sheet.getRow(ir + 1).getCell(2).setCellValue(desc);
                    sheet.getRow(ir + 2).getCell(2).setCellValue(supplier);

                    for (int l = 1; l < 6; l++) {
                        if ((row = reader.readLine()) == null) {
                            break;
                        }
                        cells = row.split("\t");
                        if ("Requirements".equalsIgnoreCase(cells[9])) {
                            int k = 6;
                            for (int j = 12; j < cells.length && j <= 24; j++) {
                                if (sheet.getRow(ir + 1).getCell(k + 1) == null) {
                                    sheet.getRow(ir + 1).createCell(k + 1);
                                }
                                if (StringUtils.isEmpty(cells[j])) {
                                    sheet.getRow(ir + 1).getCell(k++).setBlank();
                                } else {
                                    sheet.getRow(ir + 1).getCell(k++).setCellValue((int) Double.parseDouble(cells[j]));
                                }
                            }
                        }
                        if ("Supply(deliveries)".equalsIgnoreCase(cells[9])) {
                            int k = 6;
                            for (int j = 12; j < cells.length && j <= 24; j++) {
                                if (sheet.getRow(ir + 2).getCell(k + 1) == null) {
                                    sheet.getRow(ir + 2).createCell(k + 1);
                                }
                                if (StringUtils.isEmpty(cells[j])) {
                                    sheet.getRow(ir + 2).getCell(k++).setBlank();
                                } else {
                                    sheet.getRow(ir + 2).getCell(k++).setCellValue((int) Double.parseDouble(cells[j]));
                                }
                            }
                        }
                        if (!StringUtils.isEmpty(cells[1])) {
                            models.append(String.format("/%s*%s ", cells[1], cells[8]));
                        }
                    }

                    sheet.getRow(ir + 3).getCell(2).setCellValue(models.toString());

                    // copy style
                    for (int l = 0; l < sheetTemplate.getNumMergedRegions(); l++) {
                        CellRangeAddress region = sheetTemplate.getMergedRegion(l);
                        if (region.getFirstRow() >= 2) {
                            sheet.addMergedRegion(new CellRangeAddress(region.getFirstRow() + ir - 2, region.getLastRow() + ir - 2, region.getFirstColumn(), region.getLastColumn()));
                        }
                    }

                    i += 6;
                    ir += 5;
                }*/
            }
        } catch (Exception e) {
            log.error("### process cisco shortage file error", e);
            throw new CommonException(String.format("process cisco shortage file %s %s", e.getCause(), e.getMessage()));
        }
    }

    private void processQuotaArrangeFile(MultipartFile file, Map<String, Integer> materialQuotaMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            int i = 0;
            String row;
            while ((row = reader.readLine()) != null) {
                i++;
                if (StringUtils.isEmpty(row) || i < 5) {
                    continue;
                }
                String[] cells = row.split("\t");
                if (cells.length >= 25) {
                    materialQuotaMap.put(cells[25].trim(), Integer.parseInt(cells[14].trim()));
                }
            }
        } catch (Exception e) {
            log.error("### process quota arrange file error", e);
            throw new CommonException(String.format("process quota arrange file %s %s", e.getCause(), e.getMessage()));
        }
    }

    private void processDisplayAmlFile(MultipartFile file, Map<String, Integer> materialQuotaMap, Map<String, String> materialAmlMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            int i = 0;
            String row;
            while ((row = reader.readLine()) != null) {
                i++;
                if (StringUtils.isEmpty(row) || i < 7) {
                    continue;
                }
                String[] cells = row.split("\t");
                if (cells.length >= 14) {
                    String aml = materialAmlMap.getOrDefault(cells[1].trim(), "");
                    int quota = materialQuotaMap.getOrDefault(cells[2].trim(), 0);
                    if (quota != 0) {
                        aml += String.format("%s%d%%", cells[5].trim(), quota);
                        materialAmlMap.put(cells[1].trim(), aml);
                    }
                }
            }
        } catch (Exception e) {
            log.error("### process quota arrange file error", e);
            throw new CommonException(String.format("process quota arrange file %s %s", e.getCause(), e.getMessage()));
        }
    }

    private void processGrStockFile(MultipartFile file, String factory, Map<String, Map<String, Integer>> grStockMap) {
        try {
            if (file == null) {
                throw CommonException.of("gr stock file is blank");
            }

            String extension = CommonUtils.getExtension(file.getOriginalFilename());
            Workbook workbook;
            if ("xls".equalsIgnoreCase(extension)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(extension)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("gr stock file is not xls or xlsx file");
            }

            for (Sheet sheet : workbook) {
                if (!"VMI".equalsIgnoreCase(sheet.getSheetName())) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        if ((Factory.B06.equalsIgnoreCase(factory) && ExcelUtils.getStringValue(sheet.getRow(i).getCell(3)).startsWith("1230"))
                                || (Factory.B04.equalsIgnoreCase(factory) && ExcelUtils.getStringValue(sheet.getRow(i).getCell(3)).startsWith("1730"))) {
                            Map<String, Integer> map = grStockMap.getOrDefault(ExcelUtils.getStringValue(sheet.getRow(i).getCell(2)), new LinkedHashMap<>());
                            map.put(sheet.getSheetName(), ExcelUtils.getIntegerValue(sheet.getRow(i).getCell(5)));
                            grStockMap.put(ExcelUtils.getStringValue(sheet.getRow(i).getCell(2)), map);
                        }
                    }
                } else {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Map<String, Integer> map = grStockMap.getOrDefault(ExcelUtils.getStringValue(sheet.getRow(i).getCell(6)), new LinkedHashMap<>());
                        map.put(sheet.getSheetName(), ExcelUtils.getIntegerValue(sheet.getRow(i).getCell(9)));
                        grStockMap.put(ExcelUtils.getStringValue(sheet.getRow(i).getCell(6)), map);
                    }
                }
            }

        } catch (Exception e) {
            log.error("### process quota arrange file error", e);
            throw new CommonException(String.format("process quota arrange file %s %s", e.getCause(), e.getMessage()));
        }
    }

    private void processScheduleFile(MultipartFile file, Map<String, String> scheduleMap) {
        try {
            if (file == null) {
                throw CommonException.of("schedule file is blank");
            }

            String extension = CommonUtils.getExtension(file.getOriginalFilename());
            Workbook workbook;
            if ("xls".equalsIgnoreCase(extension)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(extension)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw CommonException.of("schedule file is not xls or xlsx file");
            }

            Sheet sheet = workbook.getSheet("Schedule");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                scheduleMap.put(ExcelUtils.getStringValue(sheet.getRow(i).getCell(0)), ExcelUtils.getStringValue(sheet.getRow(i).getCell(1)));
            }

        } catch (Exception e) {
            log.error("### process quota arrange file error", e);
            throw new CommonException(String.format("process quota arrange file %s %s", e.getCause(), e.getMessage()));
        }
    }

    private void processMissingPartFile(MultipartFile file, XSSFWorkbook workbook,
                                        Map<String, Map<String, Integer>> modelListMap, Map<String, String> materialAmlMap,
                                        Map<String, Map<String, Integer>> grStockMap, Map<String, String> scheduleMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            XSSFSheet sheet = workbook.createSheet("missing-part");

            Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
            headerIndexMap.put("No.", -1);
            headerIndexMap.put("Material", -1);
            headerIndexMap.put("Description", -1);
            headerIndexMap.put("StockQty", -1);
            headerIndexMap.put("Required", -1);
            headerIndexMap.put("Order Res(C", -1);
            headerIndexMap.put("Order Res(R", -1);
            headerIndexMap.put("QI Qty", -1);
            headerIndexMap.put("Buyer Code", -1);

            Set<String> models = new HashSet<>();
            modelListMap.values().forEach(e -> models.addAll(e.keySet()));

            Set<String> grs = new HashSet<>();
            grStockMap.values().forEach(e -> grs.addAll(e.keySet()));

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setRotation((short) 90);

            int i = 0;
            int ir = 0;
            String row;
            while ((row = reader.readLine()) != null) {
                if (StringUtils.isEmpty(row)) {
                    continue;
                }

                String[] cells = row.split("\t");
                if ("No.".equals(cells[0].trim())) {
                    int k = 0;
                    Row iRow = sheet.createRow(ir++);
                    iRow.setHeight((short) 1500);
                    for (int j = 0; j < cells.length; j++) {
                        for (Map.Entry<String, Integer> headerEntry : headerIndexMap.entrySet()) {
                            if (headerEntry.getKey().equalsIgnoreCase(cells[j])) {
                                headerEntry.setValue(j);
                                iRow.createCell(k++).setCellValue(headerEntry.getKey());
                            }
                        }
                    }
                    sheet.setColumnWidth(1, (short) 4000);
                    for (String model : models) {
                        sheet.setColumnWidth(k, (short) 1000);
                        Cell cell = iRow.createCell(k++);
                        cell.setCellStyle(headerStyle);
                        cell.setCellValue(model);
                    }
                    iRow.createCell(k++).setCellValue("AML");
                    iRow.createCell(k++).setCellValue("Buyer cfm");
                    for (String gr : grs) {
                        iRow.createCell(k++).setCellValue(gr + " Qty");
                    }
                    iRow.createCell(k++).setCellValue("Total");
                    sheet.createFreezePane(2, 1);

                    i = 1;
                    continue;
                } else if (i == 0) {
                    continue;
                }

                if (!cells[0].matches("\\d+")) {
                    continue;
                }

                String part = cells[headerIndexMap.get("Material")];
                int k = 0;
                Row iRow = sheet.createRow(ir++);
                for (Map.Entry<String, Integer> headerEntry : headerIndexMap.entrySet()) {
                    String value = cells[headerEntry.getValue()].trim();
                    if (value.matches("^(\\d+,*\\d*\\.*\\d*)$")) {
                        iRow.createCell(k++).setCellValue(Double.parseDouble(value.replace(",", "")));
                    } else {
                        iRow.createCell(k++).setCellValue(value);
                    }
                }

                for (String model : models) {
                    if (modelListMap.containsKey(part)) {
                        iRow.createCell(k++).setCellValue(modelListMap.get(part).getOrDefault(model, 0));
                    } else {
                        log.debug("process missing part error {}", part);
                        iRow.createCell(k++).setCellValue(0);
                    }
                }

                iRow.createCell(k++).setCellValue(materialAmlMap.getOrDefault(part, ""));
                iRow.createCell(k++).setCellValue(scheduleMap.getOrDefault(part, ""));
                int total = 0;
                for (String gr : grs) {
                    if (grStockMap.containsKey(part)) {
                        int qty = grStockMap.get(part).getOrDefault(gr, 0);
                        total += qty;
                        iRow.createCell(k++).setCellValue(qty);
                    } else {
                        iRow.createCell(k++).setCellValue(0);
                    }
                }
                iRow.createCell(k++).setCellValue(total);
            }
        } catch (Exception e) {
            log.error("### process cisco shortage file error", e);
            throw new CommonException(String.format("process cisco shortage file %s %s", e.getCause(), e.getMessage()));
        }
    }

    @PostMapping("/sd/upload")
    public CommonResponse<Boolean> sdUpload(@RequestPart MultipartFile file, String factory) {
        try {
            if (file == null) {
                throw CommonException.of("SD upload file is blank");
            }

            if (!"xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                throw CommonException.of("SD upload file is not xls or xlsx file");
            }

            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);

            Map<String, Shortage> data = new HashMap<>();
            Map<String, Integer> indexCellMap = new HashMap<>();
            indexCellMap.put("Order_Res", -1);
            indexCellMap.put("Stock_Qty", -1);
            indexCellMap.put("Total", -1);
            indexCellMap.put("Buyer_Code", -1);
            indexCellMap.put("Buyer_Name", -1);
            indexCellMap.put("L/T", -1);
            indexCellMap.put("AML", -1);
            indexCellMap.put("Buyer cfm", -1);

            Map<String, List<String>> modelIndexCellMap = new HashMap<>();
            Map<String, Integer> partIndexCellMap = new HashMap<>();

            // process stock sheet
            Sheet stockSheet = workbook.getSheetAt(0);
            int header = -1;
            int model = 0;
            for (int i = 0; i <= stockSheet.getLastRowNum(); i++) {
                if (header == -1 && !"Part_No".equalsIgnoreCase(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(0)))) {
                    continue;
                }

                if (header == -1) {
                    header = 0;
                }
                if (header == 0) {
                    for (int j = 2; j < stockSheet.getRow(i).getLastCellNum(); j++) {
                        for (Map.Entry<String, Integer> indexCellEntry : indexCellMap.entrySet()) {
                            if (indexCellEntry.getKey().equalsIgnoreCase(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(j)).trim())) {
                                indexCellEntry.setValue(j);
                                if (model == 0) {
                                    model = j;
                                }
                            }
                        }
                        if (model == 0) {
                            modelIndexCellMap.put(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(j)), new ArrayList<>());
                        }
                    }
                    header = i;
                } else {
                    Shortage shortage = new Shortage();
                    shortage.setPart(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(0)));

                    for (Map.Entry<String, Integer> indexCellEntry : indexCellMap.entrySet()) {
                        switch (indexCellEntry.getKey()) {
                            case "Stock_Qty":
                                shortage.setStock(ExcelUtils.getIntegerValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                            case "Total":
                                if (stockSheet.getRow(i).getCell(indexCellEntry.getValue()).getCellType() == CellType.FORMULA) {
                                    shortage.setGrStock(ExcelUtils.getIntegerValue(evaluator.evaluateInCell(stockSheet.getRow(i).getCell(indexCellEntry.getValue()))));
                                } else {
                                    shortage.setGrStock(ExcelUtils.getIntegerValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                }
                                break;
                            case "Buyer_Name"/*"Buyer_Code"*/:
                                shortage.setBuyerCode(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                            case "Buyer_Code"/*"Buyer_Name"*/:
                                shortage.setBuyerName(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                            case "L/T":
                                shortage.setPartLeadTime(ExcelUtils.getDoubleValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                            case "AML":
                                shortage.setPartCompany(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                            case "Buyer cfm":
                                shortage.setActions(ExcelUtils.getStringValue(stockSheet.getRow(i).getCell(indexCellEntry.getValue())));
                                break;
                        }
                    }

                    for (int j = 2; j < model; j++) {
                        if (stockSheet.getRow(i).getCell(j) != null &&
                                stockSheet.getRow(i).getCell(j).getCellType() == CellType.NUMERIC &&
                                stockSheet.getRow(i).getCell(j).getNumericCellValue() >= 1) {
                            shortage.getModelList().add(ExcelUtils.getStringValue(stockSheet.getRow(header).getCell(j)));
                            modelIndexCellMap.get(ExcelUtils.getStringValue(stockSheet.getRow(header).getCell(j))).add(String.join("_", shortage.getBuyerCode(), shortage.getPart()));
                        }
                    }

                    data.put(String.join("_", shortage.getBuyerCode(), shortage.getPart()), shortage);
                }
            }

            // generate SD sheet from plan sheet
            evaluator.clearAllCachedResultValues();
            if (workbook.getSheetIndex("SD") != -1) {
                workbook.removeSheetAt(workbook.getSheetIndex("SD"));
            }
            Sheet output = workbook.cloneSheet(1, "SD");
            workbook.setSheetName(workbook.getSheetIndex(output), "SD");
            Map<String, Integer> weekMap = new HashMap<>();
            for (int i = 6; i < output.getRow(0).getLastCellNum(); i++) {
                weekMap.put(ExcelUtils.getStringValue(output.getRow(0).getCell(i)), i);
            }

            for (int i = 2; i < output.getLastRowNum(); i += 5) {
                if (output.getRow(i) == null || output.getRow(i).getCell(0) == null) {
                    continue;
                }

                String buyerCode = ExcelUtils.getStringValue(output.getRow(i).getCell(0));
                String part = ExcelUtils.getStringValue(output.getRow(i).getCell(2));
                partIndexCellMap.put(String.join("_", buyerCode, part), i);

                if (data.containsKey(String.join("_", buyerCode, part))) {
                    Shortage shortage = data.get(String.join("_", buyerCode, part));
                    output.getRow(i).getCell(0).setCellValue(shortage.getBuyerName());
                    output.getRow(i).getCell(5).setCellValue(shortage.getGrStock());
                    output.getRow(i + 2).getCell(2).setCellValue(shortage.getPartCompany());
                    output.getRow(i + 4).getCell(2).setCellValue(shortage.getPartLeadTime());
                    output.getRow(i + 4).getCell(4).setCellValue(shortage.getActions());

                    Map<String, Integer> actionMap = shortage.getActionMap();
                    for (Map.Entry<String, Integer> actionEntry : actionMap.entrySet()) {
                        if (weekMap.containsKey(actionEntry.getKey())) {
                            output.getRow(i + 2).getCell(weekMap.get(actionEntry.getKey())).setCellValue(actionEntry.getValue());
                        }
                    }

                    try {
                        Cell value = evaluator.evaluateInCell(output.getRow(i + 3).getCell(11));
                        if (value != null) {
                            shortage.setShortage6w(shortage.getShortage6w() + ExcelUtils.getIntegerValue(value));
                        }

                        value = evaluator.evaluateInCell(output.getRow(i + 3).getCell(15));
                        if (value != null) {
                            shortage.setShortage10w(shortage.getShortage10w() + ExcelUtils.getIntegerValue(value));
                        }
                    } catch (Exception e) {
                        log.error("### error {}", i, e);
                    }
                }
            }

            // copy SD by model
            CellStyle style = output.getRow(2).getCell(0).getCellStyle();
            CellStyle redStyle = workbook.createCellStyle();
            redStyle.cloneStyleFrom(style);
            redStyle.setFillForegroundColor(IndexedColors.RED.index);
            CellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.cloneStyleFrom(style);
            yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            CellStyle greenStyle = workbook.createCellStyle();
            greenStyle.cloneStyleFrom(style);
            greenStyle.setFillForegroundColor(IndexedColors.GREEN.index);

            for (Map.Entry<String, List<String>> modelEntry : modelIndexCellMap.entrySet()) {
                if (workbook.getSheetIndex(modelEntry.getKey()) != -1) {
                    workbook.removeSheetAt(workbook.getSheetIndex(modelEntry.getKey()));
                }
                Sheet newSheet = workbook.createSheet(modelEntry.getKey());
                SheetConditionalFormatting formatting = output.getSheetConditionalFormatting();
                for (int i = 0; i < formatting.getNumConditionalFormattings(); i++) {
                    newSheet.getSheetConditionalFormatting().addConditionalFormatting(formatting.getConditionalFormattingAt(i));
                }

                // copy data
                ExcelUtils.copyRow(output, newSheet, 0, 0, 2);
                newSheet.createFreezePane(0, 2);

                modelEntry.getValue().sort(Comparator.comparingInt(part -> data.containsKey(part) ? data.get(part).getStatus() : Integer.MAX_VALUE)
                        .thenComparingInt(part -> data.containsKey(part) ? data.get(part).getShortageTotal() : Integer.MAX_VALUE));

                int ir = 2;
                for (String part : modelEntry.getValue()) {
                    if (modelEntry.getValue().contains(part)) {
                        if (partIndexCellMap.containsKey(part)) {
                            ExcelUtils.copyRow(output, newSheet, partIndexCellMap.get(part), ir, 5);
                            if (data.get(part).getStatus() == 0) {
                                newSheet.getRow(ir).getCell(0).setCellStyle(redStyle);
                            } else if (data.get(part).getStatus() == 1) {
                                newSheet.getRow(ir).getCell(0).setCellStyle(yellowStyle);
                            } else {
                                newSheet.getRow(ir).getCell(0).setCellStyle(greenStyle);
                            }
                            ir += 5;
                        } else {
                            log.debug("### error {} {}", modelEntry.getKey(), part);
                        }
                    }
                }

                // copy style
                for (int k = 0; k < output.getNumMergedRegions(); k++) {
                    CellRangeAddress region = output.getMergedRegion(k);
                    if (region.getLastRow() <= ir) {
                        newSheet.addMergedRegion(region);
                    }
                }
            }

            // evaluate formula
            evaluator.clearAllCachedResultValues();
            for (Sheet iSheet : workbook) {
                for (Row row : iSheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.FORMULA) {
                            try {
                                evaluator.evaluateFormulaCell(cell);
                            } catch (Exception e) {
                                log.error("### SD upload evaluateFormulaCell error", e);
                            }
                        }
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\tiennguyenduc\\Desktop\\sd-output.xlsx");
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            log.debug("### shortage data {}", data.size());

        } catch (Exception e) {
            log.error("### SD upload error", e);
            throw new CommonException(String.format("SD upload error %s %s", e.getCause(), e.getMessage()));
        }
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

}
