package com.nf.dbutils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 类似于QueryRunner的一个类，客户用此类完成crud
 */
public class SqlExecutor {
    private DataSource dataSource;

    /**
     * 无参的构造函数意味着不使用DataSource
     */
    public SqlExecutor() {
    }

    /**
     * 意味着要使用DataSource
     * @param dataSource
     */
    public SqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(Connection conn, String sql, Object... params) {
        return this.update(conn, false, sql, params);
    }

    public int update( String sql, Object... params) {
        Connection conn = prepareConnection();
        return this.update(conn, true, sql, params);
    }


    private int update(Connection conn, boolean closeConn, String sql, Object... params) {
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
            throw new DaoException("sql语句不能是空的");
        }

        int rows =0;

        PreparedStatement pstmt = null;
        try {
            //conn.prepareStatement(sql)
            //pstmt = this.prepareStatement(conn,sql);

            pstmt = conn.prepareStatement(sql);

            //设置参数//pstmt.setint,pstmt.setString //
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

    /**
     * 这里搞一个方法的作用是，可以让子类重写此方法，以便对DataSource
     * 返回的Connection进行一些额外的设置
     *
     * protected修饰符是为了给子类重写，不需要搞成public
     * @return
     */
    protected Connection prepareConnection() {
        try {
            return  this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException("从dataSource创建Connection失败",e);
        }
    }
}
