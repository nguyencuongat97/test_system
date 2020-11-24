package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.data.*;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcTestSerialErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/ntf/page")
public class TestApiNtfPageController {

    @Autowired
    private SfcTestSerialErrorRepository sfcTestSerialErrorRepository;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @GetMapping("/ntf-model-name")
    public  ListResponse<String> getModelNameNtf(@RequestParam String factory )
    {
        List<String> list = testModelMetaRepository.findModelByFactoryAndCustomer(factory,"");
        return  ListResponse.success(list, list.size());

    }
    @GetMapping("/ntf-test-group")
    public ListResponse<Map<String, Object>> getTestGroup(@RequestParam String factory, @RequestParam String modelName, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");

        List<Map<String, Object>> listTestGroup = sfcTestSerialErrorRepository.getTestGroupBeetwens(factory, "", modelName,dailyTimeSpan.getStartDate(),dailyTimeSpan.getEndDate());
        List<TestGroupMeta> listGroupName = testGroupMetaRepository.findByFactoryAndModelNameAndVisibleTrue(factory,modelName);


        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> rl1 : listTestGroup) {
            for(int i =0; i<listGroupName.size();i++)
            {
                if(rl1.get("TEST_GROUP").toString().equals(listGroupName.get(i).getGroupName().toString()))
                {
                    list.add(rl1);
                }
            }
        }

        List<Map<String, Object>> date = new ArrayList<>();
        List<Map<String, Object>> resultss = new ArrayList<>();
        Map<String, Object> pt1 = new LinkedHashMap<>();

        for (Map<String, Object> rl : list) {
            Map<String, Object> dataDate = new LinkedHashMap<>();
            List<Map<String, Object>> results = new ArrayList<>();
            date = sfcTestSerialErrorRepository.getNtfDayBeetwens(factory, "", modelName, dailyTimeSpan.getStartDate() , dailyTimeSpan.getEndDate());
            results = sfcTestSerialErrorRepository.countNtfByModelNameAndBeetwens(factory, "", modelName, rl.get("TEST_GROUP").toString(), dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
            for (Map<String, Object>  d : date)
            {
                dataDate.put(df.format(d.get("Day")).toString(), null);
                for(Map<String, Object> rls : results) {
                    if(d.get("Day").toString().equals(rls.get("Day").toString()))
                    {
                        NtfByDay dataDay = new NtfByDay();
                        dataDay.setDay(df1.format(rls.get("Day")));
                        dataDay.setQtyErrorNtf(Integer.parseInt(rls.get("ERROR_NTF").toString()));
                        dataDay.setQtyError(Integer.parseInt(rls.get("ERROR").toString()));
                        dataDay.setRatioNtf(Float.parseFloat(rls.get("PHAN_TRAM_NTF").toString()));

                        dataDate.remove(rls.get("Day").toString());
                        dataDate.put(df.format(rls.get("Day")).toString(),dataDay);
                    }

                }
            }
            pt1.put(rl.get("TEST_GROUP").toString(), dataDate);
        }
        resultss.add(pt1);
        return ListResponse.success(resultss, pt1.size());
    }

    @GetMapping("ntf-daily-errorCode")
    public ListResponse<ErrorCodeOfNtf> getErrorCodeByDay(@RequestParam String factory, @RequestParam String modelName, String testGroup) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Map<String, Object>> listErorCode = sfcTestSerialErrorRepository.countNtfErrorCode(factory, "",modelName ,testGroup);
        List<ErrorCodeOfNtf> result = new ArrayList<>();

        for(Map<String, Object> rls : listErorCode)
        {
            ErrorCodeOfNtf dataErrorCode = new ErrorCodeOfNtf();
            dataErrorCode.setTestCode(rls.get("TEST_CODE").toString());
            dataErrorCode.setQtyErrorNtf(Integer.parseInt(rls.get("ERROR_NTF").toString()));
            dataErrorCode.setQtyError(Integer.parseInt(rls.get("ERROR").toString()));
            dataErrorCode.setRatioNtf(Float.parseFloat(rls.get("PHAN_TRAM_NTF").toString()));

            result.add(dataErrorCode);
        }
        return ListResponse.success(result, result.size());
    }

    @GetMapping("ntf-daily-station")
    public ListResponse<StationOfNtf> getStationByDay(@RequestParam String factory, @RequestParam String modelName, String testGroup ) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Map<String, Object>> listStation = sfcTestSerialErrorRepository.countNtfStation(factory, "",modelName,testGroup );
        List<StationOfNtf> result = new ArrayList<>();

        for(Map<String, Object> rls : listStation)
        {

            StationOfNtf dataStation = new StationOfNtf();
            dataStation.setTestStation(rls.get("TEST_STATION").toString());
            dataStation.setQtyErrorNtf(Integer.parseInt(rls.get("ERROR_NTF").toString()));
            dataStation.setQtyError(Integer.parseInt(rls.get("ERROR").toString()));
            dataStation.setRatioNtf(Float.parseFloat(rls.get("PHAN_TRAM_NTF").toString()));

            result.add(dataStation);

        }
        return ListResponse.success(result, result.size());
    }

    @GetMapping("ntf-machine-by-errorcode")
    public ListResponse<MachineOfErrorCode> getMachineByErrorCode(@RequestParam String factory, @RequestParam String modelName, String testGroup, String testCode) {
        List<Map<String, Object>> listMachine = sfcTestSerialErrorRepository.getMachineOfErrorCode(factory, "",modelName,testGroup ,testCode);
        List<MachineOfErrorCode> result = new ArrayList<>();

        for(Map<String, Object> rls : listMachine)
        {

            MachineOfErrorCode dataMachine = new MachineOfErrorCode();
            dataMachine.setTestStation(rls.get("TEST_STATION").toString());
            dataMachine.setQtyMachine(Integer.parseInt(rls.get("QTY").toString()));
            result.add(dataMachine);
        }
        return ListResponse.success(result, result.size());
    }

    @GetMapping("ntf-errorcode-by-machine")
    public ListResponse<ErrorCodeOfMachine> getErrorCodeByMachine(@RequestParam String factory, @RequestParam String modelName, String testGroup, String testStation) {
        List<Map<String, Object>> listErrorCode= sfcTestSerialErrorRepository.getErrorCodeOfMachine(factory, "",modelName,testGroup ,testStation);
        List<ErrorCodeOfMachine> result = new ArrayList<>();

        for(Map<String, Object> rls : listErrorCode)
        {

            ErrorCodeOfMachine dataError = new ErrorCodeOfMachine();
            dataError.setTestCode(rls.get("TEST_CODE").toString());
            dataError.setQtyErrorCode(Integer.parseInt(rls.get("QTY").toString()));

            result.add(dataError);
        }
        return ListResponse.success(result, result.size());
    }
}
