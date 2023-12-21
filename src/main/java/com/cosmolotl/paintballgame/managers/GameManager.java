package com.cosmolotl.paintballgame.managers;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.GameState;
import com.cosmolotl.paintballgame.enums.Gamemode;
import com.cosmolotl.paintballgame.enums.maps.DeathmatchMap;
import com.cosmolotl.paintballgame.enums.maps.TurfWarMap;
import com.cosmolotl.paintballgame.games.deathmatch.Deathmatch;
import com.cosmolotl.paintballgame.games.deathmatch.TurfWar;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.tools.MarkerTeleporter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GameManager {

    private Location lobby;

    private Game currentGame = null;

    public PaintballGame paintballGame;

    public GameManager (PaintballGame paintballGame){
        this.paintballGame = paintballGame;

        setUpLobbySpawn();
    }

    public void createDeathMatch(DeathmatchMap deathmatchMap){
        if (currentGame == null){
            currentGame = new Deathmatch(paintballGame, this, deathmatchMap);
        } else {
            System.out.println("Game is currently in progress! Can't start a new one");
        }

    }

    public void createTurfWarMatch(TurfWarMap turfWarMap){
        if (currentGame == null){
            currentGame = new TurfWar(paintballGame, this, turfWarMap);
        } else {
            System.out.println("Game is currently in progress! Can't start a new one");
        }
    }

    public void startGame(){
        if (currentGame != null && currentGame.getGameState() == GameState.STANDBY){
            currentGame.start();
        }
    }

    public void deleteGame(){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (currentGame.getPlayers().contains(player.getUniqueId())){
                player.teleport(lobby);
                player.getInventory().clear();
            }
        }

        if (currentGame != null){
            currentGame.cleanup();
            currentGame = null;
        }

    }

    public void onJoin(Player player){
        if (currentGame != null && currentGame.getPlayers().contains(player.getUniqueId())){
            currentGame.rejoin(player);
        } else {
            player.getInventory().clear();
            player.teleport(lobby);
        }
    }

    public Game getGame(){
        return currentGame;
    }
    // Reset Game (Cleanup)

    public void setUpLobbySpawn(){
        FileConfiguration mapConfig = MapManager.mapConfig;
        lobby = new Location(
                Bukkit.getWorld("world"),
                mapConfig.getDouble("lobby.x"),
                mapConfig.getDouble("lobby.y"),
                mapConfig.getDouble("lobby.z"),
                (float) mapConfig.getDouble("lobby.yaw"),
                (float) mapConfig.getDouble("lobby.pitch"));
    }

}
