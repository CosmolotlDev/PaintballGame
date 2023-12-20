package com.cosmolotl.paintballgame.commands;

import com.cosmolotl.paintballgame.PaintballGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class JoinCommand implements CommandExecutor {

    private PaintballGame paintballGame;

    public JoinCommand(PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        return false;
    }
}
