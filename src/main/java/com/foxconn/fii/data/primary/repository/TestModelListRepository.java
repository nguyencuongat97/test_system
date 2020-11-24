package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestModelList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestModelListRepository extends JpaRepository<TestModelList, Integer> {

    TestModelList findTop1ByFactory(String factory);
}
