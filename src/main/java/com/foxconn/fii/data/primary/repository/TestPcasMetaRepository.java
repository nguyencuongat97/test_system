package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestPcasMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestPcasMetaRepository extends JpaRepository<TestPcasMeta, Integer> {

    List<TestPcasMeta> findByFactory(String factory);

    List<TestPcasMeta> findByFactoryAndModelName(String factory, String modelName);

    TestPcasMeta findByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);

}
