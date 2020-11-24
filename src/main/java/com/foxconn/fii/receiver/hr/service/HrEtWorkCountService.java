package com.foxconn.fii.receiver.hr.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HrEtWorkCountService {

    File excelMonthlyReport(String fileName, Map<String, Object> workData) throws IOException;

    Map<String, Object> workCount(String cardId, String departName, int year, int month, int startDay, int endDay) throws IOException, ParserConfigurationException, SAXException, ParseException;

    List<Map<String, String>> dutyData(String cardId, String departName, int year, int month, int startDay, int endDay) throws ParserConfigurationException, IOException, SAXException;

    List<Map<String, String>> overtimeData(String cardId, String departName, int year, int month, int startDay, int endDay) throws IOException, ParserConfigurationException, SAXException;

    Double usedFreeDuty(String empNo, Calendar calendar) throws ParseException;

}
