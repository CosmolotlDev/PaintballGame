package com.cosmolotl.paintballgame.managers;

import com.cosmolotl.paintballgame.PaintballGame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MapManager {

    private static File mapFile;
    public static YamlConfiguration mapConfig;

    public static void setUpConfig(PaintballGame paintballGame){
        mapFile = new File(paintballGame.getDataFolder(), "map.yml");
        mapConfig = YamlConfiguration.loadConfiguration(mapFile);
        saveConfig();
    }

    public static void saveConfig(){
        try {
            mapConfig.save(mapFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }



}
