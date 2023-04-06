package com.nf.dbutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类负责处理一行记录，不要挂羊头卖狗肉，写成可以处理多行
 * 所以，这个类不需要出现rs.next的代码
 */
public interface RowProcessor {
    default Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        Object[] row = new Object[cols];

        for (int i = 0; i < cols; i++) {
            //取resultset的数据时，序号是从1开始的，
            // 平时我们都是写sql语句中的字段名(select id,name from emp)
            row[i] = rs.getObject(i + 1);
        }
        return row;
    }

    default Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            //取列名的逻辑优先是取别名

            String columnName = metaData.getColumnLabel(i);
            if (columnName == null || columnName.isEmpty()) {
                columnName = metaData.getColumnName(i);
            }
            map.put(columnName, rs.getObject(i));
        }

        return map;

    }


    default <T> T toBean(ResultSet rs,Class<?> clz) throws SQLException {
        T instance = null;
        try {
            instance =(T) clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = rs.getObject(fieldName);
            try {
                field.set(instance,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            field.setAccessible(false);
        }

        return instance;

    }
}
