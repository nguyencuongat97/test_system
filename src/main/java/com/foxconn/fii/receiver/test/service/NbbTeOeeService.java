package com.foxconn.fii.receiver.test.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public interface NbbTeOeeService {

    Object getOverviewData(String customer, String stage, String group, String line, String station, String startDate, String endDate) throws ParseException;

//    Object countData(String factory, String customer);

//    Object stationStatus(String customer, String stage, String group);

    Object stationStatusV2(String customer, String stage, String group);

    // Tung
    void exportDataKeyPart(HttpServletResponse resonse, String keyPart, String keyPartNo, Date startDate, Date endDate)throws ParseException, IOException;
    // END Tung

}
