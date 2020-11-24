package com.foxconn.fii.receiver.re.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.data.b04.model.*;
import com.foxconn.fii.data.b04.repository.*;
import com.foxconn.fii.common.response.SortableMapResponse;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.data.sfc.repository.SfcTestSerialErrorRepository;
import com.foxconn.fii.receiver.re.service.RepairCheckInService;
import com.foxconn.fii.receiver.re.service.RepairOnlineWipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RepairOnlineWipServiceImpl implements RepairOnlineWipService {

    @Autowired
    private B04RepairOnlineWipRepository b04RepairOnlineWipRepository;

    @Autowired
    private RepairCheckInService repairCheckInService;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private B04RepairCheckInRepository b04RepairCheckInRepository;

    @Autowired
    private B04ResourceRepository b04ResourceRepository;

    @Autowired
    private B04RepairPTHCheckInRepository b04RepairPTHCheckInRepository;

    @Autowired
    private B04RepairSMTCheckInRepository b04RepairSMTCheckInRepository;

    @Autowired
    private TestModelListRepository testModelListRepository;

    @Autowired
    private RepairBC8MCheckInRepository repairBC8MCheckInRepository;

    @Autowired
    private RepairBC8MCheckOutRepository repairBC8MCheckOutRepository;

    @Autowired
    private RepairBC8MCheckInInUniqueRepository repairBC8MCheckInInUniqueRepository;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    private RepairOnlineWipInOutRepository repairOnlineWipInOutRepository;

    @Autowired
    private ReDailyRemainRepository reDailyRemainRepository;

    @Autowired
    private SfcTestSerialErrorRepository sfcTestSerialErrorRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReInfoResourceRepository reInfoResourceRepository;

    @Autowired
    @Qualifier(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> getCountModelnameBy10Day(String factory, Date startDate, Date endDate) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("count_modelname_by_weekly_T");
        SqlParameterSource in = new MapSqlParameterSource().addValue("factory", factory).addValue("time_start", startDate).addValue("time_end",endDate);
        Map<String, Object> objs = jdbcCall.execute(in);
        return (List<Map<String, Object>>)objs.get("#result-set-1");
    }

    public List<Map<String, Object>> countSerialNumberCheckInByTime(String factory) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("count_serialnumber_checkin_by_time");
        SqlParameterSource in = new MapSqlParameterSource().addValue("factory", factory);
        Map<String, Object> objs = jdbcCall.execute(in);
        return (List<Map<String, Object>>)objs.get("#result-set-1");
    }

    public List<Map<String, Object>> countSerialNumberCheckInOnlineWipByTime(String factory) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("re_count_sn_checkin_online_wip_by_time");
        SqlParameterSource in = new MapSqlParameterSource().addValue("factory", factory);
        Map<String, Object> objs = jdbcCall.execute(in);
        return (List<Map<String, Object>>)objs.get("#result-set-1");
    }


    @Override
    public void saveDataCheckInToDataRaw() throws ParserConfigurationException, SAXException, ParseException, IOException {
        Date time = new Date();
    RepairTimeDataBeta timeStartEnd = repairTimeDataBetaRepository.findTop1ByActionOrderByIdDesc("RE-CHECKIN");

        List<B04RepairCheckIn> dataCheckIn = b04RepairCheckInRepository.findByInputTimeBetween(timeStartEnd.getStartDate(), time);
        List<B04RepairOnlineWip> dataOnlineWip = b04RepairOnlineWipRepository.findByTimeInputBetween(timeStartEnd.getStartDate(), time);
        for (B04RepairCheckIn tmp : dataCheckIn) {
//            TestRepairSerialError dataUpdate = testRepairSerialErrorRepository.findTop1BySerialNumberOrderByIdDesc(tmp.getSerial());
//
//            if (dataUpdate != null){
//                dataUpdate.setCheckInTime(tmp.getInputTime());
//                testRepairSerialErrorRepository.save(dataUpdate);
//            }
            List<TestRepairSerialError> dataUpdate7 = testRepairSerialErrorRepository.findBySerialNumberOrderByIdDesc(tmp.getSerial());
            if (dataUpdate7.size() > 0){
                for (TestRepairSerialError val: dataUpdate7) {
                    if (StringUtils.isEmpty(val.getRepairTime())){
                           if ((tmp.getInputTime().compareTo(val.getCheckInTime())) > 0){
                               val.setCheckInTime(tmp.getInputTime());
                           }
                    }
                    testRepairSerialErrorRepository.save(val);
                }
            }
        }
        log.info("tung");

        if (dataOnlineWip.size() > 0){
            for (B04RepairOnlineWip tmp : dataOnlineWip) {
                TestRepairSerialError dataUpdateOnline = testRepairSerialErrorRepository.findTop1BySerialNumberOrderByIdDesc(tmp.getSerialLabel());
                if (dataUpdateOnline != null){
                    dataUpdateOnline.setCheckInTime(tmp.getTimeInput());
                    testRepairSerialErrorRepository.save(dataUpdateOnline);
                }
            }
        }
        RepairTimeDataBeta repairTimeDataBeta = new RepairTimeDataBeta();
        repairTimeDataBeta.setAction("RE-CHECKIN");
        repairTimeDataBeta.setFactory("B04");
        repairTimeDataBeta.setStartDate(time);
        repairTimeDataBetaRepository.save(repairTimeDataBeta);

    }

    public int[] updateCheckInTimeRepair(List<B04RepairCheckIn> groupList) {
        if (groupList.isEmpty()) {
            log.info("### DuongTungErrorRECheckIn");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into test_repair_serial_error as target " +
                        "using(select ID_LABEL=?, MO=?, PRODUCT=?, R_STATION=?, ERROR_CODE=?, DATE_TIME_INPUT=?, DATE_TIME_IN_LINE=?) as source " +
                        "   on target.serial_number=source.ID_LABEL and target.test_time = source.DATE_TIME_IN_LINE " +
                        "when matched then " +
                        "   update set " +
                        "   target.checkin_time=source.DATE_TIME_INPUT ;",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        B04RepairCheckIn group = groupList.get(i);
                        preparedStatement.setString(1, group.getSerial());
                        preparedStatement.setString(2, group.getMo());
                        preparedStatement.setString(3, group.getModelName());
                        preparedStatement.setString(4, group.getStationName());
                        preparedStatement.setString(5, group.getErrorCode());
                        preparedStatement.setTimestamp(6, new Timestamp(group.getInputTime().getTime()));
                        preparedStatement.setTimestamp(7, new Timestamp(group.getTimeInLine().getTime()));

                    }
                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }


    @Override
    public Map<String, Object> getDataInputOutput(String startD, String endD) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date startDate = simpleDateFormat.parse(startD);
        Date endDate = simpleDateFormat.parse(endD);
        List<Object[]> dataInput = b04RepairOnlineWipRepository.countByProductInput(startDate, endDate);
        List<Object[]> dataOutput = b04RepairOnlineWipRepository.countByproductOutput(startDate, endDate);
        long sumCountInput = 0;
        long sumCountOutput = 0;
        Map<String, Long> input = new LinkedHashMap<>();
        Map<String, Long> output = new LinkedHashMap<>();

        if (dataInput.size() > 0){
            for (Object[] tmp: dataInput) {
                sumCountInput += (long) tmp[1];
                input.put((String) tmp[0], (long) tmp[1]);
            }
        }
        if (dataOutput.size() > 0){
            for (Object[] tmp: dataOutput) {
                sumCountOutput += (long) tmp[1];
                output.put((String) tmp[0], (long) tmp[1]);
            }
        }
        input = sortMapByValue(input);
        output = sortMapByValue(output);
        Map<String, Object> result = new HashMap<>();
        result.put("countsuminput", sumCountInput);
        result.put("datainput", input);
        result.put("countsumoutput", sumCountOutput);
        result.put("dataoutput", output);

        return result;
    }


    private <T extends Comparable<? super T>> Map<String, T> sortMapByValue(Map<String, T> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }



    @Override
    public Map<String,Map<String, Integer>> getDataRepairWeeklyStatus() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date timeNow = new Date();

        Calendar starTime = Calendar.getInstance();
        starTime.setTime(timeNow);
        starTime.add(Calendar.DAY_OF_MONTH, -10);

         List<B04RepairOnlineWip> inputData = b04RepairOnlineWipRepository.findByTimeInputBetween(starTime.getTime(), timeNow);

         List<B04RepairOnlineWip> outputData = b04RepairOnlineWipRepository.findByTimeOutputBetween(starTime.getTime(), timeNow);
        Map<String,  List<B04RepairOnlineWip>> result = inputData.stream().collect(Collectors.groupingBy( e -> {
            return  df.format(e.getTimeInput());
        }));
        result = result.entrySet().stream().sorted((e1, e2) ->{
            try {
                Date date1 = df.parse(e1.getKey());
                Date date2 = df.parse(e2.getKey());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        Map<String, Integer> dataIn = new HashMap<>();
        for (String key : result.keySet()) {
            dataIn.put(key, result.get(key).size());
        }
        //////////
        Map<String,  List<B04RepairOnlineWip>> resultOutput = outputData.stream().collect(Collectors.groupingBy( e -> {
            return  df.format(e.getTimeOutput());
        }));
        resultOutput = resultOutput.entrySet().stream().sorted((e1, e2) ->{
            try {
                Date date1 = df.parse(e1.getKey());
                Date date2 = df.parse(e2.getKey());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        Map<String, Integer> dataOut = new HashMap<>();
        for (String key : resultOutput.keySet()) {
            dataOut.put(key, resultOutput.get(key).size());
        }
        Map<String,Map<String, Integer>> res = new LinkedHashMap<>();
        while (starTime.getTime().compareTo(timeNow) <= 0) {
            String strDate = df.format(starTime.getTime());
            Integer qtyIn = dataIn.get(strDate);
            Integer qtyOut = dataOut.get(strDate);
            if (qtyIn == null){
                qtyIn = 0;
            }
            if (qtyOut == null){
                qtyOut = 0;
            }
            Integer remain = qtyOut - qtyIn;
            Map<String, Integer> tmpRes = new LinkedHashMap<>();
            tmpRes.put("inputQty",qtyIn);
            tmpRes.put("outputQty",qtyOut);
            tmpRes.put("remainQty",remain);
            res.put(strDate, tmpRes);
            starTime.add(Calendar.DAY_OF_MONTH, +1);
        }
        return res;
    }
    private HSSFCellStyle createStyle(HSSFWorkbook workbook) {

        HSSFFont font = workbook.createFont();
        font.setFontName("Arial");

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

    @Override
    public void getDataDetailModelName(HttpServletResponse resonse, String modelName) throws IOException {
        String factory = "B04";
        List<TestRepairSerialError> data = testRepairSerialErrorRepository.getModelnameBySI(factory, modelName, "SI");

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {"id"
                ,"factory"
                ,"model Name"
                ,"group Name"
                ,"station Name"
                ,"tester"
                ,"repairer"
                ,"test Time"
                ,"error Code"
                ,"repair Time"
                ,"reason Code"
                ,"workdate"
                ,"shift"
                ,"serial Number"
                ,"section Name"
                ,"created_at"
                ,"updated_at"
                ,"location Code"
                ,"mo"
                ,"status"};

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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

        HSSFSheet sheet = workbook.createSheet(modelName);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (TestRepairSerialError dt: data) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getId());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getFactory());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getModelName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getGroupName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getStationName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getTester());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getRepairer());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getTestTime());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getErrorCode());
            cellNum++;

            String strDate = "";
            if (dt.getRepairTime() != null){
                strDate = df.format(dt.getRepairTime());
            }

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(strDate);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getReason());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getWorkdate());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getShift());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getSerialNumber());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getSectionName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getCreatedAt());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getUpdatedAt());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getLocationCode());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getMo());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getStatus().toString());
            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        file.delete();
    }

    @Override
    public Object getWipInOutStatus(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date timeToday = new Date();
        if (factory.equalsIgnoreCase("B04")){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -7);
            List<Object[]> dataInputOnlineWip = b04RepairOnlineWipRepository.countByProductInput(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            List<Object[]> dataCheckIn = repairOnlineWipInOutRepository.countByModelName(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            List<Object[]> dataOutput = repairOnlineWipInOutRepository.countOutputModelName(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
            List<Object[]> dataRemain = repairOnlineWipInOutRepository.countInputModelName(factory);
            Map<String, Object> result = new HashMap<>();
            long sumCountOutput = 0;
            Map<String, Long> output = new LinkedHashMap<>();
            if (dataOutput.size() > 0){
                for (Object[] tmp: dataOutput) {
                    sumCountOutput += (long) tmp[1];
                    output.put((String) tmp[0], (long) tmp[1]);
                }
            }
            output = sortMapByValue(output);
            result.put("totalOutput", sumCountOutput);
            result.put("dataOutput",output);
            long sumCountRemain = 0;
            Map<String, Long> remain = new LinkedHashMap<>();
            if (dataRemain.size() > 0){
                for (Object[] tmp:dataRemain){
                    sumCountRemain += (long)tmp[1];
                    remain.put((String) tmp[0], (long) tmp[1]);
                }
            }
            remain = sortMapByValue(remain);
            result.put("totalRemain", sumCountRemain);
            result.put("dataRemain",remain);

            List<Object[]> dtInput = new ArrayList<>();
            dtInput.addAll(dataInputOnlineWip);
            dtInput.addAll(dataCheckIn);
            Map<String, Long> input = new LinkedHashMap<>();
            long sumCountInput = 0;
            if (dtInput.size() > 0){
                for (Object[] tmp:dtInput){
                    sumCountInput += (long)tmp[1];
                    input.put((String) tmp[0], (long) tmp[1]);
                }
            }
            input = sortMapByValue(input);
            result.put("totalInput", sumCountInput);
            result.put("dataInput",input);
            return result;
        }else if (factory.equalsIgnoreCase("C03") || factory.equalsIgnoreCase("B06") || factory.equalsIgnoreCase("S03")) {

            List<TestModelMeta> modelMeta;

            if (factory.equalsIgnoreCase("S03")){
                modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
            }else {
                 modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
            }

            List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
            System.out.println("listModel.size: "+ listModel.size());
            Calendar calen = Calendar.getInstance();
            calen.setTime(fullTimeSpan.getEndDate());
            calen.add(Calendar.DAY_OF_YEAR, -30);
            fullTimeSpan.setStartDate(calen.getTime());
            List<Map<String, Object>> data = sfcTestSerialErrorRepository.getListInputDailyRe(factory, "UI", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModel);
            List<Map<String, Object>> dataOutput = sfcTestSerialErrorRepository.getListOutputDailyRe(factory, "UI", fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModel);
            log.info("au");
//            Map<String, List<Map<String, Object>>> datfaMap = data.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TIMER"))));
            Map<String, List<Map<String, Object>>> dataMap = data.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TIMER")), LinkedHashMap::new,  Collectors.mapping(Function.identity(), Collectors.toList())));
            Map<String, List<Map<String, Object>>> dataMapOutput = dataOutput.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TIMER")), LinkedHashMap::new,  Collectors.mapping(Function.identity(), Collectors.toList())));

            List<Map<String, Object>> result = new ArrayList<>();
            if (dataMap.size() > 0){
                for (Map.Entry<String, List<Map<String, Object>>> val : dataMap.entrySet()) {
                    Map<String, Object> inputTimeC03 = new LinkedHashMap<>();
                    int count = 0;
                    Map<String, Integer> tmpMap = new LinkedHashMap<>();
                    for (Map<String, Object> tmp : val.getValue()) {
                       count += ((BigDecimal)tmp.get("TOTAL")).intValue();
                       tmpMap.put((String) tmp.get("MODEL_NAME"), ((BigDecimal)tmp.get("TOTAL")).intValue());
                    }
                    tmpMap = sortMapByValue(tmpMap);
                    inputTimeC03.put(val.getKey(), count);
                    inputTimeC03.put("data", tmpMap);
                    result.add(inputTimeC03);
                }
            }

            List<Map<String, Object>> result1 = new ArrayList<>();
            if (dataMapOutput.size() > 0){
                for (Map.Entry<String, List<Map<String, Object>>> val : dataMapOutput.entrySet()) {
                    Map<String, Object> inputTimeC03 = new LinkedHashMap<>();
                    int count = 0;
                    Map<String, Integer> tmpMap = new LinkedHashMap<>();
                    for (Map<String, Object> tmp : val.getValue()) {
                       count += ((BigDecimal)tmp.get("TOTAL")).intValue();
                       tmpMap.put((String) tmp.get("MODEL_NAME"), ((BigDecimal)tmp.get("TOTAL")).intValue());
                    }
                    tmpMap = sortMapByValue(tmpMap);
                    inputTimeC03.put(val.getKey(), count);
                    inputTimeC03.put("data", tmpMap);
                    result1.add(inputTimeC03);
                }
            }
            Map<String, Object> res = new HashMap<>();
            res.put("input", result);
            res.put("output", result1);
            return res;
        }
        return "Contact 26131 support by FII";
    }

    @Override
    public Object getDataRemainDaily(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -10);
      //  List<Object[]> dt = testRepairSerialErrorRepository.getCountDataRemainDaily(factory,calendar.getTime(),fullTimeSpan.getEndDate());
        List<ReDailyRemain> data = reDailyRemainRepository.findByFactoryAndStartDateBetweenAndStatus(factory, calendar.getTime(), fullTimeSpan.getEndDate(), ReDailyRemain.Status.ONLINE_WIP);
        Map<String, Long> respons = new LinkedHashMap<>();
        for (ReDailyRemain val: data) {
            respons.put(dff.format(val.getStartDate()), val.getDailyRemain());
        }
        return respons;
    }

    @Override
    public Object getDataErrorCodeByModelName(String factory, String modelName) throws ParseException {
        List<Object[]> data = repairOnlineWipInOutRepository.countByModelNameAndErrorCode(factory, modelName);
        Map<String, Long> res = new LinkedHashMap<>();
        for (Object[] tmp : data){
            res.put((String) tmp[0], (long) tmp[1]);
        }
        res = sortMapByValue(res);
        return res;
    }

    @Override
    public Map<String, SortableMapResponse<Integer>> getDataModelNameAndTime(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
         List<Object[]> data = repairOnlineWipInOutRepository.countByModelNameAndHourTime(factory, calendar.getTime(), fullTimeSpan.getEndDate());
        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();
        result.put("<4H", new SortableMapResponse<>());
        result.put(">4H", new SortableMapResponse<>());
        result.put(">12H", new SortableMapResponse<>());
        result.put(">24H", new SortableMapResponse<>());
        result.put(">48H", new SortableMapResponse<>());
        result.put(">72H", new SortableMapResponse<>());
        result.put(">7D", new SortableMapResponse<>());
        result.put(">15D", new SortableMapResponse<>());
        for (Object[] value : data) {
            String modelName = (String) value[0];
            String repairer = (String) value[1];
            Integer diff = (Integer) value[2];
            Integer count = (Integer) value[3];
       //     if (StringUtils.isEmpty(repairer)) {
                SortableMapResponse<Integer> map;
                if (diff < 4) {
                    map = result.get("<4H");
                } else if (diff < 12) {
                    map = result.get(">4H");
                } else if (diff < 24) {
                    map = result.get(">12H");
                } else if (diff < 48) {
                    map = result.get(">24H");
                } else if (diff < 72) {
                    map = result.get(">48H");
                } else if (diff < 24 * 7) {
                    map = result.get(">72H");
                } else if (diff < 24 * 15) {
                    map = result.get(">7D");
                } else {
                    map = result.get(">15D");
                }
                map.put(modelName, map.get(modelName, 0) + count);
                map.setSize(map.getSize() + count);
           // }
        }
        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> getDataModelNameAndHoureTime(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MONTH, 1);

        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getCheckIn(factory, calendar.getTime(), fullTimeSpan.getEndDate(), listModelByFactory(factory));
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();

        List<Map<String, Object>> nhohon4 = new ArrayList<>();
        List<Map<String, Object>> lonhon4 = new ArrayList<>();
        List<Map<String, Object>> lonhon12 = new ArrayList<>();
        List<Map<String, Object>> lonhon24 = new ArrayList<>();
        List<Map<String, Object>> lonhon48 = new ArrayList<>();
        List<Map<String, Object>> lonhon72 = new ArrayList<>();
        List<Map<String, Object>> lonhon7d = new ArrayList<>();
        List<Map<String, Object>> lonhon15d = new ArrayList<>();
        Calendar dateNow = Calendar.getInstance();
        Calendar diff = Calendar.getInstance();
        for (int i = 0; i < data.size(); i++) {
            diff.setTime((Date) data.get(i).get("IN_DATETIME"));
            long diffTmp = dateNow.getTimeInMillis() - diff.getTimeInMillis();
            long hours = TimeUnit.MILLISECONDS.toHours(diffTmp);
            if (hours < 4){
                nhohon4.add(data.get(i));
            }else if (hours < 12){
                lonhon4.add(data.get(i));
            }else if (hours < 24){
                lonhon12.add(data.get(i));
            }else if (hours < 48){
                lonhon24.add(data.get(i));
            }else if (hours < 72){
                lonhon48.add(data.get(i));
            }else if (hours < 7 * 24){
                lonhon72.add(data.get(i));
            }else if (hours < 15 * 24){
                lonhon7d.add(data.get(i));
            }else {
                lonhon15d.add(data.get(i));
            }
        }
        Map<String, Long> nhohon4Map = countModelName(nhohon4);
        Map<String, Long> lonhon4Map = countModelName(lonhon4);
        Map<String, Long> lonhon12Map = countModelName(lonhon12);
        Map<String, Long> lonhon24Map = countModelName(lonhon24);
        Map<String, Long> lonhon48Map = countModelName(lonhon48);
        Map<String, Long> lonhon72Map = countModelName(lonhon72);
        Map<String, Long> lonhon7dMap = countModelName(lonhon7d);
        Map<String, Long> lonhon15dMap = countModelName(lonhon15d);

        nhohon4Map = nhohon4Map.size() > 0 ?   sortMapByValue(nhohon4Map) :  nhohon4Map ;
        lonhon4Map = lonhon4Map.size() > 0 ?   sortMapByValue(lonhon4Map) :  lonhon4Map ;
        lonhon12Map = lonhon12Map.size() > 0 ?   sortMapByValue(lonhon12Map) :  lonhon12Map ;
        lonhon24Map = lonhon24Map.size() > 0 ?   sortMapByValue(lonhon24Map) :  lonhon24Map ;
        lonhon48Map = lonhon48Map.size() > 0 ?   sortMapByValue(lonhon48Map) :  lonhon48Map ;
        lonhon72Map = lonhon72Map.size() > 0 ?   sortMapByValue(lonhon72Map) :  lonhon72Map ;
        lonhon7dMap = lonhon7dMap.size() > 0 ?   sortMapByValue(lonhon7dMap) :  lonhon7dMap ;
        lonhon15dMap = lonhon15dMap.size() > 0 ?   sortMapByValue(lonhon15dMap) :  lonhon15dMap ;

        result.put("<4H", countModelName(nhohon4Map, countModelName(nhohon4Map)));
        result.put(">4H", countModelName(lonhon4Map, countModelName(lonhon4Map)));
        result.put(">12H", countModelName(lonhon12Map, countModelName(lonhon12Map)));
        result.put(">24H", countModelName(lonhon24Map, countModelName(lonhon24Map)));
        result.put(">48H", countModelName(lonhon48Map, countModelName(lonhon48Map)));
        result.put(">72H", countModelName(lonhon72Map, countModelName(lonhon72Map)));
        result.put(">7D", countModelName(lonhon7dMap, countModelName(lonhon7dMap)));
        result.put(">15D", countModelName(lonhon15dMap, countModelName(lonhon15dMap)));
        return result;
    }

    private Map<String, Long> countModelName(List<Map<String, Object>> data){
        if (data.size() > 0){
            return data.stream().map(e -> (String) e.get("MODEL_NAME")).collect(
                    Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }else {
            return new HashMap<>();
        }

    }
    private Long countModelName(Map<String, Long> data) {
        Long sum = 0L;
        for (Long tmp : data.values()
        ) {
            sum += tmp;
        }
        return sum;
    }

    private Map<String, Object> countModelName(Map<String, Long> data, Long sum) {
        Map<String, Object> d = new HashMap<>();
        d.put("qty", sum);
        d.put("data", data);
        return d;
    }

    private List<String> listModelByFactory(String factory){
        List<TestModelMeta> modelMeta;
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }

        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        return listModel;
    }

    @Override
    public Object bonpileByModelService(String factory, String timeSpan) throws Exception {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getEndDate());
//        calendar.add(Calendar.DAY_OF_YEAR, -2);
        String dateReport = simpleDateFormat.format(calendar.getTime());
        List<Map<String, Object>> data  = sfcTestSerialErrorRepository.qtyReportBalanceOver8h(factory, dateReport, listModelByFactory(factory));
        Map<String, Integer> bonepile = new LinkedHashMap<>();
        Map<String, Integer> balance = new LinkedHashMap<>();
        Map<String, Integer> overTime = new LinkedHashMap<>();
        for (int i = 0; i < data.size(); i++) {
            Integer qtyBonepile = ((BigDecimal)data.get(i).get("BALANCE_QTY")).intValue() + ((BigDecimal)data.get(i).get("OVERTIME8H_QTY")).intValue();
            bonepile.put((String) data.get(i).get("P_NO"), qtyBonepile);
            balance.put((String) data.get(i).get("P_NO"), ((BigDecimal)data.get(i).get("BALANCE_QTY")).intValue());
            overTime.put((String) data.get(i).get("P_NO"), ((BigDecimal)data.get(i).get("OVERTIME8H_QTY")).intValue());
        }
        bonepile = sortMapByValue(bonepile);
        balance = sortMapByValue(balance);
        overTime = sortMapByValue(overTime);

        int tmp = 0;
        int tm = 0;
        int t = 0;
        Map<String, Integer> bonepile2 = new LinkedHashMap<>();
        Map<String, Integer> balance2 = new LinkedHashMap<>();
        Map<String, Integer> overTime2 = new LinkedHashMap<>();
        for (String val : bonepile.keySet()) {
            bonepile2.put(val, bonepile.get(val));
            tmp ++;
            if (tmp == 10){
                break;
            }
        }
        for (String val : balance.keySet()) {
            balance2.put(val, balance.get(val));
            tm ++;
            if (tm == 10){
                break;
            }
        }
        for (String val : overTime.keySet()) {
            overTime2.put(val, overTime.get(val));
            t ++;
            if (t == 10){
                break;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("bonepile", bonepile2);
        result.put("balance", balance2);
        result.put("overTime", overTime2);
        result.put("date", dateReport);
        return result;
    }

    @Override
    public Object bonpileByModelByErrorCodeService(String factory, String action, String modelName, String timeSpan) throws Exception {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getEndDate());
        String dateReport = simpleDateFormat.format(calendar.getTime());
//        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getDetailSNByModelNameByType(factory, action, modelName, dateReport);
//        List<String> listSN = data.stream().map(e->(String) e.get("SN")).collect(Collectors.toList());
//        List<Map<String, Object>> res = sfcTestSerialErrorRepository.getErrorCodeBySN(factory, listSN);
        List<Map<String, Object>> res = new ArrayList<>();
        if (action.equalsIgnoreCase("BONEPILE")){
            List<String> actionList = new ArrayList<>();
            actionList.add("BALANCE");
            actionList.add("OVER8H");
            res = sfcTestSerialErrorRepository.getErrorCodeBySN(factory, actionList, modelName, dateReport);

        }else {
             res = sfcTestSerialErrorRepository.getErrorCodeBySN(factory, action, modelName, dateReport);

        }
        log.info("dada");
        List<Map<String, Object>> ls = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
//        for (int i = 0; i < res.size() ; i++) {
//            Map<String, Object> tmpMap = new HashMap<>();
//            String keysn = (String) res.get(i).get("SERIAL_NUMBER");
//            if (map.get(keysn) == null){
//                map.put((String) res.get(i).get("SERIAL_NUMBER"), res.get(i).get("TEST_TIME"));
//            }else {
//                Calendar cal1 = Calendar.getInstance();
//                cal1.setTime( (Date) map.get(keysn));
//                Calendar cal2 = Calendar.getInstance();
//                cal2.setTime((Date) res.get(i).get("TEST_TIME"));
//
//            }
//
//        }
        Map<String, Long> result = res.stream().map(e -> (String) e.get("TEST_CODE")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        result = sortMapByValue(result);
        return result;
    }

    @Override
    public  Map<String, Object> getDataWipInOutTrendChart(String factory, String parameter) throws ParseException {
        if (StringUtils.isEmpty(parameter)){
            throw  CommonException.of("parameter not undefined !!! (BALANCE_QTY  -  OVERTIME8H_QTY) ");
        }
//        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<TestModelMeta> modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        Date date = new Date();
        Calendar calendarStrart = Calendar.getInstance();
//        calendarStrart.set(Calendar.HOUR_OF_DAY, 00);
//        calendarStrart.set(Calendar.MINUTE, 00);

        Calendar calendarEnd = Calendar.getInstance();
//        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
//        calendarEnd.set(Calendar.MINUTE, 59);

        if (calendarStrart.get(Calendar.DAY_OF_WEEK) == 7){
            calendarStrart.add(Calendar.DAY_OF_YEAR, -2);
        }else {
            calendarStrart.add(Calendar.WEEK_OF_YEAR, -1);
            calendarStrart.set(Calendar.DAY_OF_WEEK, 5);
//            calendarEnd.add(Calendar.DAY_OF_YEAR, -1);
        }

        List<Map<String, Object>> data  = sfcTestSerialErrorRepository.countReport(factory, parameter, calendarStrart.getTime(), calendarEnd.getTime(), listModelByFactory(factory));

        Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < data.size() ; i++) {
            result.put((String) data.get(i).get("REPORT_DATE"), data.get(i).get("QTY"));
        }

        result = result.entrySet().stream().sorted((e1, e2) ->{
            try {
                Date date1 = simpleDateFormat.parse(e1.getKey());
                Date date2 = simpleDateFormat.parse(e2.getKey());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        return  result;

    }

    @Override
    public CommonResponse<Object> getNTFAndProcessAndCompoment(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        List<Object[]> countReasonCode = testRepairSerialErrorRepository.countByReasonByT(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.countErrorCode(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModelByFactory(factory));
        List<Map<String, Object>> totalRepair = sfcTestSerialErrorRepository.countQtySNTestError(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModelByFactory(factory));

        Map<String, Object> res = new LinkedHashMap<>();
        int countNTF = 0;
        int countProcess = 0;
        int countComponent = 0;
        Map<String, Integer> dataNTF = new HashMap<>();
        Map<String, Integer> dataComponent = new HashMap<>();
        Map<String, Integer> dataProcess = new HashMap<>();
        for (Map<String, Object> tmp: data
             ) {
            switch ((String) tmp.get("REASON_CODE")){
                case "H003":
                   countNTF +=((BigDecimal) tmp.get("QTY")).intValue();
                   dataNTF.put((String) tmp.get("MODEL_NAME"), ((BigDecimal) tmp.get("QTY")).intValue());
                    break;
                case "B000":
                   countComponent += ((BigDecimal) tmp.get("QTY")).intValue();
                    dataComponent.put((String) tmp.get("MODEL_NAME"), ((BigDecimal) tmp.get("QTY")).intValue());
                    break;
                default:
                    countProcess += ((BigDecimal) tmp.get("QTY")).intValue();
                    dataProcess.put((String) tmp.get("MODEL_NAME"), ((BigDecimal) tmp.get("QTY")).intValue());
            }
        }
        dataNTF = sortMapByValue(dataNTF);
        dataComponent = sortMapByValue(dataComponent);
        dataProcess = sortMapByValue(dataProcess);
        res.put("TOTAL", ((BigDecimal)totalRepair.get(0).get("QTY")).intValue());
        res.put("NTF", countNTF);
        res.put("DATA_NTF", dataNTF);
        res.put("COMPONENT", countComponent);
        res.put("DATA_COMPONENT", dataComponent);
        res.put("PROCESS", countProcess);
        res.put("DATA_PROCESS", dataProcess);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", res);
    }

    @Override
    public Map<String, Long> getModelNameByDefected(String factory, String timeSpan, String defected) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<String> listReason = new ArrayList<>();
        listReason.add("B000");
        listReason.add("H003");
        listReason.add("C001");
        Map<String, Long> res = new HashMap<>();
        if (defected.equalsIgnoreCase("ntf")){
            List<Object[]> data = testRepairSerialErrorRepository.countModelNameByReasonDetail(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "H003");
            for (Object[] tmp: data
                 ) {
                res.put((String) tmp[0], (Long) tmp[1]);
            }
        }else if (defected.equalsIgnoreCase("component")){
            List<Object[]> data = testRepairSerialErrorRepository.countModelNameByReasonDetail(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "B000");
            for (Object[] tmp: data
            ) {
                res.put((String) tmp[0], (Long) tmp[1]);
            }
        }else if (defected.equalsIgnoreCase("process")){
            List<Object[]> data = testRepairSerialErrorRepository.countModelNameByReason(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listReason);
            for (Object[] tmp: data
            ) {
                res.put((String) tmp[0], (Long) tmp[1]);
            }
        }
        res = sortMapByValue(res);
        return res;
    }

    @Override
    public CommonResponse<Object> getModelNameByDefectedByProcess(String factory, String timeSpan, String defected, String modelName) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<String> listReason = new ArrayList<>();
        listReason.add("B000");
        listReason.add("H003");
//        listReason.add("C001");
        Map<String, Integer> res = new HashMap<>();
        if (defected.equalsIgnoreCase("NTF")){
//            List<Object[]> data = testRepairSerialErrorRepository.countModelNameByReasonDetail2(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "H003", modelName);
            List<Map<String, Object>> dataNTF = sfcTestSerialErrorRepository.countErrorCodeByDefected(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, "H003");
            for (Map<String, Object> tmp: dataNTF
            ) {
                res.put((String) tmp.get("TEST_CODE"), ((BigDecimal) tmp.get("QTY")).intValue());
            }
        }else if (defected.equalsIgnoreCase("COMPONENT")){
//            List<Object[]> data = testRepairSerialErrorRepository.countModelNameByReasonDetail2(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "B000", modelName);
            List<Map<String, Object>> dataComponent = sfcTestSerialErrorRepository.countErrorCodeByDefected(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, "B000");
            for (Map<String, Object> tmp: dataComponent
            ) {
                res.put((String) tmp.get("ERROR_ITEM_CODE"), ((BigDecimal) tmp.get("QTY")).intValue());
            }
        }else if (defected.equalsIgnoreCase("PROCESS")){
            List<Map<String, Object>> dataProcess = sfcTestSerialErrorRepository.countErrorCodeByDefected(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, listReason);
            for (Map<String, Object> tmp: dataProcess) {
                res.put((String) tmp.get("REASON_CODE"), ((BigDecimal) tmp.get("QTY")).intValue());
            }
        }
        res = sortMapByValue(res);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", res);
    }

    @Override
    public CommonResponse<Object> getErrorCodeByReasonCodeByProcess(String factory, String timeSpan, String modelName, String reasonCode) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.countErrorCodeByReasonCode(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, reasonCode);
        Map<String, Integer> res = new HashMap<>();
        for (Map<String, Object> tmp: data
        ) {
            res.put((String) tmp.get("TEST_CODE"), ((BigDecimal) tmp.get("QTY")).intValue());
        }
        res = sortMapByValue(res);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", res);
    }

    @Override
    public  Map<String, Object> getModelNameSMTAndPTHAndSI(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        List<Object[]> dataSI = b04RepairCheckInRepository.countByModelName(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<B04RepairPTHCheckIn> dataPTH = b04RepairPTHCheckInRepository.findByInputTimeBetweenOrderByIdDesc(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<B04RepairSMTCheckIn> dataSMT = b04RepairSMTCheckInRepository.findByInputTimeBetweenOrderByIdDesc(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Long sumSI = 0L;
        Long sumSMT = 0L;
        Long sumPTH = 0L;
        Map<String, Long> resultSI = new HashMap<>();
        Map<String, Integer> resultSMT = new HashMap<>();
        Map<String, Integer> resultPTH = new HashMap<>();
        if (dataSI.size() > 0){
            for (Object[] tmp: dataSI
                 ) {
                sumSI +=(Long) tmp[1];
                resultSI.put((String) tmp[0], (Long) tmp[1]);
            }
        }
        if (dataSMT.size() > 0){
            for (B04RepairSMTCheckIn tmp: dataSMT
                 ) {
                sumSMT +=  tmp.getInputQty();
                resultSMT.put(tmp.getModelName(),tmp.getInputQty());
            }
        }
        if (dataPTH.size() > 0){
            for (B04RepairPTHCheckIn tmp: dataPTH
                 ) {
                sumPTH += tmp.getInputQty();
                resultPTH.put(tmp.getModelName(), tmp.getInputQty());
            }
        }

        resultPTH = sortMapByValue(resultPTH);
        resultSMT = sortMapByValue(resultSMT);
        resultSI = sortMapByValue(resultSI);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("dataSI", resultSI);
        res.put("qtySI", sumSI);
        res.put("dataSMT", resultSMT);
        res.put("qtySMT", sumSMT);
        res.put("dataPTH", resultPTH);
        res.put("qtyPTH", sumPTH);
        return res;
    }

    @Override
    public Map<String, Integer> getErrorCodeByModelName(String factory, String timeSpan, String modelName) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<Object[]> data = b04RepairCheckInRepository.countErrorCodeByModelName(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName);
        Map<String, Integer> res = new HashMap<>();
        for (Object[] tmp: data
             ) {
            res.put((String) tmp[0],(Integer) tmp[1]);
        }
        res = sortMapByValue(res);
        return res;
    }

    @Override
    public Map<String, Integer> getInputOutputByModelName(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        fullTimeSpan.setStartDate(calendar.getTime());
        List<Object[]> dataInput = testRepairSerialErrorRepository.countInputByModelNameByDaily(factory, calendar.getTime(), fullTimeSpan.getEndDate());
        List<Object[]> dataOutput = testRepairSerialErrorRepository.countOutputByModelNameByDaily(factory, calendar.getTime(), fullTimeSpan.getEndDate());
        Map<String, Map<String, Object>> res = new HashMap<>();
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Object[] tmp : dataInput
             ) {
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put((String) tmp[0], tmp[2]);
            res.put(simpleDateFormat.format(tmp[1]), tmpMap);
        }
        for (Object[] value : dataOutput
        ) {
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put((String) value[0], value[2]);
            result.put(simpleDateFormat.format(value[1]), tmpMap);
        }
        return null;
    }

    @Override
    public Object getErrorReasonLocationByModelName(String factory, String timeSpan, String modelName, String def) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if (def.equalsIgnoreCase("PROCESS")){
        List<String> listReason = new ArrayList<>();
        listReason.add("H003");
        listReason.add("B000");
        listReason.add("C001");
        List<Object[]> dataReason = testRepairSerialErrorRepository.countReasonCodeByModelNameByProcess(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName);
        Map<String, Integer> errorQty = new HashMap<>();
        Map<String, Integer> reasonQty = new HashMap<>();
        Map<String, Integer> locationQty = new HashMap<>();

        for (Object[] tmp : dataReason
        ) {
            reasonQty.put((String) tmp[0],(Integer) tmp[1]);
        }
        reasonQty = sortMapByValue(reasonQty);
        List<Object> responsi = new ArrayList<>();
        int stop = 0;
        for (Map.Entry<String, Integer> entry: reasonQty.entrySet()
             ) {
            Map<String,Object> result = new HashMap<>();
            int tmpStop = 0;
            List<Object[]> dataLocationByReason = testRepairSerialErrorRepository.countLocationCodeByModelNameReasonDetail(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, entry.getKey());
            Map<String, Long> res = new HashMap<>();
            for (Object[] tmp: dataLocationByReason
            ) {
                res.put((String) tmp[0],(Long) tmp[1]);
                tmpStop++;
                if (tmpStop == 15){
                    break;
                }
            }
            res = sortMapByValue(res);
            result.put("qty", entry.getValue());
            result.put("reason", entry.getKey());
            result.put("weo", def);
            result.put("data", res);
            responsi.add(result);
            stop ++;
            if (stop == 15){
                break;
            }
        }
            return responsi;
        }else if (def.equalsIgnoreCase("NTF")){
             List<Object[]> dataError = testRepairSerialErrorRepository.countErrorCodeByModelNameByProcess(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName, "H003");
            Map<String, Integer> errorQty = new HashMap<>();
            for (Object[] tmp : dataError
             ) {
                errorQty.put((String) tmp[0],(Integer) tmp[1]);
             }
            errorQty = sortMapByValue(errorQty);

             return errorQty;
        }else if (def.equalsIgnoreCase("COMPONENT")){
                List<Object[]> dataLocation = testRepairSerialErrorRepository.countLocationCodeByModelNameByProcess(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelName);
            Map<String, Integer> locationQty = new HashMap<>();
            for (Object[] tmp : dataLocation
                ) {
                    locationQty.put((String) tmp[0],(Integer) tmp[1]);
                }
                locationQty= sortMapByValue(locationQty);
            return locationQty;
        }else {
            return false;
        }
    }

    @Override
    public void syncDataBC8MCheckIn(Date start, Date end) throws ParserConfigurationException, SAXException, ParseException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GET_BC8M_IN xmlns=\"http://tempuri.org/\">\n" +
                "      <start_time>" + dateFormat.format(start) + "</start_time>\n" +
                "      <end_time>" + dateFormat.format(end) + "</end_time>\n" +
                "    </GET_BC8M_IN>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.81.53/b04/Servicepostdata.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### DuongTungError01 : ", e);
            return;
        }
        TestModelList testModelList = testModelListRepository.findTop1ByFactory("B04");
        List<String> modelList = testModelList != null ? testModelList.getModelNames() : Collections.emptyList();
        Map<String, Object> objectList = parseReCheckInFromXmlB04(responseEntity.getBody(), modelList);

        log.info("###size-beforeCheckIn: "+ (((List<RepairBC8MCheckInIndexUnique>) objectList.get("dataIndex")).size()));
        List<RepairBC8MCheckInIndexUnique> dt = (List<RepairBC8MCheckInIndexUnique>) objectList.get("dataIndex");
        dt = dt.stream().sorted((e1, e2)  -> e1.getInStationTime().compareTo(e2.getInStationTime())).collect(Collectors.toList());
        List<RepairBC8MCheckIn> dt1 = (List<RepairBC8MCheckIn>)objectList.get("dataRaw");
        dt1 = dt1.stream().sorted((e1, e2)  -> e1.getInStationTime().compareTo(e2.getInStationTime())).collect(Collectors.toList());
        try {
            saveAllBC8MCheckIn(dt);
        }catch (Exception e){
            log.error("### DuongTungError03 : ", e);
        }
        log.info("###size-beforeCheckIn Raw: "+ (((List<RepairBC8MCheckIn>) objectList.get("dataRaw")).size()));
        try {
            repairBC8MCheckInRepository.saveAll(dt1);
        }catch (Exception e){
            log.error("### DuongTungError04 : ", e);
        }
    }

    private Map<String, Object> parseReCheckInFromXmlB04(String xml, List<String>modelList) {
        if (StringUtils.isEmpty(xml)) {
            log.info("### DuongTungError11");
            return (Map<String, Object>) Collections.emptyList();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Table");

            List<RepairBC8MCheckIn> repairBC8MCheckInList = new ArrayList<>();
            List<RepairBC8MCheckInIndexUnique> mData = new ArrayList<>();

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                String descrip = "";String versionCode = ""; String lineName = ""; String sectionName = "";
                String groupName = ""; String stationName = ""; String location= ""; String stationSeq = null;  String errorFlag = null;
                String scrapFlag = null; String wipGroup = "";String testCode = "";String empNo = "";String keyPartNo = ""; String customerNo = "";String bomNo= "";
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String serialNumber = eElement.getElementsByTagName("SERIAL_NUMBER").item(0).getTextContent();
                    String moNumber = eElement.getElementsByTagName("MO_NUMBER").item(0).getTextContent();
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String inStationTime = eElement.getElementsByTagName("IN_STATION_TIME").item(0).getTextContent();
                    if (eElement.getElementsByTagName("VERSION_CODE").getLength() > 0){
                        versionCode = eElement.getElementsByTagName("VERSION_CODE").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("LINE_NAME").getLength() > 0){
                        lineName = eElement.getElementsByTagName("LINE_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("SECTION_NAME").getLength() > 0){
                        sectionName = eElement.getElementsByTagName("SECTION_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("GROUP_NAME").getLength() > 0){
                        groupName = eElement.getElementsByTagName("GROUP_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("STATION_NAME").getLength() > 0){
                        stationName = eElement.getElementsByTagName("STATION_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("LOCATION").getLength() > 0){
                        location = eElement.getElementsByTagName("LOCATION").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("STATION_SEQ").getLength() > 0){
                        stationSeq = eElement.getElementsByTagName("STATION_SEQ").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("ERROR_FLAG").getLength() > 0){
                        errorFlag = eElement.getElementsByTagName("ERROR_FLAG").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("SCRAP_FLAG").getLength() > 0){
                        scrapFlag = eElement.getElementsByTagName("SCRAP_FLAG").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("CUSTOMER_NO").getLength() > 0){
                        customerNo = eElement.getElementsByTagName("CUSTOMER_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("BOM_NO").getLength() > 0){
                        bomNo = eElement.getElementsByTagName("BOM_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("KEY_PART_NO").getLength() > 0){
                        keyPartNo = eElement.getElementsByTagName("KEY_PART_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("EMP_NO").getLength() > 0){
                        empNo = eElement.getElementsByTagName("EMP_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("WIP_GROUP").getLength() > 0){
                        wipGroup = eElement.getElementsByTagName("WIP_GROUP").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("TEST_CODE").getLength() > 0){
                        testCode = eElement.getElementsByTagName("TEST_CODE").item(0).getTextContent();
                    }
                     if (eElement.getElementsByTagName("DESCRIP").getLength() > 0){
                         descrip = eElement.getElementsByTagName("DESCRIP").item(0).getTextContent();
                     }
                    String testGroup = eElement.getElementsByTagName("TEST_GROUP").item(0).getTextContent();
                    String testTime = eElement.getElementsByTagName("TEST_TIME").item(0).getTextContent();

                    String factoryName = "B04";
                    if (!modelList.contains(modelName)) {
                        factoryName = "B03";
                    }

                    RepairBC8MCheckIn repairBC8MCheckInRaw = new RepairBC8MCheckIn();

                    repairBC8MCheckInRaw.setFactory(factoryName);
                    repairBC8MCheckInRaw.setSerialNumber(serialNumber);
                    repairBC8MCheckInRaw.setMoNumber(moNumber);
                    repairBC8MCheckInRaw.setModelName(modelName);
                    repairBC8MCheckInRaw.setVersionCode(versionCode);
                    repairBC8MCheckInRaw.setLineName(lineName);
                    repairBC8MCheckInRaw.setSectionName(sectionName);
                    repairBC8MCheckInRaw.setGroupName(groupName);
                    repairBC8MCheckInRaw.setStationName(stationName);
                    repairBC8MCheckInRaw.setLocation(location);
                    repairBC8MCheckInRaw.setStationSEQ(Integer.parseInt(stationSeq));
                    repairBC8MCheckInRaw.setErrorFlag(Integer.parseInt(errorFlag));
                    repairBC8MCheckInRaw.setInStationTime(simpleDateFormat.parse(inStationTime));
                    repairBC8MCheckInRaw.setScrapFlag(Integer.parseInt(scrapFlag));
                    repairBC8MCheckInRaw.setCustomerNo(customerNo);
                    repairBC8MCheckInRaw.setBomNo(bomNo);
                    repairBC8MCheckInRaw.setKeyPartNo(keyPartNo);
                    repairBC8MCheckInRaw.setEmpNo(empNo);
                    repairBC8MCheckInRaw.setWipGroup(wipGroup);
                    repairBC8MCheckInRaw.setTestCode(testCode);
                    repairBC8MCheckInRaw.setDescription(descrip);
                    repairBC8MCheckInRaw.setTestGroup(testGroup);
                    repairBC8MCheckInRaw.setTestTime(simpleDateFormat.parse(testTime));
                    repairBC8MCheckInList.add(repairBC8MCheckInRaw);
                    ////////////////////////////////////
                    RepairBC8MCheckInIndexUnique repairBC8MCheckIn = new RepairBC8MCheckInIndexUnique();
                    repairBC8MCheckIn.setFactory(factoryName);
                    repairBC8MCheckIn.setSerialNumber(serialNumber);
                    repairBC8MCheckIn.setMoNumber(moNumber);
                    repairBC8MCheckIn.setModelName(modelName);
                    repairBC8MCheckIn.setVersionCode(versionCode);
                    repairBC8MCheckIn.setLineName(lineName);
                    repairBC8MCheckIn.setSectionName(sectionName);
                    repairBC8MCheckIn.setGroupName(groupName);
                    repairBC8MCheckIn.setStationName(stationName);
                    repairBC8MCheckIn.setLocation(location);
                    repairBC8MCheckIn.setInStationTime(simpleDateFormat.parse(inStationTime));
                    repairBC8MCheckIn.setCustomerNo(customerNo);
                    repairBC8MCheckIn.setBomNo(bomNo);
                    repairBC8MCheckIn.setKeyPartNo(keyPartNo);
                    repairBC8MCheckIn.setEmpNo(empNo);
                    repairBC8MCheckIn.setWipGroup(wipGroup);
                    repairBC8MCheckIn.setTestCode(testCode);
                    repairBC8MCheckIn.setDescription(descrip);
                    repairBC8MCheckIn.setTestGroup(testGroup);
                    repairBC8MCheckIn.setTestTime(simpleDateFormat.parse(testTime));
                    repairBC8MCheckIn.setOutStationTime(null);
                    mData.add(repairBC8MCheckIn);
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("dataRaw", repairBC8MCheckInList);
            data.put("dataIndex", mData);
            return data;
        } catch (Exception e) {
            log.error("### DuongTungError10 : ", e);
            return (Map<String, Object>) Collections.emptyList();
        }
    }

    public int[] saveAllBC8MCheckIn(List<RepairBC8MCheckInIndexUnique> groupList) {
        if (groupList.isEmpty()) {
            log.error("### DuongTungError08");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into re_bc8m_check_in_unique_index as target " +
                        "using(select factory=?, serial_number=?, mo_number=?, model_name=?, version_code=?, line_name=?, section_name=?, group_name=?, station_name=?, in_station_time=?, in_line_time=?, customer_no=?, bom_no=?, key_part_no=?, emp_no=?, wip_group=?, test_code=?, descrip=?, test_group=?, test_time=?, location=?, updated_at=?, created_at=?, out_station_time=?) as source " +
                        "   on target.SERIAL_NUMBER=source.SERIAL_NUMBER " +
                        "when matched then " +
                        "   update set " +
                        "   target.TEST_TIME=source.TEST_TIME, " +
                        "   target.TEST_GROUP=source.TEST_GROUP, " +
                        "   target.IN_STATION_TIME=source.IN_STATION_TIME, " +
                        "   target.OUT_STATION_TIME=source.OUT_STATION_TIME, " +
                        "   target.UPDATED_AT=source.UPDATED_AT " +
                        "when not matched then " +
                        "   insert (FACTORY, SERIAL_NUMBER, MO_NUMBER, MODEL_NAME, VERSION_CODE, LINE_NAME, SECTION_NAME, GROUP_NAME, STATION_NAME, IN_STATION_TIME, IN_LINE_TIME, CUSTOMER_NO, BOM_NO, KEY_PART_NO, EMP_NO, WIP_GROUP, TEST_CODE, DESCRIP, TEST_GROUP, TEST_TIME, LOCATION, UPDATED_AT, CREATED_AT, OUT_STATION_TIME) " +
                        "   values(source.FACTORY, source.SERIAL_NUMBER, source.MO_NUMBER, source.MODEL_NAME, source.VERSION_CODE, source.LINE_NAME, source.SECTION_NAME, source.GROUP_NAME, source.STATION_NAME, source.IN_STATION_TIME, source.IN_LINE_TIME, source.CUSTOMER_NO, source.BOM_NO, source.KEY_PART_NO,source.EMP_NO, source.WIP_GROUP, source.TEST_CODE, source.DESCRIP, source.TEST_GROUP, source.TEST_TIME, source.LOCATION, source.UPDATED_AT, source.CREATED_AT, null);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        RepairBC8MCheckInIndexUnique group = groupList.get(i);
                        preparedStatement.setString(1, group.getFactory());
                        preparedStatement.setString(2, group.getSerialNumber());
                        preparedStatement.setString(3, group.getMoNumber());
                        preparedStatement.setString(4, group.getModelName());
                        preparedStatement.setString(5, group.getVersionCode());
                        preparedStatement.setString(6, group.getLineName());
                        preparedStatement.setString(7, group.getSectionName());
                        preparedStatement.setString(8, group.getGroupName());
                        preparedStatement.setString(9, group.getStationName());
                        preparedStatement.setTimestamp(10, new Timestamp(group.getInStationTime().getTime()));
                        preparedStatement.setTimestamp(11, null);
                        preparedStatement.setString(12, group.getCustomerNo());
                        preparedStatement.setString(13, group.getBomNo());
                        preparedStatement.setString(14, group.getKeyPartNo());
                        preparedStatement.setString(15, group.getEmpNo());
                        preparedStatement.setString(16, group.getWipGroup());
                        preparedStatement.setString(17, group.getTestCode());
                        preparedStatement.setString(18, group.getDescription());
                        preparedStatement.setString(19, group.getTestGroup());
                        preparedStatement.setTimestamp(20, new Timestamp(group.getTestTime().getTime()));
                        preparedStatement.setString(21, group.getLocation());
                        preparedStatement.setTimestamp(22, new Timestamp(new Date().getTime()));
                        preparedStatement.setTimestamp(23, new Timestamp(new Date().getTime()));
                        preparedStatement.setTimestamp(24, null);
                    }

                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }

    @Override
    public void syncDataBC8MCheckOut(Date start, Date end) throws ParserConfigurationException, SAXException, ParseException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GET_BC8M_OUT xmlns=\"http://tempuri.org/\">\n" +
                "      <start_time>" + dateFormat.format(start) + "</start_time>\n" +
                "      <end_time>" + dateFormat.format(end) + "</end_time>\n" +
                "    </GET_BC8M_OUT>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity;
        try {
        responseEntity = restTemplate.exchange("http://10.224.81.53/b04/Servicepostdata.asmx", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### DuongTungError02 : ", e);
            return;
        }
        TestModelList testModelList = testModelListRepository.findTop1ByFactory("B04");
        List<String> modelList = testModelList != null ? testModelList.getModelNames() : Collections.emptyList();

        Map<String, Object> objectListOut = parseReCheckOutFromXmlB04(responseEntity.getBody(), modelList);
        log.info("###size-beforeCheckOut: "+ (((List<RepairBC8MCheckInIndexUnique>) objectListOut.get("dataIndex")).size()));
        List<RepairBC8MCheckOut> dt = (List<RepairBC8MCheckOut>)objectListOut.get("dataRaw");
        dt = dt.stream().sorted((e1, e2) -> e1.getInStationTime().compareTo(e2.getInStationTime())).collect(Collectors.toList());
        try {
            updateAllBC8MCheckOut((List<RepairBC8MCheckInIndexUnique>) objectListOut.get("dataIndex"));
        }catch (Exception e){
            log.error("### DuongTungError05 : ", e);
        }
        log.info("###size-beforeCheckOut Raw: "+ (((List<RepairBC8MCheckOut>) objectListOut.get("dataRaw")).size()));
        try {
            repairBC8MCheckOutRepository.saveAll(dt);
        }catch (Exception e){
            log.error("### DuongTungError06 : ", e);
        }
    }

    private Map<String, Object> parseReCheckOutFromXmlB04(String xml, List<String>modelList) {
        if (StringUtils.isEmpty(xml)) {
            log.info("### DuongTungError12");
            return (Map<String, Object>) Collections.emptyList();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Table");

            List<RepairBC8MCheckOut> metaDataBC8MCheckOut = new ArrayList<>();
            List<RepairBC8MCheckInIndexUnique> mData = new ArrayList<>();

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                String descrip = "";String versionCode = ""; String lineName = ""; String sectionName = "";
                String groupName = ""; String stationName = ""; String location= ""; String stationSeq = null;  String errorFlag = null;
                String scrapFlag = null; String wipGroup = "";String testCode = "";String empNo = "";String keyPartNo = ""; String customerNo = "";String bomNo= "";
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String serialNumber = eElement.getElementsByTagName("SERIAL_NUMBER").item(0).getTextContent();
                    String moNumber = eElement.getElementsByTagName("MO_NUMBER").item(0).getTextContent();
                    String modelName = eElement.getElementsByTagName("MODEL_NAME").item(0).getTextContent();
                    String inStationTime = eElement.getElementsByTagName("IN_STATION_TIME").item(0).getTextContent();
                    if (eElement.getElementsByTagName("VERSION_CODE").getLength() > 0){
                        versionCode = eElement.getElementsByTagName("VERSION_CODE").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("LINE_NAME").getLength() > 0){
                        lineName = eElement.getElementsByTagName("LINE_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("SECTION_NAME").getLength() > 0){
                        sectionName = eElement.getElementsByTagName("SECTION_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("GROUP_NAME").getLength() > 0){
                        groupName = eElement.getElementsByTagName("GROUP_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("STATION_NAME").getLength() > 0){
                        stationName = eElement.getElementsByTagName("STATION_NAME").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("LOCATION").getLength() > 0){
                        location = eElement.getElementsByTagName("LOCATION").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("STATION_SEQ").getLength() > 0){
                        stationSeq = eElement.getElementsByTagName("STATION_SEQ").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("ERROR_FLAG").getLength() > 0){
                        errorFlag = eElement.getElementsByTagName("ERROR_FLAG").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("SCRAP_FLAG").getLength() > 0){
                        scrapFlag = eElement.getElementsByTagName("SCRAP_FLAG").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("CUSTOMER_NO").getLength() > 0){
                        customerNo = eElement.getElementsByTagName("CUSTOMER_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("BOM_NO").getLength() > 0){
                        bomNo = eElement.getElementsByTagName("BOM_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("KEY_PART_NO").getLength() > 0){
                        keyPartNo = eElement.getElementsByTagName("KEY_PART_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("EMP_NO").getLength() > 0){
                        empNo = eElement.getElementsByTagName("EMP_NO").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("WIP_GROUP").getLength() > 0){
                        wipGroup = eElement.getElementsByTagName("WIP_GROUP").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("TEST_CODE").getLength() > 0){
                        testCode = eElement.getElementsByTagName("TEST_CODE").item(0).getTextContent();
                    }
                    if (eElement.getElementsByTagName("DESCRIP").getLength() > 0){
                        descrip = eElement.getElementsByTagName("DESCRIP").item(0).getTextContent();
                    }
                    String testTime = eElement.getElementsByTagName("TEST_TIME").item(0).getTextContent();
                    String factoryName = "B04";
                    if (!modelList.contains(modelName)) {
                        factoryName = "B03";
                    }
                    RepairBC8MCheckOut repairBC8MCheckOutRaw = new RepairBC8MCheckOut();
                    repairBC8MCheckOutRaw.setFactory(factoryName);
                    repairBC8MCheckOutRaw.setSerialNumber(serialNumber);
                    repairBC8MCheckOutRaw.setMoNumber(moNumber);
                    repairBC8MCheckOutRaw.setModelName(modelName);
                    repairBC8MCheckOutRaw.setVersionCode(versionCode);
                    repairBC8MCheckOutRaw.setLineName(lineName);
                    repairBC8MCheckOutRaw.setSectionName(sectionName);
                    repairBC8MCheckOutRaw.setGroupName(groupName);
                    repairBC8MCheckOutRaw.setStationName(stationName);
                    repairBC8MCheckOutRaw.setLocation(location);
                    repairBC8MCheckOutRaw.setStationSEQ(Integer.parseInt(stationSeq));
                    repairBC8MCheckOutRaw.setErrorFlag(Integer.parseInt(errorFlag));
                    repairBC8MCheckOutRaw.setInStationTime(simpleDateFormat.parse(inStationTime));
                    repairBC8MCheckOutRaw.setScrapFlag(Integer.parseInt(scrapFlag));
                    repairBC8MCheckOutRaw.setCustomerNo(customerNo);
                    repairBC8MCheckOutRaw.setBomNo(bomNo);
                    repairBC8MCheckOutRaw.setKeyPartNo(keyPartNo);
                    repairBC8MCheckOutRaw.setEmpNo(empNo);
                    repairBC8MCheckOutRaw.setWipGroup(wipGroup);
                    repairBC8MCheckOutRaw.setTestCode(testCode);
                    repairBC8MCheckOutRaw.setDescription(descrip);
                    repairBC8MCheckOutRaw.setTestTime(simpleDateFormat.parse(testTime));
                    metaDataBC8MCheckOut.add(repairBC8MCheckOutRaw);
                    ////////////////////////////////////
                    RepairBC8MCheckInIndexUnique repairBC8MCheckInIndexUnique = new RepairBC8MCheckInIndexUnique();
                    repairBC8MCheckInIndexUnique.setFactory(factoryName);
                    repairBC8MCheckInIndexUnique.setSerialNumber(serialNumber);
                    repairBC8MCheckInIndexUnique.setMoNumber(moNumber);
                    repairBC8MCheckInIndexUnique.setModelName(modelName);
                    repairBC8MCheckInIndexUnique.setVersionCode(versionCode);
                    repairBC8MCheckInIndexUnique.setLineName(lineName);
                    repairBC8MCheckInIndexUnique.setSectionName(sectionName);
                    repairBC8MCheckInIndexUnique.setGroupName(groupName);
                    repairBC8MCheckInIndexUnique.setStationName(stationName);
                    repairBC8MCheckInIndexUnique.setLocation(location);
                    repairBC8MCheckInIndexUnique.setCustomerNo(customerNo);
                    repairBC8MCheckInIndexUnique.setBomNo(bomNo);
                    repairBC8MCheckInIndexUnique.setKeyPartNo(keyPartNo);
                    repairBC8MCheckInIndexUnique.setEmpNo(empNo);
                    repairBC8MCheckInIndexUnique.setWipGroup(wipGroup);
                    repairBC8MCheckInIndexUnique.setTestCode(testCode);
                    repairBC8MCheckInIndexUnique.setDescription(descrip);
                    repairBC8MCheckInIndexUnique.setTestTime(simpleDateFormat.parse(testTime));
                    repairBC8MCheckInIndexUnique.setOutStationTime(simpleDateFormat.parse(inStationTime));
                    mData.add(repairBC8MCheckInIndexUnique);
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("dataRaw", metaDataBC8MCheckOut);
            data.put("dataIndex", mData);
            return data;
        } catch (Exception e) {
            log.error("### DuongTungError09 :", e);
            return (Map<String, Object>) Collections.emptyList();
        }
    }
    public int[] updateAllBC8MCheckOut(List<RepairBC8MCheckInIndexUnique> groupList) {
        if (groupList.isEmpty()) {
            log.info("### DuongTungError07");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into re_bc8m_check_in_unique_index as target " +
                        "using(select factory=?, serial_number=?, mo_number=?, model_name=?, version_code=?, line_name=?, section_name=?, group_name=?, station_name=?, in_station_time=?, in_line_time=?, customer_no=?, bom_no=?, key_part_no=?, emp_no=?, wip_group=?, test_code=?, descrip=?, test_group=?, test_time=?, location=?, updated_at=?, created_at=?, out_station_time=?) as source " +
                        "   on target.SERIAL_NUMBER=source.SERIAL_NUMBER and target.IN_STATION_TIME < source.OUT_STATION_TIME " +
                        "when matched then " +
                        "   update set " +
                        "   target.TEST_TIME=source.TEST_TIME, " +
                        "   target.OUT_STATION_TIME=source.OUT_STATION_TIME, " +
                        "   target.TEST_CODE=source.TEST_CODE, " +
                        "   target.UPDATED_AT=source.UPDATED_AT ;",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        RepairBC8MCheckInIndexUnique group = groupList.get(i);
                        preparedStatement.setString(1, group.getFactory());
                        preparedStatement.setString(2, group.getSerialNumber());
                        preparedStatement.setString(3, group.getMoNumber());
                        preparedStatement.setString(4, group.getModelName());
                        preparedStatement.setString(5, group.getVersionCode());
                        preparedStatement.setString(6, group.getLineName());
                        preparedStatement.setString(7, group.getSectionName());
                        preparedStatement.setString(8, group.getGroupName());
                        preparedStatement.setString(9, group.getStationName());
                        preparedStatement.setTimestamp(10, null);
                        preparedStatement.setTimestamp(11, null);
                        preparedStatement.setString(12, group.getCustomerNo());
                        preparedStatement.setString(13, group.getBomNo());
                        preparedStatement.setString(14, group.getKeyPartNo());
                        preparedStatement.setString(15, group.getEmpNo());
                        preparedStatement.setString(16, group.getWipGroup());
                        preparedStatement.setString(17, group.getTestCode());
                        preparedStatement.setString(18, group.getDescription());
                        preparedStatement.setString(19, group.getTestGroup());
                        preparedStatement.setTimestamp(20, new Timestamp(group.getTestTime().getTime()));
                        preparedStatement.setString(21, group.getLocation());
                        preparedStatement.setTimestamp(22, new Timestamp(new Date().getTime()));
                        preparedStatement.setTimestamp(23, new Timestamp(new Date().getTime()));
                        preparedStatement.setTimestamp(24, new Timestamp(group.getOutStationTime().getTime()));
                    }
                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }

    @Override
    public Object getDataBC8MRemain(String factory, String timeSpan) throws ParseException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
  //      List<Object[]> dataCheckIn = repairBC8MCheckInInUniqueRepository.countModelName(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
    //    List<Object[]> dataRemain = repairBC8MCheckInInUniqueRepository.countModelNameRemain(factory,simpleDateFormat.parse("2020/01/01"), fullTimeSpan.getEndDate());
   //     List<Object[]> dataOutput = repairBC8MCheckInInUniqueRepository.countOutputModelName(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<Map<String, Object>> remainSfc = sfcTestSerialErrorRepository.getRemainBc8m(factory, listModelByFactory(factory));
        List<Map<String, Object>> checkInSfc = sfcTestSerialErrorRepository.getCheckInBc8m(factory, listModelByFactory(factory), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<Map<String, Object>> checkOutSfc = sfcTestSerialErrorRepository.getCheckOutBc8m(factory, listModelByFactory(factory), fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        Map<String, Object> result = new HashMap<>();
        long sumCountOutput = 0;
        Map<String, Long> output = new LinkedHashMap<>();
        if (checkOutSfc.size() > 0){
            for (Map<String, Object> tmp: checkOutSfc) {
                sumCountOutput += ((BigDecimal)tmp.get("QTY")).longValue();
                output.put((String) tmp.get("MODEL_NAME"), ((BigDecimal)tmp.get("QTY")).longValue());
            }
        }
        output = sortMapByValue(output);
        result.put("totalOutput", sumCountOutput);
        result.put("dataOutput",output);
        long sumCountRemain = 0;
        Map<String, Long> remain = new LinkedHashMap<>();
        if (remainSfc.size() > 0){
            for (Map<String, Object> tmp:remainSfc){
                sumCountRemain += ((BigDecimal)tmp.get("QTY")).longValue();
                remain.put((String) tmp.get("MODEL_NAME"), ((BigDecimal)tmp.get("QTY")).longValue());
            }
        }
        remain = sortMapByValue(remain);
        result.put("totalRemain", sumCountRemain);
        result.put("dataRemain",remain);

        Map<String, Long> input = new LinkedHashMap<>();
        long sumCountInput = 0;
        if (checkInSfc.size() > 0){
            for (Map<String, Object> tmp:checkInSfc){
                sumCountInput += ((BigDecimal)tmp.get("QTY")).longValue();
                input.put((String) tmp.get("MODEL_NAME"), ((BigDecimal)tmp.get("QTY")).longValue());
            }
        }
        input = sortMapByValue(input);
        result.put("totalInput", sumCountInput);
        result.put("dataInput",input);
        return result;
    }

    @Override
    public Object getTop15ErrorCode(String factory, String modelName) throws ParseException {
//        List<Object[]> data = repairBC8MCheckInInUniqueRepository.countErrorCode(factory, modelName);
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getErrorCodeBc8m(factory, modelName);
        Map<String, Long> res = new HashMap<>();
        if (data.size() > 0){
            for (Map<String, Object> tmp: data
            ) {
                res.put((String) tmp.get("TEST_CODE"), ((BigDecimal)tmp.get("QTY")).longValue());

            }
        }
        res = sortMapByValue(res);
        return res;
    }

    @Override
    public Object getInOutTrendChart(String factory, String timeSpan) throws ParseException, IOException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getEndDate());
        calendar.add(Calendar.DAY_OF_MONTH, -28);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        fullTimeSpan.setStartDate(calendar.getTime());
//        List<Object[]> dataIn = repairBC8MCheckInInUniqueRepository.countModelNameInputByDay(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        List<Object[]> dataOut = repairBC8MCheckInInUniqueRepository.countModelNameOutputByDay(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        List<RepairBC8MCheckInIndexUnique> dt = repairBC8MCheckInInUniqueRepository.findByFactoryAndInStationTimeBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<Object[]> data = repairBC8MCheckInInUniqueRepository.countModelNameStatusByDay(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<Object[]> dataCheckOut = repairBC8MCheckInInUniqueRepository.countModelNameStatusByDayCheckOut(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        Map<String, Integer> tmpMap = new LinkedHashMap<>();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(fullTimeSpan.getEndDate());
        Integer flail = 0;
        String time1 = "";
        String time2 = "";
        String time3 = "";
        Map<String, Object> mMap1 = new LinkedHashMap<>();
        Map<String, Object> mMap2= new LinkedHashMap<>();
        Map<String, Object> mMap3= new LinkedHashMap<>();
        for (int i = 1; i <= 28; i++) {
           if (flail < 3){
                Date startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
               Calendar calendar144 = Calendar.getInstance();
               calendar144.setTime(calendar.getTime());
               calendar144.add(Calendar.DAY_OF_YEAR, 1);
                if (flail == 0){
                    time1 = simpleDateFormat.format(startDate)+"-"+ simpleDateFormat.format(calendar144.getTime());
                }
               if (flail == 1){
                   time2 = simpleDateFormat.format(startDate)+"-"+ simpleDateFormat.format(calendar144.getTime());
               }
               if (flail == 2){
                   time3 = simpleDateFormat.format(startDate)+"-"+ simpleDateFormat.format(calendar144.getTime());
               }
               mMap1.put(simpleDateFormat.format(startDate)+"-"+ simpleDateFormat.format(calendar144.getTime()), 0);
                flail ++;
            }
            if (flail == 3){
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                mMap2.put(simpleDateFormat.format(calendar.getTime()), 0);
                mMap3.put(simpleDateFormat.format(calendar.getTime()), 0);
            }
            if (calendar.get(Calendar.DAY_OF_YEAR) == calendarEnd.get(Calendar.DAY_OF_YEAR)){
                break;
            }
        }
        Integer count1 = 0;
        Integer count2= 0;
        Integer count3 = 0;
        Integer count11 = 0;
        Integer count22= 0;
        Integer count33 = 0;
        Map<String, Integer> tmpData = new HashMap<>();
        Map<String, Integer> tmpDataOut = new HashMap<>();
        for (Object[] tmp: data) {
            String beforeTime1 = time1.split("-")[0];
            String afterTime1 = time1.split("-")[1];
            String beforeTime2 = time2.split("-")[0];
            String afterTime2 = time2.split("-")[1];
            String beforeTime3 = time3.split("-")[0];
            String afterTime3 = time3.split("-")[1];
            String dd = simpleDateFormat.format((Date) tmp[0]);
            Date tmpTime = simpleDateFormat.parse(dd);

            if (tmpTime.before(simpleDateFormat.parse(afterTime1)) && tmpTime.after(simpleDateFormat.parse(beforeTime1))){
                count1 +=(Integer) tmp[1];
            }
            if (tmpTime.before(simpleDateFormat.parse(afterTime2)) && tmpTime.after(simpleDateFormat.parse(beforeTime2))){
                count2 +=(Integer) tmp[1];
            }
            if (tmpTime.before(simpleDateFormat.parse(afterTime3)) && tmpTime.after(simpleDateFormat.parse(beforeTime3))){
                count3 +=(Integer) tmp[1];
            }
            tmpData.put(dd,(Integer) tmp[1]);
        }
        for (Object[] tmp: dataCheckOut) {
            String beforeTime1 = time1.split("-")[0];
            String afterTime1 = time1.split("-")[1];
            String beforeTime2 = time2.split("-")[0];
            String afterTime2 = time2.split("-")[1];
            String beforeTime3 = time3.split("-")[0];
            String afterTime3 = time3.split("-")[1];
            String dd = simpleDateFormat.format((Date) tmp[0]);
            Date tmpTime = simpleDateFormat.parse(dd);

            if (tmpTime.before(simpleDateFormat.parse(afterTime1)) && tmpTime.after(simpleDateFormat.parse(beforeTime1))){
                count11 +=(Integer) tmp[1];
            }
            if (tmpTime.before(simpleDateFormat.parse(afterTime2)) && tmpTime.after(simpleDateFormat.parse(beforeTime2))){
                count22 +=(Integer) tmp[1];
            }
            if (tmpTime.before(simpleDateFormat.parse(afterTime3)) && tmpTime.after(simpleDateFormat.parse(beforeTime3))){
                count33 +=(Integer) tmp[1];
            }
            tmpDataOut.put(dd,(Integer) tmp[1]);
        }
        for (Map.Entry<String, Object> k :mMap2.entrySet()){
            if (tmpData.get(k.getKey()) != null){
                k.setValue(tmpData.get(k.getKey()));
            }
        }
        for (Map.Entry<String, Object> k: mMap3.entrySet()){
            if (tmpDataOut.get(k.getKey()) != null){
                k.setValue(tmpDataOut.get(k.getKey()));
            }
        }
        List<Map<String, Map<String, Object>>> result = new ArrayList<>();

            Map<String, Map<String, Object>> res1 = new HashMap<>();
            Map<String, Object> mRes1 = new HashMap<>();
            mRes1.put("input", count1);
            mRes1.put("output", count11);
            res1.put(time1, mRes1);
            result.add(res1);

            Map<String, Map<String, Object>> res2 = new HashMap<>();
            Map<String, Object> mRes2 = new HashMap<>();
            mRes2.put("input", count2);
            mRes2.put("output", count22);
            res2.put(time2, mRes2);
            result.add(res2);

            Map<String, Map<String, Object>> res3 = new HashMap<>();
            Map<String, Object> mRes3 = new HashMap<>();
            mRes3.put("input", count3);
            mRes3.put("output", count33);
            res3.put(time3, mRes3);
            result.add(res3);
        for (Map.Entry<String, Object> t: mMap2.entrySet()
             ) {
            Map<String, Map<String, Object>> tmpRes = new HashMap<>();
            Map<String, Object> tmpRes1 = new HashMap<>();
            tmpRes1.put("input", t.getValue());
            tmpRes1.put("output", mMap3.get(t.getKey()));
            tmpRes.put(t.getKey(), tmpRes1);
            result.add(tmpRes);
        }
        return result;
    }

    @Override
    public void exportDataCheckOutByUserCapacity(HttpServletResponse resonse, String factory, String timeSpan) throws ParseException, IOException {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
//        List<B04Resource> userRe = b04ResourceRepository.findByDem("RE");
        List<ReInfoResource> userRe = reInfoResourceRepository.findByDepartment("RE");
        Set<String> empNoSet = userRe.stream().map(element -> {return element.getEmpNo();}).collect(Collectors.toSet());
        Map<String, String> userMap = new HashMap<>();
        for (ReInfoResource tmpB04User : userRe){
            userMap.put(tmpB04User.getEmpNo(),tmpB04User.getNameEse());
        }

        List<TestModelMeta> modelMeta;
        Map<String, Integer> result = new LinkedHashMap<>();
        if (factory.equalsIgnoreCase("S03")){
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        }else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        List<Map<String, Object>> dataOutput  = sfcTestSerialErrorRepository.getCheckOut(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

       // List<TestRepairSerialError> data = testRepairSerialErrorRepository.findByFactoryAndStatusAndRepairTimeBetween(factory, TestRepairSerialError.Status.REPAIRED, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
//        data = data.stream().filter(e -> (empNoSet.contains(e.getRepairer()))).collect(Collectors.toList());
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {
                "Factory"
                ,"SERIAL NUMBER"
                ,"MODEL NAME"
                ,"MO NUMBER"
                ,"STATION NAME"
                ,"LINE NAME"
                ,"TEST CODE"
                ,"IN DATETIME"
                ,"OUT DATETIME"
                ,"REASON CODE"
                ,"FAIL LOCATION"
                ,"LOCATION CODE"
                ,"REPAIRER"
                ,"REPAIR NAME"
                };

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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
        String dd = "checkOut-"+df2.format(fullTimeSpan.getStartDate())+"-"+df2.format(fullTimeSpan.getEndDate());
        HSSFSheet sheet = workbook.createSheet(dd);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (Map<String, Object> dt: dataOutput) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(factory);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SERIAL_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MODEL_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MO_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("STATION_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("LINE_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_CODE"));
            cellNum++;

            String strDate = "";
            if (dt.get("IN_DATETIME") != null){
                strDate = df.format(dt.get("IN_DATETIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(strDate);
            cellNum++;

            String outDate = "";
            if (dt.get("OUT_DATETIME") != null){
                outDate = df.format(dt.get("OUT_DATETIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(outDate);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("REASON_CODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("FAIL_LOCATION"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("LOCATION_CODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String)dt.get("REPAIRER"));
            cellNum++;

            String userName = userMap.get(dt.get("REPAIRER"));
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(userName);
            cellNum++;

//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getShift());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getSerialNumber());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getSectionName());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getCreatedAt());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getUpdatedAt());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getLocationCode());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getMo());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getRepairer());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(userMap.get(dt.getRepairer()));
//            cellNum++;
//
//            String strDate = "";
//            if (dt.getRepairTime() != null){
//                strDate = df.format(dt.getRepairTime());
//            }
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(strDate);
//            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        file.delete();

    }

    @Override
    public void exportDataBonepile(HttpServletResponse resonse, String factory, String timeSpan) throws Exception {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getEndDate());
        String reportDate = simpleDateFormat.format(calendar.getTime());

        List<String> action =  Arrays.asList("BALANCE", "OVER8H");

        List<Map<String, Object>> dataOutput  = sfcTestSerialErrorRepository.getDetailBonePileByDayReport(factory, action, reportDate, listModelByFactory(factory));

        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {
                "Factory"
                ,"DATE TIME"
                ,"SERIAL NUMBER"
                ,"TYPE"
                ,"CHECK IN DATE"
                ,"CHECK OUT DATE"
                ,"FAILTIME"
                ,"FIRST FAILTIME"
                ,"MODEL NAME"
                ,"WIP GROUP"
        };

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-bonepile.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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
        String dd = "bonepile-"+reportDate;
        HSSFSheet sheet = workbook.createSheet(dd);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (Map<String, Object> dt: dataOutput) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(factory);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(reportDate);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SN"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SN_TYPE"));
            cellNum++;

            String checkInDate = "";
            if (dt.get("CHECKIN_DATE") != null){
                checkInDate = df.format(dt.get("CHECKIN_DATE"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(checkInDate);
            cellNum++;

            String checkOutDate = "";
            if (dt.get("CHECKOUT_DATE") != null){
                checkOutDate = df.format(dt.get("CHECKOUT_DATE"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(checkOutDate);
            cellNum++;

            String failTime = "";
            if (dt.get("FAILTIME") != null){
                failTime = df.format(dt.get("FAILTIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(failTime);
            cellNum++;

            String firstFailTime = "";
            if (dt.get("FIRST_FAILTIME") != null){
                firstFailTime = df.format(dt.get("FIRST_FAILTIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(firstFailTime);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("P_NO"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("WIP_GROUP"));
            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }
        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        file.delete();
    }

    @Override
    public Object getCountInputByTime(String factory) throws ParseException, IOException {
//        List<Map<String, Object>> data = countSerialNumberCheckInByTime(factory);
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getTimeHourBc8m(factory, listModelByFactory(factory));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MONTH, 1);
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();

        List<Map<String, Object>> nhohon4 = new ArrayList<>();
        List<Map<String, Object>> lonhon4 = new ArrayList<>();
        List<Map<String, Object>> lonhon12 = new ArrayList<>();
        List<Map<String, Object>> lonhon24 = new ArrayList<>();
        List<Map<String, Object>> lonhon48 = new ArrayList<>();
        List<Map<String, Object>> lonhon72 = new ArrayList<>();
        List<Map<String, Object>> lonhon7d = new ArrayList<>();
        List<Map<String, Object>> lonhon15d = new ArrayList<>();
        Calendar dateNow = Calendar.getInstance();
        Calendar diff = Calendar.getInstance();
        for (int i = 0; i < data.size(); i++) {
            diff.setTime((Date) data.get(i).get("IN_STATION_TIME"));
            long diffTmp = dateNow.getTimeInMillis() - diff.getTimeInMillis();
            long hours = TimeUnit.MILLISECONDS.toHours(diffTmp);
            if (hours < 4){
                nhohon4.add(data.get(i));
            }else if (hours < 12){
                lonhon4.add(data.get(i));
            }else if (hours < 24){
                lonhon12.add(data.get(i));
            }else if (hours < 48){
                lonhon24.add(data.get(i));
            }else if (hours < 72){
                lonhon48.add(data.get(i));
            }else if (hours < 7 * 24){
                lonhon72.add(data.get(i));
            }else if (hours < 15 * 24){
                lonhon7d.add(data.get(i));
            }else {
                lonhon15d.add(data.get(i));
            }
        }
        Map<String, Long> nhohon4Map = countModelName(nhohon4);
        Map<String, Long> lonhon4Map = countModelName(lonhon4);
        Map<String, Long> lonhon12Map = countModelName(lonhon12);
        Map<String, Long> lonhon24Map = countModelName(lonhon24);
        Map<String, Long> lonhon48Map = countModelName(lonhon48);
        Map<String, Long> lonhon72Map = countModelName(lonhon72);
        Map<String, Long> lonhon7dMap = countModelName(lonhon7d);
        Map<String, Long> lonhon15dMap = countModelName(lonhon15d);

        nhohon4Map = nhohon4Map.size() > 0 ?   sortMapByValue(nhohon4Map) :  nhohon4Map ;
        lonhon4Map = lonhon4Map.size() > 0 ?   sortMapByValue(lonhon4Map) :  lonhon4Map ;
        lonhon12Map = lonhon12Map.size() > 0 ?   sortMapByValue(lonhon12Map) :  lonhon12Map ;
        lonhon24Map = lonhon24Map.size() > 0 ?   sortMapByValue(lonhon24Map) :  lonhon24Map ;
        lonhon48Map = lonhon48Map.size() > 0 ?   sortMapByValue(lonhon48Map) :  lonhon48Map ;
        lonhon72Map = lonhon72Map.size() > 0 ?   sortMapByValue(lonhon72Map) :  lonhon72Map ;
        lonhon7dMap = lonhon7dMap.size() > 0 ?   sortMapByValue(lonhon7dMap) :  lonhon7dMap ;
        lonhon15dMap = lonhon15dMap.size() > 0 ?   sortMapByValue(lonhon15dMap) :  lonhon15dMap ;

        result.put("<4H", countModelName(nhohon4Map, countModelName(nhohon4Map)));
        result.put(">4H", countModelName(lonhon4Map, countModelName(lonhon4Map)));
        result.put(">12H", countModelName(lonhon12Map, countModelName(lonhon12Map)));
        result.put(">24H", countModelName(lonhon24Map, countModelName(lonhon24Map)));
        result.put(">48H", countModelName(lonhon48Map, countModelName(lonhon48Map)));
        result.put(">72H", countModelName(lonhon72Map, countModelName(lonhon72Map)));
        result.put(">7D", countModelName(lonhon7dMap, countModelName(lonhon7dMap)));
        result.put(">15D", countModelName(lonhon15dMap, countModelName(lonhon15dMap)));

        List<Object> resul = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> value : result.entrySet()) {
            Map<String, Object> tmpMap = new LinkedHashMap<>();
            tmpMap.put("name", value.getKey());
            tmpMap.put("y", value.getValue().get("qty"));
            resul.add(tmpMap);
        }
        return resul;
    }

    @Override
    public Object getDataRMAInOutRemain(String factory, String timeSpan) throws ParseException, IOException {
        String mo = "2279%";
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<Map<String, Object>> remain = sfcTestSerialErrorRepository.getQtyRemainRMA(factory, listModelByFactory(factory));
        List<Map<String, Object>> input = sfcTestSerialErrorRepository.getQtyRemainRMAByTime(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModelByFactory(factory), "TEST_TIME");
        List<Map<String, Object>> output = sfcTestSerialErrorRepository.getQtyRemainRMAByTime(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModelByFactory(factory), "REPAIR_TIME");

        Integer sumInput =  0 ; Integer sumOutput = 0;  Integer sumRemain = 0;
//        sumInput = input.stream().mapToLong(value ->(long) value[1]).sum();
//        sumOutput = output.stream().mapToLong(val ->(long)  val[1]).sum();
//        sumRemain = remain.stream().mapToLong(val ->(long)  val[1]).sum();
        Map<String, Object> data = remain.stream().sorted((e1, e2) -> (((BigDecimal) e2.get("QTY")).compareTo((BigDecimal) e1.get("QTY"))))
                .collect(Collectors.toMap(e -> (String) e.get("MODEL_NAME"), e -> e.get("QTY"),
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        sumInput = input.stream().mapToInt(value ->((BigDecimal) value.get("QTY")).intValue()).sum();
        sumOutput = output.stream().mapToInt(value ->((BigDecimal) value.get("QTY")).intValue()).sum();
        sumRemain = remain.stream().mapToInt(value ->((BigDecimal) value.get("QTY")).intValue()).sum();
        Map<String, Object> result = new HashMap<>();
        result.put("totalInput", sumInput);
        result.put("totalOutput", sumOutput);
        result.put("totalRemain", sumRemain);
        result.put("dataRemain",data);
        return result;
    }

    @Override
    public Object getDataRMAErrorCodeByModelName(String factory, String modelName) throws ParseException, IOException {
        String mo = "2279%";
        List<Map<String, Object>> errorCode = sfcTestSerialErrorRepository.getQtyErrorCodeByModelName( factory, modelName);
        Map<String, Object> data = errorCode.stream().sorted((e1, e2) -> (((BigDecimal) e2.get("QTY")).compareTo((BigDecimal) e1.get("QTY"))))
                .collect(Collectors.toMap(e -> (String) e.get("TEST_CODE"), e -> e.get("QTY"),
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return data;
    }

    @Override
    public Object getDataRMAInoutTrendChart(String factory, String timeSpan) throws ParseException, IOException {
        String mo = "2279%";
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -9);
        fullTimeSpan.setStartDate(calendar.getTime());
        List<Map<String, Object>> input = sfcTestSerialErrorRepository.getQtyTrendChartInOut(factory, listModelByFactory(factory),fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "TEST_TIME");
        List<Map<String, Object>> output = sfcTestSerialErrorRepository.getQtyTrendChartInOut(factory, listModelByFactory(factory),fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), "REPAIR_TIME");
      //  List<ReDailyRemain> remainDaily = reDailyRemainRepository.findByFactoryAndStartDateBetweenAndStatus(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), ReDailyRemain.Status.RMA);
        Map<String, Object> mapTeamplate = new LinkedHashMap<>();
        for (int i = 0; i < 10 ; i++) {
            mapTeamplate.put(simpleDateFormat.format(calendar.getTime()), 0);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        Map<String, Object> dataInput = input.stream().collect(Collectors.toMap(e-> (String)e.get("TIMER"), e ->e.get("TOTAL"), (old, newVal) -> newVal));
        Map<String, Object> dataOutput = output.stream().collect(Collectors.toMap(e->(String)e.get("TIMER"), e -> e.get("TOTAL"), (old, newVal) -> newVal));
      //  Map<String, Object> dataRemain = remainDaily.stream().collect(Collectors.toMap(e -> simpleDateFormat.format(e.getStartDate()), e -> e.getDailyRemain(),(old, newVal) -> newVal));
        for (Map.Entry<String, Object> entry : mapTeamplate.entrySet()){
            Map<String, Integer> tmpMap = new HashMap<>();
            if ((dataInput.get(entry.getKey()) == null)) {
                tmpMap.put("input", 0);
            } else {
                tmpMap.put("input", ((BigDecimal)dataInput.get(entry.getKey())).intValue());
            }
            if ((dataOutput.get(entry.getKey()) == null)) {
                tmpMap.put("output", 0);
            } else {
                tmpMap.put("output", ((BigDecimal)dataOutput.get(entry.getKey())).intValue());
            }
            Integer remain;
            if ((Integer) tmpMap.get("input") > (Integer) tmpMap.get("output")) {
                remain = (Integer) tmpMap.get("input") - (Integer) tmpMap.get("output");
            }else {
                remain = 0;
            }
//            if ((dataRemain.get(entry.getKey()) == null)) {
//                tmpMap.put("remain", 0);
//            } else {
//                tmpMap.put("remain", dataRemain.get(entry.getKey()));
//            }
            if (remain <= 0){
                tmpMap.put("remain", 0);
            }else {
                tmpMap.put("remain", remain);
            }
            entry.setValue(tmpMap);

        }
        return mapTeamplate;
    }

    @Override
    public void downloadExcelFileDataRemainBC8M(HttpServletResponse response, String factory) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Map<String, Object>> res = sfcTestSerialErrorRepository.getDetailRemainBc8m(factory, listModelByFactory(factory));
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {
                "Factory"
                ,"serial Number"
                ,"mo"
                ,"model Name"
              //  ,"version Code"
                ,"line Name"
//                ,"group Name"
//                ,"station Name"
                ,"section Name"
//                ,"location Code"
//                ,"error Code"
//                ,"test Time"
//                ,"test Group"
                ,"In put Time"
                ,"WIP GROUP"
        };

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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
        String dd = "model-BC8M";
        HSSFSheet sheet = workbook.createSheet(dd);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (Map<String, Object> dt: res) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(factory);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SERIAL_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MO_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MODEL_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("LINE_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SECTION_NAME"));
            cellNum++;

//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getGroupName());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getStationName());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getSectionName());
//            cellNum++;
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getLocation());
//            cellNum++;

//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(dt.getTestCode());
//            cellNum++;
            String strTestTime = "";
            if (dt.get("IN_STATION_TIME") != null){
                strTestTime = df.format(dt.get("IN_STATION_TIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(strTestTime);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("WIP_GROUP"));
            cellNum++;
//
//            String strDate = "";
//            if (dt.getInStationTime() != null){
//                strDate = df.format(dt.getInStationTime());
//            }
//
//            cell = row.createCell(cellNum, CellType.STRING);
//            cell.setCellValue(strDate);
//            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        response.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();

        file.delete();
    }

    @Override
    public void downloadExcelFileDataRemainRMA(HttpServletResponse response, String factory) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Map<String, Object>> res = sfcTestSerialErrorRepository.getRemainRMA(factory, listModelByFactory(factory));
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {"FACTORY"
                ,"SERIAL_NUMBER"
                ,"MODEL_NAME"
                ,"MO_NUMBER"
                ,"TEST_TIME"
                ,"TEST_CODE"
                ,"TEST_STATION"
                ,"TEST_GROUP"
                ,"TEST_SECTION"
                ,"TEST_LINE"
                ,"TESTER"
                ,"REPAIRER"
                ,"REASON_CODE"};

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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
        String dd = "remain-RMA";
        HSSFSheet sheet = workbook.createSheet(dd);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (Map<String, Object> dt: res) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(factory);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("SERIAL_NUMBER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MODEL_NAME"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("MO_NUMBER"));
            cellNum++;

            String strTestTime = "";
            if (dt.get("TEST_TIME") != null){
                strTestTime = df.format(dt.get("TEST_TIME"));
            }
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(strTestTime);
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_CODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_STATION"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_GROUP"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_SECTION"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TEST_LINE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("TESTER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("REPAIRER"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("REASON_CODE"));
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue((String) dt.get("REASON_CODE"));
            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        response.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();

        file.delete();
    }

    @Override
    public Object getListModelB04(String factory) {
        TestModelList testModelList = testModelListRepository.findTop1ByFactory(factory);
        return testModelList.getModelNames();
    }

    @Override
    public void importOnlineWipRemain(HttpServletResponse resonse, String modelName) throws Exception {
        String factory = "B04";
        List<RepairOnlineWipInOut> data = new ArrayList<>();
        if (StringUtils.isEmpty(modelName)){
            data = repairOnlineWipInOutRepository.listInputRemain(factory);
        }else{
            data = repairOnlineWipInOutRepository.listInputByModelName(factory, modelName);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        HSSFWorkbook workbook = new HSSFWorkbook();

        String fileDir = System.getProperty("user.dir").toString() + "\\tempotarydownloaddir\\";
        String[] arrTitle  = {"id"
                ,"factory"
                ,"model Name"
                ,"station Name"
                ,"error Code"
                ,"test Time"
                ,"serial Number"
                ,"section Name"
                ,"mo"
                ,"checkin Time"};

        String fileName = (new Date()).getTime() + "-" + (new Random()).nextInt(500) + "-data.xls";

        HSSFCellStyle titleStyle = createStyle(workbook);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
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

        HSSFSheet sheet = workbook.createSheet("RE_"+modelName);
        int max = 0;
        int rowNum = 0;
        Row row;
        Cell cell;

        row = sheet.createRow(rowNum);

        for (int i = 0; i < arrTitle.length; i++){
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(arrTitle[i]);
            cell.setCellStyle(titleStyle);
        }
        rowNum++;
        for (RepairOnlineWipInOut dt: data) {
            int cellNum = 0;
            row = sheet.createRow(rowNum);

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getId());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getFactory());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getModelName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getStationName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getErrorCode());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getTestTime());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getSerialNumber());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getSectionName());
            cellNum++;

            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(dt.getMo());
            cellNum++;

            String checkIn = "";
            checkIn = df.format(dt.getCheckInTime());
            cell = row.createCell(cellNum, CellType.STRING);
            cell.setCellValue(checkIn);
            cellNum++;

            rowNum++;

            if (cellNum > max){
                max = cellNum;
            }
            if (rowNum == 65530){
                break;
            }

        }
        for (int i = 0; i < max; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(fileDir + fileName);
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        file.delete();
    }

    @Override
    public Object getErrorCodeByModelNameC03(String factory, String modelName, String timeSpan) throws Exception {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calen = Calendar.getInstance();
        calen.setTime(fullTimeSpan.getStartDate());
        calen.add(Calendar.DAY_OF_YEAR, -1);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<String> timeList = new ArrayList<>();
        timeList.add(simpleDateFormat.format(calen.getTime()));
        timeList.add(simpleDateFormat.format(fullTimeSpan.getStartDate()));

        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getListErrorCodeByModelName(factory, "UI", modelName, calen.getTime(), fullTimeSpan.getStartDate());
        List<Map<String, Object>> dataOut = sfcTestSerialErrorRepository.getListReasonCodeByModelName(factory, "UI", modelName, calen.getTime(), fullTimeSpan.getStartDate());

        Map<String, List<Map<String, Object>>> dataMap = data.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TEST_GROUP")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
        Map<String, List<Map<String, Object>>> dataMapOut = dataOut.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TEST_GROUP")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
        Map<String, Object> tmpMap = new HashMap<>();
//        Object result = null;
        Map<String, Object> result = new HashMap<>();
        for(String key : dataMap.keySet()){
            List<Map<String, Object>> item = dataMap.get(key);
            Map<String, List<Map<String, Object>>> dutyDataMap1 = item.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TEST_CODE")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
            tmpMap.put(key, dutyDataMap1);
        }
//        result = tmpMap;
            result.put("errorCode", tmpMap);
        Map<String, Object> tmpMapOut = new HashMap<>();
       // Object result = null;
        for(String key : dataMapOut.keySet()){
            List<Map<String, Object>> item = dataMapOut.get(key);
            Map<String, List<Map<String, Object>>> dutyDataMap1 = item.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("REASON_CODE")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
            tmpMapOut.put(key, dutyDataMap1);
        }
//        result = tmpMapOut;
        result.put("reasonCode", tmpMapOut);
        result.put("time", timeList);

        return result;
    }


}
