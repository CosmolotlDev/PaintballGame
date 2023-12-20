package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.instance.Game;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {

    private PaintballGame paintballGame;

    public OnJoinListener (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e){
        paintballGame.getGameManager().onJoin(e.getPlayer());
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
}
