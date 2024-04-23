package com.github.liert;

import com.github.liert.Config.Settings;
import com.github.liert.SQL.SQLQuery;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listens implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (!SQLQuery.existWhitelist(player.getName())) {
            e.disallow(Result.KICK_WHITELIST, String.format(Settings.I.Message, player.getName()));
            return;
        }
    }

    @EventHandler
    public void onRemoteCommand(RemoteServerCommandEvent e) {
        CommandSender commandSender = e.getSender();
        Bukkit.getConsoleSender().sendMessage(e.getCommand());
        String[] args = e.getCommand().split(" ");
        if (args.length < 1) {
            return;
        }
        if (args[0].equals("wl") && args[1].equals("add")) {
            if (args.length < 4) {
                commandSender.sendMessage("Usage: /wl add <id>");
                return;
            }
            String result = SQLQuery.bind(args[2], args[3]);
            commandSender.sendMessage(result);
        }
        if (args[0].equals("wl") && args[1].equals("del")) {
            if (args.length < 4) {
                commandSender.sendMessage("Usage: /wl del <id>");
                return;
            }
            String result = SQLQuery.delBind(args[2], args[3]);
            commandSender.sendMessage(result);
        }
    }
}
