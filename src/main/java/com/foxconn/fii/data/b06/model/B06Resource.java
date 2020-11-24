package com.foxconn.fii.data.b06.model;

import com.foxconn.fii.data.primary.model.entity.TestResource;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TE_OWNER")
public class B06Resource {

    @Id
    @Column(name = "id")
    private String employeeNo;

    @Column(name = "vn_name")
    private String name;

    @Column(name = "cn_name")
    private String chineseName;

    public static TestResource toTestResource(B06Resource b06Resource) {
        TestResource resource = new TestResource();
        resource.setEmployeeNo(b06Resource.getEmployeeNo());
        resource.setName(b06Resource.getName());
        resource.setChineseName(b06Resource.getChineseName());
        resource.setDem("TE");
        return resource;
    }
}
