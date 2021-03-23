package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
final class TimerTaskExecutor extends AbstractTaskExecutor {

	private final TimerTaskConfig config;

	TimerTaskExecutor(@Nonnull TimerTaskConfig config) {
		this.config = config;
	}

	public void execute() {
		if (config.isAsync()) Bukkit.getScheduler().runTaskAsynchronously(Challenges.getInstance(), this);
		else                  Bukkit.getScheduler().runTask(Challenges.getInstance(), this);
	}

	@Nonnull
	@Override
	public TimerTaskConfig getConfig() {
		return config;
	}

}
