package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.policy.ExtraWorldPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import org.bukkit.Bukkit;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class TimedChallenge extends SettingModifier {

	private final boolean runAsync;
	protected int secondsUntilActivation;
	private int originalSecondsUntilActivation;
	private boolean timerStatus = false;
	private boolean startedBefore = false;

	public TimedChallenge(@Nonnull MenuType menu) {
		this(menu, true);
	}

	public TimedChallenge(@Nonnull MenuType menu, int max) {
		this(menu, max, true);
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max) {
		this(menu, min, max, true);
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max, int defaultValue) {
		this(menu, min, max, defaultValue, true);
	}

	public TimedChallenge(@Nonnull MenuType menu, boolean runAsync) {
		super(menu);
		this.runAsync = runAsync;
	}

	public TimedChallenge(@Nonnull MenuType menu, int max, boolean runAsync) {
		super(menu, max);
		this.runAsync = runAsync;
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max, boolean runAsync) {
		super(menu, min, max);
		this.runAsync = runAsync;
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max, int defaultValue, boolean runAsync) {
		super(menu, min, max, defaultValue);
		this.runAsync = runAsync;
	}

	@Override
	public void setValue(int value) {
		super.setValue(value);
		restartTimer();
	}

	@ScheduledTask(ticks = 20, worldPolicy = ExtraWorldPolicy.ALWAYS)
	public final void handleTimedChallengeSecond() {

		if (!startedBefore)
			restartTimer();

		if (timerStatus) {

			if (getTimerTrigger()) {
				secondsUntilActivation--;
				if (secondsUntilActivation <= 0) {
					secondsUntilActivation = 0;
					timerStatus = false;
					executeTimeActivation();
				} else {
					handleCountdown();
				}
			} else {
				Logger.debug("getTimerTrigger returned false for {}", this.getClass().getSimpleName());
			}
		}

	}

	public final void executeTimeActivation() {
		if (runAsync) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, this::onTimeActivation);
		} else {
			Bukkit.getScheduler().runTask(plugin, this::onTimeActivation);
		}
	}

	public final void shortCountDownTo(@Nonnegative int seconds) {
		if (!timerStatus) throw new IllegalArgumentException("Countdown is not started");
		if (seconds > originalSecondsUntilActivation) throw new IllegalArgumentException("Cannot short countdown to a higher length than originally set");
		this.secondsUntilActivation = seconds;
	}

	public final boolean isTimerRunning() {
		return timerStatus;
	}

	public final int getSecondsLeftUntilNextActivation() {
		return secondsUntilActivation;
	}

	public final int getOriginalSecondsUntilActivation() {
		return originalSecondsUntilActivation;
	}

	protected float getProgress() {
		return (float) (getSecondsLeftUntilNextActivation()) / getOriginalSecondsUntilActivation();
	}

	protected void handleCountdown() {
	}

	protected boolean getTimerTrigger() {
		return true;
	}

	protected abstract int getSecondsUntilNextActivation();

	protected void restartTimer(int seconds) {
		Logger.debug("Restarting timer of {} with {} second(s)", this.getClass().getSimpleName(), seconds);

		startedBefore = true;
		secondsUntilActivation = seconds;
		originalSecondsUntilActivation = seconds;
		timerStatus = true;
	}

	protected void restartTimer() {
		restartTimer(getSecondsUntilNextActivation());
	}

	protected abstract void onTimeActivation();

}
