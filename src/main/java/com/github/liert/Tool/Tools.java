package com.github.liert.Tool;

import com.github.liert.Config.Settings;
import com.github.liert.SQL.SQLQuery;

import java.util.HashMap;

public class Tools {
    private static final int NEW_BIND = 0;
    private static final int HAVE_BOUND = 1;
    private static final int CHANGE_QQ = 2;

    public static int isBind(String player, String qq) {
        HashMap<String, Object> result;
        if (qq.equals("")) {
            result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `player` = ?", player);
        } else {
            result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `player` = ? OR `qq` = ?", player, qq);
        }
        int status = NEW_BIND;
        if ((boolean) result.get("status")) {
            String name = (String) result.get("qq");
            if (name.equals("") && !qq.equals("")) {
                status = CHANGE_QQ;
            }
            else if (name.equals(qq)){
                status = HAVE_BOUND;
            }
        }
        return status;
    }
    public static String bind(String player) {
        return bind(player, "");
    }
    public static String bind(String player, String qq) {
        if (existPlayer(player)) {
            return format(Settings.I.Message.get("repeatWhiteList"), player);
        }
        String message = format(Settings.I.Message.get("haveWhiteList"), player);
        int status = isBind(player, qq);
        if (status == NEW_BIND) {
            message = format(Settings.I.Message.get("firstWhiteList"), player);
            if (!SQLQuery.execute("INSERT INTO `" + Settings.I.MySQL_Table + "` (`player`, `qq`, `daily_luck`, `count_luck`) VALUES (?, ?, 0, 0)", player, qq)) {
                message = "INSERT ERROR!!!";
            }
            return message;
        } else if (status == HAVE_BOUND) {
            message = format(Settings.I.Message.get("haveWhiteList"), getPlayer(qq));
            return message;
        } else if (status == CHANGE_QQ){
            message = String.format("[%s]的QQ绑定至[%s]", player, qq);
            if (!SQLQuery.executeUpdate("UPDATE `" + Settings.I.MySQL_Table + "` SET `qq` = ? WHERE `player` = ?", qq, player)) {
                message = "UPDATE ERROR!!!";
            }
            return message;
        }
        return message;
    }

    public static String getPlayer(String qq) {
        HashMap<String, Object> result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `qq` = ?", qq);
        return (boolean) result.get("status") ? (String) result.get("player") : "NULL";
    }

    public static boolean existPlayer(String player) {
        HashMap<String, Object> result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `player` = ?", player);
        return (boolean) result.get("status");
    }

//    public static boolean existQQ(String qq) {
//        HashMap<String, Object> result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `qq` = ?", qq);
//        return (boolean) result.get("status");
//    }

    public static String delBind(String player) {
        String message = String.format("[%s]不存在", player);
        if (existPlayer(player)) {
            message = format(Settings.I.Message.get("delWhiteList"), player);
            if (!SQLQuery.execute("DELETE FROM `" + Settings.I.MySQL_Table + "` WHERE `player` = ?", player)) {
                message = "DELETE ERROR!!!";
            }
            return message;
        }
        return message;
    }

    public static int[] getLuckData(String player) {
        int[] data = new int[2];
        HashMap<String, Object> result = SQLQuery.executeQuery("SELECT * FROM `" + Settings.I.MySQL_Table + "`WHERE `player` = ?", player);
        if (existPlayer(player)) {
            data[0] = (int) result.get("daily_luck");
            data[1] = (int) result.get("count_luck");
        }
        return data;
    }

    public static void dailyAdd(String player, int[] luckData) {
        luckData[1] = ++luckData[1] >= Settings.I.VeryLuck ? 0 : luckData[1];
        SQLQuery.executeUpdate("UPDATE `" + Settings.I.MySQL_Table + "` SET `daily_luck` = ?, `count_luck` = ? WHERE `player` = ?", ++luckData[0], luckData[1], player);
    }

    public static String format(String string, String value) {
        string = string.replace("%player%", "%s");
        return String.format(string, value);
    }
}
