package net.codingarea.challenges.plugin.challenges.type.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;
import javax.annotation.Nonnull;
import net.anweisen.utilities.common.collection.NumberFormatter;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IGoal;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard.ScoreboardInstance;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class GoalHelper {

	public static final int LEADERBOARD_SIZE = 9;

	private GoalHelper() {}

	public static void handleSetEnabled(@Nonnull IGoal goal, boolean enabled) {
		if (Challenges.getInstance().getChallengeManager().getCurrentGoal() != goal && enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(goal);
		} else if (Challenges.getInstance().getChallengeManager().getCurrentGoal() == goal && !enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(null);
		}
	}

	@Nonnull
	public static SortedMap<Integer, List<Player>> createLeaderboardFromPoints(@Nonnull Map<Player, Integer> points) {
		SortedMap<Integer, List<Player>> leaderboard = new TreeMap<>(Collections.reverseOrder());
		for (Entry<Player, Integer> entry : points.entrySet()) {
			List<Player> players = leaderboard.computeIfAbsent(entry.getValue(), key -> new ArrayList<>());
			players.add(entry.getKey());
		}
		return leaderboard;
	}

	@Nonnull
	public static <V> Map<Player, Integer> createPointsFromValues(@Nonnull AtomicInteger mostPoints, @Nonnull Map<UUID, V> map, @Nonnull ToIntBiFunction<UUID, V> mapper, boolean zeros) {
		Map<Player, Integer> result = new HashMap<>();
		if (zeros) ChallengeAPI.getIngamePlayers().forEach(player -> result.put(player, 0));
		for (Entry<UUID, V> entry : map.entrySet()) {
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null)
				continue;
			if (!AbstractChallenge.ignorePlayer(player)) {
				int points = mapper.applyAsInt(entry.getKey(), entry.getValue());
				if (points == 0)
					continue;

				result.put(player, points);

				if (points >= mostPoints.get())
					mostPoints.set(points);
			}
		}
		return result;
	}

	public static <E> int determinePosition(@Nonnull SortedMap<?, List<E>> map, @Nonnull E target) {
		int position = 1;
		for (Entry<?, List<E>> entry : map.entrySet()) {
			if (entry.getValue().contains(target)) break;
			position++;
		}
		return position;
	}

	@Nonnull
	public static BiConsumer<ScoreboardInstance, Player> createScoreboard(@Nonnull Supplier<Map<Player, Integer>> points) {
		return createScoreboard(points, player -> new LinkedList<>());
	}

	@Nonnull
	public static BiConsumer<ScoreboardInstance, Player> createScoreboard(@Nonnull Supplier<Map<Player, Integer>> points, Function<Player, List<String>> additionalLines) {
		return (scoreboard, player) -> {
			SortedMap<Integer, List<Player>> leaderboard = GoalHelper.createLeaderboardFromPoints(points.get());
			int playerPlace = GoalHelper.determinePosition(leaderboard, player);

			scoreboard.addLine("");
			scoreboard.addLine(Message.forName("your-place").asString(playerPlace));
			scoreboard.addLine("");
			{
				int place = 1;
				int displayed = 0;
				for (Entry<Integer, List<Player>> entry : leaderboard.entrySet()) {
					List<Player> players = entry.getValue();
					for (Player current : players) {
						displayed++;
						if (displayed >= LEADERBOARD_SIZE) break;
						scoreboard.addLine(Message.forName("scoreboard-leaderboard").asString(place, NameHelper.getName(current), NumberFormatter.MIDDLE_NUMBER.format(entry.getKey())));
					}
					if (displayed >= LEADERBOARD_SIZE) break;
					place++;
				}
			}
			scoreboard.addLine("");

			List<String> lines = additionalLines.apply(player);
			int linesThatCanBeAdded = 15 - scoreboard.getLines().size() - 1;
			for (int i = 0; i < lines.size() && linesThatCanBeAdded > 0; i++) {
				linesThatCanBeAdded--;
				String line = lines.get(i);
				scoreboard.addLine(line);
			}
			scoreboard.addLine("");
		};
	}

	public static void getWinnersOnEnd(@Nonnull List<Player> winners, @Nonnull Map<Player, Integer> points) {
		AtomicInteger mostPoints = new AtomicInteger();
		if (mostPoints.get() == 0) return; // Nobody won, nobody has anything

		for (Entry<Player, Integer> entry : points.entrySet()) {
			if (entry.getValue() != mostPoints.get() || entry.getValue() == 0) continue;
			winners.add(entry.getKey());
		}
	}

}
