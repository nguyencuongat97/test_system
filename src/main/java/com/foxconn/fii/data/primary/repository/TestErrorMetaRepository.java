package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestErrorMetaRepository extends JpaRepository<TestErrorMeta, Integer> {
    TestErrorMeta findByErrorCode(String errorCode);

    List<TestErrorMeta> findAllByFactoryAndModelName(String factory, String modelName);

    List<TestErrorMeta> findAllByFactoryAndDescription(String factory, String description);

    List<TestErrorMeta> findAllByFactory(String factory);
}
