package net.codingarea.challenges.plugin.utils.logging;

import net.codingarea.challenges.plugin.Challenges;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class Logger {

	private Logger() {
	}

	@Nonnull
	private static java.util.logging.Logger getLogger() {
		return Challenges.getInstance().getLogger();
	}

	public static void severe(@Nonnull String message) {
		getLogger().log(LogLevel.SEVERE, message);
	}

	public static void severe(@Nonnull String message, @Nonnull Throwable thrown) {
		getLogger().log(LogLevel.SEVERE, message, thrown);
	}

	public static void info(@Nonnull String message) {
		getLogger().log(LogLevel.INFO, message);
	}

	public static void debug(@Nonnull String message) {
		getLogger().log(LogLevel.DEBUG, message);
	}

	public static void warn(@Nonnull String message) {
		getLogger().log(LogLevel.WARNING, message);
	}

}
