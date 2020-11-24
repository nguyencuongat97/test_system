package com.foxconn.fii.data.b06ds03.repository;

import com.foxconn.fii.data.b06ds03.model.B06TestSerialError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface B06TestSerialErrorRepository extends JpaRepository<B06TestSerialError, B06TestSerialError.B06TestSerialErrorId> {

    @Query("SELECT modelName, errorCode, count(*) as count FROM B06TestSerialError WHERE workingDate BETWEEN :startDate AND :endDate GROUP BY modelName, errorCode")
    List<Object[]> countByModelNameAndErrorCode(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName, groupName, errorCode, count(*) as count FROM B06TestSerialError WHERE workingDate BETWEEN :startDate AND :endDate GROUP BY modelName, groupName, errorCode")
    List<Object[]> countByGroupNameAndErrorCode(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT modelName, groupName, locationCode, count(*) as count FROM B06TestSerialError WHERE workingDate BETWEEN :startDate AND :endDate GROUP BY modelName, groupName, locationCode")
    List<Object[]> countByGroupNameAndLocationCode(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT errorCode, reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND groupName = :groupName AND workingDate BETWEEN :startDate AND :endDate GROUP BY errorCode, reason")
    List<Object[]> countByErrorCodeAndReason(@Param("modelName") String modelName, @Param("groupName") String groupName,@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT errorCode, reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND workingDate BETWEEN :startDate AND :endDate GROUP BY errorCode, reason")
    List<Object[]> countByErrorCodeAndReason(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND errorCode = :errorCode AND workingDate BETWEEN :startDate AND :endDate GROUP BY reason")
    List<Object[]> countByReason(@Param("modelName") String modelName, @Param("errorCode") String errorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT groupName, stationName, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND errorCode = :errorCode AND reason = :reason AND workingDate BETWEEN :startDate AND :endDate GROUP BY groupName, stationName")
    List<Object[]> countByStation(@Param("modelName") String modelName, @Param("errorCode") String errorCode, @Param("reason") String reason, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
