package com.cosmolotl.paintballgame.guns;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.items.BulletMaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class ClassicGun extends Gun {

    BulletMaker bulletMaker = new BulletMaker();
    PaintballGame paintballGame;
    public ClassicGun (PaintballGame paintballGame){
        this.paintballGame = paintballGame;
    }

    public void startShoot(Player player, Team playerTeam) {
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setItem(bulletMaker.makeBullet(playerTeam, 9));
        Vector currentVector = snowball.getVelocity().normalize();
        Vector newVector = currentVector.multiply(2);
        snowball.setVelocity(newVector);
    }

}
