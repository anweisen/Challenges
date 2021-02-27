package net.codingarea.challenges.plugin.utils.database;

import net.codingarea.challenges.plugin.utils.config.Propertyable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class DatabaseConfig {

	private final String host;
	private final String database;
	private final String authDatabase;
	private final String password;
	private final String user;
	private final int port;
	private final boolean portIsSet;

	public DatabaseConfig(String host, String database, String password, String user, int port) {
		this(host, database, null, password, user, port, true);
	}
	public DatabaseConfig(String host, String database, String password, String user) {
		this(host, database, null, password, user, 0, false);
	}

	public DatabaseConfig(String host, String database, String authDatabase, String password, String user, int port) {
		this(host, database, authDatabase, password, user, port, true);
	}
	public DatabaseConfig(String host, String database, String authDatabase, String password, String user) {
		this(host, database, authDatabase, password, user, 0, false);
	}

	public DatabaseConfig(String host, String database, String authDatabase, String password, String user, int port, boolean portIsSet) {
		this.host = host;
		this.database = database;
		this.authDatabase = authDatabase;
		this.password = password;
		this.user = user;
		this.port = port;
		this.portIsSet = portIsSet;
	}

	public DatabaseConfig(@Nonnull Propertyable config) {
		this(
				config.getString("host"),
				config.getString("database"),
				config.getString("auth-database"),
				config.getString("password"),
				config.getString("user"),
				config.getInt("port"),
				config.contains("port")
		);
	}

	public int getPort() {
		return port;
	}

	public String getAuthDatabase() {
		return authDatabase;
	}

	public String getDatabase() {
		return database;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public String getUser() {
		return user;
	}

	public boolean isPortSet() {
		return portIsSet;
	}

}
