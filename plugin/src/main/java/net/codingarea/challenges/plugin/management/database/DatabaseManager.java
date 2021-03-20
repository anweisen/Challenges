package net.codingarea.challenges.plugin.management.database;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.database.Database;
import net.anweisen.utilities.database.DatabaseConfig;
import net.anweisen.utilities.database.SQLColumn;
import net.anweisen.utilities.database.internal.mongodb.MongoDBDatabase;
import net.anweisen.utilities.database.internal.sql.mysql.MySQLDatabase;
import net.anweisen.utilities.database.internal.sql.sqlite.SQLiteDatabase;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class DatabaseManager {

	private Database database;

	public DatabaseManager() {
		Document document = Challenges.getInstance().getConfigDocument().getDocument("database");

		String type = document.getString("type", "none").toLowerCase();
		if ("none".equals(type)) return;

		// Check dependencies
		if ("mongodb".equals(type)) {
			try {
				Class.forName("net.codingarea.challenges.mongoconnector.MongoConnector");
			} catch (ClassNotFoundException | NoClassDefFoundError ex) {
				Logger.severe("Cannot use mongodb database without Challenges-MongoConnector");
				return;
			}
		}

		try {
			Class<? extends Database> classOfDatabase = getDatabaseForName(type);
			if (classOfDatabase == null) {
				Logger.severe("Selected database type '" + type + "' which is unknown");
				return;
			}

			DatabaseConfig config = new DatabaseConfig(document.getDocument(type));
			Constructor<? extends Database> constructor = classOfDatabase.getDeclaredConstructor(DatabaseConfig.class);
			database = constructor.newInstance(config);
		} catch (Throwable ex) {
			Logger.severe("Could not create database", ex);
		}
	}

	public void connectIfCreated() {
		if (database == null) return;
		
		Challenges.getInstance().runAsync(() -> {
			database.connectSafely();
			database.createTableIfNotExistsSafely("challenges",
					new SQLColumn("uuid", "varchar", 36),
					new SQLColumn("stats", "varchar", 1500));
		});
	}

	public void disconnectIfConnected() {
		if (database != null) {
			Challenges.getInstance().runAsync(database::disconnectSafely);
		}
	}

	@Nullable
	private Class<? extends Database> getDatabaseForName(@Nonnull String type) {
		switch (type) {
			case "mongodb": return MongoDBDatabase.class;
			case "mysql":   return MySQLDatabase.class;
			case "sqlite":  return SQLiteDatabase.class;
			default:        return null;
		}
	}

	public boolean isConnected() {
		return database != null && database.isConnected();
	}

	public Database getDatabase() {
		return database;
	}

}
