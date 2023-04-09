package com.nf.dbutils.handlers;

import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 此类是一个简单的实现，以实体类的属性优先
 * @param <T>
 */
@Deprecated
public class BeanPropertyFirstHandler<T> extends AbstractResultSetHandler<T>{
    private Class<?> clz ;

    public BeanPropertyFirstHandler(Class<?> clz) {
        this.clz = clz;
    }

    public BeanPropertyFirstHandler(RowProcessor rowProcessor, Class<?> clz) {
        super(rowProcessor);
        this.clz = clz;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next()?rowProcessor.toBean(rs, this.clz):null;
    }
}
