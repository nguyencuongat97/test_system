package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "re_cl_users")
public class Repair8sUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`user_id`")
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "id_factory")
    private Integer idFactory;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
