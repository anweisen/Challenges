package net.codingarea.challenges.plugin.challenges.implementation.challenge;
import net.anweisen.utilities.bukkit.utils.logging.Logger;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.challenges.annotations.ExcludeFromRandomChallenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
@Since("2.2.3")
@ExcludeFromRandomChallenges
public class LevelEffectChallenge extends Setting {

    private List<PotionEffectType> effectTypes = new ArrayList<>();


    public LevelEffectChallenge() {
        super(MenuType.CHALLENGES);
        setCategory(SettingCategory.WORLD);
    }

    @Override
    protected void onEnable() {
        updateEffects();
    }

    @Override
    protected void onDisable() {

    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.ENCHANTING_TABLE, Message.forName("item-level-border-challenges"));
    }

    public void checkBorderSize() {
        updateEffects();
    }

    private void updateEffects() {
        Logger.error("updateBorderSize");
        for (World world : ChallengeAPI.getGameWorlds()) {
            if (world.getPlayers().isEmpty()) {
                continue;
            }
            for(Player player: world.getPlayers()){
                setEffect(player);
            }
        }
    }

    private void setEffect(Player player){
        Logger.error("setEffect");
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        int level = player.getLevel();
        PotionEffectType potionEffectType = effectTypes.get(level);
        Logger.error("potionEffectType"+potionEffectType.toString());
        PotionEffect effect = potionEffectType.createEffect(10 * 60 * 20, 5);
        player.addPotionEffect(effect);
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onTimerStart() {
        if (!isEnabled()) return;
        checkBorderSize();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLevelChange(@Nonnull PlayerLevelChangeEvent event) {
        if (!shouldExecuteEffect()) return;
        checkBorderSize();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, this::checkBorderSize, 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(@Nonnull PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, this::checkBorderSize, 1);
    }

    /**
     * Teleports the player back inside border if spawnpoint is outside of it.
     * Will rarely occur by beds since minecraft blocks bed respawn outside the border but
     * because of the random spawning mechanic at the world spawn.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(@Nonnull PlayerRespawnEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, this::checkBorderSize, 1);
    }

    /**
     * Execute level change event when dying instead of respawning like spigot does it
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(@Nonnull PlayerDeathEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getEntity())) return;
        PlayerLevelChangeEvent lvlEvent = new PlayerLevelChangeEvent(
                event.getEntity(), event.getEntity().getLevel(), 0);
        event.getEntity().setLevel(0);
        onLevelChange(lvlEvent);
    }

    @Override
    public void loadGameState(@NotNull Document document) {
        Logger.error("LoadGameState");
        List<Integer> loadedEffects = document.getIntegerList("loadedEffects");
        List<PotionEffectType> allEffects = Arrays.asList(PotionEffectType.values());
        if(loadedEffects.isEmpty()){
            effectTypes = allEffects;
            Collections.shuffle(effectTypes);
        }
        else{
            Map<Integer, PotionEffectType> potionEffectTypeMap = new HashMap<>();
            for(PotionEffectType effect: allEffects){
                potionEffectTypeMap.put(effect.getId(), effect);
            }
            effectTypes = loadedEffects.stream().map(potionEffectTypeMap::get).collect(Collectors.toList());
        }
        if (isEnabled()) {
            checkBorderSize();
        }
    }

    @Override
    public void writeGameState(@NotNull Document document) {
        Logger.error("writeGameState");
        document.set("loadedEffects", effectTypes.stream().map(PotionEffectType::getId).collect(Collectors.toList()));
    }
}
