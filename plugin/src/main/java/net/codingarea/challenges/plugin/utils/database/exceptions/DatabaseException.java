package net.codingarea.challenges.plugin.utils.database.exceptions;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class DatabaseException extends Exception {

	protected DatabaseException() {
		super();
	}

	protected DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(@Nonnull Throwable cause) {
		super(cause);
	}

}
