package com.foxconn.fii.receiver.test.service;

import com.foxconn.fii.data.primary.model.entity.TestTaskComment;

import java.util.List;
import java.util.Optional;

public interface TestTaskDailyService {

    void saveComment(TestTaskComment comment);

    void removeComment(TestTaskComment comment);

    List<TestTaskComment> getListCommentByTaskId(Long taskId);

    Optional<TestTaskComment> getCommentById(Long id);

    void increasingCommentNumber(Long taskId);
}
