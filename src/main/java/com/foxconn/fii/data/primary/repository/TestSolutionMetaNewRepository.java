package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestSolutionMetaNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TestSolutionMetaNewRepository extends JpaRepository<TestSolutionMetaNew, Integer> {

    List<TestSolutionMetaNew> findAllByFactoryAndCustomerAndErrorCode(String factory, String customer, String errorCode);

    List<TestSolutionMetaNew> findAllByFactoryAndModelNameAndErrorCode(String factory, String modelName, String errorCode);

    List<TestSolutionMetaNew> findAllByFactoryAndCustomerAndErrorCodeAndRootCause(String factory, String customer, String errorCode, String rootCause);

    List<TestSolutionMetaNew> findAllByFactoryAndModelNameAndErrorCodeAndRootCause(String factory, String modelName, String errorCode, String rootCause);

    List<TestSolutionMetaNew> findByFactoryAndCustomerAndErrorCodeAndRootCauseAndAction(String factory, String customer, String errorCode, String solution, String action);

    TestSolutionMetaNew findByFactoryAndModelNameAndErrorCodeAndRootCauseAndAction(String factory, String modelName, String errorCode, String solution, String action);
}
