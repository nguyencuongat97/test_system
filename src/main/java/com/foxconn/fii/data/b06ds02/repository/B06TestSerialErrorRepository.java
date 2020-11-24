//package com.foxconn.fii.data.b06ds02.repository;
//
//import com.foxconn.fii.data.b06ds02.model.B06TestSerialError;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Date;
//import java.util.List;
//
//public interface B06TestSerialErrorRepository extends JpaRepository<B06TestSerialError, B06TestSerialError.B06TestSerialErrorId> {
//
//    @Query("SELECT errorCode, reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND groupName = :groupName AND workingDate > :workingDate GROUP BY errorCode, reason")
//    List<Object[]> countByErrorCodeAndReason(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("workingDate") Date workingDate);
//
//    @Query("SELECT errorCode, reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND groupName = :groupName AND stationName = :stationName AND workingDate > :workingDate GROUP BY errorCode, reason")
//    List<Object[]> countByErrorCodeAndReason(@Param("modelName") String modelName, @Param("groupName") String groupName, @Param("stationName") String stationName, @Param("workingDate") Date workingDate);
//
//    @Query("SELECT reason, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND errorCode = :errorCode AND workingDate > :workingDate GROUP BY reason")
//    List<Object[]> countByReason(@Param("modelName") String modelName, @Param("errorCode") String errorCode, @Param("workingDate") Date workingDate);
//
//    @Query("SELECT groupName, stationName, count(*) as count FROM B06TestSerialError WHERE modelName = :modelName AND errorCode = :errorCode AND reason = :reason AND workingDate > :workingDate GROUP BY groupName, stationName")
//    List<Object[]> countByStation(@Param("modelName") String modelName, @Param("errorCode") String errorCode, @Param("reason") String reason, @Param("workingDate") Date workingDate);
//}
