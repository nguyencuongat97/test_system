package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.primary.model.entity.TestError;
import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import com.foxconn.fii.data.primary.repository.TestSolutionMetaRepository;
import com.foxconn.fii.receiver.test.service.TestErrorService;
import com.foxconn.fii.receiver.test.service.TestSolutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TestSolutionServiceImpl implements TestSolutionService {

    @Autowired
    private TestErrorService testErrorService;

    @Autowired
    private TestSolutionMetaRepository testSolutionMetaRepository;

    @Override
    public String generateSuggestion(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        List<TestError> errorByCodeList = testErrorService.getErrorByCodeList(factory, modelName, groupName, stationName, startDate, endDate);
        String suggestion = "";
        if (!errorByCodeList.isEmpty()) {
            List<TestSolutionMeta> solutionMetaList = getSolutionList(factory, modelName, errorByCodeList.get(0).getErrorCode());
            solutionMetaList.sort(Comparator.comparing(TestSolutionMeta::getOfficial, Comparator.reverseOrder())
                    .thenComparing(TestSolutionMeta::getNumberSuccess, Comparator.reverseOrder()));
            suggestion = errorByCodeList.get(0).getErrorCode() + " - " + (solutionMetaList.isEmpty() ? "No solution yet, we appreciate your contribution!" : String.join(" -> ", solutionMetaList.get(0).getAction()));
        }
        return suggestion;
    }

    @Override
    public String generateSuggestion(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, String errorCode) {
        List<TestSolutionMeta> solutionMetaList = getSolutionList(factory, modelName, errorCode);
        solutionMetaList.sort(Comparator.comparing(TestSolutionMeta::getOfficial, Comparator.reverseOrder())
                .thenComparing(TestSolutionMeta::getNumberSuccess, Comparator.reverseOrder()));
        return errorCode + " - " + (solutionMetaList.isEmpty() ? "No solution yet, we appreciate your contribution!" : String.join(" -> ", solutionMetaList.get(0).getAction()));
    }

    @Override
    public List<TestSolutionMeta> getSolutionList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate) {
        List<TestSolutionMeta> result =  new ArrayList<>();

        Map<String, String> errorMetaMap = testErrorService.getErrorMetaMap(factory, modelName);

//        if (notify != null && notify.getType() == TestNotify.Type.LOCKED_A) {
//            TestTracking tracking = testTrackingService.getTrackingByNotifyId(notify.getId());
//            List<TestSolutionMeta> solutionList = getSolutionList(factory, modelName, tracking.getErrorCode());
//            if (solutionList.isEmpty()) {
//                TestSolutionMeta solution = new TestSolutionMeta();
//                solution.setErrorCode(tracking.getErrorCode());
//                solution.setErrorDescription(errorMetaMap.getOrDefault(solution.getErrorCode(), ""));
//                result.add(solution);
//            } else {
//                solutionList.sort(Comparator.comparing(TestSolutionMeta::getOfficial, Comparator.reverseOrder())
//                        .thenComparing(TestSolutionMeta::getNumberSuccess, Comparator.reverseOrder()));
//                solutionList.get(0).setErrorDescription(errorMetaMap.getOrDefault(solutionList.get(0).getErrorCode(), ""));
//                result.add(solutionList.get(0));
//            }
//            return result;
//        }

        List<TestError> errorByCodeList = testErrorService.getErrorByCodeList(factory, modelName, groupName, stationName, startDate, endDate);
        if (errorByCodeList.isEmpty()) {
            return result;
        }

        for (int i = 0; i < errorByCodeList.size() && i < 3; i++) {
            List<TestSolutionMeta> solutionList = getSolutionList(factory, modelName, errorByCodeList.get(i).getErrorCode());
            if (solutionList.isEmpty()) {
                TestSolutionMeta solution = new TestSolutionMeta();
                solution.setErrorCode(errorByCodeList.get(i).getErrorCode());
                solution.setErrorDescription(errorMetaMap.getOrDefault(solution.getErrorCode(), ""));
                result.add(solution);
            } else {
                solutionList.sort(Comparator.comparing(TestSolutionMeta::getOfficial, Comparator.reverseOrder())
                        .thenComparing(TestSolutionMeta::getNumberSuccess, Comparator.reverseOrder()));
                solutionList.get(0).setErrorDescription(errorMetaMap.getOrDefault(solutionList.get(0).getErrorCode(), ""));
                result.add(solutionList.get(0));
            }
        }
        return result;
    }

    @Override
    public List<TestSolutionMeta> getSolutionList(String factory, String modelName, String errorCode) {

//        if ("B04".equalsIgnoreCase(factory)) {
//            return testSolutionMetaRepository.findAllByFactoryAndModelNameAndErrorCode(factory, modelName, errorCode);
//        } else if ("B06".equalsIgnoreCase(factory)) {
//            List<B06TestSolutionMeta> b06SolutionList = b06TestSolutionMetaRepository.findAllByModelNameAndErrorCode(modelName, errorCode);
//            if (b06SolutionList.isEmpty() && modelName.toUpperCase().startsWith("U10")) {
//                b06SolutionList = b06TestSolutionMetaRepository.findAllByModelNameAndErrorCode("UBEE", errorCode);
//            }
//            return b06SolutionList.stream().map(TestSolutionMeta::of).collect(Collectors.toList());
//        }
//        return Collections.emptyList();

        List<TestSolutionMeta> solutionMetaList = testSolutionMetaRepository.findAllByFactoryAndModelNameAndErrorCode(factory, modelName, errorCode);

        if ("B06".equalsIgnoreCase(factory) && modelName.lastIndexOf('.') == modelName.length() - 3) {
            modelName = modelName.substring(0, modelName.lastIndexOf('.')) + ".XX";
            solutionMetaList.addAll(testSolutionMetaRepository.findAllByFactoryAndModelNameAndErrorCode(factory, modelName, errorCode));
        }

        return solutionMetaList;
    }

    @Override
    public List<String> getErrorCodeList(String factory, String modelName) {
        List<String> solutionMetaList = testSolutionMetaRepository.getErrorCodeList(factory, modelName);

        if ("B06".equalsIgnoreCase(factory) && modelName.lastIndexOf('.') == modelName.length() - 3) {
            modelName = modelName.substring(0, modelName.lastIndexOf('.')) + ".XX";
            solutionMetaList.addAll(testSolutionMetaRepository.getErrorCodeList(factory, modelName));
        }

        return solutionMetaList;
    }

    @Override
    public TestSolutionMeta getSolution(Integer id) {
        return testSolutionMetaRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean markOfficial(Integer id) {
        Optional<TestSolutionMeta> metaOptional = testSolutionMetaRepository.findById(id);
        if (!metaOptional.isPresent()) {
            return false;
        }

        metaOptional.get().setOfficial(true);
        testSolutionMetaRepository.save(metaOptional.get());

        return true;
    }
}
