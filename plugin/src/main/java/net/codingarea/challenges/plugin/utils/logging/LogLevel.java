package net.codingarea.challenges.plugin.utils.logging;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LogLevel extends Level {

	public static final Level
			DEBUG = new LogLevel("DEBUG", Level.CONFIG.intValue());

	public LogLevel(@Nonnull String name, int value) {
		super(name, value);
	}

}
