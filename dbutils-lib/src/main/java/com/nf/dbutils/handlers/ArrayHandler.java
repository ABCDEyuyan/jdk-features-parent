package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;
import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ArrayHandler extends AbstractResultSetHandler<Object[]> {

    private static final Object[] EMPTY_OBJECT = new Object[0];


    public ArrayHandler() {

    }

    public ArrayHandler(RowProcessor rowProcessor) {
        super(rowProcessor);
    }

    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
      //rs.next在这里调用，因为此类自己决定我只处理一行记录
       return rs.next()? rowProcessor.toArray(rs):EMPTY_OBJECT;

        //能不返回null的时候，可以尽量想办法不返回null
        //除非null有特殊的含义
       // return  null;


    }

}
