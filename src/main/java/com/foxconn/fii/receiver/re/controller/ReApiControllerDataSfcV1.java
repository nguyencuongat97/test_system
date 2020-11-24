package com.foxconn.fii.receiver.re.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.utils.CommonUtils;
import com.foxconn.fii.common.utils.ExcelUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.ReInfoResource;
import com.foxconn.fii.data.primary.model.entity.TestGroupDaily;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.repository.ReInfoResourceRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestGroupRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestSerialErrorRepository;
import com.foxconn.fii.receiver.re.service.ReCheckInOutSfcDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/re/sfc")
public class ReApiControllerDataSfcV1 {
    @Autowired
    private ReCheckInOutSfcDataService reCheckInOutSfcDataService;

    @Autowired
    private SfcTestSerialErrorRepository sfcTestSerialErrorRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private SfcTestGroupRepository sfcTestGroupRepository;

    @Autowired
    private ReInfoResourceRepository reInfoResourceRepository;

    @GetMapping("/total_in_out")
    public Object getTotalInputOutput(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        return reCheckInOutSfcDataService.getTotalInputOutputservice(factory, timeSpan);
    }

    @GetMapping("/get_in_out_weekly")
    public Object getInOutWeekly(@RequestParam String factory) {
        return reCheckInOutSfcDataService.getInOutByWeekly(factory);
    }

    @GetMapping("/get_checkout_by_day")
    public Object getCheckoutByDay(@RequestParam String factory, @RequestParam (required = false, defaultValue = "") String timeSpan) {
        return reCheckInOutSfcDataService.getQtyCheckoutByDay(factory, timeSpan);
    }

    @GetMapping("/get_in_out_by_section")
    public Object getInOutWeeklyBySection(@RequestParam String factory, @RequestParam String section) {
        return reCheckInOutSfcDataService.getInOutBySection(factory, section);
    }

