package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.primary.model.entity.TestCpk;
import com.foxconn.fii.data.primary.model.entity.TestCpkSyncConfig;
import com.foxconn.fii.data.primary.model.entity.TestParameter;
import com.foxconn.fii.data.primary.repository.TestCpkRepository;
import com.foxconn.fii.receiver.test.service.TestCpkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TestCpkServiceImpl implements TestCpkService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestCpkRepository testCpkRepository;

    @Override
    public int[] saveAll(List<TestCpk> cpkList) {
        if (cpkList.isEmpty()) {
            return null;
        }

        return jdbcTemplate.batchUpdate(
                "merge into test_cpk as target " +
                        "using(select factory=?, model_name=?, group_name=?, station_name=?, parameter=?, start_date=?, end_date=?, cpk=?, variance=?, average=?, number_value=?, cpk_new=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.station_name=source.station_name and target.parameter=source.parameter and target.start_date=source.start_date and target.end_date=source.end_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.cpk=source.cpk, " +
                        "   target.variance=source.variance, " +
                        "   target.average=source.average, " +
                        "   target.number_value=source.number_value, " +
                        "   target.cpk_new=source.cpk_new, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, group_name, station_name, parameter, start_date, end_date, cpk, variance, average, number_value, cpk_new, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.group_name, source.station_name, source.parameter, source.start_date, source.end_date, source.cpk, source.variance, source.average, source.number_value, source.cpk_new, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestCpk cpk = cpkList.get(i);
                        preparedStatement.setString(1, cpk.getFactory());
                        preparedStatement.setString(2, cpk.getModelName());
                        preparedStatement.setString(3, cpk.getGroupName());
                        preparedStatement.setString(4, cpk.getStationName());
                        preparedStatement.setString(5, cpk.getParameter());
                        preparedStatement.setTimestamp(6, new Timestamp(cpk.getStartDate().getTime()));
                        preparedStatement.setTimestamp(7, new Timestamp(cpk.getEndDate().getTime()));
                        preparedStatement.setDouble(8, cpk.getCpk());
                        preparedStatement.setDouble(9, cpk.getVariance());
                        preparedStatement.setDouble(10, cpk.getAverage());
                        preparedStatement.setInt(11, cpk.getNumberOfValue());
                        preparedStatement.setDouble(12, cpk.getNewCpk());
                        preparedStatement.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return cpkList.size();
                    }
                });
    }

    @Override
    public void saveAllConfig(List<TestCpkSyncConfig> cpkSyncConfigList) {
        if (cpkSyncConfigList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into test_cpk_sync_config as target " +
                        "using(select factory=?, model_name=?, latest_sync_id=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.latest_sync_id=source.latest_sync_id, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, latest_sync_id, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.latest_sync_id, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestCpkSyncConfig cpkSyncConfig = cpkSyncConfigList.get(i);
                        preparedStatement.setString(1, cpkSyncConfig.getFactory());
                        preparedStatement.setString(2, cpkSyncConfig.getModelName());
                        preparedStatement.setLong(3, cpkSyncConfig.getLatestSyncId());
                        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return cpkSyncConfigList.size();
                    }
                });
    }

    @Override
    public void saveAllParameter(List<TestParameter> parameterList) {
        if (parameterList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into test_cpk_parameter as target " +
                        "using(select factory=?, model_name=?, group_name=?, parameter=?, l_limit=?, h_limit=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.group_name=source.group_name and target.parameter=source.parameter " +
                        "when matched then " +
                        "   update set " +
                        "   target.l_limit=source.l_limit, " +
                        "   target.h_limit=source.h_limit " +
                        "when not matched then " +
                        "   insert (factory, model_name, group_name, parameter, l_limit, h_limit) " +
                        "   values(source.factory, source.model_name, source.group_name, source.parameter, source.l_limit, source.h_limit);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestParameter parameter = parameterList.get(i);
                        preparedStatement.setString(1, parameter.getFactory());
                        preparedStatement.setString(2, parameter.getModelName());
                        preparedStatement.setString(3, parameter.getGroupName());
                        preparedStatement.setString(4, parameter.getParameters());
                        if (parameter.getLowSpec() != null) {
                            preparedStatement.setDouble(5, parameter.getLowSpec());
                        } else {
                            preparedStatement.setNull(5, Types.NULL);
                        }

                        if (parameter.getHighSpec() != null) {
                            preparedStatement.setDouble(6, parameter.getHighSpec());
                        } else {
                            preparedStatement.setNull(6, Types.NULL);
                        }
//                        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return parameterList.size();
                    }
                });
    }

    @Override
    public List<TestCpk> getCpkList(String factory, String modelName, Date startDate, Date endDate) {
        return testCpkRepository.findByFactoryAndModelNameAndStartDateBetween(factory, modelName, startDate, endDate);
    }
}
