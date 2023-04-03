package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapHandler implements ResultSetHandler<Map<String,Object>> {
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        if (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();

            for (int i = 1; i <= cols; i++) {
                //取列名的逻辑优先是取别名

                String columnName = metaData.getColumnLabel(i);
                if (columnName == null || columnName.isEmpty()) {
                    columnName = metaData.getColumnName(i);
                }
                result.put(columnName, rs.getObject(i));
            }

            return result;
        }

        return  null;
    }
}
