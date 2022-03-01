package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.challenges.annotations.ExcludeFromRandomChallenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.NotNull;

/**
 * @author EinfachBeez | https://github.com/einfachbeez
 * @since 2.1.0
 */
@Since("2.1.0")
@ExcludeFromRandomChallenges
public class LevelBorderChallenge extends Setting {

  private final Map<World, Location> worldCenters = new HashMap<>();

  private UUID bestPlayerUUID = null;
  private int bestPlayerLevel = 0;

  public LevelBorderChallenge() {
    super(MenuType.CHALLENGES);
  }

  @Override
  protected void onEnable() {
    bossbar.setContent((bar, player) -> {
      bar.setTitle(Message.forName("bossbar-level-border").asString(bestPlayerLevel));
    });
    bossbar.show();
    updateBorderSize();
    worldCenters.put(ChallengeAPI.getGameWorld(Environment.NORMAL), getDefaultWorldSpawn());
  }

  @Override
  protected void onDisable() {
    borderReset();
    bossbar.hide();
  }

  @NotNull
  @Override
  public ItemBuilder createDisplayItem() {
    return new ItemBuilder(Material.ENCHANTING_TABLE,Message.forName("item-level-border-challenges"));
  }

  public void checkBorderSize() {
    for (Player player : ChallengeAPI.getIngamePlayers()) {
      int level = player.getLevel();

      if (bestPlayerUUID == null) {
        bestPlayerLevel = level;
        bestPlayerUUID = player.getUniqueId();
        updateBorderSize();
      } else if (player.getUniqueId().equals(bestPlayerUUID)) {
        bestPlayerLevel = level;
        updateBorderSize();
      } else if (level > bestPlayerLevel) {
        bestPlayerLevel = level;
        bestPlayerUUID = player.getUniqueId();
        updateBorderSize();
      }
    }
    bossbar.update();
  }

  public void playerSpawnTeleport() {
    broadcastFiltered(player -> {
      World world = player.getWorld();
      if (isOutsideBorder(world, player)) {
        teleportInsideBorder(world, player);
      }
    });
  }

  public boolean isOutsideBorder(@NotNull World world, Player player) {
    WorldBorder worldBorder = world.getWorldBorder();
    double size = worldBorder.getSize() / 2;
    double dis = Math.max(Math.abs(player.getLocation().getBlockX()),
        Math.abs(player.getLocation().getBlockZ()));

    return dis >= size;
  }

  public void teleportInsideBorder(@Nonnull World world, Player player) {
    if (world.getEnvironment() != Environment.NORMAL) {
      player.teleport(world.getWorldBorder().getCenter());
    } else {
      player.teleport(world.getHighestBlockAt(
          world.getWorldBorder().getCenter()).getLocation().add(0.5, 1, 0.5));
    }
  }

  private Location getDefaultWorldSpawn() {
    World world = ChallengeAPI.getGameWorld(Environment.NORMAL);
    if (worldCenters.containsKey(world)) {
      return worldCenters.get(world);
    }
    Location location = world.getSpawnLocation();
    location.setX(location.getBlockX() + 0.5);
    location.setZ(location.getBlockZ() + 0.5);
    return location;
  }

  private void updateBorderSize() {
    for (World world : ChallengeAPI.getGameWorlds()) {
      if (world.getPlayers().isEmpty()) {
        continue;
      }
      updateBorderSize(world);
    }
  }

  private void updateBorderSize(@NotNull World world) {
    Location location = worldCenters.get(world);
    if (location == null) return;
    WorldBorder worldBorder = world.getWorldBorder();
    worldBorder.setCenter(location);
    worldBorder.setSize(bestPlayerLevel + 1);
  }

  public void borderReset() {
    for (World world : ChallengeAPI.getGameWorlds()) {
      world.getWorldBorder().reset();
    }
  }

  @TimerTask(status = TimerStatus.RUNNING, async = false)
  public void onTimerStart() {
    if (!isEnabled()) return;
    checkBorderSize();
    playerSpawnTeleport();
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onLevelUp(@Nonnull PlayerLevelChangeEvent event) {
    if (!shouldExecuteEffect()) return;
    checkBorderSize();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPLayerJoin(@Nonnull PlayerJoinEvent event) {
    if (!shouldExecuteEffect()) return;
    checkBorderSize();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerLeave(@Nonnull PlayerQuitEvent event) {
    checkBorderSize();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onRespawn(@Nonnull PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      for (World world : ChallengeAPI.getGameWorlds()) {
        if (isOutsideBorder(world, player)) {
          teleportInsideBorder(world, player);
        }
      }
    }, 1);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTeleport(@Nonnull PlayerTeleportEvent event) {
    if (event.getTo() == null) return;
    if (!shouldExecuteEffect()) return;
    if (ignorePlayer(event.getPlayer())) return;
    if (event.getCause() != TeleportCause.NETHER_PORTAL &&
        event.getCause() != TeleportCause.END_PORTAL) return;
    World world = event.getTo().getWorld();
    if (worldCenters.containsKey(world)) return;
    if (world == null) return;
    if (world == Challenges.getInstance().getWorldManager().getExtraWorld()) return;
    Location location = event.getTo().getBlock().getLocation().add(0.5, 0, 0.5);
    worldCenters.put(world, location);
    updateBorderSize(world);
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
      checkBorderSize();
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
}
