package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.CountingGoal;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class BreakBlockGoal extends CountingGoal implements Listener, ITimerStatusExecutor {

	public BreakBlockGoal() {
		super(MenuType.GOALS);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.WOODEN_PICKAXE, ItemTranslation.BREAK_BLOCKS).hideAttributes().build();
	}

	@Override
	public void onTimerStart() {
		if (!isCurrentGoal) return;
		points = new ConcurrentHashMap<>();
		updateScoreboard();
	}

	@Override
	public List<Player> getWinners() {

		int best = 0;

		for (Entry<Player, Integer> entry : points.entrySet()) {

			if (entry.getValue() > best) best = entry.getValue();

		}

		List<Player> winners = new ArrayList<>();

		for (Entry<Player, Integer> entry : points.entrySet()) {

			if (entry.getValue() == best) {
				winners.add(entry.getKey());
			}

		}

		return winners;

	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		if (event.getBlock().getType() == Material.GRASS
				|| event.getBlock().getType() == Material.TALL_GRASS
				|| event.getBlock().getType() == Material.TALL_SEAGRASS
				|| event.getBlock().getType() == Material.KELP_PLANT
				|| event.getBlock().getType() == Material.SEAGRASS
				|| event.getBlock().getType() == Material.FERN
				|| event.getBlock().getType() == Material.LARGE_FERN) return;
		handleNewPoint(event.getPlayer());

	}

}
