package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.deletion;

import net.codingarea.challenges.plugin.utils.database.DatabaseDeletion;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.ObjectWhere;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.SQLWhere;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.StringIgnoreCaseWhere;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class SQLDeletion implements DatabaseDeletion {

	protected final AbstractSQLDatabase database;
	protected final String table;
	protected final Map<String, SQLWhere> where = new HashMap<>();

	public SQLDeletion(@Nonnull AbstractSQLDatabase database, @Nonnull String table) {
		this.database = database;
		this.table = table;
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable Object value) {
		where.put(column, new ObjectWhere(column, value));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable Number value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable String value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable String value, boolean ignoreCase) {
		if (!ignoreCase) return where(column, value);
		if (value == null) throw new NullPointerException("Cannot use where ignore case with null value");
		where.put(column, new StringIgnoreCaseWhere(column, value));
		return this;
	}

	@Nonnull
	protected PreparedStatement prepare() throws SQLException, DatabaseException {
		StringBuilder command = new StringBuilder();
		List<Object> args = new ArrayList<>();

		command.append("DELETE FROM ");
		command.append(table);

		if (!where.isEmpty()) {
			command.append(" WHERE ");
			int index = 0;
			for (Entry<String, SQLWhere> entry : where.entrySet()) {
				SQLWhere where = entry.getValue();
				if (index > 0) command.append(" AND ");
				command.append(where.getAsSQLString());
				args.addAll(Arrays.asList(where.getArgs()));
				index++;
			}
		}

		return database.prepare(command.toString(),args.toArray());
	}

	@Override
	public void execute() throws DatabaseException {
		try {
			PreparedStatement statement = prepare();
			statement.execute();
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

}
