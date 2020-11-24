package com.foxconn.fii.receiver.test.service.impl;

import com.foxconn.fii.data.primary.model.entity.TestTaskComment;
import com.foxconn.fii.data.primary.repository.TestTaskCommentRepository;
import com.foxconn.fii.data.primary.repository.TestTaskDailyRepository;
import com.foxconn.fii.receiver.test.service.TestTaskDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TestTaskDailyServiceImpl implements TestTaskDailyService {

    @Autowired
    private TestTaskDailyRepository testTaskDailyRepository;

    @Autowired
    private TestTaskCommentRepository testTaskCommentRepository;

    @Override
    public void saveComment(TestTaskComment comment) {
        testTaskCommentRepository.save(comment);
    }

    @Override
    public void removeComment(TestTaskComment comment) {
        testTaskCommentRepository.delete(comment);
    }

    @Override
    public List<TestTaskComment> getListCommentByTaskId(Long taskId) {
        return testTaskCommentRepository.findByTaskId(taskId);
    }

    @Override
    public Optional<TestTaskComment> getCommentById(Long id) {
        return testTaskCommentRepository.findById(id);
    }

    @Override
    public void increasingCommentNumber(Long taskId) {
        testTaskDailyRepository.increasingCommentNumber(taskId);
    }
}
