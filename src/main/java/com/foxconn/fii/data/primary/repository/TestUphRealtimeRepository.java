package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestUphRealtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestUphRealtimeRepository extends JpaRepository<TestUphRealtime, Long> {

    List<TestUphRealtime> findByFactoryOrderByWorkSectionAsc(String factory);

    List<TestUphRealtime> findByFactoryAndWorkSection(String factory, Integer workSection);

}
