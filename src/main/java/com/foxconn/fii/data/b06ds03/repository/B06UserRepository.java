package com.foxconn.fii.data.b06ds03.repository;

import com.foxconn.fii.data.b06ds03.model.B06User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface B06UserRepository extends JpaRepository<B06User, B06User.B06UserId> {

    @Query("SELECT user, userName, shift FROM B06User WHERE user LIKE 'V%' AND userName IS NOT NULL ")
    List<Object[]> findAllUserB06();

    List<B06User> findByUserLikeAndUserNameIsNotNull(String user);
}
