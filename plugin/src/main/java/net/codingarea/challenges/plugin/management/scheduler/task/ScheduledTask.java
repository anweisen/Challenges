package net.codingarea.challenges.plugin.management.scheduler.task;

import net.codingarea.challenges.plugin.management.scheduler.policy.ChallengeStatusPolicy;
import net.codingarea.challenges.plugin.management.scheduler.policy.PlayerCountPolicy;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;

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
public @interface ScheduledTask {

	@Nonnegative
	int ticks();

	boolean async() default true;

	@Nonnull
	TimerPolicy timerPolicy() default TimerPolicy.STARTED;

	@Nonnull
	ChallengeStatusPolicy challengePolicy() default ChallengeStatusPolicy.ENABLED;

	@Nonnull
	PlayerCountPolicy playerPolicy() default PlayerCountPolicy.SOME_ONE;

}
