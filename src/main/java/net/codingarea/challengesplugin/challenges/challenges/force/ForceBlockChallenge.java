package net.codingarea.challengesplugin.challenges.challenges.force;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
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
import net.codingarea.challengesplugin.utils.RandomizerUtil;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
public class ForceBlockChallenge extends Setting implements ISecondExecutor, CommandExecutor {

    private BossBar bossBar;

    private int count;
    private int timeUntilNew;
    private int time;
    private Material material;

    private Random maxRandom,
                   materialRandom,
                   newRandom;

    public ForceBlockChallenge() {
        super(MenuType.CHALLENGES);
        time = 0;
        count = 0;
        material = Material.AIR;
        newRandom = new Random();
        timeUntilNew = newRandom.nextInt(7*60 - 5*60) + 5*60;
        newRandom = null;
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        maxRandom = new Random();
        materialRandom = new Random();
        newRandom = new Random();
        bossBar = Bukkit.createBossBar("§7Waiting...", BarColor.WHITE, BarStyle.SOLID);
        ScoreboardManager.getInstance().activateBossBar(bossBar);
    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        maxRandom = null;
        materialRandom = null;
        newRandom = null;
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
                setNewMaterial();
            }

            bossBar.setColor(BarColor.WHITE);
            bossBar.setProgress(1);
            bossBar.setTitle("§7Waiting...");
            return;

        }

        count++;
        if (count >= time) {

            List<Player> playersOnTheFalseHeight = checkPlayersBlock(material);

            if (playersOnTheFalseHeight.isEmpty()) {
                Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.FORCE_BLOCK_COMPLETE.get());
                AnimationSound.KLING_SOUND.broadcast();
            } else {
                for (Player currentPlayer : playersOnTheFalseHeight) {
                    Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.FORCE_BLOCK_FAIL.get().replace("%player%", currentPlayer.getName()).replace("%block%", "§e" + Utils.getEnumName(currentPlayer.getLocation().getBlock().getType()) + " §7/ §e " + Utils.getEnumName(currentPlayer.getLocation().clone().add(0, -1, 0).getBlock().getType())));
                }
                ServerManager.simulateChallengeEnd(playersOnTheFalseHeight.get(0), ChallengeEndCause.PLAYER_CHALLENGE_FAIL);
                setNewMaterial();
            }

            timeUntilNew = newRandom.nextInt(7*60 - 5*60) + 5*60;

        }

        bossBar.setColor(BarColor.GREEN);
        bossBar.setTitle("§7Block §e" + Utils.getEnumName(material.name()) + " §7in §a" + (ChallengeTimer.getTimeDisplay(this.time - this.count)));
        bossBar.setProgress(((double) count / time));

    }

    private void setNewMaterial() {
        timeUntilNew = -1;
        time = maxRandom.nextInt(5*60 - 2*60) + 2*60;
        List<Material> materials = RandomizerUtil.getForceBlockBlocks();
        material = materials.get(materialRandom.nextInt(materials.size()));
        count = 0;
    }

    /**
     * @return returns if the players not on the right position
     */
    private List<Player> checkPlayersBlock(Material material) {

        List<Player> list = new ArrayList<>();

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

            if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;

            Location location = currentPlayer.getLocation();
            List<Block> blocks = new ArrayList<>();

            blocks.addAll(getBlocksAround(location.clone()));
            blocks.addAll(getBlocksAround(location.clone().add(0, 1, 0)));
            blocks.addAll(getBlocksAround(location.subtract(0,1,0)));

            blocks.removeIf(block -> block.getType() != material);

            if (blocks.isEmpty()) list.add(currentPlayer);

        }

        return list;

    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.DIAMOND_BOOTS, ItemTranslation.FORCE_BLOCK).hideAttributes().build();
    }

    public static List<Block> getBlocksAround(Location middle) {

        List<Block> list = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                list.add(middle.clone().add(i, 0, j).getBlock());
            }
        }

        return list;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!enabled || !Challenges.timerIsStarted()) {
            sender.sendMessage(Prefix.CHALLENGES + Translation.FEATURE_DISABLED.get());
            return true;
        }

        sender.sendMessage(Prefix.CHALLENGES + Translation.FORCE_BLOCK_SKIP.get());
        setNewMaterial();

        return true;

    }
}