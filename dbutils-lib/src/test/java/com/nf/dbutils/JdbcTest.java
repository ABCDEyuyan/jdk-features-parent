package com.nf.dbutils;

import com.nf.dbutils.handlers.*;
import org.junit.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 此测试类是用来了解jdbc驱动对getObject与具体的getXxx方法实现的联系与区别
 */
public class JdbcTest {


    @Test
    public void testGetString() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");
        String sql = "select id,uname from t5 ";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            String unameString = resultSet.getString(2);
            System.out.println("getString = " + unameString);
        }

    }

    @Test
    public void testGetObject() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");
        String sql = "select id,uname from t5 ";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            Object o = resultSet.getObject(2);
            System.out.println("getObject = " + o);
        }

    }




}