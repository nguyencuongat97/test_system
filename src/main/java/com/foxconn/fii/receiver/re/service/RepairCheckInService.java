package com.foxconn.fii.receiver.re.service;

import java.util.Date;
import java.util.List;

public interface RepairCheckInService {

    List<Object[]> countByModelNameAndSection(String factory, Date startDate, Date endDate);
}
