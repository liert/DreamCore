package com.github.liert.util;

import com.github.liert.Config.Settings;

import java.util.HashMap;

public class WhiteList {
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
        if (Tools.existPlayer(player)) {
            return Tools.format(Settings.I.Message.get("repeatWhiteList"), player);
        }
        String message = Tools.format(Settings.I.Message.get("haveWhiteList"), player);
        int status = isBind(player, qq);
        if (status == NEW_BIND) {
            message = Tools.format(Settings.I.Message.get("firstWhiteList"), player);
            if (!SQLQuery.execute("INSERT INTO `" + Settings.I.MySQL_Table + "` (`player`, `qq`, `daily_luck`, `count_luck`) VALUES (?, ?, 0, 0)", player, qq)) {
                message = "INSERT ERROR!!!";
            }
            return message;
        } else if (status == HAVE_BOUND) {
            message = Tools.format(Settings.I.Message.get("haveWhiteList"), Tools.getPlayer(qq));
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

    public static String delBind(String player) {
        String message = String.format("[%s]不存在", player);
        if (Tools.existPlayer(player)) {
            message = Tools.format(Settings.I.Message.get("delWhiteList"), player);
            if (!SQLQuery.execute("DELETE FROM `" + Settings.I.MySQL_Table + "` WHERE `player` = ?", player)) {
                message = "DELETE ERROR!!!";
            }
            return message;
        }
        return message;
    }
}
