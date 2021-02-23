package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled.ChallengeStatusPolicy;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled.TimerPolicy;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ScheduledFunction {

	private final Method method;
	private final Object holder;

	private final Scheduled annotation;

	ScheduledFunction(@Nonnull Object holder, @Nonnull Method method, @Nonnull Scheduled annotation) {
		this.method = method;
		this.holder = holder;
		this.annotation = annotation;
	}

	public void invoke() throws InvocationTargetException, IllegalAccessException {
		if (shouldInvoke())
			invokeAnyway();
	}

	public void invokeAnyway() throws InvocationTargetException, IllegalAccessException {
		method.setAccessible(true);
		method.invoke(holder);
	}

	private boolean shouldInvoke() {
		if (annotation.timerPolicy() == TimerPolicy.STARTED && ChallengeAPI.isPaused()) return false;
		if (annotation.timerPolicy() == TimerPolicy.PAUSED  && ChallengeAPI.isStarted()) return false;
		if (holder instanceof IChallenge) {
			IChallenge challenge = (IChallenge) holder;
			if (annotation.challengePolicy() == ChallengeStatusPolicy.ENABLED  && !challenge.isEnabled()) return false;
			if (annotation.challengePolicy() == ChallengeStatusPolicy.DISABLED &&  challenge.isEnabled()) return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScheduledFunction function = (ScheduledFunction) o;
		return method.equals(function.method) && holder.equals(function.holder);
	}

	@Override
	public int hashCode() {
		return Objects.hash(method, holder);
	}

	@Nonnull
	public Object getHolder() {
		return holder;
	}

}
