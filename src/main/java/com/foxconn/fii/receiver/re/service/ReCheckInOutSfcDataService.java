package com.foxconn.fii.receiver.re.service;

import com.foxconn.fii.common.response.CommonResponse;

import java.util.Date;
import java.util.Map;

public interface ReCheckInOutSfcDataService {
    Map<String, Object> getTotalInputOutputservice(String factory, String timeSpan);

    Object getInOutByWeekly(String factory);

    Object getQtyCheckoutByDay(String factory, String timeSpan);

    Map<String, Object> getInOutBySection(String factory, String section);

    Boolean checkBalance(String factory);

    Object getBalanceAndOverTime8h(String factory);

    Object getBonePileTotal(String factory);

    CommonResponse<Object> getErrorCodeByNtf(String factory, String timeSpan)throws Exception;

}
