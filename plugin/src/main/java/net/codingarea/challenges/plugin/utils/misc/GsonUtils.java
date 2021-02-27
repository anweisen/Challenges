package net.codingarea.challenges.plugin.utils.misc;

import com.google.gson.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class GsonUtils {

	private GsonUtils() {
	}

	@Nullable
	public static Object unpack(@Nullable JsonElement element) {
		if (element == null || element.isJsonNull())
			return null;
		if (element.isJsonObject())
			return convertToMap(element.getAsJsonObject());
		if (element.isJsonArray())
			return convertToList(element.getAsJsonArray());
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isNumber()) return primitive.getAsNumber();
			if (primitive.isString()) return primitive.getAsString();
			if (primitive.isBoolean()) return primitive.getAsBoolean();
		}
		return element;
	}

	@Nullable
	public static String convertToString(@Nullable JsonElement element) {
		if (element == null || element.isJsonNull())
			return null;
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) return primitive.getAsString();
			if (primitive.isNumber()) return primitive.getAsNumber() + "";
			if (primitive.isBoolean()) return primitive.getAsBoolean() + "";
		}
		return element.toString();
	}

	@Nonnull
	public static Map<String, Object> convertToMap(@Nonnull JsonObject object) {
		Map<String, Object> map = new LinkedHashMap<>();
		convertToMap(object, map);
		return map;
	}

	public static void convertToMap(@Nonnull JsonObject object, @Nonnull Map<String, Object> map) {
		for (Entry<String, JsonElement> entry : object.entrySet()) {
			map.put(entry.getKey(), unpack(entry.getValue()));
		}
	}

	@Nonnull
	public static List<String> convertToList(@Nonnull JsonArray array) {
		List<String> list = new ArrayList<>(array.size());
		for (JsonElement element : array) {
			list.add(convertToString(element));
		}
		return list;
	}

	@Nonnull
	public static String[] convertToStringArray(@Nonnull JsonArray array) {
		String[] list = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			list[i] = convertToString(array.get(i));
		}
		return list;
	}

	@Nonnull
	public static JsonArray convertIterableToJsonArray(@Nonnull Gson gson, @Nonnull Iterable<?> iterable) {
		JsonArray array = new JsonArray();
		iterable.forEach(object -> array.add(gson.toJsonTree(object)));
		return array;
	}

	@Nonnull
	public static JsonArray convertArrayToJsonArray(@Nonnull Gson gson, @Nonnull Object array) {
		JsonArray jsonArray = new JsonArray();
		ReflectionUtils.forEachInArray(array, object -> jsonArray.add(gson.toJsonTree(object)));
		return jsonArray;
	}

	public static void setDocumentProperties(@Nonnull Gson gson, @Nonnull JsonObject object, @Nonnull Map<String, Object> values) {
		for (Entry<String, Object> entry : values.entrySet()) {
			Object value = entry.getValue();

			if (value == null) {
				object.add(entry.getKey(), null);
			} else if (value instanceof JsonElement) {
				object.add(entry.getKey(), (JsonElement) value);
			} else if (value instanceof Iterable) {
				Iterable<?> iterable = (Iterable<?>) value;
				object.add(entry.getKey(), convertIterableToJsonArray(gson, iterable));
			} else if (value.getClass().isArray()) {
				object.add(entry.getKey(), convertArrayToJsonArray(gson, value));
			} else if (value instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) value;
				JsonObject newObject = new JsonObject();
				object.add(entry.getKey(), newObject);
				setDocumentProperties(gson, newObject, map);
			} else {
				object.add(entry.getKey(), gson.toJsonTree(value));
			}
		}
	}

}
