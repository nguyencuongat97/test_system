package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sSteps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repair8sStepsRepository extends JpaRepository<Repair8sSteps, Long> {

    List<Repair8sSteps> findByIdCateOrderByNumberAsc(Long idCate);

}
