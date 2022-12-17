package net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle;

import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.targets.BiomeTarget;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.1.3
 */
public class ForceBiomeBattleGoal extends ForceBattleGoal<BiomeTarget> {

    public ForceBiomeBattleGoal() {
        super(Message.forName("menu-force-biome-battle-goal-settings"));
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.FILLED_MAP, Message.forName("item-force-biome-battle-goal"));
    }

    @Override
    protected BiomeTarget[] getTargetsPossibleToFind() {
        List<Biome> biomes = BiomeTarget.getPossibleBiomes();
        return biomes.stream().map(BiomeTarget::new).toArray(BiomeTarget[]::new);
    }

    @Override
    public BiomeTarget getTargetFromDocument(Document document, String path) {
        return new BiomeTarget(document.getEnum(path, Biome.class));
    }

    @Override
    public List<BiomeTarget> getListFromDocument(Document document, String path) {
        return document.getEnumList(path, Biome.class).stream().map(BiomeTarget::new).collect(Collectors.toList());
    }

    @Override
    protected Message getLeaderboardTitleMessage() {
        return Message.forName("force-biome-battle-leaderboard");
    }

    @ScheduledTask(ticks = 5, async = false, timerPolicy = TimerPolicy.STARTED)
    public void checkBiomes() {
        if(!shouldExecuteEffect()) return;
        broadcastFiltered(player -> {
            if(currentTarget.get(player.getUniqueId()).check(player)) {
                handleTargetFound(player);
            }
        });
    }

}
