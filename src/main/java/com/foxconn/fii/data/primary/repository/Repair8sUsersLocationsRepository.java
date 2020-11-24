package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sUsersLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface Repair8sUsersLocationsRepository extends JpaRepository<Repair8sUsersLocations, Long> {

    @Query("SELECT r8ul " +
            "FROM Repair8sUsersLocations AS r8ul " +
            "INNER JOIN Repair8sUsers AS r8u ON r8ul.idUser = r8u.id " +
            "INNER JOIN Repair8sLocations AS r8l ON r8ul.idLocation = r8l.id " +
            "WHERE r8u.userId = :userId " +
            "AND r8l.location = :location ")
    Page<Repair8sUsersLocations> jpqlGetUserLocation(String userId, String location, Pageable pageable);

    @Query("SELECT r8ul.idUser AS idUser, r8u.userId AS userId, r8u.fullName AS fullName, r8ul.idLocation AS idLocation, r8l.location AS location " +
            "FROM Repair8sUsersLocations AS r8ul " +
            "INNER JOIN Repair8sUsers AS r8u ON r8ul.idUser = r8u.id " +
            "INNER JOIN Repair8sLocations AS r8l ON r8ul.idLocation = r8l.id " +
            "WHERE r8u.userId IS NOT NULL " +
            "AND r8l.location IS NOT NULL ")
    List<Map<String, Object>> jpqlGetAllUserLocation();

    @Query("SELECT r8ul AS userLocation, r8u AS user, r8l AS location, shift AS shift " +
            "FROM Repair8sUsersLocations AS r8ul " +
            "INNER JOIN Repair8sUsers AS r8u ON r8ul.idUser = r8u.id " +
            "LEFT JOIN Repair8sLocations AS r8l ON r8ul.idLocation = r8l.id " +
            "LEFT JOIN Repair8sShift AS shift ON r8ul.idShift = shift.id " +
            "WHERE r8u.userId = :userId ")
    Page<Map<String, Object>> jpqlGetUserConfig(String userId, Pageable pageable);

    @Query("SELECT r8ul AS userLocation, r8u AS user, r8l AS location, shift AS shift " +
            "FROM Repair8sUsersLocations AS r8ul " +
            "INNER JOIN Repair8sUsers AS r8u ON r8ul.idUser = r8u.id " +
            "INNER JOIN Repair8sLocations AS r8l ON r8ul.idLocation = r8l.id " +
            "INNER JOIN Repair8sShift AS shift ON r8ul.idShift = shift.id " +
            "WHERE r8u.isActive = 1 "+
            "ORDER BY r8u.userId, r8l.location ")
    List<Map<String, Object>> jpqlGetAllUserConfig();

}
