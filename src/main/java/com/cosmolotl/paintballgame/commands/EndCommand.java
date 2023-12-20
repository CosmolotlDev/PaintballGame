package com.cosmolotl.paintballgame.commands;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.items.MarkerEggsMaker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndCommand implements CommandExecutor {

    private PaintballGame paintballGame;

    public EndCommand(PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("paintball.end")) {
                paintballGame.getGameManager().deleteGame();
                player.performCommand("setup");
            }
        }
        return false;
    }
}
