package com.foxconn.fii.receiver.re.service;

import com.foxconn.fii.common.TimeSpan;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Repair8sService {

    Object re8sIntroduceGetdata();

    Object re8sChecklistByDailyGetdata(String owner, TimeSpan timeSpan);

    Object re8sChecklistByDailySavedata(String jsonString) throws IOException;

    Object re8sLeaderconfirmByDailyGetdata(String leaderId, String owner, TimeSpan timeSpan);

    Object re8sLeaderconfirmByDailySavedata(String jsonString) throws IOException;

    Object re8sChecklistStatus();

    Object re8sChecklistReport(String startDateStr, String endDateStr);

    Object re8sChecklistDailyReport();

    Object re8sUserGetconfig(String userId);

    Object re8sUserSaveconfig(String jsonString) throws IOException;

    Object re8sUserSaveUpdateconfig(String jsonString) throws  IOException;

    List<Map<String, Object>> re8sUserGetListDataDetail();
}
