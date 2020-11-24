package com.foxconn.fii.data.b06ds03.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(schema = "RE", name = "STORE_SN_RE_B6_USER")
@IdClass(B06User.B06UserId.class)
public class B06User {

    @Id
    @Column(name = "USERID")
    private String user;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "CHINANAME")
    private String chinaName;

    @Column(name = "SHIFT")
    private String shift;

    @Data
    public static class B06UserId implements Serializable{
        private String user;
    }
}
