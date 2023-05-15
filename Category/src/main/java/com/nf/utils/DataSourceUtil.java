package com.nf.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName DataSourceUtil
 * @Author ZL
 * @Date 2023/5/9 8:45
 * @Version 1.0
 * @Explain
 **/
public class DataSourceUtil {
    private static DataSource dataSource=null;
    static {
        InputStream inputStream=DataSourceUtil.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties=new Properties();
        try{
            properties.load(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            dataSource= DruidDataSourceFactory.createDataSource(properties);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static DataSource getDataSource(){return dataSource;}
}
