package net.codingarea.challenges.plugin.challenges.implementation.challenge;
import net.anweisen.utilities.bukkit.utils.logging.Logger;

import net.anweisen.utilities.bukkit.utils.misc.MinecraftVersion;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.challenges.annotations.ExcludeFromRandomChallenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSProvider;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.PacketBorder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    private final Map<World, Location> worldCenters = new HashMap<>();

    private final boolean useAPI = MinecraftVersion.current().isNewerOrEqualThan(MinecraftVersion.V1_19);

    private UUID bestPlayerUUID = null;
    private int bestPlayerLevel = 0;

    private List<PotionEffectType> effectTypes = new ArrayList<>();


    public LevelEffectChallenge() {
        super(MenuType.CHALLENGES);
        setCategory(SettingCategory.WORLD);
    }

    @Override
    protected void onEnable() {
        updateBorderSize(false);
        worldCenters.put(ChallengeAPI.getGameWorld(World.Environment.NORMAL), getDefaultWorldSpawn());
    }

    @Override
    protected void onDisable() {
        borderReset();
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.ENCHANTING_TABLE, Message.forName("item-level-border-challenges"));
    }

    public void checkBorderSize(boolean animate) {
        updateBorderSize(animate);
    }

    public boolean isOutsideBorder(Location location) {
        return false;
    }

    private Location getDefaultWorldSpawn() {
        World world = ChallengeAPI.getGameWorld(World.Environment.NORMAL);
        if (worldCenters.containsKey(world)) {
            return worldCenters.get(world);
        }
        Location location = world.getSpawnLocation();
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        return location;
    }

    private void updateBorderSize(boolean animate) {
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

    public void borderReset() {
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onTimerStart() {
        if (!isEnabled()) return;
        checkBorderSize(false);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLevelChange(@Nonnull PlayerLevelChangeEvent event) {
        if (!shouldExecuteEffect()) return;
        checkBorderSize(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> checkBorderSize(false), 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(@Nonnull PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> checkBorderSize(false), 1);
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
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            checkBorderSize(false);
        }, 1);
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!shouldExecuteEffect()) return;
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (!event.isCancelled()) return;
        if (isOutsideBorder(event.getEntity().getLocation()) &&
                !isOutsideBorder(event.getDamager().getLocation())) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(@Nonnull PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL &&
                event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        World world = event.getTo().getWorld();
        if (world == null) return;
        if (world == Challenges.getInstance().getWorldManager().getExtraWorld()) return;
        Location location = event.getTo().getBlock().getLocation().add(0.5, 0, 0.5);
        if (!worldCenters.containsKey(world)) worldCenters.put(world, location);
    }

    @Override
    public void loadGameState(@NotNull Document document) {
        bestPlayerLevel = document.getInt("level");
        String uuid = document.getString("uuid");
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

        if (uuid != null) {
            bestPlayerUUID = UUID.fromString(uuid);
        }
        worldCenters.clear();
        Document worlds = document.getDocument("worlds");
        for (String worldName : worlds.keys()) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            worldCenters.put(world, worlds.getInstance(worldName, Location.class));
        }

        if (isEnabled()) {
            checkBorderSize(false);
        }
    }

    @Override
    public void writeGameState(@NotNull Document document) {
        Logger.error("writeGameState");
        document.set("level", bestPlayerLevel);
        GsonDocument doc = new GsonDocument();
        worldCenters.forEach((world, location) -> doc.set(world.getName(), location));
        document.set("worlds", doc);
        document.set("uuid", bestPlayerUUID);
        document.set("loadedEffects", effectTypes.stream().map(PotionEffectType::getId).collect(Collectors.toList()));
    }
}
