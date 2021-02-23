package net.codingarea.challenges.plugin.management.scheduler;

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

	public void register(@Nonnull Object object) {
		for (Method method : ReflectionUtils.getDeclaredMethodsAnnotatedWith(object.getClass(), Scheduled.class)) {
			Scheduled annotation = method.getAnnotation(Scheduled.class);
			ScheduledFunction function = new ScheduledFunction(object, method, annotation);

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
		schedulerByConfig.put(config, scheduler);
		return scheduler;
	}

	public void stop() {
		schedulerByConfig.values().forEach(Scheduler::stop);
		schedulerByConfig.clear();
	}

	public void start() {
		schedulerByConfig.values().forEach(Scheduler::start);
	}

}
