package com.qianfeng.service.impl;

import com.qianfeng.dao.UserMapper;
import com.qianfeng.pojo.User;
import com.qianfeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userDao;

    @Override
    public User login(String name, String password) {
        User user = userDao.selectByName(name);
        if(user == null) {
            throw new RuntimeException("用户名错误");
        }
        if(!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");

        }
        return user;
    }
}
