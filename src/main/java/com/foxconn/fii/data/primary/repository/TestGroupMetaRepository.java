package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestGroupMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestGroupMetaRepository extends JpaRepository<TestGroupMeta, Integer> {

    List<TestGroupMeta> findByFactoryAndModelNameAndVisibleTrue(String factory, String modelName);

    List<TestGroupMeta> findAllByFactory(String factory);

    List<TestGroupMeta> findAllByFactoryAndCustomer(String factory, String customer);

    List<TestGroupMeta> findAllByFactoryAndCustomerAndStage(String factory, String customer, String stage);

    List<TestGroupMeta> findAllByFactoryAndModelName(String factory, String modelName);

    List<TestGroupMeta> findAllByFactoryAndModelNameAndParameterIsTrue(String factory, String modelName);

    TestGroupMeta findTop1ByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);

    @Query("SELECT DISTINCT(stage) FROM TestGroupMeta WHERE factory = :factory AND customer = :customer")
    List<String> findStageByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT g FROM TestGroupMeta g WHERE factory = :factory AND customer = :customer ORDER BY step")
    List<TestGroupMeta> findByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT g FROM TestGroupMeta g WHERE factory = :factory AND customer = :customer AND (remark LIKE '%IN%' or remark LIKE '%OUT%')")
    List<TestGroupMeta> findGroupInputOutputByFactory(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT g FROM TestGroupMeta g WHERE factory = :factory AND modelName in :modelList AND (remark LIKE '%IN%' or remark LIKE '%OUT%')")
    List<TestGroupMeta> findGroupInputOutputByFactory(@Param("factory") String factory, @Param("modelList") List<String> modelList);

    @Query("SELECT g FROM TestGroupMeta g WHERE factory = :factory AND modelName in :modelList AND remark LIKE '%OUT%'")
    List<TestGroupMeta> findGroupOutputByFactory(@Param("factory") String factory, @Param("modelList") List<String> modelList);

    @Query("SELECT g FROM TestGroupMeta g WHERE factory = :factory AND modelName in :modelList AND remark LIKE '%WIP%'")
    List<TestGroupMeta> findGroupWipByFactory(@Param("factory") String factory, @Param("modelList") List<String> modelList);
}
