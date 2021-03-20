package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ObjectWhere implements SQLWhere {

	protected final String column;
	protected final Object value;

	public ObjectWhere(@Nonnull String column, @Nullable Object value) {
		this.column = column;
		this.value = value;
	}

	@Nonnull
	@Override
	public Object[] getArgs() {
		return new Object[] { value };
	}

	@Nonnull
	@Override
	public String getAsSQLString() {
		return String.format("%s = ?", column);
	}

}
