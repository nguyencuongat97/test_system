package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.Factory;
import com.foxconn.fii.data.b06.service.B06TestGroupService;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestUphRealtime;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.data.primary.repository.TestUphRealtimeRepository;
import com.foxconn.fii.data.primary.repository.TestUphTargetRepository;
import com.foxconn.fii.receiver.test.service.TestModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestApiUphController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestUphRealtimeRepository testUphRealtimeRepository;

    @Autowired
    private TestUphTargetRepository testUphTargetRepository;

    @Autowired
    private B06TestGroupService b06TestGroupService;

    @RequestMapping("/uph")
    public List<TestUphTarget> getUph(String factory, String customer, String modelName) {
        if (StringUtils.isEmpty(customer)) {
            customer = "";
        }

        if ((StringUtils.isEmpty(customer) || "ALL".equalsIgnoreCase(customer)) && StringUtils.isEmpty(modelName)) {
            return testUphTargetRepository.findByFactory(factory);
        } else if (StringUtils.isEmpty(modelName)) {
            return testUphTargetRepository.findByFactoryAndCustomer(factory, customer);
        } else {
            return testUphTargetRepository.findByFactoryAndCustomerAndModelName(factory, customer, modelName);
        }
    }

    @PostMapping("/uph")
    public Boolean createUph(@RequestBody TestUphTarget uph) {
        TestUphTarget uphTarget = new TestUphTarget();
        BeanUtils.copyPropertiesIgnoreNull(uph, uphTarget, "id");
        uphTarget.setFactory(uphTarget.getFactory().toUpperCase());


        if (StringUtils.isEmpty(uphTarget.getCustomer())) {
            uphTarget.setCustomer("");
        }

        if (testUphTargetRepository.findByFactoryAndCustomerAndLineNameAndModelNameAndGroupNameAndTime(uphTarget.getFactory(), uphTarget.getCustomer(), uphTarget.getLineName(), uphTarget.getModelName(), uphTarget.getGroupName(), uphTarget.getTime()).isEmpty()) {
            testUphTargetRepository.save(uphTarget);
        }
        return true;
    }

    @PutMapping("/uph/{id}")
    public Boolean updateUph(@PathVariable("id") Integer id, @RequestBody TestUphTarget uph) {
        TestUphTarget uphTarget = testUphTargetRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(uph, uphTarget, "id");
        uphTarget.setFactory(uphTarget.getFactory().toUpperCase());

        testUphTargetRepository.save(uphTarget);
        return true;
    }

    @DeleteMapping("/uph/{id}")
    public Boolean removeUph(@PathVariable("id") Integer id) {
        TestUphTarget uphTarget = testUphTargetRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph id %d not found", id)));

        testUphTargetRepository.delete(uphTarget);
        return true;
    }

    @GetMapping("/group/uph")
    public Map<String, Map<String, Integer>> getUPH(String modelName, String workDate, ShiftType shiftType) {
        return b06TestGroupService.getUPH(modelName, workDate, shiftType);
    }

    @GetMapping("/group/uph/all")
    public Object getAllUPH(@RequestParam String factory,
                            @RequestParam(required = false) Boolean parameter,
                            @RequestParam(required = false) String timeSpan,
                            @RequestParam String workDate,
                            @RequestParam ShiftType shiftType) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.DAILY));

        List<String> modelList = testModelService.getModelMetaList(factory, parameter)
                .stream()
                .filter(meta -> meta.getUpdatedAt().after(fullTimeSpan.getStartDate()))
                .sorted(Comparator.comparing(TestModelMeta::getUpdatedAt, Comparator.reverseOrder()))
                .map(e -> e.getModelName())
                .collect(Collectors.toList());

        return b06TestGroupService.getAllUPH(modelList, workDate, shiftType);
    }

    @GetMapping("/uph/realtime")
    public List<TestUphRealtime> getUphRealTime(@RequestParam String factory) {
        return testUphRealtimeRepository.findByFactoryOrderByWorkSectionAsc("B06");
    }

    @PostMapping("/uph/realtime")
    public Boolean createUphRealTime(@RequestBody TestUphRealtime realtime) {
        realtime.setFactory("B06");
        TestUphRealtime uphRealtime = new TestUphRealtime();
        BeanUtils.copyPropertiesIgnoreNull(realtime, uphRealtime, "id");
        if (testUphRealtimeRepository.findByFactoryAndWorkSection(uphRealtime.getFactory(), uphRealtime.getWorkSection()).isEmpty()) {
            testUphRealtimeRepository.save(uphRealtime);
        }
        return true;
    }

    @PutMapping("/uph/realtime/{id}")
    public Boolean updateUphRealTime(@PathVariable("id") Long id, @RequestBody TestUphRealtime realtime) {
        realtime.setFactory("B06");
        TestUphRealtime uphRealtime = testUphRealtimeRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph realtime id %d not found", id)));

        BeanUtils.copyPropertiesIgnoreNull(realtime, uphRealtime, "id");
        testUphRealtimeRepository.save(uphRealtime);
        return true;
    }

    @DeleteMapping("/uph/realtime/{id}")
    public Boolean removeUphRealTime(@PathVariable("id") Long id) {
        TestUphRealtime uphRealtime = testUphRealtimeRepository.findById(id)
                .orElseThrow(() -> CommonException.of(String.format("uph realtime id %d not found", id)));

        testUphRealtimeRepository.delete(uphRealtime);
        return true;
    }
}
