package com.cosmolotl.paintballgame.enums.maps;

import org.bukkit.Bukkit;
import org.bukkit.World;

public enum DeathmatchMap {

    TEST_WORLD(
            Bukkit.getWorld("test_world")
    );

    private World world;

    DeathmatchMap(World world){
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
