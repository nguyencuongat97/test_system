package com.foxconn.fii.receiver.re.service.impl;

import com.foxconn.fii.data.primary.model.entity.RepairIODaily;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import com.foxconn.fii.data.primary.repository.RepairIODailyRepository;
import com.foxconn.fii.receiver.re.service.RepairIODailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RepairIODailyServiceImpl implements RepairIODailyService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RepairIODailyRepository repairIODailyRepository;

    @Override
    public void saveAll(List<RepairIODaily> repairIODailyList) {
        if (repairIODailyList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into re_io_daily as target " +
                        "using(select factory=?, section_name=?, model_name=?, status=?, start_date=?, end_date=?, total_qty=?, input_qty=?, output_qty=?, remain_qty=?) as source " +
                        "   on target.factory=source.factory and target.section_name=source.section_name and target.model_name=source.model_name and target.status=source.status and target.start_date=source.start_date and target.end_date=source.end_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.total_qty=source.total_qty, " +
                        "   target.input_qty=source.input_qty, " +
                        "   target.output_qty=source.output_qty, " +
                        "   target.remain_qty=source.remain_qty " +
                        "when not matched then " +
                        "   insert (factory, section_name, model_name, status, start_date, end_date, total_qty, input_qty, output_qty, remain_qty) " +
                        "   values(source.factory, source.section_name, source.model_name, source.status, source.start_date, source.end_date, source.total_qty, source.input_qty, source.output_qty, source.remain_qty);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        RepairIODaily repair = repairIODailyList.get(i);
                        preparedStatement.setString(1, repair.getFactory());
                        preparedStatement.setString(2, repair.getSectionName());
                        preparedStatement.setString(3, repair.getModelName());
                        preparedStatement.setInt(4, repair.getStatus().ordinal());
                        preparedStatement.setTimestamp(5, new Timestamp(repair.getStartDate().getTime()));
                        preparedStatement.setTimestamp(6, new Timestamp(repair.getEndDate().getTime()));
                        preparedStatement.setInt(7, repair.getTotal());
                        preparedStatement.setInt(8, repair.getInput());
                        preparedStatement.setInt(9, repair.getOutput());
                        preparedStatement.setInt(10, repair.getRemain());
                    }

                    @Override
                    public int getBatchSize() {
                        return repairIODailyList.size();
                    }
                });
    }

    @Override
    public List<RepairIODaily> findByFactoryAndInputTimeBetween(String factory, TestRepairSerialError.Status status, Date startDate, Date endDate) {
        return repairIODailyRepository.findByFactoryAndStatusAndStartDateBetween(factory, status, startDate, endDate);
    }

    @Override
    public List<Object[]> findByFactoryAndInputTimeBefore(String factory, TestRepairSerialError.Status status, Date startDate) {
        return repairIODailyRepository.findByFactoryAndStatusAndStartDateBefore(factory, status, startDate);
    }
}
