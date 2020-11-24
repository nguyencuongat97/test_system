package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestModelMetaNbb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestModelMetaNbbRepository extends JpaRepository<TestModelMetaNbb, Integer> {

    @Query("SELECT DISTINCT(customer) FROM TestModelMetaNbb WHERE factory = :factory")
    List<String> findCustomerByFactory(@Param("factory") String factory);

    @Query("SELECT DISTINCT(stage) FROM TestModelMetaNbb WHERE factory = :factory AND customer = :customer")
    List<String> findStageByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    List<TestModelMetaNbb> findByFactory(String factory);

    List<TestModelMetaNbb> findByFactoryAndCustomer(String factory, String customer);

    List<TestModelMetaNbb> findByFactoryAndCustomerIn(String factory, List<String> customer);

    List<TestModelMetaNbb> findByFactoryAndCustomerIsNot(String factory, String customer);

    @Query("SELECT DISTINCT(modelName) FROM TestModelMetaNbb WHERE factory = :factory AND customer = :customer")
    List<String> findModelByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT DISTINCT(modelName) FROM TestModelMetaNbb WHERE factory = :factory AND customer = :customer AND stage = :stage")
    List<String> findModelByFactoryAndCustomerAndStage(@Param("factory") String factory, @Param("customer") String customer, @Param("stage") String stage);
}
