package com.github.liert.SQL;

import com.github.liert.Config.Settings;

import java.sql.*;

public class SQLQuery {
    public static String ip = Settings.I.MySQL_IP;
    public static String port = Settings.I.MySQL_port;
    public static String database = Settings.I.MySQL_database;
    public static String table = Settings.I.MySQL_Table;
    public static String user = Settings.I.MySQL_user;
    public static String password = Settings.I.MySQL_password;

    public static final int NEW_BIND = 0;
    public static final int HAVE_BOUND = 1;
    public static final int RE_BIND = 2;

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
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (" + "`qq` varchar(255) NOT NULL," + "`player` varchar(255) NOT NULL," + "`is_bind` int(10) UNSIGNED NOT NULL," + "PRIMARY KEY (`qq`)" + ");");
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
    }

    public static String getPlayer(String qq) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "NULL";
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + table + "`WHERE `qq` = ?");
            ps.setString(1, qq);
            rs = ps.executeQuery();
            if (rs.next()) {
                String resultPlayer = rs.getString("player");
                status = resultPlayer;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return status;
    }

    public static boolean existPlayer(String qq, String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + table + "`WHERE `player` = ?");
            ps.setString(1, player);
            rs = ps.executeQuery();
            if (rs.next()) {
                String resultQQ = rs.getString("qq");
                if (!qq.equals(resultQQ)) {
                    status = true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return status;
    }

    public static int isBind(String qq, String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int status = NEW_BIND;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + table + "`WHERE `qq` = ?");
            ps.setString(1, qq);
            rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("player");
                if (player.equals(name)){
                    status = HAVE_BOUND;
                } else {
                    status = RE_BIND;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return status;
    }

    public static String bind(String qq, String player) {
        if (existPlayer(qq, player)) {
            return String.format("%s 名称重复", player);
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String message = String.format("已绑定 %s", player);
        int status = isBind(qq, player);
        if (status == NEW_BIND) {
            message = String.format("绑定 %s", player);
            try {
                conn = SQLQuery.getConnection();
                ps = conn.prepareStatement("INSERT INTO `" + table + "` (`qq`, `player`, `is_bind`) VALUES (?, ?, ?)");
                ps.setString(1, qq);
                ps.setString(2, player);
                ps.setInt(3, 1);
                ps.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
                message = "INSERT 错误";
            }
            finally {
                SQLQuery.free(rs, ps, conn);
            }
            return message;
        } else if (status == RE_BIND) {
            message = String.format("已绑定 %s", getPlayer(qq));
//            try {
//                conn = SQLQuery.getConnection();
//                ps = conn.prepareStatement("UPDATE `" + table + "` SET `player`=?, `is_bind`=? WHERE (`qq`=?)");
//                ps.setString(1, player);
//                ps.setInt(2, 1);
//                ps.setString(3, qq);
//                ps.execute();
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//                message = "UPDATE 错误";
//            }
//            finally {
//                SQLQuery.free(rs, ps, conn);
//            }
            return message;
        } else {
            return message;
        }
    }

    public static String delBind(String qq, String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String message = String.format("解除绑定 %s", player);
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + table + "`WHERE `player` = ?");
            ps.setString(1, player);
            rs = ps.executeQuery();
            if (rs.next()) {
                conn = SQLQuery.getConnection();
                ps = conn.prepareStatement("DELETE FROM `" + table + "` WHERE (`player`=?)");
                ps.setString(1, player);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
    public static boolean existWhitelist(String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        try {
            conn = SQLQuery.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + table + "`WHERE `player` = ?");
            ps.setString(1, player);
            rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("player");
                if (player.equals(name)){
                        status = true;
                    }
                }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            SQLQuery.free(rs, ps, conn);
        }
        return status;
    }
}
