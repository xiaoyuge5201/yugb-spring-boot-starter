package dao;

import bean.RequestLog;
import org.springframework.stereotype.Repository;
import util.JdbcClient;

import java.sql.*;

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
                "\t`create_man`,\n" +
                "\t`deleted`,\n" +
                "\t`user_id`,\n" +
                "\t`operater_username`,\n" +
                "\t`operater_type`,\n" +
                "\t`log_type`,\n" +
                "\t`name`,\n" +
                "\t`remote_addr`,\n" +
                "\t`request_uri`,\n" +
                "\t`method`,\n" +
                "\t`params`,\n" +
                "\t`exception`\n" +
                ")\n" +
                "VALUES(?,?,?, ?, ?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            conn = JdbcClient.getConnection();
            prep = conn.prepareStatement(sql);
            prep.setDate(1, (Date) requestLog.getCreate_date());
            prep.setString(2, requestLog.getCreate_man());
            prep.setBoolean(3, false);
            prep.setInt(4, requestLog.getUser_id());
            prep.setString(5, requestLog.getOperater_username());
            prep.setString(6, requestLog.getOperater_type());
            prep.setString(7, requestLog.getLog_type());
            prep.setString(8, requestLog.getName());
            prep.setString(9, requestLog.getRemote_addr());
            prep.setString(10, requestLog.getRequest_uri());
            prep.setString(11, requestLog.getMethod());
            prep.setString(12, requestLog.getParams());
            prep.setString(13, requestLog.getException());
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcClient.close(conn, prep, rs);
        }
    }
}
