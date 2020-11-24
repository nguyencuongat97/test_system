package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestTaskComment;
import com.foxconn.fii.data.primary.model.entity.TestTaskDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTaskCommentRepository extends JpaRepository<TestTaskComment, Long> {

    List<TestTaskComment> findByTaskId(Long taskId);
}
