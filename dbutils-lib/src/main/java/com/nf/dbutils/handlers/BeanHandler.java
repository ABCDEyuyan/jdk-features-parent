package com.nf.dbutils.handlers;

import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanHandler<T> extends AbstractResultSetHandler<T>{
    private Class<?> clz ;

    public BeanHandler(Class<?> clz) {
        this.clz = clz;
    }

    public BeanHandler(RowProcessor rowProcessor, Class<?> clz) {
        super(rowProcessor);
        this.clz = clz;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next()?rowProcessor.toBean(rs, this.clz):null;
    }
}
