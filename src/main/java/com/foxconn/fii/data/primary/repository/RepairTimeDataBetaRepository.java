package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.RepairTimeDataBeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairTimeDataBetaRepository extends JpaRepository <RepairTimeDataBeta, Long> {

    RepairTimeDataBeta findTop1ByActionOrderByIdDesc(String action);

}
