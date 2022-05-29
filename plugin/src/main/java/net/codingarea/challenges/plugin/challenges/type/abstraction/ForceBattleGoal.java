package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.*;
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
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.0
 */
public abstract class ForceBattleGoal<T> extends MenuGoal {
    private ItemStack jokerItem;
    private Map<Player, ArmorStand> displayStands;

    protected final Map<UUID, Integer> jokerUsed = new HashMap<>();
    protected final Map<UUID, List<T>> foundTargets = new HashMap<>();
    protected final Map<UUID, T> currentTarget = new HashMap<>();
    protected T[] targetsPossibleToFind;

    public ForceBattleGoal(@NotNull MenuType menu, @NotNull Message title) {
        super(menu, title);
        setCategory(SettingCategory.SCORE_POINTS);;

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

        targetsPossibleToFind = getTargetsPossibleToFind();

        broadcastFiltered(this::updateJokersInInventory);
        broadcastFiltered(this::updateDisplayStand);

        scoreboard.setContent((board, player) -> {
            List<Player> ingamePlayers = ChallengeAPI.getIngamePlayers();
            int emptyLinesAvailable = 15 - ingamePlayers.size();

            if (emptyLinesAvailable > 0) {
                board.addLine("");
                emptyLinesAvailable--;
            }

            for (int i = 0; i < ingamePlayers.size() && i < 15; i++) {
                Player ingamePlayer = ingamePlayers.get(i);
                T target = currentTarget.get(ingamePlayer.getUniqueId());
                String display = target == null ? Message.forName("none").asString()
                        : getTargetName(target);
                board.addLine(NameHelper.getName(ingamePlayer) + " §8» §e" + display);
            }

            if (emptyLinesAvailable > 0) {
                board.addLine("");
            }
        });
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
        targetsPossibleToFind = null;
    }

    protected abstract T[] getTargetsPossibleToFind();

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
        this.currentTarget.clear();
        this.foundTargets.clear();

        List<Document> players = document.getDocumentList("players");
        for (Document player : players) {
            UUID uuid = player.getUUID("uuid");

            T currentTarget = getTargetFromDocument(player, "currentTarget");

            if (currentTarget != null) {
                this.currentTarget.put(uuid, currentTarget);
            }
            List<T> foundItems = getListFromDocument(player, "foundTargets");
            this.foundTargets.put(uuid, foundItems);

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

    @Override
    public void writeGameState(@NotNull Document document) {
        List<Document> playersDocuments = new LinkedList<>();
        for (Map.Entry<UUID, T> entry : currentTarget.entrySet()) {
            List<T> foundItems = this.foundTargets.get(entry.getKey());
            int jokerUsed = this.jokerUsed.getOrDefault(entry.getKey(), 0);
            GsonDocument playerDocument = new GsonDocument();
            playerDocument.set("uuid", entry.getKey());
            playerDocument.set("currentTarget", entry.getValue());
            playerDocument.set("foundTargets", foundItems);
            playerDocument.set("jokerUsed", jokerUsed);
            playersDocuments.add(playerDocument);
        }

        document.set("players", playersDocuments);
    }

    public abstract T getTargetFromDocument(Document document, String key);

    public abstract List<T> getListFromDocument(Document document, String key);

    public void setRandomTargetIfCurrentlyNone(Player player) {
        if (currentTarget.containsKey(player.getUniqueId())) {
            return;
        }
        setRandomTarget(player);
    }

    public void setRandomTarget(Player player) {
        T target = globalRandom.choose(targetsPossibleToFind);
        currentTarget.put(player.getUniqueId(), target);
        scoreboard.update();
        updateDisplayStand(player);
        getNewTargetMessage()
                .send(player, Prefix.CHALLENGES, getTargetName(target));
        SoundSample.PLING.play(player);
    }

    protected abstract Message getNewTargetMessage();

    public void handleTargetFound(Player player) {
        T foundTarget = currentTarget.get(player.getUniqueId());
        if (foundTarget != null) {
            List<T> list = foundTargets
                    .computeIfAbsent(player.getUniqueId(), uuid -> new LinkedList<>());
            list.add(foundTarget);
            Message.forName("force-item-battle-found")
                    .send(player, Prefix.CHALLENGES, getTargetName(foundTarget));
        }
        setRandomTarget(player);
    }

    public abstract String getTargetName(T target);

    @Override
    public void getWinnersOnEnd(@NotNull List<Player> winners) {

        Bukkit.getScheduler().runTask(plugin, () -> {
            int place = 0;
            int placeValue = -1;

            List<Map.Entry<UUID, List<T>>> list = foundTargets.entrySet().stream()
                    .sorted(Comparator.comparingInt(value -> value.getValue().size()))
                    .collect(Collectors.toList());
            Collections.reverse(list);

            getLeaderboardTitleMessage().broadcast(Prefix.CHALLENGES);

            for (Map.Entry<UUID, List<T>> entry : list) {
                if (entry.getValue().size() != placeValue) {
                    place++;
                    placeValue = entry.getValue().size();
                }
                UUID uuid = entry.getKey();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                ChatColor color = getPlaceColor(place);
                Message.forName("force-battle-leaderboard-entry")
                        .broadcast(Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
            }

        });

    }

    public void sendResult(@NotNull Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            int place = 0;
            int placeValue = -1;

            List<Map.Entry<UUID, List<T>>> list = foundTargets.entrySet().stream()
                    .sorted(Comparator.comparingInt(value -> value.getValue().size()))
                    .collect(Collectors.toList());
            Collections.reverse(list);

            getLeaderboardTitleMessage().broadcast(Prefix.CHALLENGES);

            for (Map.Entry<UUID, List<T>> entry : list) {
                if (entry.getValue().size() != placeValue) {
                    place++;
                    placeValue = entry.getValue().size();
                }
                UUID uuid = entry.getKey();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                ChatColor color = getPlaceColor(place);
                Message.forName("force-battle-leaderboard-entry")
                        .send(player, Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
            }

        });
    }

    protected abstract Message getLeaderboardTitleMessage();

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
