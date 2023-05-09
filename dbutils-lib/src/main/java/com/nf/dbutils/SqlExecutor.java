package com.nf.dbutils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 类似于QueryRunner的一个类，客户用此类完成crud
 */
public class SqlExecutor extends AbstractSqlExecutor {
    private DataSource dataSource;

    /**
     * 无参的构造函数意味着不使用DataSource
     * <p>
     * 这样写，默认就是调用父类的默认构造函数
     */
    public SqlExecutor() {
    }

    /**
     * 意味着要使用DataSource
     *
     * @param dataSource
     */
    public SqlExecutor(DataSource dataSource) {

        super(dataSource);
    }

    public int update(Connection conn, String sql, Object... params) {
        return this.update(conn, false, sql, params);
    }

    public int update(String sql, Object... params) {
        Connection conn = prepareConnection();
        return this.update(conn, true, sql, params);
    }

    private int update(Connection conn, boolean closeConn, String sql, Object... params) {
        try {
            return this.update0(conn, closeConn, sql, params);
        } catch (SQLException e) {
            throw new DaoException("执行sql出错", e);
        }
    }

    private int update0(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        checkConnetion(conn);
        checkSql(conn, closeConn, sql);

        int rows = 0;

        PreparedStatement pstmt = null;
        try {
            //conn.prepareStatement(sql)
            //pstmt = this.prepareStatement(conn,sql);

            pstmt = conn.prepareStatement(sql);

            //设置参数//pstmt.setint,pstmt.setString //
            this.fillStatement(pstmt, params);
            rows = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("sql执行失败", e);
        } finally {
            close(pstmt);
            if (closeConn) {
                close(conn);
            }
        }
        return rows;
    }

    public <T> T query(String sql, ResultSetHandler<T> handler, Object... params) {
        return query(prepareConnection(), true, sql, handler, params);
    }

    public <T> T query(Connection conn, String sql, ResultSetHandler<T> handler, Object... params) {
        return query(conn, false, sql, handler, params);
    }

    private <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return query0(conn, closeConn, sql, handler, params);
        } catch (SQLException e) {
            throw new RuntimeException("执行查询出错", e);
        }
    }

    private <T> T query0(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> handler, Object... params) throws SQLException {
        checkConnetion(conn);
        checkSql(conn, closeConn, sql);
        checkResultSetHandler(handler, closeConn, conn);

        PreparedStatement pstmt = null;
        T result = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            fillStatement(pstmt, params);
            rs = pstmt.executeQuery();
            //框架的原始代码中是有一个wrap，它里面的实现原理是动态代理，
            // 现在就暂时不处理
            result = handler.handle(rs);
        } catch (SQLException e) {
            throw new DaoException("执行查询出错", e);
        } finally {
            try {
                close(rs);
            } finally {
                close(pstmt);
                if (closeConn) {
                    close(conn);
                }
            }
        }

        return result;

    }

    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params)  {
        return insert(this.prepareConnection(), true, sql, rsh, params);
    }

    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) {
        return insert(conn, false, sql, rsh, params);
    }

    private <T> T insert(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return insert0(conn, closeConn, sql, handler, params);
        } catch (SQLException e) {
            throw new RuntimeException("执行查询出错", e);
        }
    }

    private <T> T insert0(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> handler, Object... params)
            throws SQLException {
        checkConnetion(conn);
        checkSql(conn, closeConn, sql);
        checkResultSetHandler(handler, closeConn, conn);

        PreparedStatement stmt = null;
        T generatedKeys = null;

        try {
            //1.获取自增长列的值的要点一
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            //2.获取自增长列的值的要点二
            ResultSet resultSet = stmt.getGeneratedKeys();
            //3.获取自增长列的值的要三
            generatedKeys = handler.handle(resultSet);
        } catch (SQLException e) {
            throw new DaoException(sql + "参数:" + params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }
}
