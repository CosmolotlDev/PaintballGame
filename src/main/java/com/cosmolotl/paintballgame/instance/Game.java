package com.cosmolotl.paintballgame.instance;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.GameState;
import com.cosmolotl.paintballgame.enums.PlayerSelector;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.managers.GameManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class Game {

    private PaintballGame paintballGame;
    private GameManager gameManager;

    private GameState gameState;

    public Game (PaintballGame paintballGame, GameManager gameManager){
        this.paintballGame = paintballGame;
        this.gameManager = gameManager;
    }

    public GameState getGameState(){
        return gameState;
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public PaintballGame getPaintballGame(){
        return paintballGame;
    }

    public abstract List<UUID> getPlayers();
    public abstract void spawnPlayer(Player player, Boolean giveKit);
    public abstract void start();
    public abstract void setup();
    public abstract void end();
    public abstract void rejoin(Player player);
    public abstract void cleanup();
    public abstract Team getTeam(Player player);
    public abstract List<Team> getTeams();
    public abstract void addScore(Player player, int amount, PlayerSelector selector);
    public abstract void addTeamScore(Team team, int amount);
    public abstract World getWorld();

}
