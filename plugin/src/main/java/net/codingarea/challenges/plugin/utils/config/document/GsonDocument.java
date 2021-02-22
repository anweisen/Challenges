package net.codingarea.challenges.plugin.utils.config.document;

import com.google.gson.*;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.gson.ConfigurationSerializableTypeAdapter;
import net.codingarea.challenges.plugin.utils.config.document.gson.GsonDocumentTypeAdapter;
import net.codingarea.challenges.plugin.utils.config.document.gson.GsonTypeAdapter;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;
import net.codingarea.challenges.plugin.utils.misc.GsonUtils;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
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
			.registerTypeAdapterFactory(GsonTypeAdapter.newTypeHierarchyFactory(GsonDocument.class, new GsonDocumentTypeAdapter()))
			.registerTypeAdapterFactory(GsonTypeAdapter.newTypeHierarchyFactory(ConfigurationSerializable.class, new ConfigurationSerializableTypeAdapter()))
			.create();

	protected JsonObject jsonObject;

	public GsonDocument(@Nonnull File file) throws IOException {
		this(FileUtils.createReader(file));
	}

	public GsonDocument(@Nonnull Reader reader) throws IOException {
		this(GSON.fromJson(reader, JsonObject.class));
	}

	public GsonDocument(@Nonnull String json) {
		this(GSON.fromJson(json, JsonObject.class));
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
		return GsonUtils.convertToString(element);
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
		return GsonUtils.unpack(element);
	}

	@Nullable
	public <T> T get(@Nonnull String path, @Nonnull Class<T> classOfType) {
		JsonElement element = getElement(path).orElse(null);
		return GSON.fromJson(element, classOfType);
	}

	@Nonnull
	public <T> T get(@Nonnull String path, @Nonnull T def, @Nonnull Class<T> classOfType) {
		T value = get(path, classOfType);
		return value == null ? def : value;
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
		return GsonUtils.convertToList(array);
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		return get(path, Location.class);
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		return get(path, def, Location.class);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		return get(path, ItemStack.class);
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		return get(path, def, ItemStack.class);
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
	public Document clear() {
		jsonObject = new JsonObject();
		return this;
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		return this;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return GsonUtils.convertToMap(jsonObject);
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
			System.out.println(current);
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

		System.out.println(object);

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

	@Nonnull
	public JsonObject getJsonObject() {
		return jsonObject;
	}

}
