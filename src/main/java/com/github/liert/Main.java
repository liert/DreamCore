package com.github.liert;

import com.github.liert.Config.ConfigurationLoader;
import com.github.liert.Config.Settings;
import com.github.liert.SQL.SQLQuery;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    static Ws ws;
    static Main main;
    public static int reCount = 0;
    @Override
    public void onEnable() {
        this.loadConfig();
        SQLQuery.createTable();
        if (Settings.I.Koishi) {
            ws = Ws.connectKoishi();
            Ws.startHeartbeat();
        }
        main = this;
        Bukkit.getPluginCommand("wl").setExecutor(new Commands());
        Bukkit.getPluginCommand("luck").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new Listens(), this);
//        this.getServer().getConsoleSender().sendMessage("-----***** Debug *****-----");
//        this.getServer().getConsoleSender().sendMessage("-----***** End *****-----");
    }

    public void loadConfig() {
        ConfigurationLoader.loadYamlConfiguration(this, Settings.class, true);
        this.saveDefaultConfig();
        this.reloadConfig();
    }
    public static Ws getWs() {
        return ws;
    }
    public static Main getInstance() {
        return main;
    }
    public static void reconnect() {
        ws = Ws.connectKoishi();
    }

    @Override
    public void onDisable() {
    }
}
