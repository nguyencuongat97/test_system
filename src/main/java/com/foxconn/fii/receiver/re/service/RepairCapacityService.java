package com.foxconn.fii.receiver.re.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface RepairCapacityService {

    Object getTotalSiSmtRcviCapacity(String factory, String timeSpan) throws ParseException;
    Map<String, List<Object>> getTotalSiSmtRcviCapacityByShift(String factory, String timeSpan) throws ParseException;
    File listUserExport(String fileName) throws IOException;
    Map<String, Object> getDataUserByMonth(String factory) throws ParseException;
    void  leanrTestStream();
    void sendMail(String user, String title, String content) throws JsonProcessingException;
}
