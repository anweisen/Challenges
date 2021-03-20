package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.query;

import net.codingarea.challenges.plugin.utils.database.DatabaseQuery;
import net.codingarea.challenges.plugin.utils.database.ExecutedQuery;
import net.codingarea.challenges.plugin.utils.database.Order;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.ObjectWhere;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.SQLWhere;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.StringIgnoreCaseWhere;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class SQLQuery implements DatabaseQuery {

	protected final AbstractSQLDatabase database;
	protected final String table;
	protected final Map<String, SQLWhere> where;
	protected String[] selection = { "*" };
	protected String orderBy;
	protected Order order;

	public SQLQuery(@Nonnull AbstractSQLDatabase database, @Nonnull String table) {
		this.database = database;
		this.table = table;
		this.where = new HashMap<>();
	}

	public SQLQuery(@Nonnull AbstractSQLDatabase database, @Nonnull String table, @Nonnull Map<String, SQLWhere> where) {
		this.database = database;
		this.table = table;
		this.where = where;
	}

	@Nonnull
	@Override
	public DatabaseQuery where(@Nonnull String column, @Nullable Object object) {
		where.put(column, new ObjectWhere(column, object));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseQuery where(@Nonnull String column, @Nullable Number value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseQuery where(@Nonnull String column, @Nullable String value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseQuery where(@Nonnull String column, @Nullable String value, boolean ignoreCase) {
		if (!ignoreCase) return where(column, value);
		if (value == null) throw new NullPointerException("Cannot use where ignore case with null value");
		where.put(column, new StringIgnoreCaseWhere(column, value));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseQuery orderBy(@Nonnull String column, @Nonnull Order order) {
		this.orderBy = column;
		this.order = order;
		return this;
	}

	@Nonnull
	@Override
	public DatabaseQuery select(@Nonnull String... selection) {
		this.selection = selection;
		return this;
	}

	@Nonnull
	protected PreparedStatement prepare() throws SQLException, DatabaseException {
		StringBuilder command = new StringBuilder();
		List<Object> args = new LinkedList<>();

		command.append("SELECT ");
		for (int i = 0; i < selection.length; i++) {
			if (i > 0) command.append(", ");
			command.append(selection[i]);
		}
		command.append(" FROM ");
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

		if (orderBy != null) {
			command.append(" ORDER BY ");
			command.append(orderBy);
			if (order != null)
				command.append(" " + (order == Order.HIGHEST ? "DESC" : "ASC"));
			command.append(" ");
		}

		return database.prepare(command.toString(), args.toArray());
	}

	@Nonnull
	@Override
	public ExecutedQuery execute() throws DatabaseException {
		try {
			PreparedStatement statement = prepare();
			ResultSet result = statement.executeQuery();
			return new ExecutedSQLQuery(result);
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

}
