package com.foxconn.fii.receiver.re.service;

import java.util.Date;

public interface RepairSyncDataService {

    void syncIODaily(Date startDate, Date endDate);

    void B06ReSyncDataFromWebService(Date startDate, Date endDate) throws Exception;

    void syncDataOnlineWipIn(Date startDate, Date endDate) throws Exception;

    void syncDataRepairOnlineWipFromB04(Date startDate, Date endDate) throws Exception;


}
