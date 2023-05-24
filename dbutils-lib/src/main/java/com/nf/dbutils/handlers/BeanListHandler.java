package com.nf.dbutils.handlers;


import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BeanListHandler
 * @Author ZL
 * @Date 2023/5/9 8:52
 * @Version 1.0
 * @Explain
 **/
public class BeanListHandler<T> extends AbstractResultSetHandler<List<T>>{
    private Class<?> cls;

    public BeanListHandler(Class<?> cls) {
        this.cls = cls;
    }

    public BeanListHandler(RowProcessor rowProcessor, Class<?> cls) {
        super(rowProcessor);
        this.cls = cls;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> result=new ArrayList<>();
        while (rs.next()) {
            result.add(this.rowProcessor.toBean(rs,cls));
        }
        return result;
    }
}
