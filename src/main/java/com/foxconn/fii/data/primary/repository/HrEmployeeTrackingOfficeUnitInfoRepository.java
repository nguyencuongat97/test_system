package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingOfficeUnitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface HrEmployeeTrackingOfficeUnitInfoRepository extends JpaRepository<HrEmployeeTrackingOfficeUnitInfo, Long> {

    @Query("SELECT hetoui.officeUnitCode AS ouCode, hetoui.officeUnitName AS ouName, hetoui.factoryName AS factoryName " +
            "FROM HrEmployeeTrackingOfficeUnitInfo AS hetoui ")
    List<Map<String, String>> jpqlGetAll();

    List<HrEmployeeTrackingOfficeUnitInfo> findByOfficeUnitCodeIn(List<String> officeUnitCodeList);

    List<HrEmployeeTrackingOfficeUnitInfo> findByFactoryName(String factoryName);

}
