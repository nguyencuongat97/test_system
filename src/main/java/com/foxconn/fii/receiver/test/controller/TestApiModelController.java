package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.MoType;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestModel;
import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import com.foxconn.fii.data.primary.model.entity.TestPcasMeta;
import com.foxconn.fii.data.primary.repository.TestGroupMetaRepository;
import com.foxconn.fii.data.primary.repository.TestPcasMetaRepository;
import com.foxconn.fii.receiver.test.service.TestGroupService;
import com.foxconn.fii.receiver.test.service.TestModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestApiModelController {

    @Autowired
    private TestModelService testModelService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private TestGroupMetaRepository testGroupMetaRepository;

    @GetMapping("/group/list")
    public List<TestGroupMeta> getTestGroupMetaList(String factory, String customer, String stage, String modelName) {
        List<TestGroupMeta> groupMetaList = testGroupService.getGroupMetaList(factory, customer, stage, modelName, null);
        groupMetaList.sort(Comparator.comparing(TestGroupMeta::getStep));
        return groupMetaList;
    }

    @PostMapping("/group/list")
    public Boolean getTestGroupMetaList(@RequestBody TestGroupMeta testGroupMeta) {
        TestGroupMeta ins;
        if (testGroupMeta.getId() != 0) {
            ins = testGroupMetaRepository.findById(testGroupMeta.getId()).orElse(null);
        } else {
            ins = testGroupMetaRepository.findTop1ByFactoryAndModelNameAndGroupName(testGroupMeta.getFactory(), testGroupMeta.getModelName(), testGroupMeta.getGroupName());
        }
        if (ins == null) {
            ins = new TestGroupMeta();
        }
        BeanUtils.copyPropertiesIgnoreNull(testGroupMeta, ins, "id");
        testGroupMetaRepository.save(ins);
        return true;
    }

    @DeleteMapping("/group/list/{id}")
    public Boolean getTestGroupMetaList(@PathVariable Integer id) {
        testGroupMetaRepository.deleteById(id);
        return true;
    }

    @RequestMapping("/model/list")
    public List<TestModelMeta> getIgnoreModelList(String factory, String customer, String stage) {
        return testModelService.getModelMetaList(factory, customer, stage);
    }

    @RequestMapping(value = "/model/list", method = RequestMethod.POST)
    public String updateIgnoreModelList(@RequestBody TestModelMeta request) {
        TestModelMeta modelMeta = testModelService.getModelMeta(request.getFactory(), request.getModelName());
        if (modelMeta == null) {
            modelMeta = new TestModelMeta();
        }

        BeanUtils.copyPropertiesIgnoreNull(request, modelMeta, "id");
        testModelService.save(modelMeta);

        return "success";
    }

    @RequestMapping(value = "/model/list", method = RequestMethod.DELETE)
    public String deleteIgnoreModelList(@RequestBody TestModelMeta request) {
        testModelService.delete(request);
        return "success";
    }

    @RequestMapping("/model/daily")
    public List<TestModel> getDailyModel(
            @RequestParam String factory,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean customer,
            @RequestParam(required = false, defaultValue = "ALL") MoType moType) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        return testModelService.getAllModelDaily(factory, customerName, fullTimeSpan, customer, moType);
    }

    @GetMapping("/model/weekly")
    public Map<String, TestModel> getWeeklyModel(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "false") Boolean customer,
            @RequestParam(required = false, defaultValue = "ALL") MoType moType) {

        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendarNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendarNow.setTime(fullTimeSpan.getStartDate());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        Map<String, TestModel> result = new LinkedHashMap<>();

        SimpleDateFormat monthdf = new SimpleDateFormat("yyyy/MM");
        while (calendar.get(Calendar.MONTH) < calendarNow.get(Calendar.MONTH)) {
            Date startDate = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);

            TestModel model = testModelService.getModel(factory, modelName, TimeSpan.of(startDate, calendar.getTime()), customer, moType);

            result.put(monthdf.format(startDate), model);
        }

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        while (calendar.get(Calendar.WEEK_OF_MONTH) < (calendarNow.get(Calendar.WEEK_OF_MONTH) - 1)) {
            Date startDate = calendar.getTime();
            calendar.add(Calendar.WEEK_OF_MONTH, 1);

            TestModel model = testModelService.getModel(factory, modelName, TimeSpan.of(startDate, calendar.getTime()), customer, moType);

            result.put(df.format(startDate) + " - " + df.format(calendar.getTime()), model);
        }

        fullTimeSpan.setStartDate(calendar.getTime());
        testModelService.getModelWeekly(factory, modelName, fullTimeSpan, customer, moType).forEach(result::put);

        return result;
    }

}
