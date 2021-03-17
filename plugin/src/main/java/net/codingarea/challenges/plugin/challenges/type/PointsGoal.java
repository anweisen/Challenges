package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.helper.GoalHelper;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class PointsGoal extends SettingGoal {

	private final Map<UUID, Integer> points = new HashMap<>();

	public PointsGoal() {
		super();
	}

	public PointsGoal(boolean enabledByDefault) {
		super(enabledByDefault);
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
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);

		Document scores = document.getDocument("scores");
		for (String key : scores.keys()) {
			try {
				UUID uuid = UUID.fromString(key);
				int value = scores.getInt(key);
				points.put(uuid, value);
			} catch (Exception ex) {
				Logger.severe("Could not load scores for " + key);
			}
		}
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);

		Document scores = document.getDocument("scores");
		points.forEach((uuid, points) -> scores.set(uuid.toString(), points));
	}

	@Override
	public void getWinnersOnEnd(@Nonnull List<Player> winners) {
		GoalHelper.getWinnersOnEnd(winners, getPoints(new AtomicInteger(), false));
	}

	@Nonnull
	@CheckReturnValue
	protected Map<Player, Integer> getPoints(@Nonnull AtomicInteger mostPoints, boolean zeros) {
		return GoalHelper.createPointsFromValues(mostPoints, points, value -> value, zeros);
	}

	protected void collect(@Nonnull Player player) {
		points.compute(player.getUniqueId(), (uuid, integer) -> integer == null ? 1 : integer + 1);
		scoreboard.update();
	}

}