    @GetMapping("/get_in_out_by_section_smt")
    public Map<String, Object> getInOutWeeklyBySectionSmt(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        Map<String, Object> dataTotal = reCheckInOutSfcDataService.getTotalInputOutputservice(factory, "");
        Map<String, Object> dataSi = reCheckInOutSfcDataService.getInOutBySection(factory, "SI");
        Set<String> listKey =  ((Map<String, Object>)dataTotal.get("output")).keySet();
        Map<String, Object> input = new LinkedHashMap<>();
        Map<String, Object> output = new LinkedHashMap<>();
        for (String set: listKey) {
            Integer valIn = ((BigDecimal)((Map<String, Object>)dataTotal.get("input")).get(set)).intValue()
                    - ((BigDecimal)((Map<String, Object>)dataSi.get("input")).get(set)).intValue();
            Integer valOut = ((BigDecimal)((Map<String, Object>)dataTotal.get("output")).get(set)).intValue()
                    - ((BigDecimal)((Map<String, Object>)dataSi.get("output")).get(set)).intValue();
            input.put(set, valIn);
            output.put(set, valOut);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("input", input);
        res.put("output", output);
        return res;
    }

    @GetMapping("/bonepile")
    public Object getBonepile(@RequestParam String factory) {
        return reCheckInOutSfcDataService.getBalanceAndOverTime8h(factory);
    }

    @GetMapping("/get_model_meta_factory")
    public List<TestModelMeta> getModelNameMetaFactory(@RequestParam String factory) {
        List<TestModelMeta> modelMeta;
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        return modelMeta;
    }

    @GetMapping("/get_error_code_ntf")
    public CommonResponse<Object> getErrorCodeNtf(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        return reCheckInOutSfcDataService.getErrorCodeByNtf(factory, timeSpan);
    }
//    return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);

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



    ///// Start PE By TUNG
    @GetMapping("/get_pe_count_model_name")
    public Object countModelNamePE(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, @RequestParam(required = false, defaultValue = "") String parameter) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<TestModelMeta> modelMeta;
        Map<String, Integer> result = new LinkedHashMap<>();
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }

        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        List<Map<String, Object>> data = new ArrayList<>();
//        if (parameter.equalsIgnoreCase("REASON_CODE")){
//            data = sfcTestSerialErrorRepository.qtyPEModelName(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModel, parameter, "REPAIR_TIME");
//        }else {
        data = sfcTestSerialErrorRepository.qtyPEModelName(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModel, parameter, "TEST_TIME");
//        }

        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                BigDecimal val = (BigDecimal) data.get(i).get("qty");
                result.put((String) data.get(i).get(parameter), val.intValue());
            }
        }
        result = sortMapByValue(result);
        return result;
    }

    private <T extends Comparable<? super T>> Map<String, T> sortMapByValue(Map<String, T> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    @GetMapping("/get_pe_detail")
    public Object countModeddlNamePE(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<TestModelMeta> modelMeta;
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.detailModelNamePE(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), listModel);
//        Map<String, List<Map<String, Object>>> dataMap = data.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("MODEL_NAME")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
//
//        Map<String, Object> tmpMap = new HashMap<>();
//        try {
//            Map<String, Object> result = new HashMap<>();
//            for(String key : dataMap.keySet()){
//                List<Map<String, Object>> item = dataMap.get(key);
//                Map<String, List<Map<String, Object>>> dutyDataMap1 = item.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("TEST_CODE")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
//                Map<String, Object> tmpData = new HashMap<>();
//                for (String k : dutyDataMap1.keySet()){
//                    List<Map<String, Object>> tem = dutyDataMap1.get(k);
//                    Map<String, List<Map<String, Object>>> tmpDutyDataMap1 = tem.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("REASON_CODE")), LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
//                    tmpData.put(k, tmpDutyDataMap1);
//                }
//                tmpMap.put(key, tmpData);
//            }
//            log.info("paise");
//            return tmpMap;
//        }catch (Exception e){
//            log.info("###Error: " + e);
//            return false;
//        }
        return data;
    }

    ///// Start TE By TUNG
    @GetMapping("/get_model_meta")
    public Object getModel(@RequestParam String factory){
        List<TestModelMeta> modelMeta;
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        return listModel;
    }

    @GetMapping("/get_te_report")
    public Object getTEReport(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, @RequestParam(required = false, defaultValue = "") String modelName) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<TestModelMeta> modelMeta;
        if (factory.equalsIgnoreCase("S03")) {
            modelMeta = testModelMetaRepository.findByFactoryAndCustomerAndVisibleIsTrue(factory, "UI");
        } else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        List<TestGroupDaily> data = new ArrayList<>();
        String customer = "";
        if (Factory.S03.equalsIgnoreCase(factory)){
            customer = "UI";
        }
        if (modelName == null || modelName.isEmpty() || modelName.trim().isEmpty()){
           data = sfcTestGroupRepository.findByWorkDateBetween(factory,customer, "TEST",listModel, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());

        }else {
            data = sfcTestGroupRepository.findByWorkDateBetween(factory,customer, "TEST", modelName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        }

        Map<String, Map<String, List<TestGroupDaily>>> result =  data
                .stream()
                .collect(
                        Collectors.groupingBy(
                            e -> TimeSpan.format(TimeSpan.of(e.getStartDate(), e.getEndDate()), TimeSpan.Type.DAILY).split(" ")[0],
                            LinkedHashMap::new,
                            Collectors.groupingBy(
                                    e -> TimeSpan.format(TimeSpan.of(e.getStartDate(), e.getEndDate()), TimeSpan.Type.DAILY).split(" ")[1],
                                    HashMap::new,
                                    Collectors.toList()
                            )
                        )
                );
        return result;
    }


    ///// Start Daily Quality report By TUNG
    @GetMapping("/get_quality_daily")
    public Map<String, Object> getQualityDailly1(@RequestParam String factory, @RequestParam String customer, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String sectionName = "SI";
//         customer = "AMMAN"; // OPTIMATOR
         if (factory.equalsIgnoreCase("B06")){
             sectionName = "TEST";
         }
        List<TestModelMeta> modelMeta = testModelMetaRepository.findByFactoryAndCustomer(factory, customer);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
//        calendar.add(Calendar.DAY_OF_YEAR, -15);

        Calendar cal = Calendar.getInstance();
        cal.setTime(fullTimeSpan.getEndDate());
        Map<String, Float> dataDate = new LinkedHashMap<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(calendar.getTime());
        int tmp = cal.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < tmp; i++) {
            dataDate.put(simpleDateFormat.format(startCal.getTime()), null);
            startCal.add(Calendar.DAY_OF_YEAR, 1);
        }
