package com.nf.dbutils.handlers;

<<<<<<< HEAD
=======
import com.nf.dbutils.ResultSetHandler;
>>>>>>> origin/master
import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
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
=======
public class BeanListHandler <T> extends AbstractResultSetHandler<List<T>> {
    private Class<?> clz;
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
>>>>>>> origin/master
