package com.nf.dbutils.handlers;

import com.nf.dbutils.ResultSetHandler;
import com.nf.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapHandler extends AbstractResultSetHandler<Map<String,Object>> {
    public MapHandler() {
    }

    public MapHandler(RowProcessor rowProcessor) {
        super(rowProcessor);
    }

    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next()?rowProcessor.toMap(rs):null;
    }
}
