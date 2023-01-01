package net.codingarea.challenges.plugin.challenges.implementation.challenge;

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
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.bukkit.nms.PacketBorder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
@Since("2.2.3")
@ExcludeFromRandomChallenges
public class LevelBorderChallenge extends Setting {
    private final Map<World, Location> worldCenters = new HashMap<>();
    private final Map<UUID, WorldBorder> playerWorldBorders = new HashMap<>();
    private final Map<UUID, PacketBorder> playerPacketBorders = new HashMap<>();

    private UUID bestPlayerUUID = null;
    private int bestPlayerLevel = 0;

    public LevelBorderChallenge() {
        super(MenuType.CHALLENGES);
        setCategory(SettingCategory.WORLD);
    }

    @Override
    protected void onEnable() {
        bossbar.setContent((bar, player) -> {
            bar.setTitle(Message.forName("bossbar-level-border").asString(bestPlayerLevel));
        });
        bossbar.show();
        updateBorderSize(false);
        worldCenters.put(ChallengeAPI.getGameWorld(World.Environment.NORMAL), getDefaultWorldSpawn());
    }

    @Override
    protected void onDisable() {
        borderReset();
        bossbar.hide();
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.ENCHANTING_TABLE, Message.forName("item-level-border-challenges"));
    }

    public void checkBorderSize(boolean animate) {
        Player currentBestPlayer = bestPlayerUUID != null ? Bukkit.getPlayer(bestPlayerUUID) : null;

        for (Player player : ChallengeAPI.getIngamePlayers()) {
            int level = player.getLevel();

            if(bestPlayerUUID != null) {
                bestPlayerLevel = level;
                bestPlayerUUID = player.getUniqueId();
                updateBorderSize(animate);
                // Checks if the player is better than the saved level or if online the current player level.
                // Checking with the player instance is required to fix issues with dying and the level of the best player being 0.
            } else if (level > bestPlayerLevel || (currentBestPlayer != null && level > currentBestPlayer.getLevel())) {
                bestPlayerLevel = level;
                bestPlayerUUID = player.getUniqueId();
                updateBorderSize(animate);
            } else if (player.getUniqueId().equals(bestPlayerUUID)) {
                bestPlayerLevel = level;
                updateBorderSize(animate);
            }
        }

        bossbar.update();
    }

    public void playerSpawnTeleport() {
        broadcastFiltered(player -> {
            World world = player.getWorld();
            if(isOutsideBorder(world, player.getLocation())) {
                teleportInsideBorder(world, player);
            }
        });
    }

    public boolean isOutsideBorder(@NotNull World world, Location location) {
        WorldBorder worldBorder = world.getWorldBorder();
        return !worldBorder.isInside(location);
    }

    public void teleportInsideBorder(@NotNull World world, Player player) {
        if(world.getEnvironment() != World.Environment.NORMAL) {
            player.teleport(world.getWorldBorder().getCenter());
        } else {
            player.teleport(world.getHighestBlockAt(
                    world.getWorldBorder().getCenter()).getLocation().add(0.5, 1, 0.5));
        }
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
        for (World world : ChallengeAPI.getGameWorlds()) {
            if (world.getPlayers().isEmpty()) {
                continue;
            }
            updateBorderSize(world, animate);
        }
    }

    private void updateBorderSize(@Nonnull World world, boolean animate) {
        Location location = worldCenters.get(world);
        if (location == null) return;
        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(location);
        worldBorder.setWarningDistance(0);
        int newSize = bestPlayerLevel + 1;

        for (Player player : world.getPlayers()) {
            sendBorder(player, location, newSize, animate);
        }
        if (animate) {
            worldBorder.setSize(newSize, 1);
        } else {
            worldBorder.setSize(newSize);
        }
    }

    public void borderReset() {
        for (World world : ChallengeAPI.getGameWorlds()) {
            world.getWorldBorder().reset();
        }
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onTimerStart() {
        if (!isEnabled()) return;
        checkBorderSize(false);
        playerSpawnTeleport();
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
        checkBorderSize(false);
        playerSpawnTeleport();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(@Nonnull PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        checkBorderSize(false);
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
        Bukkit.getScheduler().runTaskLater(plugin, this::playerSpawnTeleport, 1);
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
        if (isOutsideBorder(event.getEntity().getWorld(), event.getEntity().getLocation()) &&
                !isOutsideBorder(event.getDamager().getWorld(), event.getDamager().getLocation())) {
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
        if (worldCenters.containsKey(world)) return;
        if (world == null) return;
        if (world == Challenges.getInstance().getWorldManager().getExtraWorld()) return;
        Location location = event.getTo().getBlock().getLocation().add(0.5, 0, 0.5);
        worldCenters.put(world, location);
        updateBorderSize(world, false);
    }

    @Override
    public void loadGameState(@NotNull Document document) {
        bestPlayerLevel = document.getInt("level");
        String uuid = document.getString("uuid");
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
        document.set("level", bestPlayerLevel);
        GsonDocument doc = new GsonDocument();
        worldCenters.forEach((world, location) -> {
            doc.set(world.getName(), location);
        });
        document.set("worlds", doc);
        document.set("uuid", bestPlayerUUID);
    }

    private void sendBorder(@Nonnull Player player, Location center, int size, boolean animate) {
        boolean useAPI = MinecraftVersion.current().isNewerOrEqualThan(MinecraftVersion.V1_19);
        if(useAPI) {
            WorldBorder worldBorder = playerWorldBorders.computeIfAbsent(player.getUniqueId(), uuid -> {
                WorldBorder border = Bukkit.createWorldBorder();
                player.setWorldBorder(border);
                return border;
            });
            worldBorder.setCenter(center);
            if(animate) {
                worldBorder.setSize(size, 1);
            } else {
                worldBorder.setSize(size);
            }
        } else {
            //ToDo test if this works for all versions
            PacketBorder packetBorder = playerPacketBorders.computeIfAbsent(player.getUniqueId(), uuid -> new PacketBorder());
            packetBorder.setCenter(center.getX(), center.getZ());
            packetBorder.setSize(size);
            packetBorder.send(player, PacketBorder.UpdateType.values());
        }
    }
}
