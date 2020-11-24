package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmtModelMetaRepository extends JpaRepository<SmtModelMeta, Integer> {

    List<SmtModelMeta> findByFactory(String factory);

    SmtModelMeta findTop1ByFactoryAndModelName(String factory, String modelName);

//    SmtModelMeta findTop1ByFactoryAndModelNameAndSide(String factory, String modelName, String side);

    @Query("SELECT smm FROM SmtModelMeta as smm WHERE smm.factory = :factory AND :modelNameAoi like '%'+smm.modelNameAoi+'%' AND NOT smm.modelNameAoi = ''")
    List<SmtModelMeta> findByFactoryAndModelNameAoiINSIDE(String factory, String modelNameAoi);

    @Query("SELECT smm FROM SmtModelMeta as smm WHERE smm.factory = :factory AND :modelNameAoi like '%'+smm.modelNameMounter+'%' AND NOT smm.modelNameAoi = ''")
    List<SmtModelMeta> findByFactoryAndModelNameMounterINSIDE(String factory, String modelNameAoi);

}
