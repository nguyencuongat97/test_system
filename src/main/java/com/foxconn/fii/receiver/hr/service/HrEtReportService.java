package com.foxconn.fii.receiver.hr.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public interface HrEtReportService {

    Object monthlySalartReport(String empNo, int month, double basicSalary, int relatedPerson) throws ParserConfigurationException, SAXException, ParseException, IOException;

}
