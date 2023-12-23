package com.cosmolotl.paintballgame.items;

import com.cosmolotl.paintballgame.enums.GunType;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.enums.VillagerStoreType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class VillagerMaker {

    GunMaker gunMaker = new GunMaker();

    public void spawnCustomVillager(Location location, Team team, VillagerStoreType storeType) {
        switch (storeType){
            case WEAPONS:
                spawnWeaponsStore(location, team);
                break;
            case UTILITY:
                spawnUtilityStore(location, team);
                break;
            case PERKS:
                spawnPerksStore(location, team);
                break;
        }
    }

    public void spawnWeaponsStore (Location location, Team team){
        Villager weaponsStore = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);

        // Store Properties
        weaponsStore.setCustomName("Gunsmith");
        weaponsStore.setCustomNameVisible(true);
        weaponsStore.setAdult();
        weaponsStore.setAI(false);
        weaponsStore.setVillagerExperience(1);
        weaponsStore.setProfession(Villager.Profession.WEAPONSMITH);

        // Items
        List<MerchantRecipe> tradeList = new ArrayList<>();
        tradeList.add(createTrade(gunMaker.makeGun(team, GunType.SHOTGUN), new ItemStack(Material.EMERALD, 15)));
        tradeList.add(createTrade(gunMaker.makeGun(team, GunType.SNIPER_GUN), new ItemStack(Material.EMERALD, 10)));
        tradeList.add(createTrade(gunMaker.makeGun(team, GunType.SUB_MACHINE_GUN), new ItemStack(Material.EMERALD, 15)));

        weaponsStore.setRecipes(tradeList);
    }

    public void spawnUtilityStore (Location location, Team team){

    }

    public void spawnPerksStore (Location location, Team team){

    }

    public MerchantRecipe createTrade (ItemStack itemToBuy, ItemStack itemToSell){
        MerchantRecipe trade = new MerchantRecipe(itemToBuy, Integer.MAX_VALUE);
        trade.addIngredient(itemToSell);

        return trade;
    }
}
