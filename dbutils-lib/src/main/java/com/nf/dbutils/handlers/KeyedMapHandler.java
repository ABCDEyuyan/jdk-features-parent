package com.nf.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 键化的Handler，是一个Map的Map
 * 内部map表示一条记录
 * 所以Map的Map就可以表示多条记录
 */
public class KeyedMapHandler<K> extends
        AbstractKeyedHandler<K,Map<String,Object>> {


    public KeyedMapHandler() {
        this(1, null);
    }

    public KeyedMapHandler(int columnIndex) {
        this(columnIndex, null);
    }

    public KeyedMapHandler(String columnName) {
        this(1, columnName);
    }

    /**
     * 在java，c# 语言中，实例化一个对象的时候，是一定会调用此类的构造函数的
     *
     * 每一个类的构造函数执行的时候，是会预先调用父类的默认构造函数的
     *
     * 如果父类没有默认构造函数，那么就需要你手动的指定调用父类的哪一个构造函数
     * 必须先调用父类的构造函数之后再写本类的初始化逻辑
     * @param columnIndex
     * @param columnName
     */
    private KeyedMapHandler(int columnIndex, String columnName) {
       super(columnIndex, columnName);
    }

    @Override
    protected Map<String, Object> createRow(ResultSet rs) throws SQLException {
        return this.rowProcessor.toMap(rs);
    }

}
