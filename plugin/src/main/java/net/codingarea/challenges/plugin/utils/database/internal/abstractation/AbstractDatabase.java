package net.codingarea.challenges.plugin.utils.database.internal.abstractation;

import net.codingarea.challenges.plugin.utils.database.Database;
import net.codingarea.challenges.plugin.utils.database.DatabaseConfig;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseConnectionClosedException;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractDatabase implements Database {

	protected final DatabaseConfig config;

	public AbstractDatabase(@Nonnull DatabaseConfig config) {
		this.config = config;
	}

	@Override
	public void disconnectSafely() {
		try {
			disconnect();
			Logger.info("Successfully closed connection to database of type " + this.getClass().getSimpleName());
		} catch (DatabaseException ex) {
			Logger.severe("Could not disconnect from database (" + this.getClass().getSimpleName() + ")", ex);
		}
	}

	@Override
	public void connectSafely() {
		try {
			connect();
			Logger.info("Successfully created connection to database of type " + this.getClass().getSimpleName());
		} catch (DatabaseException ex) {
			Logger.severe("Could not connect to database (" + this.getClass().getSimpleName() + ")", ex);
		}
	}

	@Nonnull
	@Override
	public DatabaseConfig getConfig() {
		return config;
	}

	protected final void verifyConnection() throws DatabaseConnectionClosedException {
		if (!isConnected())
			throw new DatabaseConnectionClosedException();
	}

}
