package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface B04ResourceRepository extends JpaRepository<B04Resource, Integer> {

    B04Resource findByEmployeeNo(String employeeNo);

    List<B04Resource> findByDem(String dem);
}
