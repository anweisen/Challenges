package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
    updateBorderSize(false);
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

  public void checkBorderSize(boolean animate) {
    Player currentBestPlayer = bestPlayerUUID != null ? Bukkit.getPlayer(bestPlayerUUID) : null;

    for (Player player : ChallengeAPI.getIngamePlayers()) {
      int level = player.getLevel();

      if (bestPlayerUUID == null) {
        bestPlayerLevel = level;
        bestPlayerUUID = player.getUniqueId();
        updateBorderSize(animate);
        // Checks if the player is better than the saved level or if online the current player level.
        // Checking with the player instance is reqired to fix issues with dying and the level of the best player being 0.
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
      if (isOutsideBorder(world, player.getLocation())) {
        teleportInsideBorder(world, player);
      }
    });
  }

  public boolean isOutsideBorder(@NotNull World world, Location location) {
    WorldBorder worldBorder = world.getWorldBorder();
    return !worldBorder.isInside(location);
//    double size = worldBorder.getSize() / 2;
//    double dis = Math.max(Math.abs(location.getBlockX()),
//        Math.abs(location.getBlockZ()));
//
//    return dis >= size;
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
      sendPacketBorder(player, location, newSize, animate);
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
  public void onEntityDamageByEntity(PlayerInteractEntityEvent event) {
    if (!shouldExecuteEffect()) return;
    System.out.println(event.isCancelled());
    }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!shouldExecuteEffect()) return;
    if (event.getDamager().getType() != EntityType.PLAYER) return;
    System.out.println(event.isCancelled());
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
    if (event.getCause() != TeleportCause.NETHER_PORTAL &&
        event.getCause() != TeleportCause.END_PORTAL) return;
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

  private static void sendPacketBorder(@Nonnull Player player, Location center, int size, boolean animate) {

    // First try with 1.13-1.16 logic
    try {
      Class<?> borderClass = getMinecraftServerClass("WorldBorder");
      Object borderInstance = borderClass.getDeclaredConstructor().newInstance();

      Class<?> craftWorldClass = getCraftBukkitClass("CraftWorld");
      Object craftWorld = craftWorldClass.cast(center.getWorld());
      Method getHandleMethod = craftWorldClass.getDeclaredMethod("getHandle");
      Object nmsWorld = getHandleMethod.invoke(craftWorld);

      borderClass.getDeclaredField("world").set(borderInstance, nmsWorld);

      System.out.println(borderInstance.toString());

    } catch (Exception exception) {
      Logger.warn("First try failed");
      // Second try with 1.17+ logic
      try {
        Class<?> borderClass = getMojMappedClass("world.level.border.WorldBorder");
        Object borderInstance = borderClass.getDeclaredConstructor().newInstance();

        Class<?> craftWorldClass = getCraftBukkitClass("CraftWorld");
        Object craftWorld = craftWorldClass.cast(center.getWorld());
        Method getHandleMethod = craftWorldClass.getDeclaredMethod("getHandle");
        Object serverWorld = getHandleMethod.invoke(craftWorld);

        System.out.println(serverWorld);
      } catch (Exception exception1) {
        Logger.warn("Second try failed");
        exception1.printStackTrace();
      }
    }
  }

  private static Class<?> getMojMappedClass(@Nonnull String name) throws ClassNotFoundException {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    return cl.loadClass("net.minecraft." + name);
  }

  private static Class<?> getMinecraftServerClass(@Nonnull String name) throws ClassNotFoundException {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    return cl.loadClass("net.minecraft.server." + getVersion() + "." + name);
  }

  private static Class<?> getCraftBukkitClass(@Nonnull String name) throws ClassNotFoundException {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    return cl.loadClass("org.bukkit.craftbukkit." + getVersion() + "." + name);
  }

  public static String getVersion() {
    String ver = Bukkit.getServer().getClass().getPackage().getName();
    return ver.substring(ver.lastIndexOf('.') + 1);
  }

}