package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.receiver.test.service.TestMobileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TestMobileServerImpl implements TestMobileService {
    private static final String PROC_GET_NOTIFY_ALL = "get_notify_all_factory";
    private static final String PROC_GET_NOTIFY_MODEL = "get_notify_for_model_in_factory";
    private static final String PROC_GET_LIST_MODEL_BY_FACTORY_WITH_SHIFT = "get_list_model_by_factory_with_shift";
    private static final String PROC_GET_CAPACITY_AND_TOTAL_MODEL_FOR_FACTORY = "get_capacity_and_total_model_for_factory";

    @Autowired
    @Qualifier(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object getListNotifyAllFactory(TimeSpan timeSpan) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(PROC_GET_NOTIFY_ALL);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("time_start", timeSpan.getStartDate())
                .addValue("time_end", timeSpan.getEndDate());
        Map<String, Object> objs = jdbcCall.execute(in);
        return objs.get("#result-set-1");
    }

    @Override
    public Object getNotifyModelFactory(String factory, String model, TimeSpan timeSpan) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(PROC_GET_NOTIFY_MODEL);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("factory", factory)
                .addValue("model", model)
                .addValue("time_start", timeSpan.getStartDate())
                .addValue("time_end", timeSpan.getEndDate());
        Map<String, Object> objs = jdbcCall.execute(in);
        return objs.get("#result-set-1");
    }

    @Override
    public Object getListModelByFactoryWithShift(String factory, TimeSpan timeSpan) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(PROC_GET_LIST_MODEL_BY_FACTORY_WITH_SHIFT);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("factory", factory)
                .addValue("time_start", timeSpan.getStartDate())
                .addValue("time_end", timeSpan.getEndDate());
        Map<String, Object> objs = jdbcCall.execute(in);
        return objs.get("#result-set-1");
    }

    @Override
    public Object getCapacityAndTotalModelFactory(TimeSpan timeSpan) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(PROC_GET_CAPACITY_AND_TOTAL_MODEL_FOR_FACTORY);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("time_start", timeSpan.getStartDate())
                .addValue("time_end", timeSpan.getEndDate());
        Map<String, Object> objs = jdbcCall.execute(in);
        return objs.get("#result-set-1");
    }
}
