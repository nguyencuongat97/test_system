package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "test_resource_group")
public class TestResourceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "group_name")
    private String resourceGroupName;

    @Column(name = "group_level")
    private int groupLevel;

    @Column(name = "icivet_group_id")
    private Integer icivetGroupId;

    @Deprecated
    @Column(name = "app_key")
    private String appKey;

    @Deprecated
    @Column(name = "owner")
    private String identity;

    public String getUniqueKey() {
        return factory + '_' + sectionName + '_' + resourceGroupName + '_' + groupLevel;
    }
}
