package com.nf.demo.dao;

import com.nf.demo.entity.UserEntity;

public interface UserDao {
    boolean login(UserEntity userEntity);
}
