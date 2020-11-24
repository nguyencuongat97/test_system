package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingPersonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HrEmployeeTrackingPersonInfoRepository extends JpaRepository<HrEmployeeTrackingPersonInfo, Long> {

    Optional<HrEmployeeTrackingPersonInfo> findTop1ByEmpNo(String empNo);

}
