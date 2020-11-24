package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.b04.model.B04SmtPlanMeta;
import com.foxconn.fii.data.b04.model.B04TestPlanMeta;
import com.foxconn.fii.data.b04.repository.B04SmtPlanMetaRepository;
import com.foxconn.fii.data.b04.repository.B04TestPlanMetaRepository;
import com.foxconn.fii.data.primary.model.entity.TestPlanMeta;
import com.foxconn.fii.data.primary.model.entity.TestPlanTmp;
import com.foxconn.fii.data.primary.repository.TestPlanMetaRepository;
import com.foxconn.fii.data.primary.repository.TestPlanTmpRepository;
import com.foxconn.fii.receiver.test.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.foxconn.fii.data.b06ds02.model.B06TestPlanMeta;
//import com.foxconn.fii.data.b06ds02.repository.B06TestPlanMetaRepository;

@Service
public class TestPlanServiceImpl implements TestPlanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestPlanMetaRepository testPlanMetaRepository;

    @Autowired
    private TestPlanTmpRepository testPlanTmpRepository;

    @Autowired
    private B04TestPlanMetaRepository b04TestPlanMetaRepository;

    @Autowired
    private B04SmtPlanMetaRepository b04SmtPlanMetaRepository;

//    @Autowired
//    private B06TestPlanMetaRepository b06TestPlanMetaRepository;

    public void saveAll(List<TestPlanMeta> planMetaList) {
        if (planMetaList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into test_plan_meta as target " +
                        "using(values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)) " +
                        "as source (factory, section_name, model_name, mo, line_name, shift, shift_time, plan_qty, start_date, type, work_time, total, demand, note, remark) " +
                        "   on target.factory=source.factory and target.section_name=source.section_name and target.model_name=source.model_name and target.line_name=source.line_name and target.mo=source.mo and target.shift_time=source.shift_time and target.shift=source.shift " +
                        "when matched then " +
                        "   update set " +
                        "   target.plan_qty=source.plan_qty, " +
                        "   target.start_date=source.start_date, " +
                        "   target.type=source.type, " +
                        "   target.work_time=source.work_time, " +
                        "   target.total=source.total, " +
                        "   target.demand=source.demand, " +
                        "   target.note=source.note, " +
                        "   target.remark=source.remark " +
                        "when not matched then " +
                        "   insert (factory, section_name, model_name, mo, line_name, shift, shift_time, plan_qty, start_date, type, work_time, total, demand, note, remark) " +
                        "   values(source.factory, source.section_name, source.model_name, source.mo, source.line_name, source.shift, source.shift_time, source.plan_qty, source.start_date, source.type, source.work_time, source.total, source.demand, source.note, source.remark);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestPlanMeta planMeta = planMetaList.get(i);
                        preparedStatement.setString(1, planMeta.getFactory());
                        preparedStatement.setString(2, planMeta.getSectionName());
                        preparedStatement.setString(3, planMeta.getModelName());
                        preparedStatement.setString(4, planMeta.getMo());
                        preparedStatement.setString(5, planMeta.getLineName());
                        preparedStatement.setString(6, planMeta.getShift().name());
                        preparedStatement.setTimestamp(7, new Timestamp(planMeta.getShiftTime().getTime()));
                        preparedStatement.setInt(8, planMeta.getPlan());
                        preparedStatement.setTimestamp(9, new Timestamp(planMeta.getStartDate().getTime()));
                        preparedStatement.setInt(10, planMeta.getType().ordinal());
                        preparedStatement.setFloat(11, planMeta.getWorkTime());
                        preparedStatement.setInt(12, planMeta.getTotal());
                        preparedStatement.setInt(13, planMeta.getDemand());
                        preparedStatement.setString(14, planMeta.getNote());
                        preparedStatement.setInt(15, planMeta.getRemark().ordinal());
                    }

                    @Override
                    public int getBatchSize() {
                        return planMetaList.size();
                    }
                });
    }

