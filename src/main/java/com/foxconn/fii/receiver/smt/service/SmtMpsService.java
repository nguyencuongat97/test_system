package com.foxconn.fii.receiver.smt.service;


import com.foxconn.fii.data.primary.model.entity.SmtMps;

import java.util.Date;
import java.util.List;

public interface SmtMpsService {

    void saveAll(List<SmtMps> mpsList);

    List<SmtMps> getSmtMpsList(String factory, Date startDate, Date endDate);

    List<SmtMps> getSmtMpsList(String factory, String cft, Date startDate, Date endDate);
}
