package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.update;

import net.codingarea.challenges.plugin.utils.database.DatabaseUpdate;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.SQLHelper;
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
public class SQLUpdate implements DatabaseUpdate {

	protected final AbstractSQLDatabase database;
	protected final String table;
	protected final Map<String, SQLWhere> where;
	protected final Map<String, Object> values;

	public SQLUpdate(@Nonnull AbstractSQLDatabase database, @Nonnull String table) {
		this.database = database;
		this.table = table;
		this.where = new HashMap<>();
		this.values = new HashMap<>();
	}

	public SQLUpdate(@Nonnull AbstractSQLDatabase database, @Nonnull String table, @Nonnull Map<String, SQLWhere> where, @Nonnull Map<String, Object> values) {
		this.database = database;
		this.table = table;
		this.where = where;
		this.values = values;
	}

	@Nonnull
	@Override
	public DatabaseUpdate where(@Nonnull String column, @Nullable Object value) {
		where.put(column, new ObjectWhere(column, value));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseUpdate where(@Nonnull String column, @Nullable Number value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseUpdate where(@Nonnull String column, @Nullable String value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseUpdate where(@Nonnull String column, @Nullable String value, boolean ignoreCase) {
		if (!ignoreCase) return where(column, value);
		if (value == null) throw new NullPointerException("Cannot use where ignore case with null value");
		where.put(column, new StringIgnoreCaseWhere(column, value));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseUpdate set(@Nonnull String column, @Nullable Object value) {
		values.put(column, value);
		return this;
	}

	@Nonnull
	protected PreparedStatement prepare() throws SQLException, DatabaseException {
		if (values.isEmpty()) throw new IllegalArgumentException("Can't update nothing");

		StringBuilder command = new StringBuilder();
		List<Object> args = new ArrayList<>();

		command.append("UPDATE ");
		command.append(table);
		command.append(" SET ");

		{
			int index = 0;
			for (Entry<String, Object> entry : values.entrySet()) {
				if (index > 0) command.append(", ");
				command.append(entry.getKey() + " = ?");
				args.add(entry.getValue());
				index++;
			}
		}

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

		return database.prepare(command.toString(), args.toArray());
	}

	@Override
	public void execute() throws DatabaseException {
		try {
			PreparedStatement statement = prepare();
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

}
