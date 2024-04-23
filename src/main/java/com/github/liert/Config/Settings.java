package com.github.liert.Config;

public class Settings {
    public static Settings I;
    public String MySQL_IP;
    public String MySQL_port;
    public String MySQL_database;
    public String MySQL_user;
    public String MySQL_password;
    public String MySQL_Table;
    public boolean Koishi;
    public String KoishiWsUrl;
    public String Message;
    public Settings() {
        I = this;
    }
}
