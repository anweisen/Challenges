package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction;

import net.codingarea.challenges.plugin.utils.database.*;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.abstractation.AbstractDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.deletion.SQLDeletion;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.insertion.SQLInsertion;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.insertorupdate.SQLInsertionOrUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.query.SQLQuery;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.update.SQLUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.SQLWhere;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractSQLDatabase extends AbstractDatabase {

	protected Connection connection;

	public AbstractSQLDatabase(@Nonnull DatabaseConfig config) {
		super(config);
	}

	@Override
	public void disconnect() throws DatabaseException {
		verifyConnection();
		try {
			connection.close();
			connection = null;
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void connect() throws DatabaseException {
		try {
			connection = DriverManager.getConnection(createURL(), config.getUser(), config.getPassword());
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	protected abstract String createURL();

	@Override
	public boolean isConnected() {
		try {
			if (connection == null) return false;
			connection.isClosed();
			return true;
		} catch (SQLException ex) {
			Logger.severe("Could not check connection state: " + ex.getMessage());
			return false;
		}
	}

	@Override
	public void createTableIfNotExists(@Nonnull String name, @Nonnull SQLColumn... columns) throws DatabaseException {
		try {
			StringBuilder command = new StringBuilder();
			command.append("CREATE TABLE IF NOT EXISTS ");
			command.append(name);
			command.append(" (");
			{
				int index = 0;
				for (SQLColumn column : columns) {
					if (index > 0) command.append(", ");
					command.append(column.getName());
					command.append(" ");
					command.append(column.getType());
					command.append("(");
					command.append(column.getLength());
					command.append(") ");
					index++;
				}
			}
			command.append(")");

			PreparedStatement statement = prepare(command.toString());
			statement.execute();

		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	@Nonnull
	@Override
	public DatabaseQuery query(@Nonnull String table) {
		return new SQLQuery(this, table);
	}

	@Nonnull
	public DatabaseQuery query(@Nonnull String table, @Nonnull Map<String, SQLWhere> where) {
		return new SQLQuery(this, table, where);
	}

	@Nonnull
	@Override
	public DatabaseUpdate update(@Nonnull String table) {
		return new SQLUpdate(this, table);
	}

	@Nonnull
	@Override
	public DatabaseInsertion insert(@Nonnull String table) {
		return new SQLInsertion(this, table);
	}

	@Nonnull
	@Override
	public DatabaseInsertion insert(@Nonnull String table, @Nonnull Map<String, Object> values) {
		return new SQLInsertion(this, table, values);
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate insertOrUpdate(@Nonnull String table) {
		return new SQLInsertionOrUpdate(this, table);
	}

	@Nonnull
	@Override
	public DatabaseDeletion delete(@Nonnull String table) {
		return new SQLDeletion(this, table);
	}

	public PreparedStatement prepare(@Nonnull String command, @Nonnull Object... args) throws SQLException, DatabaseException {
		verifyConnection();
		PreparedStatement statement = connection.prepareStatement(command);
		SQLHelper.fillParams(statement, args);
		return statement;
	}

}
