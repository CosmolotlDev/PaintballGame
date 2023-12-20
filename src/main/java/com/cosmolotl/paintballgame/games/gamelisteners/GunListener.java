package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.items.BulletMaker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GunListener implements Listener {

    private PaintballGame paintballGame;

    public GunListener (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    BulletMaker bulletMaker = new BulletMaker();

    @EventHandler
    public void shootGun (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

        if (e.getHand().equals(EquipmentSlot.HAND) && e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (item.getType().equals(Material.LEATHER_HORSE_ARMOR) && item.getItemMeta().getCustomModelData() == 1){
                Snowball snowball = player.launchProjectile(Snowball.class);
                Team playersTeam = paintballGame.getGameManager().getGame().getTeam(player); // possible error
                if (playersTeam != null){
                    snowball.setItem(bulletMaker.makeBullet(playersTeam));
                } else {
                    player.sendMessage(ChatColor.RED + "Error: Gunlistener.Shootgun, No color team");
                }
            }
        }
    }

}
