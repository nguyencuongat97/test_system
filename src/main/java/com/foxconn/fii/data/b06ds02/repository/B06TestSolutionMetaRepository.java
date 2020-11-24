//package com.foxconn.fii.data.b06ds02.repository;
//
//import com.foxconn.fii.data.b06ds02.model.B06TestSolutionMeta;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface B06TestSolutionMetaRepository extends JpaRepository<B06TestSolutionMeta, Integer> {
//
//    List<B06TestSolutionMeta> findAllByModelNameAndErrorCode(String modelName, String errorCode);
//
//    @Query("SELECT errorCode FROM B06TestSolutionMeta WHERE modelName = :modelName GROUP BY modelName, errorCode")
//    List<String> getErrorCodeList(@Param("modelName") String modelName);
//}
