package net.codingarea.challenges.plugin.utils.config.document;

import com.google.gson.*;
import net.codingarea.challenges.plugin.utils.config.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class GsonDocument implements Document {

	public static final Gson GSON = new GsonBuilder()
			.serializeNulls()
			.disableHtmlEscaping()
			.setPrettyPrinting()
//			.registerTypeAdapterFactory(TypeAdapters.newTypeHierarchyFactory(JsonDocument.class, new JsonDocumentTypeAdapter()))
			.create();

	protected final JsonObject jsonObject;

	public GsonDocument(@Nonnull String value) {
		this(new JsonParser().parse(value).getAsJsonObject());
	}

	public GsonDocument(@Nonnull JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public GsonDocument() {
		this(new JsonObject());
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		JsonElement element = getElement(path).orElse(null);
		return toString(element);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		String string = getString(path);
		return string == null ? def : string;
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		JsonElement element = getElement(path).orElse(null);
		if (element == null || element.isJsonNull()) return null;
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				return primitive.getAsNumber();
			} else if (primitive.isBoolean()) {
				return primitive.getAsBoolean();
			} else if (primitive.isString()) {
				return primitive.getAsString();
			}
		} else if (element.isJsonObject()) {
			return element.getAsJsonObject();
		} else if (element.isJsonArray()) {
			return element.getAsJsonArray();
		}
		return element;
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		JsonElement element = getElement(path).orElse(null);
		JsonObject object = element == null ? new JsonObject() : element.getAsJsonObject();
		if (element == null) setElement(path, object);
		return new GsonDocument(object);
	}

	@Override
	public long getLong(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsLong();
	}

	@Override
	public int getInt(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsInt();
	}

	@Override
	public short getShort(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsShort();
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsByte();
	}

	@Override
	public char getChar(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsCharacter();
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsDouble();
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(0)).getAsFloat();
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return getElement(path).orElse(new JsonPrimitive(false)).getAsBoolean();
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		JsonArray array = jsonObject.getAsJsonArray(path);
		List<String> list = new ArrayList<>(array.size());
		for (JsonElement element : array) {
			list.add(toString(element));
		}
		return list;
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return getElement(path).isPresent();
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		setElement(path, value);
		return this;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		Map<String, Object> map = new LinkedHashMap<>();
		jsonObject.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
		return map;
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		Collection<String> keys = new ArrayList<>(jsonObject.size());
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			keys.add(entry.getKey());
		}
		return keys;
	}

	@Nonnull
	private Optional<JsonElement> getElement(@Nonnull String path) {
		return getElement(path, jsonObject);
	}

	@Nonnull
	private Optional<JsonElement> getElement(@Nonnull String path, @Nonnull JsonObject object) {

		JsonElement fullPathElement = object.get(path);
		if (fullPathElement != null) return Optional.of(fullPathElement);

		int index = path.indexOf('.');
		if (index == -1) return Optional.empty();

		String child = path.substring(0, index);
		String newPath = path.substring(index + 1);

		JsonElement element = object.get(child);
		if (element == null) return Optional.empty();

		return getElement(newPath, element.getAsJsonObject());

	}

	private void setElement(@Nonnull String path, @Nullable Object value) {

		LinkedList<String> paths = determinePath(path);
		JsonObject object = jsonObject;

		for (int i = 0; i < paths.size() - 1; i++) {

			String current = paths.get(i);
			JsonElement element = object.get(current);
			if (element == null || element.isJsonNull()) {
				if (value == null) return; // There's noting to remove
				object.add(current, element = new JsonObject());
			}

			if (!element.isJsonObject()) throw new IllegalArgumentException("Cannot replace '" + current + "' on '" + path + "'; It's not an object");
			object = element.getAsJsonObject();

		}

		String lastPath = paths.getLast();
		object.add(lastPath, GSON.toJsonTree(value));

	}

	@Nonnull
	private LinkedList<String> determinePath(@Nonnull String path) {

		LinkedList<String> paths = new LinkedList<>();
		String pathCopy = path;
		int index;
		while ((index = pathCopy.indexOf('.')) != -1) {
			String child = pathCopy.substring(0, index);
			pathCopy = pathCopy.substring(index + 1);
			paths.add(child);
		}
		paths.add(pathCopy);

		return paths;

	}

	@Nullable
	private String toString(@Nullable JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return null;
		} else if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) {
				return primitive.getAsString();
			} else if (primitive.isNumber()) {
				return primitive.getAsNumber().toString();
			} else if (primitive.isBoolean()) {
				return primitive.getAsBoolean() + "";
			} else {
				return primitive.toString();
			}
		} else {
			return element.toString();
		}
	}

	@Override
	public String toString() {
		return jsonObject.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GsonDocument that = (GsonDocument) o;
		return jsonObject.equals(that.jsonObject);
	}

	@Override
	public int hashCode() {
		return jsonObject.hashCode();
	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		GSON.toJson(jsonObject, writer);
	}

}
