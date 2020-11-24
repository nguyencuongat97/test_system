package com.foxconn.fii.receiver.re.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.common.response.SortableMapResponse;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import com.foxconn.fii.data.primary.repository.RepairTimeDataBetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.receiver.hr.service.HrEmployeeMealTypeVnService;
import com.foxconn.fii.receiver.re.service.RepairOnlineWipService;
import com.foxconn.fii.receiver.re.service.RepairSyncDataService;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/re/online")
public class ReApiOnlineWipController {
    @Autowired
    private RepairOnlineWipService repairOnlineWipService;

    @Autowired
    private RepairSyncDataService repairSyncDataService;

    @Autowired
    private TestRepairSerialErrorService testRepairSerialErrorService;

    @Autowired
    private HrEmployeeMealTypeVnService hrEmployeeMealTypeVnService;

    @Autowired
    private RepairTimeDataBetaRepository repairTimeDataBetaRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @GetMapping("/wipInOutStatus")
    public Object wipInOutStatus(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getWipInOutStatus(factory, timeSpan);
    }

    @RequestMapping("/remainDaily")
    public Object remainByDayli(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getDataRemainDaily(factory, timeSpan);
    }

    @RequestMapping("/modelByTime")
    public Map<String, Map<String, Object>> modenamelByTime(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
//        return repairOnlineWipService.getDataModelNameAndTime(factory, timeSpan);
        return repairOnlineWipService.getDataModelNameAndHoureTime(factory, timeSpan);
    }

    @RequestMapping("/bonpile_by_model")
    public Object bonpileByModel(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        return repairOnlineWipService.bonpileByModelService(factory, timeSpan);
    }

    @RequestMapping("/bonpile_by_model_by_error_code")
    public Object bonpileByModelByErrorCode(@RequestParam String factory, @RequestParam String action, @RequestParam String modelName, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        return repairOnlineWipService.bonpileByModelByErrorCodeService(factory, action, modelName, timeSpan);
    }

    @RequestMapping("/errorCodeWip")
    public Object errorCodeByModelName(@RequestParam String factory, @RequestParam String modelName) throws ParseException{
        return repairOnlineWipService.getDataErrorCodeByModelName(factory, modelName);
    }

//    @PostMapping("/8s/leaderconfirm/by/daily/savedata")
//    public void re8sLeaderconfirmByDailySavedata(@RequestBody String jsonString) throws IOException {}

    @GetMapping("/wipInOutTrendChart")
    public Object getWipInOutTrendChart(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String parameter) throws ParseException{

        Map<String, Object> balance = repairOnlineWipService.getDataWipInOutTrendChart(factory, "BALANCE_QTY");
        Map<String, Object> overTime8h = repairOnlineWipService.getDataWipInOutTrendChart(factory, "OVERTIME8H_QTY");
        Map<String, Object> bonepile = new LinkedHashMap<>();
        for (String key : balance.keySet()) {
            Integer qty = ((BigDecimal) balance.get(key)).intValue() + ((BigDecimal) overTime8h.get(key)).intValue();
            bonepile.put(key, qty);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("balance",balance);
        result.put("overtime8h",overTime8h);
        result.put("bonePile",bonepile);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", result);
//        return repairOnlineWipService.getDataWipInOutTrendChart(factory, parameter);
    }

    @GetMapping("/ProcessNTF")
    public  CommonResponse<Object> getNTFAndProcess(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getNTFAndProcessAndCompoment(factory, timeSpan);
    }

    @GetMapping("/defected/all")
    public Map<String, Long> getDefectedNtf(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String defected) throws ParseException{
        return repairOnlineWipService.getModelNameByDefected(factory, timeSpan, defected);
    }

    @GetMapping("/defected/detail")
    public CommonResponse<Object> getDefectedNtf(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String defected, String modelName) throws ParseException{
        return repairOnlineWipService.getModelNameByDefectedByProcess(factory, timeSpan, defected, modelName);
    }

    @GetMapping("/defected/reason/detail")
    public CommonResponse<Object> getErrorCodeByReasonCode(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String modelName, String reasonCode) throws ParseException{
        return repairOnlineWipService.getErrorCodeByReasonCodeByProcess(factory, timeSpan, modelName, reasonCode);
    }

    @RequestMapping("/detailError")
    public Map<String, Long> detailErrorByModelName(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String modelName) throws ParseException{
            TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dailyTimeSpan.getEndDate());
            calendar.add(Calendar.YEAR, -1);
            dailyTimeSpan.setStartDate(calendar.getTime());

            List<Object[]> data = testRepairSerialErrorService.countByModelNameAndErrorCode(factory, TestRepairSerialError.Status.UNDER_REPAIR, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

            Map<String, Long> result = new LinkedHashMap<>();

            for (Object[] value : data) {
                String model = (String) value[0];
                String errorCode = (String) value[1];
                Long count = (Long) value[2];

                if (model.equalsIgnoreCase(modelName)) {
                    result.put(errorCode, result.getOrDefault(errorCode, 0L) + count);
                }
            }

            result = result.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(15)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            return result;
    }

    @GetMapping("/modelnamesismtpth")
    public Map<String, Object> getModelNameSISMTPTH(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getModelNameSMTAndPTHAndSI(factory, timeSpan);
    }

    @GetMapping("/modelnamesismtpth/error")
    public Map<String, Integer> getErrorCodeByModelNameSI(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String modelName) throws ParseException{
        return repairOnlineWipService.getErrorCodeByModelName(factory, timeSpan, modelName);
    }

    @GetMapping("/loadTblInOut")
    public Map<String, Integer> getInOutByModelName(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getInputOutputByModelName(factory, timeSpan);
    }

    @RequestMapping("/errorReasonLocation")
    public Object modenamelByTime(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan, String modelName, String defected) throws ParseException{
        return repairOnlineWipService.getErrorReasonLocationByModelName(factory, timeSpan, modelName, defected);
    }

    @RequestMapping("/testApi")
    public void dddd(@RequestParam String startDate, @RequestParam String endDate)throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            //return repairOnlineWipService.syncDataBC8MCheckIn(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
          //   repairOnlineWipService.syncDataBC8MCheckOut(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
          //   hrEmployeeMealTypeVnService.HrSyncDataCanteenFromAPI(simpleDateFormat.parse(endDate));
//        hrEmployeeMealTypeVnService.getDataMealBNAndBN3SendMail(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
      //  repairOnlineWipService.saveDataCheckInToDataRaw();

       // repairSyncDataService.syncDataRepairOnlineWipFromB04(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
    }

