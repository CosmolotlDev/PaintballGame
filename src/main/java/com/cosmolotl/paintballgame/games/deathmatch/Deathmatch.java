package com.cosmolotl.paintballgame.games.deathmatch;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.enums.*;
import com.cosmolotl.paintballgame.enums.maps.DeathmatchMap;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.items.BulletMaker;
import com.cosmolotl.paintballgame.items.GunMaker;
import com.cosmolotl.paintballgame.games.gamelisteners.GunListener;
import com.cosmolotl.paintballgame.items.HatMaker;
import com.cosmolotl.paintballgame.items.VillagerMaker;
import com.cosmolotl.paintballgame.listeners.WaitForWorldLoad;
import com.cosmolotl.paintballgame.managers.ConfigManager;
import com.cosmolotl.paintballgame.managers.GameManager;
import com.cosmolotl.paintballgame.managers.MapManager;
import com.google.common.collect.TreeMultimap;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Deathmatch extends Game {

    private PaintballGame paintballGame;
    private GameManager gameManager;
    private DeathmatchMap deathmatchMap;

    private World world;

    private Random rnd = new Random();
    private GunMaker gunMaker = new GunMaker();
    private BulletMaker bulletMaker = new BulletMaker();
    private HatMaker hatMaker = new HatMaker();
    private VillagerMaker villagerMaker = new VillagerMaker();

    Listener gunListener = new GunListener(this);
    Listener waitForWorldLoad = new WaitForWorldLoad(this);

    private HashMap<String, Location> spawns;

    private List<UUID> players;
    private List<Team> teamList;
    private HashMap<UUID, Team> teams;
    private HashMap<Team, Integer> teamPoints;

    private BossBar bossBar;

    public Deathmatch(PaintballGame paintballGame, GameManager gameManager, DeathmatchMap deathmatchMap) {
        super(paintballGame, gameManager);
        this.paintballGame = paintballGame;
        this.gameManager = gameManager;
        this.deathmatchMap = deathmatchMap;
        this.spawns = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(gunListener, paintballGame);
        Bukkit.getPluginManager().registerEvents(waitForWorldLoad, paintballGame);

        world = Bukkit.createWorld(new WorldCreator(deathmatchMap.name().toLowerCase()));
        world.setAutoSave(false);

        this.teams = new HashMap<>();
        this.teamList = new ArrayList<>();
        this.players = new ArrayList<>();
        this.teamPoints = new HashMap<>();
    }
    @Override
    public void start() {
        // Teleport players
        for (Player player : Bukkit.getOnlinePlayers()){
            spawnPlayer(player, true);
            player.sendTitle(ChatColor.DARK_PURPLE + "Deathmatch!", ChatColor.BLUE + "First team to " + ConfigManager.getDeathmatchWinReq() + " kills wins!", 10, 100, 10);
        }
        setGameState(GameState.LIVE);
    }

    @Override
    public void setup() {

        setUpSpawns();
        // Add all online players
        for (Player player : Bukkit.getOnlinePlayers()){
            addPlayer(player);
            player.teleport(spawns.get("spawn"));
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
        spawnStores();

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

        setGameState(GameState.STANDBY);
    }

    public void spawnStores(){
        FileConfiguration mapConfig = MapManager.mapConfig;
        String mapName = deathmatchMap.name().toLowerCase();
        System.out.println(mapName);
        for (String string : mapConfig.getConfigurationSection("maps." + mapName + ".stores").getKeys(false)){
            for (String store : mapConfig.getConfigurationSection("maps." + mapName + ".stores." + string).getKeys(false)){
                System.out.println("Start of a loop");
                Location location = (new Location(
                        Bukkit.getWorld(mapName),
                        mapConfig.getDouble("maps." + mapName + ".stores." + string + "." + store + ".x"),
                        mapConfig.getDouble("maps." + mapName + ".stores." + string + "." + store + ".y"),
                        mapConfig.getDouble("maps." + mapName + ".stores." + string + "." + store + ".z"),
                        (float) mapConfig.getDouble("maps." + mapName + ".stores." + string + "." + store + ".yaw"),
                        (float) mapConfig.getDouble("maps." + mapName + ".stores." + string + "." + store + ".pitch")
                ));
                if (Integer.valueOf(string) <= teamList.size())
                switch (store){
                    case "Weapon":
                        villagerMaker.spawnCustomVillager(location, teamList.get(Integer.valueOf(string) - 1), VillagerStoreType.WEAPONS);
                }

            }
            System.out.println(string + " : " + spawns.get(string));
        }
    }

    @Override
    public void rejoin(Player player) {
        spawnPlayer(player, false);
    }

    @Override
    public void spawnPlayer(Player player, Boolean giveKit) {
        if (teams.keySet().contains(player.getUniqueId())){
        // Give Items
            if (giveKit){
                player.getInventory().addItem(gunMaker.makeGun(teams.get(player.getUniqueId()), GunType.CLASSIC_GUN));
                player.getInventory().setHelmet(hatMaker.MakeHat(getTeam(player), true));
            }

        // Teleport to Correct Spawn
            player.teleport(spawns.get(Integer.toString(getTeamIndex(getTeam(player)))));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 5, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3, true, true));
        } else {
            gameManager.onJoin(player);
        }
    }

    public void winGame(Team team){
        String winMessage = team.getChatColor() + team.name() + " WINS!";
        for (Player player : Bukkit.getOnlinePlayers()){
            if (players.contains(player.getUniqueId())){
                player.getInventory().clear();
                if (teams.get(player.getUniqueId()) == team){
                    player.sendTitle(ChatColor.GREEN + "VICTORY!", winMessage, 10, 200, 20);
                } else {
                    player.sendTitle(ChatColor.RED + "DEFEAT!", winMessage, 10, 200, 20);
                }
            }
        }
        setGameState(GameState.VICTORY);
    }

    @Override
    public void end() {

    }

    public void cleanup(){
        bossBar.removeAll();
        HandlerList.unregisterAll(gunListener);
        HandlerList.unregisterAll(waitForWorldLoad);
        Bukkit.unloadWorld(world, false);
    }

    // ==== Get Methods ==== //
    @Override
    public World getWorld() {
        return world;
    }
    @Override
    public List<UUID> getPlayers() {
        return players;
    }

    @Override
    public List<Team> getTeams() {
        return teamList;
    }

    public Team getTeam(Player player){
        if (teams.containsKey(player.getUniqueId())){
            Team team = teams.get(player.getUniqueId());
            return team;
        }
        return null;
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
    public int getTeamScore(Team team){
        return teamPoints.get(team);
    }
    public int getTeamIndex(Team team){
        System.out.println("Team Index;" + (teamList.indexOf(team) + 1));
        return (teamList.indexOf(team) + 1);
    }

    // ==== Updaters ==== //

    @Override
    public void addTeamScore(Team team, int amount) {
        int currentScore = teamPoints.get(team);
        int newScore = currentScore + amount;
        teamPoints.replace(team, newScore);
    }

    public void updateBossBar(){
        bossBar.setTitle(bossBarText());
    }

    // ==== Tools ==== //
    public void addScore(Player player, int amount, PlayerSelector selector){
        Team team = getTeam(player);
        int teamScore = teamPoints.get(team);

        switch (selector){
            case BOTH:
                // To be made
                break;
            case PLAYER:
                // To be Made
                break;
            case TEAM:
                teamScore += amount;
                break;
        }
        teamPoints.replace(team, teamScore);
        updateBossBar();
        if (teamPoints.get(team) >= ConfigManager.getDeathmatchWinReq()){
            winGame(team);
        }
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
            teamPoints.put(allTeams.get(num), 0);
            allTeams.remove(num);
        }
    }

    public String bossBarText(){
        String bossText = "";
        for (Team team : teamList){
            bossText = bossText + team.getChatColor().toString() + ChatColor.BOLD + getTeamScore(team) + ChatColor.GRAY.toString() + ChatColor.BOLD + " - ";
        }
        bossText = bossText.substring(0, bossText.length() - 3); // Remove last " - "
        return bossText;
    }

    private void setUpSpawns(){
        FileConfiguration mapConfig = MapManager.mapConfig;
        String mapName = deathmatchMap.name().toLowerCase();
        System.out.println(mapName);
        for (String string : mapConfig.getConfigurationSection("maps." + mapName + ".spawns").getKeys(false)){
            System.out.println("Start of a loop");
            spawns.put(string, new Location(
                    Bukkit.getWorld(mapName),
                    mapConfig.getDouble("maps." + mapName + ".spawns." + string + ".x"),
                    mapConfig.getDouble("maps." + mapName + ".spawns." + string + ".y"),
                    mapConfig.getDouble("maps." + mapName + ".spawns." + string + ".z"),
                    (float) mapConfig.getDouble("maps." + mapName + ".spawns." + string + ".yaw"),
                    (float) mapConfig.getDouble("maps." + mapName + ".spawns." + string + ".pitch")
            ));
            System.out.println(string + " : " + spawns.get(string));
        }
    }

}
