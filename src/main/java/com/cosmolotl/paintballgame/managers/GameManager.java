package com.cosmolotl.paintballgame.managers;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.GameState;
import com.cosmolotl.paintballgame.enums.Gamemode;
import com.cosmolotl.paintballgame.games.deathmatch.Deathmatch;
import com.cosmolotl.paintballgame.games.deathmatch.TurfWar;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.tools.MarkerTeleporter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;

public class GameManager {

    MarkerTeleporter markerTeleporter = new MarkerTeleporter();
    private Game currentGame = null;

    public PaintballGame paintballGame;

    public GameManager (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    public void createGame(Gamemode gamemode){
        if (currentGame == null){
            switch (gamemode){
                case DEATHMATCH:
                    currentGame = new Deathmatch(paintballGame, this);
                    currentGame.setup();
                    break;
                case TURFWAR:
                    currentGame = new TurfWar(paintballGame, this);
                    currentGame.setup();
                    break;
            }
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
