package com.cosmolotl.paintballgame.tools;

import org.bukkit.Location;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarkerTeleporter {

    public void teleport(Player player, String markerID){
        Random rnd = new Random();

        List<Marker> possibleSpawn = new ArrayList<>();
        for (Marker marker : player.getWorld().getEntitiesByClass(Marker.class)){
            if (marker.getCustomName().contains(markerID)){
                possibleSpawn.add(marker);
            }
        }

        player.teleport(possibleSpawn.get(rnd.nextInt(possibleSpawn.size())));
    }

    public Location location(Player player, String markerID){
        Random rnd = new Random();

        List<Marker> possibleSpawn = new ArrayList<>();
        for (Marker marker : player.getWorld().getEntitiesByClass(Marker.class)){
            if (marker.getCustomName().contains(markerID)){
                possibleSpawn.add(marker);
            }
        }

        Location location = possibleSpawn.get(rnd.nextInt(possibleSpawn.size())).getLocation();

        return location;
    }
}
