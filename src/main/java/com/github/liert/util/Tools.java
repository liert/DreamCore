package com.github.liert.util;

import com.github.liert.Config.Settings;

import java.util.HashMap;

public class Tools {

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
