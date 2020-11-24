package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTaskDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface TestTaskDailyRepository extends JpaRepository<TestTaskDaily, Long> {

    List<TestTaskDaily> findByFactoryAndCreatedAtBetween(String factory, Date startDate, Date endDate);

    @Query("SELECT t FROM TestTaskDaily t WHERE factory = :factory AND resourceGroup = :resourceGroup AND createdAt BETWEEN :startDate AND :endDate")
    List<TestTaskDaily> findByFactoryAndResourceGroupAndCreatedAtBetween(String factory, String resourceGroup, Date startDate, Date endDate);

    @Query("SELECT t FROM TestTaskDaily t WHERE factory = :factory AND resourceGroup = :resourceGroup AND (createdAt BETWEEN :startDate AND :endDate OR status != :status)")
    List<TestTaskDaily> findByFactoryAndResourceGroupAndCreatedAtBetween(String factory, String resourceGroup, Date startDate, Date endDate, TestTaskDaily.Status status);

    List<TestTaskDaily> findByFactoryAndEmployeeIdAndCreatedAtBetween(String factory, String employeeId, Date startDate, Date endDate);

    @Modifying
    @Transactional
    @Query("UPDATE TestTaskDaily SET commentNumber = commentNumber + 1 WHERE id = :id")
    void increasingCommentNumber(@Param("id") long id);
}
