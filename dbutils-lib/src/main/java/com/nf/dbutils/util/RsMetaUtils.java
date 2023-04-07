package com.nf.dbutils.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RsMetaUtils {
    private RsMetaUtils() {
    }

    public static  String getColumnName(ResultSetMetaData rsmd,int index) throws SQLException {
        String columnName = rsmd.getColumnLabel(index);
        if (columnName == null || columnName.isEmpty()) {
            columnName = rsmd.getColumnName(index);
        }
        return columnName;
    }
}
