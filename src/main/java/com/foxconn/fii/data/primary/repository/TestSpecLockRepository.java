package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestSpecLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSpecLockRepository extends JpaRepository<TestSpecLock, Integer> {

    List<TestSpecLock> findAllByFactory(String factory);
}
