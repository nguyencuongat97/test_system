package com.foxconn.fii.receiver.hr.service;

import java.util.Date;

public interface HrEmployeeMealTypeVnService {
    Object getDataMealBNAndBN3SendMail(Date startDate, Date endDate) throws Exception;

    void HrSyncDataCanteenFromAPI(Date startDate) throws Exception;
}
