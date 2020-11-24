package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.model.entity.TestPlanTmp;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TestPlanService {

    void saveAll(List<TestPlanMeta> planMetaList);

//    Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, ShiftType shiftType);

//    Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, Date endDate, boolean isHourly);

    Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, Date endDate, TestPlanMeta.Type type);

//    Integer getPlanQty(String factory, String modelName, Date startDate, ShiftType shiftType, boolean isHourly);

    Integer getPlanQty(String factory, String modelName, Date startDate, Date endDate, TestPlanMeta.Type type);

    List<TestPlanMeta> getPlanList(String factory, String sectionName, String modelName, Date startDate, Date endDate, TestPlanMeta.Type type);

    void savePlan(TestPlanMeta planMeta);

    void syncPlanQtyFromB04();

    Optional<TestPlanMeta> findById(Integer id);

    List<TestPlanMeta> findByFactoryAndMo(String factory, String mo);

    List<TestPlanMeta> findByFactoryAndMo(String factory, String mo, Date startDate, Date endDate);

    List<TestPlanMeta> findByFactory(String factory, Date startDate, Date endDate);

    void deleteById(int id);

    void deleteAll(List<TestPlanMeta> planMetaList);


    List<TestPlanTmp> getTmpPlanList(String factory, String sectionName, String modelName, Date startDate, Date endDate, TestPlanTmp.Type type);

    Optional<TestPlanTmp> findTmpPlanById(Integer id);

    void savePlanTmp(TestPlanTmp tmpPlan);

    void deleteTmpPlanById(int id);
}
