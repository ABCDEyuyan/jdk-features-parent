package com.nf.dao.Impl;

import com.nf.dao.LoginDao;
import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.ScalarHandler;
import com.nf.entity.UserEntity;
import com.nf.utils.DataSourceUtil;

import javax.sql.DataSource;

/**
 * @ClassName LoginDaoImpl
 * @Author ZL
 * @Date 2023/5/12 9:16
 * @Version 1.0
 * @Explain
 **/
public class LoginDaoImpl implements LoginDao {

    @Override
    public boolean login(UserEntity userEntity) {
        DataSource dataSource = DataSourceUtil.getDataSource();
        SqlExecutor sqlExecutor = new SqlExecutor(dataSource);
        String sql = "select passwrod from user where ?";
        Integer password = sqlExecutor.query(sql, new ScalarHandler<Integer>("passwrod"), userEntity.getName());
        return password.equals(userEntity.getPassword());
    }
}
