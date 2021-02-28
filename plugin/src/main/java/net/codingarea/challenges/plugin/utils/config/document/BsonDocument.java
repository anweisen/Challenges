package net.codingarea.challenges.plugin.utils.config.document;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class BsonDocument implements Document {

	protected org.bson.Document bsonDocument;

	public BsonDocument(@Nonnull File file) throws IOException {
		this(FileUtils.newBufferedReader(file));
	}

	public BsonDocument(@Nonnull Reader reader) {
		BufferedReader buffered = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
		StringBuilder content = new StringBuilder();
		buffered.lines().forEach(content::append);
		bsonDocument = org.bson.Document.parse(content.toString());
	}

	public BsonDocument(@Nonnull org.bson.Document bsonDocument) {
		this.bsonDocument = bsonDocument;
	}

	public BsonDocument() {
		this(new org.bson.Document());
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		return new BsonDocument(bsonDocument.get(path, org.bson.Document.class));
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return bsonDocument.getString(path);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		String value = getString(path);
		return value == null ? def : value;
	}

	@Override
	public long getLong(@Nonnull String path) {
		return getLong(path, 0);
	}

	@Override
	public long getLong(@Nonnull String path, long def) {
		Long value = bsonDocument.getLong(path);
		if (value == null) return def;
		return value;
	}

	@Override
	public int getInt(@Nonnull String path) {
		return getInt(path, 0);
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		Integer value = bsonDocument.getInteger(path);
		if (value == null) return def;
		return value;
	}

	@Override
	public short getShort(@Nonnull String path) {
		return getShort(path, (short) 0);
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		Number value = bsonDocument.getInteger(path);
		if (value == null) return def;
		return value.shortValue();
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return getByte(path, (byte) 0);
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		Number value = bsonDocument.getInteger(path);
		if (value == null) return def;
		return value.byteValue();
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return getDouble(path, 0);
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		Double value = bsonDocument.getDouble(path);
		if (value == null) return def;
		return value;
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return getFloat(path, 0);
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		Double value = bsonDocument.getDouble(path);
		if (value == null) return def;
		return (float) (double) value;
	}

	@Override
	public char getChar(@Nonnull String path) {
		return getChar(path, (char) 0);
	}

	@Override
	public char getChar(@Nonnull String path, char def) {
		try {
			return getString(path).charAt(0);
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return getBoolean(path, false);
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		return bsonDocument.getBoolean(path, def);
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return bsonDocument.get(path);
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		return bsonDocument.getList(path, String.class);
	}

	@Nullable
	@Override
	public UUID getUUID(@Nonnull String path) {
		return bsonDocument.get(path, UUID.class);
	}

	@Nonnull
	@Override
	public UUID getUUID(@Nonnull String path, @Nonnull UUID def) {
		return bsonDocument.get(path, def);
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		try {
			return Enum.valueOf(classOfEnum, getString(path));
		} catch (Exception ex) {
			return null;
		}
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		E value = getEnum(path, (Class<E>) def.getClass());
		return value == null ? def : value;
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		if (contains(path)) return null;
		return Location.deserialize(bsonDocument.get(path, org.bson.Document.class));
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		Location value = getLocation(path);
		return value == null ? def : value;
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		if (contains(path)) return null;
		return ItemStack.deserialize(bsonDocument.get(path, org.bson.Document.class));
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		ItemStack value = getItemStack(path);
		return value == null ? def : value;
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return bsonDocument.containsKey(path);
	}

	@Override
	public boolean isEmpty() {
		return bsonDocument.isEmpty();
	}

	@Nonnull
	@Override
	public Document clear() {
		bsonDocument.clear();
		return this;
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		bsonDocument.put(path, value);
		return this;
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		bsonDocument.remove(path);
		return this;
	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		String json = bsonDocument.toString();
		writer.write(json);
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return Collections.unmodifiableMap(bsonDocument);
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return bsonDocument.keySet();
	}

	@Nonnull
	@Override
	public String toJson() {
		return bsonDocument.toJson();
	}

	@Override
	public String toString() {
		return toJson();
	}

	@Override
	public boolean isReadonly() {
		return false;
	}

}
