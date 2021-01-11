package com.github.yugb.util;

import com.github.yugb.config.LogConnectionProperties;

import java.sql.*;

public class JdbcClient {

    private static Connection connection = null;
    /**
     * 连接参数
     */
    protected static LogConnectionProperties properties;

    public LogConnectionProperties getProperties() {
        return properties;
    }

    public void setProperties(LogConnectionProperties properties) {
        this.properties = properties;
    }

    public JdbcClient() {
    }

    public JdbcClient(LogConnectionProperties properties) {
        this.properties = properties;
    }

    /**
     * 构造实例
     *
     * @return 返回结果
     */
    public static JdbcClient create(LogConnectionProperties properties) {
        JdbcClient jdbcClient = new JdbcClient(properties);
        return jdbcClient;
    }

    /**
     * 获取连接
     * @return 返回结果 连接
     */
    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName(properties.getDriverClassName());
                // 获取连接
                connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    /**
     * 关闭流
     *
     * @param conn
     * @param stmt
     * @param rs
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
