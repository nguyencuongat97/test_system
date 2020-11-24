package com.foxconn.fii.receiver.smt.service;

import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import com.foxconn.fii.data.primary.model.entity.SmtModelOnline;

import java.util.Date;
import java.util.List;

public interface SmtModelOnlineService {

    void saveAll(List<SmtModelOnline> modelOnlineList);

    void saveAllMeta(List<SmtModelMeta> modelMetaList);

    SmtModelOnline findModelOnline(String factory, String sectionName, String lineName, Date endDate);
}
