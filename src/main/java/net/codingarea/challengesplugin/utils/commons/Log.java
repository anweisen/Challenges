package net.codingarea.challengesplugin.utils.commons;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class Log {

	private static class StaticLogger extends Logger {
		public StaticLogger(String name) {
			super(name, null);
		}
	}

	private static Logger logger = new StaticLogger("static");

	static {
		logger.setLevel(Level.ALL);
		logger.addHandler(new Handler() {
			@Override
			public void publish(LogRecord record) {
				System.err.println("[" + getLevelName(record.getLevel()) + "]: " + record.getMessage());
			}

			@Override
			public void flush() { }

			@Override
			public void close() throws SecurityException { }

			private String getLevelName(Level level) {
				if (level == Level.SEVERE) {
					return "ERROR";
				} else {
					return level.getName();
				}
			}

		});
	}

	public static void log(Level level, String message) {
		logger.log(level, message);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warning(String message) {
		logger.warning(message);
	}

	public static void severe(String message) {
		logger.severe(message);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(@NotNull Logger logger) {
		Log.logger = logger;
	}

}
