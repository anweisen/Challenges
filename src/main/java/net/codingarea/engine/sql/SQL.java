package net.codingarea.engine.sql;

import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.engine.sql.constant.ConstSQL;
import net.codingarea.engine.sql.source.DataSource;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class SQL {

	/**
	 * @deprecated You should use the {@link PreparedStatement} to prevent SQLInjection.
	 *             You can use ? as placeholders and set its value afterwords using {@link PreparedStatement#setObject(int, Object)}
	 *             If you use {@link #prepare(String, Object...)} it will already set the object array as the params to the {@link PreparedStatement}
	 */
	@Nonnull
	@Deprecated
	@CheckReturnValue
	public static String removeInjectionPossibility(@Nonnull String string) {
		return string.replaceAll("[']", "\\\\'").replaceAll("[`]", "\\\\`");
	}

	/**
	 * @param result The {@link ResultSet} which should be stored into the {@link CachedRowSet}
	 * @return A new {@link CachedRowSet} using {@link RowSetProvider#newFactory()} as factory
	 * @throws SQLException If a {@link SQLException} is thrown while creating the {@link CachedRowSet}
	 * @see CachedRowSet#populate(ResultSet)
	 */
	@Nonnull
	@CheckReturnValue
	public static CachedRowSet cache(@Nonnull ResultSet result) throws SQLException {
		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(result);
		return cachedRowSet;
	}


	/**
	 * @see PreparedStatement#setObject(int, Object)
	 */
	public static void fillParams(@Nonnull PreparedStatement statement, @Nonnull Object... params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			statement.setObject(i+1, params[i]);
		}
	}

	@Nonnull
	@CheckReturnValue
	public static SQL anonymous(@Nonnull DataSource dataSource) throws SQLException {
		SQL instance = new SQL(dataSource) { };
		instance.connect();
		return instance;
	}

	public static void disconnect(SQL sql) {
		if (sql != null) {
			try {
				sql.disconnect();
			} catch (Throwable ignored) { }
		}
	}

	protected final DataSource dataSource;
	protected Connection connection;
	protected Logger logger = Log.getLogger();

	public SQL(@Nonnull DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SQL(@Nonnull DataSource dataSource, @Nonnull Logger logger) {
		this.dataSource = dataSource;
		this.logger = logger;
	}

	@CheckReturnValue
	public boolean connectionIsOpened() {
		try {
			return connection != null && !connection.isClosed();
		} catch (Exception ignored) {
			return false;
		}
	}

	/**
	 * Terminates the existing connection, using {@link #disconnect()}
	 * Then it creates a new connection using {@link DataSource#createConnection()}
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while disconnection or creating a new connection to the sql server
	 */
	public void connect() throws SQLException {
		if (connectionIsOpened()) {
			disconnect();
		}
		connection = dataSource.createConnection();
		if (logger != null) logger.log(Level.INFO, "Connection to database successfully created");
	}

	/**
	 * Closes the the connection ({@link #getConnection()}) to the sql server using {@link Connection#close()}
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while closing the connection
	 */
	public void disconnect() throws SQLException {
		connection.close();
		if (logger != null) logger.log(Level.INFO, "Connection to database closed");
	}

	public void disconnectSafely() {
		try {
			disconnect();
		} catch (Throwable ex) {
			logger.log(Level.WARNING, "Exception while disconnecting", ex);
		}
	}

	/**
	 * Connects to the sql server ({@link #connect()}) if the connection is no longer opened (not {@link #connectionIsOpened()})
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while connecting to the server
	 */
	public void verifyConnection() throws SQLException {
		if (!connectionIsOpened()) {
			connect();
		}
	}

	@Nonnull
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	@Nonnull
	public CachedRowSet executeQuery(@Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	/**
	 * Executes a update to the database using {@link Statement#executeUpdate(String)}.
	 * Be aware of SQLInjection.
	 *
	 * @see #update(String, Object...)
	 * @param sql The command which should be executed
	 * @throws SQLException If a {@link SQLException} is thrown while creating a {@link Statement} (using {@link #createStatement()}),
	 *                      executing the update (using {@link Statement#executeUpdate(String)})
	 *                      or closing the statement (using {@link Statement#close()})
	 */
	public int executeUpdate(@Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		int result = statement.executeUpdate(sql);
		statement.close();
		return result;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedStatement prepare(@Nonnull String sql) throws SQLException {
		verifyConnection();
		return connection.prepareStatement(sql);
	}

	/**
	 * Creates a {@link PreparedStatement} using {@link #prepare(String)} and sets the params using {@link #fillParams(PreparedStatement, Object...)}
	 *
	 * @param sql The SQLCommand
	 * @param params The params which replace the ?s in the command.
	 * @return The {@link PreparedStatement} just created
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while preparing the {@link PreparedStatement} ({@link #prepare(String)})
	 *         or while filling the params ({@link #fillParams(PreparedStatement, Object...)})
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedStatement prepare(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql);
		fillParams(statement, params);
		return statement;
	}

	public CachedRowSet query(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		ResultSet resultSet = statement.executeQuery();
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	public int update(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		int result = statement.executeUpdate();
		statement.close();
		return result;
	}

	public boolean isSet(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		ResultSet result = query(sql, params);
		boolean set = result.next();
		result.close();
		return set;
	}

	public boolean paramIsSet(@Nonnull String sql, @Nonnull String param, @Nonnull Object... params) throws SQLException {
		ResultSet result = query(sql, params);
		boolean set = false;
		if (result.next()) {
			set = result.getObject(param) != null;
		}
		result.close();
		return set;
	}

	@Nonnull
	@CheckReturnValue
	public Connection getConnection() {
		return connection;
	}

	@Nonnull
	@CheckReturnValue
	public DataSource getDataSource() {
		return dataSource;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Nonnull
	public <T extends SQL> T setConstant() {
		ConstSQL.setInstance(this);
		return (T) this;
	}

	@Nonnull
	@Override
	public String toString() {
		return "SQL{" +
				"class=" + this.getClass().getSimpleName() +
				", dataSource=" + dataSource +
				", connection=" + connection +
				'}';
	}
}
