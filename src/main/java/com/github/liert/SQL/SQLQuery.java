package com.github.liert.SQL;

import com.github.liert.Config.Settings;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;

public class SQLQuery {
    public static String ip = Settings.I.MySQL_IP;
    public static String port = Settings.I.MySQL_port;
    public static String database = Settings.I.MySQL_database;
    public static String table = Settings.I.MySQL_Table;
    public static String user = Settings.I.MySQL_user;
    public static String password = Settings.I.MySQL_password;
    public static String eventName = String.format("%s_%s_update_event", database, table);

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=false", user, password);
    }

    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null) {
                st.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (" + "`player` varchar(255) NOT NULL," + "`qq` varchar(255)," + "`daily_luck` int(10) UNSIGNED NOT NULL," + "`count_luck` int(10) UNSIGNED NOT NULL," + "PRIMARY KEY (`player`)" + ");");
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
    }

    public static boolean execute(String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
            result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return result;
    }

    public static HashMap<String, Object> executeQuery(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", false);
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                result.put("status", true);
                if (sql.contains(table)) {
                    result.put("player", rs.getString("player"));
                    result.put("qq", rs.getString("qq"));
                    result.put("daily_luck", rs.getInt("daily_luck"));
                    result.put("count_luck", rs.getInt("count_luck"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
//        Bukkit.getConsoleSender().sendMessage(String.valueOf(result));
        return result;
    }

    public static boolean executeUpdate(String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
            result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return result;
    }

    public static void dailyTask() {
        HashMap<String, Object> result = SQLQuery.executeQuery("SELECT * FROM information_schema.events WHERE event_name = ?", eventName);
        if (!(boolean) result.get("status")) {
            executeUpdate("SET GLOBAL event_scheduler = ON");
            String createEventSQL = "CREATE EVENT %s ON SCHEDULE EVERY 1 DAY "
                    + "STARTS DATE_ADD(CURDATE(), INTERVAL 1 DAY) "
                    + "DO BEGIN "
                    + "UPDATE %s SET daily_luck = 0; "
                    + "END";
            createEventSQL = String.format(createEventSQL, eventName, table);
            executeUpdate(createEventSQL);
        }
    }
}
