package com.cosmolotl.paintballgame.listeners;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.Gamemode;
import com.cosmolotl.paintballgame.enums.maps.DeathmatchMap;
import com.cosmolotl.paintballgame.enums.maps.TurfWarMap;
import com.cosmolotl.paintballgame.games.deathmatch.Deathmatch;
import com.cosmolotl.paintballgame.items.MarkerEggsMaker;
import com.cosmolotl.paintballgame.managers.GameManager;
import org.bukkit.Material;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EggButtons implements Listener {

    private PaintballGame paintballGame;
    private MarkerEggsMaker markerMaker;

    private GameManager gameManager;

    public EggButtons(PaintballGame paintballGame){
        this.paintballGame = paintballGame;

        this.gameManager = paintballGame.getGameManager();
        this.markerMaker = new MarkerEggsMaker();
    }

    @EventHandler
    public void OnSpawnEgg (PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(item.getType().name().toLowerCase().contains("spawn_egg")) {
                e.setCancelled(true);
                switch (item.getItemMeta().getDisplayName()){

                    case "Edit":
                        player.performCommand("setup");
                        break;

                    // Start Game
                    case "Start":
                        player.getInventory().clear();
                        paintballGame.getGameManager().startGame();
                        break;

                    // Start Game
                    case "Reset":
                        paintballGame.getGameManager().deleteGame();
                        mainMenu(player);
                        break;

                    // Join Deathmatch
                    case "Deathmatch":
                        mapSelection(Gamemode.DEATHMATCH, player);
                        break;

                    // Join Turfwar
                    case "TurfWar":
                        mapSelection(Gamemode.TURFWAR, player);
                        break;

                    // Remove All
                    case "Remove All":
                        for (Marker marker : player.getWorld().getEntitiesByClass(Marker.class)){
                            marker.remove();
                        }
                        break;

                    // Finish Setup
                    case "Finish Edit":
                        mainMenu(player);
                        break;

                    // Marker Makers
                    default:
                        if (item.getType().equals(Material.PARROT_SPAWN_EGG)){
                            player.getInventory().clear();
                            createGame(item);
                            startStopGame(player);
                        }

                        /*
                        Marker locationMarker = (Marker) player.getWorld().spawnEntity(player.getLocation(), EntityType.MARKER);
                        locationMarker.setCustomName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                        System.out.println(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                         */
                }
            }
        }
    }

    public void mainMenu (Player player){
        player.getInventory().clear();
        player.getInventory().setItem(0, markerMaker.MarkerEgg("Deathmatch", Material.SKELETON_HORSE_SPAWN_EGG));
        player.getInventory().setItem(1, markerMaker.MarkerEgg("TurfWar", Material.SKELETON_HORSE_SPAWN_EGG));
        player.getInventory().setItem(8, markerMaker.MarkerEgg("Edit", Material.SQUID_SPAWN_EGG));
    }

    public void mapSelection (Gamemode gamemode, Player player){
        player.getInventory().clear();

        switch (gamemode){
            case DEATHMATCH:
                for (DeathmatchMap map : DeathmatchMap.values()){
                    ItemStack mapButton = markerMaker.MarkerEgg(map.name(), Material.PARROT_SPAWN_EGG);
                    ItemMeta mapMeta = mapButton.getItemMeta();
                    mapMeta.setLocalizedName("Deathmatch");
                    mapButton.setItemMeta(mapMeta);
                    player.getInventory().addItem(mapButton);
                }
                break;
            case TURFWAR:
                for (TurfWarMap map : TurfWarMap.values()){
                    ItemStack mapButton = markerMaker.MarkerEgg(map.name(), Material.PARROT_SPAWN_EGG);
                    ItemMeta mapMeta = mapButton.getItemMeta();
                    mapMeta.setLocalizedName("TurfWar");
                    mapButton.setItemMeta(mapMeta);
                    player.getInventory().addItem(mapButton);
                }
                break;
        }
    }

    public void createGame (ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        switch (itemMeta.getLocalizedName()){
            case "Deathmatch":
                paintballGame.getGameManager().createDeathMatch(DeathmatchMap.valueOf(itemMeta.getDisplayName()));
                break;
            case "TurfWar":
                paintballGame.getGameManager().createTurfWarMatch(TurfWarMap.valueOf(itemMeta.getDisplayName()));
                break;
        }

    }

    public void startStopGame (Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, markerMaker.MarkerEgg("Start", Material.CREEPER_SPAWN_EGG));
        player.getInventory().setItem(1, markerMaker.MarkerEgg("Reset", Material.STRIDER_SPAWN_EGG));
    }
}
