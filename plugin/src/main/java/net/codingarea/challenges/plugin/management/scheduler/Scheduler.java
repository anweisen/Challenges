package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see ScheduleManager
 */
final class Scheduler implements Runnable {

	private final List<ScheduledFunction> functions = new ArrayList<>(1);
	private final SchedulerConfig config;
	private BukkitTask task;

	Scheduler(@Nonnull SchedulerConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		for (ScheduledFunction function : functions) {
			try {
				function.invoke();
			} catch (InvocationTargetException | IllegalAccessException ex) {
				Logger.severe("An exception occurred while executing " + function, ex);
			}
		}
	}

	public void stop() {
		if (task != null && !task.isCancelled()) {
			task.cancel();
			task = null;
		}
	}

	public void start() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		Challenges plugin = Challenges.getInstance();
		task = config.isAsync() ? scheduler.runTaskTimerAsynchronously(plugin, this, 0, config.getRate()) :
								  scheduler.runTaskTimer(plugin, this, 0, config.getRate());
	}

	public void register(@Nonnull ScheduledFunction function) {
		functions.add(function);
	}

	public void unregister(@Nonnull Object object) {
		functions.removeIf(function -> function.getHolder() == object);
	}

}
