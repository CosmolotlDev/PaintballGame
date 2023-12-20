package com.cosmolotl.paintballgame.commands;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.items.MarkerEggsMaker;
import org.bukkit.*;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SetupCommand implements CommandExecutor {

    private PaintballGame paintballGame;
    private MarkerEggsMaker markerMaker;

    public SetupCommand(PaintballGame paintballGame){
        this.paintballGame = paintballGame;

        this.markerMaker = new MarkerEggsMaker();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // If sender is a Player, and has permission
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (player.hasPermission("paintball.setup")){

                    player.getInventory().clear();

                    for (World world : Bukkit.getWorlds()){
                        world.setGameRule(GameRule.KEEP_INVENTORY, true);
                        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
                        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                        world.setDifficulty(Difficulty.PEACEFUL);
                    }

                    // Give user spawn eggs (Spawns (1,2,3,4), Waiting Box)
                    player.getInventory().setItem(0, markerMaker.MarkerEgg("Spawn", Material.SLIME_SPAWN_EGG));
                    player.getInventory().setItem(1, markerMaker.MarkerEgg("1", Material.BAT_SPAWN_EGG));
                    player.getInventory().setItem(2, markerMaker.MarkerEgg("2", Material.BAT_SPAWN_EGG));
                    player.getInventory().setItem(3, markerMaker.MarkerEgg("3", Material.BAT_SPAWN_EGG));
                    player.getInventory().setItem(4, markerMaker.MarkerEgg("4", Material.BAT_SPAWN_EGG));

                    // Give Tool Items
                    player.getInventory().setItem(7, markerMaker.MarkerEgg("Remove All", Material.BLAZE_SPAWN_EGG));
                    player.getInventory().setItem(8, markerMaker.MarkerEgg("Finish Edit", Material.ALLAY_SPAWN_EGG));

                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission!");
                }
            }
        return false;
    }

}
