package com.cosmolotl.paintballgame.enums.maps;

import org.bukkit.Bukkit;
import org.bukkit.World;

public enum TurfWarMap {

    TEST_WORLD(
            Bukkit.getWorld("test_world")
    );

    private World world;

    TurfWarMap(World world){
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
