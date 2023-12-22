package com.cosmolotl.paintballgame.items;

import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.enums.VillagerStoreType;

import javax.xml.stream.Location;

public class VillagerMaker {

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

    }

    public void spawnUtilityStore (Location location, Team team){

    }

    public void spawnPerksStore (Location location, Team team){

    }

}
