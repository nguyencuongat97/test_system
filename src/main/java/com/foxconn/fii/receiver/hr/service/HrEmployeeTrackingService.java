package com.foxconn.fii.receiver.hr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface HrEmployeeTrackingService {

    Object getDataHistoryByEmpNo(String empNo, String startDate, String endDate) throws ParserConfigurationException, IOException, SAXException, ParseException;

//    Object getForeignEmployeeClockRecord(List<String> empNoList, String timeSpanStr) throws IOException, ParserConfigurationException, SAXException, ParseException;

    Object getForeignEmployeeDuty(List<String> empNoList, String timeSpanStr) throws ParseException, IOException, ParserConfigurationException, SAXException;

}
