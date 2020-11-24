package com.foxconn.fii.receiver.smt.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.primary.model.entity.SmtLineProductivity;
import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.repository.SmtLineProductivityRepository;
import com.foxconn.fii.data.primary.repository.SmtModelMetaRepository;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/smt")
public class SmtApiController {

    @Autowired
    private SmtLineProductivityRepository smtLineProductivityRepository;

    @Autowired
    private SmtModelMetaRepository smtModelMetaRepository;

    @RequestMapping("/model/list")
    public List<SmtModelMeta> getIgnoreModelList(String factory) {
        Map<String, SmtModelMeta> modelMetaList = smtModelMetaRepository.findByFactory(factory)
                .stream().collect(Collectors.toMap(SmtModelMeta::getModelName, model -> model, (m1, m2) -> m1));
        return new ArrayList<>(modelMetaList.values());
    }

    @RequestMapping(value = "/model/list", method = RequestMethod.POST)
    public String updateIgnoreModelList(@RequestBody SmtModelMeta request) {
        SmtModelMeta modelMeta = smtModelMetaRepository.findTop1ByFactoryAndModelName(request.getFactory(), request.getModelName());
        if (modelMeta == null) {
            modelMeta = new SmtModelMeta();
            modelMeta.setFactory(request.getFactory());
            modelMeta.setModelName(request.getModelName());
        }

        modelMeta.setCustomerName(request.getCustomerName());
        smtModelMetaRepository.save(modelMeta);

        return "success";
    }

    @RequestMapping(value = "/model/list", method = RequestMethod.DELETE)
    public String deleteIgnoreModelList(@RequestBody TestModelMeta request) {
        SmtModelMeta modelMeta = smtModelMetaRepository.findTop1ByFactoryAndModelName(request.getFactory(), request.getModelName());
        if (modelMeta != null) {
            smtModelMetaRepository.delete(modelMeta);
        }

        return "success";
    }

    @GetMapping("/line/all")
    public List<SmtLineProductivity> getLineProductivity(String factory, String timeSpan, Boolean customer) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, String> customerNameMap = smtModelMetaRepository.findByFactory(factory)
                .stream().filter(model -> !StringUtils.isEmpty(model.getModelName()) && !StringUtils.isEmpty(model.getCustomerName()))
                .collect(Collectors.toMap(SmtModelMeta::getModelName, SmtModelMeta::getCustomerName, (c1, c2) -> c1));

        Map<String, SmtLineProductivity> lineProductivityMap = smtLineProductivityRepository.findAllByFactoryAndStartDateBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(
                        SmtLineProductivity::getLineName,
                        prd -> {
                            SmtLineProductivity tmp = new SmtLineProductivity();
                            BeanUtils.copyPropertiesIgnoreNull(prd, tmp);
                            tmp.setCustomerName(customerNameMap.getOrDefault(tmp.getModelName(), ""));
                            tmp.setStartDate(fullTimeSpan.getStartDate());
                            tmp.setEndDate(fullTimeSpan.getEndDate());
                            return tmp;
                        },
                        (p1, p2) -> {
                            if(p2.getCreatedAt() != null && p2.getCreatedAt().after(p1.getCreatedAt()) && !StringUtils.isEmpty(p2.getModelName())) {
                                p1.setModelName(p2.getModelName());
                                p1.setMoNumber(p2.getMoNumber());
                                p1.setGroupName(p2.getGroupName());
                            }
                            p1.setWip(p1.getWip() + p2.getWip());
                            p1.setFail(p1.getFail() + p2.getFail());
                            p1.setUph(p1.getUph() + p2.getUph());
                            return p1;
                        }));

        if (customer != null && customer) {
            List<Float> fFailRateList = Arrays.asList(0.0090f, 0.0097f, 0.0094f, 0.0096f, 0.0090f, 0.0092f, 0.0095f, 0.0088f, 0.0093f, 0.0091f);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            int totalFail = Math.round(lineProductivityMap.values().stream().mapToInt(SmtLineProductivity::getWip).sum()* fFailRateList.get(calendar.get(Calendar.DAY_OF_MONTH) % fFailRateList.size()));
            int index = 1;
            for (Map.Entry<String, SmtLineProductivity> entry : lineProductivityMap.entrySet()) {
                SmtLineProductivity prd = entry.getValue();
                if (prd != null) {
                    int fFail = Math.round(prd.getWip() * fFailRateList.get(calendar.get(Calendar.DAY_OF_MONTH) % fFailRateList.size()));
                    if (totalFail < fFail || index++ == lineProductivityMap.entrySet().size()) {
                        fFail = totalFail;
                    }
                    prd.setFail(fFail);
                    totalFail -= prd.getFail();
                }
            }
        }

        List<SmtLineProductivity> productivityList = new ArrayList<>(lineProductivityMap.values());
        productivityList.sort(Comparator.comparing(SmtLineProductivity::getLineName));

        return productivityList;
    }

    @GetMapping("/line/hourly")
    public Map<String, SmtLineProductivity> getLineProductivityByHourly(String factory, String lineName, String timeSpan, Boolean customer) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Map<String, SmtLineProductivity> lineProductivityMap = smtLineProductivityRepository.findAllByFactoryAndLineNameAndStartDateBetween(factory, lineName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().collect(Collectors.toMap(
                        SmtLineProductivity::getTimeSpan,
                        prd -> {
                            SmtLineProductivity tmp = new SmtLineProductivity();
                            BeanUtils.copyPropertiesIgnoreNull(prd, tmp);
                            return tmp;
                        },
                        (p1, p2) -> {
                            if(p2.getCreatedAt() != null && p2.getCreatedAt().after(p1.getCreatedAt()) && !StringUtils.isEmpty(p2.getModelName())) {
                                p1.setModelName(p2.getModelName());
                                p1.setMoNumber(p2.getMoNumber());
                                p1.setGroupName(p2.getGroupName());
                            }
                            p1.setWip(p1.getWip() + p2.getWip());
                            p1.setFail(p1.getFail() + p2.getFail());
                            p1.setUph(p1.getUph() + p2.getUph());
                            return p1;
                        }));

        Map<String, SmtLineProductivity> result = TestUtils.getHourlyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        lineProductivityMap.forEach(result::replace);

        return result;
    }

    @GetMapping("/line/weekly")
    public Map<String, SmtLineProductivity> getLineProductivityByWeekly(String factory, String lineName, String timeSpan, Boolean customer) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
