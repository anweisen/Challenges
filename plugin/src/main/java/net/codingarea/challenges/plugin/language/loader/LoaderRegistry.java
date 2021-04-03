package net.codingarea.challenges.plugin.language.loader;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LoaderRegistry {

	private class Subscribers {

		private final Class<? extends ContentLoader> loader;
		private final Collection<Runnable> actions = new ArrayList<>(1);
		private boolean executed = false;

		public Subscribers(Class<? extends ContentLoader> loader) {
			this.loader = loader;
		}

		public void execute() {
			Logger.debug("Executing subscribers for {}", loader.getSimpleName());
			executed = true;
			for (Runnable subscriber : actions) {
				try {
					subscriber.run();
				} catch (Exception ex) {
					Logger.error("Could not execute subscriber for {}", loader.getSimpleName(), ex);
				}
			}
		}

	}

	private static final AtomicInteger loading = new AtomicInteger(); // Keeps track of how many loaders are still loading
	private final Map<Class<? extends ContentLoader>, Subscribers> subscribers = new HashMap<>();
	private final Collection<ContentLoader> loaders;

	public LoaderRegistry(@Nonnull ContentLoader... loaders) {
		this.loaders = Arrays.asList(loaders);
	}

	public void load() {
		if (isLoading()) {
			ConsolePrint.alreadyExecutingContentLoader();
			return;
		}

		loaders.forEach(this::executeLoader);
	}

	private void executeLoader(@Nonnull ContentLoader loader) {
		Challenges.getInstance().runAsync(() -> {
			loading.incrementAndGet();
			loader.load();
			loading.decrementAndGet();
			handleCompleteLoading(loader.getClass());
		});
	}

	private void handleCompleteLoading(@Nonnull Class<? extends ContentLoader> classOfLoader) {
		Logger.debug("{} finished loading. {} loader(s) left", classOfLoader.getSimpleName(), loading);

		if (Challenges.getInstance().isEnabled()) {
			Subscribers subscribers = this.subscribers.get(classOfLoader);
			if (subscribers == null) return;
			subscribers.execute();
		}
	}

	public void enable() {
		for (Subscribers subscribers : subscribers.values()) {
			if (subscribers.executed) continue;
			subscribers.execute();
		}
	}

	public void disable() {
		subscribers.clear();
	}

	public boolean isLoading() {
		return loading.get() > 0;
	}

	public void subscribe(@Nonnull Class<? extends ContentLoader> classOfLoader, @Nonnull Runnable action) {
		Subscribers subscribers = this.subscribers.computeIfAbsent(classOfLoader, key -> new Subscribers(classOfLoader));
		subscribers.actions.add(action);
	}

}
