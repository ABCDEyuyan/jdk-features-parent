package com.nf.dbutils.handlers.properties;

import com.nf.dbutils.PropertyHandler;

import java.sql.Date;
import java.time.LocalDate;

/**
 * 默认情况下数据库返回的日期类型是java.sql.Date(此类是java.util.Date的子类)，
 * 它是不能转换为LocalDate的，所以添加一个PropertyHandler的实现，
 * 把sql.Date转换为LocalDate
 */
public class LocalDatePropertyHandler implements PropertyHandler {
    @Override
    public boolean support(Class<?> clz, Object value) {
        return value instanceof Date && LocalDate.class.isAssignableFrom(clz);
    }

    @Override
    public Object apply(Class<?> clz, Object value) {
        return ((Date)value).toLocalDate();
    }
}
