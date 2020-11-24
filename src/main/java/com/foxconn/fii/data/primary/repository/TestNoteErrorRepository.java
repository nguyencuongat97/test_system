package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestNoteError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestNoteErrorRepository extends JpaRepository<TestNoteError, Integer> {
    List<TestNoteError>findByFactory(String factory);

    List<TestNoteError>findByFactoryAndStartDateBetween(String factory, Date startDate, Date endDate);

    List<TestNoteError>findByFactoryAndModelNameAndStartDateBetween(String factory, String modelName, Date startDate, Date endDate);

    TestNoteError findTop1ByFactoryAndModelNameAndError(String factory, String modelName, String error);

    List<TestNoteError>findByFactoryAndModelName(String factory, String modelName);

    TestNoteError findById(int id);
}
