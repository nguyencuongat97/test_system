package com.foxconn.fii.data.b06.repository;

import com.foxconn.fii.data.b06.model.B06Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface B06ResourceRepository extends JpaRepository<B06Resource, String> {

    B06Resource findByEmployeeNo(String employeeNo);
}
