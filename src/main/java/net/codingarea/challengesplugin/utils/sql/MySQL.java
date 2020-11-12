package net.codingarea.challengesplugin.utils.sql;

import net.codingarea.challengesplugin.utils.commons.Log;

import java.sql.*;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class MySQL {

	public static void createDatabases() {
		try {
			set("CREATE TABLE IF NOT EXISTS user (user VARCHAR(150), player VARCHAR(16), settings VARCHAR(10000), stats VARCHAR(3000))");
		} catch (SQLException ex) {
			Log.severe("Could not create default tables :: " + ex.getMessage());
		}
	}

	public static final String URL_TEMPLATE = "jdbc:mysql://%host/%database?autoReconnect=true&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=true";

	private static Connection connection;

	public static void connectWithException(String host, String database, String user, String password) throws SQLException {

		String url = URL_TEMPLATE
				.replace("%host", host)
				.replace("%database", database);

		connection = DriverManager.getConnection(url, user, password);
		Log.info("Database connection created!");

	}

	public static void connect(String host, String database, String user, String password) {
		try {
			connectWithException(host, database, user, password);
		} catch (Exception ex) {
			Log.severe("Could not connect to MySQL server :: " + ex.getMessage());
		}
	}

	public static void disconnectWithException() throws SQLException {
		connection.close();
	}

	public static void disconnect() {
		try {
			disconnectWithException();
		} catch (SQLException ex) {
			Log.severe("Could not disconnect from MySQL server :: " + ex.getMessage());
		}
	}

	public static void set(String query) throws SQLException {
		if (connection == null) throw new SQLException("No connection to MySQL server found");
		PreparedStatement statement = connection.prepareStatement(query);
		statement.executeUpdate();
	}

	public static ResultSet get(String query) throws SQLException {
		if (connection == null) throw new SQLException("No connection to MySQL server found");
		PreparedStatement statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static boolean isSet(String query) throws SQLException {
		ResultSet result = get(query);
		return result != null && result.next();
	}

}
