package net.codingarea.challenges.plugin.utils.database.internal.mongodb.deletion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.UpdateOptions;
import net.codingarea.challenges.plugin.utils.database.DatabaseDeletion;
import net.codingarea.challenges.plugin.utils.database.DatabaseQuery;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.MongoDBDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.where.MongoDBWhere;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.where.ObjectWhere;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.where.StringIgnoreCaseWhere;
import net.codingarea.challenges.plugin.utils.misc.BsonUtils;
import net.codingarea.challenges.plugin.utils.misc.MongoUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MongoDBDeletion implements DatabaseDeletion {

	protected final MongoDBDatabase database;
	protected final String collection;
	protected final Map<String, MongoDBWhere> where = new HashMap<>();

	public MongoDBDeletion(@Nonnull MongoDBDatabase database, @Nonnull String collection) {
		this.database = database;
		this.collection = collection;
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable Object object) {
		where.put(column, new ObjectWhere(column, object));
		return this;
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable Number value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable String value) {
		return where(column, (Object) value);
	}

	@Nonnull
	@Override
	public DatabaseDeletion where(@Nonnull String column, @Nullable String value, boolean ignoreCase) {
		if (!ignoreCase) return where(column, value);
		if (value == null) throw new NullPointerException("Cannot use where ignore case with null value");
		where.put(column, new StringIgnoreCaseWhere(column, value));
		return this;
	}

	@Override
	public void execute() throws DatabaseException {
		try {
			MongoCollection<Document> collection = database.getCollection(this.collection);

			Document filter = new Document();
			DeleteOptions options = new DeleteOptions();

			for (MongoDBWhere where : where.values()) {
				Bson whereBson = where.toBson();
				BsonDocument asBsonDocument = BsonUtils.convertBsonToBsonDocument(whereBson);
				filter.putAll(asBsonDocument);

				Collation collation = where.getCollation();
				if (collation != null)
					options.collation(collation);
			}

			collection.deleteMany(filter, options);
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

}
