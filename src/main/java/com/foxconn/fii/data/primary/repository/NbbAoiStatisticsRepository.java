package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.NbbAoiStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NbbAoiStatisticsRepository extends JpaRepository<NbbAoiStatistics, Integer> {

    List<NbbAoiStatistics> findByFactoryAndLineNameAndModelNameAndSideAndMachineTypeAndWorkDateAndWorkSection(String factory, String lineName, String modelName, String side, String machineType, String workDate, Integer workSection);

    @Query(value = "SELECT * FROM aoi_statistics_nbb WHERE factory = :factory AND line_name = :lineName AND (work_date + right('00'+work_section, 2)) >= (:startDate + :startSection) and (work_date + right('00'+work_section, 2)) < (:endDate + :endSection)", nativeQuery = true)
    List<NbbAoiStatistics> findByFactoryAndLineNameAndWorkDateBetween(String factory, String lineName, String startDate, String startSection, String endDate, String endSection);

    @Query(value = "SELECT DISTINCT(model_name) FROM aoi_statistics_nbb WHERE factory = :factory AND line_name = :lineName AND (work_date + right('00'+work_section, 2)) >= (:startDate + :startSection) and (work_date + right('00'+work_section, 2)) < (:endDate + :endSection)", nativeQuery = true)
    List<String> getModelNameByFactoryAndLineNameAndWorkDateBetween(String factory, String lineName, String startDate, String startSection, String endDate, String endSection);
}
