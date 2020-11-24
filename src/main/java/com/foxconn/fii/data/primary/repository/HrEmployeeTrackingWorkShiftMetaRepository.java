package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingWorkShiftMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HrEmployeeTrackingWorkShiftMetaRepository extends JpaRepository<HrEmployeeTrackingWorkShiftMeta, Long> {

    HrEmployeeTrackingWorkShiftMeta findTop1ByShiftCode(String shiftCode);

    List<HrEmployeeTrackingWorkShiftMeta> findByShiftSectionEn(String shiftSectionEn);

}
