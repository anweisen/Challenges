package net.codingarea.challenges.plugin.utils.database.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class DatabaseConnectionClosedException extends DatabaseException {

	public DatabaseConnectionClosedException() {
		super("Database connection closed");
	}

}
