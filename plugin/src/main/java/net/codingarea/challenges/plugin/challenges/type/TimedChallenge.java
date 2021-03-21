package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class TimedChallenge extends SettingModifier {

	private int secondsUntilActivation;
	private boolean timerStatus = false;
	private boolean startedBefore = false;

	public TimedChallenge(@Nonnull MenuType menu) {
		super(menu);
	}

	public TimedChallenge(@Nonnull MenuType menu, int max) {
		super(menu, max);
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max) {
		super(menu, min, max);
	}

	public TimedChallenge(@Nonnull MenuType menu, int min, int max, int defaultValue) {
		super(menu, min, max, defaultValue);
	}

	@Override
	public void setValue(int value) {
		super.setValue(value);
		restartTimer();
	}

	@Scheduled(ticks = 20)
	public void onSecond() {

		if (!startedBefore) {
			restartTimer();
		}

		if (timerStatus) {

			if (getTimerCondition()) {
				secondsUntilActivation--;
				if (secondsUntilActivation <= 0) {
					secondsUntilActivation = 0;
					timerStatus = false;
					onTimeActivation();
				}
			} else {
				Logger.debug("getTimerCondition returned false for " + this.getClass().getSimpleName());
			}
		}

	}

	protected boolean getTimerCondition() {
		return true;
	}

	protected abstract int getSecondsUntilNextActivation();

	protected void restartTimer(int seconds) {
		Logger.debug("Restarting timer of " + this.getClass().getSimpleName() + " with " + seconds + " second(s)");

		startedBefore = true;
		secondsUntilActivation = seconds;
		timerStatus = true;
	}

	protected void restartTimer() {
		restartTimer(getSecondsUntilNextActivation());
	}

	protected abstract void onTimeActivation();

}
