package net.codingarea.challenges.plugin.management.database;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.database.Database;
import net.codingarea.challenges.plugin.utils.database.DatabaseConfig;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.MongoDBDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.mysql.MySQLDatabase;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class DatabaseManager {

	private Database database;

	public DatabaseManager() {
		Document document = Challenges.getInstance().getConfigDocument().getDocument("database");

		String type = document.getString("type", "none");
		if (("none").equals(type)) return;

		try {
			Class<? extends Database> classOfDatabase = getDatabaseForName(type);
			if (classOfDatabase == null) {
				Logger.severe("Selected database type '" + type + "' which is unknown");
				return;
			}

			DatabaseConfig config = new DatabaseConfig(document.getDocument(type));
			database = classOfDatabase.getDeclaredConstructor(DatabaseConfig.class).newInstance(config);
		} catch (Throwable ex) {
			Logger.severe("Could not create database", ex);
		}
	}

	public void connectIfCreated() {
		if (database != null) {
			Thread thread = new Thread(database::connectSafely);
			thread.setName("DatabaseConnector");
			thread.start();
		}
	}

	public void disconnectIfConnected() {
		if (database != null) {
			Thread thread = new Thread(database::disconnectSafely);
			thread.setName("DatabaseDisconnector");
			thread.start();
		}
	}

	@Nullable
	private Class<? extends Database> getDatabaseForName(@Nonnull String type) {
		switch (type) {
			case "mongodb":
				return MongoDBDatabase.class;
			case "mysql":
				return MySQLDatabase.class;
			case "sqlite":
				return SQLiteDatabase.class;
			default:
				return null;
		}
	}

	public boolean isConnected() {
		return database != null && database.isConnected();
	}

	public Database getDatabase() {
		return database;
	}

}
