package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestParameterRepository extends JpaRepository<TestParameter, Integer> {

    List<TestParameter> findAllByFactoryAndModelName(String factory, String modelName);

    List<TestParameter> findAllByFactoryAndCustomerAndGroupName(String factory, String customer, String groupName);

    List<TestParameter> findAllByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);

    TestParameter findByFactoryAndModelNameAndParameters(String factory, String modelName, String parameters);

    TestParameter findByFactoryAndCustomerAndGroupNameAndParameters(String factory, String customer, String groupName, String parameters);

    TestParameter findByFactoryAndModelNameAndGroupNameAndParameters(String factory, String modelName, String groupName, String parameters);


    List<String> findDistinctCustomerByFactory(String factory);

    List<String> findDistinctModelNameByFactory(String factory);

    List<String> findDistinctGroupNameByFactoryAndCustomer(String factory, String customer);

    List<String> findDistinctGroupNameByFactoryAndModelName(String factory, String modelName);

    List<String> findDistinctParametersByFactoryAndCustomerAndGroupName(String factory, String customer, String groupName);

    List<String> findDistinctParametersByFactoryAndModelNameAndGroupName(String factory, String modelName, String groupName);
}
