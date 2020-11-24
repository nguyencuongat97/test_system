package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingWorkResultMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HrEmployeeTrackingWorkResultMetaRepository extends JpaRepository<HrEmployeeTrackingWorkResultMeta, Long> {

    HrEmployeeTrackingWorkResultMeta findTop1ByWorkResultCn(String workResultCn);

    List<HrEmployeeTrackingWorkResultMeta> findByIsWorkDay(Boolean isWorkDay);

}
