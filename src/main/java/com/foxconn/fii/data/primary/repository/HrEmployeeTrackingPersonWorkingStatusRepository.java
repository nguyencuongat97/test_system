package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingPersonWorkingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface HrEmployeeTrackingPersonWorkingStatusRepository extends JpaRepository<HrEmployeeTrackingPersonWorkingStatus, Long> {

    List<HrEmployeeTrackingPersonWorkingStatus> findByWorkDate(Date workDate);

    List<HrEmployeeTrackingPersonWorkingStatus> findByWorkDateBetween(Date startDate, Date endDate);

    HrEmployeeTrackingPersonWorkingStatus findTop1ByOrderByWorkDateAsc();

    HrEmployeeTrackingPersonWorkingStatus findTop1ByOrderByWorkDateDesc();

    HrEmployeeTrackingPersonWorkingStatus findTop1ByEmpNoAndWorkDate(String empNo, Date workDate);

//    @Query("SELECT COUNT(DISTINCT hetpws.empNo) AS qty, hetpws.workDate AS workDate " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "INNER JOIN HrEmployeeTrackingOfficeUnitInfo AS hetoui ON hetpi.officeCode = hetoui.officeUnitCode " +
//            "WHERE hetpws.workDate <= :endDate " +
//            "AND hetpws.workDate >= :startDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetoui.officeUnitCode IN :officeUnitCodeList " +
//            "AND hetpi.leaveDate > hetpws.workDate " +
//            "GROUP BY hetpws.workDate " +
//            "ORDER BY hetpws.workDate ASC ")
//    List<Map<String, Object>> jpqlCountDaily(String workStatus, List<String> officeUnitCodeList, Date startDate, Date endDate);
//
//
//    @Query("SELECT COUNT(DISTINCT hetpws.empNo) AS qty, hetpws.workDate AS workDate " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "INNER JOIN HrEmployeeTrackingOfficeUnitInfo AS hetoui ON hetpi.officeCode = hetoui.officeUnitCode " +
//            "WHERE hetpws.workDate <= :endDate " +
//            "AND hetpws.workDate >= :startDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetpws.workShift = :workShift " +
//            "AND hetoui.officeUnitCode IN :officeUnitCodeList " +
//            "AND hetpi.leaveDate > hetpws.workDate " +
//            "GROUP BY hetpws.workDate " +
//            "ORDER BY hetpws.workDate ASC ")
//    List<Map<String, Object>> jpqlCountShift(String workStatus, String workShift, List<String> officeUnitCodeList, Date startDate, Date endDate);
//
//    @Query("SELECT hetpws.empNo AS empNo, COUNT(hetpws.empNo) AS qty " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "INNER JOIN HrEmployeeTrackingOfficeUnitInfo AS hetoui ON hetpi.officeCode = hetoui.officeUnitCode " +
//            "WHERE hetpws.workDate <= :endDate " +
//            "AND hetpws.workDate >= :startDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetoui.officeUnitCode IN :officeUnitCodeList " +
//            "AND hetpi.leaveDate > hetpws.workDate " +
//            "GROUP BY hetpws.empNo " +
//            "ORDER BY qty DESC ")
//    List<Map<String, Object>> jpqlCountStatusBetween(String workStatus, Date startDate, Date endDate, List<String> officeUnitCodeList);
//
//    @Query("SELECT DISTINCT hetpi.empNo AS empNo, hetpi.cardNo AS cardNo, hetpi.name AS name " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "WHERE hetpws.workDate = :queryDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "ORDER BY hetpi.empNo ASC ")
//    List<Map<String, Object>> jpqlPersonListByWorkDateAndWorkStatus(Date queryDate, String workStatus);
//
//    @Query("SELECT DISTINCT hetpi.empNo AS empNo, hetpi.cardNo AS cardNo, hetpi.name AS name " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "WHERE hetpws.workDate = :queryDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetpws.workShift = :workShift " +
//            "ORDER BY hetpi.empNo ASC ")
//    List<Map<String, Object>> jpqlPersonListByWorkDateAndWorkStatusAndWorkShift(Date queryDate, String workStatus, String workShift);
//
//    @Query("SELECT DISTINCT hetpi.empNo AS empNo, hetpi.cardNo AS cardNo, hetpi.name AS name, hetoui.officeUnitCode as officeUnitCode, hetoui.officeUnitName as officeUnitName " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "INNER JOIN HrEmployeeTrackingOfficeUnitInfo AS hetoui ON hetpi.officeCode = hetoui.officeUnitCode " +
//            "WHERE hetpws.workDate = :queryDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetoui.officeUnitCode IN :officeUnitCodeList " +
//            "AND hetpi.leaveDate > :queryDate " +
//            "ORDER BY hetpi.empNo ASC ")
//    List<Map<String, Object>> jpqlPersonListByWorkDateAndWorkStatus(Date queryDate, String workStatus, List<String> officeUnitCodeList);
//
//    @Query("SELECT DISTINCT hetpi.empNo AS empNo, hetpi.cardNo AS cardNo, hetpi.name AS name, hetoui.officeUnitCode as officeUnitCode, hetoui.officeUnitName as officeUnitName " +
//            "FROM HrEmployeeTrackingPersonWorkingStatus AS hetpws " +
//            "INNER JOIN HrEmployeeTrackingPersonInfo AS hetpi ON hetpws.empNo = hetpi.empNo " +
//            "INNER JOIN HrEmployeeTrackingOfficeUnitInfo AS hetoui ON hetpi.officeCode = hetoui.officeUnitCode " +
//            "WHERE hetpws.workDate = :queryDate " +
//            "AND hetpws.workStatus = :workStatus " +
//            "AND hetpws.workShift = :workShift " +
//            "AND hetoui.officeUnitCode IN :officeUnitCodeList " +
//            "AND hetpi.leaveDate > :queryDate " +
//            "ORDER BY hetpi.empNo ASC ")
//    List<Map<String, Object>> jpqlPersonListByWorkDateAndWorkStatusAndWorkShift(Date queryDate, String workStatus, String workShift, List<String> officeUnitCodeList);

}
