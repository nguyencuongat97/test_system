package com.foxconn.fii.receiver.re.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.data.primary.model.entity.ReSfcDailyBonepile;
import com.foxconn.fii.data.primary.model.entity.RepairTimeDataBeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.repository.ReSfcDailyBonepileRepository;
import com.foxconn.fii.data.primary.repository.RepairTimeDataBetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestSerialErrorRepository;
import com.foxconn.fii.receiver.re.service.ReCheckInOutSfcDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReCheckInOutSfcDataServiceImpl implements ReCheckInOutSfcDataService {
    @Autowired
    private SfcTestSerialErrorRepository sfcTestSerialErrorRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @Autowired
    private ReSfcDailyBonepileRepository reSfcDailyBonepileRepository;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    // My FUNCTION
    private Map<String, List<Map<String, Object>>> sortMapByKeyDate(Map<String, List<Map<String, Object>>> result){
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
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
        return result;
    }

    private <T extends Comparable<? super T>> Map<String, T> sortMapByValue(Map<String, T> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
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
    // End My FUNCTION
    @Override
    public Map<String, Object> getTotalInputOutputservice(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
        List<TestModelMeta> modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.MILLISECOND, 01);
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        cal.add(Calendar.DAY_OF_YEAR, -6);
        List<Map<String, Object>> data1  = sfcTestSerialErrorRepository.getCheckInCheckOutReportWeekly(factory, "", cal.getTime(), calendar.getTime(), listModel);
        List<Map<String, Object>> data  = sfcTestSerialErrorRepository.getCheckInCheckOutReport(factory, "", calendar.getTime(), date, listModel);

        String time = simpleDateFormat.format(cal.getTime())+" - "+ simpleDateFormat.format(calendar.getTime());
//        Map<String, List<Map<String, Object>>> result = data.stream().collect(Collectors.groupingBy(e -> {
//            return  df.format(e.get("IN_DATETIME"));
//        }));

        Map<String, Object> input = new LinkedHashMap<>();
        Map<String, Object> output = new LinkedHashMap<>();
        input.put(time, data1.get(0).get("QTY_IN"));
        output.put(time, data1.get(0).get("QTY_OUT"));

//        result = sortMapByKeyDate(result);

//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put(time, data1.size());
        for (int i = 0; i < data.size(); i++) {
            input.put((String) data.get(i).get("REPORT_DATE"), data.get(i).get("QTY_IN"));
            output.put((String) data.get(i).get("REPORT_DATE"), data.get(i).get("QTY_OUT"));
        }


//        List<Map<String, Object>> dataOutput1  = sfcTestSerialErrorRepository.getListOutputDailyRe(factory, "", cal.getTime(), calendar.getTime(), listModel);
//        List<Map<String, Object>> dataOutput  = sfcTestSerialErrorRepository.getListOutputDailyRe(factory, "", calendar.getTime(), date, listModel);
//        Map<String, List<Map<String, Object>>> resultOut = dataOutput.stream().collect(Collectors.groupingBy(e -> {
//            return  df.format(e.get("OUT_DATETIME"));
//        }));
//        resultOut = sortMapByKeyDate(resultOut);
//        Map<String, Object> responseOutput = new LinkedHashMap<>();
//
//        responseOutput.put(time, dataOutput1.size());
//        for (Map.Entry<String, List<Map<String, Object>>> entry : resultOut.entrySet()) {
//            responseOutput.put(entry.getKey(), entry.getValue().size());
//        }
        Map<String, Object> res = new HashMap<>();
        res.put("input", input);
        res.put("output", output);

        return res;
    }

    @Override
    public Object getInOutByWeekly(String factory) {
        Calendar calendarStrart = Calendar.getInstance();
        calendarStrart.set(Calendar.HOUR_OF_DAY, 00);
        calendarStrart.set(Calendar.MINUTE, 00);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        if (calendarStrart.get(Calendar.DAY_OF_WEEK) == 7){
            calendarStrart.add(Calendar.DAY_OF_YEAR, -2);
        }else {
            calendarStrart.add(Calendar.WEEK_OF_YEAR, -1);
            calendarStrart.set(Calendar.DAY_OF_WEEK, 5);

            calendarEnd.add(Calendar.DAY_OF_YEAR, -1);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<TestModelMeta> modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());

        List<Map<String, Object>> data  = sfcTestSerialErrorRepository.getCheckInCheckOutReport(factory, "", calendarStrart.getTime(), calendarEnd.getTime(), listModel);
        Map<String, Object> input = new LinkedHashMap<>();
        Map<String, Object> output = new LinkedHashMap<>();
        for (int i = 0; i < data.size(); i++) {
            input.put((String) data.get(i).get("REPORT_DATE"), data.get(i).get("QTY_IN"));
            output.put((String) data.get(i).get("REPORT_DATE"), data.get(i).get("QTY_OUT"));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("input", input);
        res.put("output", output);

        return res;

    }

    @Override
    public Object getQtyCheckoutByDay(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<TestModelMeta> modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());

        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getCheckoutByDayReportDaily(factory,dailyTimeSpan.getEndDate() ,listModel);

        Map<String, Integer> res = new LinkedHashMap<>();
        int i = 0;
        while (i < data.size()){
            res.put((String) data.get(i).get("P_NO"), ((BigDecimal)data.get(i).get("CHECKOUT_QTY")).intValue());
            i++;
        }
        res =  sortMapByValue(res);


        List<Map<String, Object>> dataReasonCode = sfcTestSerialErrorRepository.getReasonCodeByDay(factory,dailyTimeSpan.getStartDate() ,dailyTimeSpan.getEndDate() ,listModel);

        Map<String, Integer> resReason = new LinkedHashMap<>();
        int j = 0;
        while (j < dataReasonCode.size()){
            resReason.put((String) dataReasonCode.get(j).get("REASON_CODE"), ((BigDecimal)dataReasonCode.get(j).get("QTY")).intValue());
            j++;
        }
        resReason =  sortMapByValue(resReason);
        Map<String, Object> response = new HashMap<>();
        response.put("checkout", res);
        response.put("reason", resReason);
        return response;
    }

    @Override
    public Map<String, Object> getInOutBySection(String factory, String section) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");

        List<TestModelMeta> modelMeta = new ArrayList<>();
        if (factory.equalsIgnoreCase("B06") && section.equalsIgnoreCase("SMT")){
            modelMeta = testModelMetaRepository.findAllByFactoryAndStage(factory, section);
        }else {
            modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        }

        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.MILLISECOND, 01);
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        cal.add(Calendar.DAY_OF_YEAR, -6);
     //   List<Map<String, Object>> data1  = sfcTestSerialErrorRepository.getListInputDailyReBySection(factory, "", cal.getTime(), calendar.getTime(), listModel, section);
     //   List<Map<String, Object>> data  = sfcTestSerialErrorRepository.getListInputDailyReBySection(factory, "", calendar.getTime(), date, listModel, section);

        List<Map<String, Object>> dataInW  = sfcTestSerialErrorRepository.getDataBySectionCheckInAndCheckOutWeekly(factory, "", cal.getTime(), calendar.getTime(), "CHECKIN", listModel);
        List<Map<String, Object>> dataOutW  = sfcTestSerialErrorRepository.getDataBySectionCheckInAndCheckOutWeekly(factory, "", cal.getTime(), calendar.getTime(), "CHECKOUT", listModel);
        List<Map<String, Object>> dataIn  = sfcTestSerialErrorRepository.getDataBySectionCheckInAndCheckOut(factory, "", calendar.getTime(), date, "CHECKIN", listModel);
        List<Map<String, Object>> dataOut  = sfcTestSerialErrorRepository.getDataBySectionCheckInAndCheckOut(factory, "", calendar.getTime(), date, "CHECKOUT", listModel);

        String time = simpleDateFormat.format(cal.getTime())+" - "+ simpleDateFormat.format(calendar.getTime());
        Map<String, Object> input = new LinkedHashMap<>();
        Map<String, Object> output = new LinkedHashMap<>();
        input.put(time, dataInW.get(0).get("QTY"));
        output.put(time, dataOutW.get(0).get("QTY"));
        for (int i = 0; i < dataIn.size(); i++) {
            input.put((String) dataIn.get(i).get("REPORT_DATE"), dataIn.get(i).get("QTY"));
        }

        for (int i = 0; i < dataOut.size(); i++) {
            output.put((String) dataOut.get(i).get("REPORT_DATE"), dataOut.get(i).get("QTY"));
        }

