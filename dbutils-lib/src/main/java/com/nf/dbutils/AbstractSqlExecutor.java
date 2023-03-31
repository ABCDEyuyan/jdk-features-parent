package com.nf.dbutils;

import javax.sql.DataSource;
import java.sql.*;

public abstract class AbstractSqlExecutor {

    /**
     * 子类不要直接使用此字段，应该用getDataSource方法来获取字段
     */
    protected DataSource dataSource;

    /**
     * 无参的构造函数是用户提供Connetion的方式来使用我们的库
     */
    public AbstractSqlExecutor() {
    }

    public AbstractSqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
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
            return  getDataSource().getConnection();
        } catch (SQLException e) {
            throw new DaoException("从dataSource创建Connection失败",e);
        }
    }

    protected void fillStatement(PreparedStatement stmt, Object... params) throws SQLException{
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

    protected void checkSql(Connection conn, boolean closeConn, String sql) {
        //执行到下面的代码时，已经表示conn不为null
        if (sql == null || sql.trim().length() == 0) {
           //此时此刻，连接是有得，sql没有，所以执行crud是没有意义的
            //问closeConn是不是true，需要我们这个方法帮你关闭吗？
            if (closeConn) {
                ResourceCleanerUtils.closeQuietly(conn);
            }
            throw new DaoException("sql语句不能是空的");
        }
    }

    protected void checkConnetion(Connection conn) {
        //下面的代码有一个专门的称呼：guard code（守护代码），先写这些guard代码
        if (conn == null) {
            throw new DaoException("要有一个连接");
        }
    }

    protected void checkResultSetHandler(ResultSetHandler handler,boolean closeConn,Connection conn) {
        if (handler == null) {
            if (closeConn) {
                ResourceCleanerUtils.closeQuietly(conn);
            }
            throw new DaoException("resultSet handler必须要有，查询结果才能处理");
        }
    }

    protected void close(ResultSet resultSet) throws SQLException {
        ResourceCleanerUtils.closeQuietly(resultSet);
    }

    protected void close(Connection conn) throws SQLException {
        ResourceCleanerUtils.closeQuietly(conn);
    }

    protected void close(Statement stmt) throws SQLException {
        ResourceCleanerUtils.closeQuietly(stmt);
    }
}
