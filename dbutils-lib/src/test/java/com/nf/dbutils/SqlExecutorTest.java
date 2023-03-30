package com.nf.dbutils;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SqlExecutorTest {

    @Test
    public void update() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root")
            ;

            SqlExecutor executor = new SqlExecutor();
            String sql = "delete from t5 where id = 1";
            executor.update(connection, false, sql);


        connection.close();
        //因为没要让update方法帮我们关（false），所以我们自己要关连接
    }

    @Test
    public void updateAutoClose() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root")
                ;

        SqlExecutor executor = new SqlExecutor();
        String sql = "delete from t5 where id = 4";
        executor.update(connection , sql);

    }
}