package com.nf.dao;

import com.nf.entity.UserEntity;

public interface LoginDao {
    boolean login(UserEntity userEntity);
}
