package net.codingarea.challenges.plugin.lang.loader;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.misc.ConsolePrint;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class ContentLoader {

	private static volatile int loads = 0;

	@Nonnull
	protected final File getFolder() {
		File folder = new File(Challenges.getInstance().getDataFolder(), "messages");
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}

	@Nonnull
	protected final File getFile(@Nonnull String name, @Nonnull String extension) {
		return new File(getFolder(), name + "." + extension);
	}

	private void execute() {
		Thread thread = new Thread(() -> {
			startLoading();
			load();
			finishLoading();
		});
		thread.start();
	}

	protected abstract void load();

	private static synchronized void startLoading() {
		loads++;
	}

	private static synchronized void finishLoading() {
		loads--;
	}

	public static boolean isLoading() {
		return loads > 0;
	}

	public static void executeLoaders(@Nonnull ContentLoader... loaders) {
		if (isLoading()) {
			ConsolePrint.alreadyExecutingContentLoader();
			return;
		}
		for (ContentLoader loader : loaders) {
			loader.execute();
		}
	}

}