package com.nf.demo.dao.impl;

import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.ScalarHandler;
import com.nf.demo.dao.UserDao;
import com.nf.demo.entity.UserEntity;
import com.nf.demo.util.SqlExecutorUtils;

public class UserDaoImpl implements UserDao {
    SqlExecutor sqlExecutor = SqlExecutorUtils.getExecutor();

    @Override
    public boolean login(UserEntity userEntity) {
        String sql="select count(*) from userinfo where username=? and pwd=? ";
        Long result = sqlExecutor.query(sql, new ScalarHandler<Long>(), userEntity.getUsername(), userEntity.getPwd());
        return result==1;
    }

    public static void main(String[] args) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPwd("123");
        new UserDaoImpl().login(userEntity);
    }
}
