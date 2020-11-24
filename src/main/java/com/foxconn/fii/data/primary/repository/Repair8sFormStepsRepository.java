package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.Repair8sFormSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Repair8sFormStepsRepository extends JpaRepository<Repair8sFormSteps, Long> {

    List<Repair8sFormSteps> findByIdFormOrderByIdAsc(Long idForm);

    @Query("SELECT r8c.number AS cateNumber, r8c.name AS cateName, r8c.title AS cateTitle, " +
            "r8s.number AS stepsNumber, r8s.number.description AS stepsDescription, " +
            "r8fs.id AS formStepsId, r8fs.confirm AS formStepsConfirm, r8fs.description AS formStepsDescription, r8fs.check AS formStepsCheck " +
            "FROM Repair8sFormSteps AS r8fs " +
            "INNER JOIN Repair8sSteps AS r8s ON r8fs.idStep = r8s.id " +
            "INNER JOIN Repair8sCategories AS r8c ON r8s.idCate = r8c.id " +
            "WHERE r8fs.idForm = :idForm " +
            "ORDER BY r8fs.id ASC")
    List<Map<String, Object>> jpqlGetFormStepsByIdForm(Long idForm);

    @Query("SELECT r8fs.id AS idFormSteps, r8fs.confirm AS confirm, r8f.createdAt AS createdAt, r8fs.check AS check, r8s.description AS description " +
            "FROM Repair8sFormSteps AS r8fs " +
            "INNER JOIN Repair8sForms AS r8f ON r8fs.idForm = r8f.id " +
            "INNER JOIN Repair8sSteps AS r8s ON r8fs.idStep = r8s.id " +
            "INNER JOIN Repair8sUsers AS r8u ON r8f.idUserOwner = r8u.id " +
            "INNER JOIN Repair8sLocations AS r8l ON r8f.idLocation = r8l.id " +
            "WHERE r8u.userId = :owner " +
            "AND r8l.location = :location " +
            "AND r8f.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY r8f.createdAt ASC ")
    List<Map<String, Object>> jpqlGetFormsAndFormStepsByOwnerAndLocation(String owner, String location, Date startDate, Date endDate);

    @Query("SELECT r8fs " +
            "FROM Repair8sFormSteps AS r8fs " +
            "INNER JOIN Repair8sForms AS r8f ON r8fs.idForm = r8f.id " +
            "WHERE r8f.idUserOwner = :idUserOwner " +
            "AND r8f.idLocation = :idLocation " +
            "AND r8f.idShift = :idShift " +
            "AND r8f.createdAt BETWEEN :startDate AND :endDate ")
    List<Repair8sFormSteps> jpqlGetFormStepsByIdUserOwnerAndIdLocationAndIdShift(long idUserOwner, long idLocation, long idShift, Date startDate, Date endDate);

}
