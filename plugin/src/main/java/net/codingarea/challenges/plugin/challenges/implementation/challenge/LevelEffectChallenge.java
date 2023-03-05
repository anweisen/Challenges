package net.codingarea.challenges.plugin.challenges.implementation.challenge;
import net.anweisen.utilities.bukkit.utils.logging.Logger;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
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
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
@Since("2.2.3")
@ExcludeFromRandomChallenges
public class LevelEffectChallenge extends SettingModifier
{

    private List<PotionEffectType> effectTypes = new ArrayList<>();
    private List<Integer> amplifierList = new ArrayList<>();

    public LevelEffectChallenge() {
        super(MenuType.CHALLENGES, 1, 8, 5);
        setCategory(SettingCategory.EFFECT);
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
        return new ItemBuilder(Material.EXPERIENCE_BOTTLE, Message.forName("item-level-effect-challenges"));
    }

    public void updateEffects() {
        Logger.error("updateEffects");
        for (World world : ChallengeAPI.getGameWorlds()) {
            if (world.getPlayers().isEmpty()) {
                continue;
            }
            for(Player player: world.getPlayers()){
                setEffect(player);
            }
        }
    }

    private int getNewAmplifier(){
        Logger.error("getNewAmplifier");
        return getValue() == getMaxValue() ? (int) (Math.random() * getValue()): (getValue() - 1);
    }

    private void setEffect(Player player){
        Logger.error("setEffect");
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        int level = player.getLevel();
        Logger.error("level "+ level);
        while(level + 10 >= effectTypes.size()){
            Logger.error("effectTypes.size()"+effectTypes.size());
            effectTypes.add(RandomPotionEffectChallenge
                .getRandomEffect());
        }
        while(level + 10 >= amplifierList.size()){
            Logger.error("amplifierList.size()"+amplifierList.size());
            amplifierList.add(getNewAmplifier());
        }

        PotionEffectType potionEffectType = effectTypes.get(level);
        int amplifier = amplifierList.get(level);

        Logger.error("potionEffectType "+potionEffectType.toString());
        Logger.error("amplifier "+amplifier);

        PotionEffect effect = potionEffectType.createEffect(10 * 60 * 20, amplifier);
        player.addPotionEffect(effect);
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onTimerStart() {
        if (!isEnabled()) return;
        updateEffects();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLevelChange(@Nonnull PlayerLevelChangeEvent event) {
        if (!shouldExecuteEffect()) return;
        updateEffects();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, this::updateEffects, 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(@Nonnull PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, this::updateEffects, 1);
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
        Bukkit.getScheduler().runTaskLater(plugin, this::updateEffects, 1);
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
        super.loadGameState(document);
        Logger.error("LoadGameState");
        List<Integer> loadedEffects = document.getIntegerList("loadedEffects");
        Logger.error("loadedEffects", loadedEffects.toString());
        List<PotionEffectType> allEffects = Arrays.asList(PotionEffectType.values());
        amplifierList = document.getIntegerList("amplifier");
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
            updateEffects();
        }
    }

    @Override
    public void writeGameState(@NotNull Document document) {
        super.writeGameState(document);
        List<Integer> effects = effectTypes.stream().map(PotionEffectType::getId).collect(Collectors.toList());
        Logger.error("writeGameState", effects.toString());
        document.set("loadedEffects", effects);
        document.set("amplifier", amplifierList);
    }


    @Nonnull
    @Override
    public ItemBuilder createSettingsItem() {
        int amount = 1;
        if(isEnabled()){
            amount = getValue();
        }
        if(amount == getMaxValue()){
           return super.createSettingsItem().setType(Material.COMMAND_BLOCK).setAmount(1);
        }
        return super.createSettingsItem().amount(isEnabled() ? getValue() : 1);
    }

    @Override
    public void playValueChangeTitle() {
        ChallengeHelper.playChallengeHeartsValueChangeTitle(this, getValue() * 100);
    }


    @Override
    protected void onValueChange() {
        amplifierList = new ArrayList<>();
        updateEffects();
    }

    @Nullable
    @Override
    protected String[] getSettingsDescription() {
        return Message.forName(getValue() == getMaxValue()? "item-level-random-effect-description" :"item-level-effect-description").asArray(getValue());
    }



}
