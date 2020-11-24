package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04TestErrorMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface B04TestErrorMetaRepository extends JpaRepository<B04TestErrorMeta, Integer> {
    List<B04TestErrorMeta> findAllByModelName(String modelName);
}
