package com.nf.demo.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.nf.dbutils.SqlExecutor;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class SqlExecutorUtils {
    private static DataSource dataSource ;
    static {

        try {
            InputStream inputStream = SqlExecutorUtils.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new IllegalStateException("无法正确创建dataSource对象");
        }

    }
    public static SqlExecutor getExecutor(){
        return new SqlExecutor(dataSource);
    }
}
