package net.codingarea.challenges.plugin.management.scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class SchedulerConfig {

	private final int rate;
	private final boolean async;

	SchedulerConfig(@Nonnull Scheduled annotation) {
		this(annotation.ticks(), annotation.async());
	}

	public SchedulerConfig(int rate, boolean async) {
		this.rate = rate;
		this.async = async;
	}

	public int getRate() {
		return rate;
	}

	public boolean isAsync() {
		return async;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SchedulerConfig that = (SchedulerConfig) o;
		return rate == that.rate && async == that.async;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rate, async);
	}

}
