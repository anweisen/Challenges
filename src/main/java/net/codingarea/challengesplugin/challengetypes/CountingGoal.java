package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

public abstract class CountingGoal extends Goal {

	protected ConcurrentHashMap<Player, Integer> points = new ConcurrentHashMap<>();

	public CountingGoal(MenuType menu) {
		super(menu);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		showScoreboard();
		updateScoreboard();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		hideScoreboard();
	}

	@Override
	public void onTimerStarted() {
		if (!isCurrentGoal) return;
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

	public void updateScoreboard() {

		scoreboard.checkUpdate();

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (!points.containsKey(currentPlayer)) {
				points.put(currentPlayer, 0);
			}
		}

		for (Entry<Player, Integer> entry : points.entrySet()) {
			scoreboard.getScore("§7" + entry.getKey().getName()).setScore(entry.getValue());
		}

		scoreboard.applyChanges();


	}

	public void handleNewPoint(Player player) {

		if (!points.containsKey(player)) {
			points.put(player, 1);
		} else {
			points.put(player, points.get(player) + 1);
		}

		updateScoreboard();

	}

}
