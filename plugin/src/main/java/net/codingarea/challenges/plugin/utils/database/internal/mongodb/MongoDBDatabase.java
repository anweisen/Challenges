package net.codingarea.challenges.plugin.utils.database.internal.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.codingarea.challenges.plugin.utils.database.*;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.abstractation.AbstractDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.deletion.MongoDBDeletion;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.insertion.MongoDBInsertion;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.insertorupdate.MongoDBInsertionOrUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.query.MongoDBQuery;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.update.MongoDBUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.where.MongoDBWhere;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.query.SQLQuery;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.update.SQLUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.SQLWhere;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MongoDBDatabase extends AbstractDatabase {

	static {
		String[] loggerNames = {
			"org.mongodb.driver.management",
			"org.mongodb.driver.connection",
			"org.mongodb.driver.cluster",
			"org.mongodb.driver.protocol.insert",
			"org.mongodb.driver.protocol.query",
			"org.mongodb.driver.protocol.update"
		};

		for (String name : loggerNames) {
			Logger logger = Logger.getLogger(name);
			if (logger == null) continue;
			logger.setLevel(Level.OFF);
		}
	}

	protected MongoClient client;
	protected MongoDatabase database;

	public MongoDBDatabase(@Nonnull DatabaseConfig config) {
		super(config);
	}

	@Override
	public void connect() throws DatabaseException {
		try {

			MongoCredential credential = MongoCredential.createCredential(config.getUser(), config.getAuthDatabase(), config.getPassword().toCharArray());
			MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
					.applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(config.getHost(), config.isPortSet() ? config.getPort() : 27017))))
					.build();

			client = MongoClients.create(settings);
			database = client.getDatabase(config.getDatabase());

		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void disconnect() throws DatabaseException {
		verifyConnection();
		try {
			client.close();
			client = null;
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void createTableIfNotExists(@Nonnull String name, @Nonnull SQLColumn... columns) throws DatabaseException {
		verifyConnection();

		boolean collectionExists = database.listCollectionNames()
				.into(new ArrayList<>())
				.contains(name);
		if (collectionExists) return;

		try {
			database.createCollection(name);
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	@Nonnull
	@Override
	public DatabaseQuery query(@Nonnull String table) {
		return new MongoDBQuery(this, table);
	}


	@Nonnull
	public DatabaseQuery query(@Nonnull String table, @Nonnull Map<String, MongoDBWhere> where) {
		return new MongoDBQuery(this, table, where);
	}

	@Nonnull
	@Override
	public DatabaseUpdate update(@Nonnull String table) {
		return new MongoDBUpdate(this, table);
	}

	@Nonnull
	@Override
	public DatabaseInsertion insert(@Nonnull String table) {
		return new MongoDBInsertion(this, table);
	}

	@Nonnull
	@Override
	public DatabaseInsertion insert(@Nonnull String table, @Nonnull Map<String, Object> values) {
		return new MongoDBInsertion(this, table, new Document(values));
	}

	@Nonnull
	public DatabaseInsertion insert(@Nonnull String table, @Nonnull Document document) {
		return new MongoDBInsertion(this, table, document);
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate insertOrUpdate(@Nonnull String table) {
		return new MongoDBInsertionOrUpdate(this, table);
	}

	@Nonnull
	@Override
	public DatabaseDeletion delete(@Nonnull String table) {
		return new MongoDBDeletion(this, table);
	}

	@Nonnull
	public MongoCollection<Document> getCollection(@Nonnull String collection) {
		return database.getCollection(collection);
	}

	@Override
	public boolean isConnected() {
		return client != null && database != null;
	}

}
