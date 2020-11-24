package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sForms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Repair8sFormsRepository extends JpaRepository<Repair8sForms, Long> {

//    Repair8sForms findTop1ByOwnerAndLocationAndCreatedAtBetweenOrderByCreatedAtDesc(String owner, String location, Date startDate, Date endDate);

//    Repair8sForms findTop1ByOwnerAndCreatedAtBetweenOrderByCreatedAtDesc(String owner, Date startDate, Date endDate);

    @Query("SELECT r8f " +
            "FROM Repair8sForms AS r8f " +
            "INNER JOIN Repair8sUsers AS r8u ON r8f.idUserOwner = r8u.id " +
            "WHERE r8u.userId = :owner " +
            "AND r8f.createdAt BETWEEN :startDate AND :endDate ")
    Page<Repair8sForms> jpqlGetFormsBetween(String owner, Date startDate, Date endDate, Pageable pageable);

    @Query("SELECT r8u AS user, r8ul AS config, r8f AS forms, r8fs AS formSteps, r8s AS steps " +
            "FROM Repair8sUsersLocations AS r8ul " +
            "INNER JOIN Repair8sUsers AS r8u ON r8ul.idUser = r8u.id " +
            "LEFT JOIN Repair8sForms AS r8f ON r8f.idUserOwner = r8ul.idUser AND r8f.idLocation = r8ul.idLocation AND r8f.idShift = r8ul.idShift " +
            "LEFT JOIN Repair8sFormSteps AS r8fs ON r8f.id = r8fs.idForm " +
            "LEFT JOIN Repair8sSteps AS r8s ON r8fs.idStep = r8s.id " +
            "WHERE r8u.isActive = 1 AND r8f.createdAt BETWEEN :startDate AND :endDate " +
            "OR r8f IS NULL ")
    List<Map<String, Object>> jpqlGetFormStepsAndFormsAndUserAndConfig(Date startDate, Date endDate);

}