//    @Override
//    public Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, ShiftType shiftType) {
//        if ("B04".equalsIgnoreCase(factory)) {
//            List<B04TestPlanMeta> testPlanMetaList = b04TestPlanMetaRepository.findByShiftAndShiftTime(shiftType, new java.sql.Date(startDate.getTime()));
//            return testPlanMetaList.stream().collect(Collectors.toMap(
//                    B04TestPlanMeta::getModelName, plan -> plan.getPlan() / 12,
//                    (u, v) -> u + v,
//                    HashMap::new));
//        } else if ("B06".equalsIgnoreCase(factory)) {
//            List<B06TestPlanMeta> testPlanMetaList = b06TestPlanMetaRepository.findByWorkDate(B06TestPlanMeta.WORK_DATE_FORMAT.format(startDate));
//            return testPlanMetaList.stream().collect(Collectors.toMap(
//                    B06TestPlanMeta::getModelName, B06TestPlanMeta::getUph,
//                    (u, v) -> u + v,
//                    HashMap::new));
//        } else {
//            List<TestPlanMeta> testPlanMetaList = testPlanMetaRepository.findByFactoryAndShiftAndShiftTime(factory, shiftType, new java.sql.Date(startDate.getTime()));
//            return testPlanMetaList.stream().collect(Collectors.toMap(TestPlanMeta::getModelName, plan -> plan.getPlan() / 12));
//        }
//    }

//    @Override
//    public Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, Date endDate, boolean isHourly) {
//        List<TestPlanMeta> testPlanMetaList = testPlanMetaRepository.findByFactoryAndStartDateBetween(factory, startDate, endDate);
//        int diff = isHourly ? (int)((endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000)) : 1;
//        return testPlanMetaList.stream().collect(Collectors.toMap(
//                TestPlanMeta::getModelName,
//                plan -> plan.getPlan() / diff,
//                (u, v) -> u + v,
//                HashMap::new));
//    }


    @Override
    public Optional<TestPlanMeta> findById(Integer id) {
        return testPlanMetaRepository.findById(id);
    }

    @Override
    public List<TestPlanMeta> findByFactory(String factory, Date startDate, Date endDate) {
        return testPlanMetaRepository.findByFactoryAndTypeAndStartDateBetween(factory, TestPlanMeta.Type.DAILY, startDate, endDate);
    }

    @Override
    public List<TestPlanMeta> findByFactoryAndMo(String factory, String mo) {
        return testPlanMetaRepository.findByFactoryAndMo(factory, mo);
    }

    @Override
    public List<TestPlanMeta> findByFactoryAndMo(String factory, String mo, Date startDate, Date endDate) {
        return testPlanMetaRepository.findByFactoryAndMoAndStartDateBetweenOrderByStartDateDesc(factory, mo, startDate, endDate);
    }

    @Override
    public void deleteById(int id) {
        testPlanMetaRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<TestPlanMeta> planMetaList) {
        testPlanMetaRepository.deleteAll(planMetaList);
    }

    @Override
    public Map<String, Integer> getPlanQtyByModelMap(String factory, Date startDate, Date endDate, TestPlanMeta.Type type) {
        TestPlanMeta.Type diffType = (type == TestPlanMeta.Type.MONTHLY) ? TestPlanMeta.Type.MONTHLY : TestPlanMeta.Type.DAILY;
        List<TestPlanMeta> testPlanMetaList = testPlanMetaRepository.findByFactoryAndSectionNameAndTypeAndStartDateBetween(factory, "SI", diffType, startDate, endDate);
        int diff = (type == TestPlanMeta.Type.HOURLY) ? (int)((endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000)) : 1;
        return testPlanMetaList.stream().collect(Collectors.toMap(
                TestPlanMeta::getModelName,
                plan -> plan.getPlan() / diff,
                (u, v) -> u + v,
                HashMap::new));
    }

