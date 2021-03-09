package net.codingarea.challenges.plugin.utils.misc;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.Sorts;
import net.codingarea.challenges.plugin.utils.config.Json;
import net.codingarea.challenges.plugin.utils.database.Order;
import net.codingarea.challenges.plugin.utils.database.internal.mongodb.where.MongoDBWhere;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MongoUtils {

	private MongoUtils() {
	}

	public static void applyWhere(@Nonnull FindIterable<Document> iterable, @Nonnull Map<String, MongoDBWhere> where) {
		for (Entry<String, MongoDBWhere> entry : where.entrySet()) {
			MongoDBWhere value = entry.getValue();
			iterable.filter(value.toBson());

			Collation collation = value.getCollation();
			if (collation != null)
				iterable.collation(collation);
		}
	}

	public static void applyOrder(@Nonnull FindIterable<Document> iterable, @Nullable String orderBy, @Nullable Order order) {
		if (order == null || orderBy == null) return;
		switch (order) {
			case HIGHEST:
				iterable.sort(Sorts.descending(orderBy));
				break;
			case LOWEST:
				iterable.sort(Sorts.ascending(orderBy));
				break;
		}
	}

	@Nullable
	public static Object packObject(@Nullable Object value) {
		if (value instanceof Json) {
			String json = ((Json) value).toJson();
			return Document.parse(json);
		} else if (value instanceof ConfigurationSerializable) {
			ConfigurationSerializable serializable = (ConfigurationSerializable) value;
			Map<String, Object> values = serializable.serialize();
			BsonDocument bson = new BsonDocument();
			BsonUtils.setDocumentProperties(bson, values);
			return bson;
		} else if (value instanceof UUID) {
			return ((UUID) value).toString();
		} else {
			return value;
		}
	}

}
