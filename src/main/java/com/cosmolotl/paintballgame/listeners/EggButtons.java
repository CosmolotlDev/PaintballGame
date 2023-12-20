package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.PaintballGame;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class MarkerEggListener implements Listener {

    private PaintballGame paintballGame;

    public MarkerEggListener (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    @EventHandler
    public void OnSpawnEgg (PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(item.getType().name().toLowerCase().contains("spawn_egg")) {
                e.setCancelled(true);
                if (item.getItemMeta().getDisplayName().contains("Remove All")){
                    for (Marker marker : player.getWorld().getEntitiesByClass(Marker.class)){
                        marker.remove();
                    }
                } else {
                    Marker locationMarker = (Marker) player.getWorld().spawnEntity(player.getLocation(), EntityType.MARKER);
                    locationMarker.setCustomName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                }

            }
        }
    }



}
