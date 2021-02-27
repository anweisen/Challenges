package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface SQLWhere {

	@Nonnull
	Object[] getArgs();

	@Nonnull
	String getAsSQLString();

}
