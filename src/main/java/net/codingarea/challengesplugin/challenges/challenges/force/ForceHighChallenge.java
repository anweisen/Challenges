package net.codingarea.challengesplugin.challenges.challenges.force;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.ServerManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.manager.scoreboard.ScoreboardManager;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private Random random;

    public ForceHighChallenge() {
        super(MenuType.CHALLENGES);
        time = 0;
        count = 0;
        height = -1;
        random = new Random();
        timeUntilNew = random.nextInt(7*60 - 5*60) + 5*60;
        random = null;
    }

    @Override
    public void onTimerStart() {
        time = 125;
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        random = new Random();

        bossBar = Bukkit.createBossBar("§7Waiting...", BarColor.WHITE, BarStyle.SOLID);
        ScoreboardManager.getInstance().activateBossBar(bossBar);
    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        random = null;
        ScoreboardManager.getInstance().deactivateBossBar(bossBar);
    }

    @Override
    public void onSecond() {
        if (!this.enabled) return;

        if (!Challenges.timerIsStarted()) {
            bossBar.setColor(BarColor.RED);
            bossBar.setProgress(1);
            bossBar.setTitle("§cTimer stopped");
            return;
        }

        if (timeUntilNew != -1) {
            timeUntilNew--;

            if (timeUntilNew <= 0) {
                timeUntilNew = -1;
                time = random.nextInt(5*60 - 2*60) + 2*60;
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                height = random.nextInt((players.get(random.nextInt(players.size())).getWorld().getMaxHeight() -1)) + 1;
                count = 0;
            }

            bossBar.setColor(BarColor.WHITE);
            bossBar.setProgress(1);
            bossBar.setTitle("§7Waiting...");
            return;
        }
        count++;
        if (count >= time) {

            List<Player> playersOnTheFalseHeight = checkPlayersHeight(height);

            for (Player currentPlayer : playersOnTheFalseHeight) {
                Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.FORCE_HEIGHT_FAIL.get().replace("%player%", currentPlayer.getName()).replace("%height%", currentPlayer.getLocation().getBlockY() + ""));
            }

            timeUntilNew = random.nextInt(7*60 - 5*60) + 5*60;
            if (playersOnTheFalseHeight.isEmpty()) {
                Bukkit.broadcastMessage(Prefix.CHALLENGES.get() + Translation.FORCE_HEIGHT_COMPLETE);
            } else {
                ServerManager.simulateChallengeEnd(playersOnTheFalseHeight.get(0), ChallengeEndCause.PLAYER_CHALLENGE_FAIL);
            }
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

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.IRON_BOOTS, ItemTranslation.FORCE_HIGH).hideAttributes().build();
    }

}