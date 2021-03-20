package net.codingarea.challenges.plugin.utils.database;

import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface DatabaseInsertionOrUpdate extends DatabaseUpdate, DatabaseInsertion {

	@Nonnull
	@CheckReturnValue
	DatabaseInsertionOrUpdate where(@Nonnull String field, @Nullable Object value);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertionOrUpdate where(@Nonnull String field, @Nullable Number value);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertionOrUpdate where(@Nonnull String field, @Nullable String value, boolean ignoreCase);

	@Nonnull
	@CheckReturnValue
	DatabaseInsertionOrUpdate where(@Nonnull String field, @Nullable String value);

	@Nonnull
	@Override
	DatabaseInsertionOrUpdate set(@Nonnull String field, @Nullable Object value);

	void execute() throws DatabaseException;

}
