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
import org.bukkit.entity.Player;

public class GameManager {

    MarkerTeleporter markerTeleporter = new MarkerTeleporter();
    private Game currentGame = null;

    public PaintballGame paintballGame;

    public GameManager (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    public void createDeathMatch(DeathmatchMap deathmatchMap){
        if (currentGame == null){
            currentGame = new Deathmatch(paintballGame, this, deathmatchMap);
            currentGame.setup();
        } else {
            System.out.println("Game is currently in progress! Can't start a new one");
        }

    }

    public void createTurfWarMatch(TurfWarMap turfWarMap){
        if (currentGame == null){
            currentGame = new TurfWar(paintballGame, this, turfWarMap);
            currentGame.setup();
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
        markerTeleporter = new MarkerTeleporter();

        if (currentGame != null){
            currentGame.cleanup();
            currentGame = null;
        }
        for (Player player : Bukkit.getOnlinePlayers()){
            markerTeleporter.teleport(player, "Spawn");
            player.getInventory().clear();
        }
    }

    public void onJoin(Player player){
        if (currentGame != null && currentGame.getPlayers().contains(player.getUniqueId())){
            currentGame.rejoin(player);
        } else {
            player.getInventory().clear();
            markerTeleporter.teleport(player, "Spawn");
        }
    }

    public Game getGame(){
        return currentGame;
    }
    // Reset Game (Cleanup)


}
