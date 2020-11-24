package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.FactoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryEntityRepository extends JpaRepository<FactoryEntity, Integer> {
}
