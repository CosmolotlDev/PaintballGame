package com.cosmolotl.paintballgame.items;

import com.cosmolotl.paintballgame.enums.GunType;
import com.cosmolotl.paintballgame.enums.Team;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GunMaker {

    public ItemStack makeGun(Team team, GunType gunType){
        ItemStack gun = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta gunMeta = (LeatherArmorMeta) gun.getItemMeta();
        gunMeta.setColor(team.getColor());
        gunMeta.setCustomModelData(gunType.getCustomModel());
        gunMeta.setDisplayName(team.getChatColor() + gunType.getDisplayName());
        gunMeta.addItemFlags(ItemFlag.HIDE_DYE);
        gun.setItemMeta(gunMeta);
        return gun;
    }

}
