package net.codingarea.challenges.plugin.utils.logging;

import net.anweisen.utilities.commons.logging.JavaILogger;
import net.codingarea.challenges.plugin.Challenges;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class Logger {

	private Logger() {}

	@Nonnull
	public static JavaILogger getInstance() {
		return Challenges.getInstance().getLogger();
	}

	public static void error(@Nullable Object message, @Nonnull Object... args) {
		getInstance().error(message, args);
	}

	public static void warn(@Nullable Object message, @Nonnull Object... args) {
		getInstance().warn(message, args);
	}

	public static void info(@Nullable Object message, @Nonnull Object... args) {
		getInstance().info(message, args);
	}

	public static void debug(@Nullable Object message, @Nonnull Object... args) {
		getInstance().debug(message, args);
	}

}