    @GetMapping("/bc8mremain")
    public Object countModelNameRemain(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairOnlineWipService.getDataBC8MRemain(factory, timeSpan);
    }

    @GetMapping("/bc8mtop15errorcode")
    public Object countTop15ErrorCode(@RequestParam String factory, @RequestParam String modelName) throws ParseException{
        return repairOnlineWipService.getTop15ErrorCode(factory, modelName);
    }

    @GetMapping("/bc8minoutchart")
    public Object getInOutTrendChart(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException, IOException{
        return repairOnlineWipService.getInOutTrendChart(factory, timeSpan);
    }

    @GetMapping("/importData")
    public void importDataByModelName(HttpServletResponse resonse, @RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException, IOException {
        repairOnlineWipService.exportDataCheckOutByUserCapacity(resonse, factory, timeSpan);
    }

    @GetMapping("/export_data_bonepile")
    public void exportDataBonepile(HttpServletResponse response, @RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        repairOnlineWipService.exportDataBonepile(response, factory, timeSpan);
    }

    @GetMapping("/inputTime")
    public Object countInputByTime(@RequestParam String factory) throws ParseException, IOException {
        return repairOnlineWipService.getCountInputByTime(factory);
    }

    @GetMapping("/inoutrma")
    public Object getStatusRemainRMA(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException, IOException {
        return repairOnlineWipService.getDataRMAInOutRemain(factory, timeSpan);
    }

    @GetMapping("/errorcoderma")
    public Object getErrorCodeRMAByModelName(@RequestParam String factory, @RequestParam String modelName) throws ParseException, IOException {
        return repairOnlineWipService.getDataRMAErrorCodeByModelName(factory, modelName);
    }

    @GetMapping("/rmatrendchart")
    public Object getRMATrendChart(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException, IOException {
        return repairOnlineWipService.getDataRMAInoutTrendChart(factory, timeSpan);
    }

    @GetMapping("/downloadBC8M")
    public void dowloadFileExcelDataRemainBC8M(HttpServletResponse resonse, @RequestParam String factory) throws ParseException, IOException {
//       String[] d = modelName.split(";");
//        List<String> models = new ArrayList<>();
//        for (String item : d) {
//            models.add(item);
//        }
        repairOnlineWipService.downloadExcelFileDataRemainBC8M(resonse, factory);
    }

    @GetMapping("/getListModel")
    public Object getListModelsB04(String factory){
        return repairOnlineWipService.getListModelB04(factory);
    }

    @GetMapping("/downloadRMA")
    public void dowloadFileExcelDataRemainRMA(HttpServletResponse resonse, @RequestParam String factory) throws ParseException, IOException {
        repairOnlineWipService.downloadExcelFileDataRemainRMA(resonse, factory);
    }


    @GetMapping("/importonlinewip")
    public void importOnlineWipRemain(HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String modelName) throws Exception{
        repairOnlineWipService.importOnlineWipRemain(response, modelName);
    }

    @GetMapping("/get_error_code_repair")
    public Object getErrorCodeRepair( @RequestParam String factory, @RequestParam String modelName, @RequestParam(required = false, defaultValue = "") String timeSpan) throws Exception{
        return repairOnlineWipService.getErrorCodeByModelNameC03(factory, modelName, timeSpan);
    }


}
