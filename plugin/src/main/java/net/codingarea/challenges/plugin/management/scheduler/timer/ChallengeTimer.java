package net.codingarea.challenges.plugin.management.scheduler.timer;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled.TimerPolicy;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.management.stats.Statistic;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.wrapper.FileDocumentWrapper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class ChallengeTimer {

	private long time = 0;
	private boolean countingUp = true;
	private boolean paused = true;

	private final TimerFormat format;
	private final String stoppedMessage, upMessage, downMessage;

	public ChallengeTimer() {

		// Load format + messages
		Document timerConfig = Challenges.getInstance().getConfigDocument().getDocument("timer");
		stoppedMessage = timerConfig.getString("stopped-message", "");
		upMessage = timerConfig.getString("count-up-message", "");
		downMessage = timerConfig.getString("count-down-message", "");

		Document formatConfig = timerConfig.getDocument("format");
		format = new TimerFormat(formatConfig);

		Challenges.getInstance().getScheduler().register(this);
	}

	@Scheduled(ticks = 20, async = false, timerPolicy = TimerPolicy.ALWAYS)
	public void onTimerSecond() {

		if (!paused) {
			if (countingUp) time++;
			else time--;

			if (time <= 0) {
				time = 0;
				handleHitZero();
			}
		}

		updateActionbar();
		Challenges.getInstance().getMenuManager().updateTimerMenu();

	}

	@Scheduled(ticks = 20, timerPolicy = TimerPolicy.PAUSED)
	public void onTimerPaused() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getGameMode() == GameMode.SPECTATOR) continue;
			Location location = player.getLocation();

			if (location.getWorld() == null) continue;
			location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 1);
		}
	}

	private void handleHitZero() {
		Challenges.getInstance().getServerManager().endChallenge(ChallengeEndCause.TIMER_HIT_ZERO);
	}

	public void resume() {
		if (!paused) return;


		if (Challenges.getInstance().getServerManager().isFresh() && Challenges.getInstance().getStatsManager().isEnabled()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				Challenges.getInstance().getStatsManager().getStats(player.getUniqueId()).incrementStatistic(Statistic.CHALLENGES_PLAYED, 1);
			}
		}
		Challenges.getInstance().getServerManager().setNotFresh();
		paused = false;
		updateActionbar();
		Challenges.getInstance().getMenuManager().updateTimerMenu();

		Goal currentGoal = Challenges.getInstance().getChallengeManager().getCurrentGoal();
		if (currentGoal != null)
			currentGoal.getStartSound().broadcast();
		else SoundSample.DRAGON_BREATH.broadcast();

	}

	public void pause() {
		if (paused) return;
		paused = true;
		updateActionbar();
		Challenges.getInstance().getMenuManager().updateTimerMenu();
	}

	public void reset() {
		pause();
		time = 0;
		countingUp = true;
		updateActionbar();
		Challenges.getInstance().getMenuManager().updateTimerMenu();
	}

	public void updateActionbar() {
		String actionbar = getActionbar();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbar));
		}
	}

	@Nonnull
	private String getActionbar() {
		String message = !paused || (!countingUp && time > 0) ? (countingUp ? upMessage : downMessage) : stoppedMessage;
		String time = getFormattedTime();
		return message.replace("{time}", time);
	}

	public void addSeconds(int amount) {
		time += amount;
		if (time < 0)
			time = 0;
	}

	public void setSeconds(int seconds) {
		this.time = seconds;
	}

	public void setCountingUp(boolean countingUp) {
		this.countingUp = countingUp;
		updateActionbar();
		Challenges.getInstance().getMenuManager().updateTimerMenu();
	}

	@Nonnull
	public String getFormattedTime() {
		return format.format(time);
	}

	@Nonnull
	public TimerFormat getFormat() {
		return format;
	}

	public long getTime() {
		return time;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isStarted() {
		return !paused;
	}

	public boolean isCountingUp() {
		return countingUp;
	}
}
