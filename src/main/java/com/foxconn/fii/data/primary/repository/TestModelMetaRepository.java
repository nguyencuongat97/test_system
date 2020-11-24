package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestModelMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestModelMetaRepository extends JpaRepository<TestModelMeta, Integer> {

    List<TestModelMeta> findAllByFactory(String factory);

    List<TestModelMeta> findAllByFactoryAndVisibleIsTrue(String factory);

    List<TestModelMeta> findAllByFactoryAndStage(String factory, String stage);

    List<TestModelMeta> findAllByFactoryAndParameterIsTrueAndVisibleIsTrue(String factory);

    TestModelMeta findByFactoryAndModelName(String factory, String modelName);
    ///
    List<TestModelMeta> findAllByFactoryAndCustomer(String factory, String customer);



    @Query("SELECT DISTINCT(stage) FROM TestModelMeta WHERE factory = :factory AND customer = :customer AND visible = true")
    List<String> findStageByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT DISTINCT(customer) FROM TestModelMeta WHERE factory = :factory AND visible = true")
    List<String> findCustomerByFactory(@Param("factory") String factory);

    @Query("SELECT DISTINCT(modelName) FROM TestModelMeta WHERE factory = :factory AND customer = :customer AND visible = true")
    List<String> findModelByFactoryAndCustomer(@Param("factory") String factory, @Param("customer") String customer);

    @Query("SELECT DISTINCT(modelName) FROM TestModelMeta WHERE factory = :factory AND customer = :customer AND stage = :stage AND visible = true")
    List<String> findModelByFactoryAndCustomerAndStage(@Param("factory") String factory, @Param("customer") String customer, @Param("stage") String stage);

    List<TestModelMeta> findByFactoryAndCustomer(String factory, String customer);

    List<TestModelMeta> findByFactoryAndCustomerAndVisibleIsTrue(String factory, String customer);

    List<TestModelMeta> findByFactoryAndCustomerAndStageAndVisibleIsTrue(String factory, String customer, String stage);


    ////
    @Query(value = "SELECT customer FROM TestModelMeta\n" +
            "    WHERE customer != ''\n" +
            "    and factory = :factory \n" +
            "    group by customer")
    List<String> getCustomer(@Param("factory") String factory);

//    @Query(value = "SELECT  [id]\n" +
//            ",[model_name]\n" +
//            ",[factory]\n" +
//            ",[table_name]\n" +
//            ",[parameter]\n" +
//            ",[created_at]\n" +
//            ",[updated_at]\n" +
//            ",[customer_name]\n" +
//            ",[customer]\n" +
//            ",[stage]\n" +
//            ",[sub_stage]\n" +
//            ",[visible]\n" +
//            "FROM [dbo].[test_model_meta]\n" +
//            "where customer = :customer \n" +
//            "tand factory = :factory ")
//    List<> getCustomer(@Param("factory") String factory,@Param("customer") String  customer) ;
}
