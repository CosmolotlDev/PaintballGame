package com.cosmolotl.paintballgame.items;

import com.cosmolotl.paintballgame.enums.Team;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class HatMaker {

    public ItemStack MakeHat (Team team, boolean binding) {
        ItemStack hat = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta hatMeta = (LeatherArmorMeta) hat.getItemMeta();
        hatMeta.setColor(team.getColor());
        if (binding){
            hatMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
            hatMeta.addEnchant(Enchantment.DURABILITY, 1000, true);
            hatMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            hatMeta.addItemFlags(ItemFlag.HIDE_DYE);
            hatMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            hatMeta.setUnbreakable(true);
        }
        hat.setItemMeta(hatMeta);

        return hat;
    }

}
