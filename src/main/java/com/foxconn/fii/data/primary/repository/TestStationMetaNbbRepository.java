package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestStationMetaNbb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestStationMetaNbbRepository extends JpaRepository<TestStationMetaNbb, Long> {

    List<TestStationMetaNbb> findAllByFactoryAndCustomerAndStageNameAndGroupNameOrderByStationIdAsc(String factory, String customer, String stageName, String groupName);

}
