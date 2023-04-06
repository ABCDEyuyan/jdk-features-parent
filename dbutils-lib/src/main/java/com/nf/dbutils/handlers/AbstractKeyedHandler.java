package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类外层是固定，就是一个map
 * 其子类是确定内部一条记录的类型，可以是bean，Object[],Map
 */
public abstract class AbstractKeyedHandler<K,V>
        extends AbstractResultSetHandler<Map<K,V>>{
    protected int columnIndex = 1 ;
    protected String columnName;

  /*  public AbstractKeyedHandler() {

    }*/
    public AbstractKeyedHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    @Override
    public Map<K,V> handle(ResultSet rs) throws SQLException {
        Map<K,V> outer = createMap();

        while (rs.next()) {
            K key = createKey(rs);
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
    protected  Map<K,V> createMap(){
        return new HashMap<>();
    }

    protected K createKey(ResultSet rs) throws SQLException {
        if (columnName != null && columnName.isEmpty() == false) {
            return (K)rs.getObject(this.columnName);
        }

        return (K)rs.getObject(this.columnIndex);
    }

    protected abstract V createRow(ResultSet rs) throws SQLException;
}
