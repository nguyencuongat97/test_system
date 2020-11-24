package com.foxconn.fii.receiver.test.config;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.sfc.repository.SfcSmtGroupRepository;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class TmpSchedulerConfig {

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    /** SFC */
    @Autowired
    private SfcSmtGroupRepository sfcSmtGroupRepository;

    @Scheduled(cron = "0 45 7 * * *")
    public void syncWeeklyData() {
        log.info("### sync output mo START");

        String factory = "B06";
        String customer = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -12);
        TimeSpan fullTimeSpan = TimeSpan.from(calendar, TimeSpan.Type.FULL);

        List<TestPlanMeta> planList = testPlanService.findByFactory(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        List<String> modelOnlineList = planList.stream().map(TestPlanMeta::getModelName).distinct().collect(Collectors.toList());
        List<String> moList = planList.stream().map(TestPlanMeta::getMo).distinct().collect(Collectors.toList());

        if (modelOnlineList.isEmpty()) {
            return;
        }

        List<TestGroupMeta> groupMetaList = testGroupMetaRepository.findGroupOutputByFactory(factory, modelOnlineList);
        Map<String, String> groupMapByModel = groupMetaList.stream()
                .sorted(Comparator.comparingInt(g -> g.getStep() == null ? 1000 : g.getStep()))
                .collect(Collectors.toMap(TestGroupMeta::getModelName, TestGroupMeta::getGroupName, (g1, g2) -> g2));
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

        log.info("### sync output mo END");
    }
}
