package com.nf.dbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 类似于QueryRunner的一个类，客户用此类完成crud
 */
public class SqlExecutor {

    public int update(Connection conn, String sql, Object... params) {
        return this.update(conn, true, sql, params);
    }

    public int update(Connection conn, boolean closeConn, String sql, Object... params) {
        //下面的代码有一个专门的称呼：guard code（守护代码），先写这些guard代码
        if (conn == null) {
            throw new DaoException("要有一个连接");
        }

        //执行到下面的代码时，已经表示conn不为null
        if (sql == null || sql.trim().length() == 0) {
           //此时此刻，连接是有得，sql没有，所以执行crud是没有意义的
            //问closeConn是不是true，需要我们这个方法帮你关闭吗？
            if (closeConn) {
                ResourceCleanerUtils.closeQuietly(conn);
            }
        }

        int rows =0;

        PreparedStatement pstmt = null;
        try {
            //conn.prepareStatement(sql)
            //pstmt = this.prepareStatement(conn,sql);

            pstmt = conn.prepareStatement(sql);
            //设置参数
            //pstmt.setint,pstmt.setString //
            this.fillStatement(pstmt,params);
            rows = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("sql执行失败",e);
        }finally {
            ResourceCleanerUtils.closeQuietly(pstmt);
            if (closeConn) {
                ResourceCleanerUtils.closeQuietly(conn);
            }
        }

        return  rows;
    }

    private void fillStatement(PreparedStatement stmt, Object... params) throws SQLException{
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i+1,params[i]);
            }else{
                stmt.setNull(i+1, Types.VARCHAR);
            }
            
        }


    }
}
