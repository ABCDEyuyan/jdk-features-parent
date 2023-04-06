package com.nf.dbutils.handlers;

import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeanListHandler<T> extends AbstractResultSetHandler<List<T>>{
    private Class<?> clz ;

    public BeanListHandler(Class<?> clz) {
        this.clz = clz;
    }

    public BeanListHandler(RowProcessor rowProcessor, Class<?> clz) {
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
