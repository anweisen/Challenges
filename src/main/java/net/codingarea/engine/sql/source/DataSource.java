package net.codingarea.engine.sql.source;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Used to create database connections ({@link #createConnection()})
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface DataSource {

	String MYSQL_URL = "jdbc:mysql://%host/%database",
		   LITESQL_URL = "jdbc:sqlite:%file";

	/**
	 * @return Creates a connection to the SQL server using the url ({@link #getURL()}) (Default: using {@link DriverManager#getConnection(String)})
	 * @throws SQLException If a {@link SQLException} happens while creating the connection
	 * @see DriverManager#getConnection(String)
	 */
	@Nonnull
	@CheckReturnValue
	default Connection createConnection() throws SQLException {
		return DriverManager.getConnection(getURL());
	}

	/**
	 * @return The URL which is used to connect to a database
	 * @see #createConnection()
	 */
	@Nonnull
	@CheckReturnValue
	String getURL();

}
