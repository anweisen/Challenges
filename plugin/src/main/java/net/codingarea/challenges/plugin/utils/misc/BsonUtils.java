package net.codingarea.challenges.plugin.utils.misc;

import com.mongodb.MongoClient;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class BsonUtils {

	private BsonUtils() {
	}

	@Nonnull
	public static BsonDocument convertBsonToBsonDocument(@Nonnull Bson bson) {
		return bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
	}

	@Nonnull
	public static BsonValue convertToBsonElement(@Nullable Object value) {
		if (value == null) {
			return BsonNull.VALUE;
		} else if (value instanceof BsonValue) {
			return (BsonValue) value;
		} else if (value instanceof Bson) {
			return convertBsonToBsonDocument((Bson) value);
		} else if (value instanceof String) {
			return new BsonString((String) value);
		} else if (value instanceof Boolean) {
			return new BsonBoolean((Boolean) value);
		} else if (value instanceof ObjectId) {
			return new BsonObjectId((ObjectId) value);
		} else if (value instanceof Double) {
			return new BsonDouble((Double) value);
		} else if (value instanceof Float) {
			return new BsonDouble((Float) value);
		} else if (value instanceof Long) {
			return new BsonInt64((Long) value);
		} else if (value instanceof Integer) {
			return new BsonInt32((Integer) value);
		} else if (value instanceof Number) {
			return new BsonInt32(((Number) value).intValue());
		} else if (value instanceof byte[]) {
			return new BsonBinary((byte[]) value);
		} else if (value instanceof Iterable) {
			return convertIterableToJsonArray((Iterable<?>) value);
		} else if (value.getClass().isArray()) {
			return convertArrayToJsonArray(value);
		} else if (value instanceof Map) {
			BsonDocument document = new BsonDocument();
			setDocumentProperties(document, (Map<String, Object>) value);
			return document;
		} else {
			Logger.severe("Could not serialize " + value.getClass().getName() + " to BsonValue");
			return BsonNull.VALUE;
		}
	}

	public static void setDocumentProperties(@Nonnull BsonDocument document, @Nonnull Map<String, Object> values) {
		for (Entry<String, Object> entry : values.entrySet()) {
			Object value = entry.getValue();
			document.put(entry.getKey(), convertToBsonElement(value));
		}
	}

	@Nonnull
	public static BsonArray convertIterableToJsonArray(@Nonnull Iterable<?> iterable) {
		BsonArray bsonArray = new BsonArray();
		iterable.forEach(object -> bsonArray.add(convertToBsonElement(object)));
		return bsonArray;
	}

	@Nonnull
	public static BsonArray convertArrayToJsonArray(@Nonnull Object array) {
		BsonArray bsonArray = new BsonArray();
		ReflectionUtils.forEachInArray(array, object -> bsonArray.add(convertToBsonElement(object)));
		return bsonArray;
	}

}
