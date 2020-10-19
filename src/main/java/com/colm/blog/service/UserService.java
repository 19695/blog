package com.colm.blog.service;

import com.colm.blog.po.User;

/**
 * Created by Colm on 2020/10/19
 */
public interface UserService {

    User checkUser(String username, String password);
}
