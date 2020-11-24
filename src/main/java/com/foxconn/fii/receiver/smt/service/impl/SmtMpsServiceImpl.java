package com.foxconn.fii.receiver.smt.service.impl;

import com.foxconn.fii.data.primary.model.entity.SmtMps;
import com.foxconn.fii.data.primary.repository.SmtMpsRepository;
import com.foxconn.fii.receiver.smt.service.SmtMpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SmtMpsServiceImpl implements SmtMpsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SmtMpsRepository smtMpsRepository;

    @Override
    public void saveAll(List<SmtMps> mpsList) {
        if (mpsList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into smt_mps as target " +
                        "using(select factory=?, cft=?, model_name=?, model_name_si=?, start_date=?, running_day=?, plan_qty=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.cft=source.cft and target.model_name=source.model_name and target.start_date=source.start_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.running_day=source.running_day, " +
                        "   target.model_name_si=source.model_name_si, " +
                        "   target.plan_qty=source.plan_qty, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, cft, model_name, model_name_si, start_date, running_day, plan_qty, created_at, updated_at) " +
                        "   values(source.factory, source.cft, source.model_name, source.model_name_si, source.start_date, source.running_day, source.plan_qty, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        SmtMps mps = mpsList.get(i);
                        preparedStatement.setString(1, mps.getFactory());
                        preparedStatement.setString(2, mps.getCft());
                        preparedStatement.setString(3, mps.getModelName());
                        preparedStatement.setString(4, mps.getModelNameSI());
                        preparedStatement.setTimestamp(5, new Timestamp(mps.getStartDate().getTime()));
                        preparedStatement.setDouble(6, mps.getRunningDay());
                        preparedStatement.setDouble(7, mps.getPlan());
                        preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return mpsList.size();
                    }
                });
    }

    @Override
    public List<SmtMps> getSmtMpsList(String factory, Date startDate, Date endDate) {
        return smtMpsRepository.findByFactoryAndStartDateBetween(factory, startDate, endDate);
    }

    @Override
    public List<SmtMps> getSmtMpsList(String factory, String cft, Date startDate, Date endDate) {
        return smtMpsRepository.findByFactoryAndCftAndStartDateBetween(factory, cft, startDate, endDate);
    }
}
