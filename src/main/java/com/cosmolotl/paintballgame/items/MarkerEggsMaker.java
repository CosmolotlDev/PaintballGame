package com.cosmolotl.paintballgame.items;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class MarkerEggsMaker {

    public ItemStack MarkerEgg(String name, Material material){

        ItemStack egg = new ItemStack(material);
        if (egg.getItemMeta() instanceof SpawnEggMeta){
            SpawnEggMeta eggMeta = (SpawnEggMeta) egg.getItemMeta();

            eggMeta.setDisplayName(name);

            egg.setItemMeta(eggMeta);
        } else {
            egg = new ItemStack(Material.BARRIER);
            ItemMeta eggMeta = egg.getItemMeta();

            eggMeta.setDisplayName("Invalid!");

            egg.setItemMeta(eggMeta);
        }
        return egg;
    }
}
