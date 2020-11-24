package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestSpecWarning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestWarningSpecRepository extends JpaRepository<TestSpecWarning, Integer> {

    List<TestSpecWarning> findAllByFactory(String factory);
}
