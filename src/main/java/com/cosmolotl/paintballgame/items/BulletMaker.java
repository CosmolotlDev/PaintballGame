package com.cosmolotl.paintballgame.items;

import com.cosmolotl.paintballgame.enums.Team;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class BulletMaker {

    public ItemStack makeBullet (Team team, int damage){
        ItemStack bullet = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta bulletMeta = (LeatherArmorMeta) bullet.getItemMeta();
        bulletMeta.setCustomModelData(team.getBulletModel());
        bulletMeta.setColor(team.getColor()); // remove later
        bulletMeta.setLocalizedName(String.valueOf(damage));
        bullet.setItemMeta(bulletMeta);
        return bullet;
    }
}