//        Map<String, TestModel> re//sult = new LinkedHashMap<>();
        SimpleDateFormat monthdf = new SimpleDateFormat("yyyy/MM");
        List<TestGroupDaily> retestData = sfcTestGroupRepository.getWorkDateByGroupNameByDay(factory, "", listModel, sectionName, calendar.getTime(), fullTimeSpan.getEndDate());
        List<TestGroupDaily> eteData = sfcTestGroupRepository.getWorkDateByGroupNameByDay(factory, "", listModel, calendar.getTime(), fullTimeSpan.getEndDate());
        Map<String, Map<String, List<TestGroupDaily>>> retestMap = retestData
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> ((TestGroupDaily) e).getGroupName(),
                                LinkedHashMap::new,
                                Collectors.groupingBy(
                                        e -> e.getWorkDate(),
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        )
                );

        Map<String, Map<String, List<TestGroupDaily>>> eteMap = eteData
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> ((TestGroupDaily) e).getGroupName(),
                                LinkedHashMap::new,
                                Collectors.groupingBy(
                                        e -> e.getWorkDate(),
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        )
                );

        Map<String, List<TestGroupDaily>> actualEte = eteData
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> ((TestGroupDaily) e).getWorkDate(),
                                LinkedHashMap::new,
                                    Collectors.toList()
                                )
                );
        Map<String, Float> mapActual = new LinkedHashMap<>();
        for (Map.Entry<String, List<TestGroupDaily>> v: actualEte.entrySet()) {
            float actual = 0.0f;
            if (v.getValue().size() > 0){
                actual = 1.0f;
                for (int i = 0; i < v.getValue().size(); i++) {
                    if (v.getValue().get(i).getGroupName().equalsIgnoreCase("RCVI") || v.getValue().get(i).getGroupName().equalsIgnoreCase("ICT") || v.getValue().get(i).getGroupName().equalsIgnoreCase("INSP") || v.getValue().get(i).getWip() <= 100){
                        continue;
                    }
                    actual *= ((float) v.getValue().get(i).getPass() / (float) v.getValue().get(i).getWip());
                }
                if (actual == 1){
                    mapActual.put(v.getKey(), 0.0f);
                }else{
                    mapActual.put(v.getKey(), actual*100.0f);
                }

            }else {
                mapActual.put(v.getKey(), actual);
            }
        }

        Map<String, Float> mapActl = new LinkedHashMap<>();
        for (Map.Entry<String, Float> t: dataDate.entrySet()) {
            if (mapActual.get(t.getKey()) == null || !mapActual.containsKey(t.getKey())){
                mapActl.put(t.getKey(), 0.0f);
                continue;
            }
            mapActl.put(t.getKey(), mapActual.get(t.getKey()));
        }

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        Map<String, Object> resultEte = new LinkedHashMap<>();
            for (Map.Entry<String, Map<String, List<TestGroupDaily>>> val : eteMap.entrySet()) {
                Map<String, Float> map = new LinkedHashMap<>();
                for (Map.Entry<String, Float> t: dataDate.entrySet()) {
                    if (val.getValue().get(t.getKey()) == null || !val.getValue().containsKey(t.getKey())){
                        map.put(t.getKey(), 0.0f);
                        continue;
                    }
                    int pass = val.getValue().get(t.getKey()).get(0).getPass();
                    int wip = val.getValue().get(t.getKey()).get(0).getWip();
                    if (wip <= 100){
                        map.put(t.getKey(), 0.0f);
                        continue;
                    }
                    float ete = ((float) pass/(float)wip) * 100.0f;
                    map.put(t.getKey(), ete);

                    log.info("dad");
                }

              if (val.getKey().equalsIgnoreCase("INSP")){
                  resultEte.put("SMT", map);
              }else if (!val.getKey().equalsIgnoreCase("RCVI")){
                  resultEte.put(val.getKey(), map);
              }
            }


        Map<String, Object> resultRetest = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, List<TestGroupDaily>>> val : retestMap.entrySet()) {
            Map<String, Float> map = new LinkedHashMap<>();
            int count = 0;
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(calendar.getTime());
            for (Map.Entry<String, List<TestGroupDaily>> valDt : val.getValue().entrySet()){
                Calendar tmpCal = Calendar.getInstance();
                tmpCal.setTime(calendar1.getTime());
                String keyTime = simpleDateFormat.format(tmpCal.getTime());
                float retest = 0.0f;
                if (val.getValue().get(keyTime) == null){
//                        map.put(keyTime, 0.0f);
                    log.info("dad");
                    continue;
                }else {
                    if (val.getValue().get(keyTime).size() > 1){
                        int firstFail = 0;
                        int secondFail = 0;
                        int wip = 0;
                        for (int i = 0; i < val.getValue().get(keyTime).size(); i++) {
                            firstFail += val.getValue().get(keyTime).get(i).getFirstFail();
                            secondFail += val.getValue().get(keyTime).get(i).getSecondFail();
                            wip += val.getValue().get(keyTime).get(i).getWip();
                        }
                        if (wip < 100){
                            continue;
                        }
                        retest = ((float) firstFail - (float) secondFail) / (float) wip * 100.0f ;
                    }else if (val.getValue().get(keyTime).size() == 1){
                        int firstFail = val.getValue().get(keyTime).get(0).getFirstFail();
                        int secondFail = val.getValue().get(keyTime).get(0).getSecondFail();
                        int wip = val.getValue().get(keyTime).get(0).getWip();
                        if (wip < 100){
                            continue;
                        }
                        retest = ((float) firstFail - (float) secondFail) / (float) wip * 100.0f ;
                    }
                }
                if (retest < 0){
                    retest = 0.0f;
                }
                map.put(keyTime, retest);
                calendar1.add(Calendar.DAY_OF_YEAR, 1);
                if (count == tmp){
                    break;
                }
                count++;
            }
            if (!val.getKey().equalsIgnoreCase("RCVI")){
                resultRetest.put(val.getKey(), map);
            }
        }
        Map<String, Object> modifyEte = new LinkedHashMap<>();
        Set<String> set = resultEte.keySet();
        List<String> listSet = new ArrayList<>(set);
        if (set.size() > 0){
            modifyEte.put("SMT", resultEte.get("SMT"));
            modifyEte.put("ICT", resultEte.get("ICT"));
            for (int i = 0; i < listSet.size(); i++) {
                if (!listSet.get(i).equalsIgnoreCase("SMT") || !listSet.get(i).equalsIgnoreCase("ICT")){
                    modifyEte.put(listSet.get(i), resultEte.get(listSet.get(i)));
                }
            }
        }
        modifyEte.put("Actual ETE", mapActl);
        Map<String, Object> kq = new HashMap<>();
            kq.put("ETE", modifyEte);
            kq.put("RETEST-RATE", resultRetest);
        return kq;
    }

    @PostMapping("/upload_user_re")
    public Boolean uploadListOp(@RequestPart MultipartFile file) throws Exception, FileNotFoundException {
        Workbook workbook = null;
        try {
            if ("xls".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equalsIgnoreCase(CommonUtils.getExtension(file.getOriginalFilename()))) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
//                throw CommonException.of("upload mps file support only xls and xlsx");
                log.info("dad");
            }
            Sheet sheet = workbook.getSheetAt(1);
            List<Map<String, String>> data =  new ArrayList<>();

            List<String> workingList = new ArrayList<>();
            Row rowWorkingDate = sheet.getRow(5);
            for (int i = 1; i < 13 && i < rowWorkingDate.getLastCellNum(); i++) {
                Cell cell = rowWorkingDate.getCell(i);
                workingList.add(cell.getStringCellValue());
            }
            int count = 0;
            List<String> test = new ArrayList<>();
            List<String> thgMlHuy= new ArrayList<>();

            List<ReInfoResource> resources = new ArrayList<>();

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                count++;

                Cell cell = row.getCell(0);
                String stt = ExcelUtils.getStringValue(cell);

                cell = row.getCell(1);
                String empNo = ExcelUtils.getStringValue(cell);

                cell = row.getCell(2);
                String name = ExcelUtils.getStringValue(cell);

                cell = row.getCell(3);
                String nameChina = ExcelUtils.getStringValue(cell);

                cell = row.getCell(4);
                String factory = ExcelUtils.getStringValue(cell);

                cell = row.getCell(7);
                String bu = ExcelUtils.getStringValue(cell);

                cell = row.getCell(9);
                String department = ExcelUtils.getStringValue(cell);

                cell = row.getCell(11);
                String role = ExcelUtils.getStringValue(cell);

                cell = row.getCell(12);
                String quequan = ExcelUtils.getStringValue(cell);

                ReInfoResource resource = new ReInfoResource();

                resource.setBu(bu);
                resource.setDepartment(department);
                resource.setEmpNo(empNo);
                resource.setDomicile(quequan);
                resource.setFactory(factory);
                resource.setRole(role);
                resource.setNameChina(nameChina);
                resource.setNameEse(name);
                resources.add(resource);

//                hrOppmTmpRepository.saveAll(users);
                if (empNo.equalsIgnoreCase("V0994776")){
                    break;
                }

            }
//            GpsOrder gpsOrder = new GpsOrder();
//            gpsOrder.setPalet(palet);
//            gpsOrder.setTonnage(tonnage);
//            gpsOrder.setDepotIdFactory(idWarehouse);
            System.out.println(thgMlHuy);
//            hrOppmService.saveUserOppm(data);
            reInfoResourceRepository.saveAll(resources);
//            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            byte[] encoded = Base64.encodeBase64(file.getBytes());

            log.info("### TEN FILE: " + file.getOriginalFilename());
            String factory = "B06";
            String body = "<html><body><b>Dear Users,</b><br/>do you ask for material from "+factory+".<br/><br/> Please check ad prepare material.</body></html>";
//          <a href=\"http://10.224.81.70:8888/calc-line-balance?factory="+factory+"\">Smart Factory System</a>
            MailMessage message = MailMessage.of("[Urgent] NOTICE OF NEW ORDER", body, new String(encoded, StandardCharsets.US_ASCII), "LIT GOI LIEU KITTING B06 CA NGAY.xlsx");
            //   notifyService.notifyToMail(message, "", email);
            return true;
        } catch (IOException e) {
            log.error("### uploadFileMPS error", e);
            return false;
        }
    }
}
