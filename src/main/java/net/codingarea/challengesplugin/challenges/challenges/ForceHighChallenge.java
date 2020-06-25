package net.codingarea.challengesplugin.challenges.challenges.force;

import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.ServerManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-16-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class ForceHighChallenge extends Setting implements Listener, ISecondExecutor, ITimerStatusExecutor {

    private BossBar bossBar;

    private int count;
    private int timeUntilNew;
    private int time;
    private int height;

    private Random maxRandom,
                    highRandom,
                    newRandom,
                    playerRandom;

    public ForceHighChallenge() {
        this.menu = MenuType.CHALLENGES;

        time = 0;
        count = 0;
        height = -1;
        newRandom = new Random();
        timeUntilNew = newRandom.nextInt(7*60 - 5*60) + 5*60;
        newRandom = null;
    }

    @Override
    public void onTimerStart() {
        time = 125;
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        maxRandom = new Random();
        highRandom = new Random();
        playerRandom = new Random();
        newRandom = new Random();

        this.bossBar = Bukkit.createBossBar("§7Waiting...", BarColor.WHITE, BarStyle.SOLID);
        addAllPlayersToBossBar();

    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        maxRandom = null;
        highRandom = null;
        newRandom = null;
        playerRandom = null;
        this.bossBar.removeAll();
    }

    @Override
    public void onSecond() {
        if (!this.enabled) return;

        if (!Challenges.timerIsStarted()) {
            this.bossBar.setColor(BarColor.RED);
            this.bossBar.setProgress(1);
            this.bossBar.setTitle("§cTimer stopped");
            return;
        }

        if (timeUntilNew != -1) {
            timeUntilNew--;

            if (timeUntilNew <= 0) {
                timeUntilNew = -1;
                time = maxRandom.nextInt(5*60 - 2*60) + 2*60;
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                height = highRandom.nextInt((players.get(playerRandom.nextInt(players.size())).getWorld().getMaxHeight() -1)) + 1;
                count = 0;

            }

            this.bossBar.setColor(BarColor.WHITE);
            this.bossBar.setProgress(1);
            this.bossBar.setTitle("§7Waiting...");
            return;
        }
        count++;
        if (count >= time) {
            List<Player> playersOnTheFalseHeight = checkPlayersHeight(height);

            if (!playersOnTheFalseHeight.isEmpty()) {
                ServerManager.simulateChallengeEnd(playersOnTheFalseHeight.get(0), ChallengeEndCause.PLAYER_CHALLENGE_FAIL);
                return;
            }
            timeUntilNew = newRandom.nextInt(7*60 - 5*60) + 5*60;
        }

        this.bossBar.setColor(BarColor.GREEN);
        this.bossBar.setTitle("§7Height §e" + this.height + " §7in §a" + ChallengeTimer.getTimeDisplay(this.time - this.count));
        this.bossBar.setProgress(((double)count / time));

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!this.enabled) return;
        this.bossBar.addPlayer(event.getPlayer());

    }

    /**
     * @return returns if the players not on the right position
     */
    private List<Player> checkPlayersHeight(int height) {

        List<Player> list = new ArrayList<>();

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

            if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
            if (currentPlayer.getLocation().getBlockY() != height) list.add(currentPlayer);

        }

        return list;

    }

    private void addAllPlayersToBossBar() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.bossBar.addPlayer(player);
        }

    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_BOOTS, ItemTranslation.FORCE_HIGH).hideAttributes().build();
    }

}