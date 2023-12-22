package com.cosmolotl.paintballgame;

import com.cosmolotl.paintballgame.commands.EndCommand;
import com.cosmolotl.paintballgame.commands.JoinCommand;
import com.cosmolotl.paintballgame.commands.SetupCommand;
import com.cosmolotl.paintballgame.commands.StartCommand;
import com.cosmolotl.paintballgame.guns.Gun;
import com.cosmolotl.paintballgame.listeners.RespawnListener;
import com.cosmolotl.paintballgame.listeners.EggButtons;
import com.cosmolotl.paintballgame.listeners.OnJoinListener;
import com.cosmolotl.paintballgame.managers.ConfigManager;
import com.cosmolotl.paintballgame.managers.GameManager;
import com.cosmolotl.paintballgame.managers.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaintballGame extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        ConfigManager.setUpConfig(this);
        MapManager.setUpConfig(this);
        System.out.println("Paintball Loaded V2");

        Bukkit.getPluginManager().registerEvents(new EggButtons(this), this);
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(this), this);

        getCommand("end").setExecutor(new EndCommand(this));
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));

        this.gameManager = new GameManager(this);
    }

    @Override
    public void onDisable(){
        gameManager.deleteGame();
    }
    public GameManager getGameManager() {
        return gameManager;
    }
}
