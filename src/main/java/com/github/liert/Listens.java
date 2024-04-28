package com.github.liert;

import com.github.liert.Config.Settings;
import com.github.liert.Tool.Tools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.RemoteServerCommandEvent;

public class Listens implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (!Tools.existPlayer(player.getName())) {
            e.disallow(Result.KICK_WHITELIST, Tools.format(Settings.I.Message.get("noWhiteList"), player.getName()));
        }
    }

    @EventHandler
    public void onRemoteCommand(RemoteServerCommandEvent e) {
    }
}
