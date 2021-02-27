package net.codingarea.challenges.plugin.utils.database.internal.mongodb.insertion;

import net.codingarea.challenges.plugin.utils.database.DatabaseInsertion;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.MongoDBDatabase;
import net.codingarea.challenges.plugin.utils.misc.MongoUtils;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MongoDBInsertion implements DatabaseInsertion {

	protected final MongoDBDatabase database;
	protected final String collection;
	protected final Document values;

	public MongoDBInsertion(@Nonnull MongoDBDatabase database, @Nonnull String collection) {
		this.database = database;
		this.collection = collection;
		this.values = new Document();
	}

	public MongoDBInsertion(@Nonnull MongoDBDatabase database, @Nonnull String collection, @Nonnull Document values) {
		this.database = database;
		this.collection = collection;
		this.values = values;
	}

	@Nonnull
	@Override
	public DatabaseInsertion set(@Nonnull String field, @Nullable Object value) {
		values.put(field, MongoUtils.packObject(value));
		return this;
	}

	@Override
	public void execute() throws DatabaseException {
		try {
			database.getCollection(collection).insertOne(values);
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

}
