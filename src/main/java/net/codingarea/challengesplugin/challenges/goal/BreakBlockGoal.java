package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

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

public class BreakBlockGoal extends Goal implements Listener {

	private ConcurrentHashMap<Player, Integer> points;

	public BreakBlockGoal() {
		menu = MenuType.GOALS;
		points = new ConcurrentHashMap<>();
		name = "breakblocks";
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.WOODEN_PICKAXE, ItemTranslation.BREAK_BLOCKS).hideAttributes().build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		if (Challenges.timerIsStarted()) {
			showScoreboard();
			updateScoreboard();
		}
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		hideScoreboard();
	}

	@Override
	public void onTimerStart() {
		if (!isCurrentGoal) return;
		points = new ConcurrentHashMap<>();
		showScoreboard();
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

	private void updateScoreboard() {

		scoreboard.checkUpdate();

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

			if (!points.containsKey(currentPlayer)) {
				points.put(currentPlayer, 0);
			}

		}

		for (Entry<Player, Integer> entry : points.entrySet()) {
			scoreboard.getScore("§7" + entry.getKey().getDisplayName()).setScore(entry.getValue());
		}

		scoreboard.applyChanges();


	}

	private void handleNewBlock(Player player) {

		if (!points.containsKey(player)) {
			points.put(player, 1);
		} else {
			points.put(player, points.get(player) + 1);
		}

		updateScoreboard();

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
		handleNewBlock(event.getPlayer());

	}

}
