package net.codingarea.challenges.plugin.management.server;

import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ScoreboardManager {

	private final List<ChallengeBossBar> bossbars = new ArrayList<>();

	public void handleQuit(@Nonnull Player player) {
		for (ChallengeBossBar bossbar : bossbars) {
			bossbar.applyHide(player);
		}
	}

	public void handleJoin(@Nonnull Player player) {
		for (ChallengeBossBar bossbar : bossbars) {
			bossbar.applyShow(player);
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

	public void disable() {
		for (ChallengeBossBar bossbar : bossbars.toArray(new ChallengeBossBar[0])) {
			hideBossBar(bossbar);
		}
	}

}
