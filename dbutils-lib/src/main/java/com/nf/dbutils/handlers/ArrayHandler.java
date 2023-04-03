package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ArrayHandler implements ResultSetHandler<Object[]> {

    private static final Object[] EMPTY_OBJECT = new Object[0];

    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        if(rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            Object[] result = new Object[count];

            for (int i = 0; i < count; i++) {
                //取resultset的数据时，序号是从1开始的，
                // 平时我们都是写sql语句中的字段名(select id,name from emp)
                result[i] = rs.getObject(i + 1);
            }
            return result;
        }
        //能不返回null的时候，可以尽量想办法不返回null
        //除非null有特殊的含义
       // return  null;

        return EMPTY_OBJECT;
    }
}
