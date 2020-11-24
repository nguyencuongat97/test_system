package com.foxconn.fii.receiver.hr.service;

import com.foxconn.fii.receiver.hr.service.impl.HrEtEmployeeInfoServiceImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface HrEtEmployeeInfoService {

    HrEtEmployeeInfoServiceImpl.EmployeeInfo employeeInfo(String cardId) throws ParserConfigurationException, IOException, SAXException;

    Object countWorkingStatusByDay(String startDateStr, String endDateStr, String departName, Set<String> departList) throws ParseException;

    Object listEmployee(String dateStr, String shift, String departName, Set<String> departList, String status) throws ParseException;

//    Object listMissing(String dateStr, String departName, Set<String> departList) throws ParseException;
//
//    Object listAlert(String dateStr, String departName, Set<String> departList) throws ParseException;
}
