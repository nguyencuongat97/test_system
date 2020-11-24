package com.foxconn.fii.data.b06te.repository;

import com.foxconn.fii.data.b06te.model.TeEFOwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeEFOwnerTypeRepository extends JpaRepository<TeEFOwnerType, Long> {

    Optional<TeEFOwnerType> findByOwnerType(String ownerType);

}
