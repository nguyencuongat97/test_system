package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sRoles;
import com.foxconn.fii.data.primary.model.entity.Repair8sUsersLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface Repair8sRolesRepository extends JpaRepository<Repair8sRoles, Long> {

    @Query("SELECT r8r AS role, r8u AS user " +
            "FROM Repair8sUsers AS r8u " +
            "INNER JOIN Repair8sRoles AS r8r ON r8u.idRole = r8r.id " +
            "WHERE r8u.userId = :userId ")
    Page<Map<String, Object>> jpqlGetUserRoleByUserId(String userId, Pageable pageable);

}
