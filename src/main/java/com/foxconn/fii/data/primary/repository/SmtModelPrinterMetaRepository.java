package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.SmtModelPrinterMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmtModelPrinterMetaRepository extends JpaRepository<SmtModelPrinterMeta, Integer> {

    List<SmtModelPrinterMeta> findByFactory(String factory);

    SmtModelPrinterMeta findTop1ByFactoryAndModelName(String factory, String modelName);

    SmtModelPrinterMeta findTop1ByFactoryAndModelNameAndSide(String factory, String modelName, String side);

}
