package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.TestTracking;

public interface NotifyService {

    void notifyToIcivet(TestTracking... trackingList);

    void notifyToIcivet(String message, TestTracking tracking);

    void notifyToMail(MailMessage message, String from, String to);
}
