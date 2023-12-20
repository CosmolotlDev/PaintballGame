package com.cosmolotl.paintballgame.managers;

import com.cosmolotl.paintballgame.PaintballGame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MapManager {

    private static File mapFile;
    public static YamlConfiguration mapConfig;

    public static void setUpConfig(PaintballGame paintballGame){
        mapFile = new File(paintballGame.getDataFolder(), "map.yml");
        paintballGame.saveDefaultConfig();
    }



}
