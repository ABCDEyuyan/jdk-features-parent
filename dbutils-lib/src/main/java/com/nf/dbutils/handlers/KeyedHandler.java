package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 键化的Handler，是一个Map的Map
 * 内部map表示一条记录
 * 所以Map的Map就可以表示多条记录
 */
public class KeyedHandler extends
        AbstractResultSetHandler<Map<Integer,
                            Map<String,Object>>> {
    @Override
    public Map<Integer,Map<String, Object>> handle(ResultSet rs) throws SQLException {
        Map<Integer,Map<String, Object>> outer = createMap();

        while (rs.next()) {
            Integer key = createKey(rs);
            Map<String, Object> row = createRow(rs);
            outer.put(key, row);
        }
        return outer;
    }

    private Map<Integer,Map<String, Object>> createMap(){
        return new HashMap();
    }

    private Integer createKey(ResultSet rs) throws SQLException {
       return Integer.valueOf(rs.getObject(1).toString());
    }

    private Map<String, Object> createRow(ResultSet rs) throws SQLException{
        return rowProcessor.toMap(rs);
    }
}
