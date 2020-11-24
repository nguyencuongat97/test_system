package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestIgnoreModelMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestIgnoreModelMetaRepository extends JpaRepository<TestIgnoreModelMeta, Integer> {
    TestIgnoreModelMeta findTop1ByFactory(String factory);
}