//        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
//
//        if (factory.equalsIgnoreCase("B04")){
//            result = data.stream().collect(Collectors.groupingBy(e -> {
//                return  df.format(e.get("IN_DATETIME"));
//            }));
//        }else {
//            result = data.stream().collect(Collectors.groupingBy(e -> {
//                return  df.format(e.get("TEST_TIME"));
//            }));
//        }
//
//
//        result = sortMapByKeyDate(result);
//
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put(time, data1.size());
//        for (Map.Entry<String, List<Map<String, Object>>> entry : result.entrySet()) {
//            response.put(entry.getKey(), entry.getValue().size());
//        }
//
//        List<Map<String, Object>> dataOutput1  = sfcTestSerialErrorRepository.getListOutputDailyReBySection(factory, "", cal.getTime(), calendar.getTime(), listModel,section);
//        List<Map<String, Object>> dataOutput  = sfcTestSerialErrorRepository.getListOutputDailyReBySection(factory, "", calendar.getTime(), date, listModel, section);
//
//        Map<String, List<Map<String, Object>>> resultOut = new LinkedHashMap<>();
//
//        if (factory.equalsIgnoreCase("B04")){
//            resultOut = dataOutput.stream().collect(Collectors.groupingBy(e -> {
//                return  df.format(e.get("OUT_DATETIME"));
//            }));
//        }else {
//            resultOut = dataOutput.stream().collect(Collectors.groupingBy(e -> {
//                return  df.format(e.get("REPAIR_TIME"));
//            }));
//        }
//
//        resultOut = sortMapByKeyDate(resultOut);
//        Map<String, Object> responseOutput = new LinkedHashMap<>();
//
//        responseOutput.put(time, dataOutput1.size());
//        for (Map.Entry<String, List<Map<String, Object>>> entry : resultOut.entrySet()) {
//            responseOutput.put(entry.getKey(), entry.getValue().size());
//        }
        Map<String, Object> res = new HashMap<>();
        res.put("input", input);
        res.put("output", output);

        return res;
    }

    @Override
    public Boolean checkBalance(String factory) {
        RepairTimeDataBeta timeStartEnd = repairTimeDataBetaRepository.findTop1ByActionOrderByIdDesc("BALANCE");
        Date time = new Date();

        List<TestModelMeta> modelMeta = testModelMetaRepository.findAllByFactoryAndVisibleIsTrue(factory);
        List<String> listModel = modelMeta.stream().map(e -> e.getModelName()).collect(Collectors.toList());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -8);
        List<Map<String, Object>> balance = new ArrayList<>();
        List<Map<String, Object>> overTime8h = new ArrayList<>();
        if (timeStartEnd == null){
            balance = sfcTestSerialErrorRepository.getBalancaRe(factory, calendar.getTime(), time, listModel);
            overTime8h = sfcTestSerialErrorRepository.getOverTime8h(factory, calendar.getTime(), cal.getTime(), listModel);
        }else {
            balance = sfcTestSerialErrorRepository.getBalancaRe(factory, timeStartEnd.getStartDate(), time, listModel);
            overTime8h = sfcTestSerialErrorRepository.getOverTime8h(factory, timeStartEnd.getStartDate(), cal.getTime(), listModel);
        }

        try {
            ReSfcDailyBonepile reSfcDailyBonepile = new ReSfcDailyBonepile();
            reSfcDailyBonepile.setBalanceQty(balance.size());
            reSfcDailyBonepile.setOverTime8h(overTime8h.size());
            reSfcDailyBonepile.setFactory(factory);
            reSfcDailyBonepile.setDateTime(time);
            reSfcDailyBonepileRepository.save(reSfcDailyBonepile);

            return true;
        }catch (Exception e ){
            log.info("### error insert balance !!!");
            return false;
        }
    }

    @Override
    public Object getBalanceAndOverTime8h(String factory) {
        Calendar calendarStrart = Calendar.getInstance();
        calendarStrart.set(Calendar.HOUR_OF_DAY, 00);
        calendarStrart.set(Calendar.MINUTE, 00);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        if (calendarStrart.get(Calendar.DAY_OF_WEEK) == 7){
            calendarStrart.add(Calendar.DAY_OF_YEAR, -2);
        }else {
            calendarStrart.add(Calendar.WEEK_OF_YEAR, -1);
            calendarStrart.set(Calendar.DAY_OF_WEEK, 5);

            calendarEnd.add(Calendar.DAY_OF_YEAR, -1);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        List<ReSfcDailyBonepile> data = reSfcDailyBonepileRepository.findByDateTimeBetweenAndFactory(calendarStrart.getTime(), calendarEnd.getTime(), factory);
        Integer dem = calendarEnd.get(Calendar.DAY_OF_YEAR) - calendarStrart.get(Calendar.DAY_OF_YEAR);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i <= dem; i++) {
            Calendar tmpCal = Calendar.getInstance();
             tmpCal.setTime(calendarStrart.getTime());
            List<Map<String, Object>> res = sfcTestSerialErrorRepository.qtyReportBalanceOver8h(factory, simpleDateFormat.format(tmpCal.getTime()), listModelByFactory(factory));
            calendarStrart.add(Calendar.DAY_OF_YEAR, 1);
            Integer qty = 0;
            for (int j = 0; j < res.size() ; j++) {
                qty += ((BigDecimal)res.get(j).get("BALANCE_QTY")).intValue() + ((BigDecimal)res.get(j).get("OVERTIME8H_QTY")).intValue();
            }
            result.put(simpleDateFormat.format(tmpCal.getTime()), qty);

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
        return result;
    }

    @Override
    public Object getBonePileTotal(String factory) {
        Calendar calendarStrart = Calendar.getInstance();
        calendarStrart.set(Calendar.HOUR_OF_DAY, 00);
        calendarStrart.set(Calendar.MINUTE, 00);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        if (calendarStrart.get(Calendar.DAY_OF_WEEK) == 7){
            calendarStrart.add(Calendar.DAY_OF_YEAR, -1);
        }else {
            calendarStrart.add(Calendar.WEEK_OF_YEAR, -1);
            calendarStrart.set(Calendar.DAY_OF_WEEK, 6);

            calendarEnd.add(Calendar.DAY_OF_YEAR, -1);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Integer dem = calendarEnd.get(Calendar.DAY_OF_YEAR) - calendarStrart.get(Calendar.DAY_OF_YEAR);
        Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i <= dem; i++) {
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTime(calendarStrart.getTime());
            List<Map<String, Object>> res = sfcTestSerialErrorRepository.qtyReportBalanceOver8h(factory, simpleDateFormat.format(tmpCal.getTime()), listModelByFactory(factory));
            calendarStrart.add(Calendar.DAY_OF_YEAR, 1);
            Integer qtyBalance = 0;
            Integer qtyOverTime = 0;
            Integer qty = 0;
            for (int j = 0; j < res.size() ; j++) {
                qtyBalance += ((BigDecimal)res.get(j).get("BALANCE_QTY")).intValue();
                qtyOverTime += ((BigDecimal)res.get(j).get("OVERTIME8H_QTY")).intValue();
                qty += ((BigDecimal)res.get(j).get("BALANCE_QTY")).intValue() + ((BigDecimal)res.get(j).get("OVERTIME8H_QTY")).intValue();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("balance", qtyBalance);
            map.put("overtime", qtyOverTime);
            map.put("totalqty", qty);
            result.put(simpleDateFormat.format(tmpCal.getTime()), map);
        }
        return result;
    }

    @Override
    public CommonResponse<Object> getErrorCodeByNtf(String factory, String timeSpan) throws Exception {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        List<String> modelList = listModelByFactory(factory);
        List<Map<String, Object>> data = sfcTestSerialErrorRepository.getSNErrorCodeNTF(factory,fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelList, "H003");
        List<String> listSn = new ArrayList<>();
        if (data.size() > 0){
             listSn = data.stream().map(e ->(String) e.get("serial_number")).collect(Collectors.toList());
        }else {
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "There is no NTF Error in the interval", false);
        }

        List<Map<String, Object>> dataLogErrorCode1 = sfcTestSerialErrorRepository.getLogErrorCodeNTF(factory, listSn, 0);
        List<Map<String, Object>> dataLogErrorCode2 = sfcTestSerialErrorRepository.getLogErrorCodeNTF(factory, listSn, 1);
        List<Map<String, Object>> qtyR = sfcTestSerialErrorRepository.countQtySNTestError(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate(), modelList);
        Integer totalError = ((BigDecimal)qtyR.get(0).get("QTY")).intValue();
        float rate = ((float) data.size()/(float)totalError) * 100.0f;
        log.info("pause");
        Map<Object, Map<Object, Map<String, Object>>> log1 =  dataLogErrorCode1
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> {
                                    String tmp = (String) ((Map<String,Object >) e).get("model_name");
                                    return tmp;
                                },
                                LinkedHashMap::new,
                                Collectors.groupingBy(
                                        e -> {
                                            String integer = (String) ((Map<String,Object >) e).get("group_name");
                                            return integer;
                                        },
                                        LinkedHashMap::new,
                                        Collectors.toMap( e -> {
                                            String string = (String) ((Map<String,Object >) e).get("error_code");
                                            return  string;
                                        },e -> e.get("qty"),(e1 , e2) -> e1, LinkedHashMap::new)
                                )
                        )
                );
        Map<Object, Map<Object, Map<String, Object>>> log2 =  dataLogErrorCode2
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> {
                                    String tmp = (String) ((Map<String,Object >) e).get("model_name");
                                    return tmp;
                                },
                                LinkedHashMap::new,
                                Collectors.groupingBy(
                                        e -> {
                                            String integer = (String) ((Map<String,Object >) e).get("group_name");
                                            return integer;
                                        },
                                        LinkedHashMap::new,
                                        Collectors.toMap( e -> {
                                            String string = (String) ((Map<String,Object >) e).get("error_code");
                                            return  string;
                                        },e -> e.get("qty"),(e1 , e2) -> e1, LinkedHashMap::new)
                                )
                        )
                );

    log.debug("pause");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> tmpResult = new HashMap<>();
        tmpResult.put("logBefor", log1);
        tmpResult.put("logAfter", log2);
        tmpResult.put("rate", rate);
        result.add(tmpResult);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
    }


}
