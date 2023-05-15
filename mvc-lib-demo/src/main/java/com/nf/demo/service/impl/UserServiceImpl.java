package com.nf.demo.service.impl;

import com.nf.demo.dao.UserDao;
import com.nf.demo.dao.impl.UserDaoImpl;
import com.nf.demo.entity.UserEntity;
import com.nf.demo.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    @Override
    public boolean login(UserEntity userEntity) {
        return userDao.login(userEntity);
    }
}
