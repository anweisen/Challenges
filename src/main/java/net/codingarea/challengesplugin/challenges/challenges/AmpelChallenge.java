package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.manager.scoreboard.ScoreboardManager;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author anweisen & Dominik
 * Challenges developed on 10-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class AmpelChallenge extends AdvancedChallenge implements Listener, ISecondExecutor {

    private final Random random = new Random();
    private int ampel = 0;
    private BossBar bossBar;

    public AmpelChallenge() {
        super(MenuType.CHALLENGES, 10);
        this.value = 5;
        setNextSeconds();
    }

    @Override
    public void onValueChange(ChallengeEditEvent event) {
        if (nextActionInSeconds > (value*60) || nextActionInSeconds < ((value-1)*60)) {
            setNextSeconds();
        }
    }

    @Override
    public void onTimeActivation() {
        nextActionInSeconds = -1;

        new BukkitRunnable() {
            int count = 1;
            @Override
            public void run() {

                if (count >= 3) {

                    ampel = 2;
                    onSecond();
                    Bukkit.getOnlinePlayers().forEach(AnimationSound.BASS_SOUND::play);
                    cancel();

                    Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
                        ampel = 0;
                        onSecond();
                        Bukkit.getOnlinePlayers().forEach(AnimationSound.PLOP_SOUND::play);
                    }, (random.nextInt(5 + 1 - 3) + 3) * 20);
                    return;

                }

                ampel = 1;
                onSecond();
                Bukkit.getOnlinePlayers().forEach(AnimationSound.PLING_SOUND::play);

                count++;
            }
        }.runTaskTimer(Challenges.getInstance(), 0, 20);

    }

    @Override
    public void onSecond() {
        if (nextActionInSeconds < 0) setNextSeconds();
        if (!this.enabled) return;

        if (!Challenges.timerIsStarted()) {
            bossBar.setColor(BarColor.RED);
            bossBar.setProgress(1);
            bossBar.setTitle("§cTimer stopped");
            return;
        }

        String c = "■";
        String title =  ("§8§l{ §1xxx §8§l} §8§l{ §2xxx §8§l} §8§l{ §3xxx §8§l}").replace("x", c);

        if (ampel == 0) {
            bossBar.setColor(BarColor.GREEN);
            title = title.replace("1", "a");
            title = title.replace("2", "7");
            title = title.replace("3", "7");
        } else if (ampel == 1) {
            bossBar.setColor(BarColor.YELLOW);
            title = title.replace("1", "7");
            title = title.replace("2", "6");
            title = title.replace("3", "7");
        } else {
            bossBar.setColor(BarColor.RED);
            title = title.replace("1", "7");
            title = title.replace("2", "7");
            title = title.replace("3", "c");
        }
        this.bossBar.setTitle(title);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!this.enabled) return;
        this.bossBar.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!isEnabled() || !Challenges.timerIsStarted() || ampel != 2) return;
        if (Utils.onlyMovedView(event.getFrom(), event.getTo())) return;
        setNextSeconds();
        event.getPlayer().damage(event.getPlayer().getMaxHealth());
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        bossBar = Bukkit.createBossBar("§7Waiting...", BarColor.WHITE, BarStyle.SOLID);
        ScoreboardManager.getInstance().activateBossBar(bossBar);
    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        ScoreboardManager.getInstance().deactivateBossBar(bossBar);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.LIME_STAINED_GLASS, ItemTranslation.TRAFFIC_LIGHT).build();
    }

    private void setNextSeconds() {
        nextActionInSeconds = getRandomSeconds();
    }

    private int getRandomSeconds() {
        int max = Utils.getRandomSecondsUp(value*60);
        int min = Utils.getRandomSecondsDown(value*60);
        return random.nextInt(max - min) + min;
    }

}