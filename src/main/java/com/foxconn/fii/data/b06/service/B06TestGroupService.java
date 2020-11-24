package com.foxconn.fii.data.b06.service;

import com.foxconn.fii.common.ShiftType;

import java.util.List;
import java.util.Map;

public interface B06TestGroupService {

    Map<String, Map<String, Integer>> getUPH(String modelName, String workDate, ShiftType shiftType);

    Object getAllUPH(List<String> modelList, String workDate, ShiftType shiftType);
}
