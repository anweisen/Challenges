package net.codingarea.challenges.plugin.utils.database;

import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface DatabaseInsertion {

	@Nonnull
	@CheckReturnValue
	DatabaseInsertion set(@Nonnull String field, @Nullable Object value);

	void execute() throws DatabaseException;

}
