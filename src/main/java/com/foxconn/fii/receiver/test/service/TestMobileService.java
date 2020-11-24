package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.common.TimeSpan;

public interface TestMobileService {

    Object getListNotifyAllFactory(TimeSpan timeSpan);

    Object getNotifyModelFactory(String factory, String model, TimeSpan timeSpan);

    Object getListModelByFactoryWithShift(String factory, TimeSpan timeSpan);

    Object getCapacityAndTotalModelFactory(TimeSpan timeSpan);
}
