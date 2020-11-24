package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;

import java.util.Date;
import java.util.List;

public interface TestSolutionService {

    String generateSuggestion(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    String generateSuggestion(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate, String errorCode);

    @Deprecated
    List<TestSolutionMeta> getSolutionList(String factory, String modelName, String groupName, String stationName, Date startDate, Date endDate);

    List<TestSolutionMeta> getSolutionList(String factory, String modelName, String errorCode);

    List<String> getErrorCodeList(String factory, String modelName);

    TestSolutionMeta getSolution(Integer id);

    Boolean markOfficial(Integer id);
}
