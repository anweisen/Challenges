package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class StringIgnoreCaseWhere implements SQLWhere {

	protected final String column;
	protected final String value;

	public StringIgnoreCaseWhere(@Nonnull String column, @Nonnull String value) {
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
		return String.format("LOWER(%s) = LOWER(?)", column);
	}

}