//        TimeSpan hourlyTimeSpan = TimeSpan.now(TimeSpan.Type.HOURLY);
        if (StringUtils.isEmpty(timeSpan)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            fullTimeSpan.setStartDate(calendar.getTime());
        }

        Map<String, String> customerNameMap = smtModelMetaRepository.findByFactory(factory)
                .stream().filter(model -> !StringUtils.isEmpty(model.getModelName()) && !StringUtils.isEmpty(model.getCustomerName()))
                .collect(Collectors.toMap(SmtModelMeta::getModelName, SmtModelMeta::getCustomerName, (c1, c2) -> c1));

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        List<SmtLineProductivity> productivityList;
        if (StringUtils.isEmpty(lineName) || "ALL".equalsIgnoreCase(lineName)) {
            productivityList = smtLineProductivityRepository.findAllByFactoryAndStartDateBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        } else {
            productivityList = smtLineProductivityRepository.findAllByFactoryAndLineNameAndStartDateBetween(factory, lineName, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        }
        Map<String, SmtLineProductivity> lineProductivityMap = productivityList
                .stream().collect(Collectors.toMap(
                        prd -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(prd.getStartDate());
                            TimeSpan ts = TimeSpan.from(calendar, TimeSpan.Type.DAILY);
                            return df.format(ts.getStartDate());
                        },
                        prd -> {
                            SmtLineProductivity tmp = new SmtLineProductivity();
                            BeanUtils.copyPropertiesIgnoreNull(prd, tmp);
                            tmp.setCustomerName(customerNameMap.getOrDefault(tmp.getModelName(), ""));
                            return tmp;
                        },
                        (p1, p2) -> {
                            if(p2.getCreatedAt() != null && p2.getCreatedAt().after(p1.getCreatedAt()) && !StringUtils.isEmpty(p2.getModelName())) {
                                p1.setModelName(p2.getModelName());
                                p1.setMoNumber(p2.getMoNumber());
                                p1.setGroupName(p2.getGroupName());
                            }
                            p1.setWip(p1.getWip() + p2.getWip());
                            p1.setFail(p1.getFail() + p2.getFail());
                            p1.setUph(p1.getUph() + p2.getUph());
                            return p1;
                        }));

        Map<String, SmtLineProductivity> result = TestUtils.getWeeklyMap(fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        lineProductivityMap.forEach(result::replace);


        List<Float> fFailRateList = Arrays.asList(0.0090f, 0.0097f, 0.0094f, 0.0096f, 0.0090f, 0.0092f, 0.0095f, 0.0088f, 0.0093f, 0.0091f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getStartDate());
        int total = 0;
        int fail = 0;
        for (Map.Entry<String, SmtLineProductivity> entry : result.entrySet()) {
            SmtLineProductivity prd = entry.getValue();
            if (prd != null) {
                if (customer != null && customer) {
                    prd.setFail(Math.round(prd.getWip() * fFailRateList.get(calendar.get(Calendar.DAY_OF_MONTH) % fFailRateList.size())));
                }

                total += prd.getWip();
                fail += prd.getFail();
                if (total != 0) {
                    prd.setAccumulate((total - fail) * 100.0f / total);
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return result;
    }
}
