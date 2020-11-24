package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04TestPlanMeta;
import com.foxconn.fii.common.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface B04TestPlanMetaRepository extends JpaRepository<B04TestPlanMeta, Integer> {

    List<B04TestPlanMeta> findByShiftTimeAfter(Date shiftTime);

    List<B04TestPlanMeta> findByShiftAndShiftTime(ShiftType shiftType, Date shiftTime);

    List<B04TestPlanMeta> findByModelNameAndShiftAndShiftTime(String modelName, ShiftType shiftType, Date shiftTime);
}
