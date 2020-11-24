package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingBellNo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;

public interface HrEmployeeTrackingBellNoRepository extends JpaRepository<HrEmployeeTrackingBellNo, Long> {

    HashSet<HrEmployeeTrackingBellNo> findByFactoryName(String factoryName);

}
