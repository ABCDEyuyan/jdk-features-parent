package com.nf.dbutils;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SqlExecutorTest {

    @Test
    public void update() throws SQLException {
        //使用try with resource语法帮我们自动调用connection的close方法关闭资源，
        // update方法本身没有做关闭Connection的动作
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root")) {
            SqlExecutor executor = new SqlExecutor();
            String sql = "delete from t5 where id = 1";
            executor.update(connection,  sql);

            //因为没要让update方法帮我们关（false），所以我们自己要关连接
            //connection.close();
        }
    }

    @Test
    public void updateAutoClose() throws SQLException {

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root");

        SqlExecutor executor = new SqlExecutor();
        String sql = "delete from t5 where id = 4";
        executor.update(connection , sql);

    }
}