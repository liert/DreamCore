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
        Bukkit.getPluginManager().registerEvents(new Listens(), this);
//        this.getServer().getConsoleSender().sendMessage("-----***** Star *****-----");
//        try {
//            Class<?> commandWhitelistClass = Class.forName("net.minecraft.server.v1_12_R1.CommandWhitelist");
//            Object commandWhitelistInstance = commandWhitelistClass.newInstance();
//            Method getCommandMethod = commandWhitelistClass.getDeclaredMethod("getCommand");
//            String name = (String) getCommandMethod.invoke(commandWhitelistInstance);
//            this.getServer().getConsoleSender().sendMessage("-----***** " + name + " *****-----");
//            // 获取 CraftServer
//            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit.v1_12_R1.CraftServer");
//            Method getServerMethod = craftServerClass.getMethod("getServer");
//            Method getCommandMapMethod = craftServerClass.getMethod("getCommandMap");
//            Server bukkitServer = Bukkit.getServer();
//            Object craftServer = null;
//            if (craftServerClass.isInstance(bukkitServer)) {
//                craftServer = craftServerClass.cast(bukkitServer);
//            } else {
//                System.out.println("无法将变量转换成 CraftServer 类型的实例。");
//            }
//            Object simpleCommandMapObject = getCommandMapMethod.invoke(craftServer);
//            Class<?> simpleCommandMapClass = simpleCommandMapObject.getClass();
//            Method getCommandMethod = simpleCommandMapClass.getMethod("getCommand", String.class);
//            Command whitelist = (Command) getCommandMethod.invoke(null, "whitelist");
//            Class<?> whitelistClass = whitelist.getClass();
//            Field commandMapField = whitelistClass.getDeclaredField("commandMap");
//            commandMapField.setAccessible(true);
//            whitelist.unregister((CommandMap) simpleCommandMapObject);
//            Field knownCommandsField = simpleCommandMapClass.getDeclaredField("knownCommands");
//            knownCommandsField.setAccessible(true);
//            Map<String, Command> mapS_C = (Map<String, Command>) knownCommandsField.get(simpleCommandMapObject);
//            this.getServer().getConsoleSender().sendMessage("-----***** mapS_C *****-----");
//            Field commandMapField = Command.class.getDeclaredField("commandMap");
//            commandMapField.setAccessible(true);
//            Command whitelist = mapS_C.get("whitelist".toLowerCase(java.util.Locale.ENGLISH));
//            System.out.println(whitelist.getName());
//            CommandMap whitelistMap = (CommandMap) commandMapField.get(whitelist);
//            whitelist.unregister(whitelistMap);
//            mapS_C.remove("whitelist");
//            for (Map.Entry<String, Command> entry : mapS_C.entrySet()) {
//                String key = entry.getKey();
//                Command value = entry.getValue();
//                System.out.println("key: " + key + " --> value: " + value);
//            }
//            Object minecraftServerObject = getServerMethod.invoke(craftServer);
////            Class<?> minecraftServerClass = minecraftServerObject.getClass();
//            Class<?> minecraftServerClass = Class.forName("net.minecraft.server.v1_12_R1.MinecraftServer");
//            Field bField = minecraftServerClass.getDeclaredField("b");
//            Object commandHandlerObject = bField.get(minecraftServerObject);
//
//            Class<?> commandHandlerClass = Class.forName("net.minecraft.server.v1_12_R1.CommandHandler");
//            Field bCommandHandlerField = commandHandlerClass.getDeclaredField("b");
//            bCommandHandlerField.setAccessible(true);
//            Map<String, Object> map = (Map<String, Object>) bCommandHandlerField.get(commandHandlerObject);
//            this.getServer().getConsoleSender().sendMessage("-----***** 原始 *****-----");
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                System.out.println("key: " + key + " --> value: " + value);
//            }
//
//            this.getServer().getConsoleSender().sendMessage("-----***** 替换 *****-----");
//            for (String key : map.keySet()) {
//                if (key.contains("whitelist")){
//                    Object o = map.get(key);
//                    System.out.println(o);
////                    Class<?> iCommandClass = Class.forName("net.minecraft.server.v1_12_R1.ICommand");
//                    Object newO = Proxy.newProxyInstance(o.getClass().getClassLoader()
//                            ,o.getClass().getInterfaces()
//                            ,new CommandProxy(o));
////                    newO = commandWhitelistClass.cast(newO);
//                    map.put("whitelist", 1);
////                    map.put("liert", newO);
////                    this.getServer().getConsoleSender().sendMessage("-----***** " + getCommandMethod.invoke(o) + " *****-----");
//                    System.out.println(newO);
//                    break;
//                }
//            }
//            this.getServer().getConsoleSender().sendMessage("-----***** 结果 *****-----");
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                System.out.println("key: " + key + " --> value: " + value);
//            }
//            Map<String, Object> againMap = (Map<String, Object>) bCommandHandlerField.get(commandHandlerObject);
//            this.getServer().getConsoleSender().sendMessage("-----***** againMap *****-----");
//            for (Map.Entry<String, Object> entry : againMap.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                System.out.println("key: " + key + " --> value: " + value);
//            }
//            this.getServer().getConsoleSender().sendMessage("-----***** " + sets + " *****-----");
//        } catch (Throwable e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
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
