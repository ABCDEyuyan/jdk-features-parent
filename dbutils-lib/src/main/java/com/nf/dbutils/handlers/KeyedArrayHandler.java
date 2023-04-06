package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 键化的Handler，是一个Map的Map
 * 内部map表示一条记录
 * 所以Map的Map就可以表示多条记录
 */
public class KeyedArrayHandler<K> extends AbstractKeyedHandler<K,Object[]> {

    public KeyedArrayHandler() {
        this(1,null);
    }

    public KeyedArrayHandler(int columnIndex) {
        this(columnIndex, null);
    }

    public KeyedArrayHandler(String columnName) {
        this(1, columnName);
    }

    public KeyedArrayHandler(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        if (columnName != null && columnName.isEmpty() == false) {
            return (K)rs.getObject(this.columnName);
        }

        return (K)rs.getObject(this.columnIndex);
    }

    @Override
    protected Object[] createRow(ResultSet rs) throws SQLException {
        return rowProcessor.toArray(rs);
    }

}
