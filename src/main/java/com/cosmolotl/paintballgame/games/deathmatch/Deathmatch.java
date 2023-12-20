package com.cosmolotl.paintballgame.games;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.Team;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.items.BulletMaker;
import com.cosmolotl.paintballgame.items.GunMaker;
import com.cosmolotl.paintballgame.managers.ConfigManager;
import com.cosmolotl.paintballgame.managers.GameManager;
import com.google.common.collect.TreeMultimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class Deathmatch extends Game {

    private PaintballGame paintballGame;
    private GameManager gameManager;

    private Random rnd = new Random();
    private GunMaker gunMaker = new GunMaker();
    private BulletMaker bulletMaker = new BulletMaker();

    private List<UUID> players;
    private List<Team> teamList;
    private HashMap<UUID, Team> teams;
    private HashMap<Team, Integer> points;

    private BossBar bossBar;

    public Deathmatch(PaintballGame paintballGame, GameManager gameManager) {
        super(paintballGame, gameManager);
        this.paintballGame = paintballGame;
        this.gameManager = gameManager;

        this.teams = new HashMap<>();
        this.teamList = new ArrayList<>();
        this.players = new ArrayList<>();
        this.points = new HashMap<>();
    }

    @Override
    public void start() {

        // Teleport players
        for (Player player : Bukkit.getOnlinePlayers()){
            spawnPlayer(player);
        }

        // Give Weapons

        // Give Players Teamed Hat

        // Give Players Colored names (Tab, or NameTag)
    }

    @Override
    public void setup() {
        System.out.println("Ran Setup");
        // Add all online players
        for (Player player : Bukkit.getOnlinePlayers()){
            addPlayer(player);
        }

        createTeams();

        // Add players to teams
        for (UUID uuid : players){
            TreeMultimap<Integer, Team> count = TreeMultimap.create();
            for (Team team : teamList){
                count.put(countTeam(team), team);
            }
            teams.put(uuid, (Team) count.values().toArray()[0]);
        }

        bossBar = Bukkit.createBossBar(
                bossBarText(),
                BarColor.BLUE,
                BarStyle.SOLID
        );

        for (Player player : Bukkit.getOnlinePlayers()){
            if (players.contains(player.getUniqueId())){
                bossBar.addPlayer(player);
            }
        }
        // Teleport players to correct sides
    }

    public void spawnPlayer (Player player) {
        if (teams.keySet().contains(player.getUniqueId())){
        // Give Items
            player.getInventory().addItem(gunMaker.makeBasicGun(teams.get(player.getUniqueId())));
        // Teleport to Correct Spawn
            List<Marker> possibleSpawn = new ArrayList<>();
            for (Marker marker : player.getWorld().getEntitiesByClass(Marker.class)){
                if (marker.getCustomName().contains(String.valueOf(getTeamIndex(teams.get(player.getUniqueId()))))){
                    possibleSpawn.add(marker);
                }
            }
            System.out.println("Spawns: " + possibleSpawn.size());
            System.out.println("Teleported: " + player.getName().toString());
            player.teleport(possibleSpawn.get(rnd.nextInt(possibleSpawn.size())));
        }
    }

    public void setScore(Team team, int amount){
        points.replace(team, amount);
    }

    public void addScore(Team team, int amount){
        int score = points.get(team);
        score += amount;
        points.replace(team, score);
    }

    public int getTeamIndex(Team team){
        System.out.println("Team Index;" + (teamList.indexOf(team) + 1));
        return (teamList.indexOf(team) + 1);
    }

    public int getScore(Team team){
        return points.get(team);
    }

    public String bossBarText(){
        String bossText = "";
        for (Team team : teamList){
            bossText = bossText + team.getChatColor().toString() + ChatColor.BOLD + getScore(team) + ChatColor.GRAY.toString() + ChatColor.BOLD + " - ";
        }
        bossText = bossText.substring(0, bossText.length() - 3); // Remove last " - "
        return bossText;
    }

    public void addPlayer(Player player){
        players.add(player.getUniqueId());
    }

    public void createTeams(){
        List<Team> allTeams = new ArrayList<>();
        for (Team team : Team.values()){
            allTeams.add(team);
        }
        for (int i = 0; i < ConfigManager.getDeathmatchTeam(); i++){
            int num = rnd.nextInt(allTeams.size());
            teamList.add(allTeams.get(num));
            points.put(allTeams.get(num), 0);
            allTeams.remove(num);
        }
    }

    public int countTeam(Team team) {
        int amount = 0;
        for (Team t : teams.values()){
            if (t == team){
                amount ++;
            }
        }
        return amount;
    }

    public Team getTeam(Player player){
        if (teams.containsKey(player.getUniqueId())){
            Team team = teams.get(player.getUniqueId());
            return team;
        }
        return null;
    }

    public void removeTeam(Player player){
        if (teams.containsKey(player.getUniqueId())){
            teams.remove(player.getUniqueId());
        }
    }

    public void cleanup(){
        bossBar.removeAll();
    }
}
