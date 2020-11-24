package com.foxconn.fii.receiver.re.service;

import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.SortableMapResponse;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RepairOnlineWipService {

    void saveDataCheckInToDataRaw() throws ParserConfigurationException, SAXException, ParseException, IOException;
    Map<String, Object> getDataInputOutput(String startD, String endD) throws ParseException;
    Map<String,Map<String, Integer>> getDataRepairWeeklyStatus() throws ParseException;

    void getDataDetailModelName(HttpServletResponse resonse, String modelName) throws IOException;

    Object getWipInOutStatus(String factory, String timeSpan) throws ParseException;

    Object getDataRemainDaily(String factory, String timeSpan) throws ParseException;

    Object getDataErrorCodeByModelName(String factory, String modelName) throws ParseException;

    Map<String, SortableMapResponse<Integer>> getDataModelNameAndTime(String factory, String timeSpan) throws ParseException;

    Map<String, Map<String, Object>> getDataModelNameAndHoureTime(String factory, String timeSpan) throws ParseException;

    Object bonpileByModelService(String factory, String timeSpan) throws Exception;

    Object bonpileByModelByErrorCodeService(String factory, String action,String modelName, String timeSpan) throws Exception;

    Map<String, Object> getDataWipInOutTrendChart(String factory, String parameter)throws ParseException;

    CommonResponse<Object> getNTFAndProcessAndCompoment(String factory, String timeSpan)throws ParseException;

    Map<String, Long> getModelNameByDefected(String factory, String timeSpan, String defected)throws ParseException;

    CommonResponse<Object> getModelNameByDefectedByProcess(String factory, String timeSpan, String defected, String modelName)throws ParseException;

    CommonResponse<Object> getErrorCodeByReasonCodeByProcess(String factory, String timeSpan, String modelName,  String reasonCode)throws ParseException;

    Map<String, Object> getModelNameSMTAndPTHAndSI(String factory, String timeSpan)throws ParseException;

    Map<String, Integer> getErrorCodeByModelName(String factory, String timeSpan, String modelName)throws ParseException;

    Map<String, Integer> getInputOutputByModelName(String factory, String timeSpan)throws ParseException;

    Object getErrorReasonLocationByModelName(String factory, String timeSpan, String modelName, String def)throws ParseException;

    void syncDataBC8MCheckIn(Date start, Date end) throws ParserConfigurationException, SAXException, ParseException, IOException;

    void syncDataBC8MCheckOut(Date start, Date end) throws ParserConfigurationException, SAXException, ParseException, IOException;

    Object getDataBC8MRemain(String factory, String timeSpan)throws ParseException;

    Object getTop15ErrorCode(String factory, String modelName)throws ParseException;

    Object getInOutTrendChart(String factory, String timeSpan)throws ParseException, IOException;

    void exportDataCheckOutByUserCapacity(HttpServletResponse resonse, String factory, String timeSpan)throws ParseException, IOException;

    void exportDataBonepile(HttpServletResponse resonse, String factory, String timeSpan) throws Exception;

    Object getCountInputByTime(String factory)throws ParseException, IOException;

    Object getDataRMAInOutRemain(String factory, String timeSpan)throws ParseException, IOException;

    Object getDataRMAErrorCodeByModelName(String factory, String modelName)throws ParseException, IOException;

    Object getDataRMAInoutTrendChart(String factory, String timeSpan)throws ParseException, IOException;

    void downloadExcelFileDataRemainBC8M(HttpServletResponse response, String factory)throws IOException;

    void downloadExcelFileDataRemainRMA(HttpServletResponse response, String factory)throws IOException;

    Object getListModelB04(String factory);

    void importOnlineWipRemain(HttpServletResponse resonse, String modelName) throws Exception;

    Object getErrorCodeByModelNameC03(String factory, String modelName,String timeSpan) throws  Exception;

}
