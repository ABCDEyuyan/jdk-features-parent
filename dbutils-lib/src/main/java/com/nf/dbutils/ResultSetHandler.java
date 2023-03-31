package com.nf.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler <T>{
    /**
     * @param rs
     * @return
     */
    T handle(ResultSet rs) throws SQLException;
}
