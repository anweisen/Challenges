package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 */

public abstract class CollectGoal<T> extends Goal {

	protected ConcurrentHashMap<Player, List<T>> points = new ConcurrentHashMap<>();

	@Override
	public List<Player> getWinners() {

		List<Player> winners = new ArrayList<>();

		int best = 0;

		for (List<T> pointList : points.values()) {

			if (pointList.size() > best) best = pointList.size();

		}

		for (Entry<Player, List<T>> entry : points.entrySet()) {

			if (entry.getValue().size() == best) {
				winners.add(entry.getKey());
			}

		}

		return winners;

	}

	protected void updateScoreboard() {

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (!points.containsKey(currentPlayer)) {
				points.put(currentPlayer, new ArrayList<>());
			}
		}

		scoreboard.checkUpdate();

		for (Entry<Player, List<T>> entry : points.entrySet()) {

			Player currentPlayer = entry.getKey();
			List<T> collection = entry.getValue();
			scoreboard.getUpdateObjective().getScore("§7" + currentPlayer.getDisplayName()).setScore(collection.size());

		}

		scoreboard.applyChanges();

	}

	protected void handleNewPoint(Player player, T t, String name, Translation translation) {

		if (t == null) return;
		if (t == Material.AIR) return;

		List<T> points;
		if (this.points.containsKey(player)) {
			points = this.points.get(player);
		} else {
			points = new ArrayList<>();
			this.points.put(player, points);
		}

		if (!points.contains(t)) {
			points.add(t);
			player.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + translation.get()
					.replace("%new%", Utils.getEnumName(name)).replace("%count%", points.size() + ""));
			AnimationSound.PLING_SOUND.play(player);
			updateScoreboard();
		}
	}

	@Override
	public void showScoreboard() {
		super.showScoreboard();
		updateScoreboard();
	}
}
