package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.instance.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WaitForWorldLoad implements Listener {

    private Game game;

    public WaitForWorldLoad (Game game){
        this.game = game;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){
        System.out.println("World Load! " + e.getWorld().getName());
        Bukkit.getScheduler().runTaskLater(game.getPaintballGame(), new Runnable() {
            @Override
            public void run() {
                if (e.getWorld() == game.getWorld()){
                    System.out.println("Same world!");
                    game.setup();
                }
            }
        }, 1);

    }

}
