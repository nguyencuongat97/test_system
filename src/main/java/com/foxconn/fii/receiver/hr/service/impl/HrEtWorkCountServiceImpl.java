package com.foxconn.fii.receiver.hr.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.receiver.hr.service.HrEmployeeTrackingService;
import com.foxconn.fii.receiver.hr.service.HrEtEmployeeInfoService;
import com.foxconn.fii.receiver.hr.service.HrEtWorkCountService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrEtWorkCountServiceImpl implements HrEtWorkCountService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private HrEmployeeTrackingWorkShiftMetaRepository hrEmployeeTrackingWorkShiftMetaRepository;

    @Autowired
    private HrEmployeeTrackingWorkResultMetaRepository hrEmployeeTrackingWorkResultMetaRepository;

    @Autowired
    private HrEmployeeTrackingOfficeUnitInfoRepository hrEmployeeTrackingOfficeUnitInfoRepository;

    @Autowired
    private HrEmployeeTrackingPersonInfoRepository hrEmployeeTrackingPersonInfoRepository;

    @Autowired
    private HrEmployeeTrackingPersonWorkingStatusRepository hrEmployeeTrackingPersonWorkingStatusRepository;

    @Autowired
    private HrEtEmployeeInfoService employeeInfoService;

    @Autowired
    private HrEmployeeTrackingService hrEmployeeTrackingService;

    @Autowired
    private HrEmployeeInfoEatRiceRepository hrEmployeeInfoEatRiceRepository;

    @Autowired
    private HrEtBackupDutyRepository hrEtBackupDutyRepository;

    @Value("${path.data}")
    private String dataPath;

    @Override
    public File excelMonthlyReport(String fileName, Map<String, Object> workData) throws IOException {
        if (workData.isEmpty()) {
            return null;
        }

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        HSSFWorkbook workbook = new HSSFWorkbook();

        fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-" + fileName;

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle normalStyle = createStyle(workbook);

        HSSFCellStyle abnormalStyle = createStyle(workbook);
        HSSFFont abnormalFont = workbook.createFont();
        abnormalFont.setFontName("Times New Roman");
        abnormalFont.setColor(IndexedColors.RED.getIndex());
        abnormalStyle.setFont(abnormalFont);

        HSSFCellStyle restDayStyle = createStyle(workbook);
        restDayStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        restDayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFSheet sheet = workbook.createSheet("Làm Việc");
        int rowNum = 0;
        int cellNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("STT");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Mã nhân viên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        List<Integer> dayOfMonth = (List<Integer>) workData.get("dayOfMonth");

        for (Integer day : dayOfMonth) {
            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(day);
            cell.setCellStyle(titleStyle);
            cellNum++;
        }

        List<String> labelList = (List<String>) workData.get("labelList");
        for (String label : labelList) {
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(label);
            cell.setCellStyle(titleStyle);
            cellNum++;
        }

        int indexCount = 0;

        Map<String, Map<String, Object>> workCountData = (Map<String, Map<String, Object>>) workData.get("data");

        for (Map.Entry<String, Map<String, Object>> entry : workCountData.entrySet()) {
            rowNum++;
            row = sheet.createRow(rowNum);
            cellNum = 0;
            indexCount++;

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(indexCount);
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) entry.getValue().get("userId"));
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) entry.getValue().get("userName"));
            cell.setCellStyle(normalStyle);
            cellNum++;

            List<DailyResult> workResultList = (List<DailyResult>) entry.getValue().get("workResultList");

            for (DailyResult dailyResult : workResultList) {
                cell = row.createCell(cellNum, CellType.STRING);
                if (dailyResult.getWorkResult().equalsIgnoreCase("NULL")) {
                    cell.setCellValue("noData");
                } else {
                    cell.setCellValue(dailyResult.getWorkResult());
                }
                if (dailyResult.getWorkResult().equalsIgnoreCase("L")) {
                    cell.setCellStyle(restDayStyle);
                } else if (dailyResult.getWorkResult().equalsIgnoreCase("LV")) {
                    cell.setCellStyle(normalStyle);
                } else {
                    cell.setCellStyle(abnormalStyle);
                }
                cellNum++;
            }

            Map<String, Double> totalCountResult = (Map<String, Double>) entry.getValue().get("totalCountResult");
            for (Map.Entry<String, Double> countResult : totalCountResult.entrySet()) {
                cell = row.createCell(cellNum, CellType.NUMERIC);
                cell.setCellValue(countResult.getValue());
                cell.setCellStyle(normalStyle);
                cellNum++;
            }

            rowNum++;
            row = sheet.createRow(rowNum);
            cellNum = 0;

            for (int i = 1; i <= 3; i++) {
                cell = row.createCell(cellNum, CellType.STRING);
                cell.setCellValue("");
                cell.setCellStyle(normalStyle);
                cellNum++;
            }

            double totalCa3Time = 0;

            for (DailyResult dailyResult : workResultList) {
                cell = row.createCell(cellNum, CellType.NUMERIC);
                cell.setCellValue(dailyResult.getCa3Time());
                cell.setCellStyle(normalStyle);
                cellNum++;

                totalCa3Time += dailyResult.getCa3Time();
            }

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(totalCa3Time);
            cell.setCellStyle(normalStyle);
            cellNum++;

        }

        for (int i = 0; i < cellNum + 18; i++) {
            sheet.autoSizeColumn(i);
        }

        //END SHEET ONE

        sheet = workbook.createSheet("Tăng ca");
        rowNum = 0;
        cellNum = 0;

        row = sheet.createRow(rowNum);

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("STT");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Mã nhân viên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        for (Integer day : dayOfMonth) {
            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(day);
            cell.setCellStyle(titleStyle);
            cellNum++;
        }

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Tổng cộng");
        cell.setCellStyle(titleStyle);
        cellNum++;

        indexCount = 0;

        for (Map.Entry<String, Map<String, Object>> entry : workCountData.entrySet()) {
            rowNum++;
            row = sheet.createRow(rowNum);
            cellNum = 0;
            indexCount++;

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(indexCount);
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) entry.getValue().get("userId"));
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) entry.getValue().get("userName"));
            cell.setCellStyle(normalStyle);
            cellNum++;

            List<DailyResult> workResultList = (List<DailyResult>) entry.getValue().get("workResultList");

            double totalNormalOtTime = 0;

            for (DailyResult dailyResult : workResultList) {
                cell = row.createCell(cellNum, CellType.NUMERIC);
                if (dailyResult.getNormalOtTime() > 0) {
                    cell.setCellValue(dailyResult.getNormalOtTime());
                    totalNormalOtTime += dailyResult.getNormalOtTime();
                }
                cell.setCellStyle(normalStyle);
                cellNum++;
            }

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(totalNormalOtTime);
            cell.setCellStyle(normalStyle);
            cellNum++;

            rowNum++;
            row = sheet.createRow(rowNum);
            cellNum = 0;

            for (int i = 1; i <= 3; i++) {
                cell = row.createCell(cellNum, CellType.STRING);
                cell.setCellValue("");
                cell.setCellStyle(normalStyle);
                cellNum++;
            }

            double ca3NormalOtTime = 0;

            for (DailyResult dailyResult : workResultList) {
                cell = row.createCell(cellNum, CellType.NUMERIC);
                if (dailyResult.getCa3OtTime() > 0) {
                    cell.setCellValue(dailyResult.getCa3OtTime());
                    ca3NormalOtTime += dailyResult.getCa3OtTime();
                }
                cell.setCellStyle(normalStyle);
                cellNum++;
            }

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(ca3NormalOtTime);
            cell.setCellStyle(normalStyle);
            cellNum++;
        }

        for (int i = 0; i < cellNum; i++) {
            sheet.autoSizeColumn(i);
        }

        //END SHEET TWO

        sheet = workbook.createSheet("Đi muộn về sớm");
        rowNum = 0;
        cellNum = 0;

        row = sheet.createRow(rowNum);

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("STT");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Mã nhân viên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Ngày tháng");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Giờ đi làm");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Giờ đi ăn cơm");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Giờ ăn xong vào xưởng");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Giờ tan ca");
        cell.setCellStyle(titleStyle);
        cellNum++;

        cell = row.createCell(cellNum, CellType.STRING);
        cell.setCellValue("Thời gian đi muộn/về sớm (phút)");
        cell.setCellStyle(titleStyle);
        cellNum++;

        List<DailyResult> earlyLateList = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : workCountData.entrySet()) {
            List<DailyResult> workResultList = (List<DailyResult>) entry.getValue().get("workResultList");
            for (DailyResult dailyResult : workResultList) {
                if (dailyResult.getEarlyLateTime() > 0D) {
                    earlyLateList.add(dailyResult);
                }
            }
        }

        earlyLateList = earlyLateList.stream().sorted((e1, e2) -> e1.getWorkDate().compareTo(e2.getWorkDate())).collect(Collectors.toList());

        indexCount = 0;

        for (DailyResult dailyResult : earlyLateList) {
            rowNum++;
            row = sheet.createRow(rowNum);
            cellNum = 0;
            indexCount++;

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(indexCount);
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dailyResult.getCardId());
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dailyResult.getNameCn());
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(sdf.format(dailyResult.getWorkDate()));
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            if (dailyResult.getBeginWork() == null) {
                cell.setCellValue("NULL");
            } else {
                cell.setCellValue(sdfTime.format(dailyResult.getBeginWork()));
            }
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            if (dailyResult.getBeginRest() == null) {
                cell.setCellValue("NULL");
            } else {
                cell.setCellValue(sdfTime.format(dailyResult.getBeginRest()));
            }
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            if (dailyResult.getEndRest() == null) {
                cell.setCellValue("NULL");
            } else {
                cell.setCellValue(sdfTime.format(dailyResult.getEndRest()));
            }
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            if (dailyResult.getEndWork() == null) {
                cell.setCellValue("NULL");
            } else {
                cell.setCellValue(sdfTime.format(dailyResult.getEndWork()));
            }
            cell.setCellStyle(normalStyle);
            cellNum++;

            cell = row.createCell(cellNum, CellType.NUMERIC);
            cell.setCellValue(dailyResult.getEarlyLateTime());
            cell.setCellStyle(normalStyle);
            cellNum++;

        }

        for (int i = 0; i < cellNum; i++) {
            sheet.autoSizeColumn(i);
        }

        //END SHEET THREE

        File file = new File(dataPath + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();

        return file;
    }

    @Override
    public List<Map<String, String>> dutyData(String cardId, String departName, int year, int month, int startDay, int endDay) throws ParserConfigurationException, IOException, SAXException {
        Map<String, String> jsDataMap = new HashMap<>();
        jsDataMap.put("EmpNo", cardId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month - 1);
        if (startDay > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, startDay);
        }
        jsDataMap.put("DutyDate_S", sdf.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (endDay > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, endDay);
        }
        jsDataMap.put("DutyDate_E", sdf.format(calendar.getTime()));

        jsDataMap.put("DepartName", departName);
        String jsData = objectMapper.writeValueAsString(jsDataMap);

        String url = "http://10.224.69.75:8006/ClockRecordService/ClockRecordService.asmx/Get_DutyStatus";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("JSData", jsData);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));
        NodeList nodeList = document.getElementsByTagName("string");
        List<Map<String, String>> dutyDataList = objectMapper.readValue(nodeList.item(0).getTextContent(), new TypeReference<List<Map<String, String>>>(){});

        return dutyDataList;
    }

    @Override
    public List<Map<String, String>> overtimeData(String cardId, String departName, int year, int month, int startDay, int endDay) throws IOException, ParserConfigurationException, SAXException {

        Map<String, String> jsDataMap = new HashMap<>();
        jsDataMap.put("EmpNo", cardId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month - 1);
        if (startDay > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, startDay);
        }
        jsDataMap.put("DutyDate_S", sdf.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (endDay > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, endDay);
        }
        jsDataMap.put("DutyDate_E", sdf.format(calendar.getTime()));

        jsDataMap.put("DepartName", departName);
        String jsData = objectMapper.writeValueAsString(jsDataMap);

        String url = "http://10.224.69.75/ClockRecordService.asmx/Get_OTData";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("JSData", jsData);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(responseEntity.getBody())));
        NodeList nodeList = document.getElementsByTagName("string");
        List<Map<String, String>> overtimeData = objectMapper.readValue(nodeList.item(0).getTextContent(), new TypeReference<List<Map<String, String>>>(){});

        return overtimeData;

    }

    @Override
    public Double usedFreeDuty(String empNo, Calendar calendar) throws ParseException {

        HrEmployeeTrackingPersonInfo personInfo = hrEmployeeTrackingPersonInfoRepository.findTop1ByEmpNo(empNo).orElse(null);

        if (personInfo == null) {
            return null;
        }

        List<HrEmployeeTrackingWorkShiftMeta> shiftMetaList = hrEmployeeTrackingWorkShiftMetaRepository.findAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfWorkDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfWorkTime = new SimpleDateFormat("HH:mm:ss");

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(calendar.getTime());
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        if (startCal.get(Calendar.MONTH) >= 2) {
            startCal.set(Calendar.MONTH, 2);
        } else {
            startCal.set(Calendar.MONTH, 2);
            startCal.add(Calendar.YEAR, -1);
        }

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(calendar.getTime());
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        endCal.add(Calendar.MONTH, -1);
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);

        double result = 0;

        List<HrEtBackupDuty> backupDutyList = hrEtBackupDutyRepository.findAllByEmployeeNoAndWorkDateBetween(personInfo.getEmpNo(), startCal.getTime(), endCal.getTime());

        for (HrEtBackupDuty backupDuty : backupDutyList) {
            if (backupDuty.getWorkResult().equalsIgnoreCase("年休假")) {
                if (StringUtils.isEmpty(backupDuty.getEmployeeClassNo())) {
                    continue;
                }

                HrEmployeeTrackingWorkShiftMeta shiftMeta = shiftMetaList.stream().filter(e -> !StringUtils.isEmpty(e.getShiftCode()) && e.getShiftCode().equalsIgnoreCase(backupDuty.getEmployeeClassNo())).findFirst().orElse(null);

                if (shiftMeta == null || shiftMeta.getBeginWork() == null || shiftMeta.getBeginRest() == null || shiftMeta.getEndRest() == null || shiftMeta.getEndWork() == null) {
                    continue;
                }

                Date shiftBeginWork = sdf.parse(sdfWorkDate.format(backupDuty.getWorkDate()) + " " + sdfWorkTime.format(shiftMeta.getBeginWork()));
                Date shiftBeginRest = sdf.parse(sdfWorkDate.format(backupDuty.getWorkDate()) + " " + sdfWorkTime.format(shiftMeta.getBeginWork()));
                Date shiftEndRest = sdf.parse(sdfWorkDate.format(backupDuty.getWorkDate()) + " " + sdfWorkTime.format(shiftMeta.getBeginWork()));
                Date shiftEndWork = sdf.parse(sdfWorkDate.format(backupDuty.getWorkDate()) + " " + sdfWorkTime.format(shiftMeta.getBeginWork()));

                if (shiftBeginRest.before(shiftBeginWork)) {
                    shiftBeginRest.setTime(shiftBeginRest.getTime() + 24 * 60 * 60 * 1000);
                }

                if (shiftEndRest.before(shiftBeginRest)) {
                    shiftEndRest.setTime(shiftEndRest.getTime() + 24 * 60 * 60 * 1000);
                }

                if (shiftEndWork.before(shiftEndRest)) {
                    shiftEndWork.setTime(shiftEndWork.getTime() + 24 * 60 * 60 * 1000);
                }

                boolean firstHalfFlag = false;
                boolean lastHalfFlag = false;

                if (backupDuty.getBeginWork() != null && backupDuty.getBeginWork().before(shiftBeginWork) && backupDuty.getBeginRest() != null && backupDuty.getBeginRest().after(shiftBeginRest)) {
                    firstHalfFlag = true;
                }

                if (backupDuty.getEndRest() != null && backupDuty.getEndRest().before(shiftEndRest) && backupDuty.getEndWork() != null && backupDuty.getEndWork().after(shiftEndWork)) {
                    lastHalfFlag = true;
                }

                if (!firstHalfFlag && !lastHalfFlag) {
                    result += 1;
                } else if (!firstHalfFlag && lastHalfFlag) {
                    result += 0.5;
                } else if (firstHalfFlag && !lastHalfFlag) {
                    result += 0.5;
                }
            }
        }

        return result;
    }

    private Map<String, Calendar> workCheckinData(Map<String, String> map, Calendar calShiftBeginWork, Calendar calShiftBeginRest, Calendar calShiftEndRest, Calendar calShiftEndWork, HrEmployeeTrackingWorkShiftMeta workShiftMeta) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        boolean isModifiedDutyResult = map.get("F_ISMODIFYRESULT").equals("Y") ? true : false;

        // get begin work
        Calendar calBeginWork = null;
        if (!StringUtils.isEmpty(map.get("F_BEGINWORK"))) {
            calBeginWork = Calendar.getInstance();
            calBeginWork.setTime(sdfDate.parse(map.get("F_DUTYDATE") + " " + map.get("F_BEGINWORK")));
        }

        if (isModifiedDutyResult && calBeginWork == null && calShiftBeginWork != null) {
            calBeginWork = Calendar.getInstance();
            calBeginWork.setTime(calShiftBeginWork.getTime());
        }

        // get begin rest
        Calendar calBeginRest = null;
        if (!StringUtils.isEmpty(map.get("F_BEGINREST"))) {
            calBeginRest = Calendar.getInstance();
            calBeginRest.setTime(sdfDate.parse(map.get("F_DUTYDATE") + " " + map.get("F_BEGINREST")));
        }

        if (isModifiedDutyResult && calBeginRest == null && calShiftBeginRest != null) {
            calBeginRest = Calendar.getInstance();
            calBeginRest.setTime(calShiftBeginRest.getTime());
        }

        // get end rest
        Calendar calEndRest = null;
        if (!StringUtils.isEmpty(map.get("F_ENDREST"))) {
            calEndRest = Calendar.getInstance();
            calEndRest.setTime(sdfDate.parse(map.get("F_DUTYDATE") + " " + map.get("F_ENDREST")));
        }

        if (isModifiedDutyResult && calEndRest == null && calShiftEndRest != null) {
            calEndRest = Calendar.getInstance();
            calEndRest.setTime(calShiftEndRest.getTime());

            if (workShiftMeta.getLunchTime() != null && calEndRest.getTimeInMillis() - calBeginRest.getTimeInMillis() > workShiftMeta.getLunchTime() * 1000L) {
                calEndRest.setTime(calBeginRest.getTime());
                calEndRest.add(Calendar.MINUTE, workShiftMeta.getLunchTime());
            }
        }

        // get end work
        Calendar calEndWork = null;
        if (!StringUtils.isEmpty(map.get("F_ENDWORK"))) {
            calEndWork = Calendar.getInstance();
            calEndWork.setTime(sdfDate.parse(map.get("F_DUTYDATE") + " " + map.get("F_ENDWORK")));
        }

        if (isModifiedDutyResult && calEndWork == null && calShiftEndWork != null) {
            calEndWork = Calendar.getInstance();
            calEndWork.setTime(calShiftEndWork.getTime());
        }

        Calendar cal = null;
        if (calBeginWork != null) {
            cal = Calendar.getInstance();
            cal.setTime(calBeginWork.getTime());
        }

        if (cal != null && calBeginRest != null) {
            if (calBeginRest.before(cal)) {
                calBeginRest.add(Calendar.DAY_OF_MONTH, 1);
            }
            cal.setTime(calBeginRest.getTime());
        } else if (calBeginRest != null) {
            cal = Calendar.getInstance();
            cal.setTime(calBeginRest.getTime());
        }

        if (cal != null && calEndRest != null) {
            if (calEndRest.before(cal)) {
                calEndRest.add(Calendar.DAY_OF_MONTH, 1);
            }
            cal.setTime(calEndRest.getTime());
        } else if (calEndRest != null) {
            cal = Calendar.getInstance();
            cal.setTime(calEndRest.getTime());
        }

        if (cal != null && calEndWork != null) {
            if (calEndWork.before(calEndRest)) {
                calEndWork.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        Map<String, Calendar> result = new HashMap<>();

        result.put("calBeginWork", calBeginWork);
        result.put("calBeginRest", calBeginRest);
        result.put("calEndRest", calEndRest);
        result.put("calEndWork", calEndWork);

        return result;
    }

    private Map<String, Calendar> dailyShiftData(HrEmployeeTrackingWorkShiftMeta workShiftMeta, String fDutyDate) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        Calendar calShiftBeginWork = null;
        if (workShiftMeta.getBeginWork() != null) {
            calShiftBeginWork = Calendar.getInstance();
            calShiftBeginWork.setTime(sdfDate.parse(fDutyDate + " " + sdfTime.format(workShiftMeta.getBeginWork())));
        }

        Calendar calShiftBeginRest = null;
        if (workShiftMeta.getBeginRest() != null) {
            calShiftBeginRest = Calendar.getInstance();
            calShiftBeginRest.setTime(sdfDate.parse(fDutyDate + " " + sdfTime.format(workShiftMeta.getBeginRest())));
        }

        Calendar calShiftEndRest = null;
        if (workShiftMeta.getEndRest() != null) {
            calShiftEndRest = Calendar.getInstance();
            calShiftEndRest.setTime(sdfDate.parse(fDutyDate + " " + sdfTime.format(workShiftMeta.getEndRest())));
        }

        Calendar calShiftEndWork = null;
        if (workShiftMeta.getEndWork() != null) {
            calShiftEndWork = Calendar.getInstance();
            calShiftEndWork.setTime(sdfDate.parse(fDutyDate + " " + sdfTime.format(workShiftMeta.getEndWork())));
        }

        Calendar cal = null;
        if (calShiftBeginWork != null) {
            cal = Calendar.getInstance();
            cal.setTime(calShiftBeginWork.getTime());
        }

        if (cal != null && calShiftBeginRest != null) {
            if (calShiftBeginRest.before(cal)) {
                calShiftBeginRest.add(Calendar.DAY_OF_MONTH, 1);
            }
            cal.setTime(calShiftBeginRest.getTime());
        } else if (calShiftBeginRest != null ) {
            cal.setTime(calShiftBeginRest.getTime());
        }

        if (cal != null && calShiftEndRest != null) {
            if (calShiftEndRest.before(cal)) {
                calShiftEndRest.add(Calendar.DAY_OF_MONTH, 1);
            }
            cal.setTime(calShiftEndRest.getTime());
        } else if (calShiftEndRest != null) {
            cal.setTime(calShiftEndRest.getTime());
        }

        if (cal != null && calShiftEndWork != null) {
            if (calShiftEndWork.before(cal)) {
                calShiftEndWork.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        Map<String, Calendar> result = new HashMap<>();

        result.put("calShiftBeginWork", calShiftBeginWork);
        result.put("calShiftBeginRest", calShiftBeginRest);
        result.put("calShiftEndRest", calShiftEndRest);
        result.put("calShiftEndWork", calShiftEndWork);

        return result;
    }

    @Override
    public Map<String, Object> workCount(String cardId, String departName, int year, int month, int startDay, int endDay) throws IOException, ParserConfigurationException, SAXException, ParseException {

        // setup input data
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, year);
        if (month < 1 || month > 12) {
            return new HashMap<>();
        } else {
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            if (startDay > cal.get(Calendar.DAY_OF_MONTH)) {
                startDay = cal.get(Calendar.DAY_OF_MONTH);
            } else if (startDay <= 0) {
                startDay = 1;
            }

            if (endDay > cal.get(Calendar.DAY_OF_MONTH)) {
                endDay = cal.get(Calendar.DAY_OF_MONTH);
            } else if (endDay <= 0) {
                endDay = cal.get(Calendar.DAY_OF_MONTH);
            }
        }

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(cal.getTime());
        calStart.set(Calendar.DAY_OF_MONTH, startDay);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(cal.getTime());
        calEnd.set(Calendar.DAY_OF_MONTH, endDay);
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);
        calEnd.set(Calendar.MILLISECOND, 999);

        if (StringUtils.isEmpty(cardId) && StringUtils.isEmpty(departName)) {
            return new HashMap<>();
        }

        Set<String> departList = new HashSet<>();
        Set<String> cardIdList = new HashSet<>();

        departList.addAll(Arrays.asList(departName.split(";")));
        cardIdList.addAll(Arrays.asList(cardId.split(";")));

        List<Map<String, String>> dutyDataList = new ArrayList<>();
        List<Map<String, String>> overtimeDataList = new ArrayList<>();

        if (StringUtils.isEmpty(cardId)) {
            for (String depart : departList) {
                dutyDataList.addAll(dutyData("", depart.trim(), year, month, startDay, endDay));
                overtimeDataList.addAll(overtimeData("", depart.trim(), year, month, startDay, endDay));
            }
        } else {
            for (String cardNo : cardIdList) {
                dutyDataList.addAll(dutyData(cardNo.trim(), "", year, month, startDay, endDay));
                overtimeDataList.addAll(overtimeData(cardNo.trim(), "", year, month, startDay, endDay));
            }
        }

        //BEGIN get raw data
        dutyDataList = dutyDataList.stream().filter(e -> e.get("F_EMPNO").startsWith("V")).collect(Collectors.toList());
        overtimeDataList = overtimeDataList.stream().filter(e -> e.get("ID").startsWith("V")).collect(Collectors.toList());

        Map<String, List<Map<String, String>>> dutyDataMap = dutyDataList.stream().collect(Collectors.groupingBy(e -> e.get("F_EMPNO")));
        Map<String, List<Map<String, String>>> overtimeDataMap = overtimeDataList.stream().collect(Collectors.groupingBy(e -> e.get("ID")));
        //END get raw data

        //BEGIN prepare meta data
        Map<String, Map<String, Object>> dataResult = new HashMap<>();

        List<HrEmployeeTrackingWorkResultMeta> workResultMetaList = hrEmployeeTrackingWorkResultMetaRepository.findAll();
        List<HrEmployeeTrackingWorkShiftMeta> workShiftMetaList = hrEmployeeTrackingWorkShiftMetaRepository.findAll();
        List<HrEmployeeTrackingOfficeUnitInfo> officeUnitInfoList = hrEmployeeTrackingOfficeUnitInfoRepository.findAll();

//        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cal3Begin = Calendar.getInstance();
        cal3Begin.set(Calendar.HOUR_OF_DAY, 22);
        cal3Begin.set(Calendar.MINUTE, 0);
        cal3Begin.set(Calendar.SECOND, 0);
        cal3Begin.set(Calendar.MILLISECOND, 0);

        Calendar cal3End = Calendar.getInstance();
        cal3End.set(Calendar.HOUR_OF_DAY, 6);
        cal3End.set(Calendar.MINUTE, 0);
        cal3End.set(Calendar.SECOND, 0);
        cal3End.set(Calendar.MILLISECOND, 0);
        //END prepare meta data

        for (Map.Entry<String, List<Map<String, String>>> dutyEntry : dutyDataMap.entrySet()) {
            List<Map<String, String>> dutyData = dutyEntry.getValue();
            List<Map<String, String>> overtimeData = overtimeDataMap.get(dutyEntry.getKey()) != null ? overtimeDataMap.get(dutyEntry.getKey()) : new ArrayList<>();
            Map<String, Object> tmpResult = new LinkedHashMap<>();

            String userName = dutyData.get(0).get("F_NAME");
            String department = dutyData.get(0).get("F_DEPARTNAME");
            String userId = dutyData.get(0).get("F_EMPNO");

//            HrEtEmployeeInfoServiceImpl.EmployeeInfo employeeInfo = employeeInfoService.employeeInfo(userId);
            HrEtEmployeeInfoServiceImpl.EmployeeInfo employeeInfo = new HrEtEmployeeInfoServiceImpl.EmployeeInfo();

            HrEmployeeTrackingPersonInfo personInfo = hrEmployeeTrackingPersonInfoRepository.findTop1ByEmpNo(userId).orElse(null);
            if (personInfo == null) {
                employeeInfo = employeeInfoService.employeeInfo(userId);
            } else {
                employeeInfo.setUserId(userId);
                employeeInfo.setUserName(userName);

                Calendar tmpHireCal = Calendar.getInstance();
                tmpHireCal.setTime(personInfo.getHireDate());
                employeeInfo.setHireDate(tmpHireCal);

                Calendar tmpLeaveCal = Calendar.getInstance();
                tmpLeaveCal.setTime(personInfo.getLeaveDate());
                employeeInfo.setLeaveDate(tmpLeaveCal);
            }

            Map<String, Double> totalCountResult = new LinkedHashMap<>();
            totalCountResult.put("1", 0.0D);
            totalCountResult.put("F", 0.0D);
            totalCountResult.put("O", 0.0D);
            totalCountResult.put("LV", 0.0D);
            totalCountResult.put("N", 0.0D);
            totalCountResult.put("V", 0.0D);
            totalCountResult.put("B", 0.0D);
            totalCountResult.put("BL", 0.0D);
            totalCountResult.put("BT", 0.0D);
            totalCountResult.put("KT", 0.0D);
            totalCountResult.put("L", 0.0D);
            totalCountResult.put("D", 0.0D);
            totalCountResult.put("K", 0.0D);
            totalCountResult.put("H", 0.0D);
            totalCountResult.put("T", 0.0D);
            totalCountResult.put("CT", 0.0D);
            totalCountResult.put("A", 0.0D);
            totalCountResult.put("TS", 0.0D);
            totalCountResult.put("P", 0.0D);

            List<DailyResult> workResultList = new ArrayList<>();
            List<String> unknownList = new ArrayList<>();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, startDay);
            while (calendar.get(Calendar.MONTH) == (month - 1) && calendar.get(Calendar.DAY_OF_MONTH) <= endDay) {
                Optional<Map<String, String>> mapOptional = dutyData.stream().filter(e -> e.get("F_DUTYDATE").equalsIgnoreCase(sdf.format(calendar.getTime()))).findFirst();
                Map<String, String> dailyOvertimeData = overtimeData.stream().filter(e -> e.get("DUTYDATE").equalsIgnoreCase(sdf.format(calendar.getTime()))).findFirst().orElse(new HashMap<>());
                DailyResult dailyResult = new DailyResult();
                dailyResult.setWorkDate(new java.sql.Date(calendar.getTimeInMillis()));
                dailyResult.setDepartName(department);
                dailyResult.setNameCn(userName);
                dailyResult.setCardId(userId);
                if (mapOptional.isPresent()) {
                    dailyResult.setWorkResultCn(mapOptional.get().get("F_MODIFYRESULT"));
                }

                cal3Begin.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                cal3Begin.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                cal3End.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                cal3End.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

                if (mapOptional.isPresent() && calendar.getTime().compareTo(employeeInfo.getLeaveDate().getTime()) < 0) {
                    Map<String, String> map = mapOptional.get();
                    String workingStatus = map.get("F_MODIFYRESULT");
                    String modifyStatus = map.get("F_ISMODIFYRESULT");
                    String shiftCode = map.get("F_CLASSNO");

                    if (!StringUtils.isEmpty(map.get("F_OWTIME"))) {
                        dailyResult.setSystemEarlyLateTime(Double.valueOf(map.get("F_OWTIME")));
                    }

                    Optional<HrEmployeeTrackingWorkResultMeta> workResultMetaOptional = workResultMetaList.stream().filter(e -> !StringUtils.isEmpty(e.getWorkResultCn()) && e.getWorkResultCn().equalsIgnoreCase(workingStatus)).findFirst();

                    if (workResultMetaOptional.isPresent() && !StringUtils.isEmpty(workResultMetaOptional.get().getWorkResultVi())) {
                        HrEmployeeTrackingWorkResultMeta workResultMeta = workResultMetaOptional.get();
                        dailyResult.setWorkResult(workResultMeta.getWorkResultVi());

                        Optional<HrEmployeeTrackingWorkShiftMeta> workShiftMetaOptional = workShiftMetaList.stream().filter(e -> e.getShiftCode() != null && e.getShiftCode().equalsIgnoreCase(shiftCode)).findFirst();
                        if (workShiftMetaOptional.isPresent() && !StringUtils.isEmpty(workShiftMetaOptional.get().getShiftSectionEn())) {
                            HrEmployeeTrackingWorkShiftMeta workShiftMeta = workShiftMetaOptional.get();
                            dailyResult.setWorkShiftCode(workShiftMeta.getShiftCode());
                            dailyResult.setWorkShiftType(workShiftMeta.getShiftSectionEn());
                            dailyResult.setResultModified(modifyStatus);

                            Map<String, Calendar> dailyShiftData = dailyShiftData(workShiftMeta, map.get("F_DUTYDATE"));
                            Calendar calShiftBeginWork = dailyShiftData.get("calShiftBeginWork");
                            Calendar calShiftBeginRest = dailyShiftData.get("calShiftBeginRest");
                            Calendar calShiftEndRest = dailyShiftData.get("calShiftEndRest");
                            Calendar calShiftEndWork = dailyShiftData.get("calShiftEndWork");

                            Map<String, Calendar> workCheckinData = workCheckinData(map, calShiftBeginWork, calShiftBeginRest, calShiftEndRest, calShiftEndWork, workShiftMeta);
                            Calendar calBeginWork = workCheckinData.get("calBeginWork");
                            Calendar calBeginRest = workCheckinData.get("calBeginRest");
                            Calendar calEndRest = workCheckinData.get("calEndRest");
                            Calendar calEndWork = workCheckinData.get("calEndWork");

                            //BEGIN get rest time
                            Long restTimeMs = null;
                            if (calBeginRest != null && calEndRest != null) {
                                restTimeMs = calEndRest.getTimeInMillis() - calBeginRest.getTimeInMillis();
                            }
                            Long restLimitMs = null;
                            if (workShiftMeta.getLunchTime() != null) {
                                restLimitMs = workShiftMeta.getLunchTime() * 60 * 1000L;
                            }
                            Long restOverLimitMs = null;
                            if (restTimeMs != null && restLimitMs != null) {
                                restOverLimitMs = restTimeMs - restLimitMs > 0 ? restTimeMs - restLimitMs : 0;
                            }
                            //END get rest time

                            //BEGIN get valid working time
                            Calendar calTimeBeginWork = null;
                            if (calShiftBeginWork != null && calBeginWork != null) {
                                calTimeBeginWork = Calendar.getInstance();
                                calTimeBeginWork.setTime(calShiftBeginWork.after(calBeginWork) ? calShiftBeginWork.getTime() : calBeginWork.getTime());
                            }

                            Calendar calTimeBeginRest = null;
                            if (calShiftBeginRest != null && calBeginRest != null) {
                                calTimeBeginRest = Calendar.getInstance();
                                calTimeBeginRest.setTime(calShiftBeginRest.before(calBeginRest) ? calShiftBeginRest.getTime() : calBeginRest.getTime());
                            }

                            Calendar calTimeEndRest = null;
                            if (calShiftEndRest != null && calEndRest != null) {
                                calTimeEndRest = Calendar.getInstance();
                                calTimeEndRest.setTime(calShiftEndRest.after(calEndRest) ? calShiftEndRest.getTime() : calEndRest.getTime());
                                if (restOverLimitMs != null && restOverLimitMs == 0 && calShiftBeginRest != null && calShiftEndRest != null && workShiftMeta.getLunchTime() != null) {
                                    if (calTimeBeginRest == null) {
                                        long restGap = calShiftEndRest.get(Calendar.MILLISECOND) - calShiftBeginRest.get(Calendar.MILLISECOND) - workShiftMeta.getLunchTime() * 60 * 1000;
                                        calTimeEndRest.add(Calendar.MILLISECOND, (int) -restGap);
                                    } else {
                                        calTimeEndRest.setTime(calTimeBeginRest.getTime());
                                        calTimeEndRest.add(Calendar.MINUTE, workShiftMeta.getLunchTime());
                                    }
                                }
                            }

                            Calendar calTimeEndWork = null;
                            if (calShiftEndWork != null && calEndWork != null) {
                                calTimeEndWork = Calendar.getInstance();
                                calTimeEndWork.setTime(calShiftEndWork.before(calEndWork) ? calShiftEndWork.getTime() : calEndWork.getTime());
                            }
                            //END get valid working time

                            long firstHalfLateMs = 0;
                            long restEarlyMs = 0;
                            long lastHalfLateMs = 0;
                            long leaveEarlyMs = 0;

                            if (calShiftBeginWork != null && calBeginWork != null) {
                                firstHalfLateMs = calBeginWork.getTimeInMillis() - calShiftBeginWork.getTimeInMillis() > 0 ? calBeginWork.getTimeInMillis() - calShiftBeginWork.getTimeInMillis() : 0;
                            }

                            if (calShiftBeginRest != null && calBeginRest != null) {
                                restEarlyMs = calShiftBeginRest.getTimeInMillis() - calBeginRest.getTimeInMillis() > 0 ? calShiftBeginRest.getTimeInMillis() - calBeginRest.getTimeInMillis() : 0;
                            }

                            if (calShiftEndRest != null && calEndRest != null) {
                                lastHalfLateMs = calEndRest.getTimeInMillis() - calShiftEndRest.getTimeInMillis() > 0 ? calEndRest.getTimeInMillis() - calShiftEndRest.getTimeInMillis() : 0;
                            }

                            if (calShiftEndWork != null && calEndWork != null) {
                                leaveEarlyMs = calShiftEndWork.getTimeInMillis() - calEndWork.getTimeInMillis() > 0 ? calShiftEndWork.getTimeInMillis() - calEndWork.getTimeInMillis() : 0;
                            }

                            if (restOverLimitMs != null && restOverLimitMs > 0) {
                                if (calShiftBeginRest != null && calBeginRest != null && calBeginRest.before(calShiftBeginRest)) {
                                    restEarlyMs += restOverLimitMs;
                                } else if (calShiftEndRest != null && calEndRest != null && calEndRest.after(calShiftEndRest)) {
                                    lastHalfLateMs += restOverLimitMs;
                                } else {
                                    lastHalfLateMs += restOverLimitMs;
                                }
                            }

                            //BEGIN set daily workResult
                            boolean firstHalftMark = false;
                            boolean lastHalfMark = false;

                            if (calBeginWork != null && calBeginRest != null && (firstHalfLateMs + restEarlyMs) <= 1800000) {
                                firstHalftMark = true;
                            }

                            if (calEndRest != null && calEndWork != null && (lastHalfLateMs + leaveEarlyMs) <= 1800000) {
                                lastHalfMark = true;
                            }

                            if (workResultMeta.getIsWorkDay() && modifyStatus.equalsIgnoreCase("N")) {
                                double tmpCount = totalCountResult.get("N");
                                double tmpLv = totalCountResult.get("LV");

                                if (firstHalftMark && !lastHalfMark) {
                                    tmpLv += 0.5D;
                                    tmpCount += 0.5D;
                                    dailyResult.setWorkResult("LV/N");

                                    dailyResult.setFirstHalfWork(workResultMeta.getIsWorkDay());
                                    dailyResult.setTotalEarlyLateMs(firstHalfLateMs + restEarlyMs);
                                } else if (!firstHalftMark && lastHalfMark) {
                                    tmpLv += 0.5D;
                                    tmpCount += 0.5D;
                                    dailyResult.setWorkResult("LV/N");

                                    dailyResult.setLastHalfWork(workResultMeta.getIsWorkDay());
                                    dailyResult.setTotalEarlyLateMs(lastHalfLateMs + leaveEarlyMs);
                                } else if (!firstHalftMark && !lastHalfMark) {
                                    tmpLv += 0.0D;
                                    tmpCount += 1.0D;
                                    dailyResult.setWorkResult("N");

                                    dailyResult.setTotalEarlyLateMs(0);
                                } else {
                                    tmpLv += 1.0D;
                                    tmpCount += 0.0D;
                                    dailyResult.setWorkResult("LV");

                                    dailyResult.setFirstHalfWork(workResultMeta.getIsWorkDay());
                                    dailyResult.setLastHalfWork(workResultMeta.getIsWorkDay());
                                    dailyResult.setTotalEarlyLateMs(firstHalfLateMs + restEarlyMs + lastHalfLateMs + leaveEarlyMs);
                                }

                                totalCountResult.put("N", tmpCount);
                                totalCountResult.put("LV", tmpLv);
                            } else if (workResultMeta.getIsWorkDay() || workResultMeta.getNoneWorkDay()) {
                                double tmpCount = totalCountResult.get(workResultMeta.getWorkResultVi()) + 1.0D;
                                totalCountResult.put(workResultMeta.getWorkResultVi(), tmpCount);

                                dailyResult.setFirstHalfWork(workResultMeta.getIsWorkDay());
                                dailyResult.setLastHalfWork(workResultMeta.getIsWorkDay());
                                dailyResult.setTotalEarlyLateMs(firstHalfLateMs + restEarlyMs + lastHalfLateMs + leaveEarlyMs);
                            } else {
                                double tmpCount = totalCountResult.get(workResultMeta.getWorkResultVi());
                                double tmpLv = totalCountResult.get("LV");

                                if (firstHalftMark && !lastHalfMark) {
                                    dailyResult.setWorkResult("LV/" + workResultMeta.getWorkResultVi());
                                    tmpCount += 0.5D;
                                    tmpLv += 0.5D;

                                    dailyResult.setFirstHalfWork(firstHalftMark);
                                    dailyResult.setTotalEarlyLateMs(firstHalfLateMs + restEarlyMs);
                                } else if (!firstHalftMark && lastHalfMark) {
                                    dailyResult.setWorkResult("LV/" + workResultMeta.getWorkResultVi());
                                    tmpCount += 0.5D;
                                    tmpLv += 0.5D;

                                    dailyResult.setLastHalfWork(lastHalfMark);
                                    dailyResult.setTotalEarlyLateMs(lastHalfLateMs + leaveEarlyMs);
                                } else if (!firstHalftMark && !lastHalfMark) {
                                    dailyResult.setWorkResult(workResultMeta.getWorkResultVi());
                                    tmpCount += 1.0D;

                                    dailyResult.setTotalEarlyLateMs(0);
                                } else {
                                    long totalEarlyLateMs = firstHalfLateMs + restEarlyMs + lastHalfLateMs + leaveEarlyMs;
                                    if (workResultMeta.getWorkResultVi().equalsIgnoreCase("1") && totalEarlyLateMs > 0) {
                                        double leaveTime = totalEarlyLateMs / 3600000D;
                                        leaveTime = Math.ceil(leaveTime * 2) / 2;
                                        dailyResult.setWorkResult(String.valueOf(leaveTime) + "/8");
                                    } else {
                                        dailyResult.setWorkResult(workResultMeta.getWorkResultVi());
                                    }
                                    tmpCount += 1.0D;

                                    dailyResult.setConfuseResult(true);
                                    dailyResult.setTotalEarlyLateMs(0);
                                }

                                totalCountResult.put(workResultMeta.getWorkResultVi(), tmpCount);
                                totalCountResult.put("LV", tmpLv);
                            }
                            //END set daily workResult

                            //BEGIN count ca3 work time
                            long totalCa3Ms = 0;

                            Calendar cal3BeginWork = null;
                            if (calTimeBeginWork != null) {
                                cal3BeginWork = Calendar.getInstance();
                                cal3BeginWork.setTime(calTimeBeginWork.before(cal3Begin) ? cal3Begin.getTime() : calTimeBeginWork.getTime());
                            }

                            Calendar cal3BeginRest = null;
                            if (calTimeBeginRest != null) {
                                cal3BeginRest = Calendar.getInstance();
                                cal3BeginRest.setTime(calTimeBeginRest.before(cal3End) ? calTimeBeginRest.getTime() : cal3End.getTime());
                            }

                            Calendar cal3EndRest = null;
                            if (calTimeEndRest != null) {
                                cal3EndRest = Calendar.getInstance();
                                cal3EndRest.setTime(calTimeEndRest.before(cal3Begin) ? cal3Begin.getTime() : calTimeEndRest.getTime());
                            }

                            Calendar cal3EndWork = null;
                            if (calTimeEndWork != null) {
                                cal3EndWork = Calendar.getInstance();
                                cal3EndWork.setTime(calTimeEndWork.before(cal3End) ? calTimeEndWork.getTime() : cal3End.getTime());
                            }

                            if (cal3BeginWork != null && cal3BeginRest != null) {
                                totalCa3Ms += cal3BeginRest.after(cal3BeginWork) ? cal3BeginRest.getTimeInMillis() - cal3BeginWork.getTimeInMillis() : 0;
                            }

                            if (cal3EndRest != null && cal3EndWork != null) {
                                totalCa3Ms += cal3EndWork.after(cal3EndRest) ? cal3EndWork.getTimeInMillis() - cal3EndRest.getTimeInMillis() : 0;
                            }

                            double ca3Time = totalCa3Ms / 3600000D;
                            ca3Time = ca3Time - (ca3Time % 0.25);
                            dailyResult.setCa3Time(ca3Time);
                            //END count ca3 work time

                            //BEGIN count total over time
                            String isOvertimeStr = dailyOvertimeData.isEmpty() ? "False" :dailyOvertimeData.get("ISOVERTIME");
                            boolean isOvertime = isOvertimeStr.equalsIgnoreCase("TRUE") ? true : false;

                            Calendar calOtFrom = null;
                            Calendar calOtTo = null;
                            long totalOtMs = 0;

                            if (isOvertime) {
                                try {
                                    calOtFrom = Calendar.getInstance();
                                    calOtFrom.setTime(sdfDate.parse(dailyOvertimeData.get("OVERTIME_S")));

                                    calOtTo = Calendar.getInstance();
                                    calOtTo.setTime(sdfDate.parse(dailyOvertimeData.get("OVERTIME_E")));
                                } catch (Exception e) {
                                    calOtFrom = null;
                                    calOtTo = null;
                                }
                            }

                            if (calOtFrom != null && calOtTo != null) {
                                totalOtMs = calOtTo.after(calOtFrom) ? calOtTo.getTimeInMillis() - calOtFrom.getTimeInMillis() : 0;
                                double tmpDouble = totalOtMs / 3600000D;
                                tmpDouble = tmpDouble - (tmpDouble % 0.25);
                                dailyResult.setOtTime(tmpDouble >= 0.5 ? tmpDouble : 0);
                            }
                            //END count total over time

                            //BEGIN count over time ca3
                            Calendar cal3OtFrom = null;
                            Calendar cal3OtTo = null;
                            long ca3OtMs = 0;

                            if (calOtFrom != null && cal3Begin != null) {
                                cal3OtFrom = Calendar.getInstance();
                                cal3OtFrom.setTime(calOtFrom.after(cal3Begin) ? calOtFrom.getTime() : cal3Begin.getTime());
                            }

                            if (calOtTo != null && cal3End != null) {
                                cal3OtTo = Calendar.getInstance();
                                cal3OtTo.setTime(calOtTo.before(cal3End) ? calOtTo.getTime() : cal3End.getTime());
                            }

                            if (cal3OtFrom != null && cal3OtTo != null) {
                                ca3OtMs = cal3OtTo.after(cal3OtFrom) ? cal3OtTo.getTimeInMillis() - cal3OtFrom.getTimeInMillis() : 0;
                                double tmpDouble = ca3OtMs / 3600000D;
                                tmpDouble = tmpDouble - (tmpDouble % 0.25);
                                dailyResult.setCa3OtTime(tmpDouble);
                            }

                            dailyResult.setNormalOtTime(dailyResult.getOtTime() - dailyResult.getCa3OtTime());
                            //END count over time ca3

                            if (calBeginWork != null) {
                                dailyResult.setBeginWork(new Time(calBeginWork.getTimeInMillis()));
                            }
                            if (calBeginRest != null) {
                                dailyResult.setBeginRest(new Time(calBeginRest.getTimeInMillis()));
                            }

                            if (calEndRest != null) {
                                dailyResult.setEndRest(new Time(calEndRest.getTimeInMillis()));
                            }

                            if (calEndWork != null) {
                                dailyResult.setEndWork(new Time(calEndWork.getTimeInMillis()));
                            }

                            dailyResult.setFirstHalfLate(new Time(firstHalfLateMs - 25200000));
                            dailyResult.setRestEarly(new Time(restEarlyMs - 25200000));
                            dailyResult.setLastHalfLate(new Time(lastHalfLateMs - 25200000));
                            dailyResult.setLeaveEarly(new Time(leaveEarlyMs - 25200000));
                            dailyResult.setEarlyLateTime(Math.ceil(dailyResult.getTotalEarlyLateMs() / 60000D));

                            if (isOvertime) {
                                dailyResult.setOtFrom(new Time(calOtFrom.getTimeInMillis()));
                                dailyResult.setOtTo(new Time(calOtTo.getTimeInMillis()));

                                if (dailyResult.getBeginWork() != null && dailyResult.getOtFrom().before(dailyResult.getBeginWork())) {
                                    dailyResult.setEndWork(workShiftMeta.getBeginWork());
                                }

                                if (dailyResult.getBeginRest() != null && dailyResult.getOtFrom().before(dailyResult.getBeginRest())) {
                                    dailyResult.setEndWork(workShiftMeta.getBeginRest());
                                }

                                if (dailyResult.getEndRest() != null && dailyResult.getOtFrom().before(dailyResult.getEndRest())) {
                                    dailyResult.setEndWork(workShiftMeta.getEndRest());
                                }

                                if (dailyResult.getEndWork() != null && dailyResult.getOtFrom().before(dailyResult.getEndWork())) {
                                    dailyResult.setEndWork(workShiftMeta.getEndWork());
                                }
                            }
                        } else {
                            dailyResult.setWorkShiftCode(map.get("F_CLASSNO"));
                            dailyResult.setHaveShiftData(false);
                        }
                    } else {
                        dailyResult.setWorkResult(map.get("F_MODIFYRESULT"));
                        unknownList.add(map.get("F_DUTYDATE"));
                    }
                } else {
                    if (calendar.getTime().compareTo(employeeInfo.getHireDate().getTime()) >= 0 && calendar.getTime().compareTo(employeeInfo.getLeaveDate().getTime()) < 0) {
                        dailyResult.setWorkResult("NULL");
                    } else if (calendar.getTime().compareTo(employeeInfo.getLeaveDate().getTime()) >= 0) {
                        dailyResult.setWorkResult("T");
                        double tmpCount = totalCountResult.get("T") + 1.0D;
                        totalCountResult.put("T", tmpCount);
                    } else {
                        dailyResult.setWorkResult("CT");
                        double tmpCount = totalCountResult.get("CT") + 1.0D;
                        totalCountResult.put("CT", tmpCount);
                    }
                }

                workResultList.add(dailyResult);

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

//            Object clockRecord = ((Map) hrEmployeeTrackingService.getDataHistoryByEmpNo(userId, sdfDate.format(calStart.getTime()), sdfDate.format(calEnd.getTime()))).get("dataByDay");

            List<HrEmployeeInfoEatRice> mealData = new ArrayList<>();

            mealData.addAll(hrEmployeeInfoEatRiceRepository.findAllByEmpCodeAndConsumeTimeBetween(userId, calStart.getTime(), calEnd.getTime()));

            double totalMeal = mealData.stream().mapToDouble(e -> e.getBreakfastM() + e.getLunchM() + e.getDinnerM() + e.getSupperM()).sum();

            Map<String, List<HrEmployeeInfoEatRice>> mealConsume = mealData.stream().collect(Collectors.groupingBy(e -> sdf.format(e.getConsumeTime())));

            double totalFreeDuty = 12;
            if (employeeInfo.getHireDate() != null) {
                Calendar tmpCal = Calendar.getInstance();
                tmpCal.setTime(calStart.getTime());

                int yearDiff = tmpCal.get(Calendar.YEAR) - employeeInfo.getHireDate().get(Calendar.YEAR);
                int monthDiff = tmpCal.get(Calendar.MONTH) - employeeInfo.getHireDate().get(Calendar.MONTH);

                if (monthDiff < 0) {
                    yearDiff -= 1;
                }

                if (yearDiff > 0) {
                    totalFreeDuty += yearDiff / 5;
                }
            }

            tmpResult.put("totalCountResult", totalCountResult);
            tmpResult.put("userId", userId);
            tmpResult.put("userName", userName);
            tmpResult.put("factory", officeUnitInfoList.stream().filter(e -> e.getOfficeUnitName().equalsIgnoreCase(department)).map(e -> e.getFactoryName()).findFirst().orElse(""));
            tmpResult.put("department", department);
            tmpResult.put("unknownList", unknownList);
            tmpResult.put("shortResult", workResultList.stream().map(e -> e.getWorkResult()).collect(Collectors.toList()).toString());
            tmpResult.put("ca3ShortResult", workResultList.stream().map(e -> e.getCa3Time()).collect(Collectors.toList()).toString());
            tmpResult.put("ca3TotalTime", workResultList.stream().map(e -> e.getCa3Time()).reduce(0.0D, (a, b) -> a + b));
            tmpResult.put("normalOtShortResult", workResultList.stream().map(e -> e.getNormalOtTime()).collect(Collectors.toList()).toString());
            tmpResult.put("normalOtTotal", workResultList.stream().mapToDouble(e -> e.getNormalOtTime()).sum());
            tmpResult.put("ca3OtShortResult", workResultList.stream().map(e -> e.getCa3OtTime()).collect(Collectors.toList()).toString());
            tmpResult.put("ca3OtTotal", workResultList.stream().mapToDouble(e -> e.getCa3OtTime()).sum());
            tmpResult.put("totalMeal", totalMeal);
            tmpResult.put("workResultList", workResultList);
//            tmpResult.put("clockRecord", clockRecord);
            tmpResult.put("mealConsume", mealConsume);
            tmpResult.put("usedFreeDuty", usedFreeDuty(userId, calStart));
            tmpResult.put("totalFreeDuty", totalFreeDuty);

            dataResult.put(dutyEntry.getKey(), tmpResult);
        }

        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        List<Integer> dayOfMonths = new ArrayList<>();
        List<String> dayOfWeeks = new ArrayList<>();
        while (calendar.get(Calendar.MONTH) == (month - 1)) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfMonth >= startDay && dayOfMonth <= endDay) {
                dayOfMonths.add(dayOfMonth);
                if (dayOfWeek == 1) {
                    dayOfWeeks.add("CN");
                } else {
                    dayOfWeeks.add("T" + String.valueOf(dayOfWeek));
                }
            } else if (startDay == 0 && endDay == 0){
                dayOfMonths.add(dayOfMonth);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dayOfMonth", dayOfMonths);
        result.put("dayOfWeek", dayOfWeeks);
        result.put("data", dataResult);
        result.put("labelList", Arrays.asList("1", "F", "O", "LV", "N", "V", "B", "BL", "BT", "KT", "L", "D", "K", "H", "T", "CT", "A", "TS", "P"));

        return result;
    }


    @Data
    public static class DailyResult {

        private String workResult;

        private String workResultCn;

        private String resultModified;

        private double ca3Time = 0.0D;

        private java.sql.Date workDate;

        private String workShiftCode;

        private String workShiftType;

        private Time firstHalfLate;

        private Time restEarly;

        private Time lastHalfLate;

        private Time leaveEarly;

        private Time beginWork;

        private Time beginRest;

        private Time endRest;

        private Time endWork;

        private Time otFrom;

        private Time otTo;

        private double otTime = 0.0;

        private double normalOtTime = 0.0;

        private double ca3OtTime = 0.0;

        private long totalEarlyLateMs = 0;

        private Double earlyLateTime = 0.0;

        private Double systemEarlyLateTime;

        private boolean confuseResult = false;

        private String departName;

        private String cardId;

        private String nameCn;

        private boolean haveShiftData = true;

        private boolean firstHalfWork = false;

        private boolean lastHalfWork = false;
    }

    private HSSFCellStyle createStyle(HSSFWorkbook workbook) {

        HSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);

        return style;

    }
}
