package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ScheduledTaskConfig extends AbstractTaskConfig {

	private final int rate;

	ScheduledTaskConfig(@Nonnull ScheduledTask annotation) {
		this(annotation.ticks(), annotation.async());
	}

	ScheduledTaskConfig(@Nonnegative int rate, boolean async) {
		super(async);
		this.rate = rate;
	}

	public int getRate() {
		return rate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScheduledTaskConfig that = (ScheduledTaskConfig) o;
		return rate == that.rate && async == that.async;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rate, async);
	}

}
