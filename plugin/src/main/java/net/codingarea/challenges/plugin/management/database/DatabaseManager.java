package net.codingarea.challenges.plugin.management.database;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.database.Database;
import net.anweisen.utilities.database.DatabaseConfig;
import net.anweisen.utilities.database.SQLColumn;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
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
		if ("mongodb".equals(type) && !checkDependencies("com.mongodb.client.MongoClient")) {
			ConsolePrint.noMongoDependencies();
			return;
		}

		try {
			String nameOfClass = getDatabaseForName(type);
			if (nameOfClass == null) {
				Logger.error("Selected illegal database type '{}'", type);
				return;
			}
			Class<? extends Database> classOfDatabase = (Class<? extends Database>) Class.forName(nameOfClass);

			DatabaseConfig config = new DatabaseConfig(document.getDocument(type));
			Constructor<? extends Database> constructor = classOfDatabase.getDeclaredConstructor(DatabaseConfig.class);
			database = constructor.newInstance(config);
		} catch (Throwable ex) {
			Logger.error("Could not create database", ex);
		}
	}

	public void connectIfCreated() {
		if (database == null) return;
		
		Challenges.getInstance().runAsync(() -> {
			database.connectSafely();
			database.createTableIfNotExistsSafely("challenges",
					new SQLColumn("uuid", "varchar", 36),
					new SQLColumn("name", "varchar", 16),
					new SQLColumn("textures", "varchar", 265),
					new SQLColumn("stats", "varchar", 1500),
					new SQLColumn("config", "varchar", 7500)
			);
		});
	}

	public void disconnectIfConnected() {
		if (database != null) {
			Challenges.getInstance().runAsync(database::disconnectSafely);
		}
	}

	@Nullable
	private String getDatabaseForName(@Nonnull String type) {
		switch (type) {
			case "mongodb": return "net.anweisen.utilities.database.internal.mongodb.MongoDBDatabase";
			case "mysql":   return "net.anweisen.utilities.database.internal.sql.mysqlMySQLDatabase";
			case "sqlite":  return "net.anweisen.utilities.database.internal.sql.sqliteSQLiteDatabase";
			default:        return null;
		}
	}

	public boolean isConnected() {
		return database != null && database.isConnected();
	}

	public boolean isEnabled() {
		return database != null;
	}

	public Database getDatabase() {
		return database;
	}

	private boolean checkDependencies(@Nonnull String... classes) {
		try {
			for (String name : classes) {
				Class.forName(name);
			}
		} catch (Throwable ex) {
			return false;
		}
		return true;
	}

}
