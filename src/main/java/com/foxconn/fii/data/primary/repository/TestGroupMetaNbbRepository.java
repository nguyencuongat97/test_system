package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestGroupMetaNbb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestGroupMetaNbbRepository extends JpaRepository<TestGroupMetaNbb, Integer> {

    @Query("SELECT groupName FROM TestGroupMetaNbb WHERE factory = :factory AND customer = :customer ORDER BY step")
    List<String> findGroupByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT groupName FROM TestGroupMetaNbb WHERE factory = :factory AND customer = :customer AND stage = :stage ORDER BY step")
    List<String> findGroupByFactoryAndCustomerAndStage(@Param("factory") String factory, @Param("customer") String customer, @Param("stage") String stage);

    @Query("SELECT DISTINCT(stage) FROM TestGroupMetaNbb WHERE factory = :factory AND customer = :customer")
    List<String> findStageByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT g FROM TestGroupMetaNbb g WHERE factory = :factory AND customer = :customer AND (remark = 3 or remark = 0 or remark = 1)")
    List<TestGroupMetaNbb> findGroupOutputByFactory(@Param("factory") String factory, @Param("customer") String customer);

    List<TestGroupMetaNbb> findByFactory(String factory);

    @Query("SELECT g FROM TestGroupMetaNbb g WHERE factory = :factory AND customer = :customer ORDER BY step")
    List<TestGroupMetaNbb> findByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT g FROM TestGroupMetaNbb g WHERE factory = :factory AND customer = :customer AND stage = :stage ORDER BY step")
    List<TestGroupMetaNbb> findByFactoryAndCustomerAndStage(@Param("factory") String factory, @Param("customer") String customer, @Param("stage") String stage);

    List<TestGroupMetaNbb> findByFactoryAndCustomerAndStageAndGroupName(String factory, String customer, String stage, String groupName);
}
