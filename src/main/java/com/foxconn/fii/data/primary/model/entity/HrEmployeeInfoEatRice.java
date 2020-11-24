package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "hr_et_info_eat_rice")
public class HrEmployeeInfoEatRice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee_code")
    private String empCode;

    @Column(name = "employee_name")
    private String empName;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "department")
    private String department;

    @Column(name = "business_group")
    private String busGroup;

    @Column(name = "consume_time")
    private Date consumeTime;

    @Column(name = "get_time_update")
    private Date timeUpdateSV;

    @Column(name = "meal_type")
    private String mealType;

    @Column(name = "restaurant")
    private String restaurant;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "breakfast_money")
    private Integer breakfastM;

    @Column(name = "lunch_money")
    private Integer lunchM;

    @Column(name = "dinner_money")
    private Integer dinnerM;

    @Column(name = "supper_money")
    private Integer supperM;

    @Column(name = "type_meal_vn")
    private TypeMeal typeMealVn;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Date createdAt;

    public enum TypeMeal {
        BREAKFAST,
        LUNCH,
        DINNER,
        SUPPER,
        OTHER
    }
}
