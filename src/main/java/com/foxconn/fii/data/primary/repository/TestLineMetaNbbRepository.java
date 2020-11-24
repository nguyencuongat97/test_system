package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestLineMetaNbb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestLineMetaNbbRepository extends JpaRepository<TestLineMetaNbb, Integer> {
    List<TestLineMetaNbb> findByFactory(String factory);
}
