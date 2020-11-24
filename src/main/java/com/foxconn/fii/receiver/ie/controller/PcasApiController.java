package com.foxconn.fii.receiver.ie.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.primary.model.PcasManpower;
import com.foxconn.fii.data.primary.model.entity.SmtMps;
import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;
import com.foxconn.fii.data.primary.model.entity.TestPcasMeta;
import com.foxconn.fii.data.primary.repository.SmtModelMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtPcasCycleTimeRepository;
import com.foxconn.fii.data.primary.repository.TestPcasMetaRepository;
import com.foxconn.fii.receiver.smt.service.SmtMpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class PcasApiController {

    @Autowired
    private TestPcasMetaRepository testPcasMetaRepository;

    @Autowired
    private SmtModelMetaRepository smtModelMetaRepository;

    @Autowired
    private SmtPcasCycleTimeRepository smtPcasCycleTimeRepository;

    @Autowired
    private SmtMpsService smtMpsService;

    @PostMapping("/model/pcas")
    public Boolean savePcas(@RequestBody TestPcasMeta pcasMeta) {
        if (StringUtils.isEmpty(pcasMeta.getFactory()) || StringUtils.isEmpty(pcasMeta.getModelName()) || StringUtils.isEmpty(pcasMeta.getGroupName())) {
            return false;
        }
        if (pcasMeta.getId() == 0) {
            TestPcasMeta pcas = testPcasMetaRepository.findByFactoryAndModelNameAndGroupName(pcasMeta.getFactory(), pcasMeta.getModelName(), pcasMeta.getGroupName());
            if (pcas == null) {
                pcas = new TestPcasMeta();
            }
            BeanUtils.copyPropertiesIgnoreNull(pcasMeta, pcas, "id");
            testPcasMetaRepository.save(pcas);
        } else {
            TestPcasMeta pcas = testPcasMetaRepository.findById(pcasMeta.getId()).orElse(null);
            if (pcas == null) {
                return false;
            }
            BeanUtils.copyPropertiesIgnoreNull(pcasMeta, pcas, "id");
            testPcasMetaRepository.save(pcas);
        }
        return true;
    }

    @RequestMapping("/model/pcas")
    public List<TestPcasMeta> getDetailPcas(String factory, String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            List<TestPcasMeta> pcasMetaList = testPcasMetaRepository.findByFactory(factory);
            pcasMetaList.sort(Comparator.comparing(TestPcasMeta::getModelName));
            return pcasMetaList;
        }
        return testPcasMetaRepository.findByFactoryAndModelName(factory, modelName);
    }

    @RequestMapping("/manpower")
    public Map<String, List<PcasManpower>> manPower(String factory, String sectionName, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan);
        if (fullTimeSpan == null) {
            fullTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fullTimeSpan.getStartDate());
            calendar.add(Calendar.MONTH, 2);
            fullTimeSpan.setEndDate(calendar.getTime());
        }

        Map<String, List<SmtMps>> smtMpsMap = smtMpsService.getSmtMpsList(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream()
                .filter(mps -> !StringUtils.isEmpty(mps.getModelNameSI()))
                .collect(Collectors.groupingBy(SmtMps::getModelNameSI));

        Map<String, Map<String, SmtPcasCycleTime>> cycleTimeList = smtPcasCycleTimeRepository.findByFactoryAndVisibleIsTrue(factory)
                .stream().filter(cycleTime -> "PTH".equalsIgnoreCase(cycleTime.getSectionName()) || "SI".equalsIgnoreCase(cycleTime.getSectionName()))
                .collect(Collectors.groupingBy(SmtPcasCycleTime::getModelName, Collectors.toMap(SmtPcasCycleTime::getSectionName, cycleTime -> cycleTime, (c1, c2) -> c1)));

//        Map<String, SmtModelMeta> modelMetaMap = smtModelMetaRepository.findByFactory(factory).stream().collect(Collectors.toMap(SmtModelMeta::getModelNameSI, meta -> meta, (m1, m2) -> m1));

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<PcasManpower> data = new ArrayList<>();
        for (Map.Entry<String, Map<String, SmtPcasCycleTime>> modelEntry : cycleTimeList.entrySet()) {
            for (Map.Entry<String, SmtPcasCycleTime> sectionEntry : modelEntry.getValue().entrySet()) {
                SmtPcasCycleTime pcasCycleTime = sectionEntry.getValue();
//                SmtModelMeta modelMeta = modelMetaMap.get(pcasCycleTime.getModelName());
//                if (modelMeta == null) {
//                    continue;
//                }

                PcasManpower manpower = new PcasManpower();
                manpower.setFactory(factory);
                manpower.setSectionName(pcasCycleTime.getSectionName());
                manpower.setModelName(pcasCycleTime.getModelName());
                manpower.setCycleTime(pcasCycleTime.getCycleTime());
                manpower.setPcasManPower(pcasCycleTime.getManPower());

                if (manpower.getCycleTime() != null && manpower.getPcasManPower() != null &&
                        smtMpsMap.containsKey(pcasCycleTime.getModelName())) {
                    List<SmtMps> mpsList = smtMpsMap.get(pcasCycleTime.getModelName());
                    int forecast = 0;
                    for (SmtMps mps : mpsList) {
                        manpower.setCft(mps.getCft());
                        PcasManpower.ManPowerItem item = new PcasManpower.ManPowerItem();
                        item.setDatetime(df.format(mps.getStartDate()));
                        item.setRunningDay(mps.getRunningDay());
                        item.setForecast(mps.getPlan());
                        item.setMPoint(item.getForecast() / (item.getRunningDay() * manpower.getDailyOutput()));
                        item.setManPower(item.getMPoint() * manpower.getPcasManPower() * 7 / 6 * 2);
                        manpower.getItemList().put(item.getDatetime(), item);
                        forecast += mps.getPlan();
                    }

                    if (forecast > 0) {
                        data.add(manpower);
                    }
                }
            }
        }

        Map<String, List<PcasManpower>> result = data.stream().collect(Collectors.groupingBy(PcasManpower::getModelName, LinkedHashMap::new, Collectors.toList()));

        Map<String, PcasManpower> total = new HashMap<>();
        result.forEach((model, manPowerList) -> {
            for (PcasManpower mp : manPowerList) {
                PcasManpower manpower = total.get(mp.getSectionName());
                if (manpower == null) {
                    manpower = new PcasManpower();
                    BeanUtils.copyPropertiesIgnoreNull(mp, manpower);
                    Map<String, PcasManpower.ManPowerItem> copiedItemList = new TreeMap<>();
                    for (Map.Entry<String, PcasManpower.ManPowerItem> itemEntry : mp.getItemList().entrySet()) {
                        PcasManpower.ManPowerItem copiedItem = new PcasManpower.ManPowerItem();
                        BeanUtils.copyPropertiesIgnoreNull(itemEntry.getValue(), copiedItem);
                        copiedItemList.put(itemEntry.getKey(), copiedItem);
                    }
                    manpower.setItemList(copiedItemList);
                    total.put(mp.getSectionName(), manpower);
                } else {
                    for (Map.Entry<String, PcasManpower.ManPowerItem> itemEntry : mp.getItemList().entrySet()) {
                        PcasManpower.ManPowerItem item = manpower.getItemList().get(itemEntry.getKey());
                        if (item != null) {
                            item.setMPoint(item.getMPoint() + itemEntry.getValue().getMPoint());
                            item.setForecast(item.getForecast() + itemEntry.getValue().getForecast());
                            item.setManPower(item.getManPower() + itemEntry.getValue().getManPower());
                        }
                    }
                }
            }
        });
        result.put("TOTAL", new ArrayList<>(total.values()));

        result.forEach((model, manPowerList) -> {
            if (manPowerList.isEmpty()) {
                return;
            }

            PcasManpower manpower = new PcasManpower();
            BeanUtils.copyPropertiesIgnoreNull(manPowerList.get(0), manpower);
            manpower.setSectionName("ALL");
            Map<String, PcasManpower.ManPowerItem> copiedItemList = new TreeMap<>();
            for (Map.Entry<String, PcasManpower.ManPowerItem> itemEntry : manpower.getItemList().entrySet()) {
                PcasManpower.ManPowerItem copiedItem = new PcasManpower.ManPowerItem();
                BeanUtils.copyPropertiesIgnoreNull(itemEntry.getValue(), copiedItem);
                copiedItemList.put(itemEntry.getKey(), copiedItem);
            }
            manpower.setItemList(copiedItemList);

            for (int i = 1; i < manPowerList.size(); i++) {
                for (Map.Entry<String, PcasManpower.ManPowerItem> itemEntry : manPowerList.get(i).getItemList().entrySet()) {
                    PcasManpower.ManPowerItem item = manpower.getItemList().get(itemEntry.getKey());
                    item.setManPower(item.getManPower() + itemEntry.getValue().getManPower());
                }
            }
            manPowerList.removeIf(mp -> !StringUtils.isEmpty(sectionName) && !sectionName.equalsIgnoreCase(mp.getSectionName()));
            manPowerList.add(manpower);
        });
        log.debug("manpower END");
        return result;
    }
}
