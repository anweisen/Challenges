package net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.targets.*;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleDisplayGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class ExtremeForceBattleGoal extends ForceBattleDisplayGoal<ForceTarget<?>> {


    public ExtremeForceBattleGoal() {
        super(MenuType.GOAL, Message.forName("menu-extreme-force-battle-goal-settings"));
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BOOK, Message.forName("item-extreme-force-battle-goal"));
    }

    @Override
    public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
        if(currentTarget.containsKey(player.getUniqueId())) {
            currentTarget.get(player.getUniqueId()).updateDisplayStand(player, armorStand);
        }
    }

    @Override
    protected ForceTarget<?>[] getTargetsPossibleToFind() {
        //Currently not needed as the setRandomTarget logic of the ForceBattleGoal class is not used
        return new ForceTarget<?>[0];
    }

    @ScheduledTask(ticks = 5, async = false, timerPolicy = TimerPolicy.STARTED)
    public void checkTargets() {
        for (Map.Entry<UUID, ForceTarget<?>> entry : currentTarget.entrySet()) {
            UUID uuid = entry.getKey();
            ForceTarget<?> target = entry.getValue();
            Player player = Bukkit.getPlayer(uuid);
            if(player == null || ignorePlayer(player)) continue;

            if(target.check(player)) {
                handleTargetFound(player);
            }
        }
    }

    @Override
    public ForceTarget<?> getTargetFromDocument(Document document, String key) {
        Document targetDocument = document.getDocument(key);
        String targetTypeString = targetDocument.getString("type");
        String value = targetDocument.getString("value");

        TargetType targetType = TargetType.valueOf(targetTypeString);
        return targetType.parse().apply(value);
    }

    @Override
    public List<ForceTarget<?>> getListFromDocument(Document document, String key) {
        List<Document> targetDocuments = document.getDocumentList(key);
        List<ForceTarget<?>> targets = new ArrayList<>();
        for (Document targetDocument : targetDocuments) {
            String targetTypeString = targetDocument.getString("type");
            String value = targetDocument.getString("value");

            TargetType targetType = TargetType.valueOf(targetTypeString);
            targets.add(targetType.parse().apply(value));
        }
        return targets;
    }

    @Override
    public void setTargetInDocument(Document document, String key, ForceTarget<?> target) {
        document.set(key, Document.create().set("type", target.getType().name()).set("value", target.toString()));
    }

    @Override
    public void setFoundListInDocument(Document document, String key, List<ForceTarget<?>> targets) {
        List<Document> documents = new ArrayList<>();
        for (ForceTarget<?> forceTarget : targets) {
            documents.add(Document.create().set("type", forceTarget.getType().name()).set("value", forceTarget.toString()));
        }
        document.set(key, documents);
    }

    @Override
    protected Message getNewTargetMessage(ForceTarget<?> newTarget) {
        return newTarget.getNewTargetMessage();
    }

    @Override
    protected Message getTargetCompletedMessage(ForceTarget<?> target) {
        return target.getCompletedMessage();
    }

    @Override
    public Object getTargetMessageReplacement(ForceTarget<?> target) {
        return target.toMessage();
    }

    @Override
    public String getTargetName(ForceTarget<?> target) {
        return target.getName();
    }

    @Override
    protected Message getLeaderboardTitleMessage() {
        return Message.forName("extreme-force-battle-leaderboard");
    }

    @Override
    public void setRandomTarget(Player player) {
        updateDisplayStand(player);

        TargetType targetType = globalRandom.choose(TargetType.values());
        ForceTarget<?> newTarget = targetType.getRandomTarget().apply(player);

        currentTarget.put(player.getUniqueId(), newTarget);
        getNewTargetMessage(newTarget)
                .send(player, Prefix.CHALLENGES, getTargetMessageReplacement(newTarget));
        SoundSample.PLING.play(player);

        if(scoreboard.isShown()) {
            scoreboard.update();
        }

        //ToDo reset advancement progress if the target is an advancement target
    }

    @Override
    protected void setScoreboardContent() {
        scoreboard.setContent((board, player) -> {
            List<Player> ingamePlayers = ChallengeAPI.getIngamePlayers();
            int emptyLinesAvailable = 15 - ingamePlayers.size();

            if (emptyLinesAvailable > 0) {
                board.addLine("");
                emptyLinesAvailable--;
            }

            for (int i = 0; i < ingamePlayers.size() && i < 15; i++) {
                Player ingamePlayer = ingamePlayers.get(i);
                ForceTarget<?> target = currentTarget.get(ingamePlayer.getUniqueId());
                String display = target == null ? Message.forName("none").asString() : (target.getScoreboardDisplayMessage().asString(getTargetName(target)));
                board.addLine(NameHelper.getName(ingamePlayer) + " §8» §e" + display);
            }

            if (emptyLinesAvailable > 0) {
                board.addLine("");
            }
        });
    }

    @Override
    protected boolean shouldRegisterDupedTargetsSetting() {
        return false;
    }

    public enum TargetType {
        ITEM(string -> {
            return new ItemTarget(Material.valueOf(string));
        }, player -> {
            return new ItemTarget(globalRandom.choose(ItemTarget.getPossibleItems()));
        }),
        BLOCK(string -> {
            return new BlockTarget(Material.valueOf(string));
        }, player -> {
            return new BlockTarget(globalRandom.choose(BlockTarget.getPossibleBlocks()));
        }),
        HEIGHT(string -> {
            return new HeightTarget(Integer.valueOf(string));
        }, player -> {
            World world = ChallengeAPI.getGameWorld(World.Environment.NORMAL);
            int height = globalRandom.range(BukkitReflectionUtils.getMinHeight(world), world.getMaxHeight());
            return new HeightTarget(height);
        }),
        MOB(string -> {
            return new MobTarget(EntityType.valueOf(string));
        }, player -> {
            return new MobTarget(globalRandom.choose(MobTarget.getPossibleMobs()));
        });

        private final Function<String, ForceTarget<?>> parseFunction;
        private final Function<Player, ForceTarget<?>> randomTargetFunction;

        TargetType(Function<String, ForceTarget<?>> parseFunction, Function<Player, ForceTarget<?>> randomTargetFunction) {
            this.parseFunction = parseFunction;
            this.randomTargetFunction = randomTargetFunction;
        }

        public Function<String, ForceTarget<?>> parse() {
            return parseFunction;
        }

        public Function<Player, ForceTarget<?>> getRandomTarget() {
            return randomTargetFunction;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(@Nonnull EntityDeathEvent event) {
        if (!shouldExecuteEffect()) return;
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;
        if (ignorePlayer(killer)) return;
        if (currentTarget.get(killer.getUniqueId()) == null) return;
        if (currentTarget.get(killer.getUniqueId()) instanceof MobTarget) {
            MobTarget target = (MobTarget) currentTarget.get(killer.getUniqueId());
            if (target.getTarget() != entity.getType()) return;
            handleTargetFound(killer);
        }
    }

}
