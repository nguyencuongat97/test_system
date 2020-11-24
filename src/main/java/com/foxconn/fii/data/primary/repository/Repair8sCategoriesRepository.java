package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repair8sCategoriesRepository extends JpaRepository<Repair8sCategories, Long> {

    List<Repair8sCategories> findAllByOrderByNumberAsc();

}
