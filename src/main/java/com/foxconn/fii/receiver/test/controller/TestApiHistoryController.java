package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.data.b04.repository.B04LogRepository;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.primary.model.entity.TestFixedHistory;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import com.foxconn.fii.data.primary.repository.TestFixedHistoryRepository;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaRepository;
import com.foxconn.fii.receiver.test.service.TestLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/test/history")
public class TestApiHistoryController {

    @Autowired
    private B04LogRepository b04LogRepository;

    @Autowired
    private TestLockService testLockService;

    @Autowired
    private TestFixedHistoryRepository testFixedHistoryRepository;

    @Autowired
    private TestSolutionMetaRepository testSolutionMetaRepository;

    @RequestMapping("/calibration")
    public List<Timestamp> getCalibrationHistory(
            @RequestParam String factory, @RequestParam String modelName,
            @RequestParam String groupName, @RequestParam String stationName) {

        if ("B04".equalsIgnoreCase(factory)) {
            if ("P02X001.00".equalsIgnoreCase(modelName)) {
                modelName = "P02X001T00";
            }
            return b04LogRepository.getCalibrationHistory(modelName, groupName, stationName, 10);
        } else {
            return Arrays.asList(Timestamp.valueOf("2018-12-28 07:30:00"), Timestamp.valueOf("2018-12-27 07:35:00"), Timestamp.valueOf("2018-12-26 07:33:00"));
        }
    }

    @RequestMapping("/maintain")
    public List<String> getMaintainHistory() {
        return Arrays.asList("2018-12-24 07:30:00", "2018-12-17 07:35:00", "2018-12-10 07:33:00");
    }

    @RequestMapping("/fixed")
    public List<TestFixedHistory> getFixedHistory(
            @RequestParam String factory,
            @RequestParam String modelName,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String stationName,
            @RequestParam(required = false) String timeSpan,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

//        return testLockService.getLockedHistory(factory, modelName, groupName, stationName, 10);
        TimeSpan convertedTimeSpan = TimeSpan.from(timeSpan);
        if (convertedTimeSpan == null) {
            convertedTimeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        }

        List<TestFixedHistory> result;
        if (StringUtils.isEmpty(groupName)) {
            result = testFixedHistoryRepository.findByFactoryAndModelNameAndCreatedAtBetween(factory, modelName, convertedTimeSpan.getStartDate(), convertedTimeSpan.getEndDate(), PageRequest.of(pageNumber, pageSize));
        } else if (StringUtils.isEmpty(stationName)) {
            result = testFixedHistoryRepository.findByFactoryAndModelNameAndGroupNameAndCreatedAtBetween(factory, modelName, groupName, convertedTimeSpan.getStartDate(), convertedTimeSpan.getEndDate(), PageRequest.of(pageNumber, pageSize));
        } else {
            result = testFixedHistoryRepository.findByFactoryAndModelNameAndGroupNameAndStationNameAndCreatedAtBetween(factory, modelName, groupName, stationName, convertedTimeSpan.getStartDate(), convertedTimeSpan.getEndDate(), PageRequest.of(pageNumber, pageSize));
        }

        for (TestFixedHistory history : result) {
            Optional<TestSolutionMeta> optionalSolutionMeta = testSolutionMetaRepository.findById(history.getSolutionId());
            if (!optionalSolutionMeta.isPresent()) {
                log.error("### getFixedHistory solution meta {} null", history.getSolutionId());
                continue;
            }
            history.setWhy(optionalSolutionMeta.get().getSolution());
            history.setAction(optionalSolutionMeta.get().getAction());
        }
        return result;
    }
}
