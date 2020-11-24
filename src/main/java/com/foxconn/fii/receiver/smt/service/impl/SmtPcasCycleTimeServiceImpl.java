package com.foxconn.fii.receiver.smt.service.impl;

import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;
import com.foxconn.fii.data.primary.repository.SmtPcasCycleTimeRepository;
import com.foxconn.fii.receiver.smt.service.SmtPcasCycleTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class SmtPcasCycleTimeServiceImpl implements SmtPcasCycleTimeService {

    @Autowired
    private SmtPcasCycleTimeRepository smtPcasCycleTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void saveAll(List<SmtPcasCycleTime> pcasCycleTimeList) {
        if (pcasCycleTimeList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into smt_pcas_cycle_time as target " +
                        "using(select factory=?, plant_name=?, line_name=?, pcas_line_name=?, section_name=?, model_name=?, side=?, cycle_time=?, visible=?) as source " +
                        "   on target.factory=source.factory and target.line_name=source.line_name and target.pcas_line_name=source.pcas_line_name and target.section_name=source.section_name and target.model_name=source.model_name and target.side=source.side " +
                        "when matched then " +
                        "   update set " +
                        "   target.plant_name=source.plant_name, " +
                        "   target.cycle_time=source.cycle_time " +
                        "when not matched then " +
                        "   insert (factory, plant_name, line_name, pcas_line_name, section_name, model_name, side, cycle_time, visible) " +
                        "   values(source.factory, source.plant_name, source.line_name, source.pcas_line_name, source.section_name, source.model_name, source.side, source.cycle_time, source.visible);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        SmtPcasCycleTime pcasCycleTime = pcasCycleTimeList.get(i);
                        preparedStatement.setString(1, pcasCycleTime.getFactory());
                        preparedStatement.setString(2, pcasCycleTime.getPlantName());
                        preparedStatement.setString(3, pcasCycleTime.getLineName());
                        preparedStatement.setString(4, pcasCycleTime.getPcasLineName());
                        preparedStatement.setString(5, pcasCycleTime.getSectionName());
                        preparedStatement.setString(6, pcasCycleTime.getModelName());
                        preparedStatement.setString(7, pcasCycleTime.getSide());
                        preparedStatement.setFloat(8, pcasCycleTime.getCycleTime());
                        preparedStatement.setInt(9, pcasCycleTime.isVisible() ? 1 : 0);
                    }

                    @Override
                    public int getBatchSize() {
                        return pcasCycleTimeList.size();
                    }
                });
    }

    @Override
    public void saveAllSi(List<SmtPcasCycleTime> pcasCycleTimeList) {
        if (pcasCycleTimeList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into smt_pcas_cycle_time as target " +
                        "using(select factory=?, section_name=?, model_name=?, cycle_time=?, man_power=?, visible=?) as source " +
                        "   on target.factory=source.factory and target.model_name=source.model_name and target.section_name=source.section_name " +
                        "when matched then " +
                        "   update set " +
                        "   target.man_power=source.man_power, " +
                        "   target.cycle_time=source.cycle_time, " +
                        "   target.visible=source.visible " +
                        "when not matched then " +
                        "   insert (factory, section_name, model_name, cycle_time, man_power, visible) " +
                        "   values(source.factory, source.section_name, source.model_name, source.cycle_time, source.man_power, source.visible);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        SmtPcasCycleTime pcasCycleTime = pcasCycleTimeList.get(i);
                        preparedStatement.setString(1, pcasCycleTime.getFactory());
                        preparedStatement.setString(2, pcasCycleTime.getSectionName());
                        preparedStatement.setString(3, pcasCycleTime.getModelName());
                        preparedStatement.setFloat(4, pcasCycleTime.getCycleTime());
                        preparedStatement.setInt(5, pcasCycleTime.getManPower());
                        preparedStatement.setInt(6, pcasCycleTime.isVisible() ? 1 : 0);

                    }

                    @Override
                    public int getBatchSize() {
                        return pcasCycleTimeList.size();
                    }
                });
    }

//                modelLineMeta.setCycleTime(Float.parseFloat(cycleTime));
//                modelLineMeta.setFactory(factory);
//                modelLineMeta.setManPower((int) Double.parseDouble(manPower));
//                modelLineMeta.setModelName(modelName);
//                modelLineMeta.setSectionName("SI");
//                modelLineMeta.setVisible(true);
//                data.add(modelLineMeta);
}
