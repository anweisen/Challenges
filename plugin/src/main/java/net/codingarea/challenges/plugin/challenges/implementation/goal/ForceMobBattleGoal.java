package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingModifierGoal;
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
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.1.4
 */
public class ForceMobBattleGoal extends SettingModifierGoal {
    private final Map<UUID, List<EntityType>> killedMobs = new HashMap<>();
    private final Map<UUID, EntityType> currentMob = new HashMap<>();
    private final Map<UUID, Integer> jokerUsed = new HashMap<>();

    private Map<Player, ArmorStand> displayStands;
    private ItemStack jokerItem;
    private EntityType[] mobsPossibleToFind;

    public ForceMobBattleGoal() {
        super(MenuType.GOAL, 1, 20, 5);
        setCategory(SettingCategory.SCORE_POINTS);
    }

    @Override
    protected void onEnable() {
        jokerItem = new ItemBuilder(Material.BARRIER, "§cJoker").build();

        List<EntityType> entityTypes = new ArrayList<>(Arrays.asList(EntityType.values()));
        entityTypes.removeIf(entityType -> !entityType.isSpawnable());
        entityTypes.removeIf(entityType -> !entityType.isAlive());
        Utils.removeEnums(entityTypes, "ILLUSIONER", "ARMOR_STAND", "ZOMBIE_HORSE", "GIANT");

        mobsPossibleToFind = entityTypes.toArray(new EntityType[0]);

        displayStands = new HashMap<>();

        scoreboard.setContent((board, player) -> {
            List<Player> ingamePlayers = ChallengeAPI.getIngamePlayers();
            int emptyLinesAvailable = 15 - ingamePlayers.size();

            if (emptyLinesAvailable > 0) {
                board.addLine("");
                emptyLinesAvailable--;
            }

            for (int i = 0; i < ingamePlayers.size() && i < 15; i++) {
                Player ingamePlayer = ingamePlayers.get(i);
                EntityType entityType = currentMob.get(ingamePlayer.getUniqueId());
                String display = entityType == null ? Message.forName("none").asString()
                        : StringUtils.getEnumName(entityType);
                board.addLine(NameHelper.getName(ingamePlayer) + " §8» §e" + display);
            }

            if (emptyLinesAvailable > 0) {
                board.addLine("");
            }
        });
        scoreboard.show();
        broadcastFiltered(this::updateJokersInInventory);
        broadcastFiltered(this::updateDisplayStand);
    }

    @Override
    protected void onDisable() {
        if (jokerItem == null) return; // Disable through plugin disable
        broadcastFiltered(this::updateJokersInInventory);
        scoreboard.hide();
        jokerItem = null;
        mobsPossibleToFind = null;
        displayStands.values().forEach(Entity::remove);
        displayStands = null;
    }

    @Override
    protected void onValueChange() {
        broadcastFiltered(this::updateJokersInInventory);
    }

    @Override
    public void writeGameState(@NotNull Document document) {

        List<Document> playersDocuments = new LinkedList<>();
        for (Map.Entry<UUID, EntityType> entry : currentMob.entrySet()) {
            List<EntityType> killedMobs = this.killedMobs.get(entry.getKey());
            int jokerUsed = this.jokerUsed.getOrDefault(entry.getKey(), 0);
            GsonDocument playerDocument = new GsonDocument();
            playerDocument.set("uuid", entry.getKey());
            playerDocument.set("currentMob", entry.getValue());
            playerDocument.set("killedMobs", killedMobs);
            playerDocument.set("jokerUsed", jokerUsed);
            playersDocuments.add(playerDocument);
        }

        document.set("players", playersDocuments);
    }

    @Override
    public void loadGameState(@NotNull Document document) {
        this.currentMob.clear();
        this.jokerUsed.clear();
        this.killedMobs.clear();

        List<Document> players = document.getDocumentList("players");
        for (Document player : players) {
            UUID uuid = player.getUUID("uuid");

            EntityType currentMob = player.getEnum("currentMob", EntityType.class);
            if (currentMob != null) {
                this.currentMob.put(uuid, currentMob);
            }
            List<EntityType> killedMobs = player.getEnumList("killedMobs", EntityType.class);
            this.killedMobs.put(uuid, killedMobs);
            int jokerUsed = player.getInt("jokerUsed");
            this.jokerUsed.put(uuid, jokerUsed);
        }

        if (isEnabled()) {
            if (ChallengeAPI.isStarted()) {
                broadcastFiltered(this::setRandomMobIfCurrentlyNone);
            }
            scoreboard.update();
            broadcastFiltered(this::updateJokersInInventory);
            broadcastFiltered(this::updateDisplayStand);
        }
    }

    @Override
    public void getWinnersOnEnd(@NotNull List<Player> winners) {

        Bukkit.getScheduler().runTask(plugin, () -> {
            int place = 0;
            int placeValue = -1;

            List<Map.Entry<UUID, List<EntityType>>> list = killedMobs.entrySet().stream()
                    .sorted(Comparator.comparingInt(value -> value.getValue().size()))
                    .collect(Collectors.toList());
            Collections.reverse(list);

            Message.forName("force-mob-battle-leaderboard").broadcast(Prefix.CHALLENGES);

            for (Map.Entry<UUID, List<EntityType>> entry : list) {
                if (entry.getValue().size() > placeValue) {
                    place++;
                    placeValue = entry.getValue().size();
                }
                UUID uuid = entry.getKey();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                ChatColor color = getPlaceColor(place);
                Message.forName("force-mob-battle-leaderboard-entry")
                        .broadcast(Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
            }

        });

    }

    ChatColor getPlaceColor(int place) {
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

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BOW, Message.forName("item-force-mob-battle-goal"));
    }

