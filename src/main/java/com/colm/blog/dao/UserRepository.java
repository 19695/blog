package com.colm.blog.dao;

import com.colm.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Colm on 2020/10/19
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndAndPassword(String username, String password);
}
