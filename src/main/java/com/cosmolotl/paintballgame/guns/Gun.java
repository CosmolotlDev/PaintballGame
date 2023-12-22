package com.cosmolotl.paintballgame.guns;

import com.cosmolotl.paintballgame.enums.Team;
import org.bukkit.entity.Player;

public abstract class Gun {

    public abstract void startShoot(Player player, Team playerTeam);

}
