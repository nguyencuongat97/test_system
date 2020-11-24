package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestSolutionMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TestSolutionMetaRepository extends JpaRepository<TestSolutionMeta, Integer> {

    List<TestSolutionMeta> findAllByFactoryAndModelNameAndErrorCode(String factory, String modelName, String errorCode);

    @Query("SELECT DISTINCT(s.errorCode) FROM TestSolutionMeta s WHERE s.factory = :factory AND s.modelName = :modelName")
    List<String> getErrorCodeList(@Param("factory") String factory, @Param("modelName") String modelName);

    TestSolutionMeta findByFactoryAndModelNameAndErrorCodeAndSolutionAndAction(String factory, String modelName, String errorCode, String solution, String action);

    @Modifying
    @Transactional
    @Query("UPDATE TestSolutionMeta SET numberUsed = numberUsed + 1 WHERE id = :id")
    void increasingNumberUsed(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE TestSolutionMeta SET numberSuccess = numberSuccess + 1 WHERE id = :id")
    void increasingNumberSuccess(@Param("id") Integer id);
}
