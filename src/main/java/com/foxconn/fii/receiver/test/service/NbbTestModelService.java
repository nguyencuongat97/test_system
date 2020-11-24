package com.foxconn.fii.receiver.test.service;

import java.util.List;

public interface NbbTestModelService {

    List<String> getModelListByCustomerAndStage(String customer, String stage);
}
