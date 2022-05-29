package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.0
 */
public abstract class ForceBattleGoal extends MenuGoal {
    private ItemStack jokerItem;
    private Map<Player, ArmorStand> displayStands;

    protected final Map<UUID, Integer> jokerUsed = new HashMap<>();

    public ForceBattleGoal(@NotNull MenuType menu, @NotNull Message title) {
        super(menu, title);
        setCategory(SettingCategory.SCORE_POINTS);

        registerSetting("jokers", new NumberSubSetting(
                () -> new ItemBuilder(Material.BARRIER, Message.forName("item-force-battle-goal-jokers")),
                value -> null,
                value -> "§e" + value,
                1,
                20,
                5
        ));
    }

    @Override
    protected void onEnable() {
        jokerItem = new ItemBuilder(Material.BARRIER, "§cJoker").build();

        displayStands = new HashMap<>();

        broadcastFiltered(this::updateJokersInInventory);
        broadcastFiltered(this::updateDisplayStand);

        scoreboard.show();
    }

    @Override
    protected void onDisable() {
        if (jokerItem == null) return; // Disable through plugin disable
        broadcastFiltered(this::updateJokersInInventory);
        jokerItem = null;
        displayStands.values().forEach(Entity::remove);
        displayStands = null;
        scoreboard.hide();
    }

    private void updateJokersInInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        boolean enabled = isEnabled() && ChallengeAPI.isStarted();
        int usableJokers = getUsableJokers(player.getUniqueId());

        int jokersInInventory = 0;

        for (ItemStack itemStack : new LinkedList<>(Arrays.asList(inventory.getContents()))) {
            if (jokerItem.isSimilar(itemStack)) {
                if (enabled) {
                    jokersInInventory += itemStack.getAmount();
                    if (jokersInInventory >= usableJokers) {
                        int jokersToSubtract = jokersInInventory - usableJokers;
                        jokersInInventory -= jokersToSubtract;
                        itemStack.setAmount(itemStack.getAmount() - jokersToSubtract);
                    }
                } else {
                    inventory.removeItem(itemStack);
                }
            }

        }

        if (enabled) {
            if (jokersInInventory < usableJokers) {
                ItemStack clone = jokerItem.clone();
                clone.setAmount(usableJokers - jokersInInventory);
                InventoryUtils.dropOrGiveItem(inventory, player.getLocation(), clone);
            }
        }

    }

    public void updateDisplayStand(Player player) {
        ArmorStand armorStand = displayStands.computeIfAbsent(player, player1 -> {
            World world = player1.getWorld();
            ArmorStand entity = (ArmorStand) world
                    .spawnEntity(player1.getLocation().clone().add(0, getDisplayStandYOffset(), 0), EntityType.ARMOR_STAND);
            entity.setInvisible(true);
            entity.setInvulnerable(true);
            entity.setGravity(false);
            entity.setMarker(true);
            entity.setSilent(true);
            return entity;
        });
        armorStand.teleport(player.getLocation().clone().add(0, getDisplayStandYOffset(), 0));
        armorStand.setVelocity(player.getVelocity());

        handleDisplayStandUpdate(player, armorStand);
    }

    public abstract void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand);

    public abstract double getDisplayStandYOffset();

    @Override
    public void loadGameState(@NotNull Document document) {
        this.jokerUsed.clear();

        List<Document> players = document.getDocumentList("players");
        for (Document player : players) {
            UUID uuid = player.getUUID("uuid");
            int jokerUsed = player.getInt("jokerUsed");
            this.jokerUsed.put(uuid, jokerUsed);
        }

        if (isEnabled()) {
            if (ChallengeAPI.isStarted()) {
                broadcastFiltered(this::setRandomTargetIfCurrentlyNone);
            }
            scoreboard.update();
            broadcastFiltered(this::updateJokersInInventory);
            broadcastFiltered(this::updateDisplayStand);
        }
    }

    public abstract void setRandomTargetIfCurrentlyNone(Player player);

    public abstract void setRandomTarget(Player player);

    public abstract void handleTargetFound(Player player);

    public abstract void sendResult(@NotNull Player player);

    protected ChatColor getPlaceColor(int place) {
        switch (place) {
            case 1:
                return ChatColor.GOLD;
            case 2:
                return ChatColor.YELLOW;
            case 3:
                return ChatColor.RED;
            default:
                return ChatColor.GRAY;
        }
    }

    private int getUsableJokers(UUID uuid) {
        return Math.max(0, getJokers() - jokerUsed.getOrDefault(uuid, 0));
    }

    public void handleJokerUse(Player player) {
        int jokerUsed = this.jokerUsed.getOrDefault(player.getUniqueId(), 0);
        jokerUsed++;
        this.jokerUsed.put(player.getUniqueId(), jokerUsed);
        handleTargetFound(player);
        updateJokersInInventory(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        updateDisplayStand(event.getPlayer());
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onStart() {
        broadcastFiltered(this::setRandomTargetIfCurrentlyNone);
        broadcastFiltered(this::updateJokersInInventory);
    }

    @EventHandler
    public void onStatusChange(PlayerIgnoreStatusChangeEvent event) {
        if (!shouldExecuteEffect()) return;
        if (event.isNotIgnored()) {
            setRandomTargetIfCurrentlyNone(event.getPlayer());
            updateDisplayStand(event.getPlayer());
        } else {
            ArmorStand stand = displayStands.get(event.getPlayer());
            if (stand != null) {
                stand.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        setRandomTargetIfCurrentlyNone(event.getPlayer());
        updateDisplayStand(event.getPlayer());
        updateJokersInInventory(event.getPlayer());
    }

    @ScheduledTask(ticks = 1, async = false, timerPolicy = TimerPolicy.ALWAYS)
    public void onTick() {
        if (!isEnabled()) return;
        for (Player player : displayStands.keySet()) {
            updateDisplayStand(player);
        }
        broadcastFiltered(this::updateJokersInInventory);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        ArmorStand stand = displayStands.remove(event.getPlayer());
        if (stand != null) {
            stand.remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (jokerItem.isSimilar(event.getItem())) {
            handleJokerUse(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (jokerItem.isSimilar(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getEntity())) return;
        event.getDrops().removeIf(itemStack -> jokerItem.isSimilar(itemStack));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Bukkit.getScheduler().runTask(plugin, () -> {
            updateJokersInInventory(event.getPlayer());
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (jokerItem.isSimilar(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }

    private int getJokers() {
        return getSetting("jokers").getAsInt();
    }
}
