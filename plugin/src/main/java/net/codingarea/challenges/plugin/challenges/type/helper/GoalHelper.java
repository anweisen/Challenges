package net.codingarea.challenges.plugin.challenges.type.helper;

import net.anweisen.utilities.commons.misc.NumberFormatter;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard.ScoreboardInstance;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class GoalHelper {

	public static final int LEADERBOARD_SIZE = 9;

	private GoalHelper() {}

	public static void handleSetEnabled(@Nonnull Goal goal, boolean enabled) {
		if (Challenges.getInstance().getChallengeManager().getCurrentGoal() != goal && enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(goal);
		} else if (Challenges.getInstance().getChallengeManager().getCurrentGoal() == goal && !enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(null);
		}
	}

	public static SortedMap<Integer, List<Player>> createLeaderboardFromPoints(@Nonnull Map<Player, Integer> points) {
		SortedMap<Integer, List<Player>> leaderboard = new TreeMap<>(Collections.reverseOrder());
		for (Entry<Player, Integer> entry : points.entrySet()) {
			List<Player> players = leaderboard.computeIfAbsent(entry.getValue(), key -> new ArrayList<>());
			players.add(entry.getKey());
		}
		return leaderboard;
	}

	public static <V> Map<Player, Integer> createPointsFromValues(@Nonnull AtomicInteger mostPoints, @Nonnull Map<UUID, V> map, @Nonnull ToIntBiFunction<UUID, V> mapper, boolean zeros) {

		Map<Player, Integer> result = new HashMap<>();
		if (zeros) Bukkit.getOnlinePlayers().forEach(player -> result.put(player, 0));
		for (Entry<UUID, V> entry : map.entrySet()) {
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null) continue;
			int points = mapper.applyAsInt(entry.getKey(), entry.getValue());
			System.out.println(points);
			if (points == 0) continue;

			if (points >= mostPoints.get()) {
				mostPoints.set(points);
				result.put(player, points);
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
