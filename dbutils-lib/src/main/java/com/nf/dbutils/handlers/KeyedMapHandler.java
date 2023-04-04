package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 键化的Handler，是一个Map的Map
 * 内部map表示一条记录
 * 所以Map的Map就可以表示多条记录
 */
public class KeyedMapHandler extends
        AbstractKeyedHandler<Map<String,Object>> {

    @Override
    protected Integer createKey(ResultSet rs) throws SQLException {
        return Integer.valueOf(rs.getObject(1).toString());
    }

    @Override
    protected Map<String, Object> createRow(ResultSet rs) throws SQLException {
        return rowProcessor.toMap(rs);
    }

    /*private Map<Integer,Map<String, Object>> createMap(){
        return new HashMap();
    }

    private Integer createKey(ResultSet rs) throws SQLException {
       return Integer.valueOf(rs.getObject(1).toString());
    }

    private Map<String, Object> createRow(ResultSet rs) throws SQLException{
        return rowProcessor.toMap(rs);
    }*/
}
