package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface Repair8sUsersRepository extends JpaRepository<Repair8sUsers, Long> {

    Repair8sUsers findByUserIdOrderByIdDesc(String userId);

    @Query("SELECT r8u.id AS id, r8u.userId AS user, r8u.fullName AS fullName, r8u.isActive AS isActive, r8u.idRole AS idRole, shift.shift AS shift, r8l.location AS location " +
            "FROM Repair8sUsers AS r8u " +
            "LEFT JOIN Repair8sUsersLocations AS r8ul ON r8u.id = r8ul.idUser " +
            "LEFT JOIN Repair8sLocations AS r8l ON r8ul.idLocation = r8l.id " +
            "LEFT JOIN Repair8sShift AS shift ON r8ul.idShift = shift.id " +
            "WHERE r8u.isActive = 1 ORDER BY r8u.id DESC ")
    List<Map<String, Object>> jpqlGetAllDetailUser();

    List<Repair8sUsers> findByIdLessThan(Long id);

}
