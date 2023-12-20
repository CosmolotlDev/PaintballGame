package com.cosmolotl.paintballgame.managers;

import com.cosmolotl.paintballgame.PaintballGame;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    public static FileConfiguration config;

    public static void setUpConfig(PaintballGame paintballGame){
        ConfigManager.config = paintballGame.getConfig();
        paintballGame.saveDefaultConfig();
    }

    public static int getWorldBorder () {return config.getInt("worldborder");}
    public static int getTurfTime () {return config.getInt("turfwar.time");}
    public static int getTurfTeam () {return config.getInt("turfwar.teams");}
    public static int getDeathmatchTeam (){
        return config.getInt("deathmatch.teams");
    }
    public static int getDeathmatchWinReq () {return config.getInt("deathmatch.kills-requirement");}

}
