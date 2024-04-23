package com.github.liert;

import com.github.liert.SQL.SQLQuery;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            return true;
        }
        if (strings[0].equals("add")) {
            if (strings.length < 3) {
                commandSender.sendMessage("Usage: /wl add <id>");
                return true;
            }
            String result = SQLQuery.bind(strings[1], strings[2]);
            commandSender.sendMessage(result);
            return true;
        }
        if (strings[0].equals("def")) {
            if (strings.length < 3) {
                commandSender.sendMessage("Usage: /wl def <id>");
                return true;
            }
            String result = SQLQuery.delBind(strings[1], strings[2]);
            commandSender.sendMessage(result);
            return true;
        }
        return true;
    }
}
