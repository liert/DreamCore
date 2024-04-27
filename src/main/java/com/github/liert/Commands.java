package com.github.liert;

import com.github.liert.SQL.SQLQuery;
import com.github.liert.Tool.Tools;
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
                case "getLuckData": { getLuckData(commandSender, strings); break; }
            }
        }
        return true;
    }

    public void addWhiteList(CommandSender commandSender, String[] strings){
        String result;
        if (strings.length == 2) {
            result = Tools.bind(strings[1]);
        } else if (strings.length == 3) {
            result = Tools.bind(strings[1], strings[2]);
        } else {
            commandSender.sendMessage("Usage: /wl add <id> [qq]");
            return;
        }
        commandSender.sendMessage(result);
    }
    public void delWhiteList(CommandSender commandSender, String[] strings){
        if (strings.length < 2) {
            commandSender.sendMessage("Usage: /wl def <id>");
            return;
        }
        String result = Tools.delBind(strings[1]);
        commandSender.sendMessage(result);
    }
    public void getLuckData(CommandSender commandSender, String[] strings){
        if (strings.length < 2) {
            commandSender.sendMessage("Usage: /luck getLuckData <qq>");
            return;
        }
        String player = Tools.getPlayer(strings[1]);
        if (!player.equals("NULL")) {
            int[] luckData = Tools.getLuckData(player);
            if (luckData[0] != 0) {
                commandSender.sendMessage(player + " HAVE_LUCK");
                return;
            }
            Tools.dailyAdd(player, luckData);
            if (luckData[1] == 0) {
                commandSender.sendMessage(player + " VERY_LUCK");
                return;
            }
        }
        commandSender.sendMessage(player + " LUCK");
    }
}
