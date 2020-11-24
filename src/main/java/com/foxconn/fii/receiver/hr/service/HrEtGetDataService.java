package com.foxconn.fii.receiver.hr.service;

import com.foxconn.fii.data.primary.model.entity.HrEtBackupDuty;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface HrEtGetDataService {

    void getDailyData(int dayOfMonth, int month) throws ParserConfigurationException, SAXException, ParseException, IOException;

    void getData(Date startDate, Date endDate) throws ParseException, ParserConfigurationException, IOException, SAXException;

    void employeeCounting(java.sql.Date startDate, java.sql.Date endDate);

    void updateEmployeePersonInfo() throws ParserConfigurationException, IOException, SAXException;

    void backupDutyData() throws IOException, SAXException, ParserConfigurationException, ParseException;

    void backupOvertimeData() throws ParseException;

}
