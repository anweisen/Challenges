package net.codingarea.challenges.plugin.management.server;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class TitleManager {

	private final int fadein = 5, duration = 20, fadeout = 10;

	private final boolean timerStatusEnabled;
	private final boolean challengeStatusEnabled;

	public TitleManager() {
		Document config = Challenges.getInstance().getConfigDocument().getDocument("titles");
		timerStatusEnabled = config.getBoolean("timer-status");
		challengeStatusEnabled = config.getBoolean("challenge-status");
	}

	public void sendTimerStatusTitle(@Nonnull Message message) {
		if (!timerStatusEnabled) return;
		message.broadcastTitle();
	}

	public void sendChallengeStatusTitle(@Nonnull Message message, @Nonnull Object... args) {
		if (!timerStatusEnabled) return;
		message.broadcastTitle(args);
	}

	public boolean isChallengeStatusEnabled() {
		return challengeStatusEnabled;
	}

	public boolean isTimerStatusEnabled() {
		return timerStatusEnabled;
	}

	public void sendTitle(@Nonnull Player player, @Nonnull String title, @Nonnull String subtitle) {
		player.sendTitle(title, subtitle, fadein, duration, fadeout);
	}

}
