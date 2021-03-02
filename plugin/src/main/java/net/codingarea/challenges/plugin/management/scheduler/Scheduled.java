package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

	enum TimerPolicy {
		ALWAYS,
		PAUSED,
		STARTED
	}

	enum ChallengeStatusPolicy {
		ALWAYS,
		DISABLED,
		ENABLED
	}

	@Nonnegative
	int ticks();

	boolean async() default true;

	/**
	 * Indicates if this scheduler should be executed, while the timer is started or paused.
	 * The timer's state can be checked by calling {@link ChallengeAPI#isPaused()} or {@link ChallengeAPI#isStarted()}
	 *
	 * @see ChallengeAPI#isPaused()
	 * @see ChallengeAPI#isStarted()
	 * @see TimerPolicy
	 */
	@Nonnull
	TimerPolicy timerPolicy() default TimerPolicy.STARTED;

	/**
	 * Indicates if this scheduler should be executed when the challenge is enabled or disabled.
	 * This function will only work if the method's object is an instance of {@link IChallenge}
	 *
	 * @see IChallenge
	 * @see IChallenge#isEnabled()
	 * @see ChallengeStatusPolicy
	 */
	@Nonnull
	ChallengeStatusPolicy challengePolicy() default ChallengeStatusPolicy.ENABLED;

}