//    @Override
//    public Integer getPlanQty(String factory, String modelName, Date startDate, ShiftType shiftType, boolean isHourly) {
//        if ("B04".equalsIgnoreCase(factory)) {
//            List<B04TestPlanMeta> testPlanMeta = b04TestPlanMetaRepository.findByModelNameAndShiftAndShiftTime(modelName, shiftType, new java.sql.Date(startDate.getTime()));
//            return testPlanMeta.isEmpty() ? 0 : testPlanMeta.stream().mapToInt(B04TestPlanMeta::getPlan).sum() / (isHourly ? 12 : 1);
//        } else if ("B06".equalsIgnoreCase(factory)) {
//            List<B06TestPlanMeta> testPlanMetaList = b06TestPlanMetaRepository.findByModelNameAndWorkDate(modelName, B06TestPlanMeta.WORK_DATE_FORMAT.format(startDate));
//            return testPlanMetaList.isEmpty() ? 0 : testPlanMetaList.stream().mapToInt(B06TestPlanMeta::getUph).sum() * (isHourly ? 1 : 12);
//        } else {
//            TestPlanMeta testPlanMeta = testPlanMetaRepository.findByFactoryAndModelNameAndShiftAndShiftTime(factory, modelName, shiftType, new java.sql.Date(startDate.getTime()));
//            return testPlanMeta != null ? testPlanMeta.getPlan() / (isHourly ? 12 : 1) : 0;
//        }
//    }

    @Override
    public Integer getPlanQty(String factory, String modelName, Date startDate, Date endDate, TestPlanMeta.Type type) {
        TestPlanMeta.Type diffType = type == TestPlanMeta.Type.MONTHLY ? TestPlanMeta.Type.MONTHLY : TestPlanMeta.Type.DAILY;
        List<TestPlanMeta> testPlanMeta = testPlanMetaRepository.findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(factory, "SI", modelName, diffType, startDate, endDate);
        return testPlanMeta.isEmpty() ? 0 : testPlanMeta.stream().mapToInt(TestPlanMeta::getPlan).sum() / (type == TestPlanMeta.Type.HOURLY ? 12 : 1);
    }

    @Override
    public List<TestPlanMeta> getPlanList(String factory, String sectionName, String modelName, Date startDate, Date endDate, TestPlanMeta.Type type) {
        List<TestPlanMeta> result;
        if (StringUtils.isEmpty(sectionName)) {
            result = testPlanMetaRepository.findByFactoryAndTypeAndStartDateBetween(factory, type, startDate, endDate);
        } else if (StringUtils.isEmpty(modelName)) {
            result = testPlanMetaRepository.findByFactoryAndSectionNameAndTypeAndStartDateBetween(factory, sectionName, type, startDate, endDate);
        } else {
            result = testPlanMetaRepository.findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(factory, sectionName, modelName, type, startDate, endDate);
            if (type == TestPlanMeta.Type.MONTHLY && result.isEmpty()) {
                result = testPlanMetaRepository.findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(factory, sectionName, modelName, TestPlanMeta.Type.DAILY, startDate, endDate);
            }
        }
        return result;
    }

    @Override
    public void savePlan(TestPlanMeta planMeta) {
        TestPlanMeta plan;
        if (planMeta.getId() != 0) {
            plan = testPlanMetaRepository.findById(planMeta.getId());
            com.foxconn.fii.common.utils.BeanUtils.copyPropertiesIgnoreNull(planMeta, plan, "id");
        } else {
            plan = new TestPlanMeta();
            com.foxconn.fii.common.utils.BeanUtils.copyPropertiesIgnoreNull(planMeta, plan, "id");
        }
        if (StringUtils.isEmpty(plan.getSectionName())) {
            plan.setSectionName("SI");
        }
        if (planMeta.getType() == null) {
            plan.setType(TestPlanMeta.Type.DAILY);
        }
        if (planMeta.getShift() == null && planMeta.getShiftTime() == null) {
            if (planMeta.getType() == TestPlanMeta.Type.DAILY) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                plan.setShiftTime(new java.sql.Date(timeSpan.getStartDate().getTime()));
                plan.setShift(timeSpan.getShiftType());
                plan.setStartDate(timeSpan.getStartDate());
            } else  if (planMeta.getType() == TestPlanMeta.Type.MONTHLY) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timeSpan.getStartDate());
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                plan.setShiftTime(new java.sql.Date(calendar.getTimeInMillis()));
                plan.setShift(timeSpan.getShiftType());
                plan.setStartDate(calendar.getTime());
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(planMeta.getShiftTime());
            if (planMeta.getShift() == ShiftType.DAY) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 19);
            }
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            plan.setStartDate(calendar.getTime());
        }
        testPlanMetaRepository.save(plan);
    }

    @Override
    public void syncPlanQtyFromB04() {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.add(Calendar.MONTH, -3);
        timeSpan.setStartDate(calendar.getTime());

        List<TestPlanMeta> planMetaList = new ArrayList<>();
        List<B04TestPlanMeta> b04TestPlanMetaList = b04TestPlanMetaRepository.findByShiftTimeAfter(new java.sql.Date(timeSpan.getStartDate().getTime()));
        b04TestPlanMetaList.forEach(plan -> {
            TestPlanMeta planMeta = new TestPlanMeta();
            BeanUtils.copyPropertiesIgnoreNull(plan, planMeta, "id");
            planMeta.setFactory("B04");
            planMeta.setSectionName("SI");

            calendar.setTime(planMeta.getShiftTime());
            if (planMeta.getShift() == ShiftType.DAY) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 19);
            }
            calendar.set(Calendar.MINUTE, 29);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            planMeta.setStartDate(calendar.getTime());

            planMetaList.add(planMeta);
        });

        List<B04SmtPlanMeta> b04SmtPlanMetaList = b04SmtPlanMetaRepository.findByShiftTimeAfter(new java.sql.Date(timeSpan.getStartDate().getTime()));
        b04SmtPlanMetaList.forEach(plan -> {
            TestPlanMeta planMeta = new TestPlanMeta();
            BeanUtils.copyPropertiesIgnoreNull(plan, planMeta, "id");
            planMeta.setFactory("B04");
            planMeta.setSectionName("SMT");

            calendar.setTime(planMeta.getShiftTime());
            if (planMeta.getShift() == ShiftType.DAY) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 19);
            }
            calendar.set(Calendar.MINUTE, 29);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            planMeta.setStartDate(calendar.getTime());

            planMetaList.add(planMeta);
        });

        saveAll(planMetaList);
    }

    @Override
    public List<TestPlanTmp> getTmpPlanList(String factory, String sectionName, String modelName, Date startDate, Date endDate, TestPlanTmp.Type type) {
        List<TestPlanTmp> result;
        if (StringUtils.isEmpty(sectionName)) {
            result = testPlanTmpRepository.findByFactoryAndTypeAndStartDateBetween(factory, type, startDate, endDate);
        } else if (StringUtils.isEmpty(modelName)) {
            result = testPlanTmpRepository.findByFactoryAndSectionNameAndTypeAndStartDateBetween(factory, sectionName, type, startDate, endDate);
        } else {
            result = testPlanTmpRepository.findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(factory, sectionName, modelName, type, startDate, endDate);
            if (type == TestPlanTmp.Type.MONTHLY && result.isEmpty()) {
                result = testPlanTmpRepository.findByFactoryAndSectionNameAndModelNameAndTypeAndStartDateBetween(factory, sectionName, modelName, TestPlanTmp.Type.DAILY, startDate, endDate);
            }
        }
        return result;
    }

    @Override
    public Optional<TestPlanTmp> findTmpPlanById(Integer id) {
        return testPlanTmpRepository.findById(id);
    }

    @Override
    public void savePlanTmp(TestPlanTmp tmpPlan) {
        TestPlanTmp plan;
        if (tmpPlan.getId() != 0) {
            plan = testPlanTmpRepository.findById(tmpPlan.getId());
        } else {
            plan = new TestPlanTmp();
        }
        BeanUtils.copyPropertiesIgnoreNull(tmpPlan, plan, "id");
        if (StringUtils.isEmpty(plan.getSectionName())) {
            plan.setSectionName("SI");
        }
        if (tmpPlan.getType() == null) {
            plan.setType(TestPlanTmp.Type.DAILY);
        }
        if (tmpPlan.getShift() == null && tmpPlan.getShiftTime() == null) {
            if (tmpPlan.getType() == TestPlanTmp.Type.DAILY) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                plan.setShiftTime(new java.sql.Date(timeSpan.getStartDate().getTime()));
                plan.setShift(timeSpan.getShiftType());
                plan.setStartDate(timeSpan.getStartDate());
            } else  if (tmpPlan.getType() == TestPlanTmp.Type.MONTHLY) {
                TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timeSpan.getStartDate());
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                plan.setShiftTime(new java.sql.Date(calendar.getTimeInMillis()));
                plan.setShift(timeSpan.getShiftType());
                plan.setStartDate(calendar.getTime());
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tmpPlan.getShiftTime());
            if (tmpPlan.getShift() == ShiftType.DAY) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 19);
            }
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            plan.setStartDate(calendar.getTime());
        }
        testPlanTmpRepository.save(plan);
    }

    @Override
    public void deleteTmpPlanById(int id) {
        testPlanTmpRepository.deleteById(id);
    }
}
