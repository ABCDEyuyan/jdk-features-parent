package com.nf.dbutils.handlers;

import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BeanPropertyFirstListHandler<T> extends AbstractResultSetHandler<List<T>>{
    private Class<?> clz ;

    public BeanPropertyFirstListHandler(Class<?> clz) {
        this.clz = clz;
    }

    public BeanPropertyFirstListHandler(RowProcessor rowProcessor, Class<?> clz) {
        super(rowProcessor);
        this.clz = clz;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rowProcessor.toBean(rs, clz));
        }
        return list;
    }
}
