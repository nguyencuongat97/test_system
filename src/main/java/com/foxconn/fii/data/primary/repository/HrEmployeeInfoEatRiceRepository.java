package com.foxconn.fii.data.primary.repository;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeInfoEatRice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HrEmployeeInfoEatRiceRepository extends JpaRepository<HrEmployeeInfoEatRice, Long> {

    @Query("SELECT typeMealVn, COUNT(id) as coun FROM HrEmployeeInfoEatRice WHERE deviceId IN :listDeviceId " +
            "AND restaurant = :restaurant AND consumeTime BETWEEN :startDate AND :endDate GROUP BY typeMealVn")
    List<Object[]> countMealBN3(List<Integer> listDeviceId, String restaurant, Date startDate, Date endDate);

    @Query("SELECT typeMealVn, COUNT(id) as coun FROM HrEmployeeInfoEatRice WHERE deviceId NOT IN :listDeviceId " +
            "AND restaurant = :restaurant AND consumeTime BETWEEN :startDate AND :endDate GROUP BY typeMealVn")
    List<Object[]> countMealBN(List<Integer> listDeviceId, String restaurant, Date startDate, Date endDate);

    List<HrEmployeeInfoEatRice> findAllByEmpCodeAndConsumeTimeBetween(String empCode, Date consumeTimeStart, Date consumeTimeEnd);
}
