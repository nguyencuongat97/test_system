package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.primary.model.entity.TestGroup;
import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import com.foxconn.fii.data.primary.model.entity.TestLineMeta;
import com.foxconn.fii.data.primary.repository.TestLineMetaRepository;
import com.foxconn.fii.receiver.test.service.TestLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestLineServiceImpl implements TestLineService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestLineMetaRepository testLineMetaRepository;

    @Override
    public void saveAllMeta(List<TestGroup> groupList) {
        if (groupList.isEmpty()) {
            return;
        }

        List<TestLineMeta> lineMetaList = new ArrayList<>();
        for (TestGroup group : groupList) {
            TestLineMeta lineMeta = new TestLineMeta();
            lineMeta.setFactory(group.getFactory());
            lineMeta.setModelName(group.getModelName());
            lineMeta.setLineName(group.getLineName());

            lineMetaList.add(lineMeta);
        }

        jdbcTemplate.batchUpdate(
                "merge into test_line_meta as target " +
                        "using(select factory=?, model_name=?, line_name=?, created_at=?, updated_at=?) as source " +
                        "   on target.factory=source.factory and target.line_name=source.line_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.model_name=source.model_name, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, model_name, line_name, created_at, updated_at) " +
                        "   values(source.factory, source.model_name, source.line_name, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        TestLineMeta lineMeta = lineMetaList.get(i);
                        preparedStatement.setString(1, lineMeta.getFactory());
                        preparedStatement.setString(2, lineMeta.getModelName());
                        preparedStatement.setString(3, lineMeta.getLineName());
                        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return lineMetaList.size();
                    }
                });
    }

    @Override
    public List<TestLineMeta> getLineList(String factory) {
        return testLineMetaRepository.findByFactory(factory);
    }
}
