package com.nf.dao.Impl;

import com.nf.dao.LoginDao;
import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.BeanHandler;
import com.nf.dbutils.handlers.ScalarHandler;
import com.nf.entity.UserEntity;
import com.nf.utils.DataSourceUtil;

/**
 * @ClassName LoginDaoImpl
 * @Author ZL
 * @Date 2023/5/12 9:16
 * @Version 1.0
 * @Explain
 **/
public class LoginDaoImpl implements LoginDao {
    SqlExecutor sqlExecutor = new SqlExecutor(DataSourceUtil.getDataSource());
    @Override
    public boolean login(UserEntity userEntity) {
        String sql = "select count(*) from user where name = ? and password = ?";
        Long password = sqlExecutor.query(sql, new ScalarHandler<Long>(), userEntity.getName(),userEntity.getPassword());
        return password==1;
    }
}
