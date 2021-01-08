package com.github.yugb.dao;

import com.github.yugb.bean.RequestLog;
import com.github.yugb.util.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 源数据源的Dao操作
 */
@Repository
public class RequestLogDao {
    /**
     * 保存
     *
     * @param requestLog
     */
    public void save(RequestLog requestLog) {
        String sql = "INSERT INTO `t_log` (\n" +
                "\t`create_date`,\n" +
                "\t`username`,\n" +
                "\t`operater_type`,\n" +
                "\t`log_type`,\n" +
                "\t`module`,\n" +
                "\t`description`,\n" +
                "\t`remote_addr`,\n" +
                "\t`request_uri`,\n" +
                "\t`method`,\n" +
                "\t`params`,\n" +
                "\t`exception`\n" +
                ")\n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            conn = JdbcClient.getConnection();
            prep = conn.prepareStatement(sql);
            prep.setString(1, requestLog.getCreateDate());
            prep.setString(2, requestLog.getUsername());
            prep.setString(3, requestLog.getOperatorType());
            prep.setString(4, requestLog.getLogType());
            prep.setString(5, requestLog.getModule());
            prep.setString(6, requestLog.getDescription());
            prep.setString(7, requestLog.getRemoteAddr());
            prep.setString(8, requestLog.getRequestUri());
            prep.setString(9, requestLog.getMethod());
            prep.setString(10, requestLog.getParams());
            prep.setString(11, requestLog.getException());
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcClient.close(conn, prep, rs);
        }
    }
}
