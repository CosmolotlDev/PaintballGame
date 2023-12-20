package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

public class RespawnListener implements Listener {

    private PaintballGame paintballGame;

    public RespawnListener (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        if (paintballGame.getGameManager().getGame() != null){
            Bukkit.getScheduler().runTaskLater(paintballGame, new Runnable() {
                @Override
                public void run() {
                    paintballGame.getGameManager().getGame().spawnPlayer(e.getPlayer(), false);
                }
            }, 1);
        } else {
            Bukkit.getScheduler().runTaskLater(paintballGame, new Runnable() {
                @Override
                public void run(){
                    paintballGame.getGameManager().onJoin(e.getPlayer());
                }
            }, 1);
        }

    }

}
