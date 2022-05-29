package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.1.4
 */
public class ForceMobBattleGoal extends ForceBattleGoal {

    private final Map<UUID, List<EntityType>> killedMobs = new HashMap<>();
    private final Map<UUID, EntityType> currentMob = new HashMap<>();

    private EntityType[] mobsPossibleToFind;

    public ForceMobBattleGoal() {
        super(MenuType.GOAL, Message.forName("menu-force-mob-battle-goal-settings"));
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        List<EntityType> entityTypes = new ArrayList<>(Arrays.asList(EntityType.values()));
        entityTypes.removeIf(entityType -> !entityType.isSpawnable());
        entityTypes.removeIf(entityType -> !entityType.isAlive());
        Utils.removeEnums(entityTypes, "ILLUSIONER", "ARMOR_STAND", "ZOMBIE_HORSE", "GIANT");

        mobsPossibleToFind = entityTypes.toArray(new EntityType[0]);

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
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        mobsPossibleToFind = null;
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
        super.loadGameState(document);
        this.currentMob.clear();
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
                if (entry.getValue().size() != placeValue) {
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

    @Override
    public void sendResult(@NotNull Player player) {

        Bukkit.getScheduler().runTask(plugin, () -> {
            int place = 0;
            int placeValue = -1;

            List<Map.Entry<UUID, List<EntityType>>> list = killedMobs.entrySet().stream()
                    .sorted(Comparator.comparingInt(value -> value.getValue().size()))
                    .collect(Collectors.toList());
            Collections.reverse(list);

            Message.forName("force-mob-battle-leaderboard").send(player, Prefix.CHALLENGES);

            for (Map.Entry<UUID, List<EntityType>> entry : list) {
                if (entry.getValue().size() != placeValue) {
                    place++;
                    placeValue = entry.getValue().size();
                }
                UUID uuid = entry.getKey();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                ChatColor color = getPlaceColor(place);
                Message.forName("force-mob-battle-leaderboard-entry")
                        .send(player, Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
            }

        });

    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BOW, Message.forName("item-force-mob-battle-goal"));
    }

    @Override
    public void handleTargetFound(Player player) {
        EntityType killedMob = currentMob.get(player.getUniqueId());
        if (killedMob != null) {
            List<EntityType> list = killedMobs
                    .computeIfAbsent(player.getUniqueId(), uuid -> new LinkedList<>());
            list.add(killedMob);
            Message.forName("force-mob-battle-killed")
                    .send(player, Prefix.CHALLENGES, StringUtils.getEnumName(killedMob));
        }
        setRandomTarget(player);
    }

    @Override
    public void setRandomTargetIfCurrentlyNone(Player player) {
        if (currentMob.containsKey(player.getUniqueId())) {
            return;
        }
        setRandomTarget(player);
    }

    @Override
    public void setRandomTarget(Player player) {
        EntityType entityType = globalRandom.choose(mobsPossibleToFind);
        currentMob.put(player.getUniqueId(), entityType);
        scoreboard.update();
        updateDisplayStand(player);
        Message.forName("force-mob-battle-new-mob")
                .send(player, Prefix.CHALLENGES, StringUtils.getEnumName(entityType));
        SoundSample.PLING.play(player);
    }

    @Override
    public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
        EntityType mob = currentMob.get(player.getUniqueId());
        if (mob == null) {
            armorStand.setCustomNameVisible(false);
        } else {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(StringUtils.getEnumName(mob));
        }
    }

    @Override
    public double getDisplayStandYOffset() {
        return 2;
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
            handleTargetFound(killer);
        }
    }
}
