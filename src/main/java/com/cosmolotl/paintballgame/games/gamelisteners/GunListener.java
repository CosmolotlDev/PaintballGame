package com.cosmolotl.paintballgame.games.gamelisteners;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.GameState;
import com.cosmolotl.paintballgame.enums.PlayerSelector;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.games.deathmatch.Deathmatch;
import com.cosmolotl.paintballgame.games.deathmatch.TurfWar;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.items.BulletMaker;
import com.sun.tools.javac.jvm.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class GunListener implements Listener {

    private Game game;

    public GunListener (Game game){
        this.game = game;
    }
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long cooldownDuration = 100;

    BulletMaker bulletMaker = new BulletMaker();

    @EventHandler
    public void shootGun(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastTime = cooldowns.get(player.getUniqueId());
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastTime;

            if (timeDifference < cooldownDuration){
                e.setCancelled(true);
                return;
            }
        }

        System.out.println("Interact");
        System.out.println(e.getAction().toString());
        System.out.println(e.getHand());

        ItemStack item = player.getInventory().getItemInMainHand();
        // Check for Correct hand & is game live
        if (e.getHand().equals(EquipmentSlot.HAND) &&
                game.getGameState() == GameState.LIVE &&
                (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            System.out.println(e.getHand());
            // Check if item is a gun model
            if (item.getType().equals(Material.LEATHER_HORSE_ARMOR) && item.getItemMeta().getCustomModelData() == 1) {
                Snowball snowball = player.launchProjectile(Snowball.class);
                Team playersTeam = game.getTeam(player);
                // If player is on a team
                if (playersTeam != null) {
                    snowball.setItem(bulletMaker.makeBullet(playersTeam));
                    Vector currentVector = snowball.getVelocity().normalize();
                    Vector newVector = currentVector.multiply(2);
                    snowball.setVelocity(newVector);
                } else {
                    player.sendMessage(ChatColor.RED + "Error: Gunlistener.Shootgun, No color team");
                }
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onHit (ProjectileHitEvent e){
        Projectile projectile = e.getEntity();
        ProjectileSource sender = projectile.getShooter();
        Entity hitEntity = e.getHitEntity();
        if (sender instanceof Player && projectile instanceof Snowball && hitEntity instanceof LivingEntity){
            Snowball snowball = (Snowball) projectile;
            Player player = (Player) sender;
            LivingEntity receiver = (LivingEntity) hitEntity;
            if (hitEntity instanceof Player){
                Player hitPlayer = (Player) hitEntity;
                if (game.getTeam(hitPlayer) == game.getTeam(player)){
                    e.setCancelled(true);
                    return;
                }
            }
            if (snowball.getItem().getItemMeta().hasCustomModelData() && hasPotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 5)){ // Is it a paintball? (Snowball with customModelData)
                receiver.damage(9, player);
                receiver.setNoDamageTicks(0);
                player.playSound(player, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            }
        }
    }

    @EventHandler
    public void onBlockHit (ProjectileHitEvent e){
        ProjectileSource sender = e.getEntity().getShooter();
        Block block = e.getHitBlock();
        Projectile projectile = e.getEntity();
        if (block != null && block.getType().isSolid() && !isExcludedBlcok(block) && sender instanceof Player && isWorldBorder(block.getLocation(), e.getEntity().getWorld())){
            Player player = ((Player) sender).getPlayer();
            if (game.getPlayers().contains(player.getUniqueId())){
                if (game instanceof TurfWar){
                    if (block.getType() == game.getTeam(player).getBlock()){
                        // Nothing
                    } else if (teamBlocks().contains(block.getType())){
                        game.addScore(player, 1, PlayerSelector.TEAM);
                        for (Team team : Team.values()){
                            if (block.getType() == team.getBlock()){
                                game.addTeamScore(team, -1);
                            }
                        }
                    } else {
                        game.addScore(player, 1, PlayerSelector.TEAM);
                    }
                }
                block.setType(game.getTeam(player).getBlock());
                // Check if it is Same team
                // else if its another teams subract and add
                // if its other, than add
            }
        }
    }

    @EventHandler
    public void onKill (EntityDeathEvent e){
        System.out.println("Entity Died");
        LivingEntity killedEntity = e.getEntity();
        if (game instanceof Deathmatch && killedEntity.getKiller() instanceof Player && killedEntity instanceof Player){
            System.out.println("Killer was a player");
            Player player = e.getEntity().getKiller();
            game.addScore(player, 1, PlayerSelector.TEAM);
        }
    }

    @EventHandler
    public void onGunThrow (PlayerDropItemEvent e){
        // Prevent player from dropping gun
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.getType().equals(Material.LEATHER_HORSE_ARMOR) && item.getItemMeta().getCustomModelData() == 1){
            e.setCancelled(true);
        }
    }

    private boolean isSolidColoredConcrete(Block block) {
        // Check if the block is a solid colored concrete block
        Material blockMaterial = block.getType();
        return blockMaterial == Material.WHITE_CONCRETE ||
                blockMaterial == Material.ORANGE_CONCRETE ||
                blockMaterial == Material.MAGENTA_CONCRETE ||
                blockMaterial == Material.LIGHT_BLUE_CONCRETE ||
                blockMaterial == Material.YELLOW_CONCRETE ||
                blockMaterial == Material.LIME_CONCRETE ||
                blockMaterial == Material.PINK_CONCRETE ||
                blockMaterial == Material.GRAY_CONCRETE ||
                blockMaterial == Material.LIGHT_GRAY_CONCRETE ||
                blockMaterial == Material.CYAN_CONCRETE ||
                blockMaterial == Material.PURPLE_CONCRETE ||
                blockMaterial == Material.BLUE_CONCRETE ||
                blockMaterial == Material.BROWN_CONCRETE ||
                blockMaterial == Material.GREEN_CONCRETE ||
                blockMaterial == Material.RED_CONCRETE ||
                blockMaterial == Material.BLACK_CONCRETE;
    }

    private boolean isExcludedBlcok(Block block) {
        // Check if the block is a solid colored concrete block
        Material blockMaterial = block.getType();
        return blockMaterial == Material.BARRIER ||
                blockMaterial == Material.COMMAND_BLOCK;

    }

    private List<Material> teamBlocks (){
        List<Material> allTeam = new ArrayList<>();
        for (Team team : game.getTeams()){
            allTeam.add(team.getBlock());
        }
        return allTeam;
    }

    private boolean hasPotionEffect(Player player, PotionEffectType effectType, int amplifier){
        PotionEffect effect = player.getPotionEffect(effectType);

        return effect != null && effect.getAmplifier() == amplifier;
    }

    private boolean isWorldBorder (Location location, World world){
        Location center = world.getWorldBorder().getCenter();
        double size = world.getWorldBorder().getSize() / 2;
        if (location.getX() < center.getX() + size && location.getX() > center.getX() - size && location.getZ() < center.getZ() + size && location.getZ() > center.getZ() - size ){
            return true;
        } else {
            return false;
        }
    }
}
