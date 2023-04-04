package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类外层是固定，就是一个map
 * 其子类是确定内部一条记录的类型，可以是bean，Object[],Map
 */
public abstract class AbstractKeyedHandler<V>
        extends AbstractResultSetHandler<Map<Integer,V>>{
    @Override
    public Map<Integer,V> handle(ResultSet rs) throws SQLException {
        Map<Integer,V> outer = createMap();

        while (rs.next()) {
            Integer key = createKey(rs);
            V row = createRow(rs);
            outer.put(key, row);
        }
        return outer;
    }

    /**
     * java里面的方法，默认就是虚方法，就是子类可以重写
     * 如果方法加了final关键字，子类就不能重写，就不是虚方法
     * @return
     */
    protected  Map<Integer,V> createMap(){
        return new HashMap<>();
    }

    protected abstract Integer createKey(ResultSet rs) throws SQLException;

    protected abstract V createRow(ResultSet rs) throws SQLException;
}
