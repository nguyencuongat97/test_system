package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.CopyIcData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CopyIcDataRepository extends JpaRepository<CopyIcData, Long> {

    List<CopyIcData> findByTimeBetween(Date startDate, Date endDate);

    List<CopyIcData> findByModelAndTimeBetween(String model, Date startDate, Date endDate);
}
