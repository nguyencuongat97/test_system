package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.PcEcn;
import com.foxconn.fii.data.primary.model.entity.ReDailyRemain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PcEcnRepository extends JpaRepository<PcEcn, Long> {

    List<PcEcn> findByFactoryAndEffectiveDateAfter(String factory, Date effectiveDate);

}
