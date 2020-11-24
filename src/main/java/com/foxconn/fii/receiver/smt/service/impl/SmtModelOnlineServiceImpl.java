package com.foxconn.fii.receiver.smt.service.impl;

import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import com.foxconn.fii.data.primary.model.entity.SmtModelOnline;
import com.foxconn.fii.data.primary.repository.SmtModelOnlineRepository;
import com.foxconn.fii.receiver.smt.service.SmtModelOnlineService;
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
public class SmtModelOnlineServiceImpl implements SmtModelOnlineService {

    @Autowired
    private SmtModelOnlineRepository smtModelOnlineRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<SmtModelOnline> modelOnlineList) {
        if (modelOnlineList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into smt_model_online as target " +
                        "using(select factory=?, section_name=?, line_name=?, model_name=?, side=?, mo=?, start_date=?, end_date=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.section_name=source.section_name and target.line_name=source.line_name and target.model_name=source.model_name and target.side=source.side and target.start_date=source.start_date and target.end_date=source.end_date " +
                        "when matched then " +
                        "   update set " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, section_name, line_name, model_name, side, mo, start_date, end_date, created_at, updated_at) " +
                        "   values(source.factory, source.section_name, source.line_name, source.model_name, source.side, source.mo, source.start_date, source.end_date, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        SmtModelOnline online = modelOnlineList.get(i);
                        preparedStatement.setString(1, online.getFactory());
                        preparedStatement.setString(2, online.getSectionName());
                        preparedStatement.setString(3, online.getLineName());
                        preparedStatement.setString(4, online.getModelName());
                        preparedStatement.setString(5, online.getSide());
                        preparedStatement.setString(6, online.getMo());
                        preparedStatement.setTimestamp(7, new Timestamp(online.getStartDate().getTime()));
                        preparedStatement.setTimestamp(8, new Timestamp(online.getEndDate().getTime()));
                        preparedStatement.setTimestamp(9, new Timestamp(online.getCreatedAt().getTime()));
                        preparedStatement.setTimestamp(10, new Timestamp(online.getUpdatedAt().getTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return modelOnlineList.size();
                    }
                });
    }

    @Override
    public void saveAllMeta(List<SmtModelMeta> modelMetaList) {
        if (modelMetaList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into smt_model_meta as target " +
                        "using(select factory=?, model_name=?, cft=?, total_part=?, model_name_si=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.cft=source.cft, " +
                        "   target.total_part=source.total_part," +
                        "   target.model_name_si=source.model_name_si " +
                        "when not matched then " +
                        "   insert (factory, model_name, cft, total_part, model_name_si) " +
                        "   values(source.factory, source.model_name, source.cft, source.total_part, source.model_name_si);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        SmtModelMeta online = modelMetaList.get(i);
                        preparedStatement.setString(1, online.getFactory());
                        preparedStatement.setString(2, online.getModelName());
                        preparedStatement.setString(3, online.getCft());
                        preparedStatement.setInt(4, online.getTotalPart());
                        preparedStatement.setString(5, online.getModelNameSI());
                    }

                    @Override
                    public int getBatchSize() {
                        return modelMetaList.size();
                    }
                });
    }

    @Override
    public SmtModelOnline findModelOnline(String factory, String sectionName, String lineName, Date endDate) {
        return smtModelOnlineRepository.findTop1ByFactoryAndSectionNameAndLineNameAndCreatedAtBeforeOrderByCreatedAtDesc(factory, sectionName, lineName, endDate);
    }
}
