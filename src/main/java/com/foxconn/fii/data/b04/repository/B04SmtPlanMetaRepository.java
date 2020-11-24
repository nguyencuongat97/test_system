package com.foxconn.fii.data.b04.repository;

import com.foxconn.fii.data.b04.model.B04SmtPlanMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface B04SmtPlanMetaRepository extends JpaRepository<B04SmtPlanMeta, Integer> {

    List<B04SmtPlanMeta> findByShiftTimeAfter(Date shiftTime);
}
