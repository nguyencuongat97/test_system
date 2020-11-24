package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEtBackupDuty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HrEtBackupDutyRepository extends JpaRepository<HrEtBackupDuty, Long> {

    List<HrEtBackupDuty> findAllByEmployeeNoAndWorkDateBetween(String employeeNo, Date startDate, Date endDate);

}
