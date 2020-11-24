package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestOeeSpec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestOeeSpecRepository extends JpaRepository<TestOeeSpec, Integer> {

    TestOeeSpec findTop1ByItem(String item);

}
