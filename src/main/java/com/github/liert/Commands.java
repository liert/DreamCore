package com.github.liert;

import com.github.liert.SQL.SQLQuery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (!commandSender.isOp()) {
                return true;
            }
        }
        if (strings.length < 1) {
            return true;
        }
        if (command.getName().equals("wl")) {
            switch (strings[0]) {
                case "add": { addWhiteList(commandSender, strings); break; }
                case "del": { delWhiteList(commandSender, strings); break; }
            }
        } else if (command.getName().equals("luck")) {
            switch (strings[0]) {
                case "getPlayer": { getPlayer(commandSender, strings); break; }
            }
        }
        return true;
    }

    public void addWhiteList(CommandSender commandSender, String[] strings){
        if (strings.length < 3) {
            commandSender.sendMessage("Usage: /wl add <qq> <id>");
            return;
        }
        String result = SQLQuery.bind(strings[1], strings[2]);
        commandSender.sendMessage(result);
    }
    public void delWhiteList(CommandSender commandSender, String[] strings){
        if (strings.length < 3) {
            commandSender.sendMessage("Usage: /wl def <qq> <id>");
            return;
        }
        String result = SQLQuery.delBind(strings[1], strings[2]);
        commandSender.sendMessage(result);
    }
    public void getPlayer(CommandSender commandSender, String[] strings){
        if (strings.length < 2) {
            commandSender.sendMessage("Usage: /luck getPlayer <qq>");
            return;
        }
        String result = SQLQuery.getPlayer(strings[1]);
        commandSender.sendMessage(result);
    }
}
