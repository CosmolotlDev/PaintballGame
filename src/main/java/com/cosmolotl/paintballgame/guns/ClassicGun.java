package com.cosmolotl.paintballgame.guns;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.items.BulletMaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class ClassicGun extends Gun {

    BulletMaker bulletMaker = new BulletMaker();
    PaintballGame paintballGame;
    public ClassicGun (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    public void startShoot(Player player, Team playerTeam)  {
        shoot(player, playerTeam);
    }

    private void shoot(Player player, Team playerTeam){
        int numBullets = 1; // Number of bullets to spawn
        double spreadHorizontal = 2.0; // Horizontal spread angle for the bullets (in degrees)
        double spreadVertical = 1.0; // Vertical spread angle for the bullets (in degrees)

        Location playerLocation = player.getLocation().add(0, 1.62, 0);

        for (int i = 0; i < numBullets; i++) {
            // Apply random horizontal and vertical spreads to the bullet direction
            Vector spread = getRandomSpread(spreadHorizontal, spreadVertical, player);
            Vector bulletDirection = playerLocation.getDirection().clone().add(spread).normalize();

            // Spawn the bullet projectile (Snowball)
            spawnBulletProjectile(playerLocation, bulletDirection, player, playerTeam);
        }
    }

    private Vector getRandomSpread(double spreadHorizontal, double spreadVertical, Player player) {
        // Generate random spherical coordinates within the specified angles
        double randomHorizontalAngle = Math.toRadians(Math.random() * 360.0);
        double randomVerticalAngle = Math.toRadians(Math.random() * spreadVertical * 2 - spreadVertical);

        // Adjust the player's pitch to control the up and down direction
        double pitch = Math.toRadians(player.getLocation().getPitch());

        // Convert spherical coordinates to Cartesian coordinates
        double x = Math.cos(randomHorizontalAngle) * Math.sin(randomVerticalAngle);
        double y = Math.sin(randomVerticalAngle); // Removed pitch adjustment
        double z = Math.sin(randomHorizontalAngle) * Math.sin(randomVerticalAngle);

        return new Vector(x, y, z);
    }

    private void spawnBulletProjectile(Location location, Vector direction, Player player, Team playerTeam) {
        player.playSound(location, Sound.ENTITY_ARROW_SHOOT, 1, 1);

        // Spawn a projectile (Snowball) and set its velocity
        Snowball snowball = location.getWorld().spawn(location, Snowball.class);
        snowball.setItem(bulletMaker.makeBullet(playerTeam, 6));
        snowball.setVelocity(direction.multiply(2.0)); // Set velocity (adjust as needed)
        snowball.setShooter(player); // Set shooter to null to avoid player damage
    }

}
