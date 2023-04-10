package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyedBeanHandler<K,T> extends AbstractKeyedHandler<K,T>{
    private Class<?> clz;

    public KeyedBeanHandler(Class<?> clz) {
        this(1, null, clz);
    }

    public KeyedBeanHandler(Class<?> clz,int columnIndex) {
        this(columnIndex, null, clz);
    }

    public KeyedBeanHandler(Class<?> clz,String columnName) {
        this(1, columnName, clz);
    }
    private KeyedBeanHandler(int columnIndex, String columnName, Class<?> clz) {
        super(columnIndex, columnName);
        this.clz = clz;
    }

    @Override
    protected T createRow(ResultSet rs) throws SQLException {
        return rowProcessor.toBean(rs,clz);
    }
}
