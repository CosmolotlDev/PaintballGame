package com.cosmolotl.paintballgame.timers;

import com.cosmolotl.paintballgame.PaintballGame;
import com.cosmolotl.paintballgame.instance.Game;
import com.cosmolotl.paintballgame.managers.ConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

public class Countdown extends BukkitRunnable {

    private PaintballGame paintballGame;
    private Game game;
    private int countdownSeconds;
    private BukkitTask countTask;

    public Countdown(PaintballGame paintballGame, Game game) {
        this.paintballGame = paintballGame;
        this.countdownSeconds = ConfigManager.getTurfTime();
        this.game = game;
        start();
    }

    public void start() {
        countTask = runTaskTimer(paintballGame, 0, 20);
    }

    public void end(){
        countTask.cancel();
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            game.end();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()){
            if (game.getPlayers().contains(player.getUniqueId())){
                updateActionBarForPlayer(player);
            }
        }
        countdownSeconds--;
    }

    public void updateActionBarForPlayer(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatTime() ));
    }

    private String formatTime (){
        int minutes = countdownSeconds / 60;
        int seconds = countdownSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}