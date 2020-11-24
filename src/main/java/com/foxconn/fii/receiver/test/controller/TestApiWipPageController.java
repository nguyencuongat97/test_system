package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.data.primary.repository.TestModelMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcWipTrackingRepository;
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
@RequestMapping("/api/wip/page")
public class TestApiWipPageController {
    @Autowired
    private SfcWipTrackingRepository sfcWipTrackingRepository;

    @Autowired
    private TestModelMetaRepository testModelMetaRepository;

    @GetMapping("/")
    public ListResponse<String> getCustomer(@RequestParam String factory)
    {
        List<String> listCustomer = testModelMetaRepository.getCustomer(factory);
        return ListResponse.success(listCustomer ,listCustomer.size());
    }
    

    @GetMapping("/qty-wip")
    public CommonResponse<Integer> getQtyWip(@RequestParam String factory , @RequestParam String customer)
    {
        Integer qty =0;
        List<String> listModel = testModelMetaRepository.findModelByFactoryAndCustomer(factory,customer);
        for(int i=0;i<listModel.size();i++)
        {
            List<Map<String,Object>> list =  sfcWipTrackingRepository.getQtyWip(factory,listModel.get(i));
            qty +=Integer.parseInt(list.get(0).get("QTY").toString());
        }
        return CommonResponse.success(qty);
    }


    @GetMapping("/qty-wip-by-time")
    public CommonResponse<Map<String,Object>> getQtyWipByTime(@RequestParam String factory , @RequestParam String customer)
    {
        Integer time1 =0,time2 =0,time3=0;
        List<String> listModel = testModelMetaRepository.findModelByFactoryAndCustomer(factory,customer);
        for(int i=0;i<listModel.size();i++) {
            List<Map<String, Object>> list = sfcWipTrackingRepository.getQtyWipByTime(factory, listModel.get(i));
            for(Map<String, Object> rl:list)
            {
                time1+=Integer.parseInt(rl.get("TIME1").toString());
                time2+=Integer.parseInt(rl.get("TIME2").toString());
                time3+=Integer.parseInt(rl.get("TIME3").toString());
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("48H <= time < 72H",time1);
        result.put("72H <= time < 168H",time2);
        result.put("168H >=time",time3);
        return CommonResponse.success(result);
    }
    @GetMapping("/qty-wip-by-mo")
    public ListResponse<Map<String, Object>> getQtyWipByMo(@RequestParam String factory , @RequestParam String customer) throws  Exception
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> listModel = testModelMetaRepository.findModelByFactoryAndCustomer(factory,customer);

        for(int i=0;i<listModel.size();i++) {
            List<Map<String, Object>> listGroupByMoStarTime =sfcWipTrackingRepository.getQtyWipGroupByMoStarTime(factory, listModel.get(i));
            for(Map<String, Object> rl:listGroupByMoStarTime)
            {
                Map<String ,Object> groupM = new LinkedHashMap<>();
                List<Map<String, Object>> listGroupStation =new ArrayList<>();
                Date d =df.parse(rl.get("DAY").toString());
                listGroupStation = sfcWipTrackingRepository.getQtyWipGroupStation(factory, listModel.get(i), rl.get("MO_NUMBER").toString(), d);
                groupM.put("mo" ,rl.get("MO_NUMBER"));
                groupM.put("day",df.format(rl.get("DAY")));
                groupM.put("modelName",listModel.get(i));
                groupM.put("byStation",listGroupStation);
                result.add(groupM);
            }

        }
        return ListResponse.success(result,result.size());
    }
    @GetMapping("/qty-wip-by-model")
    public ListResponse<Map<String, Object>> getQtyWipByLine(@RequestParam String factory , @RequestParam String customer) throws  Exception
    {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> listModel = testModelMetaRepository.findModelByFactoryAndCustomer(factory,customer);
        for(int i=0;i<listModel.size();i++) {
            Map<String, Object> lm = new LinkedHashMap<>();
            List<Map<String, Object>> listModelGroupStation = sfcWipTrackingRepository.getStationByModel(factory, listModel.get(i));
            lm.put(listModel.get(i),listModelGroupStation);
            result.add(lm);
        }
        return ListResponse.success(result,result.size());
    }

}