    @Nullable
    @Override
    protected String[] getSettingsDescription() {
        return Message.forName("item-force-battle-description").asArray(getValue());
    }

    private int getUsableJokers(UUID uuid) {
        return Math.max(0, getValue() - jokerUsed.getOrDefault(uuid, 0));
    }

    private void handleMobKilled(Player player) {
        EntityType killedMob = currentMob.get(player.getUniqueId());
        if (killedMob != null) {
            List<EntityType> list = killedMobs
                    .computeIfAbsent(player.getUniqueId(), uuid -> new LinkedList<>());
            list.add(killedMob);
            Message.forName("force-mob-battle-killed")
                    .send(player, Prefix.CHALLENGES, StringUtils.getEnumName(killedMob));
        }
        setRandomMob(player);
    }

    private void setRandomMobIfCurrentlyNone(Player player) {
        if (currentMob.containsKey(player.getUniqueId())) {
            return;
        }
        setRandomMob(player);
    }

    private void setRandomMob(Player player) {
        EntityType entityType = globalRandom.choose(mobsPossibleToFind);
        currentMob.put(player.getUniqueId(), entityType);
        scoreboard.update();
        updateDisplayStand(player);
        Message.forName("force-mob-battle-new-mob")
                .send(player, Prefix.CHALLENGES, StringUtils.getEnumName(entityType));
        SoundSample.PLING.play(player);
    }

    private void handleJokerUse(Player player) {
        int jokerUsed = this.jokerUsed.getOrDefault(player.getUniqueId(), 0);
        jokerUsed++;
        this.jokerUsed.put(player.getUniqueId(), jokerUsed);
        handleMobKilled(player);
        updateJokersInInventory(player);
    }

    private void updateJokersInInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        boolean enabled = isEnabled() && ChallengeAPI.isStarted();
        int usableJokers = getUsableJokers(player.getUniqueId());

        int jokersInInventory = 0;

        for (ItemStack itemStack : new LinkedList<>(Arrays.asList(inventory.getContents()))) {
            if (jokerItem.isSimilar(itemStack)) {
                if (!enabled) {
                    inventory.removeItem(itemStack);
                } else {
                    jokersInInventory += itemStack.getAmount();
                    if (jokersInInventory >= usableJokers) {
                        int jokersToSubtract = jokersInInventory - usableJokers;
                        jokersInInventory -= jokersToSubtract;
                        itemStack.setAmount(itemStack.getAmount() - jokersToSubtract);
                    }
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

    private void updateDisplayStand(Player player) {
        ArmorStand armorStand = displayStands.computeIfAbsent(player, player1 -> {
            World world = player1.getWorld();
            ArmorStand entity = (ArmorStand) world
                    .spawnEntity(player1.getLocation().clone().add(0, 1.5, 0), EntityType.ARMOR_STAND);
            entity.setInvisible(true);
            entity.setInvulnerable(true);
            entity.setGravity(false);
            entity.setMarker(true);
            entity.setSilent(true);
            return entity;
        });
        EntityType mob = currentMob.get(player.getUniqueId());
        if (mob == null) {
            armorStand.setCustomNameVisible(false);
        } else {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(StringUtils.getEnumName(mob));
        }
        armorStand.teleport(player.getLocation().clone().add(0, 1.5, 0));
        armorStand.setVelocity(player.getVelocity());
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        updateDisplayStand(event.getPlayer());
    }

    @TimerTask(status = TimerStatus.RUNNING, async = false)
    public void onStart() {
        broadcastFiltered(this::setRandomMobIfCurrentlyNone);
        broadcastFiltered(this::updateJokersInInventory);
    }

    @EventHandler
    public void onStatusChange(PlayerIgnoreStatusChangeEvent event) {
        if (!shouldExecuteEffect()) return;
        if (event.isNotIgnored()) {
            setRandomMobIfCurrentlyNone(event.getPlayer());
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
        setRandomMobIfCurrentlyNone(event.getPlayer());
        updateDisplayStand(event.getPlayer());
    }

    @ScheduledTask(ticks = 1, async = false, timerPolicy = TimerPolicy.ALWAYS)
    public void onTick() {
        if (!isEnabled()) return;
        for (Player player : displayStands.keySet()) {
            updateDisplayStand(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!shouldExecuteEffect()) return;
        ArmorStand stand = displayStands.get(event.getPlayer());
        if (stand != null) {
            stand.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(@Nonnull EntityDeathEvent event) {
        if(!shouldExecuteEffect()) return;
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;
        if(ignorePlayer(killer)) return;
        if (currentMob.get(killer.getUniqueId()) == null) return;
        if(entity.getType() == currentMob.get(killer.getUniqueId())) {
            handleMobKilled(killer);
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
}
