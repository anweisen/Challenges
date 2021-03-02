package net.codingarea.challenges.plugin.management.scheduler;

import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ScheduleManager {

	private final Map<SchedulerConfig, Scheduler> schedulerByConfig = new ConcurrentHashMap<>();
	private boolean started = false;

	public void register(@Nonnull Object scheduler) {
		for (Method method : ReflectionUtils.getDeclaredMethodsAnnotatedWith(scheduler.getClass(), Scheduled.class)) {
			if (method.getParameterCount() != 0) {
				Logger.warn("Could not register scheduler " + method);
				continue;
			}

			Scheduled annotation = method.getAnnotation(Scheduled.class);
			ScheduledFunction function = new ScheduledFunction(scheduler, method, annotation);

			register(function, annotation);
		}
	}

	public void unregister(@Nonnull Object object) {
		for (Scheduler scheduler : schedulerByConfig.values()) {
			scheduler.unregister(object);
		}
	}

	private void register(@Nonnull ScheduledFunction function, @Nonnull Scheduled annotation) {
		SchedulerConfig config = new SchedulerConfig(annotation);
		if (config.getRate() < 1) throw new IllegalArgumentException("Schedule rate cannot be less than 1");

		Scheduler scheduler = getOrCreateScheduler(config);
		scheduler.register(function);
	}

	@Nonnull
	private Scheduler getOrCreateScheduler(@Nonnull SchedulerConfig config) {
		Scheduler scheduler = schedulerByConfig.get(config);
		if (scheduler != null) return scheduler;

		// Create new task
		scheduler = new Scheduler(config);
		if (started) scheduler.start();
		schedulerByConfig.put(config, scheduler);
		return scheduler;
	}

	public void stop() {
		started = false;
		schedulerByConfig.values().forEach(Scheduler::stop);
		schedulerByConfig.clear();
	}

	public void start() {
		started = true;
		schedulerByConfig.values().forEach(Scheduler::start);
	}

}
