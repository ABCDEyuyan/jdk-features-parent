package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ArrayHandler implements ResultSetHandler<Object[]> {
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
        return  null;
    }
}
