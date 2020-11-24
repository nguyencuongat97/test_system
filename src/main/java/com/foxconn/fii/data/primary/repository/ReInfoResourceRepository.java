package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.ReInfoResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReInfoResourceRepository extends JpaRepository<ReInfoResource, Integer> {
    List<ReInfoResource> findByDepartment(String department);
}
