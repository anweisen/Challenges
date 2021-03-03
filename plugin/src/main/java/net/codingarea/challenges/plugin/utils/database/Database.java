package net.codingarea.challenges.plugin.utils.database;

import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Database {

	boolean isConnected();

	void connect() throws DatabaseException;
	void connectSafely();

	void disconnect() throws DatabaseException;
	void disconnectSafely();

	void createTableIfNotExists(@Nonnull String name, @Nonnull SQLColumn... columns) throws DatabaseException;
	void createTableIfNotExistsSafely(@Nonnull String name, @Nonnull SQLColumn... columns);

	@Nonnull
	@CheckReturnValue
	DatabaseQuery query(@Nonnull String table);

	@Nonnull
	@CheckReturnValue
	DatabaseUpdate update(@Nonnull String table);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertion insert(@Nonnull String table);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertion insert(@Nonnull String table, @Nonnull Map<String, Object> values);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertionOrUpdate insertOrUpdate(@Nonnull String table);

	@Nonnull
	@CheckReturnValue
	DatabaseDeletion delete(@Nonnull String table);

	@Nonnull
	DatabaseConfig getConfig();

}
