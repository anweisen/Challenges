package net.codingarea.challenges.plugin.management.server;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ScoreboardManager {

	private final List<ChallengeBossBar> bossbars = new ArrayList<>();
	private ChallengeScoreboard currentScoreboard;

	public void handleQuit(@Nonnull Player player) {
		for (ChallengeBossBar bossbar : bossbars) {
			bossbar.applyHide(player);
		}
		if (currentScoreboard != null) {
			currentScoreboard.applyHide(player);

			Bukkit.getScheduler().runTaskLaterAsynchronously(Challenges.getInstance(), () -> currentScoreboard.update(), 1);
		}
	}

	public void handleJoin(@Nonnull Player player) {
		for (ChallengeBossBar bossbar : bossbars) {
			bossbar.applyShow(player);
		}
		if (currentScoreboard != null) {
			currentScoreboard.applyShow(player);
		}
	}

	public void showBossBar(@Nonnull ChallengeBossBar bossbar) {
		if (bossbars.contains(bossbar)) return;
		bossbars.add(bossbar);
		Bukkit.getOnlinePlayers().forEach(bossbar::applyShow);
	}

	public void hideBossBar(@Nonnull ChallengeBossBar bossbar) {
		if (!bossbars.remove(bossbar)) return;
		Bukkit.getOnlinePlayers().forEach(bossbar::applyHide);
	}

	public void setCurrentScoreboard(@Nullable ChallengeScoreboard scoreboard) {
		if (currentScoreboard == scoreboard) return;

		// Remove old scoreboard
		if (currentScoreboard != null)
			Bukkit.getOnlinePlayers().forEach(currentScoreboard::applyHide);

		currentScoreboard = scoreboard;

		// Add new scoreboard if available
		if (scoreboard == null) return;
		Bukkit.getOnlinePlayers().forEach(scoreboard::applyShow);
	}

	@Nullable
	public ChallengeScoreboard getCurrentScoreboard() {
		return currentScoreboard;
	}

	public void disable() {
		for (ChallengeBossBar bossbar : bossbars.toArray(new ChallengeBossBar[0])) {
			hideBossBar(bossbar);
		}
		setCurrentScoreboard(null);
	}

	public void handleLoadLanguages() {
		if (currentScoreboard != null)
			currentScoreboard.update();
	}

}
