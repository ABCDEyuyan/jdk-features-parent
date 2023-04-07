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
     Object[] toArray(ResultSet rs) throws SQLException ;

     Map<String, Object> toMap(ResultSet rs) throws SQLException ;

     <T> T toBean(ResultSet rs,Class<?> clz) throws SQLException ;

    @Deprecated
    <T> T toBeanDeprecated(ResultSet rs,Class<?> clz) throws SQLException ;

}
