package net.codingarea.challenges.plugin.management.server.scoreboard;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeScoreboard {

	private static int index = 0;

	public final class ScoreboardInstance {

		private final String[] lines = new String[15];
		private String title = Message.forName("scoreboard-title").asString();
		private int linesIndex = 0;

		private ScoreboardInstance() {}

		@Nonnull
		public ScoreboardInstance setTitle(@Nonnull String title) {
			this.title = title;
			return this;
		}

		@Nonnull
		public ScoreboardInstance addLine(@Nonnull String text) {
			lines[linesIndex++] = text;
			return this;
		}

		@Nonnull
		public Collection<String> getLines() {
			List<String> list = new ArrayList<>();
			for (String line : lines) {
				if (line == null) continue;
				list.add(line);
			}
			return list;
		}

		@Nullable
		public String getTitle() {
			return title;
		}

		@Override
		public String toString() {
			return "ScoreboardInstance{" +
					"lines=" + Arrays.toString(lines) +
					", title='" + title + '\'' +
					'}';
		}
	}

	private final Map<Player, Objective> objectives = new ConcurrentHashMap<>();
	private BiConsumer<ScoreboardInstance, Player> content = (scoreboard, player) -> {};

	public void setContent(@Nonnull BiConsumer<ScoreboardInstance, Player> content) {
		this.content = content;
	}

	public void applyHide(@Nonnull Player player) {
		unregister(objectives.get(player));
	}

	public void update() {
		Bukkit.getOnlinePlayers().forEach(this::update);
	}

	public void update(@Nonnull Player player) {
		try {

			ScoreboardInstance instance = new ScoreboardInstance();
			content.accept(instance, player);

			Collection<String> lines = instance.getLines();
			if (lines.isEmpty()) {
				unregister(objectives.get(player));
				return;
			}

			Scoreboard scoreboard = player.getScoreboard();
			if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard())
				player.setScoreboard(scoreboard = Bukkit.getScoreboardManager().getNewScoreboard());

			Objective objective = scoreboard.registerNewObjective(String.valueOf(index++), "dummy", String.valueOf(instance.getTitle()));
			{
				int score = lines.size();
				for (String line : lines) {
					if (line.isEmpty()) line = StringUtils.repeat(' ', score + 1);
					score--;
					objective.getScore(line).setScore(score);
				}
			}

			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			unregister(objectives.get(player));
			objectives.put(player, objective);

		} catch (Exception ex) {
			Logger.error("Unable to update scoreboard for player '{}'", player.getName(), ex);
		}
	}

	public final void show() {
		Challenges.getInstance().getScoreboardManager().setCurrentScoreboard(this);
	}

	public final void hide() {
		if (Challenges.getInstance().getScoreboardManager().getCurrentScoreboard() != this) return;
		Challenges.getInstance().getScoreboardManager().setCurrentScoreboard(null);
	}

	private void unregister(@Nullable Objective objective) {
		try {
			if (objective == null) return;
			objective.unregister();
		} catch (Exception ex) {
		}
	}

}
