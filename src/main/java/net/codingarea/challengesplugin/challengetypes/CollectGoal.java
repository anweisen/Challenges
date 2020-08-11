package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class CollectGoal<Collection> extends Goal {

	protected ConcurrentHashMap<UUID, List<Collection>> points = new ConcurrentHashMap<>();

	public CollectGoal(MenuType menu) {
		super(menu);
	}

	public CollectGoal(MenuType menu, boolean defaultActivated) {
		super(menu, defaultActivated);
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
	public List<Player> getWinners() {

		List<Player> winners = new ArrayList<>();

		int best = 0;

		for (List<Collection> pointList : points.values()) {
			if (pointList.size() > best) best = pointList.size();
		}

		for (Entry<UUID, List<Collection>> entry : points.entrySet()) {

			if (entry.getValue().size() == best) {
				winners.add(Bukkit.getPlayer(entry.getKey()));
			}

		}

		return winners;

	}

	protected void updateScoreboard() {

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (!points.containsKey(currentPlayer.getUniqueId())) {
				points.put(currentPlayer.getUniqueId(), new ArrayList<>());
			}
		}

		scoreboard.checkUpdate();

		for (Entry<UUID, List<Collection>> entry : points.entrySet()) {
			Player currentPlayer = Bukkit.getPlayer(entry.getKey());
			if (currentPlayer == null) continue;
			List<Collection> collection = entry.getValue();
			scoreboard.getUpdateObjective().getScore("§7" + currentPlayer.getName()).setScore(collection.size());
		}

		scoreboard.applyChanges();

	}

	protected void handleNewPoint(Player player, Collection t, String name, Translation translation) {

		if (t == null) return;
		if (t == Material.AIR) return;

		List<Collection> points;
		if (this.points.containsKey(player.getUniqueId())) {
			points = this.points.get(player.getUniqueId());
		} else {
			points = new ArrayList<>();
			this.points.put(player.getUniqueId(), points);
		}

		if (!points.contains(t)) {
			points.add(t);
			player.sendMessage(Prefix.CHALLENGES + translation.get()
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
