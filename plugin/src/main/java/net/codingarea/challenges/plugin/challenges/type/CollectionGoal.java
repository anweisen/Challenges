package net.codingarea.challenges.plugin.challenges.type;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.helper.GoalHelper;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class CollectionGoal extends SettingGoal {

	private final Map<UUID, List<String>> collections = new HashMap<>();
	private final Object[] target;

	public CollectionGoal(@Nonnull Object... target) {
		super();
		this.target = target;
	}

	public CollectionGoal(boolean enabledByDefault, @Nonnull Object... target) {
		super(enabledByDefault);
		this.target = target;
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		scoreboard.setContent(GoalHelper.createScoreboard(() -> getPoints(new AtomicInteger(), true)));
		scoreboard.show();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		scoreboard.hide();
	}

	@Override
	public void getWinnersOnEnd(@Nonnull List<Player> winners) {
		AtomicInteger mostPoints = new AtomicInteger();
		Map<Player, Integer> points = getPoints(mostPoints, false);
		if (mostPoints.get() == 0) return; // Nobody won, nobody has anything

		for (Entry<Player, Integer> entry : points.entrySet()) {
			if (entry.getValue() != mostPoints.get()) continue;
			winners.add(entry.getKey());
		}
	}

	@Nonnull
	@CheckReturnValue
	protected Map<Player, Integer> getPoints(@Nonnull AtomicInteger mostPoints, boolean zeros) {
		return GoalHelper.createPointsFromValues(mostPoints, collections, Collection::size, zeros);
	}

	protected void collect(@Nonnull Player player, @Nonnull Object item, @Nonnull Runnable success) {
		List<String> collection = collections.computeIfAbsent(player.getUniqueId(), key -> new ArrayList<>());
		if (collection.contains(item.toString())) return;
		collection.add(item.toString());
		scoreboard.update();
		success.run();
		if (collection.size() >= target.length)
			ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);

		Document scores = document.getDocument("scores");
		for (String key : scores.keys()) {
			try {
				UUID uuid = UUID.fromString(key);
				List<String> collection = scores.getStringList(key);
				collections.put(uuid, collection);
			} catch (Exception ex) {
				Logger.severe("Could not load scores for " + key);
			}
		}
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);

		Document scores = document.getDocument("scores");
		collections.forEach((uuid, collection) -> {
			scores.set(uuid.toString(), collection);
		});
	}

}
