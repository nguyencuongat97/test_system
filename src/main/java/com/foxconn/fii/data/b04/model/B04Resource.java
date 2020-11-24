package com.foxconn.fii.data.b04.model;

import com.foxconn.fii.data.primary.model.entity.TestResource;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "RESOURCE")
public class B04Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "idno")
    private String employeeNo;

    @Column(name = "name1")
    private String name;

    @Column(name = "name2")
    private String chineseName;

    @Column(name = "dem")
    private String dem;

    public static TestResource toTestResource(B04Resource b04Resource) {
        TestResource resource = new TestResource();
        resource.setEmployeeNo(b04Resource.getEmployeeNo());
        resource.setName(b04Resource.getName());
        resource.setChineseName(b04Resource.getChineseName());
        resource.setDem(b04Resource.getDem());
        return resource;
    }
}
