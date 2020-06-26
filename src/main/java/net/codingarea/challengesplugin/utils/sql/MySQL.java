package net.codingarea.challengesplugin.utils.sql;

import net.codingarea.challengesplugin.Challenges;

import java.sql.*;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class MySQL {

	public static void createDatabases() {
		try {
			set("CREATE TABLE IF NOT EXISTS user (user VARCHAR(16), settings VARCHAR(10000))");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static final String URL_TEMPLATE = "jdbc:mysql://%host/%database?serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=true";

	private static Connection connection;

	public static void connect(String host, String database, String user, String password) {

		try {

			String url = URL_TEMPLATE
					.replace("%host", host)
					.replace("%database", database);

			connection = DriverManager.getConnection(url, user, password);
			Challenges.getInstance().getLogger().info("Database connection created!");

		} catch (Exception ex) {
			Challenges.getInstance().getLogger().severe("Could not connect to mysql server :: " + ex.getMessage());
		}

	}

	public static void set(String query) throws SQLException {
		if (connection == null) throw new SQLException("No connection to mysql server found");
		PreparedStatement statement = connection.prepareStatement(query);
		statement.executeUpdate();
	}

	public static ResultSet get(String query) throws SQLException {
		if (connection == null) throw new SQLException("No connection to mysql server found");
		PreparedStatement statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static boolean isSet(String query) throws SQLException {
		ResultSet result = get(query);
		return result != null && result.next();
	}

}
