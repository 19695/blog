package com.colm.blog.service;

import com.colm.blog.dao.UserRepository;
import com.colm.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Colm on 2020/10/19
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndAndPassword(username, password);
        return user;
    }
}
